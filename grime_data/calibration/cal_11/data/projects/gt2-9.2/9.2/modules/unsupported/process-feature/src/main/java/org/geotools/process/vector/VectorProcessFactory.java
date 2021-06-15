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
package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.factory.FactoryRegistry;
import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.vector.AggregateProcess;
import org.geotools.process.vector.BoundsProcess;
import org.geotools.process.vector.BufferFeatureCollection;
import org.geotools.process.vector.CentroidProcess;
import org.geotools.process.vector.ClipProcess;
import org.geotools.process.vector.CollectGeometries;
import org.geotools.process.vector.CountProcess;
import org.geotools.process.vector.GridProcess;
import org.geotools.process.vector.InclusionFeatureCollection;
import org.geotools.process.vector.IntersectionFeatureCollection;
import org.geotools.process.vector.NearestProcess;
import org.geotools.process.vector.PointBuffers;
import org.geotools.process.vector.QueryProcess;
import org.geotools.process.vector.RectangularClipProcess;
import org.geotools.process.vector.ReprojectProcess;
import org.geotools.process.vector.SimplifyProcess;
import org.geotools.process.vector.SnapProcess;
import org.geotools.process.vector.UnionFeatureCollection;
import org.geotools.process.vector.UniqueProcess;
import org.geotools.process.vector.VectorZonalStatistics;
import org.geotools.text.Text;

/**
 * Factory providing a number of processes for working with feature data.
 * <p>
 * Internally this factory makes use of the information provided by
 * the {@link DescribeProcess} annotations to produce the correct
 * process description.
 * 
 * @author Jody Garnett (LISAsoft)
 *
 * @source $URL$
 */
public class VectorProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<VectorProcess> registry;

    public static BeanFactoryRegistry<VectorProcess> getRegistry() {
        if (registry == null) {
            synchronized (VectorProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<VectorProcess>(VectorProcess.class);
                }
            }
        }
        return registry;
    }

    public VectorProcessFactory() {
        super(Text.text("Vector processes"), "vec", getRegistry().lookupBeanClasses());
    }

}
