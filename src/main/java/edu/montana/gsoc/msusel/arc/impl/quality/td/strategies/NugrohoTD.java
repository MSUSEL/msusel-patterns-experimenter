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
package edu.montana.gsoc.msusel.arc.impl.quality.td.strategies;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.montana.gsoc.msusel.arc.impl.quality.td.param.NugrohoParams;
import edu.montana.gsoc.msusel.arc.impl.quality.td.param.TDParams;
import lombok.extern.log4j.Log4j2;

/**
 * NugrohoTD - Calculates Technical Debt using the method from Nugroho et al,
 * "An Empirical Model of Technical Debt and Interest", MTD'11, 2011.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class NugrohoTD extends TechnicalDebtCalcStrategy {

    private final Table<Integer, Integer, Integer> reworkFractionTable;

    public NugrohoTD() {
        reworkFractionTable = HashBasedTable.create();
        reworkFractionTable.put(1, 2, 60);
        reworkFractionTable.put(1, 3, 100);
        reworkFractionTable.put(1, 4, 135);
        reworkFractionTable.put(1, 5, 175);

        reworkFractionTable.put(2, 3, 40);
        reworkFractionTable.put(2, 4, 75);
        reworkFractionTable.put(3, 5, 115);

        reworkFractionTable.put(3, 4, 35);
        reworkFractionTable.put(3, 5, 75);

        reworkFractionTable.put(4, 5, 40);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculate(TDParams param) {
        double refactoringAdj = param.getNumericParam(NugrohoParams.REFACTORING_ADJ);

        double systemSize = param.getNumericParam(NugrohoParams.SYSTEM_SIZE);
        double techFactor = param.getNumericParam(NugrohoParams.TECH_FACTOR);
        double reworkFraction = reworkFractionTable.get(((Double) param.getNumericParam(NugrohoParams.MAINTAINABILITY)).intValue(), 5);
//        double maintenanceFactor = 0.0;
//        double qualityLevel = 0;
        double percentYearlyMaintGenTD = param.getNumericParam(NugrohoParams.PERCENT_YEARLY_MAINT_GEN_TD);
        double projectionYears = 0.0;

        double rebuildValue = systemSize * Math.pow((1 + percentYearlyMaintGenTD), projectionYears) * techFactor;
        double repairEffort = reworkFraction * rebuildValue * refactoringAdj;
//        double qualityFactor = Math.pow(2, ((qualityLevel - 3) / 2));
//        double maintenanceEffort = (maintenanceFactor * rebuildValue) / qualityFactor;

        return repairEffort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TDParams generateParams() {
        return TDParams.builder().create();
    }
}
