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
package net.sourceforge.ganttproject;

import java.util.GregorianCalendar;
import java.util.List;

import org.w3c.util.DateParser;
import org.w3c.util.InvalidDateException;

public interface CustomPropertyManager {
	List/*<CustomPropertyDefinitions>*/ getDefinitions();
	CustomPropertyDefinition createDefinition(String id, String typeAsString, String name, String defaultValueAsString);
	void importData(CustomPropertyManager source);

	class PropertyTypeEncoder {
	    public static String encodeFieldType(Class fieldType) {
	    	String result = null;
	        if (fieldType.equals(String.class)) {
	            result = "text";
	        }
	        else if (fieldType.equals(Boolean.class)) {
	            result = "boolean";
	        }
	        else if (fieldType.equals(Integer.class)) {
	            result = "int";
	        }
	        else if (fieldType.equals(Double.class)) {
	            result = "double";
	        }
	        else if (fieldType.isAssignableFrom(GregorianCalendar.class)) {
	            result = "date";
	        }
	        return result;
	    }

		public static CustomPropertyDefinition decodeTypeAndDefaultValue(final String typeAsString, final String valueAsString) {
			final Class type;
			final Object defaultValue;
            if (typeAsString.equals("text")) {
                type = String.class;
                defaultValue = valueAsString==null ? null : valueAsString.toString();
            } else if (typeAsString.equals("boolean")) {
                type = Boolean.class;
                defaultValue = valueAsString==null ? null : Boolean.valueOf(valueAsString);
            } else if (typeAsString.equals("int")) {
                type = Integer.class;
                defaultValue = valueAsString==null ? null : Integer.valueOf(valueAsString);
            } else if (typeAsString.equals("double")) {
                type = Double.class;
                defaultValue = valueAsString==null ? null : Double.valueOf(valueAsString);
            } else if (typeAsString.equals("date")) {
                type = GregorianCalendar.class;
                if (valueAsString!=null) {
	                GanttCalendar c = null;
	                try {
	                    c = new GanttCalendar(DateParser.parse(valueAsString));
	                } catch (InvalidDateException e) {
	                	if (!GPLogger.log(e)) {
	                		e.printStackTrace(System.err);
	                	}
	                }
	                defaultValue = c;
                }
                else {
                	defaultValue = null;
                }
            } else {
                type = String.class; // TODO remove if(...text)
                defaultValue = "";
            }
            return new CustomPropertyDefinition() {
				public Object getDefaultValue() {
					return defaultValue;
				}
				public String getDefaultValueAsString() {
					return valueAsString;
				}
				public String getID() {
					return null;
				}
				public String getName() {
					return null;
				}
				public Class getType() {
					return type;
				}
				public String getTypeAsString() {
					return typeAsString;
				}
            };
		}

	}
}
