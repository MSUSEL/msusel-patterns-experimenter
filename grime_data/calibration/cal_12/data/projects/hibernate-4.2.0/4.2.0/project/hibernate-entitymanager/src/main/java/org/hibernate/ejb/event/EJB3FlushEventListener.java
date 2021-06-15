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
package org.hibernate.ejb.event;

import java.util.IdentityHashMap;

import org.hibernate.engine.spi.CascadingAction;
import org.hibernate.event.internal.DefaultFlushEventListener;
import org.hibernate.event.spi.FlushEventListener;

/**
 * In EJB3, it is the create operation that is cascaded to unmanaged entities at flush time (instead of the
 * save-update operation in Hibernate).
 *
 * @author Gavin King
 */
public class EJB3FlushEventListener extends DefaultFlushEventListener implements HibernateEntityManagerEventListener {

	public static final FlushEventListener INSTANCE = new EJB3FlushEventListener();

	protected CascadingAction getCascadingAction() {
		return CascadingAction.PERSIST_ON_FLUSH;
	}

	protected Object getAnything() {
		return new IdentityHashMap( 10 );
	}

}
