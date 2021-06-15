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
package org.hibernate.test.annotations.lob;

import java.io.Serializable;

import org.hibernate.type.SerializableToBlobType;

/**
 * @author Janario Oliveira
 */
public class ExplicitSerializableType<T extends Serializable> extends SerializableToBlobType<T> {
	
	// TODO: Find another way to test that this type is being used by
	// SerializableToBlobTypeTest#testPersist.  Most AbstractStandardBasicType
	// methods are final.
	
//	@Override
//	public Object get(ResultSet rs, String name) throws SQLException {
//		CommonSerializable deserialize = (CommonSerializable) super.get( rs, name );
//		deserialize.setDefaultValue( "EXPLICIT" );
//		return deserialize;
//	}
//
//	@Override
//	public void set(PreparedStatement st, Object value, int index, SessionImplementor session) throws SQLException {
//		if ( value != null ) {
//			( (CommonSerializable) value ).setDefaultValue( null );
//		}
//		super.set( st, value, index, session );
//	}

}
