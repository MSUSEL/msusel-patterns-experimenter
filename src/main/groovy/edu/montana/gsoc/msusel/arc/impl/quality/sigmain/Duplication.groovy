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
package edu.montana.gsoc.msusel.arc.impl.quality.sigmain

import com.google.common.collect.Lists
import com.google.common.util.concurrent.AtomicDouble
import edu.isu.isuese.datamodel.*
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.metrics.annotations.*
import groovyx.gpars.GParsPool
import org.apache.commons.lang3.tuple.Pair

import java.nio.charset.MalformedInputException
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@MetricDefinition(
        name = "SIG Duplication",
        primaryHandle = "sigDuplication",
        description = "",
        properties = @MetricProperties(
                range = "Positive Integers",
                aggregation = [],
                scope = MetricScope.PROJECT,
                type = MetricType.Model,
                scale = MetricScale.Interval,
                category = MetricCategory.Quality
        ),
        references = []
)
class Duplication extends SigAbstractMetricEvaluator {

    AtomicInteger totalLines

    Duplication(ArcContext context) {
        super(context)
    }

    @Override
    def measureValue(Measurable node) {
        if (node instanceof Project) {
            context.open()
            boolean hasVal = Measure.valueFor(repo.getRepoKey(), "sigDuplication.RAW", node) > 0
            context.close()
            if (hasVal)
                return

            totalLines = new AtomicInteger(0)
            Project proj = node as Project

            context.open()
            List<File> srcFiles = Lists.newArrayList(proj.getFilesByType(FileType.SOURCE))
            context.close()

            AtomicDouble dupLines = new AtomicDouble(0.0)

            GParsPool.withPool(8) {
                srcFiles.eachParallel { File source ->
                    context.open()
                    dupLines.addAndGet(scanSelf(source))
                    context.close()
                }
            }

            List<Pair<Integer, Integer>> pairs = []
            for (int i = 0; i < srcFiles.size() - 1; i++) {
                for (int j = i + 1; j < srcFiles.size(); j++) {
                    pairs << Pair.of(i, j)
                }
            }

            GParsPool.withPool(8) {
                pairs.eachParallel { Pair<Integer, Integer> pair ->
                    File source = srcFiles.get(pair.getLeft())
                    File target = srcFiles.get(pair.getRight())
                    context.open()
                    dupLines.addAndGet(scanOther(source, target))
                    context.close()
                }
            }

            double dupPercent = (dupLines.get() / totalLines.get()) * 100

            context.open()
            Measure.of("${repo.getRepoKey()}:sigDuplication.RAW").on(node).withValue(dupPercent)
            context.close()
        }
    }

    double scanSelf(File file) {
        java.io.File f1 = new java.io.File(file.getFullPath())

        if (!f1.exists())
            return 0.0

        int dup = 0

        List<Method> methods = file.getAllMethods()
        for (int i = 0; i < methods.size(); i++) {
            int before
            int after
            String mod

            int size = f1.text.split("\n").size()
            if (size <= methods[i].getStart() || size <= methods[i].getEnd() || methods[i].getEnd() - methods[i].getStart() < 6)
                continue

            String m1Text = sanitize(f1.text.split("\n").toList().subList(methods[i].getStart(), methods[i].getEnd()).join("\n"))
            totalLines.addAndGet(m1Text.split("\n").size())

            try {

                before = m1Text.split("\n").size()
                mod = new String(m1Text)
                mod = processText(m1Text.split("\n").toList(), mod)
                after = mod.split("\n").size()

                dup += before - after
            } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                dup += 0
            }

            for (int j = i + 1; j < methods.size(); j++) {
                try {
                    size = f1.text.split("\n").size()
                    if (size <= methods[j].getStart() || size <= methods[j].getEnd() || methods[i].getEnd() - methods[i].getStart() < 6)
                        continue

                    String m2Text = f1.text.split("\n").toList().subList(methods[i].getStart(), methods[i].getEnd()).join("\n")
                    before = m2Text.split("\n").size()
                    totalLines.addAndGet(before)

                    mod = new String(m2Text)
                    mod = processText(m2Text.split("\n").toList(), mod)
                    after = mod.split("\n").size()

                    dup += before - after
                } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                    dup += 0
                }
            }
        }

        return dup
    }

    String processText(List<String> lines, String mod) {
        int i = 0
        for (; lines.size() >= 6 && i < lines.size() - 5; i++) {
            String subText = lines.subList(i, i + 6).join("\n")
            mod = mod.replace(subText, "")
        }
//        if (i < lines.size()) {
//            String subText = lines.subList(i, lines.size()).join("\n")
//            mod = mod.replace(subText, "")
//        }
        sanitize(mod)
    }

    double scanOther(File file1, File file2) {
        java.io.File f1 = new java.io.File(file1.getFullPath())
        java.io.File f2 = new java.io.File(file2.getFullPath())

        if (!f1.exists() || !f2.exists())
            return 0.0

        int dup = 0

        List<Method> methods = file1.getAllMethods()
        List<Method> other = file2.getAllMethods()

        methods.each { m1 ->
            try {
                int size = f1.text.split("\n").size()
                if (size > m1.getStart() && size > m1.getEnd() && m1.getEnd() - m1.getStart() >= 6) {
                    String m1Text = sanitize(f1.text.split("\n").toList().subList(m1.getStart(), m1.getEnd()).join("\n"))

                    other.each { m2 ->
                        try {
                            size = f2.text.split("\n").size()
                            if (size > m2.getStart() && size > m2.getEnd() && m2.getEnd() - m2.getStart() >= 6) {
                                String m2Text = sanitize(f2.text.split("\n").toList().subList(m2.getStart(), m2.getEnd()).join("\n"))
                                int before = m2Text.split("\n").size()
                                totalLines.addAndGet(before)
                                String mod = processText(m1Text.split("\n").toList(), m2Text)
                                int after = mod.split("\n").size()

                                dup += before - after
                            }
                        } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                            dup += 0
                        }
                    }
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException | MalformedInputException ex) {
                dup += 0
            }
        }

        return dup
    }

    String sanitize(String text) {
        text = text.replaceAll(/(?ms)\/\*.*?\*\\//, "")
        text = text.replaceAll(/\/\/.*/, "")
        text = text.replaceAll(/(?ms)^.*?\{/, "")
        List<String> lines = text.split("\n")
        for (int i = 0; i < lines.size(); i++)
            lines[i] = lines[i].trim()
        lines.removeAll("")
        for (int i = 0; i < lines.size(); i++)
            lines[i] = lines[i].replaceAll(/\s\s+/, " ")
        return lines.join("\n")
    }

    Metric toMetric(MetricRepository repository) {
        this.toMetric(repository, ["RAW", "RATING"])

        null
    }
}
