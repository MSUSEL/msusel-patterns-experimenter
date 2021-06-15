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
package org.hibernate.metamodel.domain;

import java.util.Set;

/**
 * Basic contract for any container holding attributes. This allows polymorphic handling of both
 * components and entities in terms of the attributes they hold.
 *
 * @author Steve Ebersole
 */
public interface AttributeContainer extends Type {
	/**
	 * Obtain the name of this container in terms of creating attribute role names.
	 * <p/>
	 * NOTE : A role uniquely names each attribute.  The role name is the name of the attribute prefixed by the "path"
	 * to its container.
	 *
	 * @return The container base name for role construction.
	 */
	public String getRoleBaseName();

	/**
	 * Retrieve an attribute by name.
	 *
	 * @param name The name of the attribute to retrieve.
	 *
	 * @return The attribute matching the given name, or null.
	 */
	public Attribute locateAttribute(String name);

	/**
	 * Retrieve the attributes contained in this container.
	 *
	 * @return The contained attributes
	 */
	public Set<Attribute> attributes();

	public SingularAttribute locateSingularAttribute(String name);
	public SingularAttribute createSingularAttribute(String name);
	public SingularAttribute createVirtualSingularAttribute(String name);

	public SingularAttribute locateComponentAttribute(String name);
	public SingularAttribute createComponentAttribute(String name, Component component);

	public PluralAttribute locatePluralAttribute(String name);

	public PluralAttribute locateBag(String name);
	public PluralAttribute createBag(String name);

	public PluralAttribute locateSet(String name);
	public PluralAttribute createSet(String name);

	public IndexedPluralAttribute locateList(String name);
	public IndexedPluralAttribute createList(String name);

	public IndexedPluralAttribute locateMap(String name);
	public IndexedPluralAttribute createMap(String name);

}
