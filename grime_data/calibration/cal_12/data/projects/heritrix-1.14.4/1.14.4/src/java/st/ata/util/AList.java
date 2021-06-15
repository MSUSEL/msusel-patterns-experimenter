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

package st.ata.util;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

// Tested by: none (interface)

/** Map from string keys to values.  The mapping is "strongly types",
 *  e.g., a value saved as an "int" cannot be retrieved as a "long" or
 *  a "string".  Throws {@link ClassCastException} if a type error
 *  occurs, {@link java.util.NoSuchElementException} if the key is not
 *  in the table. */

public interface AList {
    public static final int F_ARRAY = 128;
    public static final int F_ARRAY_ARRAY = 256;
    public static final int T_ALIST = 1;
    public static final int T_DATE = 2;
    public static final int T_INT = 3;
    public static final int T_LONG = 4;
    public static final int T_STRING = 5;
    public static final int T_INPUTSTREAM = 6;
    public static final int T_OBJECT = 7;
    public static final int T_UNDEFINED = 0;

    /** Returns an iterator of {@link String} containing all keys in
     *  this list */
    public Iterator getKeys();

    public boolean containsKey(String key);

    public Object getObject(String key);
    public void putObject(String key, Object val);
    public Object clone();
    public void remove(String key);
    /** Returns an array of {@link String} containing all keys in
     *  this list */

    public String[] getKeyArray();
    public int getInt(String key);

    public long getLong(String key);

    public String getString(String key);

    public AList getAList(String key);

    public Date getDate(String key);

    public InputStream getInputStream(String key);

    public int[] getIntArray(String key);

    public long[] getLongArray(String key);

    public String[] getStringArray(String key);

    public AList[] getAListArray(String key);

    public Date[] getDateArray(String key);

    public InputStream[] getInputStreamArray(String key);

    public String[][] getStringArrayArray(String key);

    public void putInt(String key, int value);

    public void putLong(String key, long value);

    public void putString(String key, String value);

    public void putAList(String key, AList value);

    public void putDate(String key, Date value);

    public void putInputStream(String key, InputStream value);

    public void putIntArray(String key, int[] value);

    public void putLongArray(String key, long[] value);

    public void putStringArray(String key, String[] value);

    public void putAListArray(String key, AList[] value);

    public void putDateArray(String key, Date[] value);

    public void putInputStreamArray(String key, InputStream[] value);

    public void putStringArrayArray(String key, String[][] value);

    /** Return the type of the value associated with a key.  Returns
     *  one of either {@link #T_UNDEFINED}, if the key is not in the
     *  table, or {@link #T_ALIST}, {@link #T_DATE}, {@link #T_INT},
     *  {@link #T_LONG}, {@link #T_STRING}, {@link #T_STRING},
     *  {@link #T_INPUTSTREAM} or {@link #F_ARRAY}
     *  bitwise-ored with any of those. */
    public int getType(String key);


    /**
     *  Closes the object and releases any resources
     *  (for example, InputStreams) held by it.
     */
    public void close();

    public AList newAList();

    public void clear();

    /**
     * Copy the iterator's keys from one AList to another, replacing any 
     * entries that exist in the destination.
     * 
     * @param keys Iterator of String keys
     * @param other source AList
     */
    public void copyKeysFrom(Iterator keys, AList other);
    
    /**
     * Copy the iterator's keys from one AList to another. 
     * 
     * @param keys Iterator of String keys
     * @param other source AList
     * @param clobber whether to replace entries that exist in the destination
     */
    public void copyKeysFrom(Iterator keys, AList other, boolean clobber);

    /**
     * Provides a somewhat pretty (matching brackets for nesting) 
     * string of AList. 
     * 
     * @return pretty String
     */
    public String toPrettyString();
}
