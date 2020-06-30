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
package edu.montana.gsoc.msusel.arc.impl.pmd;

import com.google.common.flogger.StackSize;
import edu.isu.isuese.datamodel.*;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.collector.FileCollector;
import edu.montana.gsoc.msusel.arc.impl.pmd.resultsdm.Pmd;
import lombok.Builder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.lang.System;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class PMDCollector extends FileCollector {

    PMDTool owner;

    @Builder(buildMethodName = "create")
    public PMDCollector(PMDTool owner, String resultsFile) {
        super(PMDConstants.PMD_COLL_NAME, resultsFile);
        this.owner = owner;
    }

    @Override
    public void execute(ArcContext ctx) {
        ctx.logger().atInfo().log("Starting Collecting PMD Results");

        this.project = ctx.getProject();
        try {
            JAXBContext context = JAXBContext.newInstance(Pmd.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Pmd pmd = (Pmd) unmarshaller.unmarshal(new java.io.File(resultsFile));
            System.out.println("Files: " + pmd.getFile().size());
            pmd.getFile().forEach(f -> System.out.println("File: " + f.getName() + " has " + f.getViolation().size() + " findings"));

            pmd.getFile().forEach(file ->
                file.getViolation().forEach(v -> {
                    ctx.open();
                    Namespace ns = ctx.getProject().findNamespace(v.getPackage());
                    Rule rule = Rule.findFirst("ruleKey = ?", "pmd:" + v.getRule());
                    Type t = ns.getTypeByName(v.getClazz());
                    if (t != null) {
                        Member m = t.findMemberInRange(v.getBeginline().intValue(), v.getEndline().intValue());
                        Finding finding = Finding.of(rule.getKey()).on(m);
                        rule.addFinding(finding);
                    }
                    ctx.close();
                })
            );
        } catch (JAXBException e) {
            ctx.logger().atError().withThrowable(e).log(e.getMessage());
        }

        ctx.logger().atInfo().log("Finished Collecting PMD Results");
    }

    public static void main(String args[]) {
//        new PMDCollector("/home/git/msusel/msusel-patterns-experimenter/data/pmdresults/pmdreport.xml").execute(new ArcContext());
    }
}
