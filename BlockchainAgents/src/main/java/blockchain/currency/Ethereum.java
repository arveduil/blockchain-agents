package blockchain.currency;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Ethereum extends Currency {
    private String currencyCode = "ETH";


    public Ethereum(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public Ethereum(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public Ethereum(char[] in) {
        super(in);
    }

    public Ethereum(char[] in, MathContext mc) {
        super(in, mc);
    }

    public Ethereum(String val) {
        super(val);
    }

    public Ethereum(String val, MathContext mc) {
        super(val, mc);
    }

    public Ethereum(double val) {
        super(val);
    }

    public Ethereum(double val, MathContext mc) {
        super(val, mc);
    }

    public Ethereum(BigInteger val) {
        super(val);
    }

    public Ethereum(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public Ethereum(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public Ethereum(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public Ethereum(int val) {
        super(val);
    }

    public Ethereum(int val, MathContext mc) {
        super(val, mc);
    }

    public Ethereum(long val) {
        super(val);
    }

    public Ethereum(long val, MathContext mc) {
        super(val, mc);
    }

    public static String getCurrencyName(){
        return Ethereum.class.toString();
    }

    @Override
    public String toString(){
        return this.toString() + " " + currencyCode;
    }
}
