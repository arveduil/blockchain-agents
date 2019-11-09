package blockchain.ethereumj.config;

import blockchain.ethereumj.MinerNode;
import org.ethereum.config.SystemProperties;
import org.ethereum.samples.BasicSample;
import org.springframework.context.annotation.Bean;

public class MinerConfig {

    private final String discoveryNode;
    private final int nodeIndex;
    private final String extraData;

    public MinerConfig(int nodeIndex, String discoveryNode, String extraData) {
        this.nodeIndex = nodeIndex;
        this.discoveryNode = discoveryNode;
        this.extraData = extraData;
    }

    @Bean
    public BasicSample node() {
        return new MinerNode("minerNode-" + nodeIndex);
    }

    @Bean
    public SystemProperties systemProperties() {
        return new SystemProperties(MyConfigFactory.getMinerConfig(nodeIndex, discoveryNode, extraData));
    }
}
