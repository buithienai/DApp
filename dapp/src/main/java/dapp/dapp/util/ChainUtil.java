package dapp.dapp.util;

import dapp.dapp.smartcontract.TxRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;


@Component
public class ChainUtil {
    private static final Logger logger = LoggerFactory.getLogger(ChainUtil.class);

    private static volatile  Credentials txRelay;

    private static volatile TransactionManager txRelayTransactionManager;

    private static String txRelayKey;

    private static String txRelayPassword;

    private static String txRelayContract;

    private static String whilelistOwner;

    private static String zeroAddress;

    private static BigInteger gasLimit;

    private static BigInteger gasPrice;

    private static String resourceFolder;



    private ChainUtil() {

    }

    /*static {
        initiatePublicWebUtil();
    }*/


    /// initate the txRelay
   /* synchronized static private void initiatePublicWebUtil() {
        try {

            URL url = URL.class.getResource("/" + txRelayKey + ".json");
            txRelay = WalletUtils.loadCredentials(txRelayPassword, url.getPath());
            logger.info("load tx Relayer successfully" + txRelay.getAddress());
        } catch (IOException e) {
            logger.error("Failed to load txRelay " + txRelayKey + "error " + e);
        } catch (CipherException e) {
            logger.error("Cipher Exception failed to load txRelay key" + txRelayKey + " error" + e);
        }
    }*/

    public static synchronized Credentials getTxRelay() {
        if (txRelay == null) {
            synchronized (ChainUtil.class) {
                if ( null == txRelay) {
                    try {
                        //URL url = URL.class.getResource("/" + txRelayKey + ".json");
                      //  URL url = URL.class.getResource("/files/OperationAdmin.json");
                        logger.info("tx relayer" + txRelayKey);
                        File file = new File("src//main//resources//files//" + txRelayKey + ".json");

                        logger.info("tx relay file" + file.getPath());


                        txRelay = WalletUtils.loadCredentials(txRelayPassword, file.getPath());
                        logger.info("load tx Relayer successfully" + txRelay.getAddress());

                    } catch (IOException e) {
                        logger.error("Failed to load txRelay " + txRelayKey + "error " + e);
                    } catch (CipherException e) {
                        logger.error("Cipher Exception failed to load txRelay key" + txRelayKey + " error" + e);
                    } catch (Exception e) {
                        logger.error("get Tx Relay failed at url " + e );
                    }
                }
            }
        }
        return  txRelay;
    }


    public static synchronized TransactionManager getTxRelayTransactionManager() {
        if(txRelayTransactionManager == null) {
            synchronized (ChainUtil.class) {
                if( txRelayTransactionManager == null) {
                    logger.info("get Tx Relay Transaction Manager");
                    Credentials txRelayInstance = getTxRelay();
                    txRelayTransactionManager = new FastRawTransactionManager(PubWeb3.getInstance(), txRelayInstance);
                    logger.info("Initiate the txRelayTransaction Manager");
                }
            }
        }
        return  txRelayTransactionManager;
    }



    public static String getTxRelayKey() {
        return txRelayKey;
    }



    @Value("${chain.relayer}")
    public void setTxRelayKey(String relayer) {
        ChainUtil.txRelayKey = relayer;
    }

    public static String getTxRelayContract() {
        return txRelayContract;
    }

    @Value("${blockchain.public.txRelay}")
    public void setTxRelayContract(String txRelayContract) {
        ChainUtil.txRelayContract = txRelayContract;
    }

    public static String getWhilelistOwner() {
        return whilelistOwner;
    }

    @Value("${chain.whitelistOwner}")
    public void setWhilelistOwner(String whilelistOwner) {
        ChainUtil.whilelistOwner = whilelistOwner;
    }


    public static String getZeroAddress() {
        return zeroAddress;
    }


    @Value("${chain.zeroAddress}")
    public void setZeroAddress(String zeroAddress) {
        ChainUtil.zeroAddress = zeroAddress;
    }

    public static BigInteger getGasLimit() {
        return gasLimit;
    }

    @Value("${chain.gasLimit}")
    public void setGasLimit(BigInteger gasLimit) {
        ChainUtil.gasLimit = gasLimit;
    }

    public static BigInteger getGasPrice() {
        return gasPrice;
    }

    @Value("${chain.gasPrice}")
    public void setGasPrice(BigInteger gasPrice) {
        ChainUtil.gasPrice = gasPrice;
    }

    public static String getTxRelayPassword() {
        return txRelayPassword;
    }

    @Value("${chain.txRelayPassword}")
    public void setTxRelayPassword(String txRelayPassword) {
        ChainUtil.txRelayPassword = txRelayPassword;
    }

    public static String getResourceFolder() {
        return resourceFolder;
    }


    public void setResourceFolder(String resourceFolder) {
        ChainUtil.resourceFolder = resourceFolder;
    }

    public static Credentials getCredential(String keyName) {
        try {
            if ( keyName.equals(txRelayKey)) {
                return getTxRelay();
            }
            // We then need to load our Ethereum wallet file
            logger.info("load credentail url keyNam" + keyName);


            File file = new File("src//main//resources//files//" + keyName + ".json");

            logger.info("file utils 1" + file.getPath());

            Credentials credentials =
                    WalletUtils.loadCredentials(
                            txRelayPassword,
                            file.getPath());
            logger.info("Credentials loaded - " + credentials.getAddress());
            return  credentials;
        } catch (IOException e) {
            logger.error("Credential " + keyName + "load error at" + e);
        } catch (Exception e) {
            logger.error("Exception error" + e);
        }
        return null;
    }


    /**
     * get nounce of signingAddr from Relay smart contract
     * @param signingAddr
     * @return
     */
    public static BigInteger getTxRelayNonce(String signingAddr) {
        try {
           // TransactionManager transactionManager = new FastRawTransactionManager(PubWeb3.getInstance(), getTxRelay());
            TxRelay txRelay = TxRelay.load(txRelayContract,PubWeb3.getInstance(), getTxRelayTransactionManager(), DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT );
            return txRelay.getNonce(signingAddr).send();
        } catch (Exception e) {
            logger.error("Error to get meta nounce at this address {} " + e, signingAddr );
        }
      return BigInteger.valueOf(-1);
    }


    /**
     * Relay transaction by Relayer
     * @param v
     * @param r
     * @param s
     * @param destinationAddress
     * @param data
     * @return
     * @throws Exception
     */
    public static TransactionReceipt relayTransactionByTxRalayer(byte[] v, byte[] r, byte[] s, String destinationAddress, String data, boolean isHardwareWallet) throws Exception {

        BigInteger vValue = new BigInteger(v);
        String rValue = Numeric.toHexString(r);

        String sValue =  Numeric.toHexString(s);

        logger.info("txRelay Contract: " + txRelayContract +  "- v: " + vValue + " - r: " + rValue + " - sValue: " + sValue +
                " - dest" + destinationAddress + " data: " + data + " - zeroAdd: " + getWhilelistOwner() + "ishardware: " + isHardwareWallet );

        TxRelay txRelaySmartContractInstance = TxRelay.load(txRelayContract,PubWeb3.getInstance(),getTxRelayTransactionManager(), gasPrice, gasLimit);

        logger.info("relay transaction");

        byte[] dataByte = Numeric.hexStringToByteArray(data);
        return txRelaySmartContractInstance.relayMetaTx( new BigInteger(v),r,s,destinationAddress, dataByte, getWhilelistOwner(),isHardwareWallet).send();


    }


}
