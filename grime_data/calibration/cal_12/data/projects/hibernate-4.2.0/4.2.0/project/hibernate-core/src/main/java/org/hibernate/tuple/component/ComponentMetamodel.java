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
package org.hibernate.tuple.component;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.Property;
import org.hibernate.tuple.PropertyFactory;
import org.hibernate.tuple.StandardProperty;

/**
 * Centralizes metamodel information about a component.
 *
 * @author Steve Ebersole
 */
public class ComponentMetamodel implements Serializable {

	// TODO : will need reference to session factory to fully complete HHH-1907

//	private final SessionFactoryImplementor sessionFactory;
	private final String role;
	private final boolean isKey;
	private final StandardProperty[] properties;

	private final EntityMode entityMode;
	private final ComponentTuplizer componentTuplizer;

	// cached for efficiency...
	private final int propertySpan;
	private final Map propertyIndexes = new HashMap();

//	public ComponentMetamodel(Component component, SessionFactoryImplementor sessionFactory) {
	public ComponentMetamodel(Component component) {
//		this.sessionFactory = sessionFactory;
		this.role = component.getRoleName();
		this.isKey = component.isKey();
		propertySpan = component.getPropertySpan();
		properties = new StandardProperty[propertySpan];
		Iterator itr = component.getPropertyIterator();
		int i = 0;
		while ( itr.hasNext() ) {
			Property property = ( Property ) itr.next();
			properties[i] = PropertyFactory.buildStandardProperty( property, false );
			propertyIndexes.put( property.getName(), i );
			i++;
		}

		entityMode = component.hasPojoRepresentation() ? EntityMode.POJO : EntityMode.MAP;

		// todo : move this to SF per HHH-3517; also see HHH-1907 and ComponentMetamodel
		final ComponentTuplizerFactory componentTuplizerFactory = new ComponentTuplizerFactory();
		final String tuplizerClassName = component.getTuplizerImplClassName( entityMode );
		this.componentTuplizer = tuplizerClassName == null ? componentTuplizerFactory.constructDefaultTuplizer(
				entityMode,
				component
		) : componentTuplizerFactory.constructTuplizer( tuplizerClassName, component );
	}

	public boolean isKey() {
		return isKey;
	}

	public int getPropertySpan() {
		return propertySpan;
	}

	public StandardProperty[] getProperties() {
		return properties;
	}

	public StandardProperty getProperty(int index) {
		if ( index < 0 || index >= propertySpan ) {
			throw new IllegalArgumentException( "illegal index value for component property access [request=" + index + ", span=" + propertySpan + "]" );
		}
		return properties[index];
	}

	public int getPropertyIndex(String propertyName) {
		Integer index = ( Integer ) propertyIndexes.get( propertyName );
		if ( index == null ) {
			throw new HibernateException( "component does not contain such a property [" + propertyName + "]" );
		}
		return index.intValue();
	}

	public StandardProperty getProperty(String propertyName) {
		return getProperty( getPropertyIndex( propertyName ) );
	}

	public EntityMode getEntityMode() {
		return entityMode;
	}

	public ComponentTuplizer getComponentTuplizer() {
		return componentTuplizer;
	}

}
