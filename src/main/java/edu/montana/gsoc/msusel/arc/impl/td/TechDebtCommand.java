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
package edu.montana.gsoc.msusel.arc.impl.td;

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.Measure;
import edu.isu.isuese.datamodel.Rule;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;
import edu.montana.gsoc.msusel.arc.impl.td.strategies.CastTD;
import edu.montana.gsoc.msusel.arc.impl.td.strategies.TechnicalDebtCalcStrategy;
import edu.montana.gsoc.msusel.arc.impl.td.param.CastParams;
import edu.montana.gsoc.msusel.arc.impl.td.param.TDParams;

import java.util.List;

public class TechDebtCommand extends SecondaryAnalysisCommand {

    TechnicalDebtCalcStrategy strategy;

    public TechDebtCommand() {
        super(TechDebtConstants.TD_CMD_NAME);
        strategy = new CastTD();
    }

    @Override
    public void execute(ArcContext context) {
        List<Rule> rules = Rule.findAll();

        List<Rule> high = Lists.newArrayList();
        List<Rule> med = Lists.newArrayList();
        List<Rule> low = Lists.newArrayList();

        for (Rule r : rules) {
            switch(r.getPriority()) {
                case VERY_LOW:
                case LOW:
                    low.add(r);
                    break;
                case MODERATE:
                    med.add(r);
                    break;
                case HIGH:
                case VERY_HIGH:
                    high.add(r);
                    break;
            }
        }

        TDParams params = strategy.generateParams();
        params.setParam(CastParams.COUNT_HIGH_SEVERITY, high.size());
        params.setParam(CastParams.COUNT_MED_SEVERITY, med.size());
        params.setParam(CastParams.COUNT_LOW_SEVERITY, low.size());

        double value = strategy.calculate(params);

        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.TD_MEASURE_NAME)
                .on(context.getProject())
                .withValue(value);
    }
}
