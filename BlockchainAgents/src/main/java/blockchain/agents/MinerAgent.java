package blockchain.agents;

import blockchain.behaviours.MiningBehaviour;
import blockchain.behaviours.PurchaseSellBehaviour;
import blockchain.behaviours.SendOfferForSaleBehaviour;
import blockchain.utils.Utils;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.math.BigDecimal;

public class MinerAgent extends AgentWithWallet {
    private String name="";


    protected void setup() {
        Utils.log(getAID().getLocalName(), " is ready");
        int interval = 2000;
        BigDecimal miningIncome = new BigDecimal(20);
        BigDecimal sellOfferAmount = new BigDecimal(100);
        //Args: 0 mining interval, 1 mining income,
        Object[] args = getArguments();
        if (args != null && args.length > 0){
            interval = Integer.parseInt (args[0].toString());
            miningIncome = new BigDecimal (args[1].toString());
            sellOfferAmount = new BigDecimal (args[2].toString());
        }

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("blockchain");
        sd.setName(getAID().getLocalName() );
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new MiningBehaviour(this,interval,miningIncome));
        addBehaviour(new SendOfferForSaleBehaviour(this,sellOfferAmount));
        addBehaviour(new PurchaseSellBehaviour(this));

    }
}
