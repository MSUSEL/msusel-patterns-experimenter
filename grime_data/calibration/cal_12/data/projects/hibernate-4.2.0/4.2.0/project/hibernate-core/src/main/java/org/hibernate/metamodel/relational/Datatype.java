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
package org.hibernate.metamodel.relational;

/**
 * Models a JDBC {@link java.sql.Types DATATYPE}
 *
 * @todo Do we somehow link this in with {@link org.hibernate.internal.util.jdbc.TypeInfo} ?
 *
 * @author Steve Ebersole
 */
public class Datatype {
	private final int typeCode;
	private final String typeName;
	private final Class javaType;
	private final int hashCode;

	public Datatype(int typeCode, String typeName, Class javaType) {
		this.typeCode = typeCode;
		this.typeName = typeName;
		this.javaType = javaType;
		this.hashCode = generateHashCode();
	}

    private int generateHashCode() {
        int result = typeCode;
        if ( typeName != null ) {
            result = 31 * result + typeName.hashCode();
        }
        if ( javaType != null ) {
            result = 31 * result + javaType.hashCode();
        }
        return result;
    }

    public int getTypeCode() {
		return typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public Class getJavaType() {
		return javaType;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Datatype datatype = (Datatype) o;

		return typeCode == datatype.typeCode
				&& javaType.equals( datatype.javaType )
				&& typeName.equals( datatype.typeName );

	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return super.toString() + "[code=" + typeCode + ", name=" + typeName + ", javaClass=" + javaType.getName() + "]";
	}
}
