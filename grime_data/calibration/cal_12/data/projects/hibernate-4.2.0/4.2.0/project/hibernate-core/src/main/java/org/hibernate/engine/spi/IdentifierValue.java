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

import java.io.Serializable;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;

/**
 * A strategy for determining if an identifier value is an identifier of
 * a new transient instance or a previously persistent transient instance.
 * The strategy is determined by the <tt>unsaved-value</tt> attribute in
 * the mapping file.
 *
 * @author Gavin King
 */
public class IdentifierValue implements UnsavedValueStrategy {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, IdentifierValue.class.getName());

	private final Serializable value;

	/**
	 * Always assume the transient instance is newly instantiated
	 */
	public static final IdentifierValue ANY = new IdentifierValue() {
		@Override
		public final Boolean isUnsaved(Object id) {
			LOG.trace( "ID unsaved-value strategy ANY" );
			return Boolean.TRUE;
		}
		@Override
		public Serializable getDefaultValue(Object currentValue) {
			return (Serializable) currentValue;
		}
		@Override
		public String toString() {
			return "SAVE_ANY";
		}
	};

	/**
	 * Never assume the transient instance is newly instantiated
	 */
	public static final IdentifierValue NONE = new IdentifierValue() {
		@Override
		public final Boolean isUnsaved(Object id) {
			LOG.trace( "ID unsaved-value strategy NONE" );
			return Boolean.FALSE;
		}
		@Override
		public Serializable getDefaultValue(Object currentValue) {
			return (Serializable) currentValue;
		}
		@Override
		public String toString() {
			return "SAVE_NONE";
		}
	};

	/**
	 * Assume the transient instance is newly instantiated if the identifier
	 * is null.
	 */
	public static final IdentifierValue NULL = new IdentifierValue() {
		@Override
		public final Boolean isUnsaved(Object id) {
			LOG.trace( "ID unsaved-value strategy NULL" );
			return id==null;
		}
		@Override
		public Serializable getDefaultValue(Object currentValue) {
			return null;
		}
		@Override
		public String toString() {
			return "SAVE_NULL";
		}
	};

	/**
	 * Assume nothing.
	 */
	public static final IdentifierValue UNDEFINED = new IdentifierValue() {
		@Override
		public final Boolean isUnsaved(Object id) {
			LOG.trace( "ID unsaved-value strategy UNDEFINED" );
			return null;
		}
		@Override
		public Serializable getDefaultValue(Object currentValue) {
			return null;
		}
		@Override
		public String toString() {
			return "UNDEFINED";
		}
	};

	protected IdentifierValue() {
		this.value = null;
	}

	/**
	 * Assume the transient instance is newly instantiated if
	 * its identifier is null or equal to <tt>value</tt>
	 */
	public IdentifierValue(Serializable value) {
		this.value = value;
	}

	/**
	 * Does the given identifier belong to a new instance?
	 */
	public Boolean isUnsaved(Object id) {
		LOG.tracev( "ID unsaved-value: {0}", value );
		return id==null || id.equals(value);
	}

	public Serializable getDefaultValue(Object currentValue) {
		return value;
	}

	@Override
	public String toString() {
		return "identifier unsaved-value: " + value;
	}
}