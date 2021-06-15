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
package org.hibernate.envers.test.integration.customtype;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

/**
 * Entity encapsulating {@link Object} property which concrete type may change during subsequent updates.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
@Entity
@Audited
public class ObjectUserTypeEntity implements Serializable {
	@Id
	@GeneratedValue
	private int id;

	private String buildInType;

	@Type(type = "org.hibernate.envers.test.integration.customtype.ObjectUserType")
	@Columns(columns = { @Column(name = "OBJ_TYPE"), @Column(name = "OBJ_VALUE") })
	private Object userType;

	public ObjectUserTypeEntity() {
	}

	public ObjectUserTypeEntity(String buildInType, Object userType) {
		this.buildInType = buildInType;
		this.userType = userType;
	}

	public ObjectUserTypeEntity(int id, String buildInType, Object userType) {
		this.id = id;
		this.buildInType = buildInType;
		this.userType = userType;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( ! ( o instanceof ObjectUserTypeEntity ) ) return false;

		ObjectUserTypeEntity that = (ObjectUserTypeEntity) o;

		if ( id != that.id ) return false;
		if ( buildInType != null ? !buildInType.equals( that.buildInType ) : that.buildInType != null ) return false;
		if ( userType != null ? !userType.equals( that.userType ) : that.userType != null ) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + ( buildInType != null ? buildInType.hashCode() : 0 );
		result = 31 * result + ( userType != null ? userType.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		return "ObjectUserTypeEntity(id = " + id + ", buildInType = " + buildInType + ", userType = " + userType + ")";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBuildInType() {
		return buildInType;
	}

	public void setBuildInType(String buildInType) {
		this.buildInType = buildInType;
	}

	public Object getUserType() {
		return userType;
	}

	public void setUserType(Object userType) {
		this.userType = userType;
	}
}
