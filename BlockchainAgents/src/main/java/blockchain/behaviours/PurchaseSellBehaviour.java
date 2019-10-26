package blockchain.behaviours;

import blockchain.agents.ClientAgent;
import blockchain.currencies.Ethereum;
import blockchain.utils.Utils;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PurchaseSellBehaviour extends Behaviour {
    private ClientAgent clientAgent;
    private boolean finished = false;
    public PurchaseSellBehaviour(ClientAgent clientAgent){
        super(clientAgent);

        this.clientAgent = clientAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            Ethereum amountToSell = new Ethereum(msg.getContent());
            ACLMessage reply = msg.createReply();
            if(clientAgent.getWalletState().compareTo(amountToSell) != -1){
                reply.setPerformative(ACLMessage.INFORM);

                clientAgent.substractFromWallet(amountToSell);
                reply.setContent("money substracted");

                Utils.log(clientAgent,"Giving money to " + msg.getSender().getLocalName());
                Utils.logWalletState(this.clientAgent);
            }else{
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("money not-available");
                Utils.log(clientAgent,"Cannot give money to " + msg.getSender().getLocalName());
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
