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
package org.geotools.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A linked HashMap set up for easy construction.
 * <p>
 * Example: <code>KVP map = new KVP("foo",1,"bar,2);</code>
 * 
 * @author jody
 *
 *
 *
 * @source $URL$
 */
public class KVP extends LinkedHashMap<String, Object> {
    /**
     * 
     */
    private static final long serialVersionUID = -387821381125137128L;

    /**
     * A linked HashMap set up for easy construction.
     * <p>
     * Example: <code>KVP map = new KVP("foo",1,"bar,2);</code>
     * 
     * @param pairs
     */
    public KVP(Object... pairs) {
        if ((pairs.length & 1) != 0) {
            throw new IllegalArgumentException("Pairs was not an even number");
        }
        for (int i = 0; i < pairs.length; i += 2) {
            String key = (String) pairs[i];
            Object value = pairs[i + 1];
            add(key, value);
        }
    }
    /**
     * An additive version of put; will add additional values resulting
     * in a list.
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void add(String key, Object value) {
        if( containsKey(key) ) {
            Object existing = get( key );
            if( existing instanceof List<?>){
                List<Object> list = (List<Object>) existing;
                list.add( value );
            }
            else {
                List<Object> list = new ArrayList<Object>();
                list.add( existing );
                list.add( value );
                put( key, list );
            }
        }
        else {
            put(key, value);
        }
    }
}
