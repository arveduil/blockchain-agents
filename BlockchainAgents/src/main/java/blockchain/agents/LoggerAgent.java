package blockchain.agents;

import jade.core.Agent;

public class LoggerAgent extends Agent {
    private String serverIP;
    private String addBlockPath = "api/data/add/block";
    private String clientRequestPath = "api/data/add/client";
    protected void setup() {
//        Utils.log(getAID().getLocalName(), " is ready");
//        Object[] args = getArguments();
//
//        //Args:
//        //          args[0] = ip of server logger
//
//        if (args != null && args.length > 0){
//            serverIP = args[0].toString();
//        }
//
//        if(serverIP == null || serverIP.isEmpty()){
//            return;
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//
//        ClientRequestDto requestDto = new ClientRequestDto();
//        requestDto.Hash= "HASH2137";
//        requestDto.Type= ClientType.Client;
//        requestDto.Amount=  new BigDecimal("21.37");
//        requestDto.StartDate= new Date(System.currentTimeMillis());
//        requestDto.TransactionsHashes= new LinkedList<>();
//        requestDto.TransactionsHashes.add("TRANSACTIONHASH");
//        requestDto.MinedBlocksHashes= new LinkedList<>();
//        requestDto.MinedBlocksHashes.add("MINED_BLOCK_HASH");
//
//        Gson gson = new Gson();
//        String json = gson.toJson(requestDto);
//
//        try {
//            URL url = new URL(serverIP +"/"+ clientRequestPath);
//            HttpURLConnection con = (HttpURLConnection)url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/json; utf-8");
//            con.setRequestProperty("Accept", "application/json");
//            con.setDoOutput(true);
//            try(OutputStream os = con.getOutputStream()) {
//                byte[] input = json.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//            con.connect();
//
//            try(BufferedReader br = new BufferedReader(
//                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
//                StringBuilder response = new StringBuilder();
//                String responseLine = null;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                System.out.println(response.toString());
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
