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
package org.hibernate.metamodel.source.binder;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.internal.jaxb.Origin;
import org.hibernate.metamodel.binding.CustomSQL;
import org.hibernate.metamodel.source.LocalBindingContext;

/**
 * Contract describing source of an entity
 *
 * @author Steve Ebersole
 */
public interface EntitySource extends SubclassEntityContainer, AttributeSourceContainer {

	/**
	 * Obtain the origin of this source.
	 *
	 * @return The origin of this source.
	 */
	public Origin getOrigin();

	/**
	 * Obtain the binding context local to this entity source.
	 *
	 * @return The local binding context
	 */
	public LocalBindingContext getLocalBindingContext();

	/**
	 * Obtain the entity name
	 *
	 * @return The entity name
	 */
	public String getEntityName();

	/**
	 * Obtain the name of the entity {@link Class}
	 *
	 * @return THe entity class name
	 */
	public String getClassName();

	/**
	 * Obtain the JPA name of the entity
	 *
	 * @return THe JPA-specific entity name
	 */
	public String getJpaEntityName();

	/**
	 * Obtain the primary table for this entity.
	 *
	 * @return The primary table.
	 */
	public TableSource getPrimaryTable();

	/**
	 * Obtain the secondary tables for this entity
	 *
	 * @return returns an iterator over the secondary tables for this entity
	 */
	public Iterable<TableSource> getSecondaryTables();

	/**
	 * Obtain the name of a custom tuplizer class to be used.
	 *
	 * @return The custom tuplizer class name
	 */
	public String getCustomTuplizerClassName();

	/**
	 * Obtain the name of a custom persister class to be used.
	 *
	 * @return The custom persister class name
	 */
	public String getCustomPersisterClassName();

	/**
	 * Is this entity lazy (proxyable)?
	 *
	 * @return {@code true} indicates the entity is lazy; {@code false} non-lazy.
	 */
	public boolean isLazy();

	/**
	 * For {@link #isLazy() lazy} entities, obtain the interface to use in constructing its proxies.
	 *
	 * @return The proxy interface name
	 */
	public String getProxy();

	/**
	 * Obtain the batch-size to be applied when initializing proxies of this entity.
	 *
	 * @return returns the the batch-size.
	 */
	public int getBatchSize();

	/**
	 * Is the entity abstract?
	 * <p/>
	 * The implication is whether the entity maps to a database table.
	 *
	 * @return {@code true} indicates the entity is abstract; {@code false} non-abstract.
	 */
	public boolean isAbstract();

	/**
	 * Did the source specify dynamic inserts?
	 *
	 * @return {@code true} indicates dynamic inserts will be used; {@code false} otherwise.
	 */
	public boolean isDynamicInsert();

	/**
	 * Did the source specify dynamic updates?
	 *
	 * @return {@code true} indicates dynamic updates will be used; {@code false} otherwise.
	 */
	public boolean isDynamicUpdate();

	/**
	 * Did the source specify to perform selects to decide whether to perform (detached) updates?
	 *
	 * @return {@code true} indicates selects will be done; {@code false} otherwise.
	 */
	public boolean isSelectBeforeUpdate();

	/**
	 * Obtain the name of a named-query that will be used for loading this entity
	 *
	 * @return THe custom loader query name
	 */
	public String getCustomLoaderName();

	/**
	 * Obtain the custom SQL to be used for inserts for this entity
	 *
	 * @return The custom insert SQL
	 */
	public CustomSQL getCustomSqlInsert();

	/**
	 * Obtain the custom SQL to be used for updates for this entity
	 *
	 * @return The custom update SQL
	 */
	public CustomSQL getCustomSqlUpdate();

	/**
	 * Obtain the custom SQL to be used for deletes for this entity
	 *
	 * @return The custom delete SQL
	 */
	public CustomSQL getCustomSqlDelete();

	/**
	 * Obtain any additional table names on which to synchronize (auto flushing) this entity.
	 *
	 * @return Additional synchronized table names.
	 */
	public List<String> getSynchronizedTableNames();

	/**
	 * Obtain the meta-attribute sources associated with this entity.
	 *
	 * @return The meta-attribute sources.
	 */
	public Iterable<MetaAttributeSource> metaAttributes();

	/**
	 * Get the actual discriminator value in case of a single table inheritance
	 *
	 * @return the actual discriminator value in case of a single table inheritance or {@code null} in case there is no
	 *         explicit value or a different inheritance scheme
	 */
	public String getDiscriminatorMatchValue();

	/**
	 * @return returns the source information for constraints defined on the table
	 */
	public Iterable<ConstraintSource> getConstraints();

    /**
     * @return the list of classes (this {@link Entity entity}/{@link MappedSuperclass mapped superclass}, or
     *         {@link EntityListeners entity listeners}) that define JPA callbacks for this entity/mapped superclass.
     */
    List<JpaCallbackClass> getJpaCallbackClasses();
}
