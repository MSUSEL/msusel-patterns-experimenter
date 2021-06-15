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
package org.hibernate.sql;
import org.hibernate.dialect.Dialect;

/**
 * An alias generator for SQL identifiers
 * @author Gavin King
 */
public final class Alias {

	private final int length;
	private final String suffix;

	/**
	 * Constructor for Alias.
	 */
	public Alias(int length, String suffix) {
		super();
		this.length = (suffix==null) ? length : length - suffix.length();
		this.suffix = suffix;
	}

	/**
	 * Constructor for Alias.
	 */
	public Alias(String suffix) {
		super();
		this.length = Integer.MAX_VALUE;
		this.suffix = suffix;
	}

	public String toAliasString(String sqlIdentifier) {
		char begin = sqlIdentifier.charAt(0);
		int quoteType = Dialect.QUOTE.indexOf(begin);
		String unquoted = getUnquotedAliasString(sqlIdentifier, quoteType);
		if ( quoteType >= 0 ) {
			char endQuote = Dialect.CLOSED_QUOTE.charAt(quoteType);
			return begin + unquoted + endQuote;
		}
		else {
			return unquoted;
		}
	}

	public String toUnquotedAliasString(String sqlIdentifier) {
		return getUnquotedAliasString(sqlIdentifier);
	}

	private String getUnquotedAliasString(String sqlIdentifier) {
		char begin = sqlIdentifier.charAt(0);
		int quoteType = Dialect.QUOTE.indexOf(begin);
		return getUnquotedAliasString(sqlIdentifier, quoteType);
	}

	private String getUnquotedAliasString(String sqlIdentifier, int quoteType) {
		String unquoted = sqlIdentifier;
		if ( quoteType >= 0 ) {
			//if the identifier is quoted, remove the quotes
			unquoted = unquoted.substring( 1, unquoted.length()-1 );
		}
		if ( unquoted.length() > length ) {
			//truncate the identifier to the max alias length, less the suffix length
			unquoted = unquoted.substring(0, length);
		}
		return ( suffix == null ) ? unquoted : unquoted + suffix;
	}

	public String[] toUnquotedAliasStrings(String[] sqlIdentifiers) {
		String[] aliases = new String[ sqlIdentifiers.length ];
		for ( int i=0; i<sqlIdentifiers.length; i++ ) {
			aliases[i] = toUnquotedAliasString(sqlIdentifiers[i]);
		}
		return aliases;
	}

	public String[] toAliasStrings(String[] sqlIdentifiers) {
		String[] aliases = new String[ sqlIdentifiers.length ];
		for ( int i=0; i<sqlIdentifiers.length; i++ ) {
			aliases[i] = toAliasString(sqlIdentifiers[i]);
		}
		return aliases;
	}

}
