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
package org.geotools.swing.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A Swing {@code TableModel} to retrieve attribute values from
 * each feature in a feature collection and cache them for a
 * {@code JTable}
 * <p>
 *
 *
 *
 *
 * @source $URL$
 */
public class FeatureCollectionTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -7119885084300393935L;

    private SimpleFeatureType schema;
    
    List<Object[]> cache = new ArrayList<Object[]>();

    public IOException exception;

    /**
     * A worker class to get the attributes of each feature and load 
     * them into the {@code TableModel}. The work is performed on a
     * background thread.
     */
    class TableWorker extends SwingWorker<List<Object[]>, Object[]> {
        SimpleFeatureCollection features;

        /**
         * Constructor
         *
         * @param features the feature collection to be loaded into the table
         */
        TableWorker( SimpleFeatureCollection features ) { 
            this.features = features;            
        }

        /**
         * {@code SwingWorker} method to visit each feature and retrieve
         * its attributes
         */
        public List<Object[]> doInBackground() {
            List<Object[]> list = new ArrayList<Object[]>();
            
            final NullProgressListener listener = new NullProgressListener();
            try {
                features.accepts( new FeatureVisitor() {                
                    public void visit(Feature feature) {
                        SimpleFeature simple = (SimpleFeature) feature;
                        Object[] values = simple.getAttributes().toArray();
                        ArrayList<Object> row = new ArrayList<Object>( Arrays.asList( values ));
                        row.add(0, simple.getID() );
                        publish( row.toArray() );
                        
                        if( isCancelled() ) listener.setCanceled(true);
                    }
                } , listener );
            } catch (IOException e) {
                exception = e;
            }           
            return list;
        }

        /**
         * Add a batch of feature data to the table
         *
         * @param chunks batch of feature data
         */
        @Override
        protected void process(List<Object[]> chunks) {            
            int from = cache.size();
            cache.addAll( chunks );
            int to = cache.size();
            fireTableRowsInserted( from, to );
        }        
    }

    TableWorker load;

    /**
     * Constructor
     *
     * @param features the feature collection to load into the table
     */
    public FeatureCollectionTableModel( SimpleFeatureCollection features ){
        this.load = new TableWorker( features );
        load.execute();
        this.schema = features.getSchema();
    }

    /**
     * Cancel the running job, if any
     */
    public void dispose() {
        load.cancel(false);        
    }

    /**
     * Retrieve the specified column name
     *
     * @param column column index
     *
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
    	if( column == 0 ){
    		return "FeatureIdentifer";
    	}
        return schema.getDescriptor( column-1 ).getLocalName();
    }

    /**
     * Get the number of columns in the table
     *
     * @return the number of columns
     */
    public int getColumnCount() {
        if( exception != null ){
            return 1;
        }
        return schema.getAttributeCount()+1;
    }

    /**
     * Get the number of rows in the table
     *
     * @return the number of rows
     */
    public int getRowCount() {
        if( exception != null ){
            return 1;
        }
        return cache.size();
    }

    /**
     * Get the value of a specified table entry
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     *
     * @return the table entry
     */
    public Object getValueAt(int rowIndex, int columnIndex) {    	
        if ( rowIndex < cache.size() ){
            Object row[] = cache.get( rowIndex );
            return row[ columnIndex ];
        }
        return null;
    }

}
