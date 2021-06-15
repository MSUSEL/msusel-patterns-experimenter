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
package org.geotools.gml3;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.Schemas;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class SchemasTest {

    @Test
    public void testConcurrentParse() throws Exception {
        URL location = SchemasTest.class.getResource("states.xsd");
        final File schemaFile = new File(location.toURI());
        final List locators = Arrays.asList( GML.getInstance().createSchemaLocator() );
        
        ExecutorService es = Executors.newFixedThreadPool(32);
        List<Future<Void>> results = new ArrayList<Future<Void>>();
        for(int i = 0; i < 128; i++) {
            Future<Void> future = es.submit(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    XSDSchema schema = Schemas.parse( schemaFile.getAbsolutePath(), locators, null );
                    Schemas.dispose(schema);
                    return null;
                }
                
            });
            results.add(future);
        }
        
        // make sure none threw an exception
        for (Future<Void> future : results) {
            future.get();
        }
    }
}
