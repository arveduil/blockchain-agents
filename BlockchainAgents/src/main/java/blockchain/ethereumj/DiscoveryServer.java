package blockchain.ethereumj;

import blockchain.dto.ClientRequestDto;
import blockchain.utils.RemoteConnectionHandler;
import blockchain.utils.RequestType;
import com.google.gson.Gson;

public class DiscoveryServer implements Runnable {

    private String ADD_CLIENT_PATH = "api/data/add/client";

    public static void main(String[] args) {
        new Thread(new DiscoveryServer()).start();
    }

    @Override
    public void run() {
        DiscoveryNode discoveryNode = NodeFactory.createDiscoveryNode();
        while (true) {
            ClientAddressProvider.getHashes().forEach(discoveryNode::addNodeAddress);
            ClientRequestDto request = ClientRequestDto.of(discoveryNode, false);

            String json = new Gson().toJson(request);
            String path = RemoteConnectionHandler.getInstance().getServerAddress() + "/" + ADD_CLIENT_PATH;
            RemoteConnectionHandler.sendRequestToServer(path,json, RequestType.POST);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
