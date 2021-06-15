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
package org.hibernate.test.cid;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jacob Robertson
 */
public class PurchaseRecord {
	public static class Id implements Serializable {
		private int purchaseNumber;
		private String purchaseSequence;

		public Id(int purchaseNumber, String purchaseSequence) {
			this.purchaseNumber = purchaseNumber;
			this.purchaseSequence = purchaseSequence;
		}
		public Id() {}

		/**
		 * @return Returns the purchaseNumber.
		 */
		public int getPurchaseNumber() {
			return purchaseNumber;
		}
		/**
		 * @param purchaseNumber The purchaseNumber to set.
		 */
		public void setPurchaseNumber(int purchaseNumber) {
			this.purchaseNumber = purchaseNumber;
		}
		/**
		 * @return the purchaseSequence
		 */
		public String getPurchaseSequence() {
			return purchaseSequence;
		}
		/**
		 * @param purchaseSequence the purchaseSequence to set
		 */
		public void setPurchaseSequence(String purchaseSequence) {
			this.purchaseSequence = purchaseSequence;
		}
		public int hashCode() {
			return purchaseNumber + purchaseSequence.hashCode();
		}
		public boolean equals(Object other) {
			if (other instanceof Id) {
				Id that = (Id) other;
				return purchaseSequence.equals(this.purchaseSequence) &&
					that.purchaseNumber == this.purchaseNumber;
			}
			else {
				return false;
			}
		}
	}

	private Id id;
	private Date timestamp = new Date();
	private Set details = new HashSet();

	public PurchaseRecord() {}

	/**
	 * @return Returns the id.
	 */
	public Id getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Id id) {
		this.id = id;
	}

	/**
	 * @return the details
	 */
	public Set getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(Set details) {
		this.details = details;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
