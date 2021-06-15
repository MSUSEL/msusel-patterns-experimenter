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
/**
 * 
 */
package org.geotools.coverage.io.range.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import junit.framework.Assert;

import org.geotools.coverage.io.impl.range.DefaultAxis;
import org.geotools.coverage.io.impl.range.EnumMeasure;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultLinearCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.util.SimpleInternationalString;
import org.opengis.coverage.SampleDimension;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.EngineeringDatum;
import org.junit.Test;
/**
 * @author Simone Giannecchini, GeoSolutions
 *
 *
 *
 *
 * @source $URL$
 */
public class EnumMeasureTest extends Assert {
	
	/** Bands captured as an enumeration used as an example below */
	enum Band {
		BLUE,GREE,RED,NIR,SWIT,TIR,SWR2
	};
	
	/**
	 * This test uses use the default implementations
	 * to express 7 bands of a landsat image.
	 */
	@Test
	public void testLandsatAxis(){
		CoordinateSystemAxis csAxis = new DefaultCoordinateSystemAxis(
			new SimpleInternationalString("light"),
			"light",
			AxisDirection.OTHER,
			SI.MICRO(SI.METER));
		
		DefaultLinearCS lightCS = new DefaultLinearCS("light",csAxis);
		Map<String,Object> datumProperties = new HashMap<String,Object>();
		datumProperties.put("name", "light");
		
		EngineeringDatum lightDatum = new DefaultEngineeringDatum( datumProperties );		
		DefaultEngineeringCRS lightCRS = new DefaultEngineeringCRS("wave length", lightDatum, lightCS );
		
		List<Measure<Band, Dimensionless>> keys = EnumMeasure.valueOf( Band.class );
		
		DefaultAxis<Band, Dimensionless> axis =
			new DefaultAxis<Band,Dimensionless>(
				new NameImpl("Bands"),
				new SimpleInternationalString("Landsat bands by wavelength"),
				keys,
				Unit.ONE );
		
		Map<Measure<Integer,Dimensionless>,SampleDimension> samples = new HashMap<Measure<Integer,Dimensionless>, SampleDimension>();
	}

}
