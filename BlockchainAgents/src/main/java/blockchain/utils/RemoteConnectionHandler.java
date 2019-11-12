package blockchain.utils;

import jade.core.AID;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteConnectionHandler {
    public static final String SEPARATOR = ":";
    public static final int DF_GUID_PORT_DEFAULT = 1099;
    public static final int DF_MTP_PORT_DEFAULT = 7778;
    public static final String AZURE_IP_SERVER_ADDRESS = "http://blockchainazureipserver.azurewebsites.net/api/SetIp";
    public static final String SERVER_PROTOCOL =  "http://";
    private  String serverIp;
    private  String serverPort;

    private static final RemoteConnectionHandler instance = new RemoteConnectionHandler();

    //private constructor to avoid client applications to use constructor
    private RemoteConnectionHandler(){
        setServerIpPort();
    }

    public static RemoteConnectionHandler getInstance(){
        return instance;
    }

    public  AID getRemoteDfAgent(){
        AID dfRemote =   new AID("df@"+serverIp+":"+ DF_GUID_PORT_DEFAULT  +"/JADE");
        dfRemote.addAddresses(SERVER_PROTOCOL +serverIp+":"+ DF_MTP_PORT_DEFAULT +"/acc");
        return  dfRemote;
    }

    public  void setServerIpPort(){
        //String response = sendRequestToServer(AZURE_IP_SERVER_ADDRESS,"",RequestType.GET);
        String response = null;
        try {
            response = getServerAddressFromIpServer(AZURE_IP_SERVER_ADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverIp = response.split(SEPARATOR)[0];
        serverPort = response.split(SEPARATOR)[1];
    }

    public String getOnlyServerIp(){
        return serverIp;
    }

    public String getServerAddress(){
        return SERVER_PROTOCOL + serverIp + SEPARATOR + serverPort;
    }

    public static String  sendRequestToServer(String path, String content, RequestType type){
        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod(type.toString());
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = content.getBytes("utf-8");
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
                return  response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getServerAddressFromIpServer(String urlToRead)  {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
