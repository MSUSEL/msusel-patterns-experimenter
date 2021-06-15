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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.javascript.host.PropertiesTest;

/**
 * Generates HTML file with all <tt>NotYetImplemented</tt> methods.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class NotYetImplementedTest {

    private Set<String> entries_ = new TreeSet<String>();

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        process(new File("src/test/java"));
        save();
    }

    private void process(final File dir) throws IOException {
        for (final File file : dir.listFiles()) {
            if (file.isDirectory() && !file.getName().equals(".svn")) {
                process(file);
            }
            else {
                if (file.getName().endsWith(".java") && !file.getName().equals("WebTestCase.java")
                        && !file.getName().equals("NotYetImplementedTest.java")
                        && !file.getName().equals("CodeStyleTest.java")) {
                    final List<String> lines = CodeStyleTest.getLines(file);
                    final String relativePath = file.getAbsolutePath().substring(
                        new File(".").getAbsolutePath().length() - 1).replace('\\', '/');
                    process(lines, relativePath);
                }
            }
        }
    }

    private void process(final List<String> lines, final String path) {
        int index = 1;
        for (final String line : lines) {
            if (line.contains("notYetImplemented()")) {
                String methodName = null;
                for (int i = index; i >= 0; i--) {
                    final String l = lines.get(i);
                    if (l.startsWith("    public ")) {
                        methodName = l.split(" ")[6];
                        break;
                    }
                }
                final int lineNumber = getLineNumber(lines, index);
                final String description = getDescription(lines, index);
                entries_.add(path + ';' + methodName + ';' + lineNumber + ';' + description);
            }
            else if (line.contains("@NotYetImplemented")) {
                final String browser;
                if (line.contains("(")) {
                    browser = line.replaceAll(".*\\((.*)\\).*", "$1").replaceAll("Browser\\.", "")
                        .replaceAll("[{}]", "").trim();
                }
                else {
                    browser = "";
                }
                String methodName = null;
                for (int i = index; i < lines.size(); i++) {
                    final String l = lines.get(i);
                    if (l.startsWith("    public ")) {
                        methodName = l.split(" ")[6];
                        break;
                    }
                }
                final int lineNumber = getLineNumber(lines, index);
                final String description = getDescription(lines, index);
                entries_.add(path + ';' + methodName + ';' + lineNumber + ";" + browser + ';' + description);
            }
            index++;
        }
    }

    private static int getLineNumber(final List<String> lines, final int index) {
        for (int i = index; i >= 0; i--) {
            final String l = lines.get(i);
            if (l.startsWith("    /**")) {
                return i;
            }
        }
        return 0;
    }

    private static String getDescription(final List<String> lines, final int index) {
        final StringBuilder builder = new StringBuilder();
        for (int i = getLineNumber(lines, index); i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            final int start = line.indexOf(' ') != -1 ? line.indexOf(' ') + 1 : -1;
            final boolean end = line.endsWith("*/");
            if (line.contains("* @throws ") || line.contains("* @exception")) {
                break;
            }
            if (start != -1) {
                if (builder.length() != 0) {
                    builder.append(' ');
                }
                builder.append(line.substring(start, line.length() - (end ? 2 : 0)));
            }
            if (end) {
                break;
            }
        }
        return builder.toString().replace(";", "__semicolon__");
    }

    private void save() throws Exception {
        final StringBuilder builder = new StringBuilder();
        builder.append("<html><head></head><body>\n");
        builder.append("NotYetImplemented is a condition in which a test is known to fail with HtmlUnit.");
        builder.append("<table border='1'>\n");
        builder.append("  <tr><th>File</th><th>Method</th><th>Line</th><th>Description</th></tr>\n");
        String lastFile = null;
        for (final String entry : entries_) {
            final String[] values = entry.split(";");
            final String file = values[0];
            final String fileName = file.substring(file.lastIndexOf('/') + 1, file.length() - 5);
            final String method = values[1];
            final String line = values[2];
            final String browser = values.length > 4 ? values[3] : "";
            final String description = entry.endsWith(";") ? "&nbsp;"
                    : values[values.length - 1].replace("__semicolon__", ";");
            builder.append("  <tr>\n");
            if (!file.equals(lastFile)) {
                int totalCount = 0;
                for (final String e : entries_) {
                    if (e.startsWith(file)) {
                        totalCount++;
                    }
                }
                if (totalCount != 1) {
                    builder.append("    <td rowspan='" + totalCount + "'>");
                }
                else {
                    builder.append("    <td>");
                }
                builder.append(fileName);
                builder.append("</td>\n");
                lastFile = file;
            }
            builder.append("    <td><a href='http://htmlunit.svn.sourceforge.net/viewvc/htmlunit/trunk/htmlunit/"
                    + file + "?view=markup#l_" + line + "'>").append(method).append("</a> ")
                    .append(browser).append("</td>\n");
            builder.append("    <td>").append(line).append("</td>\n");
            builder.append("    <td>").append(description).append("</td>\n");
            builder.append("  </tr>\n");
        }
        builder.append("</table>\n").append("</body></html>");
        FileUtils.writeStringToFile(new File(PropertiesTest.getArtifactsDirectory(), "notYetImplemented.html"),
                builder.toString());
    }

}
