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
package edu.montana.gsoc.msusel.arc.impl.quality.sigmain

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Measurable
import edu.isu.isuese.datamodel.Measure
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord

class MultiValueRater extends AbstractMetricRater {

    MultiValueRater(String metricHandle) {
        super(metricHandle)
    }

    @Override
    void rate(Measurable measurable) {
        Table<Integer, RiskCategory, Double> ratingTable = loadRatingTable()
        Map<RiskCategory, Double> profile = loadRiskProfile(measurable)
        double rating = 0.0

        if ((profile[RiskCategory.MODERATE] <= ratingTable.get(5, RiskCategory.MODERATE)) &&
                (profile[RiskCategory.HIGH] <= ratingTable.get(5, RiskCategory.HIGH)) &&
                (profile[RiskCategory.VERY_HIGH] <= ratingTable.get(5, RiskCategory.VERY_HIGH)))
            rating = 5.0
        else {
            (2..4).each {
                if (rating == 0.0d) {
                    boolean val = false
                    profile.each { cat, value ->
                        val = val || (value > ratingTable.get(it, cat))
                    }
                    if (val) {
                        rating = (double) it
                    }
                }
            }
        }

        if (rating == 0.0d)
            rating = 1.0

        Measure.of("${SigMainConstants.SIGMAIN_REPO_NAME}:${metricHandle}.RATING").on(measurable).withValue(rating)
    }

    Table<Integer, RiskCategory, Double> loadRatingTable() {
        Table<Integer, RiskCategory, Double> table = HashBasedTable.create()
        InputStream is = this.getClass().getResourceAsStream("/edu/montana/gsoc/msusel/arc/impl/quality/sig/${metricHandle}.csv")
        InputStreamReader isr = new InputStreamReader(is)
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(isr)
        records.each { record ->
            table.put(Integer.parseInt(record.get(0)), RiskCategory.fromString(record.get(1)), Double.parseDouble(record.get(1)))
        }
        is.close()
        table
    }

    Map<RiskCategory, Double> loadRiskProfile(Measurable measureable) {
        Map<RiskCategory, Double> map = [:]

        map[RiskCategory.LOW] = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "${metricHandle}.LOW", measureable)
        map[RiskCategory.MODERATE] = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_NAME, "${metricHandle}.MOD", measureable)
        map[RiskCategory.HIGH] = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_NAME, "${metricHandle}.HIGH", measureable)
        map[RiskCategory.VERY_HIGH] = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_NAME, "${metricHandle}.VHIGH", measureable)

        map
    }
}
