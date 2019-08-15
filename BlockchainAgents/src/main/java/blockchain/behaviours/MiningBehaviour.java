package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.utils.Utils;
import jade.core.behaviours.TickerBehaviour;

import java.math.BigDecimal;

public class MiningBehaviour extends TickerBehaviour {
    private AgentWithWallet minerAgent;
    private BigDecimal income;
    public MiningBehaviour(AgentWithWallet a, long period, BigDecimal income) {
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
