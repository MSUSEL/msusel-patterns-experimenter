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

import edu.isu.isuese.datamodel.Module;
import edu.isu.isuese.datamodel.Project;
import edu.isu.isuese.datamodel.System;
import edu.montana.gsoc.msusel.arc.app.ToolsLoader;
import edu.montana.gsoc.msusel.arc.impl.experiment.EmpiricalStudy;
import edu.montana.gsoc.msusel.arc.impl.experiment.StudyManager;
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsConstants;
import edu.montana.gsoc.msusel.arc.impl.findbugs.FindBugsProperties;
import edu.montana.gsoc.msusel.arc.impl.ghsearch.GitHubSearchConstants;
import edu.montana.gsoc.msusel.arc.impl.git.GitConstants;
import edu.montana.gsoc.msusel.arc.impl.grime.GrimeConstants;
import edu.montana.gsoc.msusel.arc.impl.java.JavaConstants;
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants;
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Constants;
import edu.montana.gsoc.msusel.arc.impl.pattern4.Pattern4Properties;
import edu.montana.gsoc.msusel.arc.impl.patterns.ArcPatternConstants;
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDConstants;
import edu.montana.gsoc.msusel.arc.impl.pmd.PMDProperties;
import edu.montana.gsoc.msusel.arc.impl.qmood.QMoodConstants;
import edu.montana.gsoc.msusel.arc.impl.quamoco.QuamocoConstants;
import edu.montana.gsoc.msusel.arc.impl.td.TechDebtConstants;
import lombok.extern.log4j.Log4j2;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Log4j2
public class ExperimentalTest {

    ArcContext context;

    @BeforeEach
    public void setup() {
        context = new ArcContext(log);

        String base = "/home/git/msusel/msusel-patterns-experimenter/data/test_proj";

        // Load Configuration
        context.addArcProperty(ArcProperties.ARC_HOME_DIR, ".");
        context.addArcProperty(FindBugsProperties.FB_TOOL_HOME, "/home/grifisaa/bin/detectors/spotbugs-4.0.3/");
        context.addArcProperty(Pattern4Properties.P4_TOOL_HOME, "/home/grifisaa/bin/detectors/pattern4/");
        context.addArcProperty(PMDProperties.PMD_TOOL_HOME, "/home/grifisaa/bin/detectors/pmd-bin-6.24.0/");
        context.addArcProperty(ArcProperties.TOOL_OUTPUT_DIR, "/home/git/msusel/msusel-patterns-experimenter/data/tool_output/");
        context.addArcProperty(ArcProperties.BASE_DIRECTORY, base);
        context.addArcProperty("arc.db.driver", "org.sqlite.JDBC");
        context.addArcProperty("arc.db.url", "jdbc:sqlite:data/test.db");
        context.addArcProperty("arc.db.type", "sqlite");
        context.addArcProperty("arc.db.user", "arc");
        context.addArcProperty("arc.db.pass", "arc");

        ToolsLoader toolLoader = new ToolsLoader();
        toolLoader.loadTools(context);

        // construct Project elements
        context.open();
        System sys;
        if (System.findFirst("name = ?", "test_proj") == null)
            sys = System.builder().name("test_proj").key("test_proj").basePath(base).create();
        else
            sys = System.findFirst("name = ?", "test_proj");

        Project proj;
        if (sys.hasProjectWithName("test_proj"))
            proj = Project.builder().name("test_proj").projKey("test_proj").relPath("").version("1.0").create();
        else
            proj = sys.getProjectByName("test_proj");

        sys.addProject(proj);
        proj.updateKeys();
        context.setProject(proj);
        context.close();
    }

    @Test
    public void test() {
        EmpiricalStudy empiricalStudy = new TestStudy(context);
        empiricalStudy.execute();
    }

    @Test
    public void testFindBugs() {
        context.open();
        context.getProject().setSrcPath(new String[]{"src/main/java"});
        context.getProject().setBinPath(new String[]{"build/classes/java/main"});
        context.close();

        EmpiricalStudy empiricalStudy = new FindBugOnly(context);
        empiricalStudy.execute();
    }
}

