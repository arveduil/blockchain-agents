package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.currency.Currency;
import blockchain.currency.Dollar;
import blockchain.currency.Ethereum;
import blockchain.utils.MessageContent;
import blockchain.utils.TransactionOffer;
import blockchain.utils.Utils;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;

public class ProceedTransactionBehaviour extends Behaviour {
    private int step = 0;
    private AID[] agentsWithSellOffer;
    private Ethereum amountToBuy;
    private MessageTemplate mt;
    private AgentWithWallet agent;
    private int repliesCount = 0;
    private TransactionOffer<Dollar,Ethereum> bestTransaction;

    public ProceedTransactionBehaviour(AgentWithWallet agent, Ethereum amountToBuy, AID[] agentsWithSellOffer) {
        super(agent);

        this.agent = agent;
        this.amountToBuy = amountToBuy;
        this.agentsWithSellOffer = agentsWithSellOffer;
    }

    @Override
    public void action() {
        switch (step) {
            case 0:
                ACLMessage cfp = createCfpMessageAsBuyOffer();
                myAgent.send(cfp);
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("blockchain"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                step = 1;
                break;
            case 1:
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null) {

                    getSellerFromProposeReply(reply);

                    step = 2;
                } else {
                    block();
                }
                break;
            case 2:
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

                decorateAcceptProposalOrderAsTransactionConfirmation(order);

                Utils.log(this.agent,"Send ACCEPT_PROPOSAL to " + bestTransaction.getOwnerAID().getLocalName());

                myAgent.send(order);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId(MessageContent.TRANSACTION_CONFIRMATION.toString()),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                step = 3;
                break;
            case 3:
                //TODO add receiving money when seller confirmed transaction
                reply = myAgent.receive(mt);
                if (reply != null) {
                    handleTransactionConfirmation(reply);
                    step = 4;
                }
                else {
                    block();
                }
                break;
        }
    }

    private ACLMessage createCfpMessageAsBuyOffer() {
        ACLMessage cfp = setupCfpMessageWithReceivers();

        //TODO SET TRANSACTION OFFER CONTENT
//        cfp.setContent(amountToBuy.toString());
        cfp.setConversationId("blockchain");
        cfp.setReplyWith("cfp" + System.currentTimeMillis());
        return cfp;
    }

    private ACLMessage setupCfpMessageWithReceivers() {
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        for (int i = 0; i < agentsWithSellOffer.length; ++i) {
            cfp.addReceiver(agentsWithSellOffer[i]);
        }
        return cfp;
    }

    private void getSellerFromProposeReply(ACLMessage reply) {
        repliesCount++;

        if (reply.getPerformative() == ACLMessage.PROPOSE) {
            try {
                TransactionOffer<Dollar, Ethereum> offer = (TransactionOffer<Dollar, Ethereum>) reply.getContentObject();


                if (bestTransaction == null) {
                    bestTransaction = offer;
                } else {
                    //TODO Check if client has enough money
                    if (bestTransaction.getRate().compareTo(offer.getRate()) > -1) {
                        bestTransaction = offer;
                    }
                }

                if (repliesCount >= agentsWithSellOffer.length) {
                    //all messages received go to step 2
                    step = 2;
                }

                Utils.log(agent.getLocalName(), "PROPOSE from seller" + reply.getSender().getLocalName());

        }catch(UnreadableException e){
                e.printStackTrace();
            }
        }
        else{
            Utils.log(agent.getLocalName(), "REFUSE from seller" + reply.getSender().getLocalName());
        }
        }


    private void decorateAcceptProposalOrderAsTransactionConfirmation(ACLMessage order) {
        order.addReceiver(bestTransaction.getOwnerAID());
        try {
            order.setContentObject(createReplyOffer(bestTransaction));
        } catch (IOException e) {
            e.printStackTrace();
        }
        order.setConversationId(MessageContent.TRANSACTION_CONFIRMATION.toString());
        order.setReplyWith("order"+System.currentTimeMillis());
    }

    private void handleTransactionConfirmation(ACLMessage reply) {
        if (reply.getPerformative() == ACLMessage.INFORM) {
            succesfullyReceivedAmount(reply);
        }
        else {
            Utils.log(agent.getLocalName(),"TransactionOffer with " + reply.getSender().getLocalName() +" failed.");
        }
    }

    private void succesfullyReceivedAmount(ACLMessage reply) {
        agent.addToWallet(amountToBuy);
        Utils.log(this.agent,"Received" + amountToBuy + " from " + reply.getSender().getLocalName());
        Utils.log(this.agent,"TransactionOffer finished successfully " + reply.getSender().getLocalName());
        Utils.logWalletState(this.agent);
    }

    @Override
        public boolean done() {
            if (step == 2 && bestTransaction == null) {
                Utils.log(this.agent,"Cannot find seller");
            }

            return ((step == 2 && bestTransaction == null) || step == 4);
        }

        private TransactionOffer<Ethereum,Dollar> createReplyOffer(TransactionOffer<Dollar,Ethereum> offer){
            return  new TransactionOffer<Ethereum, Dollar>(offer.getSellAmount(),offer.getBuyAmount(),this.agent.getAID());
        }
    }
