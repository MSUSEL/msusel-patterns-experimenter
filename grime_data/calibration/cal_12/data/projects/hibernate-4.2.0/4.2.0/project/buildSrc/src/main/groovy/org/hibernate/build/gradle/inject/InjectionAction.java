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
package org.hibernate.build.gradle.inject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstantAttribute;
import javassist.bytecode.FieldInfo;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steve Ebersole
 */
public class InjectionAction implements Action<Task> {
	private static final Logger log = LoggerFactory.getLogger( InjectionAction.class );

	private final Project project;

	private List<Injection> injections = new ArrayList<Injection>();

	private LoaderClassPath loaderClassPath;
	private ClassPool classPool;

	public InjectionAction(Project project) {
		this.project = project;
	}

	void addInjection(Injection injection) {
		injections.add( injection );
	}

	@Override
	public void execute(Task task) {
		final ClassLoader runtimeScopeClassLoader = buildRuntimeScopeClassLoader();

		loaderClassPath = new LoaderClassPath( runtimeScopeClassLoader );
		classPool =  new ClassPool( true );
		classPool.appendClassPath( loaderClassPath );

		try {
			performInjections();
		}
		finally {
			loaderClassPath.close();
		}
	}

	private ClassLoader buildRuntimeScopeClassLoader() {
		final ArrayList<URL> classPathUrls = new ArrayList<URL>();
		final SourceSet mainSourceSet = project
				.getConvention()
				.getPlugin( JavaPluginConvention.class )
				.getSourceSets()
				.findByName( SourceSet.MAIN_SOURCE_SET_NAME );
		for ( File file : mainSourceSet.getRuntimeClasspath() ) {
			try {
				classPathUrls.add( file.toURI().toURL() );
			}
			catch (MalformedURLException e) {
				throw new InjectionException( "Could not determine artifact URL [" + file.getPath() + "]", e );
			}
		}
		return new URLClassLoader( classPathUrls.toArray( new URL[classPathUrls.size()] ), getClass().getClassLoader() );
	}

	private void performInjections() {
		for ( Injection injection : injections ) {
			for ( TargetMember targetMember : injection.getTargetMembers() ) {
				resolveInjectionTarget( targetMember ).inject( injection.getExpression() );
			}
		}
	}

	private InjectionTarget resolveInjectionTarget(TargetMember targetMember) {
		try {
			final CtClass ctClass = classPool.get( targetMember.getClassName() );
			// see if it is a field...
			try {
				CtField field = ctClass.getField( targetMember.getMemberName() );
				return new FieldInjectionTarget( targetMember, ctClass, field );
			}
			catch( NotFoundException ignore ) {
			}

			// see if it is a method...
			for ( CtMethod method : ctClass.getMethods() ) {
				if ( method.getName().equals( targetMember.getMemberName() ) ) {
					return new MethodInjectionTarget( targetMember, ctClass, method );
				}
			}

			// finally throw an exception
			throw new InjectionException( "Unknown member [" + targetMember.getQualifiedName() + "]" );
		}
		catch ( Throwable e ) {
			throw new InjectionException( "Unable to resolve class [" + targetMember.getClassName() + "]", e );
		}
	}

	/**
	 * Strategy for performing an injection
	 */
	private static interface InjectionTarget {
		/**
		 * Inject the given value per this target's strategy.
		 *
		 * @param value The value to inject.
		 *
		 * @throws org.hibernate.build.gradle.inject.InjectionException Indicates a problem performing the injection.
		 */
		public void inject(String value);
	}

	private abstract class BaseInjectionTarget implements InjectionTarget {
		@SuppressWarnings( {"UnusedDeclaration"})
		private final TargetMember targetMember;
		private final CtClass ctClass;
		private final File classFileLocation;

		protected BaseInjectionTarget(TargetMember targetMember, CtClass ctClass) {
			this.targetMember = targetMember;
			this.ctClass = ctClass;
			try {
				classFileLocation = new File( loaderClassPath.find( targetMember.getClassName() ).toURI() );
			}
			catch ( Throwable e ) {
				throw new InjectionException( "Unable to resolve class file path", e );
			}
		}

		@Override
		public void inject(String value) {
			doInjection( value );
			writeOutChanges();
		}

		protected abstract void doInjection(String value);

		protected void writeOutChanges() {
			log.info( "writing injection changes back [" + classFileLocation.getAbsolutePath() + "]" );
			long timeStamp = classFileLocation.lastModified();
			ClassFile classFile = ctClass.getClassFile();
			classFile.compact();
			try {
				DataOutputStream out = new DataOutputStream( new BufferedOutputStream( new FileOutputStream( classFileLocation ) ) );
				try {

					classFile.write( out );
					out.flush();
					if ( ! classFileLocation.setLastModified( System.currentTimeMillis() ) ) {
						log.info( "Unable to manually update class file timestamp" );
					}
				}
				finally {
					out.close();
					classFileLocation.setLastModified( timeStamp );
				}
			}
			catch ( IOException e ) {
				throw new InjectionException( "Unable to write out modified class file", e );
			}
		}
	}

	private class FieldInjectionTarget extends BaseInjectionTarget {
		private final CtField ctField;

		private FieldInjectionTarget(TargetMember targetMember, CtClass ctClass, CtField ctField) {
			super( targetMember, ctClass );
			this.ctField = ctField;
			if ( ! Modifier.isStatic( ctField.getModifiers() ) ) {
				throw new InjectionException( "Field is not static [" + targetMember.getQualifiedName() + "]" );
			}
		}

		@Override
		protected void doInjection(String value) {
			final FieldInfo ctFieldInfo = ctField.getFieldInfo();

			ctFieldInfo.addAttribute(
					new ConstantAttribute(
							ctFieldInfo.getConstPool(),
							ctFieldInfo.getConstPool().addStringInfo( value )
					)
			);

		}
	}

	private class MethodInjectionTarget extends BaseInjectionTarget {
		private final CtMethod ctMethod;

		private MethodInjectionTarget(TargetMember targetMember, CtClass ctClass, CtMethod ctMethod) {
			super( targetMember, ctClass );
			this.ctMethod = ctMethod;
		}

		@Override
		protected void doInjection(String value) {
			try {
				ctMethod.setBody( "{return \"" + value + "\";}" );
			}
			catch ( Throwable t ) {
				throw new InjectionException( "Unable to replace method body", t );
			}
		}
	}
}
