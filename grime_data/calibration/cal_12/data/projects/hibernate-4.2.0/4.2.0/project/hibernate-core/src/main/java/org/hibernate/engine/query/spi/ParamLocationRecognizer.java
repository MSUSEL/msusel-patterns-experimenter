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
package org.hibernate.engine.query.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.util.collections.ArrayHelper;

/**
 * Implements a parameter parser recognizer specifically for the purpose
 * of journaling parameter locations.
 *
 * @author Steve Ebersole
 */
public class ParamLocationRecognizer implements ParameterParser.Recognizer {

	public static class NamedParameterDescription {
		private final boolean jpaStyle;
		private final List positions = new ArrayList();

		public NamedParameterDescription(boolean jpaStyle) {
			this.jpaStyle = jpaStyle;
		}

		public boolean isJpaStyle() {
			return jpaStyle;
		}

		private void add(int position) {
			positions.add( position );
		}

		public int[] buildPositionsArray() {
			return ArrayHelper.toIntArray( positions );
		}
	}

	private Map namedParameterDescriptions = new HashMap();
	private List ordinalParameterLocationList = new ArrayList();

	/**
	 * Convenience method for creating a param location recognizer and
	 * initiating the parse.
	 *
	 * @param query The query to be parsed for parameter locations.
	 * @return The generated recognizer, with journaled location info.
	 */
	public static ParamLocationRecognizer parseLocations(String query) {
		ParamLocationRecognizer recognizer = new ParamLocationRecognizer();
		ParameterParser.parse( query, recognizer );
		return recognizer;
	}

	/**
	 * Returns the map of named parameter locations.  The map is keyed by
	 * parameter name; the corresponding value is a (@link NamedParameterDescription}.
	 *
	 * @return The map of named parameter locations.
	 */
	public Map getNamedParameterDescriptionMap() {
		return namedParameterDescriptions;
	}

	/**
	 * Returns the list of ordinal parameter locations.  The list elements
	 * are Integers, representing the location for that given ordinal.  Thus
	 * {@link #getOrdinalParameterLocationList()}.elementAt(n) represents the
	 * location for the nth parameter.
	 *
	 * @return The list of ordinal parameter locations.
	 */
	public List getOrdinalParameterLocationList() {
		return ordinalParameterLocationList;
	}


	// Recognition code ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void ordinalParameter(int position) {
		ordinalParameterLocationList.add( position );
	}

	public void namedParameter(String name, int position) {
		getOrBuildNamedParameterDescription( name, false ).add( position );
	}

	public void jpaPositionalParameter(String name, int position) {
		getOrBuildNamedParameterDescription( name, true ).add( position );
	}

	private NamedParameterDescription getOrBuildNamedParameterDescription(String name, boolean jpa) {
		NamedParameterDescription desc = ( NamedParameterDescription ) namedParameterDescriptions.get( name );
		if ( desc == null ) {
			desc = new NamedParameterDescription( jpa );
			namedParameterDescriptions.put( name, desc );
		}
		return desc;
	}

	public void other(char character) {
		// don't care...
	}

	public void outParameter(int position) {
		// don't care...
	}
}
