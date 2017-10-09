package org.keithkim.stixdb.core;

import org.junit.Test;
import org.keithkim.stixdb.core.util.RowValues;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.util.Values.values;

public class TableTest {
    @Test
    public void createTable() {
        Table table = Table.ofSizeWithColumns();

        assertThat(table.rowCount()).describedAs("rowCount").isEqualTo(0);
    }

    @Test
    public void addRowWithNoValues_shouldAddRowWithoutValues() {
        Table table = Table.ofSizeWithColumns();
        Optional<RowValues> row_ = table.addRow();

        assertThat(row_).isNotEmpty();

        RowValues row = row_.get();
        assertThat(row.values()).describedAs("rowValues").isNotNull();
        assertThat(row.values()).describedAs("rowValues").isEmpty();
    }

    @Test
    public void addRowWithValues_shouldAddRowWithValues() {
        Table table = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> row_ = table.addRow("a", 1L, 1.0f);

        assertThat(row_).isNotEmpty();

        RowValues row = row_.get();
        assertThat(row.values()).describedAs("rowValues").isNotNull();
        assertThat(row.values()).describedAs("rowValues").hasSize(3);
        assertThat(row.values()).describedAs("").contains("a", 1L, 1.0f);
    }

    @Test
    public void selectColumnsFromEmptyTable_shouldReturnNoRows() {
        Table table = Table.ofSizeWithColumns("first", "second", "third");

        Table result = table.select(Column.names("first", "third"));
        assertThat(result).isNotNull();
        assertThat(result.rowCount()).describedAs("rowCount").isZero();
    }

    @Test
    public void selectColumns_shouldReturnAllRowsWithNamedColumnValues() {
        Table table = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> row1 = table.addRow("a", 1L, 1.0f);
        Optional<RowValues> row2 = table.addRow("b", 2L, 2.0f);

        Table result = table.select(Column.names("first", "third"));

        assertThat(result).isNotNull();
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(2);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(2);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(RowValues.with(values("a", 1f)), RowValues.with(values("b", 2f)));
    }

    @Test
    public void selectColumnsWhere_shouldReturnMatchingRowsWithNamedColumnValues() {
        Table table = Table.ofSizeWithColumns("first", "second", "third");
        Optional<RowValues> row1 = table.addRow("a", 1L, 1.0f);
        Optional<RowValues> row2 = table.addRow("b", 2L, 2.0f);

        Table result = table.select(Column.names("first", "third"), Map.of(Column.name("second"), 1L));

        assertThat(result).isNotNull();
        assertThat(result.rowCount()).describedAs("rowCount").isEqualTo(1);
        List<RowValues> rows = result.rows();
        assertThat(rows).describedAs("rows").hasSize(1);
        assertThat(rows).describedAs("rows").containsExactlyInAnyOrder(RowValues.with(values("a", 1f)));
    }
}