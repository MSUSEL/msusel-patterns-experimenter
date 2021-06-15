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
package org.geotools.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import org.geotools.swing.dialog.JExceptionReporter;


/**
 * A safe version of AbstractAction that will log any problems encountered.
 * <p>
 * This is not generally a good practice - we are just using it as an excuse to not mess up code
 * examples with exception handling code (gasp!).
 * </p>
 * TODO: provide a background Runnable...
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class SafeAction extends AbstractAction {
    private static final long serialVersionUID = 1118122797759176800L;

    /**
     * Constructor
     * @param name name for the associated control
     */
    public SafeAction(String name) {
        super(name);
    }

    /**
     * Sub-classes (usually anonymous) must override this method instead
     * of the usual {@linkplain javax.swing.Action#actionPerformed}
     *
     * @param e the action event
     * @throws Throwable on error
     */
    public abstract void action( ActionEvent e ) throws Throwable;

    /**
     * Calls the {@linkplain #action } method
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            action( e );
        } catch (Throwable t) {
            JExceptionReporter.showDialog(t);
        }
    }

}
