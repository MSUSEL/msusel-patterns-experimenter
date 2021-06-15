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
package com.finalist.jag.util;

import java.util.Collection;

/**
 * A template engine is a component that works from a template file, taking dynamic input from the
 * JagGenerator, to produce a set of output files.
 * <p>
 * <b>NOTE:</b> Implementations of this interface must have a no-args constructor.
 *
 * @author Michael O'Connor
 */
public interface TemplateEngine {

//this interface was introduced as a result of refactoring, so please excuse the poor API! ;)

   /**
    * This method instructs the template engine to process a collection of template files, and
    * as a result to create / update / delete files from the output directory.
    *
    * @param documents a Collection of File objects, of all the templates that you want to be processed.
    * @param outputDir the name of the directory where the destination files are.
    * @return the number of new files created by the process.
    * @throws InterruptedException The implementation should listen periodically for its Thread being
    * interrupted and thrown an InterruptedException accordingly.  This is the mechanism whereby the user
    * can halt the process.
    */
   int process(Collection documents, String outputDir) throws InterruptedException;

   /**
    * Allow specifying overwrite mode.
    * @param overwrite If set to True, the template engine will overwrite the existing files.
    */
   void setOverwrite(Boolean overwrite);
}
