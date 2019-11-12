package blockchain.ethereumj.config;

import blockchain.ethereumj.BasicNode;
import blockchain.ethereumj.DiscoveryNode;
import org.ethereum.config.SystemProperties;
import org.springframework.context.annotation.Bean;

public class DiscoveryConfig {

    private final int nodeIndex;
    private final String nodeName;
    private final String discoveryNode;

    public DiscoveryConfig(int index, String nodeName, String discoveryNode) {
        this.nodeIndex = index;
        this.nodeName = nodeName;
        this.discoveryNode = discoveryNode;
    }

    @Bean
    public BasicNode node() {
        return new DiscoveryNode(nodeName, nodeIndex);
    }

    @Bean
    public SystemProperties systemProperties() {
        return new SystemProperties(MyConfigFactory.getDiscoveryConfig(nodeIndex, nodeName, discoveryNode));
    }
}
