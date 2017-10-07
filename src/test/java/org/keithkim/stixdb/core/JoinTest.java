package org.keithkim.stixdb.core;

import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class JoinTest {
    @Test
    public void innerCrossProduct_returnsProductOfRows() throws Exception {
        Table a = Table.ofSizeWithColumns(0, "x", "y");
        Optional<Row> a1 = a.addRow("a", 1L);
        Optional<Row> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns(0, "first", "second", "third");
        Optional<Row> b1 = b.addRow("a", 1L, 1.0f);
        Optional<Row> b2 = b.addRow("b", 2L, 2.0f);
        Optional<Row> b3 = b.addRow("c", 3L, 3.0f);

        Table result = Join.inner(a, b, Set.of());

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(6);
        List<Row> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(6);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                new Row("a", 1L, "a", 1L, 1.0f),
                new Row("a", 1L, "b", 2L, 2.0f),
                new Row("a", 1L, "c", 3L, 3.0f),
                new Row("b", 2L, "a", 1L, 1.0f),
                new Row("b", 2L, "b", 2L, 2.0f),
                new Row("b", 2L, "c", 3L, 3.0f));
    }

    @Test
    public void innerJoin_returnsPairsOfMatchingRows() throws Exception {
        Table a = Table.ofSizeWithColumns(0, "x", "y");
        Optional<Row> a1 = a.addRow("a", 1L);
        Optional<Row> a2 = a.addRow("b", 2L);

        Table b = Table.ofSizeWithColumns(0, "first", "second", "third");
        Optional<Row> b1 = b.addRow("a", 1L, 1.0f);
        Optional<Row> b2 = b.addRow("b", 2L, 2.0f);
        Optional<Row> b2n = b.addRow("b", 2L, -2.0f);
        Optional<Row> b3 = b.addRow("c", 3L, 3.0f);

        Table result = Join.inner(a, b, Set.of(new Join.OnColColCond(Column.name("x"), Column.name("first")),
                                               new Join.OnColColCond(Column.name("y"), Column.name("second"))));

        assertThat(result).isNotNull();
        assertThat(result.columnCount()).describedAs("columnCount").isEqualTo(5);
        assertThat(result.columnNames()).isEqualTo(Column.names("a.x", "a.y", "b.first", "b.second", "b.third"));
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(3);
        List<Row> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(3);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(
                new Row("a", 1L, "a", 1L, 1.0f),
                new Row("b", 2L, "b", 2L, 2.0f),
                new Row("b", 2L, "b", 2L, -2.0f));
    }
}