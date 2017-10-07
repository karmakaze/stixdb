package org.keithkim.stixdb.core;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class Table {
    private final LinkedHashMap<Column.Name, Integer> columnIndex;
    private final List<Row> rows;

    public static Table ofSizeWithColumns(int capacity) {
        return ofSizeWithColumns(capacity, emptyList());
    }

    public static Table ofSizeWithColumns(int capacity, String... columns) {
        Column.Name[] columnNames = new Column.Name[columns.length];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = new Column.Name(columns[i]);
        }
        return ofSizeWithColumns(capacity, columnNames);
    }

    public static Table ofSizeWithColumns(int capacity, Column.Name... columnNames) {
        return ofSizeWithColumns(capacity, asList(columnNames));
    }

    public static Table ofSizeWithColumns(int capacity, List<Column.Name> columnNames) {
        if (new HashSet<>(columnNames).size() != columnNames.size()) {
            throw new StixException("Column names must be unique.");
        }
        return new Table(capacity, columnNames);
    }

    protected Table(int capacity, List<Column.Name> columnNames) {
        columnIndex = new LinkedHashMap<>(columnNames.size());
        int index = 0;
        for (Column.Name columnName : columnNames) {
            columnIndex.put(columnName, index);
            index++;
        }
        rows = new ArrayList<Row>(capacity);
    }

    public int columnCount() {
        return columnIndex.size();
    }

    public int rowCount() {
        return rows.size();
    }

    public Optional<Row> addRow(Object... values) {
        if (values.length != columnIndex.size()) {
            throw new StixException("Incorrect number of column values");
        }
        if (values != null && values.length == columnIndex.size()) {
            Row row = new Row(values);
            rows.add(row);
            return Optional.of(row);
        }
        return Optional.empty();
    }

    public List<Row> rows() {
        return rows;
    }

    /**
     * Select returns a table with all rows but only the values of the named columns.
     * @param columnNames
     * @return
     */
    public Table select(Column.Name... columnNames) {
        return select(asList(columnNames), null);
    }

    public Table select(Column.Name[] columnNames, Map<Column.Name, Object> whereColumnNameValues) {
        return select(asList(columnNames), whereColumnNameValues);
    }

    public Table select(Map<Column.Name, Object> whereColumnNameValues) {
        return select(new ArrayList<>(columnIndex.keySet()), whereColumnNameValues);
    }

    public Table select(List<Column.Name> columnNames) {
        return select(columnNames, null);
    }

    public Table select(List<Column.Name> columnNames, Map<Column.Name, Object> whereColumnNameValues) {
        Table table = Table.ofSizeWithColumns(rowCount(), columnNames);

        int[] indices = columnIndices(columnNames);
        int[] whereIndices = null;
        List<Object> whereValues = null;
        if (whereColumnNameValues != null) {
            whereIndices = columnIndices(whereColumnNameValues.keySet());
            whereValues = new ArrayList<>(whereColumnNameValues.values());
        }
        for (Row row : rows) {
            if (whereIndices == null || row.matches(whereIndices, whereValues)) {
                table.addRow(row.valuesArray(indices));
            }
        }
        return table;
    }

    public List<Column.Name> columnNames() {
        return columnNames("");
    }

    public List<Column.Name> columnNames(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return new ArrayList<>(columnIndex.keySet());
        }
        List<Column.Name> columnNames = new ArrayList<>();
        for (Column.Name columnName : columnIndex.keySet()) {
            columnNames.add(new Column.Name(prefix +"."+ columnName.name));
        }
        return columnNames;
    }

    protected int[] columnIndices(Set<Column.Name> columnNames) {
        return columnIndices(new ArrayList<>(columnNames));
    }

    protected int[] columnIndices(List<Column.Name> columnNames) {
        int[] indices = new int[columnNames.size()];
        for (int i = 0; i < indices.length; i++) {
            Column.Name columnName = columnNames.get(i);
            Integer index = columnIndex.get(columnName);
            if (index == null) {
                throw new StixException("Column '"+ columnName +"' does not exist");
            }
            indices[i] = index;
        }
        return indices;
    }

    public static class Name extends org.keithkim.stixdb.core.Name {
        public Name(String name) {
            super(name);
        }
    }
}
