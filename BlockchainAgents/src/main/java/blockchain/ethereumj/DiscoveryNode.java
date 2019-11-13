package blockchain.ethereumj;

import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.blockchain.EtherUtil;
import org.spongycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DiscoveryNode extends BasicNode {
    private final List<String> otherNodesAddresses = new ArrayList<>();

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

        Random rand = new Random(3225);
        for (int i = ethereum.getRepository().getNonce(senderKey.getAddress()).intValue(), j = 0; j < 20000; i++, j++) {
            {
                byte[] receiverAddr = getRandomAddress();
                Transaction tx = new Transaction(ByteUtil.intToBytesNoLeadZeroes(i),
                        ByteUtil.longToBytesNoLeadZeroes(ethereum.getGasPrice()),
                        ByteUtil.longToBytesNoLeadZeroes(0xfffff),
                        receiverAddr,
                        ByteUtil.bigIntegerToBytes(EtherUtil.convert(rand.nextInt(9) + 1, EtherUtil.Unit.ETHER)),
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

    public void addNodeAddress(String hexId) {
        if (!otherNodesAddresses.contains(hexId)) {
            otherNodesAddresses.add(hexId);
        }
    }

    Random rand = new Random();
    private byte[] getRandomAddress() {
        byte[] address;
        do {
            address = Hex.decode(otherNodesAddresses.get(rand.nextInt(otherNodesAddresses.size())));
        } while (Arrays.equals(address, getMyAddress()));

        return address;
    }
}
