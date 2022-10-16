package exception;

public class InvalidOption extends IllegalArgumentException {
    public InvalidOption() {
        super("Invalid option!");
    }
}
