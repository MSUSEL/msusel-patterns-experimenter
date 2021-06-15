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
package org.hibernate.metamodel.binding;

import java.util.Comparator;

import org.hibernate.metamodel.domain.PluralAttribute;
import org.hibernate.metamodel.relational.TableSpecification;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * @author Steve Ebersole
 */
public interface PluralAttributeBinding extends  AssociationAttributeBinding {
	// todo : really it is the element (and/or index) that can be associative not the collection itself...

	@Override
	public PluralAttribute getAttribute();

	public CollectionKey getCollectionKey();

	public AbstractCollectionElement getCollectionElement();

	public TableSpecification getCollectionTable();

	public boolean isMutable();

	public Caching getCaching();

	public Class<? extends CollectionPersister> getCollectionPersisterClass();

	public String getCustomLoaderName();

	public CustomSQL getCustomSqlInsert();

	public CustomSQL getCustomSqlUpdate();

	public CustomSQL getCustomSqlDelete();

	public CustomSQL getCustomSqlDeleteAll();

	public boolean isOrphanDelete();

	String getWhere();

	boolean isSorted();

	Comparator getComparator();

	int getBatchSize();

	java.util.Map getFilterMap();

	boolean isInverse();

	String getOrderBy();
}
