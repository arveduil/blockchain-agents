package blockchain.behaviours;

import blockchain.agents.AgentWithWallet;
import blockchain.currency.Currency;
import blockchain.utils.Utils;
import jade.core.behaviours.TickerBehaviour;

import java.math.BigDecimal;

public class MiningBehaviour extends TickerBehaviour {
    private AgentWithWallet minerAgent;
    private Currency income;
    public MiningBehaviour(AgentWithWallet a, long period, Currency income) {
        super(a, period);
        this.minerAgent = a;
        this.income = income;
    }

    @Override
    protected void onTick() {
        minerAgent.addToWallet(income);

        Utils.log(minerAgent, "Miner mining, wallet: " + minerAgent.getWalletState(Currency.getCurrencyName()));
    }
}
