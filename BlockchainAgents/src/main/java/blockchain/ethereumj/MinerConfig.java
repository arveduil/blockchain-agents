package blockchain.ethereumj;

import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

public class MinerConfig {
    private final String discoveryNode;

    private final int nodeIndex;

    public MinerConfig(int nodeIndex, String discoveryNode) {
        this.nodeIndex = nodeIndex;
        this.discoveryNode = discoveryNode;
    }

    @Bean
    public BasicSample node() {
        return new MinerNode("minerNode-" + nodeIndex);
    }

    @Bean
    public SystemProperties systemProperties() {
        return new SystemProperties(MyConfigFactory.getConfig(nodeIndex, discoveryNode));
    }
}
