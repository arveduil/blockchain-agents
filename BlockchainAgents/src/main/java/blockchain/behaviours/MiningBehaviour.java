package blockchain.behaviours;

import blockchain.agents.ClientAgent;
import blockchain.currencies.Ethereum;
import blockchain.utils.Utils;
import jade.core.behaviours.TickerBehaviour;

public class MiningBehaviour extends TickerBehaviour {
    private ClientAgent minerAgent;
    private Ethereum income;
    public MiningBehaviour(ClientAgent a, long period, Ethereum income) {
        super(a, period);
        this.minerAgent = a;
        this.income = income;
    }

    @Override
    protected void onTick() {

        minerAgent.addToWallet(income);

        Utils.log(minerAgent, "Miner mining, wallet: " + minerAgent.getWalletState());
    }
}
