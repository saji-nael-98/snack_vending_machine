package AppEnum;

import exception.InvalidMoneyException;

import java.util.Arrays;

public enum NotesSlot {
    $_20("20$", 20), $_50("50$", 50);
    private final String pattern;
    private final double num;

    NotesSlot(String pattern, double num) {
        this.pattern = pattern;
        this.num = num;
    }

    @Override
    public String toString() {
        return pattern;
    }

    public static NotesSlot findByPattern(String pattern) throws IllegalArgumentException {
        return Arrays.stream(values()).filter(notesSlot -> notesSlot.pattern.equals(pattern)).findAny().orElseThrow(() -> new InvalidMoneyException(String.format("the option must be of these %s", Arrays.toString(values()))));
    }

    public double getNum() {
        return num;
    }
}
