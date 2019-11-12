package blockchain.dto;

import blockchain.ethereumj.BasicNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientRequestDto {
    public String Hash;
    public ClientType Type;
    public BigDecimal Amount;
    public Date StartDate;

    public List<String> TransactionsHashes;
    public List<String> MinedBlocksHashes;

    public ClientRequestDto() {
        TransactionsHashes = new ArrayList<>();
        MinedBlocksHashes = new ArrayList<>();
    }

    public static ClientRequestDto of(BasicNode node, boolean isMiner) {
        ClientRequestDto dto = new ClientRequestDto();

        dto.Hash = node.getAddressString();
        dto.Amount = node.getBalance();
        dto.StartDate = new Date(System.currentTimeMillis());
        dto.Type = isMiner ? ClientType.Miner : ClientType.Client;

        return dto;
    }
}
