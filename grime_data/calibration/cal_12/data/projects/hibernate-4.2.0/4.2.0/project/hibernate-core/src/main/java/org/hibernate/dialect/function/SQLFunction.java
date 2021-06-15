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
 * Provides support routines for the HQL functions as used
 * in the various SQL Dialects
 *
 * Provides an interface for supporting various HQL functions that are
 * translated to SQL. The Dialect and its sub-classes use this interface to
 * provide details required for processing of the function.
 *
 * @author David Channon
 * @author Steve Ebersole
 */
public interface SQLFunction {
	/**
	 * Does this function have any arguments?
	 *
	 * @return True if the function expects to have parameters; false otherwise.
	 */
	public boolean hasArguments();

	/**
	 * If there are no arguments, are parentheses required?
	 *
	 * @return True if a no-arg call of this function requires parentheses.
	 */
	public boolean hasParenthesesIfNoArguments();

	/**
	 * The return type of the function.  May be either a concrete type which is preset, or variable depending upon
	 * the type of the first function argument.
	 * <p/>
	 * Note, the 'firstArgumentType' parameter should match the one passed into {@link #render}
	 *
	 * @param firstArgumentType The type of the first argument
	 * @param mapping The mapping source.
	 *
	 * @return The type to be expected as a return.
	 *
	 * @throws org.hibernate.QueryException Indicates an issue resolving the return type.
	 */
	public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException;


	/**
	 * Render the function call as SQL fragment.
	 * <p/>
	 * Note, the 'firstArgumentType' parameter should match the one passed into {@link #getReturnType}
	 *
	 * @param firstArgumentType The type of the first argument
	 * @param arguments The function arguments
	 * @param factory The SessionFactory
	 *
	 * @return The rendered function call
	 *
	 * @throws org.hibernate.QueryException Indicates a problem rendering the
	 * function call.
	 */
	public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory) throws QueryException;
}
