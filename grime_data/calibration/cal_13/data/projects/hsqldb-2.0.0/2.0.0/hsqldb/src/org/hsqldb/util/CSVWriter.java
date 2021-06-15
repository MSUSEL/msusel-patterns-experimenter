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

package org.hsqldb.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * helper class to write table data to a csv-file (comma separated values).
 * the first line in file is a list of fieldnames, all following lines
 * are data lines.
 * a descptiontion of file format can be found on: http://www.wotsit.org/
 * usage: create a object using the constructor. call writeHeader
 * for writing the filename header then add data with writeData.
 * at the end close() closes the file.
 *
 *@author jeberle@users
 */
public class CSVWriter {

    private String             newline = System.getProperty("line.separator");
    private OutputStreamWriter writer  = null;
    private int                nbrCols = 0;
    private int                nbrRows = 0;

    /**
     * constructor.
     * creates a csv file for writing data to it
     * @param file the file to write data to
     * @param encoding encoding to use or null (=defualt)
     */
    public CSVWriter(File file, String encoding) throws IOException {

        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }

        FileOutputStream fout = new FileOutputStream(file);

        writer = new OutputStreamWriter(fout, encoding);
    }

    /**
     * writes the csv header (fieldnames). should be called after
     * construction one time.
     * @param header String[] with fieldnames
     */
    public void writeHeader(String[] header) throws IOException {

        this.nbrCols = header.length;

        doWriteData(header);
    }

    /**
     * writes a data-record to the file. note that data[] must have
     * same number of elements as the header had.
     *
     * @param data data to write to csv-file
     */
    public void writeData(String[] data) throws IOException {
        doWriteData(data);
    }

    /**
     * closes the csv file.
     */
    public void close() throws IOException {
        this.writer.close();
    }

    private void doWriteData(String[] values) throws IOException {

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                this.writer.write(";");
            }

            if (values[i] != null) {
                this.writer.write("\"");
                this.writer.write(this.toCsvValue(values[i]));
                this.writer.write("\"");
            }
        }

        this.writer.write(newline);

        this.nbrRows++;
    }

    private String toCsvValue(String str) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            sb.append(c);

            switch (c) {

                case '"' :
                    sb.append('"');
                    break;
            }
        }

        return sb.toString();
    }
}
