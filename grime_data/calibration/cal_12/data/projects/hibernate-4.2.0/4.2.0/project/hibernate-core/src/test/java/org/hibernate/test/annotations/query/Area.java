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
package org.hibernate.test.annotations.query;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

/**
 * Example of a entity load incl a join fetching of an associated *ToOne entity
 *
 * @author Emmanuel Bernard
 */
@Entity
@NamedNativeQueries({
@NamedNativeQuery(
		name = "night&area", query = "select night.id as nid, night.night_duration, night.night_date, area.id as aid, "
		+ "night.area_id, area.name from Night night, tbl_area area where night.area_id = area.id",
		resultSetMapping = "joinMapping")
		})
@org.hibernate.annotations.NamedNativeQueries({
@org.hibernate.annotations.NamedNativeQuery(
		name = "night&areaCached",
		query = "select night.id as nid, night.night_duration, night.night_date, area.id as aid, "
				+ "night.area_id, area.name from Night night, tbl_area area where night.area_id = area.id",
		resultSetMapping = "joinMapping")
		})
@SqlResultSetMappings(
		@SqlResultSetMapping(name = "joinMapping", entities = {
		@EntityResult(entityClass = org.hibernate.test.annotations.query.Night.class, fields = {
		@FieldResult(name = "id", column = "nid"),
		@FieldResult(name = "duration", column = "night_duration"),
		@FieldResult(name = "date", column = "night_date"),
		@FieldResult(name = "area", column = "area_id")
				}),
		@EntityResult(entityClass = org.hibernate.test.annotations.query.Area.class, fields = {
		@FieldResult(name = "id", column = "aid"),
		@FieldResult(name = "name", column = "name")
				})
				}
		)
)
@Table(name = "tbl_area")
public class Area {
	private Integer id;
	private String name;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
