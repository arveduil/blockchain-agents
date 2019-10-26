package blockchain.ethereumj;

import com.google.common.base.Joiner;
import org.ethereum.net.rlpx.discover.NodeManager;
import org.ethereum.net.rlpx.discover.table.NodeEntry;
import org.ethereum.net.server.Channel;
import org.ethereum.net.server.ChannelManager;
import org.ethereum.samples.BasicSample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

public class BasicNode extends BasicSample {
    @Autowired
    ChannelManager channelManager;

    @Autowired
    NodeManager nodeManager;

    public BasicNode(String nodeName) {
       super(nodeName);
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
}
