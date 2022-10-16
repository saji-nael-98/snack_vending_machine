package model;

public class SnackVendingMachine {

    private final MoneySlot moneySlot;
    private final SnackSlots snackSlots;

    public SnackVendingMachine() {
        moneySlot = new MoneySlot();
        snackSlots = new SnackSlots();
    }

    public SnackSlots getSnackSlots() {
        return snackSlots;
    }

    public MoneySlot getMoneySlot() {
        return moneySlot;
    }

}
