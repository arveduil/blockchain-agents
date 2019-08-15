package blockchain.agents;

import blockchain.behaviours.BuyerInitialBehaviour;
import blockchain.utils.Utils;
import jade.core.AID;


import java.math.BigDecimal;

public class ClientAgent extends AgentWithWallet {
    private BigDecimal desiredAmount = new BigDecimal(80);

    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");

        int interval = 5000;
        //Args: 0 interval
        Object[] args = getArguments();
        if (args != null && args.length > 0){
            interval = Integer.parseInt (args[0].toString());
        }

        addBehaviour(new BuyerInitialBehaviour(this, interval,desiredAmount));
    }
}
