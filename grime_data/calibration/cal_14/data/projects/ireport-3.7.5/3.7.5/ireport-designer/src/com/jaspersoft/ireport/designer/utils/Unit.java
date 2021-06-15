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
package com.jaspersoft.ireport.designer.utils;

import java.util.HashMap;

/**
 *
 * @author gtoffoli
 */
public class Unit {

    public static final double PIXEL = 1.0;
    public static final double INCHES = 72.0;
    public static final double CENTIMETERS = 28.3464;
    public static final double MILLIMETERS = 2.83464;

    /** Holds value of property unitName. */
    private String unitName;
    private String keyName;

    /** Holds value of property conversionValue. */
    private double conversionValue;

    /** Creates a new instance of Unit */
    public Unit(String unitName, double conversionValue) {
        this(unitName, conversionValue, unitName);
    }
    
    public Unit(String unitName, double conversionValue, String keyName) {
        this.unitName = unitName;
        this.conversionValue = conversionValue;
        this.setKeyName(keyName);
    }

    /** Getter for property unitName.
     * @return Value of property unitName.
     *
     */
    public String getUnitName() {
        return this.unitName;
    }

    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     *
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /** Getter for property conversionValue.
     * @return Value of property conversionValue.
     *
     */
    public double getConversionValue() {
        return this.conversionValue;
    }

    /** Setter for property conversionValue.
     * @param conversionValue New value of property conversionValue.
     *
     */
    public void setConversionValue(double conversionValue) {
        this.conversionValue = conversionValue;
    }

    private static Unit[] units = null;
    
    private static HashMap<String,Unit> unitsMap = null;
    
    public static Unit[] getStandardUnits()
    {
        if (units == null)
        {
            unitsMap = new HashMap<String,Unit>();
            units = new Unit[4];
            units[0] = new Unit("pixels",Unit.PIXEL, "pixels");  
            unitsMap.put("pixels", units[0]);
            units[1] = new Unit("inches",Unit.INCHES, "inches");
            unitsMap.put("inches", units[1]);
            units[2] = new Unit("cm",    Unit.CENTIMETERS, "cm");
            unitsMap.put("cm", units[2]);
            units[3] = new Unit("mm",    Unit.MILLIMETERS, "mm");
            unitsMap.put("mm", units[3]);
        }
        return units;
    }

    public static int getUnitIndex(String unitName)
    {
        Unit[] units = getStandardUnits();
        for (int i=0; i<units.length; ++i)
        {
            if (units[i].getUnitName().equalsIgnoreCase(unitName)) return i;
        }
        return -1;
    }

    public String toString()
    {
        return getUnitName();
    }

    /**
     * convert from pixel to the unit represented by this class
     *
     * @param pixel
     * @return
     */
    public double convert(int pixels)
    {
        return ((double)pixels)/conversionValue;
    }

    /**
     * convert from pixel to the unit represented by this class
     *
     * @param pixel
     * @return
     */
    public long toPixel(double val)
    {
        return (long)(val*conversionValue);
    }

    static public double convertPixelsToInches(long pixels)
	{
		return ((double)pixels)/INCHES;
	}

	static public long convertInchesToPixels(double inches)
	{
		return (long)(inches*INCHES);
	}

	static public double convertPixelsToCentimeters(long pixels)
	{
		return ((double)pixels)/CENTIMETERS;
	}

	static public long convertCentimetersToPixels(double centimeters)
	{
		return (long)(centimeters*CENTIMETERS);
	}

	static public double convertPixelsToMillimeters(long pixels)
	{
		return ((double)pixels)/MILLIMETERS;
	}

	static public long convertMillimetersToPixels(double millimeters)
	{
		return (long)(millimeters*CENTIMETERS);
	}

	static public long convertToPixels(double value, double convert)
	{
		return (long)(value*convert);
	}
        
        static public Unit getUnit(String unitName)
	{
            if (unitsMap == null) getStandardUnits();
            return unitsMap.get(unitName);
        }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
