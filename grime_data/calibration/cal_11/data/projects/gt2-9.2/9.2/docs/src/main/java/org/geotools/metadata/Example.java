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
package org.geotools.metadata;

import org.geotools.util.Utilities;

public class Example {
    
    private Object field1;
    private int field2;
    private double array[];
    
    @Override
    public int hashCode() {
        int result = 1;
        result = Utilities.hash(field1, result);
        result = Utilities.hash(field2, result);
        result = Utilities.hash(array, result);
        
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Example)) {
            return false;
        }
        Example other = (Example) obj;
        return Utilities.equals(field1, other.field1) && Utilities.equals(field2, other.field2)
                && Utilities.deepEquals(array, other.array);
    }
    
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Example[");
        if (field1 != null) {
            build.append(" field1=");
            build.append(field1);
        }
        build.append(" field2=");
        build.append(field2);
        if (array != null) {
            build.append(" array=");
            build.append(Utilities.deepToString(array));
        }
        build.append("]");
        return build.toString();
    }
}