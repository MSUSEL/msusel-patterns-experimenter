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
package org.geotools.process.vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.geotools.factory.FactoryIteratorProvider;
import org.geotools.factory.GeoTools;
import org.geotools.feature.NameImpl;
import org.geotools.process.Processors;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class VectorProcessFactoryTest {

    VectorProcessFactory factory = new VectorProcessFactory();

    @Test
    public void testLookup() {
        Set<Name> names = factory.getNames(); 
        assertFalse(names.isEmpty());
        assertTrue(names.contains(new NameImpl("vec", "Aggregate")));
    }
    
    @Test
    public void testAddCustomProcess() {
        assertNull(Processors.createProcess(new NameImpl("vec", "Custom")));
        
        FactoryIteratorProvider p = new FactoryIteratorProvider() {
            @Override
            public <T> Iterator<T> iterator(Class<T> category) {
                if (category == VectorProcess.class) {
                    return (Iterator<T>) Arrays.asList(new CustomProcess()).iterator();
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(p);
        try {
            Processors.reset();
            assertNotNull(Processors.createProcess(new NameImpl("vec", "Custom")));
        }
        finally {
            GeoTools.removeFactoryIteratorProvider(p);
        }
    }
    
    @DescribeProcess(title = "Custom", description = "Custom mock process")
    public static class CustomProcess implements VectorProcess {
        @DescribeResult(name = "result", description = "The result")
        public void execute() {
        }
    }
}
