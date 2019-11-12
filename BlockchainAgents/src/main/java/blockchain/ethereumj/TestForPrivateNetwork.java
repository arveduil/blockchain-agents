package blockchain.ethereumj;

import org.ethereum.samples.BasicSample;

import java.util.Set;

public class TestForPrivateNetwork {

    public static void main(String[] args) {
        //test1();
        test2();
    }

    private static void test2() {
        Set<String> hashes = ClientAddressProvider.getHashes();
        hashes.forEach(System.out::println);
    }

    private static void test1() {
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
}
