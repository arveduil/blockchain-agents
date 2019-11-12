package blockchain.ethereumj.config;

import com.typesafe.config.*;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MyConfigFactory {

    public static Config getMinerConfig(int index, String nodeName, String discoveryNode, String extraData) {
        return getConfig(index, nodeName, discoveryNode)
                .withValue("mine.extraDataHex", value(extraData))
                .withValue("mine.cpuMineThreads", value(1))
                .withValue("cache.flush.blocks", value(1));
    }

    public static Config getConfig(int index, String nodeName, String discoveryNode) {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return createConfig(index, nodeName, discoveryNode, localHost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Config createConfig(int index, String nodeName, String discoveryNode, InetAddress localHost) {
        return ConfigFactory.empty()
                .withValue("peer.discovery.enabled", value(true))
                .withValue("peer.discovery.bind.ip", value(null))
                .withValue("peer.discovery.external.ip", value(null))
                .withValue("peer.discovery.persist", value("false"))

                .withValue("peer.listen.port", value(35000 + index))
                .withValue("peer.privateKey", value(Hex.toHexString(ECKey.fromPrivate((nodeName + localHost.getHostAddress()).getBytes()).getPrivKeyBytes())))
                .withValue("peer.networkId", value(3854))
                .withValue("sync.enabled", value(true))
                .withValue("database.incompatibleDatabaseBehavior", value("RESET"))
                .withValue("genesis", value("sample-genesis.json"))
                .withValue("database.dir", value("sampleDB-" + index))
                .withValue("peer.discovery.ip.list", value(discoveryNode != null ? Arrays.asList(discoveryNode) : Arrays.asList()));
    }

    public static Config getDiscoveryConfig(int index, String nodeName, String discoveryNode) {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ConfigFactory.empty()
                .withValue("peer.discovery.enabled", value(true))
                .withValue("peer.discovery.bind.ip", value(discoveryNode.split(":")[0]))
                .withValue("peer.discovery.external.ip", value(null))
                .withValue("peer.discovery.persist", value("false"))

                .withValue("peer.listen.port", value(35000 + index))
                .withValue("peer.privateKey", value(Hex.toHexString(ECKey.fromPrivate(nodeName.getBytes()).getPrivKeyBytes())))
                .withValue("peer.networkId", value(3854))
                .withValue("sync.enabled", value(true))
                .withValue("database.incompatibleDatabaseBehavior", value("RESET"))
                .withValue("genesis", value("sample-genesis.json"))
                .withValue("database.dir", value("sampleDB-" + index))
                .withValue("peer.discovery.ip.list", value(Arrays.asList()));

    }

    private static ConfigValue value(Object value) {
        return ConfigValueFactory.fromAnyRef(value);
    }
}
