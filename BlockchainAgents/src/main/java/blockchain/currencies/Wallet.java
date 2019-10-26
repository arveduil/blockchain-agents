package blockchain.currencies;

public class Wallet {
    private Ethereum ethAmmount;

    public Wallet(){
        ethAmmount = new Ethereum(0);
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
}