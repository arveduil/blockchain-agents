package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.utils.MessageContent;
import blockchain.utils.Utils;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.rmi.CORBA.Util;
import java.math.BigDecimal;

public class ProceedTransactionBehaviour extends Behaviour {
    private int step = 0;
    private AID[] agentsWithSellOffer;
    private BigDecimal amountToBuy;
    private MessageTemplate mt;
    private AID seller;
    private AgentWithWallet agent;

    public ProceedTransactionBehaviour(AgentWithWallet agent, BigDecimal amountToBuy, AID[] agentsWithSellOffer) {
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

                Utils.log(this.agent,"Send ACCEPT_PROPOSAL to " + seller.getLocalName());

                myAgent.send(order);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId(MessageContent.TRANSACTION_CONFIRMATION.toString()),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                step = 3;
                break;
            case 3:
                //confirm transaction
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
        cfp.setContent(amountToBuy.toString());
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
        if (reply.getPerformative() == ACLMessage.PROPOSE) {
            //int price = Integer.parseInt(reply.getContent());
            //TODO Gieda here
            seller = reply.getSender();

            Utils.log(agent.getLocalName(),"PROPOSE from seller" + reply.getSender().getLocalName());
        }else{
            Utils.log(agent.getLocalName(),"REFUSE from seller" + reply.getSender().getLocalName());
        }
    }

    private void decorateAcceptProposalOrderAsTransactionConfirmation(ACLMessage order) {
        order.addReceiver(seller);
        order.setContent(amountToBuy.toString());
        order.setConversationId(MessageContent.TRANSACTION_CONFIRMATION.toString());
        order.setReplyWith("order"+System.currentTimeMillis());
    }

    private void handleTransactionConfirmation(ACLMessage reply) {
        if (reply.getPerformative() == ACLMessage.INFORM) {
            succesfullyReceivedAmount(reply);
        }
        else {
            Utils.log(agent.getLocalName(),"Transaction with " + reply.getSender().getLocalName() +" failed.");
        }
    }

    private void succesfullyReceivedAmount(ACLMessage reply) {
        agent.addToWallet(amountToBuy);
        Utils.log(this.agent,"Received" + amountToBuy + " from " + reply.getSender().getLocalName());
        Utils.log(this.agent,"Transaction finished successfully " + reply.getSender().getLocalName());
        Utils.logWalletState(this.agent);
    }

    @Override
        public boolean done() {
            if (step == 2 && seller == null) {
                Utils.log(this.agent,"Cannot find seller");
            }

            return ((step == 2 && seller == null) || step == 4);
        }
    }
