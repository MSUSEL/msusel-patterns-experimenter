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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Interest Rate Swap with Tenor. Used here to compose
 * a swap spread deal.
 */
@Embeddable
public class Swap {

	/**
	 * Tenor (duration) of the swap (in years).
	 */
	private int tenor;

	public int getTenor() {
		return tenor;
	}

	public void setTenor(int tenor) {
		this.tenor = tenor;
	}

	/**
	 * Fixed leg (cash flows with the fixed rate).
	 */
	private FixedLeg fixedLeg;

	/**
	 * Floating leg (cash flows bound to a financial index).
	 */
	private FloatLeg floatLeg;

	@Embedded
	// We retain this annotation to test the precedence of @AttributeOverride
	// Outermost override annotation should win
	@AttributeOverride(name = "paymentFrequency", column = @Column(name = "FIXED_FREQENCY"))
	public FixedLeg getFixedLeg() {
		return fixedLeg;
	}

	public void setFixedLeg(FixedLeg fixedLeg) {
		this.fixedLeg = fixedLeg;
	}

	@Embedded
	public FloatLeg getFloatLeg() {
		return floatLeg;
	}

	public void setFloatLeg(FloatLeg floatLeg) {
		this.floatLeg = floatLeg;
	}
}
