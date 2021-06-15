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
package org.hibernate.collection.internal;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.loader.CollectionAliases;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.type.Type;

/**
 * A persistent wrapper for an XML element
 *
 * @author Gavin King
 *
 *
 * @deprecated To be removed in 5.  Removed as part of removing the notion of DOM entity-mode.  See Jira issues
 * <a href="https://hibernate.onjira.com/browse/HHH-7782">HHH-7782</a> and
 * <a href="https://hibernate.onjira.com/browse/HHH-7783">HHH-7783</a> for more information.
 */
@Deprecated
public class PersistentElementHolder extends AbstractPersistentCollection {
	protected Element element;

	public PersistentElementHolder(SessionImplementor session, Element element) {
		super(session);
		this.element = element;
		setInitialized();
	}

	public Serializable getSnapshot(CollectionPersister persister)
	throws HibernateException {

		final Type elementType = persister.getElementType();
		List elements = element.elements( persister.getElementNodeName() );
		ArrayList snapshot = new ArrayList( elements.size() );
		for ( int i=0; i<elements.size(); i++ ) {
			Element elem = (Element) elements.get(i);
			Object value = elementType.fromXMLNode( elem, persister.getFactory() );
			Object copy = elementType.deepCopy(value , persister.getFactory() );
			snapshot.add(copy);
		}
		return snapshot;

	}

	@Override
	public Collection getOrphans(Serializable snapshot, String entityName)
	throws HibernateException {
		//orphan delete not supported for EntityMode.DOM4J
		return Collections.EMPTY_LIST;
	}

	public PersistentElementHolder(SessionImplementor session, CollectionPersister persister, Serializable key)
	throws HibernateException {
		super(session);
		Element owner = (Element) session.getPersistenceContext().getCollectionOwner(key, persister);
		if (owner==null) throw new AssertionFailure("null owner");
		//element = XMLHelper.generateDom4jElement( persister.getNodeName() );
		final String nodeName = persister.getNodeName();
		if ( ".".equals(nodeName) ) {
			element = owner;
		}
		else {
			element = owner.element( nodeName );
			if (element==null) element = owner.addElement( nodeName );
		}
	}

	public boolean isWrapper(Object collection) {
		return element==collection;
	}

	public boolean equalsSnapshot(CollectionPersister persister) throws HibernateException {
		Type elementType = persister.getElementType();

		ArrayList snapshot = (ArrayList) getSnapshot();
		List elements = element.elements( persister.getElementNodeName() );
		if ( snapshot.size()!= elements.size() ) return false;
		for ( int i=0; i<snapshot.size(); i++ ) {
			Object old = snapshot.get(i);
			Element elem = (Element) elements.get(i);
			Object current = elementType.fromXMLNode( elem, persister.getFactory() );
			if ( elementType.isDirty( old, current, getSession() ) ) return false;
		}
		return true;
	}

	public boolean isSnapshotEmpty(Serializable snapshot) {
		return ( (Collection) snapshot ).isEmpty();
	}

	@Override
    public boolean empty() {
		return !element.elementIterator().hasNext();
	}

	public Object readFrom(ResultSet rs, CollectionPersister persister, CollectionAliases descriptor, Object owner)
	throws HibernateException, SQLException {
		Object object = persister.readElement( rs, owner, descriptor.getSuffixedElementAliases(), getSession() );
		final Type elementType = persister.getElementType();
		Element subelement = element.addElement( persister.getElementNodeName() );
		elementType.setToXMLNode( subelement, object, persister.getFactory() );
		return object;
	}

	public Iterator entries(CollectionPersister persister) {

		final Type elementType = persister.getElementType();
		List elements =  element.elements( persister.getElementNodeName() );
		int length = elements.size();
		List result = new ArrayList(length);
		for ( int i=0; i<length; i++ ) {
			Element elem = (Element) elements.get(i);
			Object object = elementType.fromXMLNode( elem, persister.getFactory() );
			result.add(object);
		}
		return result.iterator();
	}

	public void beforeInitialize(CollectionPersister persister, int anticipatedSize) {}

	@Override
    public boolean isDirectlyAccessible() {
		return true;
	}

	public void initializeFromCache(CollectionPersister persister, Serializable disassembled, Object owner)
	throws HibernateException {

		Type elementType = persister.getElementType();
		Serializable[] cached = (Serializable[]) disassembled;
		for ( int i=0; i<cached.length; i++ ) {
			Object object = elementType.assemble( cached[i], getSession(), owner );
			Element subelement = element.addElement( persister.getElementNodeName() );
			elementType.setToXMLNode( subelement, object, persister.getFactory() );
		}

	}

	public Serializable disassemble(CollectionPersister persister) throws HibernateException {

		Type elementType = persister.getElementType();
		List elements =  element.elements( persister.getElementNodeName() );
		int length = elements.size();
		Serializable[] result = new Serializable[length];
		for ( int i=0; i<length; i++ ) {
			Element elem = (Element) elements.get(i);
			Object object = elementType.fromXMLNode( elem, persister.getFactory() );
			result[i] = elementType.disassemble( object, getSession(), null );
		}
		return result;
	}

	@Override
    public Object getValue() {
		return element;
	}

	public Iterator getDeletes(CollectionPersister persister, boolean indexIsFormula)
	throws HibernateException {

		Type elementType = persister.getElementType();
		ArrayList snapshot = (ArrayList) getSnapshot();
		List elements = element.elements( persister.getElementNodeName() );
		ArrayList result = new ArrayList();
		for ( int i=0; i<snapshot.size(); i++ ) {
			Object old = snapshot.get(i);
			if ( i >= elements.size() ) {
				result.add(old);
			}
			else {
				Element elem = (Element) elements.get(i);
				Object object = elementType.fromXMLNode( elem, persister.getFactory() );
				if ( elementType.isDirty( old, object, getSession() ) ) result.add(old);
			}
		}
		return result.iterator();

	}

	public boolean needsInserting(Object entry, int i, Type elementType)
	throws HibernateException {
		ArrayList snapshot = (ArrayList) getSnapshot();
		return i>=snapshot.size() || elementType.isDirty( snapshot.get(i), entry, getSession() );
	}

	public boolean needsUpdating(Object entry, int i, Type elementType)
	throws HibernateException {
		return false;
	}

	public Object getIndex(Object entry, int i, CollectionPersister persister) {
		throw new UnsupportedOperationException();
	}

	public Object getElement(Object entry) {
		return entry;
	}

	public Object getSnapshotElement(Object entry, int i) {
		throw new UnsupportedOperationException();
	}

	public boolean entryExists(Object entry, int i) {
		return entry!=null;
	}

}
