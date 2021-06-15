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
package com.itextpdf.text.pdf.crypto;

/**
 * An initialization vector generator for a CBC block encryption. It's a random generator based on ARCFOUR.
 * @author Paulo Soares
 */
public final class IVGenerator {
    
    private static ARCFOUREncryption arcfour;
    
    static {
        arcfour = new ARCFOUREncryption();
        long time = System.currentTimeMillis();
        long mem = Runtime.getRuntime().freeMemory();
        String s = time + "+" + mem;
        arcfour.prepareARCFOURKey(s.getBytes());
    }
    
    /** Creates a new instance of IVGenerator */
    private IVGenerator() {
    }
    
    /**
     * Gets a 16 byte random initialization vector.
     * @return a 16 byte random initialization vector
     */
    public static byte[] getIV() {
        return getIV(16);
    }
    
    /**
     * Gets a random initialization vector.
     * @param len the length of the initialization vector
     * @return a random initialization vector
     */
    public static byte[] getIV(int len) {
        byte[] b = new byte[len];
        synchronized (arcfour) {
            arcfour.encryptARCFOUR(b);
        }
        return b;
    }    
}