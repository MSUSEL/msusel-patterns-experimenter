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
package org.geotools.process.factory;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.geotools.referencing.CRS.AxisOrder;

import com.vividsolutions.jts.geom.Geometry;

@DescribeProcess(title = "Defaults", description = "Process used to test default value processing")
public class DefaultsProcess {
    
    static final String GREET_DEFAULT = "Hello!";

    @DescribeResults({ 
        @DescribeResult(name = "string", type = String.class),
        @DescribeResult(name = "geometry", type = Geometry.class),
            @DescribeResult(name = "int", type = Integer.class),
            @DescribeResult(name = "double", type = Double.class),
            @DescribeResult(name = "axisOrder", type = AxisOrder.class),
            @DescribeResult(name = "short", type = Short.class),
            @DescribeResult(name = "greet", type = String.class),
            @DescribeResult(name = "rect", type = Rectangle.class)})
    public Map<String, Object> execute(
            // default converters usage
            @DescribeParameter(name = "string", defaultValue = "default string") String string,
            @DescribeParameter(name = "geometry", defaultValue = "POINT(0 0)") Geometry geometry,
            @DescribeParameter(name = "int", defaultValue = "1") int i,
            @DescribeParameter(name = "double", defaultValue = "0.65e-10") double d,
            // checking out enum conversion
            @DescribeParameter(name = "axisOrder", defaultValue = "EAST_NORTH") AxisOrder axisOrder,
            // reference to a constant in the target type
            @DescribeParameter(name = "short", defaultValue = "MAX_VALUE") short s,
            // reference to a constant in the process
            @DescribeParameter(name = "greet", defaultValue = "GREET_DEFAULT") String greet,
            // absolute reference to constant
            @DescribeParameter(name = "rect", defaultValue = "org.geotools.process.factory.BeanProcessFactoryTest#DEFAULT_RECTANGLE") Rectangle rect) {
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("string", string);
        results.put("geometry", geometry);
        results.put("int", i);
        results.put("double", d);
        results.put("axisOrder", axisOrder);
        results.put("short", s);
        results.put("greet", greet);
        results.put("rect", rect);

        return results;
    }
}
