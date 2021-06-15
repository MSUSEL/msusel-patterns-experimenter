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
package org.hibernate.type;

import java.util.Map;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.entity.Joinable;

/**
 * A type that represents some kind of association between entities.
 * @see org.hibernate.engine.internal.Cascade
 * @author Gavin King
 */
public interface AssociationType extends Type {

	/**
	 * Get the foreign key directionality of this association
	 */
	public ForeignKeyDirection getForeignKeyDirection();

	//TODO: move these to a new JoinableType abstract class,
	//extended by EntityType and PersistentCollectionType:

	/**
	 * Is the primary key of the owning entity table
	 * to be used in the join?
	 */
	public boolean useLHSPrimaryKey();
	/**
	 * Get the name of a property in the owning entity 
	 * that provides the join key (null if the identifier)
	 */
	public String getLHSPropertyName();
	
	/**
	 * The name of a unique property of the associated entity 
	 * that provides the join key (null if the identifier of
	 * an entity, or key of a collection)
	 */
	public String getRHSUniqueKeyPropertyName();

	/**
	 * Get the "persister" for this association - a class or
	 * collection persister
	 */
	public Joinable getAssociatedJoinable(SessionFactoryImplementor factory) throws MappingException;
	
	/**
	 * Get the entity name of the associated entity
	 */
	public String getAssociatedEntityName(SessionFactoryImplementor factory) throws MappingException;
	
	/**
	 * Get the "filtering" SQL fragment that is applied in the 
	 * SQL on clause, in addition to the usual join condition
	 */	
	public String getOnCondition(String alias, SessionFactoryImplementor factory, Map enabledFilters) 
	throws MappingException;
	
	/**
	 * Do we dirty check this association, even when there are
	 * no columns to be updated?
	 */
	public abstract boolean isAlwaysDirtyChecked();

	/**
	 * @deprecated To be removed in 5.  Removed as part of removing the notion of DOM entity-mode.
	 * See Jira issue: <a href="https://hibernate.onjira.com/browse/HHH-7771">HHH-7771</a>
	 */
	@Deprecated
	public boolean isEmbeddedInXML();
}






