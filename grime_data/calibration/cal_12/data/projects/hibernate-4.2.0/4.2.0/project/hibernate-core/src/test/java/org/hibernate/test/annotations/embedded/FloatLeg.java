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
package org.hibernate.test.annotations.embedded;
import java.text.NumberFormat;
import javax.persistence.Embeddable;

/**
 * Represents floating part of Interest Rate Swap cash flows.
 */
@Embeddable
public class FloatLeg extends Leg {

	/**
	 * Possible values for the rate index.
	 */
	public enum RateIndex {
		LIBOR, EURIBOR, TIBOR}

	;

	private RateIndex rateIndex;

	/**
	 * Spread over the selected rate index (in basis points).
	 */
	private double rateSpread;

	public RateIndex getRateIndex() {
		return rateIndex;
	}

	public void setRateIndex(RateIndex rateIndex) {
		this.rateIndex = rateIndex;
	}

	public double getRateSpread() {
		return rateSpread;
	}

	public void setRateSpread(double rateSpread) {
		this.rateSpread = rateSpread;
	}

	public String toString() {
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMinimumFractionDigits( 1 );
		format.setMaximumFractionDigits( 1 );
		return "[" + getRateIndex().toString() + "+" + format.format( getRateSpread() ) + "]";
	}
}
