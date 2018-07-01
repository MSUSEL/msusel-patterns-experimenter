/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;
import edu.montana.gsoc.msusel.arc.datamodel.Rule;
import edu.montana.gsoc.msusel.arc.td.CastTD;
import edu.montana.gsoc.msusel.arc.td.TechnicalDebtCalcStrategy;
import edu.montana.gsoc.msusel.arc.td.param.CastParams;
import edu.montana.gsoc.msusel.arc.td.param.TDParams;

import java.util.List;

public class TechDebtCommand extends SecondaryAnalysisCommand {

    TechnicalDebtCalcStrategy strategy;

    public TechDebtCommand() {
        super("TechnicalDebt");
        strategy = new CastTD();
    }

    @Override
    public void execute(ArcContext context) {
        List<Rule> rules = context.getRules();

        List<Rule> high = Lists.newArrayList();
        List<Rule> med = Lists.newArrayList();
        List<Rule> low = Lists.newArrayList();

        for (Rule r : rules) {
            switch(r.getPriority()) {
                case INFO:
                case MINOR:
                    low.add(r);
                    break;
                case MAJOR:
                    med.add(r);
                    break;
                case CRITICAL:
                case BLOCKER:
                    high.add(r);
                    break;
            }
        }

        TDParams params = TDParams.builder()
                .param(CastParams.COUNT_HIGH_SEVERITY, high.size())
                .param(CastParams.COUNT_MED_SEVERITY, med.size())
                .param(CastParams.COUNT_LOW_SEVERITY, low.size())
                .create();

        double value = strategy.calculate(params);

        context.recordMeasure(TechDebtConstants.TD_MEASURE_REPO, TechDebtConstants.TD_MEASURE_NAME, value);
    }
}
