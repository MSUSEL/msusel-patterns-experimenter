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
package edu.montana.gsoc.msusel.arc.impl.quality.td;

import edu.isu.isuese.datamodel.Measure;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants;
import edu.montana.gsoc.msusel.arc.impl.quality.sigmain.SigMainConstants;
import edu.montana.gsoc.msusel.arc.impl.quality.td.param.NugrohoParams;
import edu.montana.gsoc.msusel.arc.impl.quality.td.param.TDParams;
import edu.montana.gsoc.msusel.arc.impl.quality.td.strategies.NugrohoTD;
import lombok.extern.log4j.Log4j2;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class NugrohoTDCommand extends TechDebtCommand {

    public NugrohoTDCommand() {
        super(TechDebtConstants.NUGROHO_CMD_NAME, new NugrohoTD());
    }

    public void execute(ArcContext context) {
        log.info("Executing Nugroho TD Analysis");

        context.open();
        double analyzability = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigAnalyzability", context.getProject());
        double modifiability = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigModifiability", context.getProject());
        double testability = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigTestability", context.getProject());
        double modularity = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigModularity", context.getProject());
        double reusability = Measure.valueFor(SigMainConstants.SIGMAIN_REPO_KEY, "sigReusability", context.getProject());

        double maintainability = 0.2 * analyzability + 0.2 * modifiability + 0.2 * testability + 0.2 * modularity + 0.2 * reusability;

        double size = Measure.valueFor(MetricsConstants.METRICS_REPO_KEY, "SLOC", context.getProject());

        TDParams params = strategy.generateParams();
        params.setParam(NugrohoParams.MAINTAINABILITY, maintainability);
        params.setParam(NugrohoParams.REFACTORING_ADJ, 0.10);
        params.setParam(NugrohoParams.TECH_FACTOR, 0.00136d);
        params.setParam(NugrohoParams.SYSTEM_SIZE, size);

        double value = strategy.calculate(params);

        Measure.of(TechDebtConstants.TD_REPO_KEY + ":" + TechDebtConstants.NUGROHO_MEASURE_NAME)
                .on(context.getProject())
                .withValue(value);
        context.close();

        log.info("Finished Nugroho TD Analysis");
    }
}
