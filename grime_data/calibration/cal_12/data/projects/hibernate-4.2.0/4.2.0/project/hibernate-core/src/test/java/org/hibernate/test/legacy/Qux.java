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
//$Id: Qux.java 4599 2004-09-26 05:18:27Z oneovthafew $
package org.hibernate.test.legacy;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

public class Qux implements Lifecycle {

	boolean created;
	boolean deleted;
	boolean loaded;
	boolean stored;
	private Long key;
	private String stuff;
	private Set fums;
	private List moreFums;
	private Qux child;
	private Session session;
	private Long childKey;
	private Holder holder;

	private FooProxy foo;

	public Qux() { }

	public Qux(String s) {
		stuff=s;
	}

	public boolean onSave(Session session) throws CallbackException {
		created=true;
		try {
			foo = new Foo();
			session.save(foo);
		}
		catch (Exception e) {
			throw new CallbackException(e);
		}
		foo.setString("child of a qux");
		return NO_VETO;
	}

	public boolean onDelete(Session session) throws CallbackException {
		deleted=true;
		try {
			session.delete(foo);
		}
		catch (Exception e) {
			throw new CallbackException(e);
		}
		//if (child!=null) session.delete(child);
		return NO_VETO;
	}

	public void onLoad(Session session, Serializable id) {
		loaded=true;
		this.session=session;
	}

	public void store() {
	}

	public FooProxy getFoo() {
		return foo;
	}
	public void setFoo(FooProxy foo) {
		this.foo = foo;
	}

	public boolean getCreated() {
		return created;
	}
	private void setCreated(boolean created) {
		this.created = created;
	}

	public boolean getDeleted() {
		return deleted;
	}

	private void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean getLoaded() {
		return loaded;
	}
	private void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public boolean getStored() {
		return stored;
	}
	private void setStored(boolean stored) {
		this.stored = stored;
	}

	public Long getKey() {
		return key;
	}

	private void setKey(long key) {
		this.key = new Long(key);
	}

	public void setTheKey(long key) {
		this.key = new Long(key);
	}

	public String getStuff() {
		return stuff;
	}
	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

	public Set getFums() {
		return fums;
	}

	public void setFums(Set fums) {
		this.fums = fums;
	}

	public List getMoreFums() {
		return moreFums;
	}
	public void setMoreFums(List moreFums) {
		this.moreFums = moreFums;
	}

	public Qux getChild() throws HibernateException, SQLException {
		stored=true;
		this.childKey = child==null ? null : child.getKey();
		if (childKey!=null && child==null) child = (Qux) session.load(Qux.class, childKey);
		return child;
	}

	public void setChild(Qux child) {
		this.child = child;
	}

	private Long getChildKey() {
		return childKey;
	}

	private void setChildKey(Long childKey) {
		this.childKey = childKey;
	}

	public boolean onUpdate(Session s) throws CallbackException {
		return NO_VETO;
	}

	protected void finalize() { }

	public Holder getHolder() {
		return holder;
	}

	public void setHolder(Holder holder) {
		this.holder = holder;
	}

}







