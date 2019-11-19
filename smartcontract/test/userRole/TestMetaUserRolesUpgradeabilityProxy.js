const MetaUserRoles = artifacts.require("MetaUserRoles.sol");

const MetaUserRolesBehaviour = require('./MetaUserRolesBehaviour');

const MetaUserRolesUpgrade = artifacts.require("MetaUserRolesMock.sol");

const lightwallet = require('eth-lightwallet');
const evm_increaseTime = require('../utilslib/evmIncreaseTime.js')
const TxRelay = artifacts.require("./TxRelay.sol");
const helpers1 = require("../utilslib/helpers1.js");
const Promise = require('bluebird')
const compareCode = require('../utilslib/compareCode')
const assertThrown = require('../utilslib/assertThrown')
const solsha3 = require('solidity-sha3').default
const leftPad = require('left-pad')

const MetaUserRolesUpgradeProxy = artifacts.require("OwnedUpgradeabilityProxy");

let ethFounderBalance = 0

let zeroAddress = "0x0000000000000000000000000000000000000000"



const truffleAssert =require("truffle-assertions");

const assertRevert = require("../utilslib/assertRevert");

const encodeCall = require('../utilslib/encodeCall');

const utils = require("../UtilsLib/utils");

contract ('Meta User Role Proxy upgradeable role', function (accounts) {

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
      let acct, implMetaUserRole, implMetaUserRoleUpgrade, metaUserRolesUpgrade;
        let superAdmin, operationAdmin,directors, doctors, assistants,initializeData, devOp;


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

                  console.log(acct);
                  superAdmin = web3.utils.toChecksumAddress(acct[2]);
                  operationAdmin = web3.utils.toChecksumAddress(acct[3])
                  doctors = [web3.utils.toChecksumAddress(acct[4]),web3.utils.toChecksumAddress(acct[5])]
                  assistants =[web3.utils.toChecksumAddress(acct[6]),web3.utils.toChecksumAddress(acct[7])]
                  directors = [web3.utils.toChecksumAddress(acct[10]),web3.utils.toChecksumAddress(acct[11])]
                  devOp = accounts[2];
                  console.log("devop " + devOp);

                  creator = accounts[0];


                   errorThrown = false

                   txRelay = await TxRelay.new({from: creator})

                   metaUserRoles = await MetaUserRoles.new({from:creator});

                   proxy = await MetaUserRolesUpgradeProxy.new(devOp,{from: creator});


                       var owner1 = await proxy.proxyOwner();

                                          console.log("owner1 " + owner1 + "----" + creator + "----" + txRelay.address );

                   initializeData = encodeCall('initialize', ['address','address', 'address'], [superAdmin,operationAdmin, txRelay.address]);

                   metaUserRolesUpgrade = await MetaUserRolesUpgrade.new({from:creator});

                    proxy.upgradeToAndCall(metaUserRoles.address, initializeData,{from:devOp});

                     var owner = await proxy.proxyOwner();

                     console.log("owner " + owner);

                   implMetaUserRole = await MetaUserRoles.at(proxy.address);
                   implMetaUserRoleUpgrade = await MetaUserRolesUpgrade.at(proxy.address);

                   var relayerResult = await implMetaUserRole._relay.call();

                   assert.equal(txRelay.address, relayerResult,"relayer address is incorrect");
                    //var owner2 = await proxy.proxyOwner();

                            //            console.log("owner2 " + owner2);

                  done()
               })
             })


         })



      describe("should have the same MetaUserRoles Behaviour", () => {
           before(async function (){
                 this.metaUserRoles = await implMetaUserRole;
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

                 var resultSuperAdmin = await this.metaUserRoles.getSuperAdmin.call();

                console.log("super "   + superAdmin + " op " + operationAdmin + "result " + resultSuperAdmin );

                 (await this.metaUserRoles.getSuperAdmin()).should.be.equal(superAdmin);
                 (await this.metaUserRoles.getOperationAdmin()).should.be.equal(operationAdmin);

          });

       //  MetaUserRolesBehaviour(accounts);

      })


      describe("when there was another upgrade", function() {


             before(async function (){
                             this.metaUserRoles = await implMetaUserRole;
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

              describe("failed to upgrade and call", async function() {
                  it("failed to upgrade and initiate the upgrade again. upgradeToAndCall only do onetime", async function() {
                     truffleAssert.reverts(proxy.upgradeToAndCall(metaUserRolesUpgrade.address, initializeData, {from:devOp}));
                   })
              })

             /* describe("upgrade to another address", function() {
                    before(async() => {
                          (await proxy.proxyOwner()).should.be.equal(devOp);
                          await proxy.upgradeTo(metaUserRolesUpgrade.address, {from: devOp});

                    })

                     it("delegate calls to the last upgrade implementation", async function() {
                         (await proxy.implementation()).should.be.equal(metaUserRolesUpgrade.address);
                      })

                    it("the new upgrade have behaviour and data was the previous call", async function() {
                        this.metaUserRoles = await implMetaUserRolesUpgrade;




                       var version = await implMetaEverestRoleUpgrade.getVersion();
                       assert.equal(version.valueOf(), 0, "Version is incorrect");
                       var superAdminResult = await implMetaEverestRoleUpgrade.getSuperAdminMock();
                       assert.equal(superAdmin,superAdmin, "superAdmin is incorrect");
                    })

                    //MetaUserRolesBehaviour(accounts);
              })*/


         })


});