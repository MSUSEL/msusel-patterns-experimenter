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
import java.io.Serializable;

/**
 * Comment
 * 
 * @author Brian Stansberry
 */
public class Account implements Serializable {

   private static final long serialVersionUID = 1L;

   private Integer id;
   private AccountHolder accountHolder;
   private Integer balance;
   private String branch;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public AccountHolder getAccountHolder() {
      return accountHolder;
   }

   public void setAccountHolder(AccountHolder accountHolder) {
      this.accountHolder = accountHolder;
   }

   public Integer getBalance() {
      return balance;
   }

   public void setBalance(Integer balance) {
      this.balance = balance;
   }

   public String getBranch() {
      return branch;
   }

   public void setBranch(String branch) {
      this.branch = branch;
   }

   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (!(obj instanceof Account))
         return false;
      Account acct = (Account) obj;
      if (!safeEquals(id, acct.id))
         return false;
      if (!safeEquals(branch, acct.branch))
         return false;
      if (!safeEquals(balance, acct.balance))
         return false;
      if (!safeEquals(accountHolder, acct.accountHolder))
         return false;
      return true;
   }

   @Override
   public int hashCode() {
      int result = 17;
      result = result * 31 + safeHashCode(id);
      result = result * 31 + safeHashCode(branch);
      result = result * 31 + safeHashCode(balance);
      result = result * 31 + safeHashCode(accountHolder);
      return result;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer(getClass().getName());
      sb.append("[id=");
      sb.append(id);
      sb.append(",branch=");
      sb.append(branch);
      sb.append(",balance=");
      sb.append(balance);
      sb.append(",accountHolder=");
      sb.append(accountHolder);
      sb.append("]");
      return sb.toString();
   }

   private static int safeHashCode(Object obj) {
      return obj == null ? 0 : obj.hashCode();
   }

   private static boolean safeEquals(Object a, Object b) {
      return (a == b || (a != null && a.equals(b)));
   }

}
