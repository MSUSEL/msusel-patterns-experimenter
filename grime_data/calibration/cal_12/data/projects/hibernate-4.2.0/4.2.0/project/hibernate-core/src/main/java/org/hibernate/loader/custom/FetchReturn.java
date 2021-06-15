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
package org.hibernate.loader.custom;
import org.hibernate.LockMode;

/**
 * Represents a return which names a fetched association.
 *
 * @author Steve Ebersole
 */
public abstract class FetchReturn extends NonScalarReturn {
	private final NonScalarReturn owner;
	private final String ownerProperty;

	/**
	 * Creates a return descriptor for an association fetch.
	 *
	 * @param owner The return descriptor for the owner of the fetch
	 * @param ownerProperty The name of the property represernting the association being fetched
	 * @param alias The alias for the fetch
	 * @param lockMode The lock mode to apply to the fetched association.
	 */
	public FetchReturn(
			NonScalarReturn owner,
			String ownerProperty,
			String alias,
			LockMode lockMode) {
		super( alias, lockMode );
		this.owner = owner;
		this.ownerProperty = ownerProperty;
	}

	/**
	 * Retrieves the return descriptor for the owner of this fetch.
	 *
	 * @return The owner
	 */
	public NonScalarReturn getOwner() {
		return owner;
	}

	/**
	 * The name of the property on the owner which represents this association.
	 *
	 * @return The property name.
	 */
	public String getOwnerProperty() {
		return ownerProperty;
	}
}
