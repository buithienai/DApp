
const MetaUserRoles = artifacts.require("MetaUserRoles.sol");

const MetaUserRolesBehaviour = require('./MetaUserRolesBehaviour');

const lightwallet = require('eth-lightwallet');
const evm_increaseTime = require('../utilslib/evmIncreaseTime.js')
const TxRelay = artifacts.require("./TxRelay.sol");
const helpers1 = require("../utilslib/helpers1.js");
const Promise = require('bluebird')
const compareCode = require('../utilslib/compareCode')
const assertThrown = require('../utilslib/assertThrown')
const solsha3 = require('solidity-sha3').default
const leftPad = require('left-pad')

let ethFounderBalance = 0

let zeroAddress = "0x0000000000000000000000000000000000000000"

contract ('Meta User Roles', function (accounts) {

     let proxy
      let deployedProxy
      let txRelay
      let lw
      let keyFromPw
      let data
      let types
      let params
      let newData
      let res
      let p
      let nobody, notSender,regularUser
      let errorThrown = false;
      let creator, metaUserRoles;
      let acct,  superAdmin, operationAdmin, directors, doctors, assistants;


  before((done) =>{
              let seed = "pull rent tower word science patrol economy legal yellow kit frequent fat"

               lightwallet.keystore.createVault(
                  {hdPathString: "m/44'/60'/0'/0",
                   seedPhrase: seed,
                   password: "test",
                   salt: "testsalt"
                  },

              function (err, keystore) {

                lw = keystore
                lw.keyFromPassword("test", async function(e,k) {
                  keyFromPw = k

                  lw.generateNewAddress(keyFromPw, 20)
                  acct = lw.getAddresses()


                  superAdmin = web3.utils.toChecksumAddress(acct[2]);
                  operationAdmin = web3.utils.toChecksumAddress(acct[3])
                  doctors = [web3.utils.toChecksumAddress(acct[4]),web3.utils.toChecksumAddress(acct[5])]
                  assistants =[web3.utils.toChecksumAddress(acct[6]),web3.utils.toChecksumAddress(acct[7])]
                  directors = [web3.utils.toChecksumAddress(acct[10]),web3.utils.toChecksumAddress(acct[11])]

                  creator = accounts[0];


                   errorThrown = false

                   txRelay = await TxRelay.new({from: creator})

                   metaUserRoles = await MetaUserRoles.new({from:creator});
                   ///console.log("address " , superAdmin, operationAdmin, txRelay.address);
                   await metaUserRoles.initialize(superAdmin, operationAdmin,txRelay.address, {from:creator});



                   var relayerResult = await metaUserRoles._relay.call();

                   assert.equal(txRelay.address, relayerResult,"relayer address is incorrect");

                  done()
               })
             })


         })



      describe("test", () => {
           before(async function (){
                 this.metaUserRoles = await metaUserRoles;
                 this.txRelay = await txRelay;
                 this.acct = acct;
                 this.lw = lw;
                 this.keyFromPw = keyFromPw;
                 this.superAdmin = superAdmin;
                 this.operationAdmin = operationAdmin;
                 this.directors = directors;
                 this.doctors = doctors;
                 this.assistants = assistants;

           })

          it('test setup ', async function() {

                 var resultSuperAdmin = await metaUserRoles.getSuperAdmin.call();

                console.log("super "   + superAdmin + " op " + operationAdmin + "result " + resultSuperAdmin );

                 (await metaUserRoles.getSuperAdmin()).should.be.equal(superAdmin);
                 (await this.metaUserRoles.getOperationAdmin()).should.be.equal(operationAdmin);
          });




         MetaUserRolesBehaviour(accounts);

      })















});