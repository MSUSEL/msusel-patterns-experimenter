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
package org.hibernate.bytecode.enhance.spi;

import javassist.CtClass;
import javassist.CtField;

/**
 * todo : not sure its a great idea to expose Javassist classes this way.
 * 		maybe wrap them in our own contracts?
 *
 * @author Steve Ebersole
 */
public interface EnhancementContext {
	/**
	 * Obtain access to the ClassLoader that can be used to load Class references.  In JPA SPI terms, this
	 * should be a "temporary class loader" as defined by
	 * {@link javax.persistence.spi.PersistenceUnitInfo#getNewTempClassLoader()}
	 */
	public ClassLoader getLoadingClassLoader();

	/**
	 * Does the given class descriptor represent a entity class?
	 *
	 * @param classDescriptor The descriptor of the class to check.
	 *
	 * @return {@code true} if the class is an entity; {@code false} otherwise.
	 */
	public boolean isEntityClass(CtClass classDescriptor);

	/**
	 * Does the given class name represent an embeddable/component class?
	 *
	 * @param classDescriptor The descriptor of the class to check.
	 *
	 * @return {@code true} if the class is an embeddable/component; {@code false} otherwise.
	 */
	public boolean isCompositeClass(CtClass classDescriptor);

	/**
	 * Should we in-line dirty checking for persistent attributes for this class?
	 *
	 * @param classDescriptor The descriptor of the class to check.
	 *
	 * @return {@code true} indicates that dirty checking should be in-lined within the entity; {@code false}
	 * indicates it should not.  In-lined is more easily serializable and probably more performant.
	 */
	public boolean doDirtyCheckingInline(CtClass classDescriptor);

	public boolean hasLazyLoadableAttributes(CtClass classDescriptor);

	// todo : may be better to invert these 2 such that the context is asked for an ordered list of persistent fields for an entity/composite

	/**
	 * Does the field represent persistent state?  Persistent fields will be "enhanced".
	 * <p/>
	 // 		may be better to perform basic checks in the caller (non-static, etc) and call out with just the
	 // 		Class name and field name...

	 * @param ctField The field reference.
	 *
	 * @return {@code true} if the field is ; {@code false} otherwise.
	 */
 	public boolean isPersistentField(CtField ctField);

	/**
	 * For fields which are persistent (according to {@link #isPersistentField}), determine the corresponding ordering
	 * maintained within the Hibernate metamodel.

	 * @param persistentFields The persistent field references.
	 *
	 * @return The ordered references.
	 */
	public CtField[] order(CtField[] persistentFields);

	public boolean isLazyLoadable(CtField field);
}
