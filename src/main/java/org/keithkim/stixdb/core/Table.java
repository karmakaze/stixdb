package org.keithkim.stixdb.core;

import org.keithkim.stixdb.core.util.ArraysList;
import org.keithkim.stixdb.core.util.RowValues;
import org.keithkim.stixdb.core.util.Value;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.keithkim.stixdb.core.util.Values.values;

public class Table {
    private final LinkedHashMap<Column.Name, Short> columnIndex;
    private final List<RowValues> rows;
    private final Map<Integer, Index> indexes;

    public static Table ofSizeWithColumns() {
        return new Table(emptyList());
    }

    public static Table ofSizeWithColumns(String... columns) {
        Column.Name[] columnNames = new Column.Name[columns.length];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = new Column.Name(columns[i]);
        }
        return ofSizeWithColumns(columnNames);
    }

    public static Table ofSizeWithColumns(Column.Name... columnNames) {
        return ofSizeWithColumns(asList(columnNames));
    }

    public static Table ofSizeWithColumns(List<Column.Name> columnNames) {
        if (new HashSet<>(columnNames).size() != columnNames.size()) {
            throw new StixException("Column names must be unique.");
        }
        return new Table(columnNames);
    }

    protected Table(List<Column.Name> columnNames) {
        columnIndex = new LinkedHashMap<>(columnNames.size());
        short index = 0;
        for (Column.Name columnName : columnNames) {
            columnIndex.put(columnName, index);
            index++;
        }
        rows = new ArrayList<>();
        indexes = new HashMap<>();
    }

    public void addIndex(Column.Name columnName) {
        short i = columnIndex.get(columnName);
        addIndex(i);
    }

    public void addIndex(int column) {
        Index index = indexes.get(column);
        if (index == null) {
            index = new Index(column);
            indexes.put(column, index);

            for (RowValues row : rows) {
                index.rowAdded(row);
            }
        }
    }

    public int columnCount() {
        return columnIndex.size();
    }

    public int rowCount() {
        return rows.size();
    }

    public void addRow(RowValues row) {
        if (row.size() != columnIndex.size()) {
            throw new StixException("Incorrect number of column rowValues");
        }
        rows.add(row);
        for (Index index : indexes.values()) {
            index.rowAdded(row);
        }
    }

    public Optional<RowValues> addRow(Object... values) {
        if (values.length != columnIndex.size()) {
            throw new StixException("Incorrect number of column rowValues");
        }
        if (values != null && values.length == columnIndex.size()) {
            RowValues row = RowValues.with(values(values));
            addRow(row);
            return Optional.of(row);
        }
        return Optional.empty();
    }

    public List<RowValues> rows() {
        return rows;
    }

    /**
     * Select returns a table with all rows but only the rowValues of the named columns.
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
        Table table = Table.ofSizeWithColumns(columnNames);

        short[] indices = columnIndices(columnNames);
        short[] whereIndices = null;
        RowValues whereValues = RowValues.EMPTY_ROW;
        if (whereColumnNameValues != null) {
            Column.Name[] whereNames = new Column.Name[whereColumnNameValues.size()];
            Object[] values = new Object[whereColumnNameValues.size()];
            int[] i = new int[] {0};
            whereColumnNameValues.forEach((n, v) -> {
                whereNames[i[0]] = n;
                values[i[0]] = v;
                i[0]++;
            });
            whereIndices = columnIndices(asList(whereNames));
            whereValues = RowValues.with(values);
        }
        for (RowValues row : rows) {
            if (whereIndices == null || row.select(whereIndices).equals(whereValues)) {
                table.addRow(row.select(indices));
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

    protected short[] columnIndices(Set<Column.Name> columnNames) {
        return columnIndices(new ArrayList<>(columnNames));
    }

    protected short[] columnIndices(List<Column.Name> columnNames) {
        short[] indices = new short[columnNames.size()];
        for (int i = 0; i < indices.length; i++) {
            Column.Name columnName = columnNames.get(i);
            Short index = columnIndex.get(columnName);
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