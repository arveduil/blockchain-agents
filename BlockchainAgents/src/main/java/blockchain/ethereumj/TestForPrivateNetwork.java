package blockchain.ethereumj;

import org.ethereum.config.SystemProperties;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

public class TestForPrivateNetwork {

    public static void main(String[] args) {
        BasicSample.sLogger.info("Starting main node to which others will connect to");
        EthereumFactory.createEthereum(Node0Config.class);

        BasicSample.sLogger.info("Starting regular instance 1!");
        EthereumFactory.createEthereum(Node1Config.class);

        BasicSample.sLogger.info("Starting regular instance 2!");
        EthereumFactory.createEthereum(Node2Config.class);
    }


    private static class Node0Config extends RegularConfig {
        public Node0Config() {
            super(0, null);
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
            super(1, "127.0.0.1:20000");
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

    private static class Node2Config extends RegularConfig{
        public Node2Config() {
            super(2, "127.0.0.1:20000");
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
