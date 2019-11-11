package blockchain.dto;

import java.math.BigDecimal;
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
}
