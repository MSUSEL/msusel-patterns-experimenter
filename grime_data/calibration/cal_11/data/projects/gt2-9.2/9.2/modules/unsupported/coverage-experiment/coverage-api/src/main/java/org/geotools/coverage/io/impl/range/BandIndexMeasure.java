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

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.converter.ConversionException;
import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

/**
 * {@link Measurable} subclass suitable for modeling a band index in a multiband
 * image axis.
 * 
 * <p>
 * The band index is a measurement of a {@link Dimensionless} quantity since it
 * is just a convenience ordering.
 * 
 * @author Simone Giannecchini, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public class BandIndexMeasure extends Measure<String, Dimensionless>
		implements Measurable<Dimensionless> {
	private static final long serialVersionUID = 3895010709415779953L;

	private Long index = null;

	private String bandMnemonic = null;

	public BandIndexMeasure(final int index, final String bandMnemonic) {
		this.index = (long) index;
		this.bandMnemonic = bandMnemonic != null ? bandMnemonic : Long
				.toString(this.index);
	}

	public double doubleValue(Unit<Dimensionless> value) {
		return index;
	}

	public long longValue(Unit<Dimensionless> value) throws ArithmeticException {
		return index;
	}

	public int compareTo(Measurable<Dimensionless> o) {
		return this.index.compareTo(o.longValue(Unit.ONE));
	}

	@Override
	public Unit<Dimensionless> getUnit() {
		return Unit.ONE;
	}

	@Override
	public String getValue() {
		return this.bandMnemonic;
	}

	@Override
	public Measure<String, Dimensionless> to(Unit<Dimensionless> target) {
		if (target.isCompatible(Unit.ONE))
			return new BandIndexMeasure(this.index.intValue(),
					this.bandMnemonic);
		final StringBuilder buffer = new StringBuilder();
		buffer.append("Unable to perform requested conversion");
		buffer.append("\nsource UoM:").append("  ").append(Unit.ONE.toString());
		buffer.append("\ntarget UoM:").append("  ").append(target.toString());
		throw new ConversionException(buffer.toString());

	}
}
