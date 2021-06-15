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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import org.hibernate.AssertionFailure;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.loader.CollectionAliases;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.type.Type;
import org.hibernate.type.XmlRepresentableType;

/**
 * A persistent wrapper for an XML element
 *
 * @author Gavin King
 *
 * @deprecated To be removed in 5.  Removed as part of removing the notion of DOM entity-mode.  See Jira issues
 * <a href="https://hibernate.onjira.com/browse/HHH-7782">HHH-7782</a> and
 * <a href="https://hibernate.onjira.com/browse/HHH-7783">HHH-7783</a> for more information.
 */
@Deprecated
public abstract class PersistentIndexedElementHolder extends AbstractPersistentCollection {
	protected Element element;
	
	public PersistentIndexedElementHolder(SessionImplementor session, Element element) {
		super(session);
		this.element = element;
		setInitialized();
	}
	
	public static final class IndexedValue {
		String index;
		Object value;
		IndexedValue(String index, Object value) {
			this.index = index;
			this.value = value;
		}
	}
	
	protected static String getIndex(Element element, String indexNodeName, int i) {
		if (indexNodeName!=null) {
			return element.attributeValue(indexNodeName);
		}
		else {
			return Integer.toString(i);
		}
	}
	
	protected static void setIndex(Element element, String indexNodeName, String index) {
		if (indexNodeName!=null) element.addAttribute(indexNodeName, index);
	}
	
	protected static String getIndexAttributeName(CollectionPersister persister) {
		String node = persister.getIndexNodeName();
		return node==null ? null : node.substring(1);
	}
	
	public Serializable getSnapshot(CollectionPersister persister) 
	throws HibernateException {
		
		final Type elementType = persister.getElementType();
		String indexNode = getIndexAttributeName(persister);		
		List elements = element.elements( persister.getElementNodeName() );
		HashMap snapshot = new HashMap( elements.size() );
		for ( int i=0; i<elements.size(); i++ ) {
			Element elem = (Element) elements.get(i);
			Object value = elementType.fromXMLNode( elem, persister.getFactory() );
			Object copy = elementType.deepCopy( value, persister.getFactory() );
			snapshot.put( getIndex(elem, indexNode, i), copy );
		}
		return snapshot;
		
	}

	public Collection getOrphans(Serializable snapshot, String entityName) 
	throws HibernateException {
		//orphan delete not supported for EntityMode.DOM4J
		return Collections.EMPTY_LIST;
	}

	public PersistentIndexedElementHolder(SessionImplementor session, CollectionPersister persister, Serializable key) 
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
		String indexNode = getIndexAttributeName(persister);		
		HashMap snapshot = (HashMap) getSnapshot();
		List elements = element.elements( persister.getElementNodeName() );
		if ( snapshot.size()!= elements.size() ) return false;
		for ( int i=0; i<snapshot.size(); i++ ) {
			Element elem = (Element) elements.get(i);
			Object old = snapshot.get( getIndex(elem, indexNode, i) );
			Object current = elementType.fromXMLNode( elem, persister.getFactory() );
			if ( elementType.isDirty( old, current, getSession() ) ) return false;
		}
		return true;
	}

	public boolean isSnapshotEmpty(Serializable snapshot) {
		return ( (HashMap) snapshot ).isEmpty();
	}
	
	public boolean empty() {
		return !element.elementIterator().hasNext();
	}

	public Object readFrom(ResultSet rs, CollectionPersister persister, CollectionAliases descriptor, Object owner)
	throws HibernateException, SQLException {
		Object object = persister.readElement( rs, owner, descriptor.getSuffixedElementAliases(), getSession() );
		final Type elementType = persister.getElementType();
		final SessionFactoryImplementor factory = persister.getFactory();
		String indexNode = getIndexAttributeName(persister);

		Element elem = element.addElement( persister.getElementNodeName() );
		elementType.setToXMLNode( elem, object, factory ); 
		
		final Type indexType = persister.getIndexType();
		final Object indexValue = persister.readIndex( rs, descriptor.getSuffixedIndexAliases(), getSession() );
		final String index = ( (XmlRepresentableType) indexType ).toXMLString( indexValue, factory );
		setIndex(elem, indexNode, index);
		return object;
	}

	public Iterator entries(CollectionPersister persister) {
		
		final Type elementType = persister.getElementType();
		String indexNode = getIndexAttributeName(persister);
		List elements =  element.elements( persister.getElementNodeName() );
		int length = elements.size();
		List result = new ArrayList(length);
		for ( int i=0; i<length; i++ ) {
			Element elem = (Element) elements.get(i);
			Object object = elementType.fromXMLNode( elem, persister.getFactory() );
			result.add( new IndexedValue( getIndex(elem, indexNode, i), object ) );
		}
		return result.iterator();
	}

	public void beforeInitialize(CollectionPersister persister, int anticipatedSize) {}

	public boolean isDirectlyAccessible() {
		return true;
	}

	public Object getValue() {
		return element;
	}

	public Iterator getDeletes(CollectionPersister persister, boolean indexIsFormula) 
	throws HibernateException {
		
		final Type indexType = persister.getIndexType();
		HashMap snapshot = (HashMap) getSnapshot();
		HashMap deletes = (HashMap) snapshot.clone();
		deletes.keySet().removeAll( ( (HashMap) getSnapshot(persister) ).keySet() );
		ArrayList deleteList = new ArrayList( deletes.size() );
		for ( Object o : deletes.entrySet() ) {
			Map.Entry me = (Map.Entry) o;
			final Object object = indexIsFormula ?
					me.getValue() :
					( (XmlRepresentableType) indexType ).fromXMLString( (String) me.getKey(), persister.getFactory() );
			if ( object != null ) {
				deleteList.add( object );
			}
		}
		
		return deleteList.iterator();
		
	}

	public boolean needsInserting(Object entry, int i, Type elementType) 
	throws HibernateException {
		HashMap snapshot = (HashMap) getSnapshot();
		IndexedValue iv = (IndexedValue) entry;
		return iv.value!=null && snapshot.get( iv.index )==null;
	}

	public boolean needsUpdating(Object entry, int i, Type elementType) 
	throws HibernateException {
		HashMap snapshot = (HashMap) getSnapshot();
		IndexedValue iv = (IndexedValue) entry;
		Object old = snapshot.get( iv.index );
		return old!=null && elementType.isDirty( old, iv.value, getSession() );
	}

	public Object getIndex(Object entry, int i, CollectionPersister persister) {
		String index = ( (IndexedValue) entry ).index;
		final Type indexType = persister.getIndexType();
		return ( (XmlRepresentableType) indexType ).fromXMLString( index, persister.getFactory() );
	}

	public Object getElement(Object entry) {
		return ( (IndexedValue) entry ).value;
	}

	public Object getSnapshotElement(Object entry, int i) {
		return ( (HashMap) getSnapshot() ).get( ( (IndexedValue) entry ).index );
	}

	public boolean entryExists(Object entry, int i) {
		return entry!=null;
	}

}
