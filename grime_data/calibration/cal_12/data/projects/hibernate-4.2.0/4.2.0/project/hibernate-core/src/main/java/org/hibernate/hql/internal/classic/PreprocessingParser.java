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
package org.hibernate.hql.internal.classic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.QueryException;
import org.hibernate.hql.internal.CollectionProperties;
import org.hibernate.internal.util.StringHelper;

/**
 *
 */
public class PreprocessingParser implements Parser {

	private static final Set HQL_OPERATORS;

	static {
		HQL_OPERATORS = new HashSet();
		HQL_OPERATORS.add( "<=" );
		HQL_OPERATORS.add( ">=" );
		HQL_OPERATORS.add( "=>" );
		HQL_OPERATORS.add( "=<" );
		HQL_OPERATORS.add( "!=" );
		HQL_OPERATORS.add( "<>" );
		HQL_OPERATORS.add( "!#" );
		HQL_OPERATORS.add( "!~" );
		HQL_OPERATORS.add( "!<" );
		HQL_OPERATORS.add( "!>" );
		HQL_OPERATORS.add( "is not" );
		HQL_OPERATORS.add( "not like" );
		HQL_OPERATORS.add( "not in" );
		HQL_OPERATORS.add( "not between" );
		HQL_OPERATORS.add( "not exists" );
	}

	private Map replacements;
	private boolean quoted;
	private StringBuilder quotedString;
	private ClauseParser parser = new ClauseParser();
	private String lastToken;
	private String currentCollectionProp;

	public PreprocessingParser(Map replacements) {
		this.replacements = replacements;
	}

	public void token(String token, QueryTranslatorImpl q) throws QueryException {

		//handle quoted strings
		if ( quoted ) {
			quotedString.append( token );
		}
		if ( "'".equals( token ) ) {
			if ( quoted ) {
				token = quotedString.toString();
			}
			else {
				quotedString = new StringBuilder( 20 ).append( token );
			}
			quoted = !quoted;
		}
		if ( quoted ) return;

		//ignore whitespace
		if ( ParserHelper.isWhitespace( token ) ) return;

		//do replacements
		String substoken = ( String ) replacements.get( token );
		token = ( substoken == null ) ? token : substoken;

		//handle HQL2 collection syntax
		if ( currentCollectionProp != null ) {
			if ( "(".equals( token ) ) {
				return;
			}
			else if ( ")".equals( token ) ) {
				currentCollectionProp = null;
				return;
			}
			else {
				token = StringHelper.qualify( token, currentCollectionProp );
			}
		}
		else {
			String prop = CollectionProperties.getNormalizedPropertyName( token.toLowerCase() );
			if ( prop != null ) {
				currentCollectionProp = prop;
				return;
			}
		}


		//handle <=, >=, !=, is not, not between, not in
		if ( lastToken == null ) {
			lastToken = token;
		}
		else {
			String doubleToken = ( token.length() > 1 ) ?
					lastToken + ' ' + token :
					lastToken + token;
			if ( HQL_OPERATORS.contains( doubleToken.toLowerCase() ) ) {
				parser.token( doubleToken, q );
				lastToken = null;
			}
			else {
				parser.token( lastToken, q );
				lastToken = token;
			}
		}

	}

	public void start(QueryTranslatorImpl q) throws QueryException {
		quoted = false;
		parser.start( q );
	}

	public void end(QueryTranslatorImpl q) throws QueryException {
		if ( lastToken != null ) parser.token( lastToken, q );
		parser.end( q );
		lastToken = null;
		currentCollectionProp = null;
	}

}






