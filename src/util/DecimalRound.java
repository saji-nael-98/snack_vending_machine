package util;

public class DecimalRound {
    public static double round(double num) {
        return Math.round(num * 100.0) / 100.0;
    }
}
