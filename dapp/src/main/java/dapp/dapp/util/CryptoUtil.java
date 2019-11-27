package dapp.dapp.util;

import dapp.dapp.dto.SignatureDto;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Pattern;

public class CryptoUtil {

    private  static final Logger logger = LoggerFactory.getLogger(CryptoUtil.class);

    private static SecureRandom random = new SecureRandom();

    public static int roleExpiredTime = 100; // add 100 years


    /**
     *
     *
     * @param hexNum
     * @return String of decimal
     * @Throws error if string doesn't contain hex number form
     */
    public static String hexStringToDecimalString(String hexNum) {
        boolean match = Pattern.matches("0[xX][0-9a-fA-F]", hexNum);

        if ( !match) {
            logger.error("the string doens't contain hex number in form 0x...: [" + hexNum + "]");
            throw new Error("the string doesn't contain hex number in fomr 0x... [" + hexNum + "]");
        }

        byte[] numbeBytes = Hex.decode(hexNum.substring(2));
        return ( new BigInteger(1, numbeBytes).toString());

    }


    /**
     *
     * @param timestamp
     * @return  date in the SimpleForate
     */
    public static String longToDateTime(long timestamp) {
        Date date = new Date(timestamp * 1000) ;
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatter.format(date);

    }



    public static boolean validateSignedMessage(SignatureDto signatureDto) {
        Sign.SignatureData signedMessage = SignatureDto.__convertSignedMessageDtoIntoSignature(signatureDto.getSignedMessage());

        if(signedMessage.getS().length != 32 && signedMessage.getR().length != 32) {
            return false;
        }

        String signedAddress = CryptoUtil.getSignerAddress(signatureDto.getMessageHash(), signedMessage, signatureDto);

        if (signedAddress == null || !CryptoUtil.isValidEthereumAddress(signedAddress)) {
            return false;
        }
        signatureDto.setSender(signedAddress);

        return true;

    }


    /**
     * Get keccak hash 235
     * @param input
     * @return
     */
    // Create a SHA3256 hash (Keccak-256)
    public static String getSHA3Hash(String input) {

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
        digestSHA3.update(input.getBytes());
        String digestMessage = Hex.toHexString(digestSHA3.digest());
        return  digestMessage;
    }

    /**
     *
     * @return a sucure randome number
     */
    public static SecureRandom getRandom() {
        return random;
    }

    /**
     * an address is a Ethereum address if the length is 20 bytes or 40 hex characters
     *
     * @param addr
     * @return true if the address is valid
     */
    public static boolean isValidAddress(byte[] addr) {
        return addr != null && addr.length == 20;
    }


    /**
     * address is given in a hex account string is valid Ethereum addrss;
     * @param addr
     * @return
     */
    public static boolean isValidEthereumAddress(String addr) {
       //return true;
       /// need to fix bug
        return  isValidAddress(addr) && isCheckSubAddress(addr) ;
    }



    public static boolean isCheckSubAddress(String address) {
        String checkSumAddress = Keys.toChecksumAddress(address);
        return checkSumAddress.equals(address);
    }

    /* input : Address
     * Output: true or false
     * purpose: to validate an Ethereum Address
     *
     * Procedure :
     * If the string begins with 0x and contain numbers between 0 to 9 and a to f
     * then it is valid ethereum address
     */

    public static boolean isValidAddress(String addr) {
        String regex = "^0x[0-9a-fA-F]{40}$";

        //Print for testing purpose and more verbose output
        logger.info("Incoming Address " + addr);


        if(addr.matches(regex))
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @param addr length should be 20
     * @return short string represent 1f21c...
     */
    public static String getAddressShortString(byte[] addr) {
        if ( !isValidAddress(addr)) {
            logger.error("address is invalid");
            throw new Error("not an address");
        }

        String addrShort = Hex.toHexString(addr, 0, 3);

        StringBuffer sb = new StringBuffer();
        sb.append(addrShort);
        sb.append("...");
        return sb.toString();

    }


    /**
     * using to sign a message data and return a signed message
     * @param messData
     * @param credentials
     * @return
     */
    public static Sign.SignatureData getSignatureData(String messData, Credentials credentials) {
        byte[] hexMessage = Hash.sha3(messData.getBytes());
        return Sign.signMessage(hexMessage, credentials.getEcKeyPair());
    }





    /**
     * return transaction manager
     * @param keyName
     * @return
     */
    public static TransactionManager getTransactionManager(String keyName) {
        Credentials credentials = ChainUtil.getCredential(keyName);
        return new FastRawTransactionManager(PubWeb3.getInstance(), credentials);
    }

    /**
     * get signer adress from signed message
     * @param messageData
     * @param signedMessage
     * @return
     */
    public static String getSignerAddress(String messageData, Sign.SignatureData signedMessage, SignatureDto signatureDto) {
        try{



            byte[] byteMessage = Numeric.hexStringToByteArray(messageData);
            if(byteMessage.length != 32) {
                return  null;
            }
            String pubKey = Sign.signedMessageHashToKey(byteMessage, signedMessage).toString(16);
            String signerAddress = Keys.toChecksumAddress(Keys.getAddress(pubKey));
            return signerAddress;
        } catch (SignatureException e) {
            logger.error("Signature Exception at getSignerAddress" + e);
            return null;
        }


    }


    /**
     * padding 0 character
     * @param n
     * @return
     */
    public static String pad(String n) {
        if(n.startsWith("0x")){
            return "0x" + StringUtils.leftPad(n.substring(2),64,'0');

        } else {
            return "0x" + StringUtils.leftPad(n,64,'0');
        }
    }


    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
    }

    public static byte[] convertStringToByte(String stringValue) {
        return Numeric.hexStringToByteArray(CryptoUtil.asciiToHex(stringValue));
    }


    public static Bytes32 StringToByte32(String stringData ) {
        byte[] myStringInByte = Numeric.hexStringToByteArray(CryptoUtil.asciiToHex(stringData));
        return new Bytes32(myStringInByte);
    }



















}
