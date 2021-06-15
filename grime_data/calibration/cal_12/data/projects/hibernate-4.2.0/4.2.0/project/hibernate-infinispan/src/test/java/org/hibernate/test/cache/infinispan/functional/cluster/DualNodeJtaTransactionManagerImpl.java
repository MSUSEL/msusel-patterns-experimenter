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
package org.hibernate.test.cache.infinispan.functional.cluster;

import java.util.Hashtable;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

/**
 * Variant of SimpleJtaTransactionManagerImpl that doesn't use a VM-singleton, but rather a set of
 * impls keyed by a node id.
 *
 * TODO: Merge with single node transaction manager as much as possible
 *
 * @author Brian Stansberry
 */
public class DualNodeJtaTransactionManagerImpl implements TransactionManager {

   private static final Log log = LogFactory.getLog(DualNodeJtaTransactionManagerImpl.class);

   private static final Hashtable INSTANCES = new Hashtable();

   private ThreadLocal currentTransaction = new ThreadLocal();
   private String nodeId;

   public synchronized static DualNodeJtaTransactionManagerImpl getInstance(String nodeId) {
      DualNodeJtaTransactionManagerImpl tm = (DualNodeJtaTransactionManagerImpl) INSTANCES
               .get(nodeId);
      if (tm == null) {
         tm = new DualNodeJtaTransactionManagerImpl(nodeId);
         INSTANCES.put(nodeId, tm);
      }
      return tm;
   }

   public synchronized static void cleanupTransactions() {
      for (java.util.Iterator it = INSTANCES.values().iterator(); it.hasNext();) {
         TransactionManager tm = (TransactionManager) it.next();
         try {
            tm.suspend();
         } catch (Exception e) {
            log.error("Exception cleaning up TransactionManager " + tm);
         }
      }
   }

   public synchronized static void cleanupTransactionManagers() {
      INSTANCES.clear();
   }

   private DualNodeJtaTransactionManagerImpl(String nodeId) {
      this.nodeId = nodeId;
   }

   public int getStatus() throws SystemException {
      Transaction tx = getCurrentTransaction();
      return tx == null ? Status.STATUS_NO_TRANSACTION : tx.getStatus();
   }

   public Transaction getTransaction() throws SystemException {
      return (Transaction) currentTransaction.get();
   }

   public DualNodeJtaTransactionImpl getCurrentTransaction() {
      return (DualNodeJtaTransactionImpl) currentTransaction.get();
   }

   public void begin() throws NotSupportedException, SystemException {
      currentTransaction.set(new DualNodeJtaTransactionImpl(this));
   }

   public Transaction suspend() throws SystemException {
      DualNodeJtaTransactionImpl suspended = getCurrentTransaction();
      log.trace(nodeId + ": Suspending " + suspended + " for thread "
               + Thread.currentThread().getName());
      currentTransaction.set(null);
      return suspended;
   }

   public void resume(Transaction transaction) throws InvalidTransactionException,
            IllegalStateException, SystemException {
      currentTransaction.set(transaction);
      log.trace(nodeId + ": Resumed " + transaction + " for thread "
               + Thread.currentThread().getName());
   }

   public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
      Transaction tx = getCurrentTransaction();
      if (tx == null) {
         throw new IllegalStateException("no current transaction to commit");
      }
      tx.commit();
   }

   public void rollback() throws IllegalStateException, SecurityException, SystemException {
      Transaction tx = getCurrentTransaction();
      if (tx == null) {
         throw new IllegalStateException("no current transaction");
      }
      tx.rollback();
   }

   public void setRollbackOnly() throws IllegalStateException, SystemException {
      Transaction tx = getCurrentTransaction();
      if (tx == null) {
         throw new IllegalStateException("no current transaction");
      }
      tx.setRollbackOnly();
   }

   public void setTransactionTimeout(int i) throws SystemException {
   }

   void endCurrent(DualNodeJtaTransactionImpl transaction) {
      if (transaction == currentTransaction.get()) {
         currentTransaction.set(null);
      }
   }

   @Override
public String toString() {
      StringBuffer sb = new StringBuffer(getClass().getName());
      sb.append("[nodeId=");
      sb.append(nodeId);
      sb.append("]");
      return sb.toString();
   }
}
