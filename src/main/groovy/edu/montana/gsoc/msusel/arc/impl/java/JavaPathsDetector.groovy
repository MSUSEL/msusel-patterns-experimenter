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
package edu.montana.gsoc.msusel.arc.impl.java

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.File
import edu.isu.isuese.datamodel.FileType
import edu.isu.isuese.datamodel.Project
import edu.montana.gsoc.msusel.arc.ArcContext
import groovy.util.logging.Log4j2
import org.apache.commons.lang3.tuple.Pair

import java.nio.file.Paths

@Log4j2
class JavaPathsDetector {

    ArcContext context

    JavaPathsDetector(ArcContext context) {
        this.context = context
    }

    void detect() {
        log.info "Detecting Project Paths"
        context.open()
        Pair<List<String>, List<String>> pairs = buildGraph(FileType.SOURCE)
        String[] src = pairs.getLeft()
        String[] test = pairs.getRight()

        context.getProject().setSrcPath(src)
        context.getProject().setTestPath(test)

        pairs = buildGraph(FileType.BINARY)
        String[] bin = pairs.getLeft()

        context.getProject().setBinPath(bin)
        context.close()
        log.info "Finished Detecting Project Paths"
    }

    Pair<List<String>, List<String>> buildGraph(FileType type) {
        log.info "Building Graph"
        // setup
        Project proj = context.getProject()
        String dir = Paths.get(context.getProjectDirectory()).toAbsolutePath().toString()

        List<File> files = proj.getFilesByType(type)
        List<String> names = []
        files.each { file ->
            names << file.getName().replace(dir, "").substring(1)
        }

        List<String> rootNS = proj.getRootNamespaces()*.getName()
        Set<String> paths = Sets.newHashSet()
        rootNS.each { ns ->
            names.each { name ->
                if (name.contains(ns)) {
                    paths << name.substring(0, name.indexOf(ns) - 1)
                }
            }
        }

        Set<String> testPaths = Sets.newHashSet()
        paths.each {
            if (it.contains("test"))
                testPaths << it
        }

        paths.removeAll(testPaths)

        testPaths.each { path ->
            files.each { file ->
                if (file.getName().contains(path))
                    file.setType(FileType.TEST)
            }
        }

        log.info "Finished Building Graph"
        return Pair.of(paths.asList(), testPaths.asList())
    }
}
