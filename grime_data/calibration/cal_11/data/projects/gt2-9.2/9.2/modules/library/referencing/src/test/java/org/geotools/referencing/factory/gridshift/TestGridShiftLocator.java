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
package org.geotools.referencing.factory.gridshift;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.geotools.factory.AbstractFactory;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.logging.Logging;
import org.opengis.metadata.citation.Citation;

/**
 * Test helper that can load compressed grids
 */
public class TestGridShiftLocator  extends AbstractFactory implements GridShiftLocator {

    static final Logger LOGGER = Logging.getLogger(TestGridShiftLocator.class);

    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    @Override
    public URL locateGrid(String grid) {
        GZIPInputStream is = null;
        FileOutputStream fos = null;
        try {
            URL compressed = getClass().getResource(grid + ".gz");
            if (compressed != null) {
                is = new GZIPInputStream(compressed.openStream());
                File out = new File("./target/" + grid);
                fos = new FileOutputStream(out);
                byte[] buf = new byte[1024];
                int read;
                while ((read = is.read(buf)) > 0) {
                    fos.write(buf, 0, read);
                }

                return out.toURI().toURL();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to unpack the grid", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                //
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                //
            }
        }

        return null;
    }

}
