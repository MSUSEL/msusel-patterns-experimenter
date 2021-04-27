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
import groovy.util.logging.Log4j2
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

/**
 * @author Isaac D Griffith
 * @version 1.3.0
 */
@Log4j2
class ExperimentConfigReader {

    int index = 0
    List<File> files = []

    def findConfigs(String path) {
        def filePattern = ~/experiment.conf/
        def directory = new File(path)
        if (!directory.isDirectory()) {
            log.error "Thprovided directory name ${path} is not a directory."
            return
        }

        log.info "Searching for experimental configurations in directory ${path}..."

        directory.eachFileRecurse {
            if (filePattern.matcher(it.name).find()) {
                files << it
            }
        }
    }

    def initialize() {
        files = [new File("experiment.conf")]
    }

    def loadNext() {
        Table<String, String, String> table = HashBasedTable.create()
        File file = files[index++]
        Reader reader = new FileReader(file)
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader)
        records.each { record ->
            if (record.size() < 4)
                log.error "Record is malformed"
            table.put(record.get(0), Constants.PatternType, record.get(1))
            table.put(record.get(0), Constants.GrimeType, record.get(2))
            table.put(record.get(0), Constants.GrimeSeverity, record.get(3))
        }
        reader.close()

        return table
    }
}
