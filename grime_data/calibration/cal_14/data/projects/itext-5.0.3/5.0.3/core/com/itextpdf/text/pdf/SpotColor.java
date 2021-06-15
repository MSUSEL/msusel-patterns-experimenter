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
package com.itextpdf.text.pdf;

/**
 *
 * @author  psoares
 */
public class SpotColor extends ExtendedColor {

    private static final long serialVersionUID = -6257004582113248079L;
	PdfSpotColor spot;
    float tint;

    public SpotColor(PdfSpotColor spot, float tint) {
        super(TYPE_SEPARATION,
            (spot.getAlternativeCS().getRed() / 255f - 1f) * tint + 1,
            (spot.getAlternativeCS().getGreen() / 255f - 1f) * tint + 1,
            (spot.getAlternativeCS().getBlue() / 255f - 1f) * tint + 1);
        this.spot = spot;
        this.tint = tint;
    }
    
    public PdfSpotColor getPdfSpotColor() {
        return spot;
    }
    
    public float getTint() {
        return tint;
    }

    public boolean equals(Object obj) {
        return this == obj;
    }
    
    public int hashCode() {
        return spot.hashCode() ^ Float.floatToIntBits(tint);
    }
}
