package model;

import java.util.Stack;

public class Column {
    public static final int CAPACITY = 1;
    private Product colProduct;
    private Stack<Product> stack;

    public Column() {
        stack = new Stack<>();
    }

    public void addProductToStack(Product product) {
        stack.add(product);
    }

    public Product popProductFromStack() {
        return stack.pop();
    }

    public Product getColProduct() {
        return colProduct;
    }

    public void setColProduct(Product colProduct) {
        this.colProduct = colProduct;
    }

    public boolean isColEmpty() {
        return stack.isEmpty();
    }
}
