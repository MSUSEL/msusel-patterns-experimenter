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

/**
 * The base contract for determining transient status versus detached status.
 *
 * @author Steve Ebersole
 */
public interface UnsavedValueStrategy {
	/**
	 * Make the transient/detached determination
	 *
	 * @param test The value to be tested
	 *
	 * @return {@code true} indicates the value corresponds to unsaved data (aka, transient state; {@code false}
	 * indicates the value does not corresponds to unsaved data (aka, detached state); {@code null} indicates that
	 * this strategy was not able to determine conclusively.
	 */
	public Boolean isUnsaved(Object test);

	/**
	 * Get a default value meant to indicate transience.
	 *
	 * @param currentValue The current state value.
	 *
	 * @return The default transience value.
	 */
	public Object getDefaultValue(Object currentValue);
}
