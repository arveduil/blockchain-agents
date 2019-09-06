package blockchain.utils;

import blockchain.currency.Currency;
import blockchain.currency.Dollar;
import blockchain.currency.Ethereum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

public class Wallet {
    private HashMap<String, Currency> currencies;

    public Wallet(){
        currencies = new HashMap<String,Currency>();
        currencies.put(Dollar.getCurrencyName(),new Dollar(0));
        currencies.put(Ethereum.getCurrencyName(),new Ethereum(0));
    }

    public <T extends Currency> T addToWallet(T amount){
      T currentAmount = (T) currencies.remove(amount.getClass());
       return (T) currencies.put(T.getCurrencyName() ,(T) currentAmount.add(amount));
    }

    public <T extends Currency> T substractFromWallet(T amount){
        T currentAmount = (T) currencies.remove(amount.getClass());
        return (T) currencies.put(T.getCurrencyName() ,(T) currentAmount.subtract(amount));
    }

    public <T extends Currency> T getAmount(String currencyName){
        return  (T) currencies.get(currencyName);
    }

    public Boolean containsCurrency(String currencyName){
        return currencies.containsKey(currencyName);
    }

    public Boolean containsMoneyInCurrency(String currencyName){
        if(!this.containsCurrency(currencyName)) return false;
        return this.getAmount(currencyName).compareTo(BigDecimal.ZERO) > 0 ;
    }
}
