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
/*
 * Created on 17.06.2004
 *
 */
package net.sourceforge.ganttproject.chart;

import java.awt.Color;
import java.awt.Font;

import net.sourceforge.ganttproject.font.Fonts;
import net.sourceforge.ganttproject.gui.UIConfiguration;

/**
 * @author bard
 */
public class ChartUIConfiguration {

    private final Font mySpanningRowTextFont;

    private final Color mySpanningHeaderBackgroundColor;

    private final Color myHeaderBorderColor;

    private final Color myHorizontalGutterColor1 = new Color(0.807f, 0.807f,
            0.807f);

    private final Color myHorizontalGutterColor2 = Color.white;

    private final Color myBottomUnitGridColor;

    private final Font myBottomUnitFont;

    private final Color myWorkingTimeBackgroundColor;

    private final Color myHolidayTimeBackgroundColor;

    private final Color myPublicHolidayTimeBackgroundColor;

    private int myRowHeight;

    private UIConfiguration myProjectConfig;

    private int myHeaderHeight = 44;

    private boolean isCriticalPathOn;

    private int myYOffset = 0;

    private final int myMargin = 4;

    ChartUIConfiguration(UIConfiguration projectConfig) {
        mySpanningRowTextFont = Fonts.TOP_UNIT_FONT;
        myBottomUnitFont = projectConfig.getChartMainFont();
        mySpanningHeaderBackgroundColor = new Color(0.930f, 0.930f, 0.930f);
        myHeaderBorderColor = new Color(0.482f, 0.482f, 0.482f);
        myWorkingTimeBackgroundColor = Color.WHITE;
        myHolidayTimeBackgroundColor = new Color(0.930f, 0.930f, 0.930f);
        myPublicHolidayTimeBackgroundColor = new Color(240, 220, 240);
        // myHeaderBorderColor = new Color(0f, 1f, 0f);
        myBottomUnitGridColor = new Color(0.482f, 0.482f, 0.482f);
        myProjectConfig = projectConfig;
    }

    Font getSpanningHeaderFont() {
        return mySpanningRowTextFont;
    }

    Font getBottomUnitFont() {
        return myBottomUnitFont;
    }

    public int getHeaderHeight() {
        return myHeaderHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        myHeaderHeight = headerHeight;
    }

    public int getSpanningHeaderHeight() {
        return myHeaderHeight / 2;
    }

    public Color getSpanningHeaderBackgroundColor() {
        return mySpanningHeaderBackgroundColor;
    }

    public Color getHeaderBorderColor() {
        return myHeaderBorderColor;
    }

    public Color getHorizontalGutterColor1() {
        return myHorizontalGutterColor1;
    }

    public Color getHorizontalGutterColor2() {
        return myHorizontalGutterColor2;
    }

    public Color getBottomUnitGridColor() {
        return myBottomUnitGridColor;
    }

    public Color getWorkingTimeBackgroundColor() {
        return myWorkingTimeBackgroundColor;
    }

    public Color getHolidayTimeBackgroundColor() {
        return myHolidayTimeBackgroundColor;
    }

    public Color getPublicHolidayTimeBackgroundColor() {
        return myPublicHolidayTimeBackgroundColor;
    }

    public int getRowHeight() {
        return myRowHeight;
    }

    public void setRowHeight(int rowHeight) {
        myRowHeight = rowHeight;
    }

    public Color getDefaultTaskColor() {
        return myProjectConfig.getTaskColor();
    }

    public Color getWeekEndColor() {
        return myProjectConfig.getWeekEndColor();
    }

    public boolean isRedlineOn() {
        return myProjectConfig.isRedlineOn();
    }

    public Font getChartFont() {
        return myProjectConfig.getChartMainFont();
    }

    public Color getResourceNormalLoadColor() {
        return myProjectConfig.getResourceColor();
    }

    public Color getResourceOverloadColor() {
        return myProjectConfig.getResourceOverloadColor();
    }

    public Color getResourceUnderLoadColor() {
        return myProjectConfig.getResourceUnderloadColor();
    }

	public Color getPreviousTaskColor() {
        return myProjectConfig.getPreviousTaskColor();
    }
	
    public Color getEarlierPreviousTaskColor() {
        return myProjectConfig.getEarlierPreviousTaskColor();
    }
    public Color getLaterPreviousTaskColor() {
        return myProjectConfig.getLaterPreviousTaskColor();
    }
	
    public boolean isCriticalPathOn() {
        return myProjectConfig.isCriticalPathOn();
    }

    public Color getDayOffColor() {
        return myProjectConfig.getDayOffColor();
    }

    public int getYOffSet() {
        return myYOffset;
    }

    public void setYOffSet(int offset) {
        myYOffset = offset;
    }

    public int getMargin() {
        return myMargin;
    }
    
    public float getWeekendAlphaValue() {
        return myProjectConfig.getWeekendAlphaRenderingOption().getValueAsFloat();
    }    

}
