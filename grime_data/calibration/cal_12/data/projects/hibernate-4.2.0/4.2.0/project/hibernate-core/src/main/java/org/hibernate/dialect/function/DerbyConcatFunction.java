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
import java.util.Iterator;
import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * A specialized concat() function definition in which:<ol>
 * <li>we translate to use the concat operator ('||')</li>
 * <li>wrap dynamic parameters in CASTs to VARCHAR</li>
 * </ol>
 * <p/>
 * This last spec is to deal with a limitation on DB2 and variants (e.g. Derby)
 * where dynamic parameters cannot be used in concatenation unless they are being
 * concatenated with at least one non-dynamic operand.  And even then, the rules
 * are so convoluted as to what is allowed and when the CAST is needed and when
 * it is not that we just go ahead and do the CASTing.
 *
 * @author Steve Ebersole
 */
public class DerbyConcatFunction implements SQLFunction {
	/**
	 * {@inheritDoc}
	 * <p/>
	 * Here we always return <tt>true</tt>
	 */
	public boolean hasArguments() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Here we always return <tt>true</tt>
	 */
	public boolean hasParenthesesIfNoArguments() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Here we always return {@link StandardBasicTypes#STRING}.
	 */
	public Type getReturnType(Type argumentType, Mapping mapping) throws QueryException {
		return StandardBasicTypes.STRING;
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Here's the meat..  The whole reason we have a separate impl for this for Derby is to re-define
	 * this method.  The logic here says that if not all the incoming args are dynamic parameters
	 * (i.e. <tt>?</tt>) then we simply use the Derby concat operator (<tt>||</tt>) on the unchanged
	 * arg elements.  However, if all the args are dynamic parameters, then we need to wrap the individual
	 * arg elements in <tt>cast</tt> function calls, use the concatenation operator on the <tt>cast</tt>
	 * returns, and then wrap that whole thing in a call to the Derby <tt>varchar</tt> function.
	 */
	public String render(Type argumentType, List args, SessionFactoryImplementor factory) throws QueryException {
		boolean areAllArgsParams = true;
		Iterator itr = args.iterator();
		while ( itr.hasNext() ) {
			final String arg = ( String ) itr.next();
			if ( ! "?".equals( arg ) ) {
				areAllArgsParams = false;
				break;
			}
		}

		if ( areAllArgsParams ) {
			return join(
					args.iterator(),
					new StringTransformer() {
						public String transform(String string) {
							return "cast( ? as varchar(32672) )";
						}
					},
					new StringJoinTemplate() {
						public String getBeginning() {
							return "varchar( ";
						}
						public String getSeparator() {
							return " || ";
						}
						public String getEnding() {
							return " )";
						}
					}
			);
		}
		else {
			return join(
					args.iterator(),
					new StringTransformer() {
						public String transform(String string) {
							return string;
						}
					},
					new StringJoinTemplate() {
						public String getBeginning() {
							return "(";
						}
						public String getSeparator() {
							return "||";
						}
						public String getEnding() {
							return ")";
						}
					}
			);
		}
	}

	private static interface StringTransformer {
		public String transform(String string);
	}

	private static interface StringJoinTemplate {
		/**
		 * Getter for property 'beginning'.
		 *
		 * @return Value for property 'beginning'.
		 */
		public String getBeginning();
		/**
		 * Getter for property 'separator'.
		 *
		 * @return Value for property 'separator'.
		 */
		public String getSeparator();
		/**
		 * Getter for property 'ending'.
		 *
		 * @return Value for property 'ending'.
		 */
		public String getEnding();
	}

	private static String join(Iterator/*<String>*/ elements, StringTransformer elementTransformer, StringJoinTemplate template) {
		// todo : make this available via StringHelper?
		StringBuilder buffer = new StringBuilder( template.getBeginning() );
		while ( elements.hasNext() ) {
			final String element = ( String ) elements.next();
			buffer.append( elementTransformer.transform( element ) );
			if ( elements.hasNext() ) {
				buffer.append( template.getSeparator() );
			}
		}
		return buffer.append( template.getEnding() ).toString();
	}
}
