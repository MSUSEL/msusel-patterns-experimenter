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
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * Emulation of <tt>locate()</tt> on PostgreSQL
 * @author Gavin King
 */
public class PositionSubstringFunction implements SQLFunction {
	public boolean hasArguments() {
		return true;
	}

	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
		return StandardBasicTypes.INTEGER;
	}

	public String render(Type firstArgumentType, List args, SessionFactoryImplementor factory) throws QueryException {
		boolean threeArgs = args.size() > 2;
		Object pattern = args.get(0);
		Object string = args.get(1);
		Object start = threeArgs ? args.get(2) : null;

		StringBuilder buf = new StringBuilder();
		if (threeArgs) buf.append('(');
		buf.append("position(").append( pattern ).append(" in ");
		if (threeArgs) buf.append( "substring(");
		buf.append( string );
		if (threeArgs) buf.append( ", " ).append( start ).append(')');
		buf.append(')');
		if (threeArgs) buf.append('+').append( start ).append("-1)");
		return buf.toString();
	}


}
