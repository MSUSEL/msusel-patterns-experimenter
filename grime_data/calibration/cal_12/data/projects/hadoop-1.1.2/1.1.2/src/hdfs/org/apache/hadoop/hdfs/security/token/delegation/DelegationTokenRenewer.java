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
package org.apache.hadoop.hdfs.security.token.delegation;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;

/**
 * A daemon thread that waits for the next file system to renew.
 */
@InterfaceAudience.Private
public class DelegationTokenRenewer<T extends FileSystem & DelegationTokenRenewer.Renewable>
    extends Thread {
  /** The renewable interface used by the renewer. */
  public interface Renewable {
    /** @return the renew token. */
    public Token<?> getRenewToken();

    /** Set delegation token. */
    public <T extends TokenIdentifier> void setDelegationToken(Token<T> token);
  }

  /**
   * An action that will renew and replace the file system's delegation 
   * tokens automatically.
   */
  private static class RenewAction<T extends FileSystem & Renewable>
      implements Delayed {
    /** when should the renew happen */
    private long renewalTime;
    /** a weak reference to the file system so that it can be garbage collected */
    private final WeakReference<T> weakFs;

    private RenewAction(final T fs) {
      this.weakFs = new WeakReference<T>(fs);
      updateRenewalTime();
    }
 
    /** Get the delay until this event should happen. */
    @Override
    public long getDelay(final TimeUnit unit) {
      final long millisLeft = renewalTime - System.currentTimeMillis();
      return unit.convert(millisLeft, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(final Delayed delayed) {
      final RenewAction<?> that = (RenewAction<?>)delayed;
      return this.renewalTime < that.renewalTime? -1
          : this.renewalTime == that.renewalTime? 0: 1;
    }

    @Override
    public int hashCode() {
      return (int)renewalTime ^ (int)(renewalTime >>> 32);
    }

    @Override
    public boolean equals(final Object that) {
      if (that == null || !(that instanceof RenewAction)) {
        return false;
      }
      return compareTo((Delayed)that) == 0;
    }
    
    /**
     * Set a new time for the renewal.
     * It can only be called when the action is not in the queue.
     * @param newTime the new time
     */
    private void updateRenewalTime() {
      renewalTime = RENEW_CYCLE + System.currentTimeMillis();
    }

    /**
     * Renew or replace the delegation token for this file system.
     * @return
     * @throws IOException
     */
    private boolean renew() throws IOException, InterruptedException {
      final T fs = weakFs.get();
      final boolean b = fs != null;
      if (b) {
        synchronized(fs) {
          try {
            fs.getRenewToken().renew(fs.getConf());
          } catch (IOException ie) {
            try {
              fs.setDelegationToken(fs.getDelegationToken(null));
            } catch (IOException ie2) {
              throw new IOException("Can't renew or get new delegation token ", ie);
            }
          }
        }
      }
      return b;
    }

    @Override
    public String toString() {
      Renewable fs = weakFs.get();
      return fs == null? "evaporated token renew"
          : "The token will be renewed in " + getDelay(TimeUnit.SECONDS)
            + " secs, renewToken=" + fs.getRenewToken();
    }
  }

  /** Wait for 95% of a day between renewals */
  private static final int RENEW_CYCLE = 24 * 60 * 60 * 950;

  private DelayQueue<RenewAction<T>> queue = new DelayQueue<RenewAction<T>>();

  public DelegationTokenRenewer(final Class<T> clazz) {
    super(clazz.getSimpleName() + "-" + DelegationTokenRenewer.class.getSimpleName());
    setDaemon(true);
  }

  @Override
  public void start() {
    return; // lazy start when addRenewAction is actually called
  }
  
  /** Add a renew action to the queue. */
  public void addRenewAction(final T fs) {
    queue.add(new RenewAction<T>(fs));
    if (!isAlive()) super.start();
  }

  @Override
  public void run() {
    for(;;) {
      RenewAction<T> action = null;
      try {
        action = queue.take();
        if (action.renew()) {
          action.updateRenewalTime();
          queue.add(action);
        }
      } catch (InterruptedException ie) {
        return;
      } catch (Exception ie) {
        T.LOG.warn("Failed to renew token, action=" + action, ie);
      }
    }
  }
}