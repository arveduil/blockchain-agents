package blockchain.ethereumj;

import java.util.HashMap;
import java.util.Map;

public class EthereumJNodesContainer {

    private static final Map<String, BasicNode> nodes = new HashMap<>();

    public static BasicNode getNode(String privateKey) {
        return nodes.get(privateKey);
    }

    public static void add(String privKey, BasicNode basicNode) {
        nodes.put(privKey, basicNode);
    }
}
