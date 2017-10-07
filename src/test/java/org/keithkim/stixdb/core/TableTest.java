package org.keithkim.stixdb.core;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TableTest {
    @Test
    public void createTable() {
        Table table = Table.ofSizeWithColumns(0);

        assertThat(table.rowCount()).describedAs("rowCount").isEqualTo(0);
    }

    @Test
    public void addRowWithNoValues_shouldAddRowWithoutValues() {
        Table table = Table.ofSizeWithColumns(0);
        Optional<Row> row_ = table.addRow();

        assertThat(row_).isNotEmpty();

        Row row = row_.get();
        assertThat(row.values()).describedAs("values").isNotNull();
        assertThat(row.values()).describedAs("values").isEmpty();
    }

    @Test
    public void addRowWithValues_shouldAddRowWithValues() {
        Table table = Table.ofSizeWithColumns(0, "first", "second", "third");
        Optional<Row> row_ = table.addRow("a", 1L, 1.0f);

        assertThat(row_).isNotEmpty();

        Row row = row_.get();
        assertThat(row.values()).describedAs("values").isNotNull();
        assertThat(row.values()).describedAs("values").hasSize(3);
        assertThat(row.values()).describedAs("").contains("a", 1L, 1.0f);
    }

    @Test
    public void selectColumnsFromEmptyTable_shouldReturnNoRows() {
        Table table = Table.ofSizeWithColumns(0, "first", "second", "third");

        Table result = table.select(Column.names("first", "third"));
        assertThat(result).isNotNull();
        assertThat(result.rowCount()).describedAs("rowCount").isZero();
    }

    @Test
    public void selectColumns_shouldReturnAllRowsWithNamedColumnValues() {
        Table table = Table.ofSizeWithColumns(0, "first", "second", "third");
        Optional<Row> row1 = table.addRow("a", 1L, 1.0f);
        Optional<Row> row2 = table.addRow("b", 2L, 2.0f);

        Table result = table.select(Column.names("first", "third"));

        assertThat(result).isNotNull();
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(2);
        List<Row> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(2);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(new Row("a", 1f), new Row("b", 2f));
    }

    @Test
    public void selectColumnsWhere_shouldReturnMatchingRowsWithNamedColumnValues() {
        Table table = Table.ofSizeWithColumns(0, "first", "second", "third");
        Optional<Row> row1 = table.addRow("a", 1L, 1.0f);
        Optional<Row> row2 = table.addRow("b", 2L, 2.0f);

        Table result = table.select(Column.names("first", "third"), Map.of(Column.name("second"), 1L));

        assertThat(result).isNotNull();
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(1);
        List<Row> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(1);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(new Row("a", 1f));
    }
}