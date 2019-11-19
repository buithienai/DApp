package dapp.dapp.smartcontract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.2.
 */
@SuppressWarnings("rawtypes")
public class TxRelay extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610f20806100206000396000f3fe60806040526004361061007b5760003560e01c80639201de551161004e5780639201de5514610308578063b092145e1461039a578063c47cf5de146103e9578063ee0b1f2e146104b65761007b565b80632d0335ab14610080578063548db174146100c55780637f649783146101755780638c4b49d714610223575b600080fd5b34801561008c57600080fd5b506100b3600480360360208110156100a357600080fd5b50356001600160a01b03166104e3565b60408051918252519081900360200190f35b3480156100d157600080fd5b50610173600480360360208110156100e857600080fd5b810190602081018135600160201b81111561010257600080fd5b82018360208201111561011457600080fd5b803590602001918460208302840111600160201b8311171561013557600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610502945050505050565b005b34801561018157600080fd5b506101736004803603602081101561019857600080fd5b810190602081018135600160201b8111156101b257600080fd5b8201836020820111156101c457600080fd5b803590602001918460208302840111600160201b831117156101e557600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610510945050505050565b34801561022f57600080fd5b50610173600480360360e081101561024657600080fd5b60ff823516916020810135916040820135916001600160a01b036060820135169181019060a081016080820135600160201b81111561028457600080fd5b82018360208201111561029657600080fd5b803590602001918460018302840111600160201b831117156102b757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550506001600160a01b038335169350505060200135151561051b565b6103256004803603602081101561031e57600080fd5b5035610a26565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561035f578181015183820152602001610347565b50505050905090810190601f16801561038c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156103a657600080fd5b506103d5600480360360408110156103bd57600080fd5b506001600160a01b0381358116916020013516610b80565b604080519115158252519081900360200190f35b3480156103f557600080fd5b5061049a6004803603602081101561040c57600080fd5b810190602081018135600160201b81111561042657600080fd5b82018360208201111561043857600080fd5b803590602001918460018302840111600160201b8311171561045957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610ba0945050505050565b604080516001600160a01b039092168252519081900360200190f35b3480156104c257600080fd5b50610325600480360360208110156104d957600080fd5b503560ff16610bc5565b6001600160a01b0381166000908152602081905260409020545b919050565b61050d816000610e80565b50565b61050d816001610e80565b6001600160a01b038216158061055457506001600160a01b038216600090815260016020908152604080832033845290915290205460ff165b61055d57600080fd5b600061056884610ba0565b9050600082156107b05760606040518060400160405280601c81526020017f19457468657265756d205369676e6564204d6573736167653a0a36340000000081525090506000601960f81b600060f81b3088600080896001600160a01b03166001600160a01b03168152602001908152602001600020548c8c60405160200180886001600160f81b0319166001600160f81b0319168152600101876001600160f81b0319166001600160f81b0319168152600101866001600160a01b03166001600160a01b031660601b8152601401856001600160a01b03166001600160a01b031660601b8152601401848152602001836001600160a01b03166001600160a01b031660601b815260140182805190602001908083835b6020831061069e5780518252601f19909201916020918201910161067f565b6001836020036101000a03801982511681845116808217855250505050505090500197505050505050505060405160208183030381529060405280519060200120905060606106ec82610a26565b905082816040516020018083805190602001908083835b602083106107225780518252601f199092019160209182019101610703565b51815160209384036101000a600019018019909216911617905285519190930192850191508083835b6020831061076a5780518252601f19909201916020918201910161074b565b6001836020036101000a03801982511681845116808217855250505050505090500192505050604051602081830303815290604052805190602001209350505050610892565b6001600160a01b038216600090815260208181526040808320549051601960f81b8184018181526021830186905230606081811b60228601526bffffffffffffffffffffffff198c821b81166036870152604a8601879052908e901b16606a8501528b5192969590948b9490938e938e939092607e0191908401908083835b6020831061084e5780518252601f19909201916020918201910161082f565b6001836020036101000a0380198251168184511680821785525050505050509050019750505050505050506040516020818303038152906040528051906020012090505b604080516000808252602080830180855285905260ff8d1683850152606083018c9052608083018b90529251909260019260a080820193601f1981019281900390910190855afa1580156108ea573d6000803e3d6000fd5b505060408051601f198101516001600160a01b038116825291519193507fd6558c3ed910d959271054471fd1c326679d9fece99c5091b00ed89627cf2bfc925081900360200190a1806001600160a01b0316836001600160a01b03161461095057600080fd5b6001600160a01b038084166000908152602081815260408083208054600101905551895192938b16928a928291908401908083835b602083106109a45780518252601f199092019160209182019101610985565b6001836020036101000a0380198251168184511680821785525050505050509050019150506000604051808303816000865af19150503d8060008114610a06576040519150601f19603f3d011682016040523d82523d6000602084013e610a0b565b606091505b5050905080610a1957600080fd5b5050505050505050505050565b6060806000808060f0815b6020811015610b7457878160208110610a4657fe5b1a9450601085600f87851660041c16955006925085610a6485610bc5565b610a6d85610bc5565b6040516020018084805190602001908083835b60208310610a9f5780518252601f199092019160209182019101610a80565b51815160209384036101000a600019018019909216911617905286519190930192860191508083835b60208310610ae75780518252601f199092019160209182019101610ac8565b51815160209384036101000a600019018019909216911617905285519190930192850191508083835b60208310610b2f5780518252601f199092019160209182019101610b10565b6001836020036101000a038019825116818451168082178552505050505050905001935050505060405160208183030381529060405295508080600101915050610a31565b50939695505050505050565b600160209081526000928352604080842090915290825290205460ff1681565b6000602482511015610bb4575060006104fd565b50602401516001600160a01b031690565b606060ff8216610bed57506040805180820190915260018152600360fc1b60208201526104fd565b8160ff1660011415610c1757506040805180820190915260018152603160f81b60208201526104fd565b8160ff1660021415610c4157506040805180820190915260018152601960f91b60208201526104fd565b8160ff1660031415610c6b57506040805180820190915260018152603360f81b60208201526104fd565b8160ff1660041415610c9557506040805180820190915260018152600d60fa1b60208201526104fd565b8160ff1660051415610cbf57506040805180820190915260018152603560f81b60208201526104fd565b8160ff1660061415610ce957506040805180820190915260018152601b60f91b60208201526104fd565b8160ff1660071415610d1357506040805180820190915260018152603760f81b60208201526104fd565b8160ff1660081415610d3d57506040805180820190915260018152600760fb1b60208201526104fd565b8160ff1660091415610d6757506040805180820190915260018152603960f81b60208201526104fd565b8160ff16600a1415610d9157506040805180820190915260018152606160f81b60208201526104fd565b8160ff16600b1415610dbb57506040805180820190915260018152603160f91b60208201526104fd565b8160ff16600c1415610de557506040805180820190915260018152606360f81b60208201526104fd565b8160ff16600d1415610e0f57506040805180820190915260018152601960fa1b60208201526104fd565b8160ff16600e1415610e3957506040805180820190915260018152606560f81b60208201526104fd565b8160ff16600f1415610e6357506040805180820190915260018152603360f91b60208201526104fd565b50506040805180820190915260018152600360fc1b602082015290565b60005b8251811015610ee6573360009081526001602052604081208451849290869085908110610eac57fe5b6020908102919091018101516001600160a01b03168252810191909152604001600020805460ff1916911515919091179055600101610e83565b50505056fea265627a7a72315820d940322b6ce2b87888d6037ee5a23c9caffb253719eaae0a8859373ab624e53a64736f6c634300050b0032";

    public static final String FUNC_GETNONCE = "getNonce";

    public static final String FUNC_REMOVEFROMWHITELIST = "removeFromWhitelist";

    public static final String FUNC_ADDTOWHITELIST = "addToWhitelist";

    public static final String FUNC_RELAYMETATX = "relayMetaTx";

    public static final String FUNC_BYTES32TOSTRING = "bytes32ToString";

    public static final String FUNC_WHITELIST = "whitelist";

    public static final String FUNC_GETADDRESS = "getAddress";

    public static final String FUNC_FROMNIBBLETOSTRING = "fromNibbleToString";

    public static final Event SENDER_EVENT = new Event("Sender", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected TxRelay(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TxRelay(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TxRelay(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TxRelay(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> getNonce(String add) {
        final Function function = new Function(FUNC_GETNONCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, add)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeFromWhitelist(List<String> sendersToUpdate) {
        final Function function = new Function(
                FUNC_REMOVEFROMWHITELIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(sendersToUpdate, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addToWhitelist(List<String> sendersToUpdate) {
        final Function function = new Function(
                FUNC_ADDTOWHITELIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(sendersToUpdate, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> relayMetaTx(BigInteger sigV, byte[] sigR, byte[] sigS, String destination, byte[] data, String listOwner, Boolean isHardWallet) {
        final Function function = new Function(
                FUNC_RELAYMETATX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(sigV), 
                new org.web3j.abi.datatypes.generated.Bytes32(sigR), 
                new org.web3j.abi.datatypes.generated.Bytes32(sigS), 
                new org.web3j.abi.datatypes.Address(160, destination), 
                new org.web3j.abi.datatypes.DynamicBytes(data), 
                new org.web3j.abi.datatypes.Address(160, listOwner), 
                new org.web3j.abi.datatypes.Bool(isHardWallet)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> bytes32ToString(byte[] _bytes32, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_BYTES32TOSTRING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_bytes32)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<Boolean> whitelist(String param0, String param1) {
        final Function function = new Function(FUNC_WHITELIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.Address(160, param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> getAddress(byte[] b) {
        final Function function = new Function(FUNC_GETADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(b)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> fromNibbleToString(BigInteger c) {
        final Function function = new Function(FUNC_FROMNIBBLETOSTRING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(c)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public List<SenderEventResponse> getSenderEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SENDER_EVENT, transactionReceipt);
        ArrayList<SenderEventResponse> responses = new ArrayList<SenderEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SenderEventResponse typedResponse = new SenderEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SenderEventResponse> senderEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, SenderEventResponse>() {
            @Override
            public SenderEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SENDER_EVENT, log);
                SenderEventResponse typedResponse = new SenderEventResponse();
                typedResponse.log = log;
                typedResponse._sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SenderEventResponse> senderEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SENDER_EVENT));
        return senderEventFlowable(filter);
    }

    @Deprecated
    public static TxRelay load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TxRelay(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TxRelay load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TxRelay(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TxRelay load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TxRelay(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TxRelay load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TxRelay(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TxRelay> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TxRelay.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TxRelay> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TxRelay.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<TxRelay> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TxRelay.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TxRelay> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TxRelay.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class SenderEventResponse extends BaseEventResponse {
        public String _sender;
    }
}
