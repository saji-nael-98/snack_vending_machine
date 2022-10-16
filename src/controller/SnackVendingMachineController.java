package controller;

import AppEnum.CoinSlot;
import AppEnum.NotesSlot;
import AppEnum.PaymentMethod;
import exception.InvalidOption;
import model.*;
import util.BinarySearch;
import util.DecimalRound;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Random;

public class SnackVendingMachineController {
    private SnackVendingMachine machine;
    private int selectedRow, selectedCol;

    public SnackVendingMachineController() {
        machine = new SnackVendingMachine();
        fillMachine();
    }

    private void fillMachine() {
        int name = 65;
        for (int x = 0; x < machine.getSnackSlots().getRows().length; x++) {
            Row row = machine.getSnackSlots().getRows()[x];
            for (int y = 0; y < row.getColumns().length; y++) {
                Column column = row.getColumns()[y];
                String code = x + "" + y;
                String productName = String.format("%c%s", name, code);
                Random random = new Random();
                int price = random.nextInt(10 - 1 + 1) + 1;
                Product product = new Product(productName, DecimalRound.round(price));
                column.setColProduct(product);
                for (int z = 0; z < Column.CAPACITY; z++) {
                    column.addProductToStack(product);
                }
                name++;
            }

        }
    }

    public SnackSlots getSnacksSlot() {
        return machine.getSnackSlots();
    }


    public Product selectSnack(int code) throws InvalidOption, EmptyStackException, ArrayIndexOutOfBoundsException {
        if (validateSelectedSnack(code)) {
            int row = code / 10 - 1;
            int col = code % 10 - 1;
            if (machine.getSnackSlots().getRows()[row].getColumns()[col].isColEmpty()) {
                throw new EmptyStackException();
            }
            selectedRow = row;
            selectedCol = col;
            return machine.getSnackSlots().getRows()[selectedRow].getColumns()[selectedCol].getColProduct();
        } else {
            throw new InvalidOption();
        }
    }

    private boolean validateSelectedSnack(int code) {
        int minCode = 0;
        int maxCode = SnackSlots.ROW_NUM * 10 + Row.COL_NUM;
        return code >= minCode && code <= maxCode;
    }


    public List<String> displayPaymentMethodOptions(PaymentMethod method) {
        switch (method) {
            case COIN_SLOT: {
                return List.of("10c", "20c", "50c");
            }
            case NOTES_SLOT: {
                return List.of("20$", "50$");
            }
            case CARD_SLOT: {
                return List.of("USD card");
            }
        }
        return null;
    }

    public void insertMoney(PaymentMethod method, String money) throws IllegalArgumentException {
        switch (method) {
            case COIN_SLOT: {
                CoinSlot coinSlot = CoinSlot.findByPattern(money);
                machine.getMoneySlot().setInsertedMoney(coinSlot.getNum());
                machine.getMoneySlot().setCurrentMethod(PaymentMethod.COIN_SLOT);
                break;
            }
            case NOTES_SLOT: {
                NotesSlot notesSlot = NotesSlot.findByPattern(money);
                machine.getMoneySlot().setInsertedMoney(notesSlot.getNum());
                machine.getMoneySlot().setCurrentMethod(PaymentMethod.NOTES_SLOT);
                break;
            }
            case CARD_SLOT: {
                machine.getMoneySlot().setInsertedMoney(Double.parseDouble(money));
                machine.getMoneySlot().setCurrentMethod(PaymentMethod.CARD_SLOT);
                break;
            }
        }
    }

    public double displayInsertedMoney() {
        return machine.getMoneySlot().getInsertedMoney();
    }

    public boolean isEnoughMoney() {
        boolean isValid = machine.getMoneySlot().getInsertedMoney() >= machine.getSnackSlots().getRows()[selectedRow].getColumns()[selectedCol].getColProduct().getPrice();
        if (!isValid && getPaymentMethod() == PaymentMethod.CARD_SLOT)
            machine.getMoneySlot().setCurrentMethod(null);
        return isValid;
    }

    public Product popSnack() {
        if (getPaymentMethod() == PaymentMethod.CARD_SLOT)
            machine.getMoneySlot().clearInsertedMoney();
        return machine.getSnackSlots().getRows()[selectedRow].getColumns()[selectedCol].popProductFromStack();
    }

    public List<Double> ejectRemainingMoney() {
        if (getPaymentMethod() != PaymentMethod.CARD_SLOT) {
            double remainingMoney = DecimalRound.round(machine.getMoneySlot().getInsertedMoney() - machine.getSnackSlots().getRows()[selectedRow].getColumns()[selectedCol].getColProduct().getPrice());
            System.out.println("remaining money: " + remainingMoney);
            List<Double> remainingMoneyList = new ArrayList<>();
            double arr[] = new double[]{.1, .2, .5, 1, 20, 50};
            while (remainingMoney != 0) {
                double minus = arr[BinarySearch.binarySearch(arr, remainingMoney)];
                remainingMoneyList.add(minus);
                remainingMoney = DecimalRound.round(remainingMoney - minus);
            }
            machine.getMoneySlot().clearInsertedMoney();
            machine.getMoneySlot().setCurrentMethod(null);
            return remainingMoneyList;
        }
        machine.getMoneySlot().setCurrentMethod(null);
        return null;
    }

    public PaymentMethod getPaymentMethod() {
        return machine.getMoneySlot().getCurrentMethod();
    }
}
