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
package org.archive.util;

/**
 * @author stack
 * @version $Date: 2005-08-15 23:35:10 +0000 (Mon, 15 Aug 2005) $ $Revision: 3760 $
 */
public class PropertyUtils {
    /***
     * @param key Property key.
     * @return Named property or null if the property is null or empty.
     */
    public static String getPropertyOrNull(final String key) {
        String value = System.getProperty(key);
        return (value == null || value.length() <= 0)? null: value;
    }

    /***
     * @param key Property key.
     * @return Boolean value or false if null or unreadable.
     */
    public static boolean getBooleanProperty(final String key) {
        return (getPropertyOrNull(key) == null)?
                false: Boolean.valueOf(getPropertyOrNull(key)).booleanValue();
    }   
    
    /**
     * @param key Key to use looking up system property.
     * @param fallback If no value found for passed <code>key</code>, return
     * <code>fallback</code>.
     * @return Value of property or <code>fallback</code>.
     */
    public static int getIntProperty(final String key, final int fallback) {
        return getPropertyOrNull(key) == null?
                fallback: Integer.parseInt(getPropertyOrNull(key));
    }
}
