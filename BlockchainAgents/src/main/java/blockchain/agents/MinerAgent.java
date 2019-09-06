package blockchain.agents;

import blockchain.behaviours.MiningBehaviour;
import blockchain.behaviours.PurchaseSellBehaviour;
import blockchain.behaviours.SendOfferForSaleBehaviour;
import blockchain.currency.Dollar;
import blockchain.currency.Ethereum;
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
        Ethereum miningIncome = new Ethereum(20);
        BigDecimal sellOfferRate = BigDecimal.ONE;
        //Args: 0 mining interval, 1 mining income,
        Object[] args = getArguments();
        if (args != null && args.length > 0){
            interval = Integer.parseInt (args[0].toString());
            miningIncome = new Ethereum(args[1].toString());
            sellOfferRate = new BigDecimal(args[2].toString());
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
        addBehaviour(new SendOfferForSaleBehaviour(this,sellOfferRate));
        addBehaviour(new PurchaseSellBehaviour(this));

    }
}
