package model;

public class Card {
    private String cardCurrency;
    private double money;

    public Card(String cardCurrency, double money) {
        this.cardCurrency = cardCurrency;
        this.money = money;
    }

    public void pay(double money) {
        this.money = this.money - money;
    }

    public String getCardCurrency() {
        return cardCurrency;
    }

    public double getMoney() {
        return money;
    }
}
