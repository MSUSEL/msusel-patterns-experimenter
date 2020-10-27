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
package edu.montana.gsoc.msusel.arc.impl.qmood;

import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class QMoodCommand extends SecondaryAnalysisCommand {

    QMoodMetricProvider provider;

    public QMoodCommand(QMoodMetricProvider provider) {
        super(QMoodConstants.QMOOD_CMD_NAME);
        this.provider = provider;
    }

    @Override
    public void execute(ArcContext context) {
        context.logger().atInfo().log("Starting QMOOD Analysis");

        context.open();
        provider.getRegistrar().getPrimaryEvaluators().forEach(metricEvaluator -> {
            context.getProject().getAllTypes().forEach(metricEvaluator::measure);
            metricEvaluator.measure(context.getProject());
        });

        provider.getRegistrar().getSecondaryEvaluators().forEach(metricEvaluator -> {
            context.getProject().getAllTypes().forEach(metricEvaluator::measure);
            metricEvaluator.measure(context.getProject());
        });
        context.close();

        context.logger().atInfo().log("Finished QMOOD Analysis");
    }
}
