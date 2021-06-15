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
package org.hibernate.cfg.beanvalidation;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;

/**
 * Duplicates the javax.validation enum (because javax validation might not be on the runtime classpath)
 *
 * @author Steve Ebersole
 */
public enum ValidationMode {
	AUTO( "auto" ),
	CALLBACK( "callback" ),
	NONE( "none" ),
	DDL( "ddl" );

	private final String externalForm;

	private ValidationMode(String externalForm) {
		this.externalForm = externalForm;
	}

	public static Set<ValidationMode> getModes(Object modeProperty) {
		Set<ValidationMode> modes = new HashSet<ValidationMode>(3);
		if (modeProperty == null) {
			modes.add( ValidationMode.AUTO );
		}
		else {
			final String[] modesInString = modeProperty.toString().split( "," );
			for ( String modeInString : modesInString ) {
				modes.add( getMode(modeInString) );
			}
		}
		if ( modes.size() > 1 && ( modes.contains( ValidationMode.AUTO ) || modes.contains( ValidationMode.NONE ) ) ) {
			throw new HibernateException( "Incompatible validation modes mixed: " +  loggable( modes ) );
		}
		return modes;
	}

	private static ValidationMode getMode(String modeProperty) {
		if (modeProperty == null || modeProperty.length() == 0) {
			return AUTO;
		}
		else {
			try {
				return valueOf( modeProperty.trim().toUpperCase() );
			}
			catch ( IllegalArgumentException e ) {
				throw new HibernateException( "Unknown validation mode in " + BeanValidationIntegrator.MODE_PROPERTY + ": " + modeProperty );
			}
		}
	}

	public static String loggable(Set<ValidationMode> modes) {
		if ( modes == null || modes.isEmpty() ) {
			return "[<empty>]";
		}
		StringBuilder buffer = new StringBuilder( "[" );
		String sep = "";
		for ( ValidationMode mode : modes ) {
			buffer.append( sep ).append( mode.externalForm );
			sep = ", ";
		}
		return buffer.append( "]" ).toString();
	}
}
