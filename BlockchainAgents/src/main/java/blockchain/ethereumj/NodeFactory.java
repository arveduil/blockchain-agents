package blockchain.ethereumj;

import blockchain.ethereumj.config.Configs;
import org.apache.commons.lang3.tuple.Pair;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;

public class NodeFactory {

    public static DiscoveryNode createDiscoveryNode() {
        Ethereum ethereum = EthereumFactory.createEthereum(Configs.DiscoveryNodeConfig.class);

        return (DiscoveryNode) EthereumJNodesContainer.getNode("discoveryNode0");
    }

    public static synchronized BasicNode createRegularNode() {
        Pair<String, Class<?>> regularNodeConfig = Configs.getRegularNodeConfig();
        Ethereum ethereum = EthereumFactory.createEthereum(regularNodeConfig.getRight());

        return EthereumJNodesContainer.getNode(regularNodeConfig.getLeft());
    }

    public static synchronized MinerNode createMinerNode() {
        Pair<String, Class<?>> minerNodeConfig = Configs.getMinerNodeConfig();
        Ethereum ethereum = EthereumFactory.createEthereum(minerNodeConfig.getRight());

        return (MinerNode) EthereumJNodesContainer.getNode(minerNodeConfig.getLeft());
    }
}
