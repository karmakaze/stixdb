package org.keithkim.stixdb.core.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

public class RowValues {
    public static final RowValues EMPTY_ROW = new RowValues(0);

    protected final ArraysList<Object> values;
    protected final short[] indices;

    public static RowValues with(Value... values) {
        if (values.length == 0) {
            return EMPTY_ROW;
        }
        RowValues rowValues = new RowValues(values.length);

        int i = 0;
        for (Value value : values) {
            rowValues.values.set(i, value.value);
            i++;
        }
        return rowValues;
    }

    public static RowValues with(Object[] values) {
        if (values.length == 0) {
            return EMPTY_ROW;
        }
        RowValues rowValues = new RowValues(values.length);

        int i = 0;
        for (Object value : values) {
            rowValues.values.set(i, value);
            i++;
        }
        return rowValues;
    }

    protected RowValues(int size) {
        if (size > Short.MAX_VALUE) {
            throw new RuntimeException("RowValues(size): size must be between 0 and 32767");
        }
        values = new ArraysList<>(size);
        indices = null;
    }

    protected RowValues(ArraysList<Object> values, short[] indices) {
        this.values = values;
        this.indices = indices;
    }

    public Object value(int column) {
        return indices == null ? values.get(column) : values.get(indices[column]);
    }

    public ArraysList<Object> values() {
        return values;
    }

    public int size() {
        return indices == null ? values.size() : indices.length;
    }

    public RowValues select(short[] columns) {
        if (columns == null) {
            return this;
        }
        if (columns.length == 0) {
            return EMPTY_ROW;
        }
        short[] newIndices = new short[columns.length];
        boolean reordered = false;
        boolean natural = true;
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] != i) {
                reordered = true;
            }
            newIndices[i] = indices == null ? columns[i] : indices[columns[i]];
            if (newIndices[i] != i) {
                natural = false;
            }
        }
        if (!reordered && columns.length == size()) {
            return this;
        }
        if (natural && columns.length == this.values.size()) {
            return new RowValues(values, null);
        }
        return new RowValues(values, newIndices);
    }

    public static RowValues concat(RowValues a, RowValues b) {
        if (a.values.isEmpty() && b.values.isEmpty()) {
            return EMPTY_ROW;
        }
        ArraysList<Object> newValues = new ArraysList<>(a.values, b.values);

        short[] newIndices = new short[newValues.size()];
        short offset = 0;
        boolean natural = true;
        for (RowValues rowValues : asList(a, b)) {
            if (rowValues.indices == null) {
                int size = rowValues.values.size();
                for (int i = 0; i < size; i++) {
                    newIndices[offset + i] = (short) (offset + i);
                    if (newIndices[offset + i] != offset + i) {
                        natural = false;
                    }
                }
                offset += size;
            } else {
                int i = 0;
                for (short index : rowValues.indices) {
                    newIndices[offset + i] = (short) (offset + index);
                    if (newIndices[offset + i] != offset + i) {
                        natural = false;
                    }
                    i++;
                }
                offset += rowValues.indices.length;
            }
        }
        if (natural && newIndices.length == newValues.size()) {
            return new RowValues(newValues, null);
        }
        return new RowValues(newValues, newIndices);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Object value : values) {
            hashCode = hashCode * 31 + Objects.hashCode(value) + 1;
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RowValues))
            return false;

        RowValues that = (RowValues) o;

        int size = size();
        if (size != that.size()) {
            return false;
        }

        if (indices == null) {
            if (that.indices == null) {
                return Objects.equals(values, that.values);
            } else {
                for (int i = 0; i < size; i++) {
                    if (!Objects.equals(values.get(i), that.values.get(that.indices[i]))) {
                        return false;
                    }
                }
            }
        } else {
            if (that.indices == null) {
                for (int i = 0; i < size; i++) {
                    if (!Objects.equals(values.get(indices[i]), that.values.get(i))) {
                        return false;
                    }
                }
            } else {
                if (Arrays.equals(indices, that.indices)) {
                    for (short index : indices) {
                        if (!Objects.equals(values.get(index), that.values.get(index))) {
                            return false;
                        }
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        if (!Objects.equals(values.get(indices[i]), that.values.get(that.indices[i]))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        if (indices == null) {
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
        } else {
            String sep = "";
            for (short index : indices) {
                Object value = values.get(index);
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
        }
        return buf.toString();
    }
}