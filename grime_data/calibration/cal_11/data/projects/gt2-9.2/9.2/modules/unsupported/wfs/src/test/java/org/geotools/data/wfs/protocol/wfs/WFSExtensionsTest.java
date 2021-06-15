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
package org.geotools.data.wfs.protocol.wfs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.junit.Test;

/**
 * @author Gabriel Roldan
 * @since 2.6.x
 *
 *
 *
 * @source $URL$
 * @version $Id$
 */
@SuppressWarnings("nls")
public class WFSExtensionsTest {

    @Test
    public void testFindParserFactory() {
        GetFeatureType request = WfsFactory.eINSTANCE.createGetFeatureType();
        request.setOutputFormat("application/fakeFormat");
        WFSResponseParserFactory factory = WFSExtensions.findParserFactory(request);
        assertNotNull(factory);
        assertTrue(factory instanceof TestParserFactory);
    }

    public static class TestParserFactory implements WFSResponseParserFactory {

        public boolean canProcess( EObject request ) {
            return request instanceof GetFeatureType
                    && "application/fakeFormat"
                            .equals(((GetFeatureType) request).getOutputFormat());
        }
        public boolean isAvailable() {
            return true;
        }

        public WFSResponseParser createParser( WFS_1_1_0_DataStore wfs, WFSResponse response )
                throws IOException {
            throw new UnsupportedOperationException("not intended to be called for this test class");
        }
    }
}
