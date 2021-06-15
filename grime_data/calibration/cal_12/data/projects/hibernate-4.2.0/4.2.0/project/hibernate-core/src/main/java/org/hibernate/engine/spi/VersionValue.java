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
package org.hibernate.engine.spi;

import org.jboss.logging.Logger;

import org.hibernate.MappingException;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.internal.CoreMessageLogger;

/**
 * A strategy for determining if a version value is an version of
 * a new transient instance or a previously persistent transient instance.
 * The strategy is determined by the <tt>unsaved-value</tt> attribute in
 * the mapping file.
 *
 * @author Gavin King
 */
public class VersionValue implements UnsavedValueStrategy {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, VersionValue.class.getName());

	private final Object value;
	/**
	 * Assume the transient instance is newly instantiated if the version
	 * is null, otherwise assume it is a detached instance.
	 */
	public static final VersionValue NULL = new VersionValue() {
		@Override
		public final Boolean isUnsaved(Object version) {
			LOG.trace( "Version unsaved-value strategy NULL" );
			return version==null;
		}
		@Override
		public Object getDefaultValue(Object currentValue) {
			return null;
		}
		@Override
		public String toString() {
			return "VERSION_SAVE_NULL";
		}
	};
	/**
	 * Assume the transient instance is newly instantiated if the version
	 * is null, otherwise defer to the identifier unsaved-value.
	 */
	public static final VersionValue UNDEFINED = new VersionValue() {
		@Override
		public final Boolean isUnsaved(Object version) {
			LOG.trace( "Version unsaved-value strategy UNDEFINED" );
			return version==null ? Boolean.TRUE : null;
		}
		@Override
		public Object getDefaultValue(Object currentValue) {
			return currentValue;
		}
		@Override
		public String toString() {
			return "VERSION_UNDEFINED";
		}
	};
	/**
	 * Assume the transient instance is newly instantiated if the version
	 * is negative, otherwise assume it is a detached instance.
	 */
	public static final VersionValue NEGATIVE = new VersionValue() {

		@Override
		public final Boolean isUnsaved(Object version) throws MappingException {
			LOG.trace( "Version unsaved-value strategy NEGATIVE" );
			if (version==null) return Boolean.TRUE;
			if ( version instanceof Number ) {
				return ( (Number) version ).longValue() < 0l;
			}
			throw new MappingException( "unsaved-value NEGATIVE may only be used with short, int and long types" );
		}
		@Override
		public Object getDefaultValue(Object currentValue) {
			return IdentifierGeneratorHelper.getIntegralDataTypeHolder( currentValue.getClass() )
					.initialize( -1L )
					.makeValue();
		}
		@Override
		public String toString() {
			return "VERSION_NEGATIVE";
		}
	};

	protected VersionValue() {
		this.value = null;
	}

	/**
	 * Assume the transient instance is newly instantiated if
	 * its version is null or equal to <tt>value</tt>
	 * @param value value to compare to
	 */
	public VersionValue(Object value) {
		this.value = value;
	}

	@Override
	public Boolean isUnsaved(Object version) throws MappingException  {
		LOG.tracev( "Version unsaved-value: {0}", value );
		return version==null || version.equals(value);
	}

	@Override
	public Object getDefaultValue(Object currentValue) {
		return value;
	}

	@Override
    public String toString() {
		return "version unsaved-value: " + value;
	}
}