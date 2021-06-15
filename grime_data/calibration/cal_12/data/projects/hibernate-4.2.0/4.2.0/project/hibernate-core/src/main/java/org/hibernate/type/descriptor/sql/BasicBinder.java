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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.type.descriptor.JdbcTypeNameMapper;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;

/**
 * Convenience base implementation of {@link ValueBinder}
 *
 * @author Steve Ebersole
 */
public abstract class BasicBinder<J> implements ValueBinder<J> {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, BasicBinder.class.getName());

    private static final String BIND_MSG_TEMPLATE = "binding parameter [%s] as [%s] - %s";
    private static final String NULL_BIND_MSG_TEMPLATE = "binding parameter [%s] as [%s] - <null>";

	private final JavaTypeDescriptor<J> javaDescriptor;
	private final SqlTypeDescriptor sqlDescriptor;

	public JavaTypeDescriptor<J> getJavaDescriptor() {
		return javaDescriptor;
	}

	public SqlTypeDescriptor getSqlDescriptor() {
		return sqlDescriptor;
	}

	public BasicBinder(JavaTypeDescriptor<J> javaDescriptor, SqlTypeDescriptor sqlDescriptor) {
		this.javaDescriptor = javaDescriptor;
		this.sqlDescriptor = sqlDescriptor;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void bind(PreparedStatement st, J value, int index, WrapperOptions options) throws SQLException {
        if ( value == null ) {
            if ( LOG.isTraceEnabled() ) {
                LOG.trace(
                        String.format(
                                NULL_BIND_MSG_TEMPLATE,
                                index,
                                JdbcTypeNameMapper.getTypeName( sqlDescriptor.getSqlType() )
                        )
                );
            }
            st.setNull( index, sqlDescriptor.getSqlType() );
        }
        else {
            if ( LOG.isTraceEnabled() ) {
                LOG.trace(
                        String.format(
                                BIND_MSG_TEMPLATE,
                                index,
                                JdbcTypeNameMapper.getTypeName( sqlDescriptor.getSqlType() ),
                                getJavaDescriptor().extractLoggableRepresentation( value )
                        )
                );
            }
            doBind( st, value, index, options );
        }
	}

	/**
	 * Perform the binding.  Safe to assume that value is not null.
	 *
	 * @param st The prepared statement
	 * @param value The value to bind (not null).
	 * @param index The index at which to bind
	 * @param options The binding options
	 *
	 * @throws SQLException Indicates a problem binding to the prepared statement.
	 */
	protected abstract void doBind(PreparedStatement st, J value, int index, WrapperOptions options) throws SQLException;
}
