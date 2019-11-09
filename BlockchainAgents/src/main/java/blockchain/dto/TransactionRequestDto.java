package blockchain.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionRequestDto {
    public String Hash;
    public Date TransactionDate;
    public BigDecimal MoneyAmount;
    public double GasAmount;
    public String SourceClientHash;
    public String DestinationClientHash;
    public String BlockHash;
}
