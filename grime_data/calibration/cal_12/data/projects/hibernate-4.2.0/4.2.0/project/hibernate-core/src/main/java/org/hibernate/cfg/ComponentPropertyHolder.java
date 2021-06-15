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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import org.hibernate.AnnotationException;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

/**
 * Component implementation of property holder
 *
 * @author Emmanuel Bernard
 */
public class ComponentPropertyHolder extends AbstractPropertyHolder {
	//TODO introduce a overrideTable() method for columns held by sec table rather than the hack
	//     joinsPerRealTableName in ClassPropertyHolder
	private Component component;
	private boolean isOrWithinEmbeddedId;

	public String getEntityName() {
		return component.getComponentClassName();
	}

	public void addProperty(Property prop, Ejb3Column[] columns, XClass declaringClass) {
		//Ejb3Column.checkPropertyConsistency( ); //already called earlier
		/*
		 * Check table matches between the component and the columns
		 * if not, change the component table if no properties are set
		 * if a property is set already the core cannot support that
		 */
		if (columns != null) {
			Table table = columns[0].getTable();
			if ( !table.equals( component.getTable() ) ) {
				if ( component.getPropertySpan() == 0 ) {
					component.setTable( table );
				}
				else {
					throw new AnnotationException(
							"A component cannot hold properties split into 2 different tables: "
									+ this.getPath()
					);
				}
			}
		}
		addProperty( prop, declaringClass );
	}

	public Join addJoin(JoinTable joinTableAnn, boolean noDelayInPkColumnCreation) {
		return parent.addJoin( joinTableAnn, noDelayInPkColumnCreation );

	}

	public ComponentPropertyHolder(
			Component component,
			String path,
			PropertyData inferredData,
			PropertyHolder parent,
			Mappings mappings) {
		super( path, parent, inferredData.getPropertyClass(), mappings );
		final XProperty property = inferredData.getProperty();
		setCurrentProperty( property );
		this.component = component;
		this.isOrWithinEmbeddedId =
				parent.isOrWithinEmbeddedId()
				|| ( property != null &&
					( property.isAnnotationPresent( Id.class )
					|| property.isAnnotationPresent( EmbeddedId.class ) ) );
	}

	public String getClassName() {
		return component.getComponentClassName();
	}

	public String getEntityOwnerClassName() {
		return component.getOwner().getClassName();
	}

	public Table getTable() {
		return component.getTable();
	}

	public void addProperty(Property prop, XClass declaringClass) {
		component.addProperty( prop );
	}

	public KeyValue getIdentifier() {
		return component.getOwner().getIdentifier();
	}

	public boolean isOrWithinEmbeddedId() {
		return isOrWithinEmbeddedId;
	}

	public PersistentClass getPersistentClass() {
		return component.getOwner();
	}

	public boolean isComponent() {
		return true;
	}

	public boolean isEntity() {
		return false;
	}

	public void setParentProperty(String parentProperty) {
		component.setParentProperty( parentProperty );
	}

	@Override
	public Column[] getOverriddenColumn(String propertyName) {
		//FIXME this is yukky
		Column[] result = super.getOverriddenColumn( propertyName );
		if ( result == null ) {
			String userPropertyName = extractUserPropertyName( "id", propertyName );
			if ( userPropertyName != null ) result = super.getOverriddenColumn( userPropertyName );
		}
		if ( result == null ) {
			String userPropertyName = extractUserPropertyName( "_identifierMapper", propertyName );
			if ( userPropertyName != null ) result = super.getOverriddenColumn( userPropertyName );
		}
		return result;
	}

	private String extractUserPropertyName(String redundantString, String propertyName) {
		String result = null;
		String className = component.getOwner().getClassName();
		if ( propertyName.startsWith( className )
				&& propertyName.length() > className.length() + 2 + redundantString.length() // .id.
				&& propertyName.substring(
				className.length() + 1, className.length() + 1 + redundantString.length()
		).equals( redundantString )
				) {
			//remove id we might be in a @IdCLass case
			result = className + propertyName.substring( className.length() + 1 + redundantString.length() );
		}
		return result;
	}

	@Override
	public JoinColumn[] getOverriddenJoinColumn(String propertyName) {
		return super.getOverriddenJoinColumn( propertyName );
	}
}
