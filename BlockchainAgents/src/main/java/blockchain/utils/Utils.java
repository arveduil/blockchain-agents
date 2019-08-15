package blockchain.utils;

import blockchain.agents.AgentWithWallet;
import jade.core.Agent;

import java.time.Instant;

public  class Utils {
    //TODO Change to normal log4j class

    public static void log(String author, String message){
        System.out.println(Instant.now() + "|" + author+":"+ message);
    }

    public static void log(Agent author, String message){
        Utils.log(author.getLocalName(),message);
    }

    public static void logWalletState(AgentWithWallet authorAgent){
        Utils.log(authorAgent,"Wallet state: " + authorAgent.getWalletState());
    }
}
