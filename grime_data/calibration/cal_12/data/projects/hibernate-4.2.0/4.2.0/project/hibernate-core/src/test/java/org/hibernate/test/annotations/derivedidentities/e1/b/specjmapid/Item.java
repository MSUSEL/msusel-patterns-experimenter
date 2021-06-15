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
package org.hibernate.test.annotations.derivedidentities.e1.b.specjmapid;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


@NamedQueries({
		@NamedQuery(name = "Item.findByCategory",
				query = "SELECT i FROM Item i WHERE i.category=:category ORDER BY i.id")
})
@SuppressWarnings("serial")
@Entity
@Table(name = "O_ITEM")
public class Item implements Serializable {

	public static final String QUERY_BY_CATEGORY = "Item.findByCategory";

	@Id
	@Column(name = "I_ID")
	private String id;

	@Column(name = "I_NAME")
	private String name;

	@Column(name = "I_PRICE")
	private BigDecimal price;

	@Column(name = "I_DESC")
	private String description;

	@Column(name = "I_DISCOUNT")
	private BigDecimal discount;

	@Column(name = "I_CATEGORY")
	private int category;

	@Version
	@Column(name = "I_VERSION")
	int version;

	public String getId() {
		return id;
	}

	public void setId(String i) {
		id = i;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		if ( discount.doubleValue() < 0 || discount.doubleValue() > 100.0 ) {
			throw new IllegalArgumentException(
					this + " discount " + discount
							+ " is invalid. Must be between 0.0 and 100.0"
			);
		}
		this.discount = discount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public boolean equals(Object other) {
		if ( other == null || other.getClass() != this.getClass() ) {
			return false;
		}
		if ( other == this ) {
			return true;
		}
		return id.equals( ( ( Item ) other ).id );
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
