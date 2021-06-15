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
package org.hibernate.test.ondemandload;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Inventory {
	private int id = -1;
	private Store store;
	private Product product;
	private Long quantity;
	private BigDecimal storePrice;

	public Inventory() {
	}

	public Inventory(Store store, Product product) {
		this.store = store;
		this.product = product;
	}

	@Id
	@GeneratedValue
	@GenericGenerator( name = "increment", strategy = "increment" )
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn( name = "store_id" )
	public Store getStore() {
		return store;
	}

	public Inventory setStore(Store store) {
		this.store = store;
		return this;
	}

	@ManyToOne
	@JoinColumn( name = "prod_id" )
	public Product getProduct() {
		return product;
	}

	public Inventory setProduct(Product product) {
		this.product = product;
		return this;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Inventory setQuantity(Long quantity) {
		this.quantity = quantity;
		return this;
	}

	public BigDecimal getStorePrice() {
		return storePrice;
	}

	public Inventory setStorePrice(BigDecimal storePrice) {
		this.storePrice = storePrice;
		return this;
	}
}
