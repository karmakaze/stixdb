package org.keithkim.stixdb.core;

import org.junit.Test;
import org.keithkim.stixdb.core.util.RowValues;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.keithkim.stixdb.core.util.Values.values;

public class IndexTest {

    @Test
    public void createIndex_storesColumnNumber() throws Exception {
        Index index = new Index(0);
        assertThat(index.column).isEqualTo(0);

        index = new Index(1);
        assertThat(index.column).isEqualTo(1);
    }

    @Test
    public void rowAdded_updatesIndexWithRow() throws Exception {
        Index index = new Index(1);
        RowValues row1 = RowValues.with(values(1, "a"));

        index.rowAdded(row1);

        assertThat(index.valueRows).hasSize(1);
        assertThat(index.valueRows).containsKey("a");
        assertThat(index.valueRows).containsEntry("a", asList(row1));
    }
}