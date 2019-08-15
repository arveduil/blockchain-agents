package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.utils.Utils;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

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
        //tylko zlecenia kupna, ktore stanowia akceptacje oferty
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            BigDecimal amountToSell = new BigDecimal(msg.getContent());
            ACLMessage reply = msg.createReply();
            if(agentWithWallet.getWalletState().compareTo(amountToSell) != -1){
                reply.setPerformative(ACLMessage.INFORM);

                agentWithWallet.substractFromWallet(amountToSell);
                reply.setContent("money substracted");

                Utils.log(agentWithWallet,"Giving money to " + msg.getSender().getLocalName());
                Utils.logWalletState(this.agentWithWallet);
            }else{
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("money not-available");
                Utils.log(agentWithWallet,"Cannot give money to " + msg.getSender().getLocalName());
            }

            myAgent.send(reply);
        }
        else {
            block();
        }
    }

    public boolean done(){
        return finished;
    }
}
