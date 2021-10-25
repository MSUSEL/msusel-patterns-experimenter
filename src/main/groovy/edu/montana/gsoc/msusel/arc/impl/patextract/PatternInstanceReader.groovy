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

import edu.isu.isuese.datamodel.Field
import edu.isu.isuese.datamodel.Method
import edu.isu.isuese.datamodel.Namespace
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternInstance
import edu.isu.isuese.datamodel.Project
import edu.isu.isuese.datamodel.RefType
import edu.isu.isuese.datamodel.Reference
import edu.isu.isuese.datamodel.Role
import edu.isu.isuese.datamodel.RoleBinding
import edu.isu.isuese.datamodel.Type

@Singleton
class PatternInstanceReader {

    void read(Project project) {
        String path = project.getFullPath().replace("./", "")
        File file = new File(new File(path), "instance.conf")
        if (file.exists() && file.canRead()) {
            ConfigSlurper slurper = new ConfigSlurper()
            ConfigObject instData = slurper.parse(file.text)
            instData.flatten()

            Pattern pattern = Pattern.findFirst("patternKey = ?", instData.instance.pattern)
            PatternInstance inst = PatternInstance.builder().instKey(project.getProjectKey() + ":" + instData.instance.instKey).create()
            inst.save()
            project.addPatternInstance(inst)
            pattern.addInstance(inst)

            instData.instance.bindings.each { item ->
                Role role = Role.findFirst("roleKey = ?", item.role)
                Reference ref = null
                switch (RefType.fromValue(Integer.valueOf(item.refType))) {
                    case RefType.TYPE:
                        Type t = Type.findFirst("compKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = t.createReference()
                        break
                    case RefType.METHOD:
                        Method m = Method.findFirst("compKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = m.createReference()
                        break
                    case RefType.NAMESPACE:
                        Namespace ns = Namespace.findFirst("nsKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = ns.createReference()
                        break
                    case RefType.FIELD:
                        Field f = Field.findFirst("compKey = ?", project.getProjectKey() + ":" + item.ref)
                        ref = f.createReference()
                        break
                }
                inst.addRoleBinding(RoleBinding.of(role, ref))
            }
        }
    }
}
