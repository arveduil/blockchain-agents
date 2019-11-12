package blockchain.dto;

import org.ethereum.core.Block;
import org.ethereum.util.Utils;
import org.spongycastle.util.encoders.Hex;

import java.util.List;
import java.util.stream.Collectors;

public class BlockRequestDto {

    public List<TransactionRequestDto> transactionList;
    public String blockHash;
    public String parentHash;
    public long gasUsed;
    public long blockNumber;
    public String difficulty;
    public String timestamp;

    public static BlockRequestDto of(Block block) {
        BlockRequestDto dbBlock = new BlockRequestDto();

        dbBlock.blockHash = Hex.toHexString(block.getHash());
        dbBlock.gasUsed = block.getGasUsed();
        dbBlock.blockNumber = block.getNumber();
        dbBlock.parentHash = Hex.toHexString(block.getParentHash());
        dbBlock.timestamp = Utils.longToDateTime(block.getTimestamp());
        dbBlock.difficulty = Hex.toHexString(block.getDifficulty());

        dbBlock.transactionList = block.getTransactionsList().stream()
                .map(tx -> TransactionRequestDto.of(tx, dbBlock))
                .collect(Collectors.toList());

        return dbBlock;
    }
}
