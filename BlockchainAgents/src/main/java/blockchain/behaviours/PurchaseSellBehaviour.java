package blockchain.behaviours;

import blockchain.agents.ClientAgent;
import blockchain.currencies.Ethereum;
import blockchain.utils.Utils;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.spongycastle.util.encoders.Hex;

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
            String buyerHash = msg.getContent().split(";")[1];

            Ethereum amountToSell = new Ethereum(msg.getContent().split(";")[0]);
            ACLMessage reply = msg.createReply();
            if(clientAgent.getWalletState().add(clientAgent.ethereumNode.getCurrentGasPrice()).compareTo(amountToSell) != -1){
                //TU HASZ KUPCA
                byte[] receiverAddress = Hex.decode(buyerHash);
                clientAgent.ethereumNode.sendTransaction(receiverAddress,amountToSell.toBigInteger().intValue() ,new byte[] {});
                Utils.log(clientAgent,"Giving money to " + msg.getSender().getName() + " with hash " + buyerHash);

                reply.setPerformative(ACLMessage.INFORM);

                reply.setContent("money substracted");

                //Utils.log(clientAgent,"Giving money to " + msg.getSender().getLocalName());
              //  Utils.logWalletState(this.clientAgent);
            }else{
                reply.setPerformative(ACLMessage.FAILURE);
                reply.setContent("money not-available");
                //Utils.log(clientAgent,"Cannot give money to " + msg.getSender().getLocalName());
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
