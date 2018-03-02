package SecureMethods;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sfaxi19 on 19.05.17.
 */
public class SecureMethods {


    public static boolean isSimplesity(BigInteger value) {
        return miller_rabin(value, 10);
    }

    public static BigInteger getSimplesity(BigInteger value) {
        do {
            value = value.add(BigInteger.ONE);
        } while (!isSimplesity(value));
        return value;
    }

    public static BigInteger funcEuler(BigInteger p, BigInteger q) {
        BigInteger one = new BigInteger("1", 10);
        return (p.subtract(one)).multiply(q.subtract(one));
    }

    public static BigInteger binMod(BigInteger base, BigInteger exponent, BigInteger m) {
        int trailing_zero_bits_count = exponent.getLowestSetBit();
        if (trailing_zero_bits_count == -1) // exponent == 0
            return BigInteger.ONE.mod(m);
        BigInteger z = binMod(base, exponent.shiftRight(1), m); // base**(exponent//2)% m
        BigInteger z_sq = z.pow(2).mod(m); // z**2 % m
        if (trailing_zero_bits_count != 0) // even
            return z_sq;
        return z_sq.multiply(base).mod(m); // z**2 * base % m
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        while (b.compareTo(BigInteger.ZERO) > 0) {
            a = a.mod(b);
            BigInteger tmp = b;
            b = a;
            a = tmp;
        }
        return a;
    }

    public static BigInteger ggcd(BigInteger a, BigInteger b) {
        BigInteger mod = b;
        BigInteger zero = new BigInteger("0", 10);
        if (a.compareTo(zero) == 0) {
            return b;
        }
        BigInteger x;
        BigInteger y;
        BigInteger x2 = new BigInteger("1", 10);
        BigInteger x1 = new BigInteger("0", 10);
        BigInteger y2 = new BigInteger("0", 10);
        BigInteger y1 = new BigInteger("1", 10);
        BigInteger d = new BigInteger("0", 10);
        BigInteger r;
        BigInteger q;
        while (b.compareTo(zero) > 0) {
            q = a.divide(b);
            r = a.subtract(q.multiply(b));
            x = x2.subtract(q.multiply(x1));
            y = y2.subtract(q.multiply(y1));
            a = b;
            b = r;
            x2 = x1;
            x1 = x;
            y2 = y1;
            y1 = y;
        }
        d = a;
        x = x2;
        y = y2;
        if (a.compareTo(b) > 0) return (mod.add(x)).mod(mod);
        else return (mod.add(y)).mod(mod);
    }

    public static BigInteger primitiveRoot(BigInteger p) {
        ArrayList<BigInteger> fact = new ArrayList<BigInteger>();
        BigInteger phi = p.subtract(BigInteger.ONE);
        fact.add(BigInteger.valueOf(2));
        fact.add(p.divide(BigInteger.valueOf(2)));
        for (BigInteger res = BigInteger.valueOf(2); res.compareTo(p) <= 0; res = res.add(BigInteger.ONE)) {
            boolean ok = true;
            for (int i = 0; i < fact.size() && ok; ++i) {
                BigInteger b = SecureMethods.binMod(res, phi.divide(fact.get(i)), p);
                ok = (b.compareTo(BigInteger.ONE) != 0);
            }
            if (ok) return res;
        }
        return null;
    }

    private static boolean miller_rabin(BigInteger n, int rounds) {
        Random rand = new Random();
        if (n.compareTo(BigInteger.valueOf(2)) == 0)
            return true;
        if ((n.compareTo(BigInteger.valueOf(2)) < 0) || !(n.testBit(0)))
            return false;
        BigInteger n_1 = n.subtract(BigInteger.ONE);
        BigInteger two = BigInteger.valueOf(2);
        BigInteger s = BigInteger.ONE;
        while (n_1.mod(two.pow(s.intValue())).compareTo(BigInteger.ZERO) == 0) {
            s = s.add(BigInteger.ONE);
        }
        s = s.subtract(BigInteger.ONE);
        BigInteger t = n_1.divide(two.pow(s.intValue()));
        for (int i = 0; i < rounds; i++) {
            BigInteger a = BigInteger.valueOf(2 + Math.abs(rand.nextLong()) % (n.longValue() - 4));
            BigInteger x = binMod(a, t, n);
            if ((x.compareTo(BigInteger.ONE) == 0) || (x.compareTo(n_1) == 0)) {
                continue;
            }
            int j;
            for (j = 1; j < s.intValue(); j++) {
                x = binMod(x, two, n);
                if (x.compareTo(BigInteger.ONE) == 0) return false;
                if (x.compareTo(n_1) == 0) break;
            }
            if (j == s.intValue()) return false;
        }
        return true;
    }
}
