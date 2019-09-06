package blockchain.agents;


import blockchain.currency.Currency;
import blockchain.utils.Wallet;
import jade.core.Agent;

import java.math.BigDecimal;

public class AgentWithWallet extends Agent {
    private Wallet wallet = new Wallet();

    public <T extends Currency> T  getWalletState(String currencyName){
        return wallet.getAmount(currencyName);
    }

    public <T extends Currency> T  addToWallet(T amount){
       return wallet.addToWallet(amount);
    }

    public <T extends Currency> T  substractFromWallet(T amount){
        return wallet.substractFromWallet(amount);
    }

    public Boolean containsCurrency(String currencyName){
        return wallet.containsCurrency(currencyName);
    }

    public Boolean containsMoneyInCurrency(String currencyName){
        return wallet.containsMoneyInCurrency(currencyName);
    }
}
