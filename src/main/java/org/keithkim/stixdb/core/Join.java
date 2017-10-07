package org.keithkim.stixdb.core;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Join {
    public static Table inner(Table left, Table right, Set<OnCond> on) {
        Map<Column.Name, Object> leftValued = new HashMap<>();
        Map<Column.Name, Object> rightValued = new HashMap<>();
        List<OnCond> onColCol = new ArrayList<OnCond>();

        for (OnCond onCond : on) {
            Object onLeft = onCond.left();
            Object onRight = onCond.right();

            if (onLeft instanceof Column.Name) {
                if (onRight instanceof Column.Name) {
                    onColCol.add(onCond);
                } else {
                    leftValued.put((Column.Name) onLeft, right);
                }
            } else {
                if (onRight instanceof Column.Name) {
                    leftValued.put((Column.Name) onRight, onLeft);
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

        int[] onLeftIndices = left.columnIndices(onColCol.stream().map(c -> (Column.Name) c.left()).collect(Collectors.toList()));
        int[] onRightIndices = right.columnIndices(onColCol.stream().map(c -> (Column.Name) c.right()).collect(Collectors.toList()));

        List<Column.Name> allColumns = new ArrayList<>(left.columnNames("a"));
        allColumns.addAll(right.columnNames("b"));

        Table result = Table.ofSizeWithColumns(0, allColumns);

        for (Row leftRow : left.rows()) {
            List<Object> leftCondValues = asList(leftRow.valuesArray(onLeftIndices));
            for (Row rightRow : right.rows()) {
                if (rightRow.matches(onRightIndices, leftCondValues)) {
                    result.addRow(Rows.concat(leftRow.values, rightRow.values));
                }
            }
        }
        return result;
    }

    public static abstract class OnCond {
        abstract Object left();
        abstract Object right();

        @Override
        public int hashCode() {
            int result = left().hashCode();
            result = 31 * result + right().hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;

            OnColColCond that = (OnColColCond) o;

            if (!left().equals(that.left())) return false;
            if (!right().equals(that.right())) return false;

            return true;
        }

        @Override
        public String toString() {
            return "OnCond{" + left() + " = " + right() + "}";
        }
    }

    public static class OnColColCond extends OnCond {
        private final Column.Name leftCol;
        private final Column.Name rightCol;

        public OnColColCond(Column.Name left, Column.Name right) {
            this.leftCol = left;
            this.rightCol = right;
        }

        public Column.Name left() {
            return leftCol;
        }

        public Column.Name right() {
            return rightCol;
        }
    }

    public static class OnColValCond extends OnCond {
        private final Column.Name leftCol;
        private final Object rightVal;

        public OnColValCond(Column.Name leftCol, Object rightVal) {
            this.leftCol = leftCol;
            this.rightVal = rightVal;
        }

        public Column.Name left() {
            return leftCol;
        }

        public Object right() {
            return rightVal;
        }
    }

    public static class OnValColCond extends OnCond {
        private final Object leftVal;
        private final Column.Name rightCol;

        public OnValColCond(Object leftVal, Column.Name rightCol) {
            this.leftVal = leftVal;
            this.rightCol = rightCol;
        }

        public Object left() {
            return leftVal;
        }

        public Column.Name right() {
            return rightCol;
        }
    }
}
