package org.keithkim.stixdb.core;

import org.keithkim.stixdb.core.util.RowValues;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.util.Collections.emptyList;

public class Index<T extends Comparable<T>> {
    protected final int column;
    protected NavigableMap<T, List<RowValues>> valueRows = new ConcurrentSkipListMap<>();
    protected List<RowValues> nullValuedRows = new ArrayList<>();

    protected Index(int column) {
        this.column = column;
    }

    public void rowAdded(RowValues row) {
        T value = (T) row.value(column);
        List<RowValues> rows = valueRows.get(value);
        if (rows == null) {
            rows = new ArrayList<>();
            valueRows.put(value, rows);
        }
        rows.add(row);
    }

    public int cardinality() {
        return nullValuedRows.isEmpty() ? valueRows.size() : valueRows.size() + 1;
    }

    public List<RowValues> rowsWithIndexValue(Object value) {
        List<RowValues> rows = valueRows.get((T) value);
        return rows == null ? emptyList() : rows;
    }

    public static class Name extends org.keithkim.stixdb.core.Name {
        public Name(String name) {
            super(name);
        }
    }
}