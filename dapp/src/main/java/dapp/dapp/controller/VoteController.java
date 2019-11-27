package dapp.dapp.controller;

import dapp.dapp.domain.BCTransaction;
import dapp.dapp.dto.SignatureDto;
import dapp.dapp.dto.VoteDto;
import dapp.dapp.service.BCTransactionService;
import dapp.dapp.util.ChainUtil;
import dapp.dapp.util.CryptoUtil;
import dapp.dapp.util.IPFSUtil;
import dapp.dapp.util.PubChainConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Sign;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Value("${blockchain.public.metaklnetwork.addr}")
    private String metaklNetworkAddr;

    @Value("${chain.gasPrice}")
    private BigInteger gasPrice;

    @Value("${chain.gasLimit}")
    private BigInteger gasLimit;

    @Autowired
    private BCTransactionService bcTransactionService;

    @RequestMapping(method = RequestMethod.POST, value="/addVote")
    public @ResponseBody
    Map<String,Object> submitVote(@RequestBody VoteDto voteDto){
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("code",0);
        resultData.put("message","successfully");
        resultData.put("numberVote",0);

        if(voteDto.getSignatureDto() == null) {
            resultData.put("code",1001);
            return resultData;
        }

        SignatureDto signedMessage = voteDto.getSignatureDto();

        if(!CryptoUtil.validateSignedMessage(signedMessage)) {
            resultData.put("code",1001);
            return resultData;
        }


        try {

            Sign.SignatureData rawMessage = SignatureDto.__convertSignedMessageDtoIntoSignature(signedMessage.getSignedMessage());

            TransactionReceipt transactionReceipt = ChainUtil.relayTransactionByTxRalayer(rawMessage.getV(), rawMessage.getR(), rawMessage.getS(),
                    metaklNetworkAddr, signedMessage.getRawMessage(), signedMessage.isHardwareWallet());

            /// save transaction
            BCTransaction bcTransaction = new BCTransaction(transactionReceipt.getTransactionHash());

            bcTransaction.setGasLimit(gasLimit);
            bcTransaction.setGasPrice(gasPrice);
            PubChainConfig pubChainConfig = PubChainConfig.getInstance();
            bcTransaction.setChainId(pubChainConfig.getChainId());
            bcTransaction.setChainName(pubChainConfig.getChainName());
            bcTransaction.setRelayer(transactionReceipt.getFrom());
            bcTransaction.setMessageSender(signedMessage.getSender());

            BCTransaction bcTransactionCreated = this.bcTransactionService.createBCTransaction(bcTransaction, transactionReceipt);


        } catch (Exception e) {
            logger.error("exception" + e);
            resultData.put("code",1001);
        } finally {
            return resultData;
        }

    }


    @RequestMapping(method = RequestMethod.POST, value ="/uploadMultiFile")
    public @ResponseBody
    Map<String,Object> uploadFileList( MultipartFile[] files) {

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("code",0);
        resultData.put("message","successfully");
        resultData.put("ipfHash",new ArrayList<>());

        try {
            List<String> ipfHashList = new ArrayList<>();
            for(MultipartFile multipartFile : files) {
                File file = this.convertMultiPartToFile(multipartFile);
                String ipfsLink = IPFSUtil.uploadIPFSFile(file);

                if(ipfsLink != null) {
                    ipfHashList.add(ipfsLink);
                }
            }

            resultData.put("ipfsHash",ipfHashList);
            return resultData;

        } catch (Exception e) {
            logger.error("created company error {} " , e);
            resultData.put("code",1001);
            return resultData;

        }
    }


    /**
     * convert multipartfiles into file
     * @param multipartFile
     * @return
     * @throws IOException
     */
    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);

        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }



}
