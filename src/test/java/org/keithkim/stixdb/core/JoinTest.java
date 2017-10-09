package org.keithkim.stixdb.core;

import org.junit.Test;
import org.keithkim.stixdb.core.Join.OnCond;
import org.keithkim.stixdb.core.util.RowValues;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.Column.NameOrValue.ofName;
import static org.keithkim.stixdb.core.Column.NameOrValue.ofValue;
import static org.keithkim.stixdb.core.util.Values.values;

public class JoinTest {
    @Test
    public void innerCrossProduct_returnsProductOfRows() throws Exception {
        Table a = Table.ofSizeWithColumns("x", "y");
        Optional<RowValues> a1 = a.addRow("a", 1L);
        Optional<RowValues> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> b1 = b.addRow("a", 1L, 1.0f);
        Optional<RowValues> b2 = b.addRow("b", 2L, 2.0f);
        Optional<RowValues> b3 = b.addRow("c", 3L, 3.0f);

        Table result = Join.inner(a, b, Set.of());

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(6);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(6);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                RowValues.with(values("a", 1L, "a", 1L, 1.0f)),
                RowValues.with(values("a", 1L, "b", 2L, 2.0f)),
                RowValues.with(values("a", 1L, "c", 3L, 3.0f)),
                RowValues.with(values("b", 2L, "a", 1L, 1.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.0f)),
                RowValues.with(values("b", 2L, "c", 3L, 3.0f)));
    }

    @Test
    public void innerFilteredCrossProduct_returnsFilteredProductOfRows() throws Exception {
        Table a = Table.ofSizeWithColumns("x", "y");
        Optional<RowValues> a1 = a.addRow("a", 1L);
        Optional<RowValues> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> b1 = b.addRow("a", 1L, 1.0f);
        Optional<RowValues> b2 = b.addRow("b", 2L, 2.0f);
        Optional<RowValues> b2n = b.addRow("b", 2L, -2.0f);
        Optional<RowValues> b3 = b.addRow("c", 3L, 3.0f);

        Table result = Join.inner(a, b, Set.of(new OnCond(ofName(Column.name("x")), ofValue("a")),
                                               new OnCond(ofValue("b"), ofName(Column.name("first")))));

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(2);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(2);

        RowValues row0 = rows.get(0);
        RowValues expectedRow0 = RowValues.with(values("a", 1L, "b", 2L, 2.0f));
        assertThat(row0).describedAs("rows[0]").isEqualTo(expectedRow0);
        assertThat(rows.get(1)).describedAs("rows[0]").isEqualTo(
                RowValues.with(values("a", 1L, "b", 2L, -2.0f)));

        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                RowValues.with(values("a", 1L, "b", 2L, 2.0f)),
                RowValues.with(values("a", 1L, "b", 2L, -2.0f)));
    }

    @Test
    public void innerJoin_returnsPairsOfMatchingRows() throws Exception {
        Table a = Table.ofSizeWithColumns("x", "y");
        Optional<RowValues> a1 = a.addRow("a", 1L);
        Optional<RowValues> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> b1 = b.addRow("a", 1L, 1.0f);
        Optional<RowValues> b2 = b.addRow("b", 2L, 2.0f);
        Optional<RowValues> b21 = b.addRow("b", 2L, 2.1f);
        Optional<RowValues> b22 = b.addRow("b", 22L, 2.2f);
        Optional<RowValues> b3 = b.addRow("c", 3L, 3.0f);

        Table result = Join.inner(a, b, Set.of(new OnCond(ofName(Column.name("x")), ofName(Column.name("first"))),
                                               new OnCond(ofName(Column.name("y")), ofName(Column.name("second")))));

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
//        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(3);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(3);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                RowValues.with(values("a", 1L, "a", 1L, 1.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.1f)));
    }

    @Test
    public void innerJoinWithLeftIndexBefore_returnsPairsOfMatchingRows() throws Exception {
        Table a = Table.ofSizeWithColumns("x", "y");
        a.addIndex(Column.name("x"));
        Optional<RowValues> a1 = a.addRow("a", 1L);
        Optional<RowValues> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> b1 = b.addRow("a", 1L, 1.0f);
        Optional<RowValues> b2 = b.addRow("b", 2L, 2.0f);
        Optional<RowValues> b21 = b.addRow("b", 2L, 2.1f);
        Optional<RowValues> b22 = b.addRow("b", 22L, 2.2f);
        Optional<RowValues> b3 = b.addRow("c", 3L, 3.0f);

        Table result = Join.inner(a, b, Set.of(new OnCond(ofName(Column.name("x")), ofName(Column.name("first")))));

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(4);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(4);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                RowValues.with(values("a", 1L, "a", 1L, 1.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.1f)),
                RowValues.with(values("b", 2L, "b", 22L, 2.2f)));
    }

    @Test
    public void innerJoinWithRightIndexAfter_returnsPairsOfMatchingRows() throws Exception {
        Table a = Table.ofSizeWithColumns("x", "y");
        Optional<RowValues> a1 = a.addRow("a", 1L);
        Optional<RowValues> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> b1 = b.addRow("a", 1L, 1.0f);
        Optional<RowValues> b2 = b.addRow("b", 2L, 2.0f);
        Optional<RowValues> b21 = b.addRow("b", 2L, 2.1f);
        Optional<RowValues> b22 = b.addRow("b", 22L, 2.2f);
        Optional<RowValues> b3 = b.addRow("c", 3L, 3.0f);
        b.addIndex(Column.name("first"));

        Table result = Join.inner(a, b, Set.of(new OnCond(ofName(Column.name("x")), ofName(Column.name("first")))));

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(4);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(4);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                RowValues.with(values("a", 1L, "a", 1L, 1.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.0f)),
                RowValues.with(values("b", 2L, "b", 2L, 2.1f)),
                RowValues.with(values("b", 2L, "b", 22L, 2.2f)));
    }
}