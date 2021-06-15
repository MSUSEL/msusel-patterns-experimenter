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
package org.hibernate.test.cache.infinispan.stress.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Person {

   @Id
   @GeneratedValue
   private int id;
   private String firstName;
   @ManyToOne
   private Family family;
   private Date birthDate;
   @ManyToOne
   private Address address;
   private boolean checked;
   private int version;

   public Person(String firstName, Family family) {
      this.firstName = firstName;
      this.family = family;
      this.birthDate = null;
      this.address = null;
      this.checked = false;
      this.id = 0;
      this.version = 0;
      this.family.addMember(this);
   }

   protected Person() {
      this.firstName = null;
      this.family = null;
      this.birthDate = null;
      this.address = null;
      this.checked = false;
      this.id = 0;
      this.version = 0;
   }

   public String getFirstName() {
      return firstName;
   }

   public Family getFamily() {
      return family;
   }

   public Date getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(Date birthDate) {
      this.birthDate = birthDate;
   }

   public Address getAddress() {
      return address;
   }

   public void setAddress(Address address) {
      // To skip Hibernate BUG with access.PROPERTY : the rest should be done in DAO
      //		this.address = address;
      // Hibernate BUG : if we update the relation on 2 sides
      if (this.address != address) {
         if (this.address != null) this.address.remInhabitant(this);
         this.address = address;
         if (this.address != null) this.address.addInhabitant(this);
      }
   }

   public boolean isChecked() {
      return checked;
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getVersion() {
      return version;
   }

   protected void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   protected void setFamily(Family family) {
      this.family = family;
   }

   protected void setVersion(Integer version) {
      this.version = version;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Person person = (Person) o;

      if (checked != person.checked) return false;
      if (id != person.id) return false;
      if (version != person.version) return false;
      if (address != null ? !address.equals(person.address) : person.address != null)
         return false;
      if (birthDate != null ? !birthDate.equals(person.birthDate) : person.birthDate != null)
         return false;
      if (family != null ? !family.equals(person.family) : person.family != null)
         return false;
      if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null)
         return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = firstName != null ? firstName.hashCode() : 0;
      result = 31 * result + (family != null ? family.hashCode() : 0);
      result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
      result = 31 * result + (address != null ? address.hashCode() : 0);
      result = 31 * result + (checked ? 1 : 0);
      result = 31 * result + id;
      result = 31 * result + version;
      return result;
   }

   @Override
   public String toString() {
      return "Person{" +
            "address=" + address +
            ", firstName='" + firstName + '\'' +
            ", family=" + family +
            ", birthDate=" + birthDate +
            ", checked=" + checked +
            ", id=" + id +
            ", version=" + version +
            '}';
   }

}
