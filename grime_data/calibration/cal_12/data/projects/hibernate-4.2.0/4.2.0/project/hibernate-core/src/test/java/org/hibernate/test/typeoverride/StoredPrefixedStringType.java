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
package org.hibernate.test.typeoverride;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.AssertionFailure;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

/**
 *
 * @author Gail Badner
 */
public class StoredPrefixedStringType
		extends AbstractSingleColumnStandardBasicType<String>
		implements DiscriminatorType<String> {

	public static final String PREFIX = "PRE:";

	public static final SqlTypeDescriptor PREFIXED_VARCHAR_TYPE_DESCRIPTOR =
			new VarcharTypeDescriptor() {
				public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
							String stringValue = javaTypeDescriptor.unwrap( value, String.class, options );
							st.setString( index, PREFIX + stringValue );
						}
					};
				}

				public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicExtractor<X>( javaTypeDescriptor, this ) {
						@Override
						protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
							String stringValue = rs.getString( name );
							if ( ! stringValue.startsWith( PREFIX ) ) {
								throw new AssertionFailure( "Value read from resultset does not have prefix." );
							}
							return javaTypeDescriptor.wrap( stringValue.substring( PREFIX.length() ), options );
						}
					};
				}
			};


	public static final StoredPrefixedStringType INSTANCE = new StoredPrefixedStringType();

	public StoredPrefixedStringType() {
		super( PREFIXED_VARCHAR_TYPE_DESCRIPTOR, StringType.INSTANCE.getJavaTypeDescriptor() );
	}

	public String getName() {
		return StringType.INSTANCE.getName();
	}

	@Override
	protected boolean registerUnderJavaType() {
		return true;
	}

	public String objectToSQLString(String value, Dialect dialect) throws Exception {
		return StringType.INSTANCE.objectToSQLString( value, dialect );
	}

	public String stringToObject(String xml) throws Exception {
		return StringType.INSTANCE.stringToObject( xml );
	}

	public String toString(String value) {
		return StringType.INSTANCE.toString( value );
	}
}