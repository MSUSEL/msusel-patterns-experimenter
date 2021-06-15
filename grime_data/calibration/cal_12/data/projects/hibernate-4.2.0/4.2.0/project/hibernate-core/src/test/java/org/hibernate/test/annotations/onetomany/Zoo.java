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
package org.hibernate.test.annotations.onetomany;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * Entity used to test {@code NULL} values ordering in SQL {@code ORDER BY} clause.
 * Implementation note: By default H2 places {@code NULL} values first.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
public class Zoo implements Serializable {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany
	@JoinColumn(name = "zoo_id")
	@org.hibernate.annotations.OrderBy(clause = "name asc nulls last") // By default H2 places NULL values first.
	private Set<Tiger> tigers = new HashSet<Tiger>();

	@OneToMany
	@JoinColumn(name = "zoo_id")
	@javax.persistence.OrderBy("name asc nulls last") // According to JPA specification this is illegal, but works in Hibernate.
	private Set<Monkey> monkeys = new HashSet<Monkey>();

	@OneToMany
	@JoinColumn(name = "zoo_id")
	@javax.persistence.OrderBy("lastName desc nulls last, firstName asc nulls LaSt") // Sorting by multiple columns.
	private Set<Visitor> visitors = new HashSet<Visitor>();

	public Zoo() {
	}

	public Zoo(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( ! ( o instanceof Zoo ) ) return false;

		Zoo zoo = (Zoo) o;

		if ( id != null ? !id.equals( zoo.id ) : zoo.id != null ) return false;
		if ( name != null ? !name.equals( zoo.name ) : zoo.name != null ) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + ( name != null ? name.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		return "Zoo(id = " + id + ", name = " + name + ")";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Tiger> getTigers() {
		return tigers;
	}

	public void setTigers(Set<Tiger> tigers) {
		this.tigers = tigers;
	}

	public Set<Monkey> getMonkeys() {
		return monkeys;
	}

	public void setMonkeys(Set<Monkey> monkeys) {
		this.monkeys = monkeys;
	}

	public Set<Visitor> getVisitors() {
		return visitors;
	}

	public void setVisitors(Set<Visitor> visitors) {
		this.visitors = visitors;
	}
}
