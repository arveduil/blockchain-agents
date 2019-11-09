package blockchain.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ClientRequestDto {
    public String Hash;
    public ClientType Type;
    public BigDecimal Amount;
    public Date StartDate;

    public List<String> TransactionsHashes;
    public List<String> MinedBlocksHashes;
}
