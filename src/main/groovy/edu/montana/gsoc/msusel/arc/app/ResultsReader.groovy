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

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.util.logging.Log4j2
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class ResultsReader {

    String file
    List<String> measures

    void initialize(List<String> measures, String file) {
        this.measures = measures
        this.file = file
    }

    Table<String,String,String> readResults() {
        Table<String, String, String> table = HashBasedTable.create()
        String[] headers = Constants.HEADERS
        measures.each {
            headers += it.split(":")[1]
        }

        Reader reader = new FileReader(file)
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)
        records.each { record ->
            headers.each {
                if (it != Constants.ID) {
                    table.put(record.get(Constants.ID), it, record.get(it))
                }
            }
        }
        reader.close()

        return table
    }
}
