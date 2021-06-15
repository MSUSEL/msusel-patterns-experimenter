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
//$Id$
package org.hibernate.ejb.test.emops.cascade;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class A {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int id;

	private String name;

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "a", cascade = {CascadeType.PERSIST} )
	private Set<B1> b1List;

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "a", cascade = {CascadeType.PERSIST} )
	private Set<B2> b2List;

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "a", cascade = {CascadeType.PERSIST} )
	private Set<B3> b3List;

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "a", cascade = {CascadeType.PERSIST} )
	private Set<B4> b4List;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<B1> getB1List() {
		if ( b1List == null )
			b1List = new HashSet<B1>();
		return b1List;
	}

	public void setB1List(Set<B1> list) {
		b1List = list;
	}

	public Set<B2> getB2List() {
		if ( b2List == null )
			b2List = new HashSet<B2>();
		return b2List;
	}

	public void setB2List(Set<B2> list) {
		b2List = list;
	}

	public Set<B3> getB3List() {
		return b3List;
	}

	public void setB3List(Set<B3> list) {
		if ( b3List == null )
			b3List = new HashSet<B3>();
		b3List = list;
	}

	public Set<B4> getB4List() {
		return b4List;
	}

	public void setB4List(Set<B4> list) {
		if ( b4List == null )
			b4List = new HashSet<B4>();
		b4List = list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
