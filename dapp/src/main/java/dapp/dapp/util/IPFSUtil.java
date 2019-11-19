package dapp.dapp.util;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class IPFSUtil {
    protected  static Logger logger = LoggerFactory.getLogger(IPFSUtil.class);

    private static volatile IPFS ipfsClient;

    private static String ipfsEndPoint;

    private static String ipfsGateWay;

    private IPFSUtil() {

    }


    public static synchronized IPFS getIpfsClient() {
        if(ipfsClient == null) {
            synchronized (IPFSUtil.class) {
                if(ipfsClient == null) {
                    try {
                        ipfsClient = new IPFS(ipfsEndPoint);
                        logger.info("initate ipfs client" + ipfsEndPoint);
                    } catch ( Exception ex) {
                        logger.error("failed to initate ipfs" + ipfsEndPoint);
                    }
                }
            }
        }
        return  ipfsClient;
    }

    public static String getIpfsEndPoint() {
        return ipfsEndPoint;
    }

    @Value("${ipfs.endpoint}")
    public  void setIpfsEndPoint(String ipfsEndPoint) {
        IPFSUtil.ipfsEndPoint = ipfsEndPoint;
    }


    public static String getIpfsGateWay() {
        return ipfsGateWay;
    }

    @Value("${ipfs.gateWay}")
    public void setIpfsGateWay(String ipfsGateWay) {
        IPFSUtil.ipfsGateWay = ipfsGateWay;
    }

    public static String uploadIPFSFile(File file) throws IOException {
        NamedStreamable.FileWrapper fileStream = new NamedStreamable.FileWrapper(file);
        IPFS client = getIpfsClient();
        MerkleNode addResult = client.add(fileStream).get(0);
        return addResult.hash.toString();
    }


    public static String getIPFSLink(String keyFile) {
        return getIpfsGateWay() + "/" + keyFile;
    }


}
