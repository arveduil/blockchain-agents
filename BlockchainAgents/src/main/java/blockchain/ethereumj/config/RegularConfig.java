package blockchain.ethereumj.config;

import blockchain.ethereumj.BasicNode;
import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

public class RegularConfig {
    private final String discoveryNode;

    private final int nodeIndex;

    public RegularConfig(int nodeIndex, String discoveryNode) {
        this.nodeIndex = nodeIndex;
        this.discoveryNode = discoveryNode;
    }

    @Bean
    public BasicSample node() {
        return new BasicNode("basicNode-" + nodeIndex);
    }

    @Bean
    public SystemProperties systemProperties() {
        return new SystemProperties(MyConfigFactory.getConfig(nodeIndex, discoveryNode));
    }
}
