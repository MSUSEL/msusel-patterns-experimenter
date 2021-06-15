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
package org.hibernate.tool.instrument;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import org.hibernate.bytecode.buildtime.spi.Instrumenter;
import org.hibernate.bytecode.buildtime.spi.Logger;

/**
 * Super class for all Hibernate instrumentation tasks.  Provides the basic templating of how instrumentation
 * should occur; subclasses simply plug in to that process appropriately for the given bytecode provider.
 *
 * @author Steve Ebersole
 */
public abstract class BasicInstrumentationTask extends Task implements Instrumenter.Options {

	private final LoggerBridge logger = new LoggerBridge();

	private List filesets = new ArrayList();
	private boolean extended;

	// deprecated option...
	private boolean verbose;

	public void addFileset(FileSet set) {
		this.filesets.add( set );
	}

	protected final Iterator filesets() {
		return filesets.iterator();
	}

	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public final boolean performExtendedInstrumentation() {
		return isExtended();
	}

	protected abstract Instrumenter buildInstrumenter(Logger logger, Instrumenter.Options options);

	@Override
    public void execute() throws BuildException {
		try {
			buildInstrumenter( logger, this )
					.execute( collectSpecifiedFiles() );
		}
		catch ( Throwable t ) {
			throw new BuildException( t );
		}
	}

	private Set collectSpecifiedFiles() {
		HashSet files = new HashSet();
		Project project = getProject();
		Iterator filesets = filesets();
		while ( filesets.hasNext() ) {
			FileSet fs = ( FileSet ) filesets.next();
			DirectoryScanner ds = fs.getDirectoryScanner( project );
			String[] includedFiles = ds.getIncludedFiles();
			File d = fs.getDir( project );
			for ( int i = 0; i < includedFiles.length; ++i ) {
				files.add( new File( d, includedFiles[i] ) );
			}
		}
		return files;
	}

	protected class LoggerBridge implements Logger {
		public void trace(String message) {
			log( message, Project.MSG_VERBOSE );
		}

		public void debug(String message) {
			log( message, Project.MSG_DEBUG );
		}

		public void info(String message) {
			log( message, Project.MSG_INFO );
		}

		public void warn(String message) {
			log( message, Project.MSG_WARN );
		}

		public void error(String message) {
			log( message, Project.MSG_ERR );
		}
	}

}
