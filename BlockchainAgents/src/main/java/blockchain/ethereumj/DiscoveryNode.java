package blockchain.ethereumj;

import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;

import java.util.Random;

public class DiscoveryNode extends BasicNode {
    public DiscoveryNode(String nodeName, int nodeIndex) {
        super(nodeName, nodeIndex);
    }

    @Override
    public void onSyncDone() {
        super.onSyncDone();
        new Thread(() -> {
            try {
                generateTransactions();
            } catch (Exception e) {
                logger.error("Error generating tx: ", e);
            }
        }).start();
    }

    private void generateTransactions() throws Exception{
        logger.info("Start generating transactions...");

        // the sender which some coins from the genesis
        ECKey senderKey = getECKey();
        byte[] receiverAddr = Hex.decode("2b29bea668b044b2b355c370f85b729bcb43ec40");

        for (int i = ethereum.getRepository().getNonce(senderKey.getAddress()).intValue(), j = 0; j < 20000; i++, j++) {
            {
                Transaction tx = new Transaction(ByteUtil.intToBytesNoLeadZeroes(i),
                        ByteUtil.longToBytesNoLeadZeroes(50_000_000_000L),
                        ByteUtil.longToBytesNoLeadZeroes(0xfffff),
                        receiverAddr,
                        new byte[]{77},
                        new byte[0],
                        ethereum.getChainIdForNextBlock()
                );
                tx.sign(senderKey);
                ethereum.submitTransaction(tx);
                logger.info("<== Submitting tx: " + tx);
            }
            Thread.sleep(7000);
        }
    }

    private String getRandomAddress() {
        Random rand = new Random();
        return otherNodesAddresses.get(rand.nextInt(otherNodesAddresses.size()));
    }
}
