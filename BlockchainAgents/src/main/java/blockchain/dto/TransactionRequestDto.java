package blockchain.dto;

import org.ethereum.core.Transaction;
import org.ethereum.util.ByteUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.server.UID;
import java.util.Date;

public class TransactionRequestDto {
    public String Hash;
    public Date TransactionDate;
    public BigDecimal MoneyAmount;
    public double GasAmount;
    public String SourceClientHash;
    public String DestinationClientHash;
    public String BlockHash;

    public static TransactionRequestDto getMock(String source, String destination){
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.Hash = new UID().toString();
        dto.TransactionDate = new Date(System.currentTimeMillis());
        dto.MoneyAmount = new BigDecimal("21.37");
        dto.GasAmount = 0.2137;
        dto.SourceClientHash = source;
        dto.DestinationClientHash = destination;
        dto.BlockHash = "BLOCK_CLIENT_HASH";

        return  dto;
    }

    public static TransactionRequestDto of(Transaction transaction, BlockRequestDto dbBlock) {
        TransactionRequestDto dto = new TransactionRequestDto();

        dto.Hash = toHexString(transaction.getHash());
        dto.TransactionDate = dbBlock.MinedDate;
        dto.MoneyAmount = toBigDecimal(transaction.getValue());
        dto.GasAmount = toBigDecimal(transaction.getGasPrice()).doubleValue();
        dto.SourceClientHash = toHexString(transaction.getSender());
        dto.DestinationClientHash = toHexString(transaction.getReceiveAddress());
        dto.BlockHash = dbBlock.Hash;

        return dto;
    }

    private static String toHexString(byte[] bytes) {
        return ByteUtil.toHexString(bytes);
    }

    private static BigDecimal toBigDecimal(byte[] bytes) {
        return new BigDecimal(new BigInteger(toHexString(bytes), 16));
    }
}
