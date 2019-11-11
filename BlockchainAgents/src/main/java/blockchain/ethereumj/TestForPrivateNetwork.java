package blockchain.ethereumj;

import org.ethereum.samples.BasicSample;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestForPrivateNetwork {

    private static String discoveryIp = "127.0.0.1:35000";

    public static void main(String[] args) {
        BasicSample.sLogger.info("Starting main node to which others will connect to");
        //EthereumFactory.createEthereum(Node0Config.class);
        DiscoveryNode discoveryNode = NodeFactory.createDiscoveryNode();

        BasicSample.sLogger.info("Starting regular instance 1!");
        //EthereumFactory.createEthereum(Node1Config.class);
        NodeFactory.createRegularNode();

        BasicSample.sLogger.info("Starting miner instance!");
       // EthereumFactory.createEthereum(MinerNode1Config.class);
        NodeFactory.createMinerNode();
        //EthereumFactory.createEthereum(MinerNode2Config.class);
        NodeFactory.createMinerNode();
    }

    static InetAddress localHost;
    static {
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
