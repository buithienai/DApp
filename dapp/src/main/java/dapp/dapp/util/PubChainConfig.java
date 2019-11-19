package dapp.dapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class PubChainConfig {

    private  static final Logger logger = LoggerFactory.getLogger(PubChainConfig.class);


    private static volatile PubChainConfig publicChainConfig;
    private int chainId;
    private String chainName;
    private String description;
    private String addressNode;

    private PubChainConfig() {
        /// run and check configuration with database
       /* chainId = chainProperties.getChainId();
        chainName = chainProperties.getChainName();
        description = chainProperties.getDescription();
        addressNode = chainProperties.getNodeAddress();*/

    }

    public static synchronized PubChainConfig getInstance() {
        if(publicChainConfig == null) {
            synchronized (PubChainConfig.class) {
                if ( publicChainConfig == null) {
                    publicChainConfig = new PubChainConfig();
                }
            }
        }
        return publicChainConfig;
    }





    public static PubChainConfig getPublicChainConfig() {
        return publicChainConfig;
    }

    public static void setPublicChainConfig(PubChainConfig publicChainConfig) {
        PubChainConfig.publicChainConfig = publicChainConfig;
    }

    public int getChainId() {
        return chainId;
    }

    @Value("${chain.chainId}")
    public PubChainConfig setChainId(int chainId) {
        this.chainId = chainId;
        return this;
    }

    public String getChainName() {
        return chainName;
    }

    @Value("${chain.chainName}")
    public PubChainConfig setChainName(String chainName) {
        this.chainName = chainName;
        return this;
    }


    public String getDescription() {
        return description;
    }

    @Value("${chain.description}")
    public PubChainConfig setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAddressNode() {
        return addressNode;
    }

    @Value("${chain.nodeAddress}")
    public PubChainConfig setAddressNode(String addressNode) {
        this.addressNode = addressNode;
        return this;
    }


}
