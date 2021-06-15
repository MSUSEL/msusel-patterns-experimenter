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
package org.hibernate.testing.junit4;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.testing.FailureExpected;
import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.SkipForDialects;

/**
 * Centralized utility functionality
 *
 * @author Steve Ebersole
 */
public class Helper {
	public static final String VALIDATE_FAILURE_EXPECTED = "hibernate.test.validatefailureexpected";


	/**
	 * Standard string content checking.
	 *
	 * @param string The string to check
	 * @return Are its content empty or the reference null?
	 */
	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	/**
	 * Extract a nice test name representation for display
	 *
	 * @param frameworkMethod The test method.
	 * @return The display representation
	 */
	public static String extractTestName(FrameworkMethod frameworkMethod) {
		return frameworkMethod.getMethod().getDeclaringClass().getName() + '#' + frameworkMethod.getName();
	}

	/**
	 * Extract a nice method name representation for display
	 *
	 * @param method The method.
	 * @return The display representation
	 */
	public static String extractMethodName(Method method) {
		return method.getDeclaringClass().getName() + "#" + method.getName();
	}

	public static <T extends Annotation> T locateAnnotation(
			Class<T> annotationClass,
			FrameworkMethod frameworkMethod,
			TestClass testClass) {
		T annotation = frameworkMethod.getAnnotation( annotationClass );
		if ( annotation == null ) {
			annotation = testClass.getJavaClass().getAnnotation( annotationClass );
		}
		return annotation;
	}

	/**
	 * @param singularAnnotationClass Singular annotation class (e.g. {@link SkipForDialect}).
	 * @param pluralAnnotationClass Plural annotation class (e.g. {@link SkipForDialects}). Assuming that the only
	 * 								declared method is an array of singular annotations.
	 * @param frameworkMethod Test method.
	 * @param testClass Test class.
	 * @param <S> Singular annotation type.
	 * @param <P> Plural annotation type.
	 * @return Collection of all singular annotations or an empty list.
	 */
	@SuppressWarnings("unchecked")
	public static <S extends Annotation, P extends Annotation> List<S> collectAnnotations(Class<S> singularAnnotationClass,
																						  Class<P> pluralAnnotationClass,
																						  FrameworkMethod frameworkMethod,
																						  TestClass testClass) {
		final List<S> collection = new LinkedList<S>();
		final S singularAnn = Helper.locateAnnotation( singularAnnotationClass, frameworkMethod, testClass );
		if ( singularAnn != null ) {
			collection.add( singularAnn );
		}
		final P pluralAnn = Helper.locateAnnotation( pluralAnnotationClass, frameworkMethod, testClass );
		if ( pluralAnn != null ) {
			try {
				collection.addAll( Arrays.asList( (S[]) pluralAnnotationClass.getDeclaredMethods()[0].invoke(pluralAnn) ) );
			}
			catch ( Exception e ) {
				throw new RuntimeException( e );
			}
		}
		return collection;
	}

	public static String extractMessage(FailureExpected failureExpected) {
		StringBuilder buffer = new StringBuilder();
		buffer.append( '(' ).append( failureExpected.jiraKey() ).append( ')' );
		if ( isNotEmpty( failureExpected.message() ) ) {
			buffer.append( " : " ).append( failureExpected.message() );
		}
		return buffer.toString();
	}

	public static String extractIgnoreMessage(FailureExpected failureExpected, FrameworkMethod frameworkMethod) {
		return new StringBuilder( "Ignoring test [" )
				.append( Helper.extractTestName( frameworkMethod ) )
				.append( "] due to @FailureExpected - " )
				.append( extractMessage( failureExpected ) )
				.toString();
	}

	/**
	 * @see #createH2Schema(String, Map)
	 */
	public static void createH2Schema(String schemaName, Configuration cfg) {
		createH2Schema( schemaName, cfg.getProperties() );
	}

	/**
	 * Create additional H2 schema.
	 *
	 * @param schemaName New schema name.
	 * @param settings Current settings.
	 */
	public static void createH2Schema(String schemaName, Map settings) {
		settings.put(
				Environment.URL,
				settings.get( Environment.URL ) + ";INIT=CREATE SCHEMA IF NOT EXISTS " + schemaName
		);
	}
}
