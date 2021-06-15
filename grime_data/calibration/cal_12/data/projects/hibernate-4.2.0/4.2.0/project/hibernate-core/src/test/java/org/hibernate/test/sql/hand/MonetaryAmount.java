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
package org.hibernate.test.sql.hand;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * Represents a monetary amount as value and currency.
 *
 * @author Gavin King <gavin@hibernate.org>
 * @author Christian Bauer <christian@hibernate.org>
 */
public class MonetaryAmount implements Serializable {

	private final BigDecimal value;
	private final Currency currency;

	public MonetaryAmount(BigDecimal value, Currency currency) {
		this.value = value;
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	// ********************** Common Methods ********************** //

	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MonetaryAmount)) return false;

		final MonetaryAmount monetaryAmount = (MonetaryAmount) o;

		if (!currency.equals(monetaryAmount.currency)) return false;
		if (!value.equals(monetaryAmount.value)) return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = value.hashCode();
		result = 29 * result + currency.hashCode();
		return result;
	}

	public String toString() {
		return "Value: '" + getValue() + "', " +
		        "Currency: '" + getCurrency() + "'";
	}

	public int compareTo(Object o) {
		if (o instanceof MonetaryAmount) {
			// TODO: This would actually require some currency conversion magic
			return this.getValue().compareTo(((MonetaryAmount) o).getValue());
		}
		return 0;
	}

	// ********************** Business Methods ********************** //

	public static MonetaryAmount fromString(String amount, String currencyCode) {
		return new MonetaryAmount(new BigDecimal(amount),
								  Currency.getInstance(currencyCode));
	}

	public static MonetaryAmount convert(MonetaryAmount amount,
										 Currency toConcurrency) {
		// TODO: This requires some conversion magic and is therefore broken
		return new MonetaryAmount(amount.getValue(), toConcurrency);
	}

}
