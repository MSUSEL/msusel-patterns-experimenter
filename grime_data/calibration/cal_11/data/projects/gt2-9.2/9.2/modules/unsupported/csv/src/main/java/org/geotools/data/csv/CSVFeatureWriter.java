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
package org.geotools.data.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.Point;

// Obviously, WIP...
/**
 * 
 *
 * @source $URL$
 */
public class CSVFeatureWriter extends CSVFeatureReader implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    private CsvWriter csvWriter;
    private File tempFile;
    private SimpleFeature each;
    private boolean append = false;
    private boolean appending = false;  // TODO this is way ugly
    
    public CSVFeatureWriter(ContentState contentState) throws IOException {
        super(contentState);
        this.tempFile = File.createTempFile("cvsDataStore", ".csv");
        this.csvWriter = new CsvWriter(new FileWriter(this.tempFile), ',');
        this.csvWriter.writeRecord(this.reader.getHeaders());
    }

    public CSVFeatureWriter(ContentState contentState, Query query, boolean append) throws IOException {
        this(contentState);
        if (append) {
            this.appending = true;
            while(this.hasNext()) {
                this.each = this.next();
                this.write();
            }
            this.appending = false;
        }
        this.append = append;
    }

    public void remove() throws IOException {
        this.each = null;       // just mark it done which means it will not get written out.  
    }

    @Override
    public boolean hasNext() throws IOException {
        if (this.append) {
            return false;
        }
        return super.hasNext();
    }

    public void write() throws IOException {
        if (this.each == null) {
            return;
        }
        for (int i = 0; i < this.each.getAttributeCount(); i++) {
            Object attr = this.each.getAttribute(i);
            if (attr instanceof Point) {
                Point point = (Point) attr;
                this.csvWriter.write(Double.toString(point.getX()));
                this.csvWriter.write(Double.toString(point.getY()));
            } else {
                this.csvWriter.write(attr.toString());
            }
        }
        this.csvWriter.endRecord();
        this.each = null;       // indicate that it has been written
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        if (this.append) {
            this.builder.addAll(new Object[this.state.getEntry().getDataStore().getSchema(this.getFeatureType().getTypeName()).getAttributeCount()]);
            this.each = this.buildFeature();
            return this.each;
        }
        this.checkPendingWrite();
        return this.each = super.next();
    }

    @Override
    public void close() throws IOException {
        if (this.appending) {
            return;
        }
        this.checkPendingWrite();
        super.close();
        this.csvWriter.close();
        ((CSVDataStore) this.state.getEntry().getDataStore()).write(this.tempFile);
    }

    private void checkPendingWrite() throws IOException {
        if (this.each != null) {
            // the previous one was not written, so do it now.
            this.write();
        }
    }
}
