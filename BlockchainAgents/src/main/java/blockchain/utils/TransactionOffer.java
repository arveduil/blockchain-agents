package blockchain.utils;

import blockchain.currency.Currency;
import jade.core.AID;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionOffer<TBuy extends Currency,TSell extends Currency> implements Serializable {
    protected TBuy buyAmount;
    protected TSell sellAmount;
    protected AID  ownerAID;

    public TransactionOffer(TBuy buyAmount, TSell sellAmount, AID  ownerAID) {
        this.buyAmount = buyAmount;
        this.sellAmount = sellAmount;
        this.ownerAID = ownerAID;
    }

    public BigDecimal getRate(){
        return sellAmount.divide(buyAmount);
    }

    public TBuy getBuyAmount() {
        return buyAmount;
    }

    public TSell getSellAmount() {
        return sellAmount;
    }

    public AID getOwnerAID() {
        return ownerAID;
    }


}
