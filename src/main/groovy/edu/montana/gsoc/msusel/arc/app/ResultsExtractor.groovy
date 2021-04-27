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
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ReportingLevel

class ResultsExtractor {

    List<String> measures = []
    ReportingLevel level

    void initialize(ReportingLevel level, List<String> measures) {
        this.level = level
        this.measures = measures
    }

    void extractResults(Table<String, String, String> table) {
        switch(level) {
            case ReportingLevel.SYSTEM:
                extractSystemResults(table)
                break
            case ReportingLevel.PROJECT:
                extractProjectResults(table)
                break
        }
    }

    void extractSystemResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            String key1 = values.get("$id", Constants.Key1)
            String key2 = values.get("$id", Constants.Key2)
            System sys1 = System.findFirst("key = ?", key1)
            System sys2 = System.findFirst("key = ?", key2)
            measures.each {
                double val1 = sys1.getMeasureValueByName(it)
                double val2 = sys2.getMeasureValueByName(it)
                double diff = val2 - val1
                values.put("$id", it, "$diff")
            }
        }
    }

    void extractProjectResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            String key1 = values.get("$id", Constants.Key1)
            String key2 = values.get("$id", Constants.Key2)
            Project sys1 = Project.findFirst("projKey = ?", key1)
            Project sys2 = Project.findFirst("projKey = ?", key2)
            measures.each {
                double val1 = sys1.getMeasureValueByName(it)
                double val2 = sys2.getMeasureValueByName(it)
                double diff = val2 - val1
                values.put("$id", it, "$diff")
            }
        }
    }
}
