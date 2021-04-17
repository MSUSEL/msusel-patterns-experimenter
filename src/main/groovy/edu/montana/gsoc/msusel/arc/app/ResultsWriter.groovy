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
package edu.montana.gsoc.msusel.arc.app

import com.google.common.collect.Table
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

class ResultsWriter {

    String[] MEASURES = []
    int NUM = 0
    String file

    void initialize(int num, List<String> measures, String file) {
        this.NUM = num
        this.MEASURES = measures
        this.file = file
    }

    void writeResults(Table<String, String, String> table) {
        FileWriter out = new FileWriter(file)
        String[] headers = Constants.HEADERS
        MEASURES.each {
            headers += it.split(":")[1]
        }
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
            for (int id = 0; id < NUM; id++) {
                Map<String, String> row = table.row("$id")
                List<String> rowValues = []
                Constants.HEADERS.each {
                    if (row.containsKey(it))
                        rowValues << row.get(it)
                    else
                        rowValues << ""
                }
                MEASURES.each {
                    if (row.containsKey(it))
                        rowValues << row.get(it)
                    else
                        rowValues << ""
                }
                printer.printRecord(rowValues)
            }
        }
    }
}
