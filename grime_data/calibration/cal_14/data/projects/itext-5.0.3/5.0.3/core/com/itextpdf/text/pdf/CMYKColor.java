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
 * @author  Paulo Soares
 */
public class CMYKColor extends ExtendedColor {

    private static final long serialVersionUID = 5940378778276468452L;
	float cyan;
    float magenta;
    float yellow;
    float black;

    /**
     * Constructs a CMYK Color based on 4 color values (values are integers from 0 to 255).
     * @param intCyan
     * @param intMagenta
     * @param intYellow
     * @param intBlack
     */
    public CMYKColor(int intCyan, int intMagenta, int intYellow, int intBlack) {
        this(intCyan / 255f, intMagenta / 255f, intYellow / 255f, intBlack / 255f);
    }

    /**
     * Construct a CMYK Color.
     * @param floatCyan
     * @param floatMagenta
     * @param floatYellow
     * @param floatBlack
     */
    public CMYKColor(float floatCyan, float floatMagenta, float floatYellow, float floatBlack) {
        super(TYPE_CMYK, 1f - floatCyan - floatBlack, 1f - floatMagenta - floatBlack, 1f - floatYellow - floatBlack);
        cyan = normalize(floatCyan);
        magenta = normalize(floatMagenta);
        yellow = normalize(floatYellow);
        black = normalize(floatBlack);
    }
    
    /**
     * @return the cyan value
     */
    public float getCyan() {
        return cyan;
    }

    /**
     * @return the magenta value
     */
    public float getMagenta() {
        return magenta;
    }

    /**
     * @return the yellow value
     */
    public float getYellow() {
        return yellow;
    }

    /**
     * @return the black value
     */
    public float getBlack() {
        return black;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CMYKColor))
            return false;
        CMYKColor c2 = (CMYKColor)obj;
        return (cyan == c2.cyan && magenta == c2.magenta && yellow == c2.yellow && black == c2.black);
    }
    
    public int hashCode() {
        return Float.floatToIntBits(cyan) ^ Float.floatToIntBits(magenta) ^ Float.floatToIntBits(yellow) ^ Float.floatToIntBits(black); 
    }
    
}
