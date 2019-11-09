package blockchain.ethereumj.config;

import blockchain.ethereumj.DiscoveryNode;
import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

public class DiscoveryConfig {

    private final int nodeIndex;

    public DiscoveryConfig(int index) {
        this.nodeIndex = index;
    }

    @Bean
    public BasicSample node() {
        return new DiscoveryNode("discoveryNode-" + nodeIndex);
    }

    @Bean
    public SystemProperties systemProperties() {
        return new SystemProperties(MyConfigFactory.getConfig(nodeIndex, null));
    }
}
