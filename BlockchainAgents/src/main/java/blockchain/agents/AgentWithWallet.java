package blockchain.agents;


import jade.core.Agent;

import java.math.BigDecimal;

public class AgentWithWallet extends Agent {
    private BigDecimal wallet = BigDecimal.ZERO;

    public BigDecimal getWalletState(){
        return wallet;
    }

    public void setWallet(BigDecimal wallet){
        this.wallet = wallet;
    }

    public void addToWallet(BigDecimal money){
        this.wallet = wallet.add(money);
    }

    public void substractFromWallet(BigDecimal money){
        this.wallet = wallet.subtract(money);
    }
}
