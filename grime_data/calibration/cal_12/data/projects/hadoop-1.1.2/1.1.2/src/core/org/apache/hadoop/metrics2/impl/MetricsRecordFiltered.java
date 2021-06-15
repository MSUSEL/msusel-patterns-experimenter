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
package org.apache.hadoop.metrics2.impl;

import java.util.Iterator;
import org.apache.hadoop.metrics2.Metric;
import org.apache.hadoop.metrics2.MetricsFilter;
import org.apache.hadoop.metrics2.MetricsRecord;
import org.apache.hadoop.metrics2.MetricsTag;
import org.apache.hadoop.metrics2.util.TryIterator;

class MetricsRecordFiltered implements MetricsRecord {

  private final MetricsRecord delegate;
  private final MetricsFilter filter;

  MetricsRecordFiltered(MetricsRecord delegate, MetricsFilter filter) {
    this.delegate = delegate;
    this.filter = filter;
  }

  public long timestamp() {
    return delegate.timestamp();
  }

  public String name() {
    return delegate.name();
  }

  public String context() {
    return delegate.context();
  }

  public Iterable<MetricsTag> tags() {
    return delegate.tags();
  }

  public Iterable<Metric> metrics() {
    return new Iterable<Metric>() {
      final Iterator<Metric> it = delegate.metrics().iterator();
      public Iterator<Metric> iterator() {
        return new TryIterator<Metric>() {
          public Metric tryNext() {
            if (it.hasNext()) do {
              Metric next = it.next();
              if (filter.accepts(next.name())) {
                return next;
              }
            } while (it.hasNext());
            return done();
          }
        };
      }
    };
  }

}
