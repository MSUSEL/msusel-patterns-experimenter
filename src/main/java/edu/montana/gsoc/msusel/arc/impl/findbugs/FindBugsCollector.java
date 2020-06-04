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
package edu.montana.gsoc.msusel.arc.impl.findbugs;

import com.google.common.collect.Lists;
import edu.isu.isuese.datamodel.*;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.collector.FileCollector;
import edu.montana.gsoc.msusel.arc.impl.findbugs.resultsdm.BugCollection;
import edu.montana.gsoc.msusel.arc.impl.findbugs.resultsdm.SourceLine;
import lombok.Builder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.System;
import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class FindBugsCollector extends FileCollector {

    FindBugsTool owner;

    @Builder(buildMethodName = "create")
    public FindBugsCollector(FindBugsTool owner, String resultsFile) {
        super(FindBugsConstants.FB_COLL_NAME, resultsFile);
        this.owner = owner;
    }

    @Override
    public void execute(ArcContext ctx) {
        ctx.logger().atInfo().log("Started collecting FindBugs Results");

        this.project = ctx.getProject();
        try {
            JAXBContext context = JAXBContext.newInstance(BugCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            BugCollection bugColl = (BugCollection) unmarshaller.unmarshal(new File(resultsFile));
            System.out.println("Instances Found: " + bugColl.getBugInstance().size());

            List<Finding> findings = Lists.newArrayList();
            bugColl.getBugInstance().forEach(inst -> {
                Rule rule = Rule.findFirst("ruleKey = ?", "findbugs:" + inst.getType());
                Finding finding = Finding.of(inst.getType());

                inst.getClazzOrTypeOrMethod().forEach(obj -> {
                    if (obj instanceof BugCollection.BugInstance.Class) {
                        BugCollection.BugInstance.Class clazz = (BugCollection.BugInstance.Class) obj;
                        Type type = project.findTypeByQualifiedName(clazz.getClassname());
                        setReferenceAndLineInfo(finding, type, clazz.getSourceLine());
                    } else if (obj instanceof BugCollection.BugInstance.Method) {
                        BugCollection.BugInstance.Method meth = (BugCollection.BugInstance.Method) obj;
                        Type type = project.findTypeByQualifiedName(meth.getClassname());
                        Method method = type.findMethodBySignature(meth.getSignature());
                        setReferenceAndLineInfo(finding, method, meth.getSourceLine());
                    } else if (obj instanceof SourceLine) {
                        SourceLine line = (SourceLine) obj;
                        Type type = project.findTypeByQualifiedName(line.getClassname());
                        setReferenceAndLineInfo(finding, type, line);
                    }
                });
                findings.add(finding);
                rule.addFinding(finding);
            });

            System.out.println("Findings Created: " + findings.size());
            for (Finding f : findings) {
                System.out.println("\t" + f);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        ctx.logger().atInfo().log("Finished collecting FindBugs Resutls");
    }

    public void setReferenceAndLineInfo(Finding finding, Component comp, SourceLine line) {
        finding.on(comp);
        setStartAndEnd(finding, line);
    }

    public void setStartAndEnd(Finding finding, SourceLine line) {
        if (line != null) {
            if (line.getStart() != null)
                finding.setStart(line.getStart());
            if (line.getEnd() != null)
                finding.setEnd(line.getEnd());
        }
    }

    public static void main(String args[]) {
        //new FindBugsCollector("/home/git/msusel/msusel-patterns-experimenter/data/fbresults/fbresults.xml").execute(null);
    }
}
