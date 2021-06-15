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
package org.hibernate.annotations;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Complementary information to a table either primary or secondary
 *
 * @author Emmanuel Bernard
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface Table {
	/**
	 * name of the targeted table
	 */
	String appliesTo();

	/**
	 * Indexes
	 */
	Index[] indexes() default {};

	/**
	 * define a table comment
	 */
	String comment() default "";

	/**
	 * Defines the Foreign Key name of a secondary table
	 * pointing back to the primary table
	 */
	ForeignKey foreignKey() default @ForeignKey( name="" );

	/**
	 * If set to JOIN, the default, Hibernate will use an inner join to retrieve a
	 * secondary table defined by a class or its superclasses and an outer join for a
	 * secondary table defined by a subclass.
	 * If set to select then Hibernate will use a
	 * sequential select for a secondary table defined on a subclass, which will be issued only if a row
	 * turns out to represent an instance of the subclass. Inner joins will still be used to retrieve a
	 * secondary defined by the class and its superclasses.
	 *
	 * <b>Only applies to secondary tables</b>
	 */
	FetchMode fetch() default FetchMode.JOIN;

	/**
	 * If true, Hibernate will not try to insert or update the properties defined by this join.
	 *
	 * <b>Only applies to secondary tables</b>
	 */
	boolean inverse() default false;

	/**
	 * If enabled, Hibernate will insert a row only if the properties defined by this join are non-null
	 * and will always use an outer join to retrieve the properties.
	 *
	 * <b>Only applies to secondary tables</b>
	 */
	boolean optional() default true;

	/**
	 * Defines a custom SQL insert statement
	 *
	 * <b>Only applies to secondary tables</b>
	 */
	SQLInsert sqlInsert() default @SQLInsert(sql="");

	/**
	 * Defines a custom SQL update statement
	 *
	 * <b>Only applies to secondary tables</b>
	 */
	SQLUpdate sqlUpdate() default @SQLUpdate(sql="");

	/**
	 * Defines a custom SQL delete statement
	 *
	 * <b>Only applies to secondary tables</b>
	 */
	SQLDelete sqlDelete() default @SQLDelete(sql="");
}
