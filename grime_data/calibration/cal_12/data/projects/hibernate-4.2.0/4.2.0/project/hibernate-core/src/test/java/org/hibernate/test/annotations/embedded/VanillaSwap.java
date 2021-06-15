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
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Represents an Interest Rate Swap.
 */
@Entity
public class VanillaSwap {

	/**
	 * Possible values for the currency field.
	 */
	public enum Currency {
		USD, GBP, EUR, JPY }

	/**
	 * Identifier of the Interest Rate Swap
	 */
	private String instrumentId;

	/**
	 * Currency of the swap (and of both legs).
	 */
	private Currency currency;

	/**
	 * Fixed leg (cash flows with the fixed rate).
	 */
	private FixedLeg fixedLeg;

	/**
	 * Floating leg (cash flows bound to a financial index).
	 */
	private FloatLeg floatLeg;

	@Embedded
	@AttributeOverride(name = "paymentFrequency", column = @Column(name = "FIXED_FREQENCY"))
	public FixedLeg getFixedLeg() {
		return fixedLeg;
	}

	public void setFixedLeg(FixedLeg fixedLeg) {
		this.fixedLeg = fixedLeg;
	}

	@Embedded
	@AttributeOverride(name = "paymentFrequency", column = @Column(name = "FLOAT_FREQUENCY"))
	public FloatLeg getFloatLeg() {
		return floatLeg;
	}

	public void setFloatLeg(FloatLeg floatLeg) {
		this.floatLeg = floatLeg;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Id
	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}
}
