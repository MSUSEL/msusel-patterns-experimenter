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

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Extends {@link javax.persistence.NamedNativeQuery} with Hibernate features
 *
 * @author Emmanuel Bernard
 */
@Target({TYPE, PACKAGE})
@Retention(RUNTIME)
public @interface NamedNativeQuery {
	String name();

	String query();

	Class resultClass() default void.class;

	String resultSetMapping() default ""; // name of SQLResultSetMapping
	/** the flush mode for the query */
	FlushModeType flushMode() default FlushModeType.PERSISTENCE_CONTEXT;
	/** mark the query as cacheable or not */
	boolean cacheable() default false;
	/** the cache region to use */
	String cacheRegion() default "";
	/** the number of rows fetched by the JDBC Driver per roundtrip */
	int fetchSize() default -1;
	/**the query timeout in seconds*/
	int timeout() default -1;

	boolean callable() default false;
	/**comment added to the SQL query, useful for the DBA */
	String comment() default "";
	/**the cache mode used for this query*/
	CacheModeType cacheMode() default CacheModeType.NORMAL;
	/**marks whether the results are fetched in read-only mode or not*/
	boolean readOnly() default false;
}
