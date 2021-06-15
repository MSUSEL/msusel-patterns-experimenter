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
package org.hibernate.loader.criteria;

import java.io.Serializable;

import org.hibernate.hql.internal.ast.util.SessionFactoryHelper;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.PropertyMapping;
import org.hibernate.type.Type;

/**
 * @author David Mansfield
 */

class ScalarCollectionCriteriaInfoProvider implements CriteriaInfoProvider {
    String role;
    QueryableCollection persister;
    SessionFactoryHelper helper;

    ScalarCollectionCriteriaInfoProvider(SessionFactoryHelper helper, String role) {
	this.role = role;
	this.helper = helper;
	this.persister = helper.requireQueryableCollection(role);
    }

    public String getName() {
	return role;
    }

    public Serializable[] getSpaces() {
	return persister.getCollectionSpaces();
    }

    public PropertyMapping getPropertyMapping() {
	return helper.getCollectionPropertyMapping(role);
    }

    public Type getType(String relativePath) {
	//not sure what things are going to be passed here, how about 'id', maybe 'index' or 'key' or 'elements' ???
	// todo: wtf!
	return getPropertyMapping().toType(relativePath);
    }

}
