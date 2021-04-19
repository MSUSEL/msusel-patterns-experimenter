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
package edu.montana.gsoc.msusel.arc.impl.grime;

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.detstrat.impl.ClassGrimeDetector;
import edu.isu.isuese.detstrat.impl.GrimeDetector;
import edu.isu.isuese.detstrat.impl.ModularGrimeDetector;
import edu.isu.isuese.detstrat.impl.OrgGrimeDetector;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class GrimeDetectorCommand extends SecondaryAnalysisCommand {

    public GrimeDetectorCommand() {
        super(GrimeConstants.GRIME_DETECT_CMD_NAME);
    }

    @Override
    public void execute(ArcContext context) {
        Project project = context.getProject();

        log.info("Initializing Grime Detectors");
        List<GrimeDetector> detectors = Lists.newArrayList(
//                new ClassGrimeDetector(),
//                new ModularGrimeDetector(),
                new OrgGrimeDetector()
        );

        log.info("Starting Grime Detection");
        context.open();
        for (GrimeDetector detector : detectors) {
            log.info(String.format("Detecting %s Started", detector.getClass().getSimpleName()));
            project.getPatternInstances().forEach(detector::detect);
            log.info(String.format("Detecting %s Complete", detector.getClass().getSimpleName()));
        }
        context.close();
        log.info("Grime Detection Complete");
    }
}
