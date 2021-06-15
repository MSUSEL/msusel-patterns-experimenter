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
package org.hibernate.ejb.criteria.expression;

import java.io.Serializable;

import org.hibernate.ejb.criteria.AbstractNode;
import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.TupleElementImplementor;
import org.hibernate.ejb.criteria.ValueHandlerFactory;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public abstract class AbstractTupleElement<X>
		extends AbstractNode
		implements TupleElementImplementor<X>, Serializable {
	private final Class originalJavaType;
	private Class<X> javaType;
	private String alias;
	private ValueHandlerFactory.ValueHandler<X> valueHandler;

	protected AbstractTupleElement(CriteriaBuilderImpl criteriaBuilder, Class<X> javaType) {
		super( criteriaBuilder );
		this.originalJavaType = javaType;
		this.javaType = javaType;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<X> getJavaType() {
		return javaType;
	}

	@SuppressWarnings({ "unchecked" })
	protected void resetJavaType(Class targetType) {
		this.javaType = targetType;
//		this.valueHandler = javaType.equals( originalJavaType )
//				? null
//				: ValueHandlerFactory.determineAppropriateHandler( javaType );
		this.valueHandler = ValueHandlerFactory.determineAppropriateHandler( javaType );
	}

	protected void forceConversion(ValueHandlerFactory.ValueHandler<X> valueHandler) {
		this.valueHandler = valueHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public ValueHandlerFactory.ValueHandler<X> getValueHandler() {
		return valueHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Protected access to define the alias.
	 *
	 * @param alias The alias to use.
	 */
	protected void setAlias(String alias) {
		this.alias = alias;
	}
}
