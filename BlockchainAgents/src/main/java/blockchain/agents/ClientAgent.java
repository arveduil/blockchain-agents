package blockchain.agents;

import blockchain.behaviours.BuyerInitialBehaviour;
import blockchain.currencies.Ethereum;
import blockchain.currencies.Wallet;
import blockchain.dto.ClientRequestDto;
import blockchain.dto.ClientType;
import blockchain.dto.TransactionRequestDto;
import blockchain.utils.RemoteConnectionHandler;
import blockchain.utils.RequestType;
import blockchain.utils.Utils;
import com.google.gson.Gson;
import jade.core.Agent;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;

public class ClientAgent extends Agent {
    private static Gson gson = new Gson();
    private Wallet wallet = new Wallet();
    private int intervalMiliseconds = 0;
    private Ethereum amountToGet = new Ethereum(0);
    protected ClientType clientType;
    private String addBlockPath = "api/data/add/block";
    private String addClientRequestPath = "api/data/add/client";
    private String addTransactionRequestPath = "api/data/add/transaction";
    protected RemoteConnectionHandler remoteConnectionHandler;

    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");
        remoteConnectionHandler = RemoteConnectionHandler.getInstance();
        Object[] args = getArguments();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");

//        FileReader reader = null;
//        try {
//            reader = new FileReader("config.json");
//            JSONParser jsonParser = new JSONParser();
//            JSONObject configJson =  (JSONObject) jsonParser.parse(reader);
//            Config config = gson.fromJson( configJson.toString(), Config.class);
//        } catch (ParseException | IOException e) {
//            e.printStackTrace();
//        }


        //Args:
        //          args[0] = interval to ask for get
        //          args[1] = amount eth to get
        //          args[2] = Client or Miner

        if (args != null && args.length > 0){
            intervalMiliseconds = Integer.parseInt (args[0].toString());
            amountToGet = new Ethereum(args[1].toString());
            clientType =  ClientType.valueOf(args[2].toString());
        }

        logAddingClient();

        if(intervalMiliseconds != 0 && !amountToGet.equals(Ethereum.ZERO)){
            addBehaviour(new BuyerInitialBehaviour(this, intervalMiliseconds, amountToGet));
        }
    }

    public Ethereum getWalletState(){
        return wallet.getCurrentAmount();
    }

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
        ClientRequestDto requestDto = new ClientRequestDto();
        requestDto.MinedBlocksHashes = new LinkedList<>();
        requestDto.TransactionsHashes = new LinkedList<>();
        requestDto.Hash = this.getName();
        requestDto.Amount = (BigDecimal) wallet.getCurrentAmount();

        requestDto.StartDate =  new Date(System.currentTimeMillis());
        requestDto.TransactionsHashes= new LinkedList<>();
        //requestDto.TransactionsHashes.add("TRANSACTIONHASH");
        requestDto.MinedBlocksHashes= new LinkedList<>();
        //requestDto.MinedBlocksHashes.add("MINED_BLOCK_HASH");
        requestDto.Type = clientType;
        String json = gson.toJson(requestDto);
        String path = remoteConnectionHandler.getServerAddress() + "/" + addClientRequestPath;

        RemoteConnectionHandler.sendRequestToServer(path,json,RequestType.POST);
    }

    public void logTransaction(String source, String destinantion){
        TransactionRequestDto requestDto = TransactionRequestDto.getMock(source, destinantion);
        Gson gson = new Gson();
        String json = gson.toJson(requestDto);
        String path = remoteConnectionHandler.getServerAddress()  + "/" + addTransactionRequestPath;
        RemoteConnectionHandler.sendRequestToServer(path,json,RequestType.POST);
    }
}