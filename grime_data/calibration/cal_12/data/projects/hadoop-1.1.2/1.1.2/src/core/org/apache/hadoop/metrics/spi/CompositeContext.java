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
package org.apache.hadoop.metrics.spi;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics.ContextFactory;
import org.apache.hadoop.metrics.MetricsContext;
import org.apache.hadoop.metrics.MetricsException;
import org.apache.hadoop.metrics.MetricsRecord;
import org.apache.hadoop.metrics.MetricsUtil;
import org.apache.hadoop.metrics.Updater;

/**
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class CompositeContext extends AbstractMetricsContext {

  private static final Log LOG = LogFactory.getLog(CompositeContext.class);
  private static final String ARITY_LABEL = "arity";
  private static final String SUB_FMT = "%s.sub%d";
  private final ArrayList<MetricsContext> subctxt =
    new ArrayList<MetricsContext>();

  @InterfaceAudience.Private
  public CompositeContext() {
  }

  @InterfaceAudience.Private
  public void init(String contextName, ContextFactory factory) {
    super.init(contextName, factory);
    int nKids;
    try {
      String sKids = getAttribute(ARITY_LABEL);
      nKids = Integer.valueOf(sKids);
    } catch (Exception e) {
      LOG.error("Unable to initialize composite metric " + contextName +
                ": could not init arity", e);
      return;
    }
    for (int i = 0; i < nKids; ++i) {
      MetricsContext ctxt = MetricsUtil.getContext(
          String.format(SUB_FMT, contextName, i), contextName);
      if (null != ctxt) {
        subctxt.add(ctxt);
      }
    }
  }

  @InterfaceAudience.Private
  @Override
  public MetricsRecord newRecord(String recordName) {
    return (MetricsRecord) Proxy.newProxyInstance(
        MetricsRecord.class.getClassLoader(),
        new Class[] { MetricsRecord.class },
        new MetricsRecordDelegator(recordName, subctxt));
  }

  @InterfaceAudience.Private
  @Override
  protected void emitRecord(String contextName, String recordName,
      OutputRecord outRec) throws IOException {
    for (MetricsContext ctxt : subctxt) {
      try {
        ((AbstractMetricsContext)ctxt).emitRecord(
          contextName, recordName, outRec);
        if (contextName == null || recordName == null || outRec == null) {
          throw new IOException(contextName + ":" + recordName + ":" + outRec);
        }
      } catch (IOException e) {
        LOG.warn("emitRecord failed: " + ctxt.getContextName(), e);
      }
    }
  }

  @InterfaceAudience.Private
  @Override
  protected void flush() throws IOException {
    for (MetricsContext ctxt : subctxt) {
      try {
        ((AbstractMetricsContext)ctxt).flush();
      } catch (IOException e) {
        LOG.warn("flush failed: " + ctxt.getContextName(), e);
      }
    }
  }

  @InterfaceAudience.Private
  @Override
  public void startMonitoring() throws IOException {
    for (MetricsContext ctxt : subctxt) {
      try {
        ctxt.startMonitoring();
      } catch (IOException e) {
        LOG.warn("startMonitoring failed: " + ctxt.getContextName(), e);
      }
    }
  }

  @InterfaceAudience.Private
  @Override
  public void stopMonitoring() {
    for (MetricsContext ctxt : subctxt) {
      ctxt.stopMonitoring();
    }
  }

  /**
   * Return true if all subcontexts are monitoring.
   */
  @InterfaceAudience.Private
  @Override
  public boolean isMonitoring() {
    boolean ret = true;
    for (MetricsContext ctxt : subctxt) {
      ret &= ctxt.isMonitoring();
    }
    return ret;
  }

  @InterfaceAudience.Private
  @Override
  public void close() {
    for (MetricsContext ctxt : subctxt) {
      ctxt.close();
    }
  }

  @InterfaceAudience.Private
  @Override
  public void registerUpdater(Updater updater) {
    for (MetricsContext ctxt : subctxt) {
      ctxt.registerUpdater(updater);
    }
  }

  @InterfaceAudience.Private
  @Override
  public void unregisterUpdater(Updater updater) {
    for (MetricsContext ctxt : subctxt) {
      ctxt.unregisterUpdater(updater);
    }
  }

  private static class MetricsRecordDelegator implements InvocationHandler {
    private static final Method m_getRecordName = initMethod();
    private static Method initMethod() {
      try {
        return MetricsRecord.class.getMethod("getRecordName", new Class[0]);
      } catch (Exception e) {
        throw new RuntimeException("Internal error", e);
      }
    }

    private final String recordName;
    private final ArrayList<MetricsRecord> subrecs;

    MetricsRecordDelegator(String recordName, ArrayList<MetricsContext> ctxts) {
      this.recordName = recordName;
      this.subrecs = new ArrayList<MetricsRecord>(ctxts.size());
      for (MetricsContext ctxt : ctxts) {
        subrecs.add(ctxt.createRecord(recordName));
      }
    }

    public Object invoke(Object p, Method m, Object[] args) throws Throwable {
      if (m_getRecordName.equals(m)) {
        return recordName;
      }
      assert Void.TYPE.equals(m.getReturnType());
      for (MetricsRecord rec : subrecs) {
        m.invoke(rec, args);
      }
      return null;
    }
  }

}
