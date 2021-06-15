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
package org.geotools.wcs.v2_0;

import static org.junit.Assert.*;

import java.util.List;

import net.opengis.wcs20.DescribeCoverageType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class DescribeCoverageTest {

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseDescribeCoverage() throws Exception {
        String capRequestPath = "requestDescribeCoverage.xml";
        DescribeCoverageType dc = (DescribeCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", dc.getService());
        assertEquals("2.0.1", dc.getVersion());

        List<String> ids = dc.getCoverageId();
        assertEquals(2, ids.size());
        assertEquals("C0001", ids.get(0));
        assertEquals("C0002", ids.get(1));
    }

}
