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
package org.geotools.styling.builder;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.SLDTransformer;
import org.opengis.filter.FilterFactory2;

/**
 * 
 *
 * @source $URL$
 */
public abstract class AbstractStyleTest {

    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    protected void print(Object styleObject) {
        try {
            SLDTransformer tx = new SLDTransformer();
            tx.setIndentation(2);
            System.out.println(tx.transform(styleObject));
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while printing the style", e);
        }
    }

    protected void assertSimpleStyle(StyleCollector collector) {
        assertEquals(1, collector.featureTypeStyles.size());
        assertEquals(1, collector.rules.size());
        assertEquals(1, collector.symbolizers.size());
        assertEquals(1, collector.styles.size());
        assertTrue(collector.layers.size() == 0 || collector.layers.size() == 1);
    }

}
