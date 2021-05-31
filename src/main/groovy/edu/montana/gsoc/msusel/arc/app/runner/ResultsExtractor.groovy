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
package edu.montana.gsoc.msusel.arc.app.runner

import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.System
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.ReportingLevel
import edu.montana.gsoc.msusel.arc.app.runner.experiment.ExperimentConstants
import groovy.util.logging.Log4j2

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
class ResultsExtractor {

    List<String> measures = []
    ReportingLevel level
    ArcContext context

    void initialize(ReportingLevel level, List<String> measures, ArcContext context) {
        this.level = level
        this.measures = measures
        this.context = context
    }

    void extractResults(Table<String, String, String> table) {
        context.open()
        switch(level) {
            case ReportingLevel.SYSTEM:
                extractSystemResults(table)
                break
            case ReportingLevel.PROJECT:
                extractProjectResults(table)
                break
        }
        context.close()
    }

    void extractSystemResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            String key1 = values.get(id, ExperimentConstants.Key1)
            String key2 = values.get(id, ExperimentConstants.Key2)
            System sys1 = System.findFirst("key = ?", key1)
            System sys2 = System.findFirst("key = ?", key2)
            log.info "Severity: ${values.get(id, ExperimentConstants.GrimeSeverity)}"
            log.info "Key1: $key1"
            log.info "Key2: $key2"
            measures.each {
                log.info "Measure: $it"
                double val1 = sys1.getMeasureValueByName(it)
                double val2 = sys2.getMeasureValueByName(it)
                log.info "Value1: $val1"
                log.info "Value2: $val2"
                double diff = val2 - val1
                log.info "Diff: $diff"
                String name = it.split(/:/)[1]
                values.put(id, name, "$diff")
            }
        }
    }

    void extractProjectResults(Table<String, String, String> values) {
        values.rowKeySet().each {id ->
            String key1 = values.get(id, ExperimentConstants.Key1)
            String key2 = values.get(id, ExperimentConstants.Key2)
            Project sys1 = Project.findFirst("projKey = ?", key1)
            Project sys2 = Project.findFirst("projKey = ?", key2)
            log.info "Severity: ${values.get(id, ExperimentConstants.GrimeSeverity)}"
            log.info "Key1: $key1"
            log.info "Key2: $key2"
            measures.each {
                log.info "Measure: $it"
                double val1 = sys1.getMeasureValueByName(it)
                double val2 = sys2.getMeasureValueByName(it)
                log.info "Value1: $val1"
                log.info "Value2: $val2"
                double diff = val2 - val1
                log.info "Diff: $diff"
                String name = it.split(/:/)[1]
                values.put(id, name, "$diff")
            }
        }
    }
}
