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
//$Id: Node.java 10759 2006-11-08 00:00:53Z steve.ebersole@jboss.com $
package org.hibernate.test.nonflushedchanges;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Gavin King, Gail Badner (adapted this from "ops" tests version)
 */
public class Node implements Serializable {

	private String name;
	private String description;
	private Date created;
	private Node parent;
	private Set children = new HashSet();
	private Set cascadingChildren = new HashSet();

	public Node() {
	}

	public Node(String name) {
		this.name = name;
		created = generateCurrentDate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Set getChildren() {
		return children;
	}

	public void setChildren(Set children) {
		this.children = children;
	}

	public Node addChild(Node child) {
		children.add( child );
		child.setParent( this );
		return this;
	}

	public Set getCascadingChildren() {
		return cascadingChildren;
	}

	public void setCascadingChildren(Set cascadingChildren) {
		this.cascadingChildren = cascadingChildren;
	}

	private Date generateCurrentDate() {
		// Note : done as java.sql.Date mainly to work around issue with
		// MySQL and its lack of milli-second precision on its DATETIME
		// and TIMESTAMP datatypes.
		return new Date( new java.util.Date().getTime() );
	}
}
