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
package org.geotools.process.raster;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.text.Text;

public class RasterProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<RasterProcess> registry;

    public static BeanFactoryRegistry<RasterProcess> getRegistry() {
        if (registry == null) {
            synchronized (RasterProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<RasterProcess>(RasterProcess.class);
                }
            }
        }
        return registry;
    }

    public RasterProcessFactory() {
        super(Text.text("Raster processes"), "ras", getRegistry().lookupBeanClasses());
    }

}