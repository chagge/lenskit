/*
 * LensKit, an open source recommender systems toolkit.
 * Copyright 2010-2012 Regents of the University of Minnesota and contributors
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.grouplens.lenskit.util.tablewriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiplexedTableWriter implements TableWriter {

    private TableLayout layout;
    private List<TableWriter> writers;

    public MultiplexedTableWriter(TableLayout layout, List<TableWriter> writers) {
        this.layout = layout;
        this.writers = writers;
    }

    public MultiplexedTableWriter(TableLayout layout, TableWriter... writers) {
        this(layout, Arrays.asList(writers));
    }

    @Override
    public TableLayout getLayout() {
        return layout;
    }

    @Override
    public void writeRow(Object[] row) throws IOException {
        for (TableWriter w : writers) {
            w.writeRow(row);
        }

    }

    @Override
    public void close() throws IOException {
        ArrayList<IOException> closeExceptions = new ArrayList<IOException>(writers.size());
        for (TableWriter w : writers) {
            try {
                w.close();
            } catch (IOException e) {
                closeExceptions.add(e);
            }
        }
        if (!closeExceptions.isEmpty()) {
            throw closeExceptions.get(0);
        }
    }
}
