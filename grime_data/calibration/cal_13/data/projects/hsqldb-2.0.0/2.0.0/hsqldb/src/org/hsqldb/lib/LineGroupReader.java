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

package org.hsqldb.lib;

import java.io.LineNumberReader;

import org.hsqldb.store.ValuePool;

/**
 * Uses a LineNumberReader and returns multiple consecutive lines which conform
 * to the specified group demarcation characteristics. Any IOException
 * thrown while reading from the reader is handled internally.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class LineGroupReader {

    private final static String[] defaultContinuations = new String[] {
        " ", "*"
    };
    private final static String[] defaultIgnoredStarts = new String[]{ "--" };
    static final String LS = System.getProperty("line.separator", "\n");

    //
    LineNumberReader reader;
    String           nextStartLine       = null;
    int              startLineNumber     = 0;
    int              nextStartLineNumber = 0;

    //
    final String[] sectionContinuations;
    final String[] sectionStarts;
    final String[] ignoredStarts;

    /**
     * Default constructor for TestUtil usage.
     * Sections start at lines beginning with any non-space character.
     * SQL comment lines are ignored.
     */
    public LineGroupReader(LineNumberReader reader) {

        this.sectionContinuations = defaultContinuations;
        this.sectionStarts        = ValuePool.emptyStringArray;
        this.ignoredStarts        = defaultIgnoredStarts;
        this.reader               = reader;

        try {
            getSection();
        } catch (Exception e) {}
    }

    /**
     * Constructor for sections starting with specified strings.
     */
    public LineGroupReader(LineNumberReader reader, String[] sectionStarts) {

        this.sectionStarts        = sectionStarts;
        this.sectionContinuations = ValuePool.emptyStringArray;
        this.ignoredStarts        = ValuePool.emptyStringArray;
        this.reader               = reader;

        try {
            getSection();
        } catch (Exception e) {}
    }

    public HsqlArrayList getSection() {

        String        line;
        HsqlArrayList list = new HsqlArrayList();

        if (nextStartLine != null) {
            list.add(nextStartLine);

            startLineNumber = nextStartLineNumber;
        }

        while (true) {
            boolean newSection = false;

            line = null;

            try {
                line = reader.readLine();
            } catch (Exception e) {}

            if (line == null) {
                nextStartLine = null;

                return list;
            }

            line = line.substring(
                0, org.hsqldb.lib.StringUtil.rightTrimSize(line));

            //if the line is blank or a comment, then ignore it
            if (line.length() == 0 || isIgnoredLine(line)) {
                continue;
            }

            if (isNewSectionLine(line)) {
                newSection = true;
            }

            if (newSection) {
                nextStartLine       = line;
                nextStartLineNumber = reader.getLineNumber();

                return list;
            }

            list.add(line);
        }
    }

    /**
     * Returns a map/list which contains the first line of each line group
     * as key and the rest of the lines as a String value.
     */
    public HashMappedList getAsMap() {

        HashMappedList map = new HashMappedList();

        while (true) {
            HsqlArrayList list = getSection();

            if (list.size() < 1) {
                break;
            }

            String key   = (String) list.get(0);
            String value = LineGroupReader.convertToString(list, 1);

            map.put(key, value);
        }

        return map;
    }

    private boolean isNewSectionLine(String line) {

        if (sectionStarts.length == 0) {
            for (int i = 0; i < sectionContinuations.length; i++) {
                if (line.startsWith(sectionContinuations[i])) {
                    return false;
                }
            }

            return true;
        } else {
            for (int i = 0; i < sectionStarts.length; i++) {
                if (line.startsWith(sectionStarts[i])) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean isIgnoredLine(String line) {

        for (int i = 0; i < ignoredStarts.length; i++) {
            if (line.startsWith(ignoredStarts[i])) {
                return true;
            }
        }

        return false;
    }

    public int getStartLineNumber() {
        return startLineNumber;
    }

    public void close() {
        try {
            reader.close();
        } catch (Exception e) {}
    }

    public static String convertToString(HsqlArrayList list, int offset) {

        StringBuffer sb = new StringBuffer();

        for (int i = offset; i < list.size(); i++) {
            sb.append(list.get(i)).append(LS);
        }

        return sb.toString();
    }
}
