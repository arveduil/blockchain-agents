package blockchain.ethereumj;

import blockchain.ethereumj.config.DiscoveryConfig;
import blockchain.ethereumj.config.MinerConfig;
import blockchain.ethereumj.config.RegularConfig;
import org.ethereum.config.SystemProperties;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestForPrivateNetwork {

    private static String discoveryIp = "127.0.0.1:35000";

    public static void main(String[] args) {
        BasicSample.sLogger.info("Starting main node to which others will connect to");
        EthereumFactory.createEthereum(Node0Config.class);

        BasicSample.sLogger.info("Starting regular instance 1!");
        EthereumFactory.createEthereum(Node1Config.class);

        BasicSample.sLogger.info("Starting miner instance!");
        EthereumFactory.createEthereum(MinerNode1Config.class);
        EthereumFactory.createEthereum(MinerNode2Config.class);
    }

    static InetAddress localHost;
    static {
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static class Node0Config extends DiscoveryConfig {
        public Node0Config() {
            super(0);
        }
        @Bean
        public SystemProperties systemProperties() {
            return super.systemProperties();
        }
        @Bean
        public BasicSample node() {
            return super.node();
        }
    }

    private static class Node1Config extends RegularConfig {
        public Node1Config() {
            super(1, discoveryIp);
        }
        @Bean
        public SystemProperties systemProperties() {
            return super.systemProperties();
        }
        @Bean
        public BasicSample node() {
            return super.node();
        }
    }

    private static class MinerNode1Config extends MinerConfig {
        public MinerNode1Config() {
            super(2, discoveryIp, "cccccccccccccccccccc");
        }
        @Bean
        public SystemProperties systemProperties() {
            return super.systemProperties();
        }
        @Bean
        public BasicSample node() {
            return super.node();
        }
    }
    private static class MinerNode2Config extends MinerConfig {
        public MinerNode2Config() {
            super(3, discoveryIp, "ccccccccccccccccccdd");
        }
        @Bean
        public SystemProperties systemProperties() {
            return super.systemProperties();
        }
        @Bean
        public BasicSample node() {
            return super.node();
        }
    }
}
