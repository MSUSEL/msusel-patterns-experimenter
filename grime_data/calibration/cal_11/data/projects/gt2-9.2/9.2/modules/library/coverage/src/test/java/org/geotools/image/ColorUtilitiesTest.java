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
package org.geotools.image;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.image.IndexColorModel;

import org.geotools.resources.image.ColorUtilities;
import org.junit.Test;

public class ColorUtilitiesTest {

    @Test
    public void mergeSolidBackgroundSolidPalette() {
        byte[] r = new byte[] {-1, 0, 0};
        byte[] g = new byte[] {0, -1, 0};
        byte[] b = new byte[] {0, 0, -1};
        IndexColorModel icm = new IndexColorModel(2, 3, r, g, b);
        IndexColorModel merged = ColorUtilities.applyBackgroundColor(icm, Color.WHITE);
        assertFalse(merged.hasAlpha());
        assertEquals(merged.getTransparency(), Transparency.OPAQUE);
        byte[] mr = new byte[3];
        byte[] mb = new byte[3];
        byte[] mg = new byte[3];
        merged.getReds(mr);
        merged.getGreens(mg);
        merged.getBlues(mb);
        for (int i = 0; i < r.length; i++) {
            assertEquals(r[i], mr[i]);
            assertEquals(g[i], mg[i]);
            assertEquals(b[i], mb[i]);
        }
    }
    
    @Test
    public void mergeSolidBackgroundTranslucentPalette() {
        byte[] r = new byte[] {-1, 0, 0};
        byte[] g = new byte[] {0, -1, 0};
        byte[] b = new byte[] {0, 0, -1};
        byte[] a = new byte[] {-128, -128, -128};
        IndexColorModel icm = new IndexColorModel(2, 3, r, g, b, a);
        IndexColorModel merged = ColorUtilities.applyBackgroundColor(icm, Color.WHITE);
        assertFalse(merged.hasAlpha());
        assertEquals(merged.getTransparency(), Transparency.OPAQUE);
        byte[] mr = new byte[3];
        byte[] mb = new byte[3];
        byte[] mg = new byte[3];
        merged.getReds(mr);
        merged.getGreens(mg);
        merged.getBlues(mb);
        for (int i = 0; i < r.length; i++) {
            assertEquals((r[i] & 0xFF) == 255 ? 255 : 126, mr[i] & 0xFF);
            assertEquals((g[i] & 0xFF)  == 255 ? 255 : 126, mg[i] & 0xFF);
            assertEquals((b[i] & 0xFF)  == 255 ? 255 : 126, mb[i] & 0xFF);
        }
    }
    
    @Test
    public void mergeTranslucentBackgroundTranslucentPalette() {
        byte[] r = new byte[] {-1, 0, 0};
        byte[] g = new byte[] {0, -1, 0};
        byte[] b = new byte[] {0, 0, -1};
        byte[] a = new byte[] {-128, -128, -128};
        IndexColorModel icm = new IndexColorModel(2, 3, r, g, b, a);
        IndexColorModel merged = ColorUtilities.applyBackgroundColor(icm, new Color(255, 255, 255, 128));
        assertTrue(merged.hasAlpha());
        assertEquals(merged.getTransparency(), Transparency.TRANSLUCENT);
        byte[] mr = new byte[3];
        byte[] mb = new byte[3];
        byte[] mg = new byte[3];
        byte[] ma = new byte[3];
        merged.getReds(mr);
        merged.getGreens(mg);
        merged.getBlues(mb);
        merged.getAlphas(ma);
        for (int i = 0; i < r.length; i++) {
            assertEquals((r[i] & 0xFF) == 255 ? 255 : 84, mr[i] & 0xFF);
            assertEquals((g[i] & 0xFF)  == 255 ? 255 : 84, mg[i] & 0xFF);
            assertEquals((b[i] & 0xFF)  == 255 ? 255 : 84, mb[i] & 0xFF);
            assertEquals(191, ma[i] & 0xFF);
        }
    }
}
