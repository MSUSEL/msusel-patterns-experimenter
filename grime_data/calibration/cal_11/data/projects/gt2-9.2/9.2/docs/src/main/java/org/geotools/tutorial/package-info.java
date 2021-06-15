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
/**
 * Source code examples for the tutorial documentation.
 * <p>
 * The implementations provided here are spliced into the tutorial documentation using sphinx by
 * making use of "markers" placed into the files such as shown below.
 * <pre>
 * public void sample(){
 *     // sample start
 *     featureCollection.accepts( new FeatureVisitor(){
 *         public void visit( Feature feature ){
 *             System.out.println( feature.getID() );
 *         }
 *     }, null );
 *     // sample end
 * }
 * </pre>
 * With this in mind please consider the source code in the contenxt of the documentation; it may
 * not always show best practice (if it is part of an example leading up to best practice).
 * 
 * @author Jody Garnett
 * @version 2.7
 * @since 2.7
 */
package org.geotools.tutorial;