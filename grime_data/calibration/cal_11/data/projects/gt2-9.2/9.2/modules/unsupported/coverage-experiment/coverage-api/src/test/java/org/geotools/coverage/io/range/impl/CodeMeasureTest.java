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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import junit.framework.Assert;

import org.geotools.coverage.io.impl.range.CodeMeasure;
import org.geotools.coverage.io.impl.range.DefaultAxis;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultLinearCS;
import org.geotools.referencing.datum.DefaultEngineeringDatum;
import org.geotools.util.SimpleInternationalString;
import org.junit.Test;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.EngineeringDatum;
import org.opengis.util.CodeList;
/**
 * @author Simone Giannecchini, GeoSolutions
 *
 *
 *
 *
 * @source $URL$
 */
public class CodeMeasureTest extends Assert {
	
	/** Bands captured as an codelist used as an example below */
	private static List<Code> codes = new ArrayList<Code>();	
	static class Code extends CodeList<Code> {
		private static final long serialVersionUID = -1103556579647561536L;
		
		public Code(String name) {
			super(name, codes );
		}
		
		public Code[] family() {
			return codes.toArray(new Code[codes.size()]);
		}				
	}
	static {			
		new Code("BLUE");
		new Code("GREEN");
		new Code("RED");
		new Code("NIR");
		new Code("SWIT");
		new Code("TIR");
		new Code("SWR2");
	}	
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
		SingleCRS lightCRS = new DefaultEngineeringCRS("wave length", lightDatum, lightCS );
		
		List<Measure<Code,Dimensionless>> keys = CodeMeasure.valueOf( codes );
		
		DefaultAxis<Code,Dimensionless> axis =
			new DefaultAxis<Code,Dimensionless>(
					new NameImpl("Bands"),
					new SimpleInternationalString("Expressed in wavelengths"),
					keys,
					Unit.ONE,
					lightCRS );

		assertEquals( lightCRS, axis.getCoordinateReferenceSystem() );
		assertEquals( 7, axis.getKeys().size() );
	}

}
