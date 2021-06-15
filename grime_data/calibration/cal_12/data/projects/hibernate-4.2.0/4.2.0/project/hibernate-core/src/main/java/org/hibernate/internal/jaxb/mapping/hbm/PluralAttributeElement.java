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
package org.hibernate.internal.jaxb.mapping.hbm;

import java.util.List;

/**
 * Commonality between the various forms of plural attribute (collection) mappings: {@code <bag/>}, {@code <set/>}, etc.
 *
 * @author Steve Ebersole
 */
public interface PluralAttributeElement extends MetaAttributeContainer {
	public String getName();
	public String getAccess();

	public JaxbKeyElement getKey();

	public JaxbElementElement getElement();
	public JaxbCompositeElementElement getCompositeElement();
	public JaxbOneToManyElement getOneToMany();
	public JaxbManyToManyElement getManyToMany();
    public JaxbManyToAnyElement getManyToAny();

	public String getSchema();
	public String getCatalog();
	public String getTable();
	public String getComment();
	public String getCheck();
	public String getSubselect();
	public String getSubselectAttribute();
	public String getWhere();

	public JaxbLoaderElement getLoader();
	public JaxbSqlInsertElement getSqlInsert();
    public JaxbSqlUpdateElement getSqlUpdate();
    public JaxbSqlDeleteElement getSqlDelete();
    public JaxbSqlDeleteAllElement getSqlDeleteAll();

	public List<JaxbSynchronizeElement> getSynchronize();

	public JaxbCacheElement getCache();
	public List<JaxbFilterElement> getFilter();

	public String getCascade();
	public JaxbFetchAttributeWithSubselect getFetch();
	public JaxbLazyAttributeWithExtra getLazy();
	public JaxbOuterJoinAttribute getOuterJoin();

	public String getBatchSize();
	public boolean isInverse();
    public boolean isMutable();
	public boolean isOptimisticLock();

	public String getCollectionType();
    public String getPersister();

// todo : not available on all.  do we need a specific interface for these?
//	public String getSort();
//	public String getOrderBy();
}
