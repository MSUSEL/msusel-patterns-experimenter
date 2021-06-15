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
//$
package org.hibernate.test.annotations.idmanytoone;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Embeddable
public class BasketItemsPK implements Serializable {

	private static final long serialVersionUID = 3585214409096105241L;

	public boolean equals(Object aObj) {
		if (aObj == this)
			return true;
		if (!(aObj instanceof BasketItemsPK))
			return false;
		BasketItemsPK basketitemspk = (BasketItemsPK)aObj;
		if (getShoppingBaskets() == null && basketitemspk.getShoppingBaskets() != null)
			return false;
		if (!getShoppingBaskets().equals(basketitemspk.getShoppingBaskets()))
			return false;
		if ((getCost() != null && !getCost().equals(basketitemspk.getCost())) || (getCost() == null && basketitemspk.getCost() != null))
			return false;
		return true;
	}

	public int hashCode() {
		int hashcode = 0;
		if (getShoppingBaskets() != null) {
			hashcode = hashcode + (getShoppingBaskets().getOwner() == null ? 0 : getShoppingBaskets().getOwner().hashCode());
			hashcode = hashcode + (getShoppingBaskets().getBasketDatetime() == null ? 0 : getShoppingBaskets().getBasketDatetime().hashCode());
		}
		hashcode = hashcode + (getCost() == null ? 0 : getCost().hashCode());
		return hashcode;
	}

	@Id
	@ManyToOne(cascade={ CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumns({ @JoinColumn(name="basketDatetime", referencedColumnName="basketDatetime"), @JoinColumn(name="customerID", referencedColumnName="customerID") })
	@Basic(fetch= FetchType.LAZY)
	private ShoppingBaskets shoppingBaskets;

	public void setShoppingBaskets(ShoppingBaskets value)  {
		this.shoppingBaskets =  value;
	}

	public ShoppingBaskets getShoppingBaskets()  {
		return this.shoppingBaskets;
	}

	@Column(name="cost", nullable=false)
	@Id
	private Double cost;

	public void setCost(Double value)  {
		this.cost =  value;
	}

	public Double getCost()  {
		return this.cost;
	}

}

