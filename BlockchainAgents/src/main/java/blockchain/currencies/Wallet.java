package blockchain.currencies;

public class Wallet {
    private Ethereum ethAmmount;
    private String walletHash;
    private String blockHash;

    public Wallet(){
        ethAmmount = new Ethereum(0);
        walletHash = "DEFAULT_WALLET_HASH";
        blockHash = "DEFAULT_BlOCK_HASH";
    }

    public void addToWallet(Ethereum amount){
        ethAmmount = ethAmmount.add(amount);

    }

    public void substractFromWallet(Ethereum amount){
        ethAmmount = ethAmmount.substract(amount);
    }

    public Ethereum getCurrentAmount(){
        return  ethAmmount;
    }

    public boolean contains(Ethereum eth){
        return  !(this.getCurrentAmount().compareTo(eth) == -1);
    }

    public String getWalletHash() {
        return walletHash;
    }

    public void setWalletHash(String walletHash) {
        this.walletHash = walletHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }
}