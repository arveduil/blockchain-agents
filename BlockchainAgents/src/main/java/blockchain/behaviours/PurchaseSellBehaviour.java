package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.currency.Dollar;
import blockchain.currency.Ethereum;
import blockchain.utils.TransactionOffer;
import blockchain.utils.Utils;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.math.BigDecimal;

public class PurchaseSellBehaviour extends Behaviour {
    private AgentWithWallet agentWithWallet;
    private boolean finished = false;
    public PurchaseSellBehaviour(AgentWithWallet agentWithWallet){
        super(agentWithWallet);

        this.agentWithWallet = agentWithWallet;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            try {
                TransactionOffer<Ethereum,Dollar> offerConfirmationFromBuyer = (TransactionOffer<Ethereum,Dollar>) msg.getContentObject();

            ACLMessage reply = msg.createReply();
            if(agentWithWallet.getWalletState(Ethereum.getCurrencyName()).compareTo(offerConfirmationFromBuyer.getBuyAmount()) > -1){
                reply.setPerformative(ACLMessage.INFORM);

                agentWithWallet.substractFromWallet(offerConfirmationFromBuyer.getBuyAmount());
                reply.setContent("money substracted");

                Utils.log(agentWithWallet,"Giving money to " + msg.getSender().getLocalName());
                Utils.logWalletState(this.agentWithWallet);
            }else{
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("money not-available");
                Utils.log(agentWithWallet,"Cannot give money to " + msg.getSender().getLocalName());
            }

            myAgent.send(reply);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
        else {
            block();
        }
    }

    public boolean done(){
        return finished;
    }
}
