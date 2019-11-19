package dapp.dapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

@Component
public class PubWeb3 {

    private static final Logger logger = LoggerFactory.getLogger(PubWeb3.class);

    private static String pubBlockchainNode;


    private static volatile Web3j web3j;


    private PubWeb3() {

    }


    synchronized static  private void initiateWeb3() {
        try {
            web3j = Web3j.build(new HttpService(pubBlockchainNode));
            logger.info("Connect to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
        } catch (IOException e ) {
            logger.error("Fail to initate the public web3 object with endpoint node: " + pubBlockchainNode + "error message:" + e.toString() );
        }
    }

    public static synchronized Web3j getInstance() {
        if(web3j == null) {
            synchronized (PubWeb3.class) {
                if (null == web3j) {
                    try {
                        web3j = Web3j.build(new HttpService(pubBlockchainNode));
                        logger.info("Connect to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
                    } catch (IOException e ) {
                        logger.error("Fail to initate the public web3 object with endpoint node: " + pubBlockchainNode + "error message:" + e.toString() );
                    }
                }
            }
        }
        return web3j;
    }

    public static String getPubBlockchainNode() {
        return pubBlockchainNode;
    }

    @Value("${chain.nodeAddress}")
    public void setPubBlockchainNode(String pubBlockchainNode) {
        PubWeb3.pubBlockchainNode = pubBlockchainNode;
    }
}
