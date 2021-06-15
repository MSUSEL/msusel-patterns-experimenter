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
package org.hibernate.test.propertyref.cachedcollections;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Steve Ebersole
 */
@SuppressWarnings({"UnusedDeclaration"})
public class ManagedObject {
	private Long moid;
	private int version;
	private String name = "parent";
	private String displayName = "";
	private Set<String> members = new HashSet<String>();

	public ManagedObject() {
	}

	public ManagedObject(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	public Long getMoid() {
		return this.moid;
	}

	private void setMoid(Long moid) {
		this.moid = moid;
	}

	public int getVersion() {
		return this.version;
	}

	private void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String dName) {
		this.displayName = dName;
	}

	public Set<String> getMembers() {
		return this.members;
	}

	public void setMembers(Set<String> members1) {
		this.members = members1;
	}

}


