package blockchain.dto;

import org.ethereum.core.Block;
import org.ethereum.util.Utils;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BlockRequestDto {

    public long Id;
    public String Hash;
    public Date MinedDate;
    public int TransactionCount;
    public double GasAmount;
    public BigDecimal AwardForMining;

    // set up by agent
    public String MinerHash;
    public String ParentHash;
    public List<TransactionRequestDto> transactionList;

    public List<String> TransactionHashes;
    public String Difficulty;

    public static BlockRequestDto of(Block block) {
        BlockRequestDto dbBlock = new BlockRequestDto();

        dbBlock.Hash = Hex.toHexString(block.getHash());
        dbBlock.GasAmount = block.getGasUsed();
        dbBlock.Id = block.getNumber();
        dbBlock.ParentHash = Hex.toHexString(block.getParentHash());
        try {
            dbBlock.MinedDate = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a").parse(Utils.longToDateTime(block.getTimestamp()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbBlock.Difficulty = Hex.toHexString(block.getDifficulty());
        dbBlock.AwardForMining = toBigDecimal(block.getCoinbase());

        dbBlock.transactionList = block.getTransactionsList().stream()
                .map(tx -> TransactionRequestDto.of(tx, dbBlock))
                .collect(Collectors.toList());
        dbBlock.TransactionCount = dbBlock.transactionList.size();
        dbBlock.TransactionHashes = dbBlock.transactionList.stream()
                .map(tx -> tx.Hash)
                .collect(Collectors.toList());

        return dbBlock;
    }

    private static BigDecimal toBigDecimal(byte[] bytes) {
        return new BigDecimal(new BigInteger(bytes).toString());
    }
}
