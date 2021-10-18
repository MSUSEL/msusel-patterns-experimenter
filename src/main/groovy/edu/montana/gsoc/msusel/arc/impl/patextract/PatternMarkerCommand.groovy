/*
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
package edu.montana.gsoc.msusel.arc.impl.patextract

import edu.isu.isuese.datamodel.Finding
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand
import edu.montana.gsoc.msusel.arc.impl.pattern4.resultsdm.PatternInstance

class PatternMarkerCommand extends SecondaryAnalysisCommand {

    PatternMarkerCommand() {
        super(PatternExtractorConstants.MARKER_CMD_NAME)
    }

    @Override
    void execute(ArcContext context) {
        context.open()
        context.system.getPatternChains().each { chain ->
            List<Integer> list = []
            List<PatternInstance> instances = chain.getInstances()
            chain.getInstances().each { inst ->
                List<Finding> findings = Finding.getFindingsFor(inst)
                list << findings.size()
            }

            for (int i = 0; i < list.size() - 1; i++) {
                if (list[i] != list[i + 1])
                    instances[i + 1].markForExtraction()
            }
        }
        context.close()
    }
}
