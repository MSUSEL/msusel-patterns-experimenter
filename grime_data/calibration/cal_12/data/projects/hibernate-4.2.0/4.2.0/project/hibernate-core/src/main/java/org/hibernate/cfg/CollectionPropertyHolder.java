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
package org.hibernate.cfg;
import javax.persistence.JoinTable;

import org.hibernate.AssertionFailure;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

/**
 * @author Emmanuel Bernard
 */
public class CollectionPropertyHolder extends AbstractPropertyHolder {
	Collection collection;

	public CollectionPropertyHolder(
			Collection collection,
			String path,
			XClass clazzToProcess,
			XProperty property,
			PropertyHolder parentPropertyHolder,
			Mappings mappings) {
		super( path, parentPropertyHolder, clazzToProcess, mappings );
		this.collection = collection;
		setCurrentProperty( property );
	}

	public String getClassName() {
		throw new AssertionFailure( "Collection property holder does not have a class name" );
	}

	public String getEntityOwnerClassName() {
		return null;
	}

	public Table getTable() {
		return collection.getCollectionTable();
	}

	public void addProperty(Property prop, XClass declaringClass) {
		throw new AssertionFailure( "Cannot add property to a collection" );
	}

	public KeyValue getIdentifier() {
		throw new AssertionFailure( "Identifier collection not yet managed" );
	}

	public boolean isOrWithinEmbeddedId() {
		return false;
	}

	public PersistentClass getPersistentClass() {
		return collection.getOwner();
	}

	public boolean isComponent() {
		return false;
	}

	public boolean isEntity() {
		return false;
	}

	public String getEntityName() {
		return collection.getOwner().getEntityName();
	}

	public void addProperty(Property prop, Ejb3Column[] columns, XClass declaringClass) {
		//Ejb3Column.checkPropertyConsistency( ); //already called earlier
		throw new AssertionFailure( "addProperty to a join table of a collection: does it make sense?" );
	}

	public Join addJoin(JoinTable joinTableAnn, boolean noDelayInPkColumnCreation) {
		throw new AssertionFailure( "Add a <join> in a second pass" );
	}
}
