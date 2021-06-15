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
 * Extends {@link javax.persistence.Entity} with Hibernate features
 *
 * @author Emmanuel Bernard
 *
 * @deprecated See individual attributes for intended replacements.  To be removed in 4.1
 */
@Target(TYPE)
@Retention(RUNTIME)
@Deprecated
public @interface Entity {
	/**
	 * Is this entity mutable (read only) or not
	 *
	 * @deprecated use {@link org.hibernate.annotations.Immutable} 
	 */
	boolean mutable() default true;
	/**
	 * Needed column only in SQL on insert
	 * @deprecated use {@link DynamicInsert} instead
	 */
	boolean dynamicInsert() default false;
	/**
	 * Needed column only in SQL on update
	 * @deprecated Use {@link DynamicUpdate} instead
	 */
	boolean dynamicUpdate() default false;
	/**
	 *  Do a select to retrieve the entity before any potential update
	 *  @deprecated Use {@link SelectBeforeUpdate} instead
	 */
	boolean selectBeforeUpdate() default false;
	/**
	 * polymorphism strategy for this entity
	 * @deprecated use {@link Polymorphism} instead
	 */
	PolymorphismType polymorphism() default PolymorphismType.IMPLICIT;
	/**
	 * optimistic locking strategy
	 * @deprecated use {@link OptimisticLocking} instead.
	 */
	OptimisticLockType optimisticLock() default OptimisticLockType.VERSION;
	/**
	 * persister of this entity, default is hibernate internal one
	 * @deprecated  use {@link Persister} instead
	 */
	String persister() default "";
}
