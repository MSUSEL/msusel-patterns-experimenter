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
package org.hibernate.metamodel.binding;

/**
 * Contract describing the attribute binding for singular associations ({@code many-to-one}, {@code one-to-one}).
 *
 * @author Gail Badner
 * @author Steve Ebersole
 */
@SuppressWarnings( {"JavaDoc", "UnusedDeclaration"})
public interface SingularAssociationAttributeBinding extends SingularAttributeBinding, AssociationAttributeBinding {
	/**
	 * Is this association based on a property reference (non PK column(s) as target of FK)?
	 * <p/>
	 * Convenience form of checking {@link #getReferencedAttributeName()} for {@code null}.
	 * 
	 * @return
	 */
	public boolean isPropertyReference();

	/**
	 * Obtain the name of the referenced entity.
	 *
	 * @return The referenced entity name
	 */
	public String getReferencedEntityName();

	/**
	 * Set the name of the
	 * @param referencedEntityName
	 */
	public void setReferencedEntityName(String referencedEntityName);

	public String getReferencedAttributeName();
	public void setReferencedAttributeName(String referencedAttributeName);


	// "resolvable"
	public void resolveReference(AttributeBinding attributeBinding);
	public boolean isReferenceResolved();
	public EntityBinding getReferencedEntityBinding();
	public AttributeBinding getReferencedAttributeBinding();
}