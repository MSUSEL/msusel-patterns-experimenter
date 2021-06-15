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

import org.geotools.factory.Hints;

/**
 * ConverterFactory for handling boolean conversions.
 * <p>
 * Supported conversions:
 * <ul>
 * 	<li>"true" -> Boolean.TRUE
 * 	<li>"false" -> Boolean.FALSE
 * 	<li>"1" -> Boolean.TRUE
 *  <li>"0" -> Boolean.FALSE
 *  <li>1 -> Boolean.TRUE
 *  <li>0 -> Boolean.FALSE 
 * </ul>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.4
 *
 *
 *
 * @source $URL$
 */
public class BooleanConverterFactory implements ConverterFactory {

	public Converter createConverter(Class source, Class target, Hints hints) {
		if ( target.equals( Boolean.class ) ) {
			
			//string to boolean
			if ( source.equals( String.class ) ) {
				return new Converter() {

					public Object convert(Object source, Class target) throws Exception {
						if ( "true".equals( source ) || "1".equals( source ) ) {
							return Boolean.TRUE;
						}
						if ( "false".equals( source ) || "0".equals( source ) ) {
							return Boolean.FALSE;
						}
						
						return null;
					}
					
				};
			}
			
			//integer to boolean
			if ( source.equals( Integer.class ) ) {
				return new Converter() {

					public Object convert(Object source, Class target) throws Exception {
						if ( new Integer( 1 ).equals( source ) ) {
							return Boolean.TRUE;
						}
						if ( new Integer( 0 ).equals( source ) ) {
							return Boolean.FALSE;
						}
						
						return null;
					}
					
				};
			}
			
		}
		
		return null;
	}

}
