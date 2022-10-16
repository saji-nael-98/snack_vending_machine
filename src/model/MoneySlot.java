package model;

import AppEnum.PaymentMethod;
import util.DecimalRound;

public class MoneySlot {
    public static final String VALID_COIN = "c";
    public static final String VALID_CURRENCY = "$";
    private double insertedMoney;
    private PaymentMethod currentMethod;

    public MoneySlot() {

    }

    public void setInsertedMoney(double insertedMoney) {
        this.insertedMoney = DecimalRound.round(insertedMoney + this.insertedMoney);
    }

    public double getInsertedMoney() {
        return insertedMoney;
    }

    public void setCurrentMethod(PaymentMethod currentMethod) {
        this.currentMethod = currentMethod;
    }

    public PaymentMethod getCurrentMethod() {
        return currentMethod;
    }

    public void clearInsertedMoney() {
        this.insertedMoney = 0;
    }
}
