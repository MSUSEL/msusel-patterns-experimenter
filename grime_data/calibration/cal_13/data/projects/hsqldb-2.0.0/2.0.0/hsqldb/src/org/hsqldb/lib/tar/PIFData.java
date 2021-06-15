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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pax Interchange Format object constituted from an Input Stream.
 * <P>
 * Right now, the only Pax property that we support directly is "size".
 * </P> <P>
 */
public class PIFData extends HashMap<String, String> {
    static final long serialVersionUID = 3086795680582315773L;

    private static Pattern pifRecordPattern =
        Pattern.compile("\\d+ +([^=]+)=(.*)");

    /**
     * N.b. this is nothing to do with HashMap.size() or Map.size().
     * This returns the value of the Pax "size" property.
     */
    public Long getSize() {
        return sizeObject;
    }

    private Long sizeObject = null;

    public PIFData(InputStream stream)
    throws TarMalformatException, IOException {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String  s, k, v;
            Matcher m;
            int     lineNum = 0;

            /*
             * Pax spec does not allow for blank lines, ignored white space,
             * nor comments of any type, in the file.
             */
            while ((s = br.readLine()) != null) {
                lineNum++;

                m = pifRecordPattern.matcher(s);

                if (!m.matches()) {
                    throw new TarMalformatException(
                        RB.pif_malformat.getString(lineNum, s));
                }

                k = m.group(1);
                v = m.group(2);

                if (v == null || v.length() < 1) {
                    remove(k);
                } else {
                    put(k, v);
                }
            }
        } finally {
            try {
                stream.close();
            } finally {
                br = null;  // Encourage buffer GC
            }
        }

        String sizeString = get("size");

        if (sizeString != null) {
            try {
                sizeObject = Long.valueOf(sizeString);
            } catch (NumberFormatException nfe) {
                throw new TarMalformatException(
                    RB.pif_malformat_size.getString(sizeString));
            }
        }
    }
}
