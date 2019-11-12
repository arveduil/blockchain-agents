package blockchain.ethereumj;

public class DiscoveryServer implements Runnable {

    public static void main(String[] args) {
        new Thread(new DiscoveryServer()).start();
    }

    @Override
    public void run() {
        DiscoveryNode discoveryNode = NodeFactory.createDiscoveryNode();
        while (true) {
            ClientAddressProvider.getHashes().forEach(discoveryNode::addNodeAddress);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
