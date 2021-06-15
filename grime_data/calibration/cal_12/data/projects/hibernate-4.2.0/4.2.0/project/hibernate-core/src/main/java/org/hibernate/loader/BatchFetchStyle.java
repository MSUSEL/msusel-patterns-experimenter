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
package org.hibernate.loader;

import org.jboss.logging.Logger;

/**
 * Defines the style that should be used to perform batch loading.  Which style to use is declared using
 * the "{@value org.hibernate.cfg.AvailableSettings#BATCH_FETCH_STYLE}"
 * ({@link org.hibernate.cfg.AvailableSettings#BATCH_FETCH_STYLE}) setting
 *
 * @author Steve Ebersole
 */
public enum BatchFetchStyle {
	/**
	 * The legacy algorithm where we keep a set of pre-built batch sizes based on
	 * {@link org.hibernate.internal.util.collections.ArrayHelper#getBatchSizes}.  Batches are performed
	 * using the next-smaller pre-built batch size from the number of existing batchable identifiers.
	 * <p/>
	 * For example, with a batch-size setting of 32 the pre-built batch sizes would be [32, 16, 10, 9, 8, 7, .., 1].
	 * An attempt to batch load 31 identifiers would result in batches of 16, 10, and 5.
	 */
	LEGACY,
	/**
	 * Still keeps the concept of pre-built batch sizes, but uses the next-bigger batch size and pads the extra
	 * identifier placeholders.
	 * <p/>
	 * Using the same example of a batch-size setting of 32 the pre-built batch sizes would be the same.  However, the
	 * attempt to batch load 31 identifiers would result just a single batch of size 32.  The identifiers to load would
	 * be "padded" (aka, repeated) to make up the difference.
	 */
	PADDED,
	/**
	 * Dynamically builds its SQL based on the actual number of available ids.  Does still limit to the batch-size
	 * defined on the entity/collection
	 */
	DYNAMIC;

	private static final Logger log = Logger.getLogger( BatchFetchStyle.class );

	public static BatchFetchStyle byName(String name) {
		return valueOf( name.toUpperCase() );
	}

	public static BatchFetchStyle interpret(Object setting) {
		log.tracef( "Interpreting BatchFetchStyle from setting : %s", setting );

		if ( setting == null ) {
			return LEGACY; // as default
		}

		if ( BatchFetchStyle.class.isInstance( setting ) ) {
			return (BatchFetchStyle) setting;
		}

		try {
			final BatchFetchStyle byName = byName( setting.toString() );
			if ( byName != null ) {
				return byName;
			}
		}
		catch (Exception ignore) {
		}

		log.debugf( "Unable to interpret given setting [%s] as BatchFetchStyle", setting );

		return LEGACY; // again as default.
	}
}
