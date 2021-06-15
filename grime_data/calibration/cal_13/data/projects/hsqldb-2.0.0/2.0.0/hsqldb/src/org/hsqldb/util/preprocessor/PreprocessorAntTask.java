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

package org.hsqldb.util.preprocessor;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;

/* $Id: PreprocessorAntTask.java 751 2009-01-11 21:49:27Z unsaved $ */

/**
 * Provides a facility for invoking the Preprocessor from ANT. <p>
 *
 * Example ANT target: <p>
 *
 * <pre>
 * <target
 *     name="preprocess"
 *     depends="build-preprocessor">
 *
 *     <taskdef
 *         name="preprocess"
 *         classname="org.hsqldb.util.preprocessor.PreprocessorAntTask"
 *         classpath="${preprocessor.class.path}"/>
 *
 *     <preprocess
 *         srcdir="${src.dir}"
 *         targetdir="${target.dir}"
 *         altext=".pre"
 *         backup="true"
 *         encoding="UTF8"
 *         filter="true"
 *         indent="false"
 *         symbols="java_version=${java.version}, jdbc_version=${jdbc.version}"
 *         testonly="false"
 *         verbose="true"
 *         if="${preprocess.if}"
 *         unless="${preprocess.unless}">
 *     </preprocess>
 *
 * </target>
 * </pre>
 *
 * Task attributes :
 *
 * <table>
 *     <thead>
 *         <tr><td>name</td><td>description</td></tr>
 *     </thead>
 *     <tbody>
 *     <tr>
 *         <td>srcdir (required)</td>
 *         <td>string - directory under which input files are located</td>
 *     </tr>
 *     <tr>
 *         <td>targetdir (required)</td>
 *         <td>string - directory under which output files are to be written</td>
 *     </tr>
 *     <tr>
 *         <td>altext (optional)</td>
 *         <td>string - alternate extension to use for output file names. <br/>
 *         If needed, leading dot should be provided</td>
 *     </tr>
 *     <tr>
 *         <td>backup (optional - default: false)</td>
 *         <td>boolean - whether to back up pre-existing target files. <br/>
 *         When true, pre-existing target files are preserved by renaming with
 *         postfix "~"</td>
 *     </tr>
 *     <tr>
 *         <td>encoding (optional)</td>
 *         <td>string - the encoding with which to read and write file content.<br/>
 *         If specified, must be a valid Java encoding identifier, such as "UTF8". <br/>
 *         When unspecified, the default Java platformn encoding is used.</td>
 *     </tr>
 *     <tr>
 *         <td>filter (optional - default: false)</td>
 *         <td>boolean - whether to exclude directive lines from output.</td>
 *     </tr>
 *     <tr>
 *         <td>indent (optional - default: false)</td>
 *         <td>boolean - whether to indent directive lines in output.</td>
 *     </tr>
 *     <tr>
 *         <td>symbols (optional)</td>
 *         <td>string - CSV list of preprocessor symbols to predefine. <br/>
 *         When specified, each list element must be of the form: <br/>
 *         IDENT (ASSIGN? (STRING | NUMBER | IDENT) )?<br/>
 *         Not that forward assignments are illegal. <br/>
 *         See {@link Preprocessor Preprocessor} for details</td>
 *     </tr>
 *     <tr>
 *         <td>testonly (optional - default: false)</td>
 *         <td>boolean - whether to omit writing output files.</td>
 *     </tr>
 *     <tr>
 *         <td>verbose (optional - default: false)</td>
 *         <td>boolean - whether to log detailed information.</td>
 *     </tr>
 *     <tbody>
 * </table>
 *
 * @author boucherb@users
 * @version 1.8.1
 * @since 1.8.1
 */
public class PreprocessorAntTask extends MatchingTask {

    private String ifExpr;
    private String unlessExpr;
    private File   sourceDir;
    private File   targetDir;
    private String defines;
    private String altExt;
    private String encoding;
    private int    options = Option.INDENT;

    public void init() {
        super.init();
    }

    public void setSrcdir(final File value) {
        sourceDir = value;
    }

    public void setTargetdir(final File value) {
        targetDir = value;
    }

    public void setSymbols(final String value) {
        defines = value;
    }

    public void setVerbose(final boolean verbose) {
        options = Option.setVerbose(options, verbose);
    }

    public void setBackup(final boolean backup) {
        options = Option.setBackup(options, backup);
    }

    public void setIndent(final boolean indent) {
        options = Option.setIndent(options, indent);
    }

    public void setTestonly(final boolean testOnly) {
        options = Option.setTestOnly(options, testOnly);
    }

    public void setFilter(final boolean filter) {
        options = Option.setFilter(options, filter);
    }

    public void setAltext(final String ext) {
        this.altExt = ext;
    }

    public void setEncoding(final String encoding) {
          this.encoding = encoding;
    }

    public void setIf(final String expr) {
        this.ifExpr = expr;
    }

    public void setUnless(final String expr) {
        this.unlessExpr = expr;
    }

    public boolean isActive() {
        return (this.ifExpr == null
                || getProject().getProperty(this.ifExpr) != null
                || this.unlessExpr == null
                || getProject().getProperty(this.unlessExpr) == null);
    }

    public void execute() throws BuildException {

        if (!isActive()) {
            return;
        }

        checkTargetDir();

        this.sourceDir = getProject().resolveFile("" + this.sourceDir);

        IResolver resolver = new AntResolver(getProject());
        String[]  files    = getFiles();

        log("Preprocessing " + files.length + " file(s)");

        try {
            Preprocessor.preprocessBatch(this.sourceDir, this.targetDir, files,
                    this.altExt, this.encoding, this.options, this.defines,
                    resolver);
        } catch (Exception ex) {
            ex.printStackTrace();

            throw new BuildException("Preprocessing failed: " + ex,
                    ex);
        }
    }

    private String[] getFiles() {
        return getDirectoryScanner(sourceDir).getIncludedFiles();
    }

    private void checkTargetDir() throws BuildException {
        if (targetDir == null) {
            throw new BuildException("Target directory required.");
        }
    }
}
