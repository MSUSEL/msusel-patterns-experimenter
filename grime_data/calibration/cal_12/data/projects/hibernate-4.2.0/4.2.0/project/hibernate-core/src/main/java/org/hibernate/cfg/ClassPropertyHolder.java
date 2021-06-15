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
import java.util.HashMap;
import java.util.Map;
import javax.persistence.JoinTable;

import org.hibernate.annotations.common.AssertionFailure;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.cfg.annotations.EntityBinder;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.Join;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.MappedSuperclass;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;

/**
 * @author Emmanuel Bernard
 */
public class ClassPropertyHolder extends AbstractPropertyHolder {
	private PersistentClass persistentClass;
	private Map<String, Join> joins;
	private transient Map<String, Join> joinsPerRealTableName;
	private EntityBinder entityBinder;
	private final Map<XClass, InheritanceState> inheritanceStatePerClass;

	public ClassPropertyHolder(
			PersistentClass persistentClass,
			XClass clazzToProcess,
			Map<String, Join> joins,
			Mappings mappings,
			Map<XClass, InheritanceState> inheritanceStatePerClass) {
		super( persistentClass.getEntityName(), null, clazzToProcess, mappings );
		this.persistentClass = persistentClass;
		this.joins = joins;
		this.inheritanceStatePerClass = inheritanceStatePerClass;
	}

	public ClassPropertyHolder(
			PersistentClass persistentClass,
			XClass clazzToProcess,
			EntityBinder entityBinder,
			Mappings mappings,
			Map<XClass, InheritanceState> inheritanceStatePerClass) {
		this( persistentClass, clazzToProcess, entityBinder.getSecondaryTables(), mappings, inheritanceStatePerClass );
		this.entityBinder = entityBinder;
	}

	public String getEntityName() {
		return persistentClass.getEntityName();
	}

	public void addProperty(Property prop, Ejb3Column[] columns, XClass declaringClass) {
		//Ejb3Column.checkPropertyConsistency( ); //already called earlier
		if ( columns != null && columns[0].isSecondary() ) {
			//TODO move the getJoin() code here?
			final Join join = columns[0].getJoin();
			addPropertyToJoin( prop, declaringClass, join );
		}
		else {
			addProperty( prop, declaringClass );
		}
	}

	public void addProperty(Property prop, XClass declaringClass) {
		if ( prop.getValue() instanceof Component ) {
			//TODO handle quote and non quote table comparison
			String tableName = prop.getValue().getTable().getName();
			if ( getJoinsPerRealTableName().containsKey( tableName ) ) {
				final Join join = getJoinsPerRealTableName().get( tableName );
				addPropertyToJoin( prop, declaringClass, join );
			}
			else {
				addPropertyToPersistentClass( prop, declaringClass );
			}
		}
		else {
			addPropertyToPersistentClass( prop, declaringClass );
		}
	}

	public Join addJoin(JoinTable joinTableAnn, boolean noDelayInPkColumnCreation) {
		Join join = entityBinder.addJoin( joinTableAnn, this, noDelayInPkColumnCreation );
		this.joins = entityBinder.getSecondaryTables();
		return join;
	}

	private void addPropertyToPersistentClass(Property prop, XClass declaringClass) {
		if ( declaringClass != null ) {
			final InheritanceState inheritanceState = inheritanceStatePerClass.get( declaringClass );
			if ( inheritanceState == null ) {
				throw new AssertionFailure(
						"Declaring class is not found in the inheritance state hierarchy: " + declaringClass
				);
			}
			if ( inheritanceState.isEmbeddableSuperclass() ) {
				persistentClass.addMappedsuperclassProperty(prop);
				addPropertyToMappedSuperclass( prop, declaringClass );
			}
			else {
				persistentClass.addProperty( prop );
			}
		}
		else {
			persistentClass.addProperty( prop );
		}
	}

	private void addPropertyToMappedSuperclass(Property prop, XClass declaringClass) {
		final Mappings mappings = getMappings();
		final Class type = mappings.getReflectionManager().toClass( declaringClass );
		MappedSuperclass superclass = mappings.getMappedSuperclass( type );
		superclass.addDeclaredProperty( prop );
	}

	private void addPropertyToJoin(Property prop, XClass declaringClass, Join join) {
		if ( declaringClass != null ) {
			final InheritanceState inheritanceState = inheritanceStatePerClass.get( declaringClass );
			if ( inheritanceState == null ) {
				throw new AssertionFailure(
						"Declaring class is not found in the inheritance state hierarchy: " + declaringClass
				);
			}
			if ( inheritanceState.isEmbeddableSuperclass() ) {
				join.addMappedsuperclassProperty(prop);
				addPropertyToMappedSuperclass( prop, declaringClass );
			}
			else {
				join.addProperty( prop );
			}
		}
		else {
			join.addProperty( prop );
		}
	}

	/**
	 * Needed for proper compliance with naming strategy, the property table
	 * can be overriden if the properties are part of secondary tables
	 */
	private Map<String, Join> getJoinsPerRealTableName() {
		if ( joinsPerRealTableName == null ) {
			joinsPerRealTableName = new HashMap<String, Join>( joins.size() );
			for (Join join : joins.values()) {
				joinsPerRealTableName.put( join.getTable().getName(), join );
			}
		}
		return joinsPerRealTableName;
	}

	public String getClassName() {
		return persistentClass.getClassName();
	}

	public String getEntityOwnerClassName() {
		return getClassName();
	}

	public Table getTable() {
		return persistentClass.getTable();
	}

	public boolean isComponent() {
		return false;
	}

	public boolean isEntity() {
		return true;
	}

	public PersistentClass getPersistentClass() {
		return persistentClass;
	}

	public KeyValue getIdentifier() {
		return persistentClass.getIdentifier();
	}

	public boolean isOrWithinEmbeddedId() {
		return false;
	}
}
