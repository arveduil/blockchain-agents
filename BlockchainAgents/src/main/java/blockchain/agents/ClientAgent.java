package blockchain.agents;

import blockchain.behaviours.BuyerInitialBehaviour;
import blockchain.currencies.Ethereum;
import blockchain.currencies.Wallet;
import blockchain.utils.ConstValues;
import blockchain.utils.Utils;
import jade.core.Agent;
import org.ethereum.vm.program.Program;

public class ClientAgent extends Agent {

    private Wallet wallet = new Wallet();
    private int intervalMiliseconds = 0;
    private Ethereum amountToGet = new Ethereum(0);
    private String  hostOrRemoteDfAddress;

    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");
        Object[] args = getArguments();

        //Args:
        //          args[0] = interval to ask for get
        //          args[1] = amount eth to get
        //          args[2] = host df ip address for remote or "HOST" for host

        if (args != null && args.length > 0){
            intervalMiliseconds = Integer.parseInt (args[0].toString());
            amountToGet = new Ethereum(args[1].toString());
            hostOrRemoteDfAddress = args[2].toString();
        }



        if(intervalMiliseconds != 0 && !amountToGet.equals(Ethereum.ZERO)){
            addBehaviour(new BuyerInitialBehaviour(this, intervalMiliseconds, amountToGet, hostOrRemoteDfAddress));
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

    public boolean isInHostPlatform(){
        return hostOrRemoteDfAddress.equals(ConstValues.HOST_VALUE);
    }

    public String getHostIp(){
        if(isInHostPlatform()){
            throw  new Program.IllegalOperationException("Cannot get host ip for host")
        }
        return hostOrRemoteDfAddress;
    }
}