package blockchain.utils;

import jade.core.AID;

public class RemoteConnectionUtils {
    public static final int DF_GUID_PORT_DEFAULT = 1099;
    public static final int DF_MTP_PORT_DEFAULT = 7778;

    public static AID getRemoteDfAgent(String ip){
        AID dfRemote =   new AID("df@"+ip+":"+ DF_GUID_PORT_DEFAULT  +"/JADE");
        dfRemote.addAddresses("http://"+ip+":"+ DF_MTP_PORT_DEFAULT +"/acc");
        return  dfRemote;
    }
}
