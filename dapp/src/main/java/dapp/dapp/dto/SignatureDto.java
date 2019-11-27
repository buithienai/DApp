package dapp.dapp.dto;


import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class SignatureDto {

    SignedMessageDto signedMessage;

    private String destinationAddress;

    private String rawMessage;

    private String sender;


    private String messageHash;

    private boolean isHardwareWallet;

    public SignedMessageDto getSignedMessage() {
        return signedMessage;
    }

    public SignatureDto setSignedMessage(SignedMessageDto signedMessage) {
        this.signedMessage = signedMessage;
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

    public boolean isHardwareWallet() {
        return isHardwareWallet;
    }

    public SignatureDto setHardwareWallet(boolean hardwareWallet) {
        isHardwareWallet = hardwareWallet;
        return this;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public SignatureDto setMessageHash(String messageHash) {
        this.messageHash = messageHash;
        return this;
    }
}