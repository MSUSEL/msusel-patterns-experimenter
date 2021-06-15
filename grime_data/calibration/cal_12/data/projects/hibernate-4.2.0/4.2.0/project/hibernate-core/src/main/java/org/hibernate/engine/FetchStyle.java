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
package org.hibernate.engine;

/**
 * Enumeration of values describing <b>HOW</b> fetching should occur.
 *
 * @author Steve Ebersole
 * @see FetchTiming
 */
public enum FetchStyle {
	/**
	 * Performs a separate SQL select to load the indicated data.  This can either be eager (the second select is
	 * issued immediately) or lazy (the second select is delayed until the data is needed).
	 */
	SELECT,
	/**
	 * Inherently an eager style of fetching.  The data to be fetched is obtained as part of an SQL join.
	 */
	JOIN,
	/**
	 * Initializes a number of indicated data items (entities or collections) in a series of grouped sql selects
	 * using an in-style sql restriction to define the batch size.  Again, can be either eager or lazy.
	 */
	BATCH,
	/**
	 * Performs fetching of associated data (currently limited to only collections) based on the sql restriction
	 * used to load the owner.  Again, can be either eager or lazy.
	 */
	SUBSELECT
}
