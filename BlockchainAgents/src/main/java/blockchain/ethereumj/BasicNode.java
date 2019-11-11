package blockchain.ethereumj;

import com.google.common.base.Joiner;
import org.ethereum.core.Block;
import org.ethereum.core.Transaction;
import org.ethereum.core.TransactionReceipt;
import org.ethereum.crypto.ECKey;
import org.ethereum.db.ByteArrayWrapper;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.net.rlpx.discover.NodeManager;
import org.ethereum.net.rlpx.discover.table.NodeEntry;
import org.ethereum.net.server.Channel;
import org.ethereum.net.server.ChannelManager;
import org.ethereum.samples.BasicSample;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.blockchain.EtherUtil;
import org.spongycastle.util.encoders.Hex;
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

    private int balance = 0;
    protected final List<String> otherNodesAddresses = new ArrayList<>();
    private final Queue<Transaction> submittedTransactions;

    private final String nodeName;

    protected Map<ByteArrayWrapper, TransactionReceipt> txWaiters = Collections.synchronizedMap(new HashMap<>());

    public BasicNode(String nodeName, int nodeIndex) {
       super(nodeName);
       this.nodeName = nodeName;
       submittedTransactions = new ConcurrentLinkedQueue<>();
    }

    @PostConstruct
    public void init() throws Exception {
        System.out.println(nodeName);
        System.out.println("Public Key: " + Hex.toHexString(getECKey().getAddress()));
        System.out.println("Private Key: " + getECKey().getPrivKey());
        EthereumJNodesContainer.add(nodeName, this);
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

    private void addNodeAddress(String hexId) {
        if (!otherNodesAddresses.contains(hexId)) {
            otherNodesAddresses.add(hexId);
        }
    }

    @Override
    public void onSyncDone() {
        ethereum.addListener(new EthereumListenerAdapter() {
            @Override
            public void onBlock(Block block, List<TransactionReceipt> receipts) {
                BasicNode.this.onBlock(block, receipts);
                logger.info("BALANCE: " + ethereum.getRepository().getBalance(getAddress()));
            }
        });
        logger.info("onSyncDone");
    }


    private void onBlock(Block block, List<TransactionReceipt> receipts) {
        for (TransactionReceipt receipt : receipts) {
            ByteArrayWrapper txHashW = new ByteArrayWrapper(receipt.getTransaction().getHash());
            if (txWaiters.containsKey(txHashW)) {
                txWaiters.put(txHashW, receipt);
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    }

    public void sendTransaction(byte[] receiveAddress, int cashAmount, byte[] data) {
        BigInteger nounce = ethereum.getRepository().getNonce(getECKey().getAddress());
        Transaction tx = new Transaction(
                ByteUtil.bigIntegerToBytes(nounce),
                ByteUtil.longToBytesNoLeadZeroes(ethereum.getGasPrice()),
                ByteUtil.longToBytesNoLeadZeroes(200000),
                receiveAddress,
                ByteUtil.bigIntegerToBytes(EtherUtil.convert(cashAmount, EtherUtil.Unit.WEI)),  // Use EtherUtil.convert for easy value unit conversion
                data,
                ethereum.getChainIdForNextBlock()
        );

        tx.sign(getECKey());
        logger.info("<=== Sending transaction: " + tx);
        ethereum.submitTransaction(tx);

        submittedTransactions.add(tx);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int newBalance) {
        this.balance = newBalance;
    }

    public byte[] getAddress() {
        return config.getMyKey().getAddress();
    }

    //public void addNewAddress(byte[] addr) {
    //    otherNodesAddresses.add(addr);
    //}

    public ECKey getECKey() {
        return config.getMyKey();
    }

    public void print() {
        System.out.println("=========================");
        System.out.println("=========================");
        System.out.println("=========================");
        System.out.println(getBalance());
        System.out.println("=========================");
        System.out.println("=========================");
        System.out.println("=========================");
    }
}
