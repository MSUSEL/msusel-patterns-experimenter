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
package org.hibernate.type.descriptor.sql;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.jdbc.BinaryStream;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

/**
 * Descriptor for {@link Types#BLOB BLOB} handling.
 *
 * @author Steve Ebersole
 * @author Gail Badner
 * @author Brett Meyer
 */
public abstract class BlobTypeDescriptor implements SqlTypeDescriptor {

	private BlobTypeDescriptor() {
	}

	@Override
	public int getSqlType() {
		return Types.BLOB;
	}

	@Override
	public boolean canBeRemapped() {
		return true;
	}

	@Override
	public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
		return new BasicExtractor<X>( javaTypeDescriptor, this ) {
			@Override
			protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
				return javaTypeDescriptor.wrap( rs.getBlob( name ), options );
			}
		};
	}

	protected abstract <X> BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor);

	public <X> BasicBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
		return getBlobBinder( javaTypeDescriptor );
	}

	public static final BlobTypeDescriptor DEFAULT =
			new BlobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
							BlobTypeDescriptor descriptor = BLOB_BINDING;
							if ( byte[].class.isInstance( value ) ) {
								// performance shortcut for binding BLOB data in byte[] format
								descriptor = PRIMITIVE_ARRAY_BINDING;
							}
							else if ( options.useStreamForLobBinding() ) {
								descriptor = STREAM_BINDING;
							}
							descriptor.getBlobBinder( javaTypeDescriptor ).doBind( st, value, index, options );
						}
					};
				}
			};

	public static final BlobTypeDescriptor PRIMITIVE_ARRAY_BINDING =
			new BlobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						public void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
								throws SQLException {
							st.setBytes( index, javaTypeDescriptor.unwrap( value, byte[].class, options ) );
						}
					};
				}
			};

	public static final BlobTypeDescriptor BLOB_BINDING =
			new BlobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
								throws SQLException {
							st.setBlob( index, javaTypeDescriptor.unwrap( value, Blob.class, options ) );
						}
					};
				}
			};

	public static final BlobTypeDescriptor STREAM_BINDING =
			new BlobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getBlobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
								throws SQLException {
							final BinaryStream binaryStream = javaTypeDescriptor.unwrap( value, BinaryStream.class, options );
							st.setBinaryStream( index, binaryStream.getInputStream(), binaryStream.getLength() );
						}
					};
				}
			};

}
