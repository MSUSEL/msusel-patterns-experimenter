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
import java.util.List;

import org.junit.Ignore;
import org.junit.runners.model.FrameworkMethod;

import org.hibernate.testing.FailureExpected;

/**
 * Defines an extension to the standard JUnit {@link FrameworkMethod} information about a test method.
 *
 * @author Steve Ebersole
 */
public class ExtendedFrameworkMethod extends FrameworkMethod {
	private final FrameworkMethod delegatee;
	private final Ignore virtualIgnore;
	private final FailureExpected failureExpectedAnnotation;

	public ExtendedFrameworkMethod(FrameworkMethod delegatee, Ignore virtualIgnore, FailureExpected failureExpectedAnnotation) {
		super( delegatee.getMethod() );
		this.delegatee = delegatee;
		this.virtualIgnore = virtualIgnore;
		this.failureExpectedAnnotation = failureExpectedAnnotation;
	}

	public FailureExpected getFailureExpectedAnnotation() {
		return failureExpectedAnnotation;
	}

	@Override
	public Method getMethod() {
		return delegatee.getMethod();
	}

	@Override
	public Object invokeExplosively(Object target, Object... params) throws Throwable {
		return delegatee.invokeExplosively( target, params );
	}

	@Override
	public String getName() {
		return delegatee.getName();
	}

	@Override
	public void validatePublicVoidNoArg(boolean isStatic, List<Throwable> errors) {
		delegatee.validatePublicVoidNoArg( isStatic, errors );
	}

	@Override
	public void validatePublicVoid(boolean isStatic, List<Throwable> errors) {
		delegatee.validatePublicVoid( isStatic, errors );
	}

	@Override
	public boolean isShadowedBy(FrameworkMethod other) {
		return delegatee.isShadowedBy( other );
	}

	@Override
	@SuppressWarnings( {"EqualsWhichDoesntCheckParameterClass"})
	public boolean equals(Object obj) {
		return delegatee.equals( obj );
	}

	@Override
	public int hashCode() {
		return delegatee.hashCode();
	}

	@Override
	public Annotation[] getAnnotations() {
		return delegatee.getAnnotations();
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		if ( Ignore.class.equals( annotationType ) && virtualIgnore != null ) {
			return (T) virtualIgnore;
		}
		return delegatee.getAnnotation( annotationType );
	}
}
