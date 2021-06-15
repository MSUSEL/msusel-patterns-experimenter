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
package org.hibernate.tool.enhance;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import org.hibernate.bytecode.enhance.spi.EnhancementContext;
import org.hibernate.bytecode.enhance.spi.Enhancer;

/**
 * Ant task for performing build-time enhancement of entities and component/embeddable classes.
 * <p/>
 * IMPL NOTE : currently makes numerous assumptions, the most "horrific" being that all entities are
 * annotated @Entity which precludes {@code hbm.xml} mappings as well as complete {@code orm.xml} mappings.  This is
 * just a PoC though...
 *
 * @author Steve Ebersole
 *
 * @see org.hibernate.engine.spi.Managed
 */
public class EnhancementTask extends Task implements EnhancementContext {
	private List<FileSet> filesets = new ArrayList<FileSet>();

	// Enhancer also builds CtClass instances.  Might make sense to share these (ClassPool).
	private final ClassPool classPool = new ClassPool( false );
	private final Enhancer enhancer = new Enhancer( this );

	public void addFileset(FileSet set) {
		this.filesets.add( set );
	}

	@Override
	public void execute() throws BuildException {
		log( "Starting Hibernate EnhancementTask execution", Project.MSG_INFO );

		// we use the CtClass stuff here just as a simple vehicle for obtaining low level information about
		// the class(es) contained in a file while still maintaining easy access to the underlying byte[]
		final Project project = getProject();

		for ( FileSet fileSet : filesets ) {
			final File fileSetBaseDir = fileSet.getDir( project );
			final DirectoryScanner directoryScanner = fileSet.getDirectoryScanner( project );
			for ( String relativeIncludedFileName : directoryScanner.getIncludedFiles() ) {
				final File javaClassFile = new File( fileSetBaseDir, relativeIncludedFileName );
				if ( ! javaClassFile.exists() ) {
					continue;
				}

				processClassFile( javaClassFile );
			}
		}
	}

	private void processClassFile(File javaClassFile) {
		try {
			final CtClass ctClass = classPool.makeClass( new FileInputStream( javaClassFile ) );
			if ( ! shouldInclude( ctClass ) ) {
				return;
			}

			final byte[] enhancedBytecode;
			try {
				enhancedBytecode = enhancer.enhance( ctClass.getName(), ctClass.toBytecode() );
			}
			catch (Exception e) {
				log( "Unable to enhance class [" + ctClass.getName() + "]", e, Project.MSG_WARN );
				return;
			}

			if ( javaClassFile.delete() ) {
				if ( ! javaClassFile.createNewFile() ) {
					log( "Unable to recreate class file [" + ctClass.getName() + "]", Project.MSG_INFO );
				}
			}
			else {
				log( "Unable to delete class file [" + ctClass.getName() + "]", Project.MSG_INFO );
			}

			FileOutputStream outputStream = new FileOutputStream( javaClassFile, false );
			try {
				outputStream.write( enhancedBytecode );
				outputStream.flush();
			}
			finally {
				try {
					outputStream.close();
				}
				catch ( IOException ignore) {
				}
			}
		}
		catch (FileNotFoundException ignore) {
			// should not ever happen because of explicit checks
		}
		catch (IOException e) {
			throw new BuildException(
					String.format( "Error processing included file [%s]", javaClassFile.getAbsolutePath() ),
					e
			);
		}
	}

	private boolean shouldInclude(CtClass ctClass) {
		// we currently only handle entity enhancement
		return ! ctClass.hasAnnotation( Entity.class );
	}


	// EnhancementContext impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public ClassLoader getLoadingClassLoader() {
		return getClass().getClassLoader();
	}

	@Override
	public boolean isEntityClass(CtClass classDescriptor) {
		// currently we only call enhance on the classes with @Entity, so here we always return true
		return true;
	}

	@Override
	public boolean isCompositeClass(CtClass classDescriptor) {
		return false;
	}

	@Override
	public boolean doDirtyCheckingInline(CtClass classDescriptor) {
		return false;
	}

	@Override
	public boolean hasLazyLoadableAttributes(CtClass classDescriptor) {
		return true;
	}

	@Override
	public boolean isLazyLoadable(CtField field) {
		return true;
	}

	@Override
	public boolean isPersistentField(CtField ctField) {
		// current check is to look for @Transient
		return ! ctField.hasAnnotation( Transient.class );
	}

	@Override
	public CtField[] order(CtField[] persistentFields) {
		// for now...
		return persistentFields;
		// eventually needs to consult the Hibernate metamodel for proper ordering
	}
}
