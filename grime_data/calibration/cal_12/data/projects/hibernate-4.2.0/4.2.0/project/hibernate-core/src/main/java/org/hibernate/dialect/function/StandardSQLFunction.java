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
package org.hibernate.dialect.function;
import java.util.List;

import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Provides a standard implementation that supports the majority of the HQL
 * functions that are translated to SQL. The Dialect and its sub-classes use
 * this class to provide details required for processing of the associated
 * function.
 *
 * @author David Channon
 */
public class StandardSQLFunction implements SQLFunction {
	private final String name;
	private final Type registeredType;

	/**
	 * Construct a standard SQL function definition with a variable return type;
	 * the actual return type will depend on the types to which the function
	 * is applied.
	 * <p/>
	 * Using this form, the return type is considered non-static and assumed
	 * to be the type of the first argument.
	 *
	 * @param name The name of the function.
	 */
	public StandardSQLFunction(String name) {
		this( name, null );
	}

	/**
	 * Construct a standard SQL function definition with a static return type.
	 *
	 * @param name The name of the function.
	 * @param registeredType The static return type.
	 */
	public StandardSQLFunction(String name, Type registeredType) {
		this.name = name;
		this.registeredType = registeredType;
	}

	/**
	 * Function name accessor
	 *
	 * @return The function name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Function static return type accessor.
	 *
	 * @return The static function return type; or null if return type is
	 * not static.
	 */
	public Type getType() {
		return registeredType;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasArguments() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getReturnType(Type firstArgumentType, Mapping mapping) {
		return registeredType == null ? firstArgumentType : registeredType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor sessionFactory) {
		StringBuilder buf = new StringBuilder();
		buf.append( name ).append( '(' );
		for ( int i = 0; i < arguments.size(); i++ ) {
			buf.append( arguments.get( i ) );
			if ( i < arguments.size() - 1 ) {
				buf.append( ", " );
			}
		}
		return buf.append( ')' ).toString();
	}

	public String toString() {
		return name;
	}

}
