package blockchain.agents;

import blockchain.behaviours.MiningBehaviour;
import blockchain.behaviours.PurchaseSellBehaviour;
import blockchain.behaviours.SendOfferForSaleBehaviour;
import blockchain.currencies.Ethereum;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.math.BigDecimal;

public class MinerAgent extends ClientAgent {
    private String name="";
    private int intervalMiliseconds = 1000;
    private Ethereum miningAmount = new Ethereum(20);

    protected void setup() {
        super.setup();

        BigDecimal miningIncome = new BigDecimal(20);
        BigDecimal sellOfferAmount = new BigDecimal(100);
        Object[] args = getArguments();

        //todo remove double registry after connecting EthereumJ
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

        addBehaviour(new MiningBehaviour(this,intervalMiliseconds,miningAmount));
        addBehaviour(new SendOfferForSaleBehaviour(this));
        addBehaviour(new PurchaseSellBehaviour(this));
    }
}
