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
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
public final class Address {

   @Id
   @GeneratedValue
   private int id;
   private int streetNumber;
   private String streetName;
   private String cityName;
   private String countryName;
   private String zipCode;
   @OneToMany
   private Set<Person> inhabitants;
   private int version;

   public Address(int streetNumber, String streetName, String cityName, String countryName) {
      this.streetNumber = streetNumber;
      this.streetName = streetName;
      this.cityName = cityName;
      this.countryName = countryName;
      this.zipCode = null;
      this.inhabitants = new HashSet<Person>();
      this.id = 0;
      this.version = 0;
   }

   protected Address() {
      this.streetNumber = 0;
      this.streetName = null;
      this.cityName = null;
      this.countryName = null;
      this.zipCode = null;
      this.inhabitants = new HashSet<Person>();
      this.id = 0;
      this.version = 0;
   }

   public int getStreetNumber() {
      return streetNumber;
   }

   public String getStreetName() {
      return streetName;
   }

   public String getCityName() {
      return cityName;
   }

   public String getCountryName() {
      return countryName;
   }

   public String getZipCode() {
      return zipCode;
   }

   public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
   }

   public Set<Person> getInhabitants() {
      return inhabitants;
   }

   public boolean addInhabitant(Person inhabitant) {
      boolean done = false;
      if (inhabitants.add(inhabitant)) {
         inhabitant.setAddress(this);
         done = true;
      }
      return done;
   }

   public boolean remInhabitant(Person inhabitant) {
      boolean done = false;
      if (inhabitants.remove(inhabitant)) {
         inhabitant.setAddress(null);
         done = true;
      }
      return done;
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

   protected void removeAllInhabitants() {
      // inhabitants relation is not CASCADED, we must delete the relation on other side by ourselves
      for (Iterator<Person> iterator = inhabitants.iterator(); iterator.hasNext(); ) {
         Person p = iterator.next();
         p.setAddress(null);
      }
   }

   protected void setStreetNumber(int streetNumber) {
      this.streetNumber = streetNumber;
   }

   protected void setStreetName(String streetName) {
      this.streetName = streetName;
   }

   protected void setCityName(String cityName) {
      this.cityName = cityName;
   }

   protected void setCountryName(String countryName) {
      this.countryName = countryName;
   }

   protected void setInhabitants(Set<Person> inhabitants) {
      if (inhabitants == null) {
         this.inhabitants = new HashSet<Person>();
      } else {
         this.inhabitants = inhabitants;
      }
   }

   protected void setVersion(Integer version) {
      this.version = version;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Address address = (Address) o;

      if (id != address.id) return false;
      if (streetNumber != address.streetNumber) return false;
      if (version != address.version) return false;
      if (cityName != null ? !cityName.equals(address.cityName) : address.cityName != null)
         return false;
      if (countryName != null ? !countryName.equals(address.countryName) : address.countryName != null)
         return false;
      if (inhabitants != null ? !inhabitants.equals(address.inhabitants) : address.inhabitants != null)
         return false;
      if (streetName != null ? !streetName.equals(address.streetName) : address.streetName != null)
         return false;
      if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null)
         return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = streetNumber;
      result = 31 * result + (streetName != null ? streetName.hashCode() : 0);
      result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
      result = 31 * result + (countryName != null ? countryName.hashCode() : 0);
      result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
      result = 31 * result + (inhabitants != null ? inhabitants.hashCode() : 0);
      result = 31 * result + id;
      result = 31 * result + version;
      return result;
   }

   @Override
   public String toString() {
      return "Address{" +
            "cityName='" + cityName + '\'' +
            ", streetNumber=" + streetNumber +
            ", streetName='" + streetName + '\'' +
            ", countryName='" + countryName + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", inhabitants=" + inhabitants +
            ", id=" + id +
            ", version=" + version +
            '}';
   }

}
