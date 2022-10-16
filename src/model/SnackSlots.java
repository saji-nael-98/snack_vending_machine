package model;

public class SnackSlots {
    public final static int ROW_NUM = 5;
    private final Row[] rows;

    public SnackSlots() {
        rows = new Row[ROW_NUM];
        for (int x = 0; x < ROW_NUM; x++) {
            rows[x] = new Row();
        }
    }

    public Row[] getRows() {
        return rows;
    }


}
