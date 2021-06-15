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
package org.archive.crawler;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Calls start and stop of Heritrix when Heritrix is bundled as a webapp.
 * @author stack
 * @version $Date: 2005-11-17 00:55:56 +0000 (Thu, 17 Nov 2005) $, $Revision: 3959 $
 */
public class WebappLifecycle implements ServletContextListener {
    private Heritrix heritrix = null;
    public void contextInitialized(ServletContextEvent sce) {
        if (!Heritrix.isCommandLine()) {
            try {
				this.heritrix = new Heritrix(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
            if (this.heritrix != null) {
                this.heritrix.start();
            }
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (this.heritrix !=  null) {
            this.heritrix.destroy();
            this.heritrix = null;
        }
    }
}
