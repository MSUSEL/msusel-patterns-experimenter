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

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.ganttproject.gui.UIConfiguration;
import net.sourceforge.ganttproject.gui.options.model.ColorOption;
import net.sourceforge.ganttproject.gui.options.model.DefaultColorOption;
import net.sourceforge.ganttproject.gui.options.model.GP1XOptionConverter;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ProjectResource;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.time.TimeUnitStack;
import net.sourceforge.ganttproject.util.ColorConvertion;

public class ChartModelResource extends ChartModelBase {

    private ResourceLoadRenderer myResourceLoadRenderer;

    private HumanResourceManager myManager;

    private GPOptionGroup myColorOptions;

    private ColorOption myResourceNormalLoadOption;

    private ColorOption myResourceOverloadOption;

    private ColorOption myResourceUnderloadOption;

    private ColorOption myDayOffOption;

	private BottomUnitLineRendererImpl myBottomLineRenderer;

    private static class ResourceLoadOption extends DefaultColorOption implements GP1XOptionConverter {
        private String myTagName;
        private String myAttributeName;

        ResourceLoadOption(String id, String tagName, String attributeName) {
            super(id);
            myTagName = tagName;
            myAttributeName = attributeName;
        }

        public String getTagName() {
            return myTagName;
        }

        public String getAttributeName() {
            return myAttributeName;
        }

        public void loadValue(String legacyValue) {
            loadPersistentValue(legacyValue);
        }
    }
    public ChartModelResource(TaskManager taskManager,
            HumanResourceManager resourceManager, TimeUnitStack timeUnitStack,
            final UIConfiguration projectConfig, ResourceChart resourceChart) {
        super(taskManager, timeUnitStack, projectConfig);
        myResourceLoadRenderer = new ResourceLoadRenderer(this, resourceChart);
        myBottomLineRenderer = new BottomUnitLineRendererImpl(this, projectConfig);
        myManager = resourceManager;
        {
            myResourceNormalLoadOption = new ResourceLoadOption("resourceChartColors.normalLoad", "colors", "resources") {
                public void commit() {
                    super.commit();
                    projectConfig.setResourceColor(getValue());
                }
            };
            myResourceNormalLoadOption.lock();
            myResourceNormalLoadOption.setValue(new Color(140, 182, 206));
            myResourceNormalLoadOption.commit();
        }
        {
            myResourceOverloadOption = new ResourceLoadOption("resourceChartColors.overLoad", "colors", "resourceOverload") {
                public void commit() {
                    super.commit();
                    projectConfig.setResourceOverloadColor(getValue());
                }
            };
            myResourceOverloadOption.lock();
            myResourceOverloadOption.setValue(new Color(229, 50, 50));
            myResourceOverloadOption.commit();
        }
        {
            myResourceUnderloadOption = new DefaultColorOption("resourceChartColors.underLoad") {
                public void commit() {
                    super.commit();
                    projectConfig.setResourceUnderloadColor(getValue());
                }
            };
            myResourceUnderloadOption.lock();
            myResourceUnderloadOption.setValue(new Color(50, 229, 50));
            myResourceUnderloadOption.commit();
        }
        {
            myDayOffOption = new DefaultColorOption("resourceChartColors.dayOff") {
                public void commit() {
                    super.commit();
                    projectConfig.setDayOffColor(getValue());
                }
            };
            myDayOffOption.lock();
            myDayOffOption.setValue(new Color(0.9f, 1f, 0.17f));
            myDayOffOption.commit();
        }
        myColorOptions = new GPOptionGroup("resourceChartColors", new GPOption[] {myResourceNormalLoadOption, myResourceOverloadOption, myResourceUnderloadOption, myDayOffOption});
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics mainArea = g.create(0, getChartUIConfiguration()
                .getHeaderHeight(), (int) getBounds().getWidth(),
                (int) getBounds().getHeight());
        mainArea.translate(0, -getVerticalOffset());
        getPainter().setGraphics(mainArea);
        myResourceLoadRenderer.render();
        myBottomLineRenderer.setHeight(getBounds().height);
        myBottomLineRenderer.render();
        myBottomLineRenderer.getPrimitiveContainer().paint(getPainter(), mainArea);
        myResourceLoadRenderer.getPrimitiveContainer().paint(getPainter(), mainArea);
    }

    public ProjectResource[] getVisibleResources() {
        return (ProjectResource[]) myManager.getResources().toArray(
                new ProjectResource[0]);
    }


    public GPOptionGroup[] getChartOptionGroups() {
        List result = new ArrayList();
        GPOptionGroup[] superGroups = super.getChartOptionGroups();
        result.add(myColorOptions);
        return (GPOptionGroup[]) result.toArray(new GPOptionGroup[result.size()]);
    }

}
