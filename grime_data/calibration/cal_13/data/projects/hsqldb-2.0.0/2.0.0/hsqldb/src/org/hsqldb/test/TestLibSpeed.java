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

package org.hsqldb.test;

import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.IntKeyHashMap;
import org.hsqldb.lib.IntKeyIntValueHashMap;
import org.hsqldb.lib.IntValueHashMap;
import org.hsqldb.lib.StopWatch;

/**
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 */
public class TestLibSpeed {

    static final String[][] sNumeric = {
        {
            "ABS", "org.hsqldb.Library.abs"
        }, {
            "ACOS", "java.lang.Math.acos"
        }, {
            "ASIN", "java.lang.Math.asin"
        }, {
            "ATAN", "java.lang.Math.atan"
        }, {
            "ATAN2", "java.lang.Math.atan2"
        }, {
            "CEILING", "java.lang.Math.ceil"
        }, {
            "COS", "java.lang.Math.cos"
        }, {
            "COT", "org.hsqldb.Library.cot"
        }, {
            "DEGREES", "java.lang.Math.toDegrees"
        }, {
            "EXP", "java.lang.Math.exp"
        }, {
            "FLOOR", "java.lang.Math.floor"
        }, {
            "LOG", "java.lang.Math.log"
        }, {
            "LOG10", "org.hsqldb.Library.log10"
        }, {
            "MOD", "org.hsqldb.Library.mod"
        }, {
            "PI", "org.hsqldb.Library.pi"
        }, {
            "POWER", "java.lang.Math.pow"
        }, {
            "RADIANS", "java.lang.Math.toRadians"
        }, {
            "RAND", "java.lang.Math.random"
        }, {
            "ROUND", "org.hsqldb.Library.round"
        }, {
            "SIGN", "org.hsqldb.Library.sign"
        }, {
            "SIN", "java.lang.Math.sin"
        }, {
            "SQRT", "java.lang.Math.sqrt"
        }, {
            "TAN", "java.lang.Math.tan"
        }, {
            "TRUNCATE", "org.hsqldb.Library.truncate"
        }, {
            "BITAND", "org.hsqldb.Library.bitand"
        }, {
            "BITOR", "org.hsqldb.Library.bitor"
        }, {
            "ROUNDMAGIC", "org.hsqldb.Library.roundMagic"
        }
    };
    static HashSet          hashSet  = new HashSet();
    static DoubleIntIndex doubleIntLookup =
        new DoubleIntIndex(sNumeric.length, false);
    static IntKeyIntValueHashMap intKeyIntValueHashLookup =
        new IntKeyIntValueHashMap();
    static IntValueHashMap intValueHashLookup =
        new IntValueHashMap(sNumeric.length);
    static IntKeyHashMap intKeyHashLookup = new IntKeyHashMap();

    static {
        doubleIntLookup.setKeysSearchTarget();

        java.util.Random randomgen = new java.util.Random();
        int[]            row       = new int[2];

        for (int i = 0; i < sNumeric.length; i++) {
            hashSet.add(sNumeric[i][0]);
            intKeyIntValueHashLookup.put(randomgen.nextInt(sNumeric.length),
                                         i);
            intKeyHashLookup.put(i, new Integer(i));
            doubleIntLookup.add(randomgen.nextInt(sNumeric.length), i);
            intValueHashLookup.put(sNumeric[i][0],
                                   randomgen.nextInt(sNumeric.length));
        }
    }

    static int count = 100000;

    public TestLibSpeed() {

        java.util.Random randomgen = new java.util.Random();
        StopWatch        sw        = new StopWatch();
        int              dummy     = 0;

        System.out.println("set lookup ");

        for (int k = 0; k < 3; k++) {
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    hashSet.contains(sNumeric[r][0]);

                    dummy += r;
                }
            }

            System.out.println("HashSet contains " + sw.elapsedTime());
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    intKeyIntValueHashLookup.get(r, -1);

                    dummy += r;
                }
            }

            System.out.println("IntKeyIntValueHashMap Lookup with array "
                               + sw.elapsedTime());
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    intKeyHashLookup.get(r);

                    dummy += r;
                }
            }

            System.out.println("IntKeyHashMap Lookup " + sw.elapsedTime());
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    doubleIntLookup.findFirstEqualKeyIndex(r);

                    dummy += r;
                }
            }

            System.out.println("DoubleIntTable Lookup " + sw.elapsedTime());
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    intValueHashLookup.get(sNumeric[r][0], 0);

                    dummy += r;
                }
            }

            System.out.println("IntKeyIntValueHashMap Lookup "
                               + sw.elapsedTime());
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    dummy += r;
                }
            }

            System.out.println("emptyOp " + sw.elapsedTime());
            sw.zero();

            for (int j = 0; j < count; j++) {
                for (int i = 0; i < sNumeric.length; i++) {
                    int r = randomgen.nextInt(sNumeric.length);

                    doubleIntLookup.findFirstEqualKeyIndex(r);

                    dummy += r;
                }
            }

            System.out.println("DoubleIntTable Lookup " + sw.elapsedTime());
            sw.zero();
            System.out.println("Object Cache Test " + sw.elapsedTime());
        }
    }

    public static void main(String[] argv) {
        TestLibSpeed ls = new TestLibSpeed();
    }
}
