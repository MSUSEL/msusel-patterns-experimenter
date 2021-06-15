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
package org.hibernate.sql;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.internal.util.StringHelper;

/**
 * An SQL IN expression.
 * <br>
 * <code>... in(...)</code>
 * <br>
 * @author Gavin King
 */
public class InFragment {

	public static final String NULL = "null";
	public static final String NOT_NULL = "not null";

	private String columnName;
	private List<Object> values = new ArrayList<Object>();

	/**
	 * @param value an SQL literal, NULL, or NOT_NULL
	 *
	 * @return {@code this}, for method chaining
	 */
	public InFragment addValue(Object value) {
		values.add(value);
		return this;
	}

	public InFragment setColumn(String columnName) {
		this.columnName = columnName;
		return this;
	}

	public InFragment setColumn(String alias, String columnName) {
		this.columnName = StringHelper.qualify( alias, columnName );
		return setColumn(this.columnName);
	}

	public InFragment setFormula(String alias, String formulaTemplate) {
		this.columnName = StringHelper.replace(formulaTemplate, Template.TEMPLATE, alias);
		return setColumn(this.columnName);
	}

	public String toFragmentString() {

                if (values.size() == 0) {
                   return "1=2";
                }

                StringBuilder buf = new StringBuilder(values.size() * 5);

                if (values.size() == 1) {
                   Object value = values.get(0);
                   buf.append(columnName);

                   if (NULL.equals(value)) {
                      buf.append(" is null");
                   } else {
                      if (NOT_NULL.equals(value)) {
                         buf.append(" is not null");
                      } else {
                         buf.append('=').append(value);
                      }
                   }
                   return buf.toString();
                }
                   
                boolean allowNull = false;

                for (Object value : values) {
                   if (NULL.equals(value)) {
                      allowNull = true;
                   } else {
                      if (NOT_NULL.equals(value)) {
                         throw new IllegalArgumentException("not null makes no sense for in expression");
                      }
                   }
                }

                if (allowNull) {
                   buf.append('(').append(columnName).append(" is null or ").append(columnName).append(" in (");
                } else {
                   buf.append(columnName).append(" in (");
                }

                for (Object value : values) {
                   if ( ! NULL.equals(value) ) {
                      buf.append(value);
                      buf.append(", ");
                   }
                }

                buf.setLength(buf.length() - 2);

                if (allowNull) {
                   buf.append("))");
                } else {
                   buf.append(')');
                }

                return buf.toString();

	}
}
