package blockchain.behaviours;

import blockchain.agents.MinerAgent;
import blockchain.currencies.Ethereum;
import blockchain.dto.BlockRequestDto;
import blockchain.ethereumj.MinerNode;
import blockchain.utils.Utils;
import jade.core.behaviours.TickerBehaviour;

import java.util.List;

public class MiningBehaviour extends TickerBehaviour {
    private MinerAgent minerAgent;
    private Ethereum income;
    public MiningBehaviour(MinerAgent a, long period, Ethereum income) {
        super(a, period);
        this.minerAgent = a;
        this.income = income;
    }

    @Override
    protected void onTick() {
        List<BlockRequestDto> minedBlocks = ((MinerNode) minerAgent.ethereumNode).getMinedBlocks();
        if(!minedBlocks.isEmpty()){
            minerAgent.logBlocks(minedBlocks);
            Utils.log(minerAgent, "Miner sent blocks with size " + minedBlocks.size());
        }
    }
}
