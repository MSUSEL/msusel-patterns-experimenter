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

package org.hsqldb.store;

import java.math.BigDecimal;

import org.hsqldb.types.TimestampData;

/**
  * Supports pooling of Integer, Long, Double, BigDecimal, String and Date
  * Java Objects. Leads to reduction in memory use when an Object is used more
  * then twice in the database.
  *
  * getXXX methods are used for retrival of values. If a value is not in
  * the pool, it is added to the pool and returned. When the pool gets
  * full, half the contents that have been accessed less recently are purged.
  *
  * @author Fred Toussi (fredt@users dot sourceforge.net)
  * @version 1.9.0
  * @since 1.7.2
  */
public class ValuePool {

    //
    static ValuePoolHashMap intPool;
    static ValuePoolHashMap longPool;
    static ValuePoolHashMap doublePool;
    static ValuePoolHashMap bigdecimalPool;
    static ValuePoolHashMap stringPool;
    static ValuePoolHashMap datePool;
    static final int        SPACE_STRING_SIZE       = 50;
    static final int        DEFAULT_VALUE_POOL_SIZE = 8192;
    static final int[]      defaultPoolLookupSize   = new int[] {
        DEFAULT_VALUE_POOL_SIZE, DEFAULT_VALUE_POOL_SIZE,
        DEFAULT_VALUE_POOL_SIZE, DEFAULT_VALUE_POOL_SIZE,
        DEFAULT_VALUE_POOL_SIZE, DEFAULT_VALUE_POOL_SIZE
    };
    static final int POOLS_COUNT            = defaultPoolLookupSize.length;
    static final int defaultSizeFactor      = 2;
    static final int defaultMaxStringLength = 16;

    //
    static ValuePoolHashMap[] poolList;

    //
    static int maxStringLength;

    //
    static {
        initPool();
    }

    public static final Integer INTEGER_0 = ValuePool.getInt(0);
    public static final Integer INTEGER_1 = ValuePool.getInt(1);
    public static final Integer INTEGER_2 = ValuePool.getInt(2);
    public static final Integer INTEGER_MAX =
        ValuePool.getInt(Integer.MAX_VALUE);
    public static final BigDecimal BIG_DECIMAL_0 =
        ValuePool.getBigDecimal(new BigDecimal(0.0));
    public static final BigDecimal BIG_DECIMAL_1 =
        ValuePool.getBigDecimal(new BigDecimal(1.0));

    //
    public final static String[] emptyStringArray = new String[]{};
    public final static Object[] emptyObjectArray = new Object[]{};
    public final static int[]    emptyIntArray    = new int[]{};
    public static String         spaceString;

    //
    private static void initPool() {

        int[] sizeArray  = defaultPoolLookupSize;
        int   sizeFactor = defaultSizeFactor;

        synchronized (ValuePool.class) {
            maxStringLength = defaultMaxStringLength;
            poolList        = new ValuePoolHashMap[POOLS_COUNT];

            for (int i = 0; i < POOLS_COUNT; i++) {
                int size = sizeArray[i];

                poolList[i] = new ValuePoolHashMap(size, size * sizeFactor,
                                                   BaseHashMap.PURGE_HALF);
            }

            intPool        = poolList[0];
            longPool       = poolList[1];
            doublePool     = poolList[2];
            bigdecimalPool = poolList[3];
            stringPool     = poolList[4];
            datePool       = poolList[5];

            char[] c = new char[SPACE_STRING_SIZE];

            for (int i = 0; i < SPACE_STRING_SIZE; i++) {
                c[i] = ' ';
            }

            spaceString = new String(c);
        }
    }

    public static int getMaxStringLength() {
        return maxStringLength;
    }

    public static void resetPool(int[] sizeArray, int sizeFactor) {

        synchronized (ValuePool.class) {
            for (int i = 0; i < POOLS_COUNT; i++) {
                poolList[i].resetCapacity(sizeArray[i] * sizeFactor,
                                          BaseHashMap.PURGE_HALF);
            }
        }
    }

    public static void resetPool() {

        synchronized (ValuePool.class) {
            resetPool(defaultPoolLookupSize, defaultSizeFactor);
        }
    }

    public static void clearPool() {

        synchronized (ValuePool.class) {
            for (int i = 0; i < POOLS_COUNT; i++) {
                poolList[i].clear();
            }
        }
    }

    public static Integer getInt(int val) {

        synchronized (intPool) {
            return intPool.getOrAddInteger(val);
        }
    }

    public static Long getLong(long val) {

        synchronized (longPool) {
            return longPool.getOrAddLong(val);
        }
    }

    public static Double getDouble(long val) {

        synchronized (doublePool) {
            return doublePool.getOrAddDouble(val);
        }
    }

    public static String getString(String val) {

        if (val == null || val.length() > maxStringLength) {
            return val;
        }

        synchronized (stringPool) {
            return stringPool.getOrAddString(val);
        }
    }

    public static String getSubString(String val, int start, int limit) {

        synchronized (stringPool) {
            return stringPool.getOrAddString(val.substring(start, limit));
        }
    }

    public static TimestampData getDate(long val) {

        synchronized (datePool) {
            return datePool.getOrAddDate(val);
        }
    }

    public static BigDecimal getBigDecimal(BigDecimal val) {

        if (val == null) {
            return val;
        }

        synchronized (bigdecimalPool) {
            return (BigDecimal) bigdecimalPool.getOrAddObject(val);
        }
    }

    public static Boolean getBoolean(boolean b) {
        return b ? Boolean.TRUE
                 : Boolean.FALSE;
    }
}
