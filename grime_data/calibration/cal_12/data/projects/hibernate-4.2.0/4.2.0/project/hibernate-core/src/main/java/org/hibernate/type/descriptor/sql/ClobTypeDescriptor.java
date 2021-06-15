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

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.jdbc.CharacterStream;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

/**
 * Descriptor for {@link Types#CLOB CLOB} handling.
 *
 * @author Steve Ebersole
 * @author Gail Badner
 */
public abstract class ClobTypeDescriptor implements SqlTypeDescriptor {
	@Override
	public int getSqlType() {
		return Types.CLOB;
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
                return javaTypeDescriptor.wrap( rs.getClob( name ), options );
            }
		};
	}

	protected abstract <X> BasicBinder<X> getClobBinder(JavaTypeDescriptor<X> javaTypeDescriptor);

	@Override
    public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
		return getClobBinder( javaTypeDescriptor );
	}


	public static final ClobTypeDescriptor DEFAULT =
			new ClobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options) throws SQLException {
							if ( options.useStreamForLobBinding() ) {
								STREAM_BINDING.getClobBinder( javaTypeDescriptor ).doBind( st, value, index, options );
							}
							else {
								CLOB_BINDING.getClobBinder( javaTypeDescriptor ).doBind( st, value, index, options );
							}
						}
					};
				}
			};

	public static final ClobTypeDescriptor CLOB_BINDING =
			new ClobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
								throws SQLException {
							st.setClob( index, javaTypeDescriptor.unwrap( value, Clob.class, options ) );
						}
					};
				}
			};

	public static final ClobTypeDescriptor STREAM_BINDING =
			new ClobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
								throws SQLException {
							final CharacterStream characterStream = javaTypeDescriptor.unwrap( value, CharacterStream.class, options );
							st.setCharacterStream( index, characterStream.asReader(), characterStream.getLength() );
						}
					};
				}
			};

	public static final ClobTypeDescriptor STREAM_BINDING_EXTRACTING =
			new ClobTypeDescriptor() {
				@Override
                public <X> BasicBinder<X> getClobBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicBinder<X>( javaTypeDescriptor, this ) {
						@Override
						protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
								throws SQLException {
							final CharacterStream characterStream = javaTypeDescriptor.unwrap( value, CharacterStream.class, options );
							st.setCharacterStream( index, characterStream.asReader(), characterStream.getLength() );
						}
					};
				}
				
				@Override
				public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
					return new BasicExtractor<X>( javaTypeDescriptor, this ) {
						@Override
			            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
			                return javaTypeDescriptor.wrap( rs.getCharacterStream( name ), options );
			            }
					};
				}
			};

}
