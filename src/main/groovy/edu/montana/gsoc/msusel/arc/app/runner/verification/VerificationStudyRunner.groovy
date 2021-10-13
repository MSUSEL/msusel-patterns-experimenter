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
package edu.montana.gsoc.msusel.arc.app.runner.verification

import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.app.runner.EmpiricalStudy
import edu.montana.gsoc.msusel.arc.app.runner.StudyConfigReader

class VerificationStudyRunner extends EmpiricalStudy {

    private static final String STUDY_NAME = "Verification Study"
    private static final String STUDY_DESC = ""

    VerificationStudyRunner(ArcContext context) {
        super(STUDY_NAME, STUDY_DESC, context, new StudyConfigReader(getConfigFileName(), getConfigHeaders()))

        this.phases = [
                new VerificationStudyPhaseOne(context),
                new VerificationStudyPhaseTwo(context),
                new VerificationStudyPhaseThree(context)
        ]

        this.headers = VerificationStudyConstants.HEADERS
        this.keyHeaders = [VerificationStudyConstants.KEY]
        this.identifier = VerificationStudyConstants.ID
    }

    def static getConfigHeaders() {
        return [
                VerificationStudyConstants.KEY,
                VerificationStudyConstants.LOCATION,
                VerificationStudyConstants.INSTLOC,
                VerificationStudyConstants.RESULTSLOC
        ]
    }

    def static getConfigFileName() {
        "vstudy.conf"
    }
}
