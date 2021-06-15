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
package org.hibernate.secure.internal;

import java.security.Permission;

/**
 * @author Gavin King
 */
public class HibernatePermission extends Permission {
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String DELETE = "delete";
	public static final String READ = "read";
	public static final String ANY = "*";
	
	private final String actions;

	public HibernatePermission(String entityName, String actions) {
		super(entityName);
		this.actions = actions;
	}

	public boolean implies(Permission permission) {
		//TODO!
		return ( "*".equals( getName() ) || getName().equals( permission.getName() ) ) &&
			( "*".equals(actions) || actions.indexOf( permission.getActions() ) >= 0 );
	}

	public boolean equals(Object obj) {
		if ( !(obj instanceof HibernatePermission) ) return false;
		HibernatePermission permission = (HibernatePermission) obj;
		return permission.getName().equals( getName() ) && 
			permission.getActions().equals(actions);
	}

	public int hashCode() {
		return getName().hashCode() * 37 + actions.hashCode();
	}

	public String getActions() {
		return actions;
	}
	
	public String toString() {
		return "HibernatePermission(" + getName() + ':' + actions + ')';
	}

}
