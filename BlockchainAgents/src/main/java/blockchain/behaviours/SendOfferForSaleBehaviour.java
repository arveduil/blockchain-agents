package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.currency.Currency;
import blockchain.currency.Dollar;
import blockchain.currency.Ethereum;
import blockchain.utils.MessageContent;
import blockchain.utils.TransactionOffer;
import blockchain.utils.Utils;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.math.BigDecimal;

public class SendOfferForSaleBehaviour extends CyclicBehaviour {
    private BigDecimal rate;
    private AgentWithWallet agentWithWallet;

    public SendOfferForSaleBehaviour(AgentWithWallet a, BigDecimal rate) {
        super(a);
        this.rate = rate;
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
        String currencyRequested = msg.getContent();
        ACLMessage reply = msg.createReply();
        Utils.log(agentWithWallet,"Currency requested: " + currencyRequested + " amount to offer " + agentWithWallet.getWalletState(currencyRequested));

        if(!this.agentWithWallet.containsMoneyInCurrency(currencyRequested)){
            fillReplyWithRefuse(reply);
        }


        fillReplyWithPropose(reply);

        myAgent.send(reply);
    }

    private void fillReplyWithRefuse(ACLMessage reply) {
        reply.setPerformative(ACLMessage.REFUSE);
        reply.setContent(MessageContent.NOT_ENOUGH_MONEY.toString());
        Utils.log(agentWithWallet,"Response REFUSE");

        //this.agentWithWallet.addBehaviour(new LoggingBehaviour(agentWithWallet,"Response REFUSE"));
    }

    private void fillReplyWithPropose(ACLMessage reply) {
        reply.setPerformative(ACLMessage.PROPOSE);

        try {
            reply.setContentObject(this.createTransactionOffer());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Utils.log(agentWithWallet,"Response PROPOSE");
    }

//    private boolean requestedAmountIsLessThanOffer(BigDecimal amountRequested){
//        return amountRequested.compareTo(amountToOffer) != 1;
//    }

//    //TODO add to condition whether or not make decision
//    private boolean agentHasNotEnoughMoneyInWalletToOffer(){
//        return agentWithWallet.getWalletState().compareTo(this.amountToOffer) == -1;
//    }

    private  TransactionOffer<Dollar, Ethereum> createTransactionOffer(){
        Ethereum sellAmount = this.agentWithWallet.getWalletState(Ethereum.getCurrencyName());
        Dollar buyAmount = (Dollar) this.rate.multiply(sellAmount);
        return new TransactionOffer<Dollar, Ethereum>(buyAmount, sellAmount, this.agentWithWallet.getAID());
    }
}
