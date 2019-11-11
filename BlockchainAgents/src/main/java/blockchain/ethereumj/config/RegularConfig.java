package blockchain.ethereumj.config;

import blockchain.ethereumj.BasicNode;
import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

public class RegularConfig {
    private final String nodeName;
    private final String discoveryNode;

    private final int nodeIndex;

    public RegularConfig(int nodeIndex, String nodeName, String discoveryNode) {
        this.nodeIndex = nodeIndex;
        this.nodeName = nodeName;
        this.discoveryNode = discoveryNode;
    }

    @Bean
    public BasicSample node() {
        return new BasicNode(nodeName, nodeIndex);
    }

    @Bean
    public SystemProperties systemProperties() {
        return new SystemProperties(MyConfigFactory.getConfig(nodeIndex, nodeName, discoveryNode));
    }
}
