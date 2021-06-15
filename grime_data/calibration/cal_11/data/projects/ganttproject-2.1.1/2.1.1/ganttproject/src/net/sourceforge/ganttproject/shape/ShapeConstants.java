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
package net.sourceforge.ganttproject.shape;

/*
 *@author Etienne L'kenfack (etienne.lkenfack@itcogita.com)
 */

public class ShapeConstants {
    public static final ShapePaint TRANSPARENT = new ShapePaint(4, 4,
            new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });

    public static final ShapePaint DEFAULT = new ShapePaint(4, 4, new int[] {
            1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1 });

    public static final ShapePaint CROSS = new ShapePaint(4, 4, new int[] { 0,
            1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0 });

    public static final ShapePaint VERT = new ShapePaint(4, 4, new int[] { 1,
            0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 });

    public static final ShapePaint HORZ = new ShapePaint(4, 4, new int[] { 0,
            0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1 });

    public static final ShapePaint GRID = new ShapePaint(4, 4, new int[] { 1,
            0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1 });

    public static final ShapePaint ROUND = new ShapePaint(4, 4, new int[] { 0,
            1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 });

    public static final ShapePaint NW_TRIANGLE = new ShapePaint(4, 4,
            new int[] { 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 });

    public static final ShapePaint NE_TRIANGLE = new ShapePaint(4, 4,
            new int[] { 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0 });

    public static final ShapePaint SW_TRIANGLE = new ShapePaint(4, 4,
            new int[] { 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0 });

    public static final ShapePaint SE_TRIANGLE = new ShapePaint(4, 4,
            new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1 });

    public static final ShapePaint DIAMOND = new ShapePaint(4, 4, new int[] {
            0, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 });

    public static final ShapePaint DOTS = new ShapePaint(4, 4, new int[] { 1,
            0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0 });

    public static final ShapePaint DOT = new ShapePaint(4, 4, new int[] { 1, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 });

    public static final ShapePaint SLASH = new ShapePaint(4, 4, new int[] { 1,
            0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });

    public static final ShapePaint BACKSLASH = new ShapePaint(4, 4, new int[] {
            0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0 });

    public static final ShapePaint THICK_VERT = new ShapePaint(4, 4, new int[] {
            0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0 });

    public static final ShapePaint THICK_HORZ = new ShapePaint(4, 4, new int[] {
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 });

    public static final ShapePaint THICK_GRID = new ShapePaint(4, 4, new int[] {
            0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 });

    public static final ShapePaint THICK_SLASH = new ShapePaint(4, 4,
            new int[] { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1 });

    public static final ShapePaint THICK_BACKSLASH = new ShapePaint(4, 4,
            new int[] { 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1 });

    public static ShapePaint[] PATTERN_LIST = { TRANSPARENT, DEFAULT, CROSS,
            VERT, HORZ, GRID, ROUND, NW_TRIANGLE, NE_TRIANGLE, SW_TRIANGLE,
            SE_TRIANGLE, DIAMOND, DOTS, DOT, SLASH, BACKSLASH, THICK_VERT,
            THICK_HORZ, THICK_GRID, THICK_SLASH, THICK_BACKSLASH };
}
