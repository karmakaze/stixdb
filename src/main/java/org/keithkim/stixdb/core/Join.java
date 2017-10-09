package org.keithkim.stixdb.core;

import org.keithkim.stixdb.core.util.RowValues;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Join {
    public static Table inner(Table left, Table right, Set<OnCond> on) {
        Map<Column.Name, Object> leftValued = new HashMap<>();
        Map<Column.Name, Object> rightValued = new HashMap<>();
        List<OnCond> onColCol = new ArrayList<OnCond>();

        for (OnCond onCond : on) {
            NameOrValue onLeft = onCond.leftNameOrValue;
            NameOrValue onRight = onCond.rightNameOrValue;

            if (onLeft.isName()) {
                if (onRight.isName()) {
                    onColCol.add(onCond);
                } else {
                    leftValued.put(onLeft.name(), onRight.value());
                }
            } else {
                if (onRight.isName()) {
                    rightValued.put(onRight.name(), onLeft.value());
                } else {
                    throw new StixException("ON value = value condition is invalid (use where)");
                }
            }
        }
        if (!leftValued.isEmpty()) {
            left = left.select(leftValued);
        }
        if (!rightValued.isEmpty()) {
            right = right.select(rightValued);
        }

        short[] onLeftColumns = left.columnIndices(onColCol.stream().map(c -> c.leftName()).collect(Collectors.toList()));
        short[] onRightColumns = right.columnIndices(onColCol.stream().map(c -> c.rightName()).collect(Collectors.toList()));

        List<Column.Name> allColumns = new ArrayList<>(left.columnNames("a"));
        allColumns.addAll(right.columnNames("b"));

        Table result = Table.ofSizeWithColumns(allColumns);

        for (RowValues leftRow : left.rows()) {
            RowValues leftCondValues = leftRow.select(onLeftColumns);
            for (RowValues rightRow : right.rows()) {
                if (onRightColumns.length != leftCondValues.size()) {
                    System.out.println("onRightColumns.length != leftCondValues.size()");
                }
                if (rightRow.matches(onRightColumns, leftCondValues)) {
                    result.addRow(RowValues.concat(leftRow, rightRow));
                }
            }
        }
        return result;
    }

    public static class OnCond {
        private final NameOrValue leftNameOrValue;
        private final NameOrValue rightNameOrValue;

        protected OnCond(NameOrValue left, NameOrValue right) {
            this.leftNameOrValue = left;
            this.rightNameOrValue = right;
        }

        public Column.Name leftName() {
            return leftNameOrValue.name();
        }

        public Object leftValue() {
            return leftNameOrValue.value();
        }

        public Column.Name rightName() {
            return rightNameOrValue.name();
        }

        public Object rightValue() {
            return rightNameOrValue.value();
        }

        @Override
        public int hashCode() {
            int result = leftNameOrValue.hashCode();
            result = 31 * result + rightNameOrValue.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;

            OnCond that = (OnCond) o;

            if (!Objects.equals(leftNameOrValue, that.leftNameOrValue)) return false;
            if (!Objects.equals(rightNameOrValue, that.rightNameOrValue)) return false;

            return true;
        }

        @Override
        public String toString() {
            return "OnCond{" + leftNameOrValue + " = " + rightNameOrValue + "}";
        }
    }
}
