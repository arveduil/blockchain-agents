package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.utils.MessageContent;
import blockchain.utils.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigDecimal;

public class SendOfferForSaleBehaviour extends CyclicBehaviour {
    private BigDecimal amountToOffer;
    private AgentWithWallet agentWithWallet;

    public SendOfferForSaleBehaviour(AgentWithWallet a, BigDecimal amountToOffer) {
        super(a);
        this.amountToOffer = amountToOffer;
        this.agentWithWallet = a;
    }

    public void action() {
        ACLMessage messageFromBuyer = getCallForProposalMessage();

        if (messageFromBuyer != null) {
            //this.agentWithWallet.addBehaviour(new LoggingBehaviour(agentWithWallet,"Received message from buyer: " + messageFromBuyer.getSender()));
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
        BigDecimal amountRequested = new BigDecimal(msg.getContent());
        ACLMessage reply = msg.createReply();

        decorateReplyWithDecision(amountRequested, reply);

        myAgent.send(reply);
    }

    private void decorateReplyWithDecision(BigDecimal amountRequested, ACLMessage reply) {
        Utils.log(agentWithWallet,"Amount requested: " + amountRequested + " amount to offer " + agentWithWallet.getWalletState());

        //TODO change condition
        if (requestedAmountIsLessThanOffer(amountRequested)){
            fillReplyWithPropose(reply);
        }
        else{
            fillReplyWithRefuse(reply);
        }
    }

    private void fillReplyWithRefuse(ACLMessage reply) {
        reply.setPerformative(ACLMessage.REFUSE);
        reply.setContent(MessageContent.NOT_ENOUGH_MONEY.toString());
        Utils.log(agentWithWallet,"Response REFUSE");

        //this.agentWithWallet.addBehaviour(new LoggingBehaviour(agentWithWallet,"Response REFUSE"));
    }

    private void fillReplyWithPropose(ACLMessage reply) {
        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent(String.valueOf(myAgent.getAID()));

        Utils.log(agentWithWallet,"Response PROPOSE");
    }

    private boolean requestedAmountIsLessThanOffer(BigDecimal amountRequested){
        return amountRequested.compareTo(amountToOffer) != 1;
    }

    //TODO add to condition whether or not make decision
    private boolean agentHasNotEnoughMoneyInWalletToOffer(){
        return agentWithWallet.getWalletState().compareTo(this.amountToOffer) == -1;
    }
}
