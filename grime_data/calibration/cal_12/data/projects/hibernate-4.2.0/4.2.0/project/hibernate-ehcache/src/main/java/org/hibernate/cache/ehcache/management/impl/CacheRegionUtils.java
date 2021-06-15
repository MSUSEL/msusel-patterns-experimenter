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
package org.hibernate.cache.ehcache.management.impl;

import java.awt.*;

/**
 * CacheRegionUtils
 *
 * @author gkeim
 */
public abstract class CacheRegionUtils {
	/**
	 * HIT_COLOR
	 */
	public static final Color HIT_COLOR = Color.green;

	/**
	 * MISS_COLOR
	 */
	public static final Color MISS_COLOR = Color.red;

	/**
	 * PUT_COLOR
	 */
	public static final Color PUT_COLOR = Color.blue;

	/**
	 * HIT_FILL_COLOR
	 */
	public final static Color HIT_FILL_COLOR = CacheRegionUtils.HIT_COLOR.brighter().brighter().brighter();

	/**
	 * MISS_FILL_COLOR
	 */
	public final static Color MISS_FILL_COLOR = CacheRegionUtils.MISS_COLOR.brighter().brighter().brighter();

	/**
	 * PUT_FILL_COLOR
	 */
	public final static Color PUT_FILL_COLOR = CacheRegionUtils.PUT_COLOR.brighter().brighter().brighter();

	/**
	 * HIT_DRAW_COLOR
	 */
	public final static Color HIT_DRAW_COLOR = CacheRegionUtils.HIT_COLOR.darker();

	/**
	 * MISS_DRAW_COLOR
	 */
	public final static Color MISS_DRAW_COLOR = CacheRegionUtils.MISS_COLOR.darker();

	/**
	 * PUT_DRAW_COLOR
	 */
	public final static Color PUT_DRAW_COLOR = CacheRegionUtils.PUT_COLOR.darker();


	/**
	 * determineShortName
	 *
	 * @param fullName
	 */
	public static String determineShortName(String fullName) {
		String result = fullName;

		if ( fullName != null ) {
			String[] comps = fullName.split( "\\." );
			if ( comps.length == 1 ) {
				return fullName;
			}
			boolean truncate = true;
			for ( int i = 0; i < comps.length; i++ ) {
				String comp = comps[i];
				char c = comp.charAt( 0 );
				if ( truncate && Character.isUpperCase( c ) ) {
					truncate = false;
				}
				if ( truncate ) {
					comps[i] = Character.toString( c );
				}
			}
			result = join( comps, '.' );
		}

		return result;
	}

	/**
	 * join
	 *
	 * @param elements
	 * @param c
	 */
	private static String join(String[] elements, char c) {
		if ( elements == null ) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for ( String s : elements ) {
			sb.append( s ).append( c );
		}
		return sb.length() > 0 ? sb.substring( 0, sb.length() - 1 ) : "";
	}
}
