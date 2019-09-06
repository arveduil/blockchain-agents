package blockchain.currency;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Dollar extends Currency {
    private String currencyCode = "USD";


    public Dollar(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public Dollar(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public Dollar(char[] in) {
        super(in);
    }

    public Dollar(char[] in, MathContext mc) {
        super(in, mc);
    }

    public Dollar(String val) {
        super(val);
    }

    public Dollar(String val, MathContext mc) {
        super(val, mc);
    }

    public Dollar(double val) {
        super(val);
    }

    public Dollar(double val, MathContext mc) {
        super(val, mc);
    }

    public Dollar(BigInteger val) {
        super(val);
    }

    public Dollar(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public Dollar(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public Dollar(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public Dollar(int val) {
        super(val);
    }

    public Dollar(int val, MathContext mc) {
        super(val, mc);
    }

    public Dollar(long val) {
        super(val);
    }

    public Dollar(long val, MathContext mc) {
        super(val, mc);
    }

    public static String getCurrencyName(){
        return Dollar.class.toString();
    }


    @Override
    public String toString(){
        return this.toString() + " " + currencyCode;
    }
}
