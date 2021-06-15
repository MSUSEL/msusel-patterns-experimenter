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
package net.sourceforge.ganttproject.chart;

/**
 * Created by IntelliJ IDEA. User: bard Date: 12.10.2004 Time: 0:56:19 To change
 * this template use Options | File Templates.
 */
public class ChartRendererBase {
    private int myHeight;

    private final ChartModelBase myChartModel;

    private final GraphicPrimitiveContainer myPrimitiveContainer;

    private boolean isEnabled = true;

    public ChartRendererBase(ChartModelBase model) {
        myPrimitiveContainer = new GraphicPrimitiveContainer();
        myChartModel = model;
    }

    void setHeight(int height) {
        myHeight = height;
    }

    protected int getHeight() {
        return myHeight;
    }

    protected int getWidth() {
        return (int) getChartModel().getBounds().getWidth();
    }

    protected ChartUIConfiguration getConfig() {
        return getChartModel().getChartUIConfiguration();
    }

    public GraphicPrimitiveContainer getPrimitiveContainer() {
        return myPrimitiveContainer;
    }

    protected ChartModelBase getChartModel() {
        return myChartModel;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    
    public void beforeProcessingTimeFrames() {
        myPrimitiveContainer.clear();
    }
    
}
