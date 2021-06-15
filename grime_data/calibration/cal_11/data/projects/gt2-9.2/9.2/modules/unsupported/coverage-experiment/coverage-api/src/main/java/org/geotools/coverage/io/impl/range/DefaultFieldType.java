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
package org.geotools.coverage.io.impl.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.measure.Measure;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.geotools.coverage.io.range.Axis;
import org.geotools.coverage.io.range.FieldType;
import org.opengis.coverage.SampleDimension;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * 
 *
 * @source $URL$
 */
public class DefaultFieldType implements FieldType {
	private List<Axis<?, ?>> axes;
	private Name name;
	private InternationalString description;
	private Unit<Quantity> unit;
	private Set<SampleDimension> sampleDimensions;
	/**
	 * 
	 * @param name
	 * @param description
	 * @param unit
	 * @param axes
	 * @param samples
	 */
	public DefaultFieldType(Name name,
			InternationalString description,
			Unit<?> unit,
			List<Axis<?,?>> axes,
			Set<SampleDimension> samples) {
		this.name = name;
		this.description = description;
		this.axes = axes;
		this.sampleDimensions = samples;
	}

	public List<Axis<?,?>> getAxes() {
		return Collections.unmodifiableList(axes);
	}

	public List<Name> getAxesNames() {
		List<Name> list = new ArrayList<Name>();
		for( Axis<?,?> axis : axes ){
			list.add( axis.getName() );
		}
		return list;
	}

	public Axis<?,?> getAxis(Name name) {
		for( Axis<?,?> axis : axes ){
			if( name.equals( axis.getName() )){
				return axis;
			}
		}
		return null;
	}

	public InternationalString getDescription() {
		return description;
	}

	public Name getName() {
		return name;
	}

	public SampleDimension getSampleDimension(Measure<?,?> key) {
		return null; // TODO: need to figure out how to record this association
	}

	public Set<SampleDimension> getSampleDimensions() {
	    if (sampleDimensions!=null)
		return Collections.unmodifiableSet( sampleDimensions );
	    return Collections.emptySet();
	}
	
	/** Unit type for this field */
	public Unit<Quantity> getUnitOfMeasure() {
		return unit; // TODO Is this duplicated with sample dimensions ?
	}

}
