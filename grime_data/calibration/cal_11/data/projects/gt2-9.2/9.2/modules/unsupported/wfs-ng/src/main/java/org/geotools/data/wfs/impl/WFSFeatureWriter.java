/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.geotools.data.wfs.impl;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.store.DiffContentFeatureWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class WFSFeatureWriter extends DiffContentFeatureWriter {

    final WFSRemoteTransactionState autoCommitState;

    final SimpleFeatureBuilder featureBuilder;

    public WFSFeatureWriter(final WFSContentFeatureStore store,
            final WFSLocalTransactionState localSate,
            final FeatureReader<SimpleFeatureType, SimpleFeature> reader, final boolean autoCommit) {

        super(store, localSate.getDiff(), reader);
        featureBuilder = new SimpleFeatureBuilder(getFeatureType(),
                new MutableIdentifierFeatureFactory());

        if (autoCommit) {
            WFSContentDataStore dataStore = (WFSContentDataStore) store.getDataStore();
            autoCommitState = new WFSRemoteTransactionState(dataStore);
            // TODO: revisit
            // autoCommitState.watch(localSate);
        } else {
            autoCommitState = null;
        }
    }

    @Override
    public void write() throws IOException {
        checkClosed();
        super.write();
        if (autoCommitState != null) {
            autoCommitState.commit();
        }
    }

    @Override
    public void remove() throws IOException {
        checkClosed();
        super.remove();
        if (autoCommitState != null) {
            autoCommitState.commit();
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        checkClosed();
        return super.hasNext();
    }

    @Override
    public synchronized SimpleFeature next() throws IOException {
        throw new UnsupportedOperationException("implementation needs to be revisited");
        // checkClosed();
        // SimpleFeatureType type = getFeatureType();
        // if (hasNext()) {
        // return super.next();
        // } else {
        // // Create new content with mutable fid
        // live = null;
        // next = null;
        // featureBuilder.reset();
        // String id = "new" + diff.nextFID;
        // current = featureBuilder.buildFeature(id, new Object[type.getAttributeCount()]);
        // diff.nextFID++;
        // return current;
        // }
    }

    private void checkClosed() throws IOException {
        if (reader == null) {
            throw new IOException("FeatureWriter is closed");
        }
    }

}
