package blockchain.agents;

import blockchain.behaviours.BuyerInitialBehaviour;
import blockchain.currencies.Ethereum;
import blockchain.currencies.Wallet;
import blockchain.dto.BlockRequestDto;
import blockchain.dto.ClientRequestDto;
import blockchain.dto.ClientType;
import blockchain.dto.TransactionRequestDto;
import blockchain.ethereumj.BasicNode;
import blockchain.ethereumj.NodeFactory;
import blockchain.ethereumj.config.Configs;
import blockchain.utils.RemoteConnectionHandler;
import blockchain.utils.RequestType;
import blockchain.utils.Utils;
import com.google.gson.Gson;
import jade.core.Agent;

import java.math.BigDecimal;
import java.util.List;

public class ClientAgent extends Agent {
    private static Gson gson = new Gson();
    private Wallet wallet = new Wallet();
    private int intervalMiliseconds = 0;
    private Ethereum amountToGet = new Ethereum(0);
    public ClientType clientType;
    public BasicNode ethereumNode;
    private String addBlockPath = "api/data/add/block";
    private String addClientRequestPath = "api/data/add/client";
    private String addTransactionRequestPath = "api/data/add/transaction";
    protected RemoteConnectionHandler remoteConnectionHandler;

    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");
        remoteConnectionHandler = RemoteConnectionHandler.getInstance();
        Configs.setDiscoveryIp(remoteConnectionHandler.getOnlyServerIp());

        Object[] args = getArguments();


        //Args:
        //          args[0] = interval to ask for get
        //          args[1] = amount eth to get
        //          args[2] = Client or Miner

        if (args != null && args.length > 0){
            intervalMiliseconds = Integer.parseInt (args[0].toString());
            amountToGet = new Ethereum(args[1].toString());
            clientType =  ClientType.valueOf(args[2].toString());
        }


        if(clientType == ClientType.Client){
            ethereumNode = NodeFactory.createRegularNode();
        } else {
            ethereumNode = NodeFactory.createMinerNode();
        }
        logAddingClient();

        addBehaviour(new BuyerInitialBehaviour(this, intervalMiliseconds, amountToGet));
    }

    public Ethereum getWalletState(){
        return wallet.getCurrentAmount();
    }

   // public Ethereum setWalletState(){
   //    return wallet.getCurrentAmount();
   // }


    public void addToWallet(Ethereum amount){
        wallet.addToWallet(amount);
    }

    public void substractFromWallet(Ethereum amount){
        wallet.substractFromWallet(amount);
    }

    public boolean hasInWallet(Ethereum amount){
       return wallet.contains(amount);
    }

    public void logAddingClient(){
        ClientRequestDto requestDto =  ClientRequestDto.of(this.ethereumNode,clientType == ClientType.Miner);
        String json = gson.toJson(requestDto);
        String path = remoteConnectionHandler.getServerAddress() + "/" + addClientRequestPath;
        Utils.log(getAID().getLocalName(), " has wallet hash " +requestDto.Hash);

        RemoteConnectionHandler.sendRequestToServer(path,json,RequestType.POST);
    }

    public void logTransaction(TransactionRequestDto transactionRequestDto){
        Gson gson = new Gson();
        String json = gson.toJson(transactionRequestDto);
        String path = remoteConnectionHandler.getServerAddress()  + "/" + addTransactionRequestPath;
        Utils.log(getAID().getLocalName(), " is sending transaction " +transactionRequestDto.Hash);

        RemoteConnectionHandler.sendRequestToServer(path,json,RequestType.POST);
    }

    public void logBlocks(List<BlockRequestDto> minedBlocks){
        for (BlockRequestDto blockDto : minedBlocks) {
            blockDto.MinerHash = ethereumNode.getAddressString();
            String json = gson.toJson(blockDto);
            String path = remoteConnectionHandler.getServerAddress()  + "/" + addBlockPath;
            RemoteConnectionHandler.sendRequestToServer(path,json,RequestType.POST);
            Utils.log(getAID().getLocalName(), " is sending block " +blockDto.Hash);

            for (TransactionRequestDto transactionRequestDto : blockDto.transactionList) {
                logTransaction(transactionRequestDto);
            }
        }
    }

    public void setWalletState(BigDecimal balance) {
        this.wallet.setEthAmount(new Ethereum(balance.toString()));
        logAddingClient();
    }
}