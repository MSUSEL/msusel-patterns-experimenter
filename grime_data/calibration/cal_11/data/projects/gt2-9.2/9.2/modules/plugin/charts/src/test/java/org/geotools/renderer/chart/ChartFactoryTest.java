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
package org.geotools.renderer.chart;

import javax.swing.Icon;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

/**
 * 
 *
 * @source $URL$
 */
public class ChartFactoryTest extends TestCase {

    ChartGraphicFactory factory = new ChartGraphicFactory();

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testPie() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        JFreeChart chart = factory.getChart(null, url, ChartGraphicFactory.FORMAT, 500);
        assertNotNull(chart);
        assertTrue(chart.getPlot() instanceof PiePlot);
        PiePlot p = (PiePlot) chart.getPlot();

        // values are turned in percentages
        assertEquals(3, p.getDataset().getItemCount());
        assertEquals(0.1f, p.getDataset().getValue(0));
        assertEquals(0.2f, p.getDataset().getValue(1));
        assertEquals(0.7f, p.getDataset().getValue(2));
    }

    public void testInvalidLocation() throws Exception {
        Literal url = ff.literal("http://weirdo?cht=p&chd=t:10,20,70&chs=200x100");
        assertNull(factory.getChart(null, url, ChartGraphicFactory.FORMAT, 500));
        assertNull(factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 500));
    }

    public void testInvalidFormat() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        assertNull(factory.getChart(null, url, "application/invalid", 500));
        assertNull(factory.getIcon(null, url, "application/invalid", 500));
    }

    public void testInvalidSize() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100x1000");
        try {
            factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 500);
            fail("Test should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
    
    public void testMissingSize() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70");
        try {
            factory.getIcon(null, url, ChartGraphicFactory.FORMAT, -1);
            fail("Test should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    public void testSizeFromChartSpec() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        Icon icon = (Icon) factory.getIcon(null, url, ChartGraphicFactory.FORMAT, -1);
        assertNotNull(icon);
        assertEquals(200, icon.getIconWidth());
        assertEquals(100, icon.getIconHeight());
    }

    public void testSizeFromSLD() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70");
        Icon icon = (Icon) factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 200);
        assertNotNull(icon);
        assertEquals(200, icon.getIconWidth());
        assertEquals(200, icon.getIconHeight());
    }
    
    public void testSizeFromSLDAndChart() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=200x100");
        Icon icon = (Icon) factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 600);
        assertNotNull(icon);
        assertEquals(600, icon.getIconWidth());
        assertEquals(300, icon.getIconHeight());
    }
    
    public void testSizeFromSLDAndChartVertical() throws Exception {
        Literal url = ff.literal("http://chart?cht=p&chd=t:10,20,70&chs=100x300");
        Icon icon = (Icon) factory.getIcon(null, url, ChartGraphicFactory.FORMAT, 600);
        assertNotNull(icon);
        assertEquals(200, icon.getIconWidth());
        assertEquals(600, icon.getIconHeight());
    }

}
