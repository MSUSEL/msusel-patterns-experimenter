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
package org.geotools.jdbc;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCDateTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCDateTestSetup createTestSetup();

    public void testMappings() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema( tname("dates") );
        
        assertEquals( Date.class, ft.getDescriptor( aname("d") ).getType().getBinding() );
        assertEquals( Timestamp.class, ft.getDescriptor( aname("dt") ).getType().getBinding() );
        assertEquals( Time.class, ft.getDescriptor( aname("t") ).getType().getBinding() );
    }
    
    public void testFiltersByDate() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource( tname("dates") );
        
        FilterFactory ff = dataStore.getFilterFactory();
        
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
        df.setTimeZone( TimeZone.getTimeZone("PST"));
        
        //less than
        Filter f = ff.lessOrEqual( ff.property( aname("d") ), ff.literal( df.parse("2009-28-06")
         ) );
        assertEquals( 2, fs.getCount( new DefaultQuery(tname("dates"),f ) ) );
        
        f = ff.lessOrEqual( ff.property( aname("d") ),ff.literal( df.parse("2009-28-06") ) );
        assertEquals( 2, fs.getCount( new DefaultQuery(tname("dates"),f ) ) );
    }
    
    public void testFilterByTimeStamp() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource( tname("dates") );
        
        FilterFactory ff = dataStore.getFilterFactory();
        
        //equal to
        Filter f = ff.equals( ff.property( aname("dt") ), ff.literal( "2009-06-28 15:12:41" ) );
        assertEquals( 1, fs.getCount( new DefaultQuery(tname("dates"),f ) ) );
        
        f = ff.equals( ff.property( aname("dt") ), 
                ff.literal( new SimpleDateFormat("HH:mm:ss,dd-yyyy-MM").parse("15:12:41,28-2009-06") ) );
        assertEquals( 1, fs.getCount( new DefaultQuery(tname("dates"),f ) ) );
    }
    
    public void testFilterByTime() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource( tname("dates") );
        
        FilterFactory ff = dataStore.getFilterFactory();
        
        // greather than or equal to
        Filter f = ff.greaterOrEqual( ff.property( aname("t") ), ff.literal( "13:10:12" ) );
        assertEquals( 3, fs.getCount( new DefaultQuery(tname("dates"),f ) ) );
        
        f = ff.greaterOrEqual( ff.property( aname("t") ), 
                ff.literal( new SimpleDateFormat("ss:HH:mm").parse("12:13:10") ) );
        assertEquals( 3, fs.getCount( new DefaultQuery(tname("dates"),f ) ) );
    }
}
