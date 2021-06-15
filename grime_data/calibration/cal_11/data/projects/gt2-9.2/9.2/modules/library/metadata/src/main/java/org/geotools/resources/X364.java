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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.resources;


/**
 * Escape codes from ANSI X3.64 standard (aka ECMA-48 and ISO/IEC 6429).
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see http://en.wikipedia.org/wiki/ANSI_escape_code
 */
public final class X364 {
    /** Do not allows instantiation of this class. */
    private X364() {
    }

    /** The espace sequence.      */ private static final String ESCAPE  = "\u001B[";
    /** Reset all attributes off. */ public  static final String RESET   = ESCAPE +  "0m";
    /** Red foreground.           */ public  static final String RED     = ESCAPE + "31m";
    /** Green foreground.         */ public  static final String GREEN   = ESCAPE + "32m";
    /** Yellow foreground.        */ public  static final String YELLOW  = ESCAPE + "33m";
    /** Blue foreground.          */ public  static final String BLUE    = ESCAPE + "34m";
    /** Magenta foreground.       */ public  static final String MAGENTA = ESCAPE + "35m";
    /** Cyan foreground.          */ public  static final String CYAN    = ESCAPE + "36m";
    /** Default foreground.       */ public  static final String DEFAULT = ESCAPE + "39m";

    /** Red background.     */ public static final String BACKGROUND_RED     = ESCAPE + "41m";
    /** Default background. */ public static final String BACKGROUND_DEFAULT = ESCAPE + "49m";
}
