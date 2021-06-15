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
//$Id: LineItem.java 4806 2004-11-25 14:37:00Z steveebersole $
package org.hibernate.test.cid;
import java.io.Serializable;

/**
 * @author Gavin King
 */
public class LineItem {
	public static class Id implements Serializable {
		private String customerId;
		private int orderNumber;
		private String productId;

		public Id(String customerId, int orderNumber, String productId) {
			this.customerId = customerId;
			this.orderNumber = orderNumber;
			this.productId = productId;
		}
		public Id() {}

		/**
		 * @return Returns the customerId.
		 */
		public String getCustomerId() {
			return customerId;
		}
		/**
		 * @param customerId The customerId to set.
		 */
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
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
		 * @return Returns the orderNumber.
		 */
		public int getOrderNumber() {
			return orderNumber;
		}
		/**
		 * @param orderNumber The orderNumber to set.
		 */
		public void setOrderNumber(int orderNumber) {
			this.orderNumber = orderNumber;
		}
		public int hashCode() {
			return customerId.hashCode() + orderNumber + productId.hashCode();
		}
		public boolean equals(Object other) {
			if (other instanceof Id) {
				Id that = (Id) other;
				return that.customerId.equals(this.customerId) &&
					that.productId.equals(this.productId) &&
					that.orderNumber == this.orderNumber;
			}
			else {
				return false;
			}
		}
	}

	private Id id = new Id();
	private int quantity;
	private Order order;
	private Product product;

	public LineItem(Order o, Product p) {
		this.order = o;
		this.id.orderNumber = o.getId().getOrderNumber();
		this.id.customerId = o.getId().getCustomerId();
		this.id.productId = p.getProductId();
		o.getLineItems().add(this);
	}

	public LineItem() {}

	/**
	 * @return Returns the order.
	 */
	public Order getOrder() {
		return order;
	}
	/**
	 * @param order The order to set.
	 */
	public void setOrder(Order order) {
		this.order = order;
	}
	/**
	 * @return Returns the product.
	 */
	public Product getProduct() {
		return product;
	}
	/**
	 * @param product The product to set.
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	/**
	 * @return Returns the quantity.
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
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
}
