package blockchain.agents;

import blockchain.behaviours.BuyerInitialBehaviour;
import blockchain.currencies.Ethereum;
import blockchain.currencies.Wallet;
import blockchain.dto.ClientRequestDto;
import blockchain.dto.ClientType;
import blockchain.utils.Config;
import blockchain.utils.Utils;
import com.google.gson.Gson;
import jade.core.Agent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;

public class ClientAgent extends Agent {
    private static Gson gson = new Gson();
    private Wallet wallet = new Wallet();
    private int intervalMiliseconds = 0;
    private Ethereum amountToGet = new Ethereum(0);
    private String  dfAgentIpAddress;
    protected ClientType clientType;
    private String  serverAddress;
    private String addBlockPath = "api/data/add/block";
    private String clientRequestPath = "api/data/add/client";

    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");
        Object[] args = getArguments();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");

        FileReader reader = null;
        try {
            reader = new FileReader("config.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject configJson =  (JSONObject) jsonParser.parse(reader);
            Config config = gson.fromJson( configJson.toString(), Config.class);
            this.dfAgentIpAddress = config.dfHostAddressIp;
            this.serverAddress = config.serverAddressIpPort;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


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

    public String getDfAgentIpAdress(){
        return dfAgentIpAddress;
    }

    public void logAddingClient(){
        ClientRequestDto requestDto = new ClientRequestDto();
        requestDto.MinedBlocksHashes = new LinkedList<>();
        requestDto.TransactionsHashes = new LinkedList<>();
        requestDto.Hash = wallet.getWalletHash();
        requestDto.Amount = (BigDecimal) wallet.getCurrentAmount();

        requestDto.StartDate =  new Date(System.currentTimeMillis());
        requestDto.TransactionsHashes= new LinkedList<>();
        //requestDto.TransactionsHashes.add("TRANSACTIONHASH");
        requestDto.MinedBlocksHashes= new LinkedList<>();
        //requestDto.MinedBlocksHashes.add("MINED_BLOCK_HASH");
        requestDto.Type = clientType;
        String json = gson.toJson(requestDto);
        sendRequestToServer(json,clientRequestPath);
    }

//    public void logCreatingTransaction(String transactionHash,){
//        TransactionRequestDto requestDto = new TransactionRequestDto();
//        requestDto.TransactionDate = new Date(System.currentTimeMillis());
//        requestDto.TransactionsHashes = new LinkedList<>();
//        requestDto.Hash = wallet.getWalletHash();
//        requestDto.Amount = (BigDecimal) wallet.getCurrentAmount();
//
//        requestDto.StartDate =  new Date(System.currentTimeMillis());
//        requestDto.TransactionsHashes= new LinkedList<>();
//        //requestDto.TransactionsHashes.add("TRANSACTIONHASH");
//        requestDto.MinedBlocksHashes= new LinkedList<>();
//        //requestDto.MinedBlocksHashes.add("MINED_BLOCK_HASH");
//        requestDto.Type = clientType;
//
//        Gson gson = new Gson();
//        String json = gson.toJson(requestDto);
//        sendRequestToServer(json,clientRequestPath);
//    }

    public void sendRequestToServer(String json, String path){
        try {
            URL url = new URL(serverAddress +"/"+ path);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            con.connect();

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                //System.out.println(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}