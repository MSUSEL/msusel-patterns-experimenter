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
            ctx.logger().atInfo().log("Instances Found: " + bugColl.getBugInstance().size());

            List<Finding> findings = Lists.newArrayList();
            bugColl.getBugInstance().forEach(inst -> {
                ctx.open();
                Rule rule = Rule.findFirst("name = ?", inst.getType());
                ctx.close();
                if (rule != null) {
                    ctx.open();
                    ctx.logger().atInfo().log("Rule: " + rule.getKey());
                                        ctx.close();

                    inst.getClazzOrTypeOrMethod().forEach(obj -> {
                        Finding finding = null;
                        if (obj instanceof BugCollection.BugInstance.Class) {
                            BugCollection.BugInstance.Class clazz = (BugCollection.BugInstance.Class) obj;
                            ctx.open();
                            ctx.logger().atInfo().log("Class Bug Instance Location: " + clazz.getClassname());
                            Type type = project.findTypeByQualifiedName(clazz.getClassname());
                            if (!rule.hasFindingOn(type))
                                finding = Finding.of(rule.getKey());
                            ctx.close();
                            setReferenceAndLineInfo(ctx, finding, type, clazz.getSourceLine());
                            if (finding != null)
                                findings.add(finding);
                        } else if (obj instanceof BugCollection.BugInstance.Method) {
                            BugCollection.BugInstance.Method meth = (BugCollection.BugInstance.Method) obj;
                            ctx.open();
                            ctx.logger().atInfo().log("Method Bug Instance Location: " + meth.getName());
                            Type type = project.findTypeByQualifiedName(meth.getClassname());
                            Method method = type.getMethodWithName(meth.getName());
                            if (!rule.hasFindingOn(method))
                                finding = Finding.of(rule.getKey());
                            ctx.close();
                            setReferenceAndLineInfo(ctx, finding, method, meth.getSourceLine());
                            if (finding != null)
                                findings.add(finding);
                        }
                    });
                }
            });

            ctx.logger().atInfo().log("Findings Created: " + findings.size());
            ctx.open();
            for (Finding f : findings) {
                System.out.println("\t" + f);
            }
            ctx.close();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        ctx.logger().atInfo().log("Finished collecting FindBugs Results");
    }

    public void setReferenceAndLineInfo(ArcContext ctx, Finding finding, Component comp, SourceLine line) {
        if (finding != null && comp != null) {
            ctx.open();
            finding.on(comp);
            ctx.close();
            setStartAndEnd(ctx, finding, line);
        }
    }

    public void setStartAndEnd(ArcContext ctx, Finding finding, SourceLine line) {
        if (line != null) {
            if (line.getStart() != null) {
                ctx.open();
                finding.setStart(line.getStart());
                ctx.close();
            }
            if (line.getEnd() != null) {
                ctx.open();
                finding.setEnd(line.getEnd());
                ctx.close();
            }
        }
    }

    public static void main(String args[]) {
        //new FindBugsCollector("/home/git/msusel/msusel-patterns-experimenter/data/fbresults/fbresults.xml").execute(null);
    }
}
