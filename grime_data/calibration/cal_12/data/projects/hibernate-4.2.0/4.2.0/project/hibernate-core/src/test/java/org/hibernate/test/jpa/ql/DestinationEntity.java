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
package org.hibernate.test.jpa.ql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

/**
 * @author Janario Oliveira
 */
@Entity
@Table(name = "destination_entity")
@NamedNativeQueries({
		@NamedNativeQuery(name = "DestinationEntity.insertSelect", query = "insert into destination_entity(id, from_id, fullNameFrom) "
				+ " select fe.id, fe.id, fe.name||fe.lastName from from_entity fe where fe.id in (:ids)"),
		@NamedNativeQuery(name = "DestinationEntity.insert", query = "insert into destination_entity(id, from_id, fullNameFrom) "
				+ "values (:generatedId, :fromId, :fullName)"),
		@NamedNativeQuery(name = "DestinationEntity.update", query = "update destination_entity set from_id=:idFrom, fullNameFrom=:fullName"
				+ " where id in (:ids)"),
		@NamedNativeQuery(name = "DestinationEntity.delete", query = "delete from destination_entity where id in (:ids)"),
		@NamedNativeQuery(name = "DestinationEntity.selectIds", query = "select id, from_id, fullNameFrom from destination_entity where id in (:ids)") })
public class DestinationEntity {

	@Id
	@GeneratedValue
	Integer id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "from_id")
	FromEntity from;
	@Column(nullable = false)
	String fullNameFrom;

	public DestinationEntity() {
	}

	public DestinationEntity(FromEntity from, String fullNameFrom) {
		this.from = from;
		this.fullNameFrom = fullNameFrom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( from == null ) ? 0 : from.hashCode() );
		result = prime * result + ( ( fullNameFrom == null ) ? 0 : fullNameFrom.hashCode() );
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		DestinationEntity other = (DestinationEntity) obj;
		if ( from == null ) {
			if ( other.from != null )
				return false;
		}
		else if ( !from.equals( other.from ) )
			return false;
		if ( fullNameFrom == null ) {
			if ( other.fullNameFrom != null )
				return false;
		}
		else if ( !fullNameFrom.equals( other.fullNameFrom ) )
			return false;
		return true;
	}

}
