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

package org.hsqldb.lib.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.util.Properties;
import java.text.Collator;
import java.io.RandomAccessFile;

/**
 * Handles the differences between JDK 1.1.x and 1.2.x and above
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 */
public class JavaSystem {

    // variables to track rough count on object creation, to use in gc
    public static int gcFrequency;
    public static int memoryRecords;

    // Garbage Collection
    public static void gc() {

        if ((gcFrequency > 0) && (memoryRecords > gcFrequency)) {
            memoryRecords = 0;

            System.gc();
        }
    }

    public static IOException toIOException(Throwable t) {

        if (t instanceof IOException) {
            return (IOException) t;
        }

//#ifdef JAVA6
        return new IOException(t);

//#else
/*
        return new IOException(t.getMessage());
*/

//#endif JAVA6
    }

    final static BigDecimal BD_1  = BigDecimal.valueOf(1L);
    final static BigDecimal MBD_1 = BigDecimal.valueOf(-1L);

    public static int precision(BigDecimal o) {

        if (o == null) {
            return 0;
        }

//#ifdef JAVA6
        int precision;

        if (o.compareTo(BD_1) < 0 && o.compareTo(MBD_1) > 0) {
            precision = o.scale();
        } else {
            precision = o.precision();
        }

        return precision;

//#else
/*
        if (o.compareTo(BD_1) < 0 && o.compareTo(MBD_1) > 0) {
            return o.scale();
        }

        BigInteger big  = o.unscaledValue();
        int        sign = big.signum() == -1 ? 1
                                             : 0;

        return big.toString().length() - sign;
*/

//#endif JAVA6
    }

    public static String toString(BigDecimal o) {

        if (o == null) {
            return null;
        }

//#ifdef JAVA6
        return o.toPlainString();

//#else
/*
        return o.toString();
*/

//#endif JAVA6
    }

    public static int compareIngnoreCase(String a, String b) {

//#ifdef JAVA2FULL
        return a.compareToIgnoreCase(b);

//#else
/*
        return a.toUpperCase().compareTo(b.toUpperCase());
*/

//#endif JAVA2
    }

    public static double parseDouble(String s) {

//#ifdef JAVA2FULL
        return Double.parseDouble(s);

//#else
/*
        return new Double(s).doubleValue();
*/

//#endif JAVA2
    }

    public static BigInteger unscaledValue(BigDecimal o) {

//#ifdef JAVA2FULL
        return o.unscaledValue();

//#else
/*
        int scale = o.scale();
        return o.movePointRight(scale).toBigInteger();
*/

//#endif
    }

    public static void setLogToSystem(boolean value) {

//#ifdef JAVA2FULL
        try {
            PrintWriter newPrintWriter = (value) ? new PrintWriter(System.out)
                                                 : null;

            DriverManager.setLogWriter(newPrintWriter);
        } catch (Exception e) {}

//#else
/*
        try {
            PrintStream newOutStream = (value) ? System.out
                                               : null;
            DriverManager.setLogStream(newOutStream);
        } catch (Exception e){}
*/

//#endif
    }

    public static void deleteOnExit(File f) {

//#ifdef JAVA2FULL
        f.deleteOnExit();

//#endif
    }

    public static void saveProperties(Properties props, String name,
                                      OutputStream os) throws IOException {

//#ifdef JAVA2FULL
        props.store(os, name);

//#else
/*
        props.save(os, name);
*/

//#endif
    }

    public static void runFinalizers() {

//#ifdef JAVA2FULL
        System.runFinalizersOnExit(true);

//#endif
    }

    public static boolean createNewFile(File file) {

//#ifdef JAVA2FULL
        try {
            return file.createNewFile();
        } catch (IOException e) {}

        return false;

//#else
/*
        return true;
*/

//#endif
    }

    public static void setRAFileLength(RandomAccessFile raFile,
                                       long length) throws IOException {

//#ifdef JAVA2FULL
        raFile.setLength(length);

//#endif
    }
}
