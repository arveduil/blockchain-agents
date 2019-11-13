package blockchain.behaviours;

import blockchain.agents.ClientAgent;
import blockchain.currencies.Ethereum;
import blockchain.utils.MessageContent;
import blockchain.utils.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigDecimal;

public class SendOfferForSaleBehaviour extends CyclicBehaviour {
    private ClientAgent agentWithWallet;

    public SendOfferForSaleBehaviour(ClientAgent a) {
        super(a);
        this.agentWithWallet = a;
    }

    public void action() {
        ACLMessage messageFromBuyer = getCallForProposalMessage();

        if (messageFromBuyer != null) {
            Utils.log(agentWithWallet,"Received CFP message from buyer " + messageFromBuyer.getSender().getLocalName());

            handleMessageFromBuyer(messageFromBuyer);
        }
        else {
            block();
        }
    }

    private ACLMessage getCallForProposalMessage() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        return myAgent.receive(mt);
    }

    private void handleMessageFromBuyer(ACLMessage msg) {
        Ethereum amountRequested = new Ethereum(msg.getContent());
        ACLMessage reply = msg.createReply();

        decorateReplyWithDecision(amountRequested, reply);

        myAgent.send(reply);
    }

    private void decorateReplyWithDecision(Ethereum amountRequested, ACLMessage reply) {
        Utils.log(agentWithWallet,"Amount requested: " + amountRequested + " amount in balance " + agentWithWallet.ethereumNode.getBalance());

        Utils.log(agentWithWallet,"Is synced "+ agentWithWallet.ethereumNode.isSynced );

        if (canAfford(amountRequested)){
            fillReplyWithPropose(reply);
        }
        else{
            fillReplyWithRefuse(reply);
        }
    }

    private void fillReplyWithRefuse(ACLMessage reply) {
        reply.setPerformative(ACLMessage.REFUSE);
        reply.setContent(MessageContent.NOT_ENOUGH_MONEY.toString());
        Utils.log(agentWithWallet,"Response REFUSE, current balance " + agentWithWallet.ethereumNode.getBalance());
    }

    private void fillReplyWithPropose(ACLMessage reply) {
        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent(String.valueOf(myAgent.getAID()));
        Utils.log(agentWithWallet,"Response PROPOSE");
    }

    private boolean canAfford(Ethereum amountRequested) {
        BigDecimal totalCost = (amountRequested.add(agentWithWallet.ethereumNode.getCurrentGasPrice()));
        Utils.log(agentWithWallet,"current blance: " + agentWithWallet.ethereumNode.getBalance()+ " total cost: " + totalCost.toString());
        return (agentWithWallet.ethereumNode.getBalance().compareTo(totalCost) != -1);
    }
}
