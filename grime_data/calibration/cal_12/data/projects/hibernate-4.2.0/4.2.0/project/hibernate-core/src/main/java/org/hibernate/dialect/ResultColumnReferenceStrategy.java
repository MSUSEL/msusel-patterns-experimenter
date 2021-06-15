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
package org.hibernate.dialect;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines how we need to reference columns in the group-by, having, and order-by
 * clauses.
 *
 * @author Steve Ebersole
 */
public class ResultColumnReferenceStrategy implements Serializable {

	private static final Map INSTANCES = new HashMap();

	/**
	 * This strategy says to reference the result columns by the qualified column name
	 * found in the result source.  This strategy is not strictly allowed by ANSI SQL
	 * but is Hibernate's legacy behavior and is also the fastest of the strategies; thus
	 * it should be used if supported by the underlying database.
	 */
	public static final ResultColumnReferenceStrategy SOURCE = new ResultColumnReferenceStrategy( "source");

	/**
	 * For databases which do not support {@link #SOURCE}, ANSI SQL defines two allowable
	 * approaches.  One is to reference the result column by the alias it is given in the
	 * result source (if it is given an alias).  This strategy says to use this approach.
	 * <p/>
	 * The other QNSI SQL compliant approach is {@link #ORDINAL}.
	 */
	public static final ResultColumnReferenceStrategy ALIAS = new ResultColumnReferenceStrategy( "alias" );

	/**
	 * For databases which do not support {@link #SOURCE}, ANSI SQL defines two allowable
	 * approaches.  One is to reference the result column by the ordinal position at which
	 * it appears in the result source.  This strategy says to use this approach.
	 * <p/>
	 * The other QNSI SQL compliant approach is {@link #ALIAS}.
	 */
	public static final ResultColumnReferenceStrategy ORDINAL = new ResultColumnReferenceStrategy( "ordinal" );

	static {
		ResultColumnReferenceStrategy.INSTANCES.put( ResultColumnReferenceStrategy.SOURCE.name, ResultColumnReferenceStrategy.SOURCE );
		ResultColumnReferenceStrategy.INSTANCES.put( ResultColumnReferenceStrategy.ALIAS.name, ResultColumnReferenceStrategy.ALIAS );
		ResultColumnReferenceStrategy.INSTANCES.put( ResultColumnReferenceStrategy.ORDINAL.name, ResultColumnReferenceStrategy.ORDINAL );
	}

	private final String name;

	public ResultColumnReferenceStrategy(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private Object readResolve() throws ObjectStreamException {
		return parse( name );
	}

	public static ResultColumnReferenceStrategy parse(String name) {
		return ( ResultColumnReferenceStrategy ) ResultColumnReferenceStrategy.INSTANCES.get( name );
	}
}
