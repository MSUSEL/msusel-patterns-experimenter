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
package edu.montana.gsoc.msusel.arc.impl.pattern4;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.collector.FileCollector;
import edu.montana.gsoc.msusel.arc.impl.pattern4.resultsdm.Pattern;
import edu.montana.gsoc.msusel.arc.impl.pattern4.resultsdm.PatternInstance;
import edu.montana.gsoc.msusel.arc.impl.pattern4.resultsdm.Project;
import edu.montana.gsoc.msusel.arc.impl.pattern4.resultsdm.Role;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;

@Slf4j
public class Pattern4Collector extends FileCollector {

    Pattern4Tool owner;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    @Builder(buildMethodName = "create")
    public Pattern4Collector(Pattern4Tool owner, String resultFile, edu.isu.isuese.datamodel.Project project) {
        super(Pattern4Constants.PATTERN4_COLL_NAME, resultFile, project);
        this.owner = owner;
    }

    @Override
    public void execute(ArcContext context) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(Project.class);
        xstream.processAnnotations(Pattern.class);
        xstream.processAnnotations(PatternInstance.class);
        xstream.processAnnotations(Role.class);

        Project proj = (Project) xstream.fromXML(new File(this.resultsFile));

        // for each pattern instance's roles we need to modify the key to be consistent with the current keying scheme
        proj.getPatterns().forEach(pattern -> {
            pattern.getInstances().forEach(instance -> {
                instance.getRoles().forEach(role -> {
                    String name = role.getElement();

                    def group = ((String) it =~ /((\w+\b\.)+)((\w+)(\$(\w+))?)((::)?)((\w+)\(.*\))?/)
                    println group.count
                    println group.hasGroup()
                    if (group.size() > 0) {
                        println group[0]
                        println "Namespace: ${group[0][1]}"
                        println "Type Name: ${group[0][3]}"
                        println "Method Name: ${group[0][10]}"
                    }

                    mediator.findComponentByName(name);
                });
            });
        });
    }
}
