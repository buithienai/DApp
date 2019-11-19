package dapp.dapp.dto;


import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class SignatureDto {

    SignedMessageDto signedMessage;

    private String messageData;
    private String messageHash;
    private BigInteger nonce;
    private String destinationAddress;

    private String rawMessage;

    private String data;

    private String transactionData;

    private String sender;

    private int isHardwareWallet;

    private boolean isHardwareWallet2;

    public SignedMessageDto getSignedMessage() {
        return signedMessage;
    }

    public SignatureDto setSignedMessage(SignedMessageDto signedMessage) {
        this.signedMessage = signedMessage;
        return this;
    }

    public String getMessageData() {
        return messageData;
    }

    public SignatureDto setMessageData(String messageData) {
        this.messageData = messageData;
        return this;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public SignatureDto setMessageHash(String messageHash) {
        this.messageHash = messageHash;
        return this;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public SignatureDto setNonce(BigInteger nonce) {
        this.nonce = nonce;
        return this;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public SignatureDto setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }



    public static Sign.SignatureData __convertSignedMessageDtoIntoSignature(SignedMessageDto signedMessageDto) {
        Sign.SignatureData signedData = new Sign.SignatureData( (byte) signedMessageDto.getV(),
                Numeric.hexStringToByteArray(signedMessageDto.getR()),
                Numeric.hexStringToByteArray(signedMessageDto.getS()));


        return signedData;
    }

    public String getSender() {
        return sender;
    }

    public SignatureDto setSender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public SignatureDto setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
        return this;
    }

    public String getData() {
        return data;
    }

    public SignatureDto setData(String data) {
        this.data = data;
        return this;
    }

    public String getTransactionData() {
        return transactionData;
    }

    public SignatureDto setTransactionData(String transactionData) {
        this.transactionData = transactionData;
        return this;
    }

    public int getIsHardwareWallet() {
        return isHardwareWallet;
    }

    public SignatureDto setIsHardwareWallet(int isHardwareWallet) {
        this.isHardwareWallet = isHardwareWallet;
        return this;
    }

    public boolean isHardwareWallet2() {
        return isHardwareWallet2;
    }

    public SignatureDto setHardwareWallet2(boolean hardwareWallet2) {
        isHardwareWallet2 = hardwareWallet2;
        return this;
    }
}