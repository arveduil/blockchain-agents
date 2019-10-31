package blockchain.behaviours;

public enum BuyerState {
    INIT,
    COLLECTING_RESPONSES,
    SENDING_ACCEPT_PROPOSE_TO_SELLER,
    FINALIZING_TRANSACTION,
    END
}
