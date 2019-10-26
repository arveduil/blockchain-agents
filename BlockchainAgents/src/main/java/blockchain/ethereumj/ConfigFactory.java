package blockchain.ethereumj;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.util.Arrays;

public class ConfigFactory {

    public static Config getConfig(int index, String discoveryNode) {
        return com.typesafe.config.ConfigFactory.empty()
                .withValue("peer.discovery.enabled", value(true))
                .withValue("peer.discovery.external.ip", value("127.0.0.1"))
                .withValue("peer.discovery.bind.ip", value("127.0.0.1"))
                .withValue("peer.discovery.persist", value("false"))

                .withValue("peer.listen.port", value(20000 + index))
                .withValue("peer.privateKey", value(Hex.toHexString(ECKey.fromPrivate(("" + index).getBytes()).getPrivKeyBytes())))
                .withValue("peer.networkId", value(555))
                .withValue("sync.enabled", value(true))
                .withValue("database.incompatibleDatabaseBehavior", value("RESET"))
                .withValue("genesis", value("sample-genesis.json"))
                .withValue("database.dir", value("sampleDB-" + index))
                .withValue("peer.discovery.ip.list", value(discoveryNode != null ? Arrays.asList(discoveryNode) : Arrays.asList()));
    }

    private static ConfigValue value(Object value) {
        return ConfigValueFactory.fromAnyRef(value);
    }
}
