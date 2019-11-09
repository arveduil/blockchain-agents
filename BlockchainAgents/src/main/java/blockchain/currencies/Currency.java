package blockchain.currencies;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public abstract class Currency extends BigDecimal {
    public Currency(char[] in, int offset, int len) {
        super(in, offset, len);
    }

    public Currency(char[] in, int offset, int len, MathContext mc) {
        super(in, offset, len, mc);
    }

    public Currency(char[] in) {
        super(in);
    }

    public Currency(char[] in, MathContext mc) {
        super(in, mc);
    }

    public Currency(String val) {
        super(val);
    }

    public Currency(String val, MathContext mc) {
        super(val, mc);
    }

    public Currency(double val) {
        super(val);
    }

    public Currency(double val, MathContext mc) {
        super(val, mc);
    }

    public Currency(BigInteger val) {
        super(val);
    }

    public Currency(BigInteger val, MathContext mc) {
        super(val, mc);
    }

    public Currency(BigInteger unscaledVal, int scale) {
        super(unscaledVal, scale);
    }

    public Currency(BigInteger unscaledVal, int scale, MathContext mc) {
        super(unscaledVal, scale, mc);
    }

    public Currency(int val) {
        super(val);
    }

    public Currency(int val, MathContext mc) {
        super(val, mc);
    }

    public Currency(long val) {
        super(val);
    }

    public Currency(long val, MathContext mc) {
        super(val, mc);
    }

    public static String getCurrencyName(){
        throw new NotImplementedException();
    }
}