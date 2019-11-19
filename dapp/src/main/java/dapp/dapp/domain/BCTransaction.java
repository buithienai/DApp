package dapp.dapp.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Indexed;

import java.math.BigInteger;
import java.util.Date;

@Document(collection = "block_transaction")
public class BCTransaction {


    /**
     * blockHash: String, 32 Bytes - hash of the block where this transaction was in.
     * blockNumber: Number - block number where this transaction was in.
     * transactionHash: String, 32 Bytes - hash of the transaction.
     * transactionIndex: Number - integer of the transactions index position in the block.
     * from: String, 20 Bytes - address of the sender.
     * to: String, 20 Bytes - address of the receiver. null when its a contract creation transaction.
     * cumulativeGasUsed: Number - The total amount of gas used when this transaction was executed in the block.
     * gasUsed: Number - The amount of gas used by this specific transaction alone.
     * contractAddress: String - 20 Bytes - The contract address created, if the transaction was a contract creation, otherwise null.
     * logs: Array - Array of log objects, which this transaction generated.
     */

    @Id
    private String id;

    @Field("transaction_id")
    private String transactionId;

    @Field("chain_id")
    private int chainId;

    @Field("chain_name")
    private String chainName;

    @Field("transaction_index")
    private BigInteger transactionIndex;

    @Field("block_hash")
    private String blockHash;

    @Field("block_number")
    private BigInteger blockNumber;

    @Field("cummulative_gas_used")
    private BigInteger cummulativeGasUsed;

    @Field("from")
    private String from;

    @Field("to")
    private String to;

    @Field("contract_addr")
    private String contractAddr;


    @Field("root")
    private String root;

    @Field("logs_bloom")
    private String logsBloom;



    @Field("identity_addr")
    private String identityAddr;

    @Field("status")
    private String status;

    @Field("relayer")
    private String relayer;


    @Field("message_sender")
    private String messageSender;

  /*  @Field("transaction_type_id")
    private String transactionTypeId;*/

    @Field("transaction_type_name")
    private String transactionTypeName;

    @Field("coin_id")
    private String coinId;


    @Field("amount")
    private BigInteger amount;

    @Field("gas_limit")
    private BigInteger gasLimit;

    @Field("gas_price")
    private BigInteger gasPrice;

    @Field("gas_used")
    private BigInteger gasUse;


    @Field("created_time")
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private Date createdTime;

    @Field("created_id")
    private String createdId;

    @PersistenceConstructor
    public BCTransaction(String transactionId) {
        this.transactionId = transactionId;
    }


    public String getId() {
        return id;
    }

    public BCTransaction setId(String id) {
        this.id = id;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BCTransaction setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getIdentityAddr() {
        return identityAddr;
    }

    public BCTransaction setIdentityAddr(String identityAddr) {
        this.identityAddr = identityAddr;
        return this;
    }



    public String getRelayer() {
        return relayer;
    }

    public BCTransaction setRelayer(String relayer) {
        this.relayer = relayer;
        return this;
    }


  /*  public String getTransactionTypeId() {
        return transactionTypeId;
    }

    public BCTransaction setTransactionTypeId(String transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
        return this;
    }*/

    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    public BCTransaction setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
        return this;
    }

    public String getCoinId() {
        return coinId;
    }

    public BCTransaction setCoinId(String coinId) {
        this.coinId = coinId;
        return this;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public BCTransaction setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BCTransaction setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public BigInteger getGasUse() {
        return gasUse;
    }

    public BCTransaction setGasUse(BigInteger gasUse) {
        this.gasUse = gasUse;
        return this;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public BCTransaction setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public BigInteger getTransactionIndex() {
        return transactionIndex;
    }

    public BCTransaction setTransactionIndex(BigInteger transactionIndex) {
        this.transactionIndex = transactionIndex;
        return this;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public BCTransaction setBlockHash(String blockHash) {
        this.blockHash = blockHash;
        return this;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public BCTransaction setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }



    public String getFrom() {
        return from;
    }

    public BCTransaction setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public BCTransaction setTo(String to) {
        this.to = to;
        return this;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public BCTransaction setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
        return this;
    }

    public String getRoot() {
        return root;
    }

    public BCTransaction setRoot(String root) {
        this.root = root;
        return this;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public BCTransaction setLogsBloom(String logsBloom) {
        this.logsBloom = logsBloom;
        return this;
    }

    public BigInteger getCummulativeGasUsed() {
        return cummulativeGasUsed;
    }

    public BCTransaction setCummulativeGasUsed(BigInteger cummulativeGasUsed) {
        this.cummulativeGasUsed = cummulativeGasUsed;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BCTransaction setStatus(String status) {
        this.status = status;
        return this;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public BCTransaction setAmount(BigInteger amount) {
        this.amount = amount;
        return this;
    }

    public int getChainId() {
        return chainId;
    }

    public BCTransaction setChainId(int chainId) {
        this.chainId = chainId;
        return this;
    }

    public String getCreatedId() {
        return createdId;
    }

    public BCTransaction setCreatedId(String createdId) {
        this.createdId = createdId;
        return this;
    }

    public String getChainName() {
        return chainName;
    }

    public BCTransaction setChainName(String chainName) {
        this.chainName = chainName;
        return this;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public BCTransaction setMessageSender(String messageSender) {
        this.messageSender = messageSender;
        return this;
    }
}

