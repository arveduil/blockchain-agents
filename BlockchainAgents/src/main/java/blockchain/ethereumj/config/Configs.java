package blockchain.ethereumj.config;

import blockchain.ethereumj.BasicNode;
import org.apache.commons.lang3.tuple.Pair;
import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Stack;

public class Configs {

    private static String discoveryIp = "192.168.43.187:35000";

    public static void setDiscoveryIp(String discoveryIp) {
        discoveryIp = discoveryIp;
    }

    private static Stack<Pair<String, Class<?>>> regularNodes = new Stack<>();
    private static Stack<Pair<String, Class<?>>> miners = new Stack<>();

    static {
        regularNodes.addAll(Arrays.asList(
                Pair.of(Node1Config.nodeName, Node1Config.class),
                Pair.of(Node2Config.nodeName, Node2Config.class),
                Pair.of(Node3Config.nodeName, Node3Config.class),
                Pair.of(Node4Config.nodeName, Node4Config.class),
                Pair.of(Node5Config.nodeName, Node5Config.class)
        ));
        miners.addAll(Arrays.asList(
                Pair.of(MinerNode1Config.nodeName, MinerNode1Config.class),
                Pair.of(MinerNode2Config.nodeName, MinerNode2Config.class),
                Pair.of(MinerNode3Config.nodeName, MinerNode3Config.class),
                Pair.of(MinerNode4Config.nodeName, MinerNode4Config.class),
                Pair.of(MinerNode5Config.nodeName, MinerNode5Config.class)
        ));
    }

    public static Pair<String, Class<?>> getRegularNodeConfig() {
        return regularNodes.pop();
    }

    public static Pair<String, Class<?>> getMinerNodeConfig() {
        return miners.pop();
    }

    public static class DiscoveryNodeConfig extends DiscoveryConfig {
        public DiscoveryNodeConfig() {
            super(0, "discoveryNode", discoveryIp);
        }
        @Bean
        public SystemProperties systemProperties() {
            return super.systemProperties();
        }
        @Bean
        public BasicNode node() {
            return super.node();
        }
    }

    // regular nodes
    public static class Node1Config extends RegularConfig {
        private static String nodeName = "regularNode1";

        public Node1Config() {
            super(1, nodeName, discoveryIp);
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

    public static class Node2Config extends RegularConfig {
        private static String nodeName = "regularNode2";

        public Node2Config() {
            super(2, nodeName, discoveryIp);
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

    public static class Node3Config extends RegularConfig {
        private static String nodeName = "regularNode3";

        public Node3Config() {
            super(3, nodeName, discoveryIp);
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

    public static class Node4Config extends RegularConfig {
        private static String nodeName = "regularNode4";

        public Node4Config() {
            super(4, nodeName, discoveryIp);
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

    public static class Node5Config extends RegularConfig {
        private static String nodeName = "regularNode5";

        public Node5Config() {
            super(5, nodeName, discoveryIp);
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

    // miner nodes
    public static class MinerNode1Config extends MinerConfig {
        private static String nodeName = "minerNode6";

        public MinerNode1Config() {
            super(6, nodeName, discoveryIp, "ccccccccccccccccccca");
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

    public static class MinerNode2Config extends MinerConfig {
        private static String nodeName = "minerNode7";

        public MinerNode2Config() {
            super(7, nodeName, discoveryIp, "cccccccccccccccccccb");
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

    public static class MinerNode3Config extends MinerConfig {
        private static String nodeName = "minerNode8";

        public MinerNode3Config() {
            super(8, nodeName, discoveryIp, "cccccccccccccccccccc");
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

    public static class MinerNode4Config extends MinerConfig {
        private static String nodeName = "minerNode9";

        public MinerNode4Config() {
            super(9, nodeName, discoveryIp, "cccccccccccccccccccd");
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

    public static class MinerNode5Config extends MinerConfig {
        private static String nodeName = "minerNode10";

        public MinerNode5Config() {
            super(10 , nodeName, discoveryIp, "ccccccccccccccccccce");
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
