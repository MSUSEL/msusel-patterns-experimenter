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

package org.hsqldb.lib.tar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Encapsulates Pax Interchange Format key/value pairs.
 */
public class PIFGenerator extends ByteArrayOutputStream {

    OutputStreamWriter writer;
    String             name;
    int                fakePid;    // Only used by contructors
    char               typeFlag;

    public String getName() {
        return name;
    }

    protected PIFGenerator() {

        try {
            writer = new OutputStreamWriter(this, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException(
                "Serious problem.  JVM can't encode UTF-8", uee);
        }

        fakePid = (int) (new Date().getTime() % 100000L);

        // Java doesn't have access to PIDs, as PIF wants in the "name" field,
        // so we emulate one in a way that is easy for us.
    }

    /**
     * Construct a PIFGenerator object for a 'g' record.
     *
     * @param sequenceNum  Index starts at 1 in each Tar file
     */
    public PIFGenerator(int sequenceNum) {

        this();

        if (sequenceNum < 1) {

            // No need to localize.  Would be caught at dev-time.
            throw new IllegalArgumentException("Sequence numbers start at 1");
        }

        typeFlag = 'g';
        name = System.getProperty("java.io.tmpdir") + "/GlobalHead." + fakePid
               + '.' + sequenceNum;
    }

    /**
     * Construct a PIFGenerator object for a 'x' record.
     *
     * @param file Target file of the x record.
     */
    public PIFGenerator(File file) {

        this();

        typeFlag = 'x';

        String parentPath = (file.getParentFile() == null) ? "."
                                                           : file.getParentFile()
                                                               .getPath();

        name = parentPath + "/PaxHeaders." + fakePid + '/' + file.getName();
    }

    /**
     * Convenience wrapper for addRecord(String, String).
     * N.b. this writes values exactly as either "true" or "false".
     *
     * @see #addRecord(String, String)
     * @see Boolean#toString(boolean)
     */
    public void addRecord(String key,
                          boolean b)
                          throws TarMalformatException, IOException {
        addRecord(key, Boolean.toString(b));
    }

    /**
     * Convenience wrapper for addRecord(String, String).
     *
     * @see #addRecord(String, String)
     */
    public void addRecord(String key,
                          int i) throws TarMalformatException, IOException {
        addRecord(key, Integer.toString(i));
    }

    /**
     * Convenience wrapper for addRecord(String, String).
     *
     * @see #addRecord(String, String)
     */
    public void addRecord(String key,
                          long l) throws TarMalformatException, IOException {
        addRecord(key, Long.toString(l));
    }

    /**
     * I guess the "initial length" field is supposed to be in units of
     * characters, not bytes?
     */
    public void addRecord(String key,
                          String value)
                          throws TarMalformatException, IOException {

        if (key == null || value == null || key.length() < 1
                || value.length() < 1) {
            throw new TarMalformatException(RB.zero_write.getString());
        }

        int lenWithoutIlen = key.length() + value.length() + 3;

        // "Ilen" means Initial Length field.  +3 = SPACE + = + \n
        int lenW = 0;    // lenW = Length With initial-length-field

        if (lenWithoutIlen < 8) {
            lenW = lenWithoutIlen + 1;    // Takes just 1 char to report total
        } else if (lenWithoutIlen < 97) {
            lenW = lenWithoutIlen + 2;    // Takes 2 chars to report this total
        } else if (lenWithoutIlen < 996) {
            lenW = lenWithoutIlen + 3;    // Takes 3...
        } else if (lenWithoutIlen < 9995) {
            lenW = lenWithoutIlen + 4;    // ditto
        } else if (lenWithoutIlen < 99994) {
            lenW = lenWithoutIlen + 5;
        } else {
            throw new TarMalformatException(RB.pif_toobig.getString(99991));
        }

        writer.write(Integer.toString(lenW));
        writer.write(' ');
        writer.write(key);
        writer.write('=');
        writer.write(value);
        writer.write('\n');
        writer.flush();    // Does this do anything with a BAOS?
    }
}
