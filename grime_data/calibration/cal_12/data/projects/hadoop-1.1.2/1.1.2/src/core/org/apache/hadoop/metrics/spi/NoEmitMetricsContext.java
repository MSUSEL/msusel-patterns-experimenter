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

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.metrics.ContextFactory;
import org.apache.hadoop.metrics.MetricsServlet;

/** 
 * A MetricsContext that does not emit data, but, unlike NullContextWithUpdate,
 * does save it for retrieval with getAllRecords().
 * 
 * This is useful if you want to support {@link MetricsServlet}, but
 * not emit metrics in any other way.
 * @deprecated in favor of <code>org.apache.hadoop.metrics2</code> usage.
 */
@Deprecated
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class NoEmitMetricsContext extends AbstractMetricsContext {
    
    private static final String PERIOD_PROPERTY = "period";
      
    /** Creates a new instance of NullContextWithUpdateThread */
    @InterfaceAudience.Private
    public NoEmitMetricsContext() {
    }
    
    @InterfaceAudience.Private
    public void init(String contextName, ContextFactory factory) {
      super.init(contextName, factory);
      parseAndSetPeriod(PERIOD_PROPERTY);
    }
     
    /**
     * Do-nothing version of emitRecord
     */
    @InterfaceAudience.Private
    protected void emitRecord(String contextName, String recordName,
                              OutputRecord outRec) {
    }
}
