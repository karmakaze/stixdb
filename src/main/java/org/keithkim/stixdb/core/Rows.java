package org.keithkim.stixdb.core;

import java.util.Arrays;
import java.util.List;

public class Rows {
    protected static Object[] concat(Object[]... rows) {
        int width = 0;
        for (Object[] r : rows) {
            width += r.length;
        }
        Object[] row = new Object[width];
        int i = 0;
        for (Object[] r : rows) {
            System.arraycopy(r, 0, row, i, r.length);
            i += r.length;
        }
        return row;
    }
}
