package blockchain.ethereumj;

import com.google.common.base.Joiner;
import org.ethereum.core.AccountState;
import org.ethereum.core.Transaction;
import org.ethereum.crypto.ECKey;
import org.ethereum.net.rlpx.discover.NodeManager;
import org.ethereum.net.rlpx.discover.table.NodeEntry;
import org.ethereum.net.server.Channel;
import org.ethereum.net.server.ChannelManager;
import org.ethereum.samples.BasicSample;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.blockchain.EtherUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BasicNode extends BasicSample {
    @Autowired
    ChannelManager channelManager;

    @Autowired
    NodeManager nodeManager;

    private byte[] privateKey;
    private AccountState account;
    private final List<byte[]> otherNodesAddresses;
    private final Queue<Transaction> submittedTransactions;

    public BasicNode(String nodeName) {
       super(nodeName);
        otherNodesAddresses = new ArrayList<>();
        submittedTransactions = new ConcurrentLinkedQueue<>();
    }

    @PostConstruct
    public void init() throws Exception {
        privateKey = config.getMyKey().getPrivKeyBytes();
        account = new AccountState(config);
    }

    {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                while (true) {
                    if (logger != null) {
                        Thread.sleep(15000);
                        if (channelManager != null) {
                            final Collection<Channel> activePeers = channelManager.getActivePeers();
                            final ArrayList<String> ports = new ArrayList<>();
                            for (Channel channel: activePeers) {
                                ports.add(channel.getInetSocketAddress().getHostName() + ":" + channel.getInetSocketAddress().getPort());
                            }

                            final Collection<NodeEntry> nodes = nodeManager.getTable().getAllNodes();
                            final ArrayList<String> nodesString = new ArrayList<>();
                            for (NodeEntry node: nodes) {
                                nodesString.add(node.getNode().getHost() + ":" + node.getNode().getPort() + "@" + node.getNode().getHexId().substring(0, 6) );
                            }

                            logger.info("channelManager.getActivePeers() " + activePeers.size() + " " + Joiner.on(", ").join(ports));
                            logger.info("nodeManager.getTable().getAllNodes() " + nodesString.size() + " " + Joiner.on(", ").join(nodesString));
                        } else {
                            logger.info("Channel manager is null");
                        }
                    } else {
                        System.err.println("Logger is null for ");
                    }
                }
            } catch (Exception e) {
                logger.error("Error checking peers count: ", e);
            }
        }).start();
    }

    @Override
    public void onSyncDone() {
        logger.info("onSyncDone");
    }

    public void sendTransaction(byte[] receiverAddress, long cashAmount, byte[] data) {
        byte[] fromAddress = ECKey.fromPrivate(privateKey).getAddress();
        BigInteger nonce = ethereum.getRepository().getNonce(fromAddress);
        Transaction tx = new Transaction(
                ByteUtil.bigIntegerToBytes(nonce),
                ByteUtil.longToBytesNoLeadZeroes(ethereum.getGasPrice()),
                ByteUtil.longToBytesNoLeadZeroes(20000),
                receiverAddress,
                ByteUtil.bigIntegerToBytes(EtherUtil.convert(cashAmount, EtherUtil.Unit.WEI)),
                data,
                ethereum.getChainIdForNextBlock()
        );

        tx.sign(ECKey.fromPrivate(privateKey));
        logger.info("<=== Sending transaction: " + tx);
        submittedTransactions.add(tx);
        ethereum.submitTransaction(tx);
    }

    public BigInteger getBalance() {
        return account.getBalance();
    }

    public byte[] getAddress() {
        return config.getMyKey().getAddress();
    }


    // getters for bigger objects
    public AccountState getAccount() {
        return account;
    }

    public ECKey getECKey() {
        return config.getMyKey();
    }
}
