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
package org.hibernate.ejb.engine.spi;

import static org.hibernate.engine.spi.CascadeStyle.STYLES;

import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.engine.spi.CascadingAction;

/**
 * Becasue CascadeStyle is not opened and package protected,
 * I need to subclass and override the persist alias
 *
 * Note that This class has to be triggered by EJB3PersistEventListener at class loading time
 *
 * TODO get rid of it for 3.3
 *
 * @author Emmanuel Bernard
 */
public abstract class EJB3CascadeStyle extends CascadeStyle {

	/**
	 * cascade using EJB3CascadingAction
	 */
	public static final CascadeStyle PERSIST_EJB3 = new CascadeStyle() {
		public boolean doCascade(CascadingAction action) {
			return action== EJB3CascadingAction.PERSIST_SKIPLAZY
					|| action==CascadingAction.PERSIST_ON_FLUSH;
		}
		public String toString() {
			return "STYLE_PERSIST_SKIPLAZY";
		}
	};

	static {
		STYLES.put( "persist", PERSIST_EJB3 );
	}
}
