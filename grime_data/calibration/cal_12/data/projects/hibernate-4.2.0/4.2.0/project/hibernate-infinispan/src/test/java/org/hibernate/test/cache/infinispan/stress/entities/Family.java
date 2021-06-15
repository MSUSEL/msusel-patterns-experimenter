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
import java.util.Set;

@Entity
public final class Family {

   @Id
   @GeneratedValue
   private int id;
   private String name;
   private String secondName;
   @OneToMany
   private Set<Person> members;
   private int version;

   public Family(String name) {
      this.name = name;
      this.secondName = null;
      this.members = new HashSet<Person>();
      this.id = 0;
      this.version = 0;
   }

   protected Family() {
      this.name = null;
      this.secondName = null;
      this.members = new HashSet<Person>();
      this.id = 0;
      this.version = 0;
   }

   public String getName() {
      return name;
   }

   public Set<Person> getMembers() {
      return members;
   }

   public String getSecondName() {
      return secondName;
   }

   public void setSecondName(String secondName) {
      this.secondName = secondName;
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

   public void setName(String name) {
      this.name = name;
   }

   public void setMembers(Set<Person> members) {
      if (members == null) {
         this.members = new HashSet<Person>();
      } else {
         this.members = members;
      }
   }

   public void setVersion(Integer version) {
      this.version = version;
   }

   boolean addMember(Person member) {
      return members.add(member);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Family family = (Family) o;

      if (id != family.id) return false;
      if (version != family.version) return false;
      if (members != null ? !members.equals(family.members) : family.members != null)
         return false;
      if (name != null ? !name.equals(family.name) : family.name != null)
         return false;
      if (secondName != null ? !secondName.equals(family.secondName) : family.secondName != null)
         return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = name != null ? name.hashCode() : 0;
      result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
      result = 31 * result + (members != null ? members.hashCode() : 0);
      result = 31 * result + id;
      result = 31 * result + version;
      return result;
   }

   @Override
   public String toString() {
      return "Family{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", secondName='" + secondName + '\'' +
            ", members=" + members +
            ", version=" + version +
            '}';
   }

}
