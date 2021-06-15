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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.awt.Color;

import org.geotools.factory.Hints;

/**
 * ConverterFactory for handling color conversions.
 * <p>
 * Supported conversions:
 * <ul>
 * 	<li>"#FF0000" (String) -> Color.RED
 * 	<li>"false" -> Boolean.FALSE
 * 	<li>0xFF0000FF (Integer) -> RED with Alpha
 * </ul>
 * </p>
 * <p>
 * This code was previously part of the SLD utility class, it is being made
 * available as part of the Converters framework to allow for broader use.
 * </p>
 * @author Jody Garnett (Refractions Research)
 * @since 2.5
 *
 *
 *
 * @source $URL$
 */
public class ColorConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        if (target.equals(Color.class)) {
            // string to color
            if (source.equals(String.class)) {
                return new Converter() {
                    public Object convert(Object source, Class target) throws Exception {
                        String rgba = (String) source;
                        try {
                            return Color.decode(rgba);
                        } catch (NumberFormatException badRGB) {
                            // unavailable
                            return null;
                        }
                    }
                };
            }

            // can we convert the thing to a Integer with a safe conversion?
            if (Number.class.isAssignableFrom(source)) {
                return new Converter() {
                    public Object convert(Object source, Class target) throws Exception {
                        Number number = (Number) source;
                        // is it an integral number, and small enough to be an integer?
                        if (((int) number.doubleValue()) == number.doubleValue() && number.doubleValue() < Integer.MAX_VALUE) {
                            int rgba = number.intValue();
                            int alpha = 0xff000000 & rgba;
                            return new Color(rgba, alpha != 0);
                        } else {
                            return null;
                        }
                    }
                };
            }
        } else if (target.equals(String.class) && source.equals(Color.class)) {
            return new Converter() {
                
                public <T> T convert(Object source, Class<T> target) throws Exception {
                    Color color = (Color) source;
                    
                    String redCode = Integer.toHexString(color.getRed());
                    String greenCode = Integer.toHexString(color.getGreen());
                    String blueCode = Integer.toHexString(color.getBlue());

                    if (redCode.length() == 1) {
                        redCode = "0" + redCode;
                    }

                    if (greenCode.length() == 1) {
                        greenCode = "0" + greenCode;
                    }

                    if (blueCode.length() == 1) {
                        blueCode = "0" + blueCode;
                    }

                    return (T) ("#" + redCode + greenCode + blueCode).toUpperCase();
                }
            };
		}
		return null;
	}

}
