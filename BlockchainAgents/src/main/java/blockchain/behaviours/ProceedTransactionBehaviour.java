package blockchain.behaviours;

import blockchain.agents.ClientAgent;
import blockchain.currencies.Ethereum;
import blockchain.utils.MessageContent;
import blockchain.utils.Utils;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import static blockchain.behaviours.BuyerState.INIT;

public class ProceedTransactionBehaviour extends Behaviour {
    private BuyerState state = INIT;
    private AID[] agentsWithSellOffer;
    private Ethereum amountToBuy;
    private MessageTemplate mt;
    private AID sellerChoosen;
    private ClientAgent agent;
    private int repliesCounter = 0;
    private ArrayList sellersWhoRespondedWithProposal;
    private BigDecimal walletStateBeforeTransaction;

    public ProceedTransactionBehaviour(ClientAgent agent, Ethereum amountToBuy, AID[] agentsWithSellOffer) {
        super(agent);

        this.agent = agent;
        this.amountToBuy = amountToBuy;
        this.agentsWithSellOffer = agentsWithSellOffer;
        this.sellersWhoRespondedWithProposal = new ArrayList();
    }

    @Override
    public void action() {
        switch (state) {
            case INIT:
                ACLMessage cfp = createCfpMessageAsBuyOffer();
                myAgent.send(cfp);
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("blockchain"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                state = BuyerState.COLLECTING_RESPONSES;
                break;
            case COLLECTING_RESPONSES:
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null) {
                    getSellerFromProposeReply(reply);
                } else {
                    block();
                }

                if(repliesCounter == agentsWithSellOffer.length){
                    if(sellersWhoRespondedWithProposal.isEmpty()){
                        state = BuyerState.END;
                        break;
                    } else {
                        int sellerIndexChoosen = new Random().nextInt(sellersWhoRespondedWithProposal.size());
                        sellerChoosen = (AID) sellersWhoRespondedWithProposal.get(sellerIndexChoosen);
                        state = BuyerState.SENDING_ACCEPT_PROPOSE_TO_SELLER;
                    }
                }
                break;
            case SENDING_ACCEPT_PROPOSE_TO_SELLER:
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);

                decorateAcceptProposalOrderAsTransactionConfirmation(order);

                Utils.log(this.agent,"Send ACCEPT "+ order.getContent() +" to " + sellerChoosen.getName());

                myAgent.send(order);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId(MessageContent.TRANSACTION_CONFIRMATION.toString()),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                walletStateBeforeTransaction = agent.ethereumNode.getBalance();
                state = BuyerState.FINALIZING_TRANSACTION;
                break;
            case FINALIZING_TRANSACTION:
                //confirm transaction receive
                //HERE RECEIVER ADD MONEY TO ACCOUNT
                reply = myAgent.receive(mt);
                BigDecimal currentWalletState = agent.ethereumNode.getBalance();
                Utils.log(agent.getLocalName(),"Waiting for transaction confirmation, current wallet " + currentWalletState.toString());

                if(currentWalletState.compareTo(walletStateBeforeTransaction) == 1){

                    Utils.log(agent.getLocalName(),"TRANSACTION NOTICED, current wallet " + currentWalletState.toString());

                    agent.setWalletState(currentWalletState);
                    state = BuyerState.END;
                }

//                if (reply != null) {
//                    handleTransactionConfirmation(reply);
//                    state = BuyerState.END;
//                }
//                else {
//                    block();
//                }
                break;
        }
    }

    private ACLMessage createCfpMessageAsBuyOffer() {
        ACLMessage cfp = setupCfpMessageWithReceivers();


        cfp.setContent( amountToBuy.toString());
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
        repliesCounter++;
        if (reply.getPerformative() == ACLMessage.PROPOSE) {
            sellersWhoRespondedWithProposal.add(reply.getSender());

            Utils.log(agent.getLocalName(),"PROPOSE from seller" + reply.getSender().getName());
        }else{
            Utils.log(agent.getLocalName(),"REFUSE from seller" + reply.getSender().getName());
        }
    }

    private void decorateAcceptProposalOrderAsTransactionConfirmation(ACLMessage order) {
        order.addReceiver(sellerChoosen);

        String clientHash = agent.ethereumNode.getAddressString();

        String msgContent = amountToBuy.toString() + ";"+ clientHash;

        order.setContent(msgContent);
        order.setConversationId(MessageContent.TRANSACTION_CONFIRMATION.toString());
        order.setReplyWith("order"+System.currentTimeMillis());
    }

    private void handleTransactionConfirmation(ACLMessage reply) {
        if (reply.getPerformative() == ACLMessage.INFORM) {
            succesfullyReceivedAmount(reply);
        }
        else {
            Utils.log(agent.getLocalName(),"Transaction with " + reply.getSender().getName() +" failed.");
        }
    }

    private void succesfullyReceivedAmount(ACLMessage reply) {
        agent.addToWallet(amountToBuy);
        //agent.logTransaction(agent.getName(), reply.getSender().getName());
        Utils.log(this.agent,"Received" + amountToBuy + " from " + reply.getSender().getName());
        Utils.log(this.agent,"Transaction finished successfully with" + reply.getSender().getName());
        Utils.logWalletState(this.agent);
    }

    @Override
        public boolean done() {
//            if ( state == BuyerState.COLLECTING_RESPONSES && sellerChoosen == null) {
//                Utils.log(this.agent,"Cannot find seller");
//            }

            return (state == BuyerState.END);
        }
    }
