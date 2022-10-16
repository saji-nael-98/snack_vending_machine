package model;

import java.util.Arrays;

public class Row {
    public final static int COL_NUM = 5;
    private Column[] columns;

    public Row() {
        columns = new Column[COL_NUM];
        for (int x = 0; x < COL_NUM; x++) {
            columns[x] = new Column();
        }

    }

    public Column[] getColumns() {
        return columns;
    }


}
