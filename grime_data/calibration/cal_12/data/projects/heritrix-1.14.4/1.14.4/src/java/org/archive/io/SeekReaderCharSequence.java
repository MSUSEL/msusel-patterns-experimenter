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
package org.archive.io;

import java.io.IOException;

public class SeekReaderCharSequence implements CharSequence {

    
    final private SeekReader reader;
    final private int size;
    

    public SeekReaderCharSequence(SeekReader reader, int size) {
        this.reader = reader;
        this.size = size;
    }
    
    
    public int length() {
        return size;
    }
    
    
    public char charAt(int index) {
        if ((index < 0) || (index >= length())) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        try {
            reader.position(index);
            int r = reader.read();
            if (r < 0) {
                throw new IllegalStateException("EOF");
            }
            return (char)reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public CharSequence subSequence(int start, int end) {
        return new CharSubSequence(this, start, end);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            reader.position(0);
            for (int ch = reader.read(); ch >= 0; ch = reader.read()) {
                sb.append((char)ch);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
