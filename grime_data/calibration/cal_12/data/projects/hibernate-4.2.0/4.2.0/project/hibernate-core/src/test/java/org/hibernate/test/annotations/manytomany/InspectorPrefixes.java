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
package org.hibernate.test.annotations.manytomany;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * @author Emmanuel Bernard
 */
@Entity
@PrimaryKeyJoinColumn(name = "inspector_id")
class InspectorPrefixes extends Inspector {
	@Column(name = "prefixes", nullable = false)
	private String prefixes;
	@ManyToMany()
	@JoinTable(name = "deserted_area",
			joinColumns = @JoinColumn(name = "inspector_name", referencedColumnName = "name"),
			inverseJoinColumns = @JoinColumn(name = "area_id", referencedColumnName = "id"))
	private List<Zone> desertedAreas = new ArrayList<Zone>();

	@ManyToMany()
	@JoinTable(name = "inspector_prefixes_areas",
			joinColumns = @JoinColumn(name = "inspector_id", referencedColumnName = "inspector_id"),
			inverseJoinColumns = @JoinColumn(name = "area_id", referencedColumnName = "id"))
	private List<Zone> areas = new ArrayList<Zone>();

	InspectorPrefixes() {
	}

	InspectorPrefixes(String prefixes) {
		this.prefixes = prefixes;
	}

	public String getPrefixes() {
		return this.prefixes;
	}

	public List<Zone> getAreas() {
		return areas;
	}

	public List<Zone> getDesertedAreas() {
		return desertedAreas;
	}
}
