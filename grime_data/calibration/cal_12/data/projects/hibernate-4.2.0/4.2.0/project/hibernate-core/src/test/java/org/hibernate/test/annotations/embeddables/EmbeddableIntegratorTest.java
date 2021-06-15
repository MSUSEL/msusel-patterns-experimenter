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
package org.hibernate.test.annotations.embeddables;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.integrator.internal.IntegratorServiceImpl;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.internal.BootstrapServiceRegistryImpl;
import org.hibernate.testing.RequiresDialect;
import org.hibernate.testing.junit4.BaseUnitTestCase;
import org.junit.Test;

/**
 * @author Chris Pheby
 */
@RequiresDialect(H2Dialect.class)
public class EmbeddableIntegratorTest extends BaseUnitTestCase {

	/**
	 * Throws a mapping exception because DollarValue is not mapped
	 */
	@Test(expected=GenericJDBCException.class)
	public void testWithoutIntegrator() {
		
		ServiceRegistry reg = new ServiceRegistryBuilder(new BootstrapServiceRegistryImpl())
		.buildServiceRegistry();
		
		SessionFactory sf = new Configuration()
		.addAnnotatedClass( Investor.class )

		.buildSessionFactory(reg);
		
		Session sess = sf.openSession();
		Investor myInv = getInvestor();
		myInv.setId(1L);
		
		sess.save(myInv);
		sess.flush();
		sess.clear();
		
		Investor inv = (Investor) sess.get(Investor.class, 1L);
		assertEquals(new BigDecimal("100"), inv.getInvestments().get(0).getAmount().getAmount());
		
		sess.close();
	}

	@Test
	public void testWithIntegrator() {
		
		LinkedHashSet<Integrator> providedIntegrators = new LinkedHashSet<Integrator>();
		providedIntegrators.add(new InvestorIntegrator());
		ClassLoaderService classLoaderService = new ClassLoaderServiceImpl();
		IntegratorService integratorService = new IntegratorServiceImpl(providedIntegrators, classLoaderService);
		
		ServiceRegistry reg = new ServiceRegistryBuilder(new BootstrapServiceRegistryImpl(
				classLoaderService,
				integratorService)).buildServiceRegistry();
		
		SessionFactory sf = new Configuration()
		.addAnnotatedClass( Investor.class )

		.setProperty("hibernate.hbm2ddl.auto", "create-drop")
		.buildSessionFactory(reg);
		
		Session sess = sf.openSession();
		Investor myInv = getInvestor();
		myInv.setId(2L);
		
		sess.save(myInv);
		sess.flush();
		sess.clear();
		
		Investor inv = (Investor) sess.get(Investor.class, 2L);
		assertEquals(new BigDecimal("100"), inv.getInvestments().get(0).getAmount().getAmount());
		
		sess.close();
	}
	
	private Investor getInvestor() {
		Investor i = new Investor();
		List<Investment> investments = new ArrayList<Investment>();
		Investment i1 = new Investment();
		i1.setAmount(new DollarValue(new BigDecimal("100")));
		i1.setDate(new MyDate(new Date()));
		i1.setDescription("Test Investment");
		investments.add(i1);
		i.setInvestments(investments);
		
		return i;
	}
}
