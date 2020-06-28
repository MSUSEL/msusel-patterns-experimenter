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
package edu.montana.gsoc.msusel.arc;

import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.datamodel.System;
import edu.montana.gsoc.msusel.arc.impl.experiment.EmpiricalStudy;
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsConstants;
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants;
import org.javalite.activejdbc.Model;
import org.testng.collections.Lists;

import java.util.ArrayList;
import java.util.List;

public class FindBugsOnly extends EmpiricalStudy {

    public FindBugsOnly(ArcContext context) {
        super("FBOnly", "A Test Empirical Study", context);
    }

    @Override
    public void execute() {
        initWorkflow();
        initReport();
        executeStudy();
        //report.generate();
    }

    @Override
    public void initWorkflow() {

    }

    @Override
    public void initReport() {

    }

    public void executeStudy() {
        Command java = getContext().getRegisteredCommand(JavaConstants.JAVA_TOOL_CMD_NAME);
        Command jdi = getContext().getRegisteredCommand(JavaConstants.JAVA_DIR_IDENT_CMD_NAME);
        Command build = getContext().getRegisteredCommand(JavaConstants.JAVA_BUILD_CMD_NAME);
        Command findbugs = getContext().getRegisteredCommand(FindBugsConstants.FB_CMD_NAME);

        Collector fbColl = getContext().getRegisteredCollector(FindBugsConstants.FB_COLL_NAME);

        System sys = null;

        getContext().open();
        sys = System.findFirst("name = ?", "test_proj");
        getContext().close();

        getContext().open();
        getContext().setProject(sys.getProjects().get(0));
        getContext().close();

        build.execute(getContext());
        java.execute(getContext());
        jdi.execute(getContext());
        findbugs.execute(getContext());
        fbColl.execute(getContext());
    }
}
