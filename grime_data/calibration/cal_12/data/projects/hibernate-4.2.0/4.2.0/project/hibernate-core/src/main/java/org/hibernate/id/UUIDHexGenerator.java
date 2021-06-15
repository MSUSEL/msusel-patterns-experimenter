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
package org.hibernate.id;
import java.io.Serializable;
import java.util.Properties;

import org.jboss.logging.Logger;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.type.Type;

/**
 * <b>uuid</b><br>
 * <br>
 * A <tt>UUIDGenerator</tt> that returns a string of length 32,
 * This string will consist of only hex digits. Optionally,
 * the string may be generated with separators between each
 * component of the UUID.
 *
 * Mapping parameters supported: separator.
 *
 * @author Gavin King
 */
public class UUIDHexGenerator extends AbstractUUIDGenerator implements Configurable {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, UUIDHexGenerator.class.getName());

	private static boolean warned = false;

	private String sep = "";

	public UUIDHexGenerator() {
		if ( ! warned ) {
			warned = true;
            LOG.usingUuidHexGenerator(this.getClass().getName(), UUIDGenerator.class.getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void configure(Type type, Properties params, Dialect d) {
		sep = ConfigurationHelper.getString( "separator", params, "" );
	}

	/**
	 * {@inheritDoc}
	 */
	public Serializable generate(SessionImplementor session, Object obj) {
		return new StringBuilder( 36 )
				.append( format( getIP() ) ).append( sep )
				.append( format( getJVM() ) ).append( sep )
				.append( format( getHiTime() ) ).append( sep )
				.append( format( getLoTime() ) ).append( sep )
				.append( format( getCount() ) )
				.toString();
	}

	protected String format(int intValue) {
		String formatted = Integer.toHexString( intValue );
		StringBuilder buf = new StringBuilder( "00000000" );
		buf.replace( 8 - formatted.length(), 8, formatted );
		return buf.toString();
	}

	protected String format(short shortValue) {
		String formatted = Integer.toHexString( shortValue );
		StringBuilder buf = new StringBuilder( "0000" );
		buf.replace( 4 - formatted.length(), 4, formatted );
		return buf.toString();
	}
}
