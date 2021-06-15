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
package org.hibernate.test.cache.infinispan.functional.classloader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Comment
 * 
 * @author Brian Stansberry
 */
public class AccountHolder implements Serializable {
   private static final long serialVersionUID = 1L;

   private String lastName;
   private String ssn;
   private transient boolean deserialized;

   public AccountHolder() {
      this("Stansberry", "123-456-7890");
   }

   public AccountHolder(String lastName, String ssn) {
      this.lastName = lastName;
      this.ssn = ssn;
   }

   public String getLastName() {
      return this.lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getSsn() {
      return ssn;
   }

   public void setSsn(String ssn) {
      this.ssn = ssn;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (!(obj instanceof AccountHolder))
         return false;
      AccountHolder pk = (AccountHolder) obj;
      if (!lastName.equals(pk.lastName))
         return false;
      if (!ssn.equals(pk.ssn))
         return false;
      return true;
   }

   @Override
   public int hashCode() {
      int result = 17;
      result = result * 31 + lastName.hashCode();
      result = result * 31 + ssn.hashCode();
      return result;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer(getClass().getName());
      sb.append("[lastName=");
      sb.append(lastName);
      sb.append(",ssn=");
      sb.append(ssn);
      sb.append(",deserialized=");
      sb.append(deserialized);
      sb.append("]");
      return sb.toString();
   }

   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
      ois.defaultReadObject();
      deserialized = true;
   }

}
