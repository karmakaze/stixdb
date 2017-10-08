package org.keithkim.stixdb.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class Row {
    protected final Object[] values;

    protected Row(Value... values) {
        if (values == null) {
            this.values = new Object[] { null };
        } else {
            this.values = new Object[values.length];
            int i = 0;
            for (Value value : values) {
                this.values[i] = value.value;
                i++;
            }
        }
    }

    protected boolean matches(int[] whereIndices, List<Object> whereValues) {
        int i = 0;
        for (Object whereValue : whereValues) {
            if (!Objects.equals(values[whereIndices[i]], whereValue)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public Object value(int column) {
        try {
            return values[column];
        } catch (IndexOutOfBoundsException cause) {
            throw new StixException("Column index "+ column +" out of range");
        }
    }

    public List<Object> values() {
        return unmodifiableList(asList(values));
    }

    public List<Object> values(int... columns) {
        return unmodifiableList(asList(valuesArray(columns)));
    }

    protected Object[] valuesArray(int... columns) {
        int i = 0;
        try {
            Object[] values = new Object[columns.length];
            int j = 0;
            for (i = 0; i < columns.length; i++) {
                values[i] = this.values[columns[i]];
                if (columns[i] == j) {
                    j++;
                } else {
                    j = -1;
                }
            }
            return j == this.values.length ? this.values : values;
        } catch (IndexOutOfBoundsException cause) {
            throw new StixException("Column index "+ i +" out of range");
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Row) {
            Row that = (Row) other;
            return Arrays.equals(this.values, that.values);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        String sep = "";
        for (Object value : values) {
            buf.append(sep);
            if (value == null) {
                buf.append("null");
            } else if (value instanceof String) {
                buf.append('"').append(value.toString().replace("\\", "\\\\").replace("\"", "\\\"")).append('"');
            } else if (value instanceof Character) {
                buf.append('\'').append(((Character) value).charValue() == '\'' ? "\\" + value : value).append('\'');
            } else {
                buf.append(value.toString());
            }
            sep = ",";
        }
        return buf.toString();
    }
}
