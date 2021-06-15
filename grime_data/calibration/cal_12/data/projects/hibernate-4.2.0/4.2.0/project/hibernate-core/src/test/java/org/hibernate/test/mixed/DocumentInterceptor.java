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
//$Id: DocumentInterceptor.java 8670 2005-11-25 17:36:29Z epbernard $
package org.hibernate.test.mixed;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * @author Gavin King
 */
public class DocumentInterceptor implements Interceptor {


	public boolean onLoad(
			Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types
	) throws CallbackException {
		return false;
	}

	public boolean onFlushDirty(
			Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types
	) throws CallbackException {
		if ( entity instanceof Document ) {
			currentState[3] = Calendar.getInstance();
			return true;
		}
		else {
			return false;
		}
	}

	public boolean onSave(
			Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types
	) throws CallbackException {
		if ( entity instanceof Document ) {
			state[4] = state[3] = Calendar.getInstance();
			return true;
		}
		else {
			return false;
		}
	}

	public void onDelete(
			Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types
	) throws CallbackException {

	}

	public void preFlush(Iterator entities) throws CallbackException {

	}

	public void postFlush(Iterator entities) throws CallbackException {

	}

	public Boolean isTransient(Object entity) {
		return null;
	}

	public int[] findDirty(
			Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types
	) {
		return null;
	}

	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
		return null;
	}

	public String getEntityName(Object object) throws CallbackException {
		return null;
	}

	public Object getEntity(String entityName, Serializable id)
			throws CallbackException {
		return null;
	}

	public void afterTransactionBegin(Transaction tx) {
	}

	public void afterTransactionCompletion(Transaction tx) {
	}

	public void beforeTransactionCompletion(Transaction tx) {
	}

	public String onPrepareStatement(String sql) {
		return sql;
	}

	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
	}

	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
	}

	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
	}

}
