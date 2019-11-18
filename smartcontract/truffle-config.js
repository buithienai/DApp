/**
 * Use this file to configure your truffle project. It's seeded with some
 * common settings for different networks and features like migrations,
 * compilation and testing. Uncomment the ones you need or modify
 * them to suit your project as necessary.
 *
 * More information about configuration can be found at:
 *
 * truffleframework.com/docs/advanced/configuration
 *
 * To deploy via Infura you'll need a wallet provider (like truffle-hdwallet-provider)
 * to sign your transactions before they're sent to a remote public node. Infura accounts
 * are available for free at: infura.io/register.
 *
 * You'll also need a mnemonic - the twelve word phrase the wallet uses to generate
 * public/private key pairs. If you're publishing your code to GitHub make sure you load this
 * phrase from a file you've .gitignored so it doesn't accidentally become public.
 *
 */
require('chai/register-should');


require('dotenv').config();
require('babel-register')({
  ignore: /node_modules\/(?!openzeppelin-solidity)/
});
require('babel-polyfill');

// http://truffleframework.com/tutorials/using-infura-custom-provider
/*
const HDWalletProvider = require('truffle-hdwallet-provider');
let mnemonic = "ecology agent holiday shy sample carpet aim fury fence eager cupboard method fade angle focus";

const providerWithMnemonic = (mnemonic, providerUrl) => new HDWalletProvider(mnemonic, providerUrl);
const infuraProvider = network => providerWithMnemonic(
  process.env.MNEMONIC || '',
  `https://${network}.infura.io/${process.env.INFURA_API_KEY}`
);
*/
/*const ropstenProvider = process.env.SOLIDITY_COVERAGE
  ? undefined
  : infuraProvider('ropsten');*/

/*const HDWalletProvider = require("truffle-hdwallet-provider");
const TestRPC = require("ganache-cli");

let provider

function getNmemonic() {
  try{
    return require('fs').readFileSync("./seed", "utf8").trim();
  } catch(err){
    return " can't find seed";
  }
}

function getProvider(rpcUrl) {
  if (!provider) {
    provider = new HDWalletProvider(getNmemonic(), rpcUrl)
  }
  return provider
}*/




const solcStable = {
  version: '0.5.7',
};

const solcNightly = {
  version: 'nightly',
  docker: true,
};

const useSolcNightly = process.env.SOLC_NIGHTLY === 'true';

module.exports = {
  networks: {
   /* development: {
      host: 'localhost',
      port: 7545,
      network_id: '*', // eslint-disable-line camelcase
    },
    coverage: {
      host: 'localhost',
      network_id: '*', // eslint-disable-line camelcase
      port: 7555,
      gas: 0xfffffffffff,
      gasPrice: 0x01,*/


        in_memory: {
          get provider() {
            if (!provider) {
              provider = TestRPC.provider({total_accounts: 25})
            }
            return provider
          },
          network_id: "*"
        },
          privateTest: {
            host: "localhost",
            port: 8544,
            network_id: "234"
          },




   development: {
        host: 'localhost',
        port: 8545,
        network_id: '*', // eslint-disable-line camelcase
        gas: 0x666C46,
        gasPrice: 0x01
      },
/*      ropsten: {
        provider: ropstenProvider,
        network_id: 3 // eslint-disable-line camelcase
      },*/
      coverage: {
        host: 'localhost',
        network_id: '*', // eslint-disable-line camelcase
        port: 8555,
        gas: 0xfffffffffff,
        gasPrice: 0x01
      },
      ganache: {
        host: 'localhost',
        port: 8545,
        network_id: '*', // eslint-disable-line camelcase
        gas: 0x666C46,
        gasPrice: 0x01
      }

    },

    mocha: {
      reporter: 'eth-gas-reporter',
      reporterOptions: {
        currency: 'SGD',
        gasPrice: 5
      }
    },

  compilers: {
    solc: useSolcNightly ? solcNightly : solcStable,
  },
};
