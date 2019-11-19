package dapp.dapp.service;

import dapp.dapp.domain.BCTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public interface BCTransactionService {

    public BCTransaction createBCTransaction(BCTransaction bcTransaction, TransactionReceipt transactionReceipt);

    public BCTransaction createExceptionBCTransaction(BCTransaction bcTransaction);

    public BCTransaction getBCTransactionByTransactionHash(String transactionHash);

    public BCTransaction getBCTransactionByTransactionId(String transactionId) ;

}