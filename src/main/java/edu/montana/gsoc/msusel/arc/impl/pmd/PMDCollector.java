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
package edu.montana.gsoc.msusel.arc.impl.pmd;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.collector.FileCollector;
import edu.montana.gsoc.msusel.arc.impl.pmd.resultsdm.Pmd;
import edu.montana.gsoc.msusel.datamodel.DataModelMediator;
import edu.montana.gsoc.msusel.datamodel.measures.Finding;
import edu.montana.gsoc.msusel.datamodel.measures.Rule;
import edu.montana.gsoc.msusel.datamodel.member.Member;
import edu.montana.gsoc.msusel.datamodel.structural.File;
import edu.montana.gsoc.msusel.datamodel.type.Type;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.List;

@Slf4j
public class PMDCollector extends FileCollector {

    @Inject
    DataModelMediator mediator;

    public PMDCollector(String resultsFile) {
        super("PMD Collector", resultsFile);
    }

    @Override
    public void execute(ArcContext ctx) {
        try {
            JAXBContext context = JAXBContext.newInstance(Pmd.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Pmd pmd = (Pmd) unmarshaller.unmarshal(new java.io.File(resultsFile));
            System.out.println("Files: " + pmd.getFile().size());
            pmd.getFile().forEach(f -> System.out.println("File: " + f.getName() + " has " + f.getViolation().size() + " violations"));

            List<Finding> violations = Lists.newArrayList();
            pmd.getFile().forEach(file ->
                file.getViolation().forEach(v -> {
                    Rule rule = mediator.getRule("pmd:" + v.getRule());
                    File f = mediator.findFile(file.getName());
                    Type t = f.findType(v.getPackage() + "." + v.getClazz());
                    Member m = t.findMemberInRange(v.getBeginline().intValue(), v.getEndline().intValue());
                    Finding finding = Finding.builder().rule(rule).ref(m.asRef()).create();
                    violations.add(finding);
                })
            );
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }
    }

    public static void main(String args[]) {
        new PMDCollector("/home/git/msusel/msusel-patterns-experimenter/data/pmdresults/pmdreport.xml").execute(new ArcContext());
    }
}
