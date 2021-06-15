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
//$Id: Product.java 4806 2004-11-25 14:37:00Z steveebersole $
package org.hibernate.test.cid;
import java.math.BigDecimal;

/**
 * @author Gavin King
 */
public class Product {
	private String productId;
	private String description;
	private BigDecimal price;
	private int numberAvailable;
	private int numberOrdered;
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the numberAvailable.
	 */
	public int getNumberAvailable() {
		return numberAvailable;
	}
	/**
	 * @param numberAvailable The numberAvailable to set.
	 */
	public void setNumberAvailable(int numberAvailable) {
		this.numberAvailable = numberAvailable;
	}
	/**
	 * @return Returns the numberOrdered.
	 */
	public int getNumberOrdered() {
		return numberOrdered;
	}
	/**
	 * @param numberOrdered The numberOrdered to set.
	 */
	public void setNumberOrdered(int numberOrdered) {
		this.numberOrdered = numberOrdered;
	}
	/**
	 * @return Returns the productId.
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return Returns the price.
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price The price to set.
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
