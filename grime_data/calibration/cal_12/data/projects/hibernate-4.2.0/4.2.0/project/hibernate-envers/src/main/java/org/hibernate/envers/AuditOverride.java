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
package org.hibernate.envers;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.persistence.MappedSuperclass;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The {@code AuditingOverride} annotation is used to override the auditing
 * behavior of a superclass or single property inherited from {@link MappedSuperclass}
 * type, or attribute inside an embedded component.
 *
 * @author Erik-Berndt Scheper
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 * @see javax.persistence.Embedded
 * @see javax.persistence.Embeddable
 * @see javax.persistence.MappedSuperclass  
 * @see javax.persistence.AssociationOverride
 * @see AuditJoinTable
 */
@Target({ TYPE, METHOD, FIELD })
@Retention(RUNTIME)
public @interface AuditOverride {

	/**
	 * @return Name of the field (or property) whose mapping is being overridden. Allows empty value if
	 * {@link AuditOverride} is used to change auditing behavior of all attributes inherited from
	 * {@link MappedSuperclass} type.
	 */
	String name() default "";

	/**
	 * @return Indicates if the field (or property) is audited; defaults to {@code true}.
	 */
	boolean isAudited() default true;

	/**
	 * @return New {@link AuditJoinTable} used for this field (or property). Its value
	 * is ignored if {@link #isAudited()} equals to {@code false}.
	 */
	AuditJoinTable auditJoinTable() default @AuditJoinTable;

	/**
	 * @return Specifies class which field (or property) mapping is being overridden. <strong>Required</strong> if
	 * {@link AuditOverride} is used to change auditing behavior of attributes inherited from {@link MappedSuperclass}
	 * type.
	 */
	Class forClass() default void.class;
}
