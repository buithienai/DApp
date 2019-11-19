package dapp.dapp.service.impl;

import dapp.dapp.domain.BCTransaction;
import dapp.dapp.repository.BCTransactionRepository;
import dapp.dapp.service.BCTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.Date;

@Service
public class BCTransactionServiceImpl implements BCTransactionService {

    @Autowired
    private BCTransactionRepository bcTransactionRepository;


    public BCTransaction createBCTransaction(BCTransaction bcTransaction, TransactionReceipt transactionReceipt) {
        this.convertTransactionReceiptToBCTransaction(bcTransaction, transactionReceipt);

        Date now = new Date();
        bcTransaction.setCreatedTime(now);
        return this.bcTransactionRepository.save(bcTransaction);

    }

    public BCTransaction createExceptionBCTransaction(BCTransaction bcTransaction) {


        Date now = new Date();
        bcTransaction.setCreatedTime(now);
        return this.bcTransactionRepository.save(bcTransaction);

    }


    private void convertTransactionReceiptToBCTransaction(BCTransaction bcTransaction, TransactionReceipt transactionReceipt) {
        bcTransaction.setTransactionIndex(transactionReceipt.getTransactionIndex());
        bcTransaction.setBlockHash(transactionReceipt.getBlockHash());
        bcTransaction.setBlockNumber(transactionReceipt.getBlockNumber());
        bcTransaction.setCummulativeGasUsed(transactionReceipt.getCumulativeGasUsed());
        bcTransaction.setFrom(transactionReceipt.getFrom());
        bcTransaction.setTo(transactionReceipt.getTo());
        bcTransaction.setContractAddr(transactionReceipt.getContractAddress());
        bcTransaction.setRoot(transactionReceipt.getRoot());
        bcTransaction.setLogsBloom(transactionReceipt.getLogsBloom());
        bcTransaction.setStatus(transactionReceipt.getStatus());
        bcTransaction.setGasUse(transactionReceipt.getGasUsed());

    }

    public BCTransaction getBCTransactionByTransactionHash(String transactionHash) {
        return this.bcTransactionRepository.findByTransactionId(transactionHash);
    }

    public BCTransaction getBCTransactionByTransactionId(String transactionId) {
        return this.bcTransactionRepository.findById(transactionId).get();
    }

}
