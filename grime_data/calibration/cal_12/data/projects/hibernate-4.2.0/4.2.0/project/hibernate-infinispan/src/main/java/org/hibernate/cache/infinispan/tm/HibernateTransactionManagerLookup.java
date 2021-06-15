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
package org.hibernate.cache.infinispan.tm;

import java.util.Properties;
import javax.transaction.TransactionManager;

import org.hibernate.cfg.Settings;
import org.hibernate.service.jta.platform.spi.JtaPlatform;

/**
 * HibernateTransactionManagerLookup.
 * 
 * @author Galder Zamarreño
 * @since 3.5
 */
public class HibernateTransactionManagerLookup implements org.infinispan.transaction.lookup.TransactionManagerLookup {
	private final JtaPlatform jtaPlatform;

	public HibernateTransactionManagerLookup(Settings settings, Properties properties) {
		this.jtaPlatform = settings != null ? settings.getJtaPlatform() : null;
	}
	@Override
	public TransactionManager getTransactionManager() throws Exception {
		return jtaPlatform == null ? null : jtaPlatform.retrieveTransactionManager();
	}
   
}
