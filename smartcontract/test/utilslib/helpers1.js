const lightwallet = require('eth-lightwallet')
const evm_increaseTime = require('./evmIncreaseTime.js')
const MetaTxRelay = artifacts.require('TxRelay')
const MetaIdentityManager = artifacts.require('MetaIdentityManager')
const Proxy = artifacts.require('Proxy')
const TestRegistry = artifacts.require('TestRegistry')
const MetaTestRegistry = artifacts.require('MetaTestRegistry')
const Promise = require('bluebird')
const compareCode = require('./compareCode')
const assertThrown = require('./assertThrown')
const solsha3 = require('solidity-sha3').default
const leftPad = require('left-pad')


const utils = require('ethereumjs-util');


///const web3 = require('web3');

const LOG_NUMBER_1 = 1234
const LOG_NUMBER_2 = 2345

const userTimeLock = 50;
const adminTimeLock = 200;
const adminRate = 50;


function enc(funName, types, params) {
  return  lightwallet.txutils._encodeFunctionTxData(funName, types, params)
}



//Returns random number in [1, 99]
function getRandomNumber() { //Thanks Oed :~)
  return Math.floor(Math.random() * (100 - 1)) + 1;
}

//Left packs a (hex) string. Should probably use leftpad
function pad(n) {
  assert.equal(typeof(n), 'string', "Passed in a non string")
  let data
  if (n.startsWith("0x")) {
    data = '0x' + leftPad(n.slice(2), '64', '0')
    assert.equal(data.length, 66, "packed incorrectly")
    return data;
  } else {
    data = '0x' + leftPad(n, '64', '0')
    assert.equal(data.length, 66, "packed incorrectly")
    return data;
  }
}

async function signPayload(signingAddr, txRelay, whitelistOwner, destinationAddress, functionName,
                     functionTypes, functionParams, lw, keyFromPw, isHardwareWallet)
{
   if (functionTypes.length !== functionParams.length) {
     return //should throw error
   }
   if (typeof(functionName) !== 'string') {
     return //should throw error
   }
   let nonce
   let blockTimeout
   let data
   let hashInput
   let hash
   let sig
   let retVal = {}
   let secondHash,hashHexmessage;
   data = enc(functionName, functionTypes, functionParams)

   nonce = await txRelay.getNonce.call(signingAddr)
   //Tight packing, as Solidity sha3 does
    hashInput = '0x1900' + txRelay.address.slice(2) + whitelistOwner.slice(2) + pad(nonce.toString('hex')).slice(2)
                    + destinationAddress.slice(2) + data.slice(2);

   hash = solsha3(hashInput);
  if(isHardwareWallet) {
     hash = hash.replace("0x","");
     secondHash = utils.keccak256(Buffer.from(`\x19Ethereum Signed Message:\n`+(hash.length) + hash, 32));
     sig = lightwallet.signing.signMsgHash(lw, keyFromPw, secondHash.toString('hex'), signingAddr)
  } else {
      sig = lightwallet.signing.signMsgHash(lw, keyFromPw, hash, signingAddr)
  }



   retVal.r = '0x'+sig.r.toString('hex')
   retVal.s = '0x'+sig.s.toString('hex')
   retVal.v = sig.v //Q: Why is this not converted to hex?
   retVal.data = data
   retVal.hash = secondHash
   retVal.nonce = nonce
   retVal.dest = destinationAddress
   return retVal
}


async function checkLogs(tx, eventName, indexAddOne, indexAddTwo, notIndexAdd) {
  const log = tx.receipt.logs[0]
  assert.equal(log.topics[0], solsha3(eventName + "(address,address,address)"), "Wrong event")
  assert.equal(log.topics[1], pad(indexAddOne), "Wrong topic one")
  assert.equal(log.topics[2], pad(indexAddTwo), "Wrong topic two")
  assert.equal(log.data, pad(notIndexAdd), "Wrong initiator")
}

Object.assign(exports, {
    enc,
    getRandomNumber,
     pad,
     signPayload,
     checkLogs
})