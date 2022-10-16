package AppEnum;

import exception.InvalidMoneyException;

import java.util.Arrays;

public enum CoinSlot {
    C_10("10c", 0.1), C_20("20c", 0.2), C_50("50c", 0.5), C_100("$1", 1);
    private final String pattern;
    private final double num;

    CoinSlot(String pattern, double num) {
        this.pattern = pattern;
        this.num = num;
    }

    @Override
    public String toString() {
        return pattern;
    }

    public double getNum() {
        return num;
    }

    public static CoinSlot findByPattern(String pattern) throws IllegalArgumentException {
        return Arrays.stream(values()).filter(coin -> coin.pattern.equals(pattern)).findAny().orElseThrow(() -> new InvalidMoneyException(String.format("the option must be of these %s", Arrays.toString(values()))));
    }
}
