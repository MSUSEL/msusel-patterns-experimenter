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
/***************************************************************************
 ColorConvertion.java 
 ------------------------------------------
 begin                : 6 juil. 2004
 copyright            : (C) 2004 by Thomas Alexandre
 email                : alexthomas@ganttproject.org
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package net.sourceforge.ganttproject.util;

import java.awt.Color;
import java.util.regex.Pattern;

import net.sourceforge.ganttproject.GanttGraphicArea;

/**
 * @author athomas Convert the color format from and to differents versions
 */
public class ColorConvertion {

    /** @return the color as hexadecimal version like #RRGGBB */
    public static String getColor(Color color) {
        String res = "#";

        if (color.getRed() <= 15)
            res += "0";
        res += Integer.toHexString(color.getRed());
        if (color.getGreen() <= 15)
            res += "0";
        res += Integer.toHexString(color.getGreen());
        if (color.getBlue() <= 15)
            res += "0";
        res += Integer.toHexString(color.getBlue());

        return res;
    }

    /** parse a string as hew and return the corresponding color. */
    public static Color determineColor(String hexString) {
        if (!Pattern.matches("#[0-9abcdefABCDEF]{6}+", hexString)) {
            return GanttGraphicArea.taskDefaultColor;
        }
        int r, g, b;
        r = Integer.valueOf(hexString.substring(1, 3), 16).intValue();
        g = Integer.valueOf(hexString.substring(3, 5), 16).intValue();
        b = Integer.valueOf(hexString.substring(5, 7), 16).intValue();
        return new Color(r, g, b);
    }
}
