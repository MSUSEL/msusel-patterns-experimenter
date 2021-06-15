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
package org.hibernate.test.annotations.derivedidentities.e1.b.specjmapid.lazy;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.test.annotations.derivedidentities.e1.b.specjmapid.Item;

@NamedQueries({ @NamedQuery(name = "CustomerInventoryTwo.selectAll", query = "select a from CustomerInventoryTwo a") })
@SuppressWarnings("serial")
@Entity
@Table(name = "O_CUSTINVENTORY")
@IdClass(CustomerInventoryTwoPK.class)
public class CustomerInventoryTwo implements Serializable,
      Comparator<CustomerInventoryTwo> {

   @Id
   @TableGenerator(name = "inventory", table = "U_SEQUENCES", pkColumnName = "S_ID", valueColumnName = "S_NEXTNUM", pkColumnValue = "inventory", allocationSize = 1000)
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "inventory")
   @Column(name = "CI_ID")
   private Integer id;

   @Id
   @Column(name = "CI_CUSTOMERID", insertable = false, updatable = false)
   private int custId;

   @ManyToOne(cascade = CascadeType.MERGE)
   @JoinColumn(name = "CI_CUSTOMERID", nullable = false)
   private CustomerTwo customer;

   @ManyToOne(cascade = CascadeType.MERGE)
   @JoinColumn(name = "CI_ITEMID")
   private Item vehicle;

   @Column(name = "CI_VALUE")
   private BigDecimal totalCost;

   @Column(name = "CI_QUANTITY")
   private int quantity;

   @Version
   @Column(name = "CI_VERSION")
   private int version;

   protected CustomerInventoryTwo() {
   }

   CustomerInventoryTwo(CustomerTwo customer, Item vehicle, int quantity,
         BigDecimal totalValue) {
      this.customer = customer;
      this.vehicle = vehicle;
      this.quantity = quantity;
      this.totalCost = totalValue;
   }

   public Item getVehicle() {
      return vehicle;
   }

   public BigDecimal getTotalCost() {
      return totalCost;
   }

   public int getQuantity() {
      return quantity;
   }

   public Integer getId() {
      return id;
   }

   public CustomerTwo getCustomer() {
      return customer;
   }

   public int getCustId() {
      return custId;
   }

   public int getVersion() {
      return version;
   }

   public int compare(CustomerInventoryTwo cdb1, CustomerInventoryTwo cdb2) {
      return cdb1.id.compareTo(cdb2.id);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (obj == null || !(obj instanceof CustomerInventoryTwo)) {
         return false;
      }
      if (this.id == ((CustomerInventoryTwo) obj).id) {
         return true;
      }
      if (this.id != null && ((CustomerInventoryTwo) obj).id == null) {
         return false;
      }
      if (this.id == null && ((CustomerInventoryTwo) obj).id != null) {
         return false;
      }

      return this.id.equals(((CustomerInventoryTwo) obj).id);
   }

}
