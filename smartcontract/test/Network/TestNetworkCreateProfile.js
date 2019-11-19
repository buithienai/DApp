
const utils = require("../UtilsLib/utils");

const MetaKLNetwork =artifacts.require("./MetaKLNetwork.sol");
const MetaUserRoles = artifacts.require("./MetaUserRoles.sol");
const PatientProfile = artifacts.require("PatientProfile.sol");

const BigNumber = require('bignumber.js');

const web3 = MetaKLNetwork.web3;


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




const deployToken = () => {
    return TestToken.new()
}

const precisionUints = (new BigNumber(10).pow(12));

contract("Integration test", function (accounts) {
        const creator = accounts[0];



        const superAdmin = accounts[8];
        const operationAdmin = accounts[9];

        const bridge = accounts[8];

        const level = 6;

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
          let p, tx;
          let errorThrown = false;
          let metaUserRoles,metaKLNetwork;
          let acct, directors, doctors, assistants, director, doctor, assistant;
          let directorIndex = 1, doctorIndex = 2, assistantIndex=3;


    before((done) => {
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


             // superAdmin = web3.utils.toChecksumAddress(acct[2]);
              //operationAdmin = web3.utils.toChecksumAddress(acct[3])
              doctors = [web3.utils.toChecksumAddress(acct[4]),web3.utils.toChecksumAddress(acct[5])]
              assistants =[web3.utils.toChecksumAddress(acct[6]),web3.utils.toChecksumAddress(acct[7])]
              directors = [web3.utils.toChecksumAddress(acct[10]),web3.utils.toChecksumAddress(acct[11])]
              doctor = doctors[0];
              assistant = assistants[0];
              director = directors[0];



               errorThrown = false

               txRelay = await TxRelay.new({from: creator})



           console.log("-----Create create users roles and add roles directors, doctors and assistants------");

             /// create User Roles
             metaUserRoles = await MetaUserRoles.new({from:creator});
             await metaUserRoles.initialize(superAdmin, operationAdmin, txRelay.address, {from:creator});
             assert.ok(metaUserRoles);

             await metaUserRoles.submitAddAccount(operationAdmin, directorIndex, directors, {from:operationAdmin});
             await metaUserRoles.approveAddAccount(superAdmin,directorIndex, directors, {from: superAdmin});

              await metaUserRoles.submitAddAccount(operationAdmin, doctorIndex, doctors, {from:operationAdmin});
              await metaUserRoles.approveAddAccount(superAdmin,doctorIndex, doctors, {from: superAdmin});


             await metaUserRoles.submitAddAccount(operationAdmin, assistantIndex, assistants, {from:operationAdmin});
             await metaUserRoles.approveAddAccount(superAdmin,assistantIndex, assistants, {from: superAdmin});




            console.log("----set up KLNetwork-------------------------------------------------------------");

            metaKLNetwork = await MetaKLNetwork.new({from:creator});
            await metaKLNetwork.initialize(metaUserRoles.address, level, txRelay.address, {from:creator});

            var networkLevel = await metaKLNetwork._level.call();
            assert(networkLevel,level,"level is incorrect");
              done()
           })
         })


     })



    it(" create voteProfile and vote", async function() {
       const ipfsUserProfileData =  web3.utils.fromAscii("QmNYf5B9cpm3ZQerpbRsKnWqxpdGcpMDzkn4JRv6CW8un2");
       types = ['address', 'uint8' , 'bytes32'];
        params = [director, directorIndex,ipfsUserProfileData];

         p = await helpers1.signPayload(director,txRelay, zeroAddress, metaKLNetwork.address,"createVoteProfile",
                                   types,params,lw, keyFromPw, true);

        await txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

        var profileCount = await metaKLNetwork._profileCount.call();
        //(await metaKLNetwork._voteNumber.call(profileCount)).should.be.equal(5);


         types = ['address', 'uint8' , 'uint256'];
        params = [assistant, assistantIndex,profileCount];

         p = await helpers1.signPayload(assistant,txRelay, zeroAddress, metaKLNetwork.address,"voteProfile",
                                   types,params,lw, keyFromPw, true);

        await txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});
        profileCount = await metaKLNetwork._profileCount.call();
       // (await metaKLNetwork._voteNumber.call(profileCount)).should.be.equal(new BigNumber(7));

        var createdProfileAddress = await metaKLNetwork.registeredProfile.call(profileCount);

        var patientProfile = await PatientProfile.at(createdProfileAddress);

        (await patientProfile.getCreator.call()).should.be.equal(metaKLNetwork.address);

       /// (await patientProfile.getPatientProfile.call()).should.be.equal(ipfsUserProfileData);


    })





})