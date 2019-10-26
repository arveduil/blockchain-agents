package blockchain.agents;

import blockchain.behaviours.BuyerInitialBehaviour;
import blockchain.currencies.Ethereum;
import blockchain.currencies.Wallet;
import blockchain.utils.Utils;
import jade.core.Agent;

public class ClientAgent extends Agent {
    private Wallet wallet = new Wallet();
    private int intervalMiliseconds = 0;
    private Ethereum amountToGet = new Ethereum(0);

    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");
        Object[] args = getArguments();

        //Args:
        //          args[0] = interval to ask for get
        //          args[1] = amount eth to get

        if (args != null && args.length > 0){
            intervalMiliseconds = Integer.parseInt (args[0].toString());
            amountToGet = new Ethereum(args[1].toString());
        }

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
}