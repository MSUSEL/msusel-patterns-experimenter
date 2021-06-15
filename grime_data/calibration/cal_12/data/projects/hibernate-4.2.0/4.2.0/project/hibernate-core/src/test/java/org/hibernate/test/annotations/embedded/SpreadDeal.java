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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

/**
 * Represents a "curve spread" deal that consists of
 * two Interest Rate Swaps with different tenors (short and long).
 * For simplicity, tenors are not considered here.
 */
@Entity
@AttributeOverrides(value = {
		@AttributeOverride(name = "swap.tenor", column = @Column(name = "MEDIUM_TENOR")),
		@AttributeOverride(name = "swap.fixedLeg.paymentFrequency", column = @Column(name = "MEDIUM_FIXED_FREQUENCY")),
		@AttributeOverride(name = "swap.fixedLeg.rate", column = @Column(name = "MEDIUM_FIXED_RATE")),
		@AttributeOverride(name = "swap.floatLeg.paymentFrequency", column = @Column(name = "MEDIUM_FLOAT_FREQUENCY")),
		@AttributeOverride(name = "swap.floatLeg.rateIndex", column = @Column(name = "MEDIUM_FLOAT_RATEINDEX")),
		@AttributeOverride(name = "swap.floatLeg.rateSpread", column = @Column(name = "MEDIUM_FLOAT_RATESPREAD"))
})
public class SpreadDeal extends NotonialDeal {

	/**
	 * Swap with the tenor.
	 */
	private Swap longSwap;

	@Embedded
	public Swap getLongSwap() {
		return longSwap;
	}

	public void setLongSwap(Swap swap) {
		this.longSwap = swap;
	}


	/**
	 * Swap with the longer tenor.
	 */
	private Swap shortSwap;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name = "tenor", column = @Column(name = "SHORT_TENOR")),
			@AttributeOverride(name = "fixedLeg.paymentFrequency", column = @Column(name = "SHORT_FIXED_FREQUENCY")),
			@AttributeOverride(name = "fixedLeg.rate", column = @Column(name = "SHORT_FIXED_RATE")),
			@AttributeOverride(name = "floatLeg.paymentFrequency", column = @Column(name = "SHORT_FLOAT_FREQUENCY")),
			@AttributeOverride(name = "floatLeg.rateIndex", column = @Column(name = "SHORT_FLOAT_RATEINDEX")),
			@AttributeOverride(name = "floatLeg.rateSpread", column = @Column(name = "SHORT_FLOAT_RATESPREAD"))
	})
	public Swap getShortSwap() {
		return shortSwap;
	}

	public void setShortSwap(Swap shortSwap) {
		this.shortSwap = shortSwap;
	}
}
