package blockchain.ethereumj;

import blockchain.utils.RemoteConnectionHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClientAddressProvider {

    private static final String ADDRESSES_ENDPOINT = "http://localhost:51419/api/blockchain/clients";

    public static Set<String> getHashes() {
        String json = RemoteConnectionHandler.getServerAddressFromIpServer(ADDRESSES_ENDPOINT);

        JsonArray asJsonArray = new JsonParser().parse(json).getAsJsonArray();

        Set<String> addresses = new LinkedHashSet<>();
        for (JsonElement jsonElement : asJsonArray) {
            JsonElement hash = jsonElement.getAsJsonObject().get("hash");
            addresses.add(hash.getAsString());
        }

        return addresses;
    }
}
