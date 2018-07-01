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
package edu.montana.gsoc.msusel.arc.impl.findbugs;

import com.google.common.collect.Lists;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.collector.FileCollector;
import edu.montana.gsoc.msusel.arc.datamodel.Violation;
import edu.montana.gsoc.msusel.arc.impl.findbugs.resultsdm.BugCollection;
import edu.montana.gsoc.msusel.arc.impl.findbugs.resultsdm.SourceLine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class FindBugsCollector extends FileCollector {

    public FindBugsCollector(String resultsFile) {
        super(resultsFile, "FindBugs Collector");
    }

    @Override
    public void execute(ArcContext ctx) {
        try {
            JAXBContext context = JAXBContext.newInstance(BugCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            BugCollection bugColl = (BugCollection) unmarshaller.unmarshal(new File(resultsFile));
            System.out.println("Instances Found: " + bugColl.getBugInstance().size());

            List<Violation> violations = Lists.newArrayList();
            bugColl.getBugInstance().forEach(inst -> {
                Violation violation = new Violation();
                violation.setRule("findbugs:" + inst.getType());

                inst.getClazzOrTypeOrMethod().forEach(obj -> {
                    if (obj instanceof BugCollection.BugInstance.Class) {
                        BugCollection.BugInstance.Class clazz = (BugCollection.BugInstance.Class) obj;
                        violation.setClassName(clazz.getClassname());
                        if (clazz.getSourceLine() != null) {
                            if (clazz.getSourceLine().getStart() != null)
                                violation.setStart(clazz.getSourceLine().getStart());
                            if (clazz.getSourceLine().getEnd() != null)
                                violation.setEnd(clazz.getSourceLine().getStart());
                            if (clazz.getSourceLine().getSourcepath() != null)
                                violation.setSourcePath(clazz.getSourceLine().getSourcepath());
                        } 
                    } else if (obj instanceof BugCollection.BugInstance.Method) {
                        BugCollection.BugInstance.Method meth = (BugCollection.BugInstance.Method) obj;
                        violation.setClassName(meth.getClassname());
                        if (meth.getSourceLine() != null && meth.getClassname().equals(violation.getClassName())) {
                            if (meth.getSourceLine().getStart() != null)
                                violation.setStart(meth.getSourceLine().getStart());
                            if (meth.getSourceLine().getEnd() != null)
                                violation.setEnd(meth.getSourceLine().getStart());
                            if (meth.getSourceLine().getSourcepath() != null)
                                violation.setSourcePath(meth.getSourceLine().getSourcepath());
                        }
                    }
                    else if (obj instanceof SourceLine) {
                        SourceLine line = (SourceLine) obj;
                        if (violation.getStart() <= 0)
                            violation.setStart(line.getStart());
                        if (violation.getEnd() <= 0)
                            violation.setEnd(line.getEnd());
                        violation.setSourcePath(line.getSourcepath());
                    }
                });
                violations.add(violation);
            });

            System.out.println("Violations Created: " + violations.size());
            for (Violation f : violations) {
                System.out.println("\t" + f);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new FindBugsCollector("/home/git/msusel/msusel-patterns-experimenter/data/fbresults/fbresults.xml").execute(null);
    }
}
