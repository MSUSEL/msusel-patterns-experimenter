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

import org.hibernate.QueryException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.Type;

/**
 * Support for slightly more general templating than {@link StandardSQLFunction}, with an unlimited number of arguments.
 *
 * @author Gavin King
 */
public class VarArgsSQLFunction implements SQLFunction {
	private final String begin;
	private final String sep;
	private final String end;
	private final Type registeredType;

	/**
	 * Constructs a VarArgsSQLFunction instance with a 'static' return type.  An example of a 'static'
	 * return type would be something like an <tt>UPPER</tt> function which is always returning
	 * a SQL VARCHAR and thus a string type.
	 *
	 * @param registeredType The return type.
	 * @param begin The beginning of the function templating.
	 * @param sep The separator for each individual function argument.
	 * @param end The end of the function templating.
	 */
	public VarArgsSQLFunction(Type registeredType, String begin, String sep, String end) {
		this.registeredType = registeredType;
		this.begin = begin;
		this.sep = sep;
		this.end = end;
	}

	/**
	 * Constructs a VarArgsSQLFunction instance with a 'dynamic' return type.  For a dynamic return type,
	 * the type of the arguments are used to resolve the type.  An example of a function with a
	 * 'dynamic' return would be <tt>MAX</tt> or <tt>MIN</tt> which return a double or an integer etc
	 * based on the types of the arguments.
	 *
	 * @param begin The beginning of the function templating.
	 * @param sep The separator for each individual function argument.
	 * @param end The end of the function templating.
	 *
	 * @see #getReturnType Specifically, the 'firstArgumentType' argument is the 'dynamic' type.
	 */
	public VarArgsSQLFunction(String begin, String sep, String end) {
		this( null, begin, sep, end );
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Always returns true here.
	 */
	public boolean hasArguments() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Always returns true here.
	 */
	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
		return registeredType == null ? firstArgumentType : registeredType;
	}

	public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) {
		StringBuilder buf = new StringBuilder().append( begin );
		for ( int i = 0; i < arguments.size(); i++ ) {
			buf.append( transformArgument( ( String ) arguments.get( i ) ) );
			if ( i < arguments.size() - 1 ) {
				buf.append( sep );
			}
		}
		return buf.append( end ).toString();
	}

	/**
	 * Called from {@link #render} to allow applying a change or transformation
	 * to each individual argument.
	 *
	 * @param argument The argument being processed.
	 * @return The transformed argument; may be the same, though should never be null.
	 */
	protected String transformArgument(String argument) {
		return argument;
	}
}
