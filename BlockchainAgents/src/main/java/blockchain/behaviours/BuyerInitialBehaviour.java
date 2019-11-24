package blockchain.behaviours;

import blockchain.agents.ClientAgent;
import blockchain.currencies.Ethereum;
import blockchain.dto.ClientType;
import blockchain.utils.RemoteConnectionHandler;
import blockchain.utils.Utils;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class BuyerInitialBehaviour extends TickerBehaviour {
    private static final Logger LOGGER = Logger.getLogger( BuyerInitialBehaviour.class.getName() );

    private ClientAgent clientAgent;
    private Ethereum desiredAmount;

    public BuyerInitialBehaviour(ClientAgent agentWithWallet, long period, Ethereum desiredAmount) {
        super(agentWithWallet, period);

        this.clientAgent = agentWithWallet;
        this.desiredAmount = desiredAmount;
        LOGGER.addHandler(new ConsoleHandler());
    }

    protected void onTick()
    {
        Utils.log(clientAgent.getName(),"BuyerInitial isSynced " + clientAgent.ethereumNode.isSynced);

        if(!clientAgent.ethereumNode.isSynced  && clientAgent.clientType != ClientType.Miner) return;

        clientAgent.setWalletState( clientAgent.ethereumNode.getBalance());

        //if(desiredAmount.compareTo(new Ethereum("0")) == 0) return;

        Utils.log(clientAgent.getName(),"Buyers looks for " + desiredAmount);
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("blockchain");
        template.addServices(sd);
        try
        {
            DFAgentDescription[] result;
            AID dfAgent = RemoteConnectionHandler.getInstance().getRemoteDfAgent();
            result = DFService.search(myAgent, dfAgent,template);

            StringBuilder logMessagaBuilder = new StringBuilder("Found " + result.length + " sellers: ");
            AID[] sellerAgents = new AID[result.length];
            for (int i = 0; i < result.length; ++i)
            {
                sellerAgents[i] = result[i].getName();
                logMessagaBuilder.append(" ");
                logMessagaBuilder.append(sellerAgents[i].getName());
            }
            Utils.log(clientAgent.getLocalName(),logMessagaBuilder.toString());

            myAgent.addBehaviour(new ProceedTransactionBehaviour(clientAgent,desiredAmount,sellerAgents));
        }
        catch (FIPAException fe)
        {
            fe.printStackTrace();
        }
    }
}
