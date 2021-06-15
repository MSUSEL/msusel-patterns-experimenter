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


import org.hibernate.dialect.Dialect;
import org.hibernate.internal.util.StringHelper;

/**
 * Models an identifier (name).
 *
 * @author Steve Ebersole
 */
public class Identifier {
	private final String name;
	private final boolean isQuoted;

	/**
	 * Means to generate an {@link Identifier} instance from its simple name
	 *
	 * @param name The name
	 *
	 * @return The identifier form of the name.
	 */
	public static Identifier toIdentifier(String name) {
		if ( StringHelper.isEmpty( name ) ) {
			return null;
		}
		final String trimmedName = name.trim();
		if ( isQuoted( trimmedName ) ) {
			final String bareName = trimmedName.substring( 1, trimmedName.length() - 1 );
			return new Identifier( bareName, true );
		}
		else {
			return new Identifier( trimmedName, false );
		}
	}

	public static boolean isQuoted(String name) {
		return name.startsWith( "`" ) && name.endsWith( "`" );
	}

	/**
	 * Constructs an identifier instance.
	 *
	 * @param name The identifier text.
	 * @param quoted Is this a quoted identifier?
	 */
	public Identifier(String name, boolean quoted) {
		if ( StringHelper.isEmpty( name ) ) {
			throw new IllegalIdentifierException( "Identifier text cannot be null" );
		}
		if ( isQuoted( name ) ) {
			throw new IllegalIdentifierException( "Identifier text should not contain quote markers (`)" );
		}
		this.name = name;
		this.isQuoted = quoted;
	}

	/**
	 * Get the identifiers name (text)
	 *
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Is this a quoted identifier>
	 *
	 * @return True if this is a quote identifier; false otherwise.
	 */
	public boolean isQuoted() {
		return isQuoted;
	}

	/**
	 * If this is a quoted identifier, then return the identifier name
	 * enclosed in dialect-specific open- and end-quotes; otherwise,
	 * simply return the identifier name.
	 *
	 * @param dialect The dialect whose dialect-specific quoting should be used.
	 * @return if quoted, identifier name enclosed in dialect-specific open- and end-quotes; otherwise, the
	 * identifier name.
	 */
	public String encloseInQuotesIfQuoted(Dialect dialect) {
		return isQuoted ?
				new StringBuilder( name.length() + 2 )
						.append( dialect.openQuote() )
						.append( name )
						.append( dialect.closeQuote() )
						.toString() :
				name;
	}

	@Override
	public String toString() {
		return isQuoted
				? '`' + getName() + '`'
				: getName();
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Identifier that = (Identifier) o;

		return isQuoted == that.isQuoted
				&& name.equals( that.name );
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
