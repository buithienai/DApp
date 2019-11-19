function MetaUserRolesBehaviour(accounts) {
const helpers1 = require('../utilslib/helpers1.js');

const creator = accounts[0];
  let newAdmin;
  let types ;
         let params;
         let p;


let zeroAddress = "0x0000000000000000000000000000000000000000";
let directorIndex = 1, doctorIndex = 2, assistantIndex=3;


let lw, keyFromPw, operationAdmin, superAdmin, directors, doctors, assistants ;

      before ( async function() {
         operationAdmin = this.operationAdmin;
         superAdmin = this.superAdmin;
         directors = this.directors;
         doctors = this.doctors;
         assistants = this.assistants;
         lw = this.lw;
         keyFromPw = this.keyFromPw;

      }
  )

  it('test setup ', async function() {
        console.log("operation admin" + operationAdmin);
        console.log(" super admin" + superAdmin);

       (await this.metaUserRoles.getSuperAdmin()).should.be.equal(superAdmin);
       (await this.metaUserRoles.getOperationAdmin()).should.be.equal(operationAdmin);
   });









    it ('update superAdmin by superAdmin', async function() {
          let newSuperAdmin = web3.utils.toChecksumAddress(this.acct[15]);
         types = ['address', 'address']
         params = [superAdmin, newSuperAdmin]
         p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"updateSuperAdmin",
                         types,params,lw, keyFromPw, true);
         await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

         /// await this.everestRoles.updateSuperAdmin(newSuperAdmin, {from: superAdmin});
          (await this.metaUserRoles.getSuperAdmin()).should.be.equal(newSuperAdmin);
          superAdmin = newSuperAdmin;
       });



   it ("update a new Operation Everest Admin by operationAdmin", async function() {
       let newOperationAdmin = web3.utils.toChecksumAddress(this.acct[16]);
        types = ['address', 'address']
        params = [operationAdmin, newOperationAdmin]
        p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"updateOperationAdmin",
                        types,params,lw, keyFromPw, true);
        await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});


       (await this.metaUserRoles.getOperationAdmin()).should.be.equal(newOperationAdmin);
       operationAdmin = newOperationAdmin;
   })



  it ('add a new director', async function() {
       types = ['address','uint8', 'address[]']
       params = [operationAdmin,directorIndex, directors]
       p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitAddAccount",
                       types,params,lw, keyFromPw,true);
       await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true , {from: creator});

       for ( var i = 0; i < directors.length; ++i ){
          (await this.metaUserRoles.isPendingAccount(directorIndex,directors[i])).should.be.equal(true);
       }


       types = ['address','uint8', 'address[]']
      params = [superAdmin,directorIndex, directors]
      p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveAddAccount",
                      types,params,lw, keyFromPw, false);
      await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false, {from: creator});

      for (var j = 0; j < directors.length; ++j ) {
         (await this.metaUserRoles.isValidIndexAccount(directorIndex,directors[j])).should.be.equal(true);
      }


   });


  it("remove a director", async function() {
      types = ['address','uint8', 'address[]'];
     params = [operationAdmin,directorIndex, [directors[1]]];
     p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitRemoveIndexAccount",
                     types,params,lw, keyFromPw, true);
     await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});


     (await this.metaUserRoles.isPendingAccount(2*directorIndex,directors[1])).should.be.equal(true);



       types = ['address','uint8', 'address[]'];
       params = [superAdmin, directorIndex, [directors[1]]];
       p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveRemoveIndexAccount",
                       types,params,lw, keyFromPw, true);
       await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

       (await this.metaUserRoles.isValidIndexAccount(directorIndex,directors[1])).should.be.equal(false);
  });



   it("add doctors", async function() {




       types = ['address','uint8', 'address[]'];
       params = [operationAdmin, doctorIndex, doctors];
       p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitAddAccount",
                       types,params,lw, keyFromPw, true);
       await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

       for( var i = 0; i < doctors.length; i++){
            (await this.metaUserRoles.isPendingAccount(3,doctors[i])).should.be.equal(true);
       }


      types = ['address','uint8', 'address[]'];
       params = [superAdmin, doctorIndex, doctors];
       p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveAddAccount",
                       types,params,lw, keyFromPw, false);
       await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false,{from: creator});

       for( var i = 0; i < doctors.length; i++){
            (await this.metaUserRoles.isValidIndexAccount(doctorIndex,doctors[i])).should.be.equal(true);
       }



   });

   it("remove doctors", async function () {


      types = ['address','uint8', 'address[]'];
      params = [operationAdmin, doctorIndex, doctors];
      p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitRemoveIndexAccount",
                      types,params,lw, keyFromPw,true);
      await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});




       for( var i = 0; i < doctors.length; i++){
            (await this.metaUserRoles.isPendingAccount(4,doctors[i])).should.be.equal(true);
       }




      types = ['address','uint8', 'address[]'];
      params = [superAdmin, doctorIndex, doctors];
      p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveRemoveIndexAccount",
                      types,params,lw, keyFromPw, true);
      await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

       for( var i = 0; i < doctors.length; i++){
            (await this.metaUserRoles.isValidIndexAccount(doctorIndex,doctors[i])).should.be.equal(false);
       }


   });



   it("add assistants ", async function() {

     types = ['address','uint8', 'address[]'];
     params = [operationAdmin, assistantIndex, assistants];
     p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitAddAccount",
                     types,params,lw, keyFromPw, false);
     await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false, {from: creator});


      for( var i = 0; i < assistants.length; i++){
            (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(true);
        }


    types = ['address','uint8', 'address[]'];
     params = [superAdmin, assistantIndex, assistants];
     p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveAddAccount",
                     types,params,lw, keyFromPw, false);

     await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false, {from: creator});

       for( var i = 0; i < assistants.length; i++){
            (await this.metaUserRoles.isValidIndexAccount(assistantIndex,assistants[i])).should.be.equal(true);
       }

   })


    it("remove assistants ", async function() {


        types = ['address','uint8', 'address[]'];
        params = [operationAdmin, assistantIndex, assistants];
        p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitRemoveIndexAccount",
                        types,params,lw, keyFromPw, true);
        await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true,{from: creator});


        for( var i = 0; i < assistants.length; i++){
               (await this.metaUserRoles.isPendingAccount(6,assistants[i])).should.be.equal(true);
          }


          types = ['address','uint8', 'address[]'];
         params = [superAdmin, assistantIndex, assistants];
         p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveRemoveIndexAccount",
                         types,params,lw, keyFromPw, false);
         await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false, {from: creator});



        for( var i = 0; i < assistants.length; i++){
             (await this.metaUserRoles.isValidIndexAccount(assistantIndex,assistants[i])).should.be.equal(false);
         }


      for( var i = 0; i < assistants.length; i++){
            (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(false);
       }
    })






    it("revoke pending add account by operation admin", async function() {
        types = ['address','uint8', 'address[]'];
        params = [operationAdmin, assistantIndex, assistants];
       p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitAddAccount",
                       types,params,lw, keyFromPw, true);
       await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

       for( var i = 0; i < assistants.length; i++){
                  (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(true);
             }

     types = ['address', 'uint8', 'address[]'];
     params = [operationAdmin, assistantIndex, assistants];
     p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"revokePendingAddAccount",
                     types,params,lw, keyFromPw, true);
     await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});


       for( var i = 0; i < assistants.length; i++){
          (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(false);
      }


    })




    it("revoke pending add account by  super admin", async function() {




        types = ['address','uint8', 'address[]'];
               params = [operationAdmin, assistantIndex, assistants];
       p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitAddAccount",
                       types,params,lw, keyFromPw, false);
       await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false,{from: creator});

       for( var i = 0; i < assistants.length; i++){
                  (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(true);
             }

         types = ['address', 'uint8', 'address[]'];
         params = [superAdmin, assistantIndex, assistants];
         p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"revokePendingAddAccount",
                         types,params,lw, keyFromPw, false);
         await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false, {from: creator});


          for( var i = 0; i < assistants.length; i++){
             (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(false);
          }
    })


     it("revoke pending remove account by operation admin", async function() {



          types = ['address','uint8', 'address[]'];
                params = [operationAdmin, assistantIndex, assistants];
            p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitAddAccount",
                            types,params,lw, keyFromPw, true);
            await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

            for( var i = 0; i < assistants.length; i++){
               (await this.metaUserRoles.isPendingAccount(5,assistants[i])).should.be.equal(true);
          }

          types = ['address','uint8', 'address[]'];
         params = [superAdmin, assistantIndex, assistants];
         p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"approveAddAccount",
                         types,params,lw, keyFromPw, true);
         await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});





            types = ['address','uint8', 'address[]'];
            params = [operationAdmin, assistantIndex, assistants];
           p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitRemoveIndexAccount",
                           types,params,lw, keyFromPw, true);
           await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});

           for( var i = 0; i < assistants.length; i++){
                      (await this.metaUserRoles.isPendingAccount(6,assistants[i])).should.be.equal(true);
                 }

         types = ['address', 'uint8', 'address[]'];
         params = [operationAdmin, assistantIndex, assistants];
         p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"revokePendingRemoveAccount",
                         types,params,lw, keyFromPw, true);
         await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, true, {from: creator});


           for( var i = 0; i < assistants.length; i++){
              (await this.metaUserRoles.isPendingAccount(6,assistants[i])).should.be.equal(false);
          }


        })


        it("revoke pending remove account by  super admin", async function() {

            types = ['address','uint8', 'address[]'];
                   params = [operationAdmin, assistantIndex, assistants];
           p = await helpers1.signPayload(operationAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"submitRemoveIndexAccount",
                           types,params,lw, keyFromPw, false);
           await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false,{from: creator});

           for( var i = 0; i < assistants.length; i++){
                      (await this.metaUserRoles.isPendingAccount(6,assistants[i])).should.be.equal(true);
                 }

             types = ['address', 'uint8', 'address[]'];
             params = [superAdmin, assistantIndex, assistants];
             p = await helpers1.signPayload(superAdmin,this.txRelay, zeroAddress, this.metaUserRoles.address,"revokePendingRemoveAccount",
                             types,params,lw, keyFromPw, false);
             await this.txRelay.relayMetaTx(p.v, p.r, p.s, p.dest, p.data, zeroAddress, false, {from: creator});


              for( var i = 0; i < assistants.length; i++){
                 (await this.metaUserRoles.isPendingAccount(6,assistants[i])).should.be.equal(false);
              }
        })


}

module.exports = MetaUserRolesBehaviour