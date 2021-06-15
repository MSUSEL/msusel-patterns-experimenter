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
package org.hibernate.test.cache.infinispan.util;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Settings;
import org.hibernate.service.ServiceRegistry;

/**
 * Utilities for cache testing.
 * 
 * @author <a href="brian.stansberry@jboss.com">Brian Stansberry</a>
 */
public class CacheTestUtil {

   public static Configuration buildConfiguration(String regionPrefix, Class regionFactory, boolean use2ndLevel, boolean useQueries) {
      Configuration cfg = new Configuration();
      cfg.setProperty(Environment.GENERATE_STATISTICS, "true");
      cfg.setProperty(Environment.USE_STRUCTURED_CACHE, "true");
      cfg.setProperty( AvailableSettings.JTA_PLATFORM, BatchModeJtaPlatform.class.getName() );

      cfg.setProperty(Environment.CACHE_REGION_FACTORY, regionFactory.getName());
      cfg.setProperty(Environment.CACHE_REGION_PREFIX, regionPrefix);
      cfg.setProperty(Environment.USE_SECOND_LEVEL_CACHE, String.valueOf(use2ndLevel));
      cfg.setProperty(Environment.USE_QUERY_CACHE, String.valueOf(useQueries));

      return cfg;
   }

   public static Configuration buildLocalOnlyConfiguration(String regionPrefix, boolean use2ndLevel, boolean useQueries) {
      Configuration cfg = buildConfiguration(regionPrefix, InfinispanRegionFactory.class, use2ndLevel, useQueries);
      cfg.setProperty(InfinispanRegionFactory.INFINISPAN_CONFIG_RESOURCE_PROP,
               InfinispanRegionFactory.DEF_INFINISPAN_CONFIG_RESOURCE);
      return cfg;
   }
   
   public static Configuration buildCustomQueryCacheConfiguration(String regionPrefix, String queryCacheName) {
      Configuration cfg = buildConfiguration(regionPrefix, InfinispanRegionFactory.class, true, true);
      cfg.setProperty(InfinispanRegionFactory.QUERY_CACHE_RESOURCE_PROP, queryCacheName);
      return cfg;
   }

   public static InfinispanRegionFactory startRegionFactory(
		   ServiceRegistry serviceRegistry,
		   Configuration cfg) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

      Settings settings = cfg.buildSettings( serviceRegistry );
      Properties properties = cfg.getProperties();

      String factoryType = cfg.getProperty(Environment.CACHE_REGION_FACTORY);
      Class factoryClass = Thread.currentThread().getContextClassLoader().loadClass(factoryType);
      InfinispanRegionFactory regionFactory = (InfinispanRegionFactory) factoryClass.newInstance();
      regionFactory.start(settings, properties);
      return regionFactory;
   }

   public static InfinispanRegionFactory startRegionFactory(
		   ServiceRegistry serviceRegistry,
		   Configuration cfg,
		   CacheTestSupport testSupport) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
      InfinispanRegionFactory factory = startRegionFactory( serviceRegistry, cfg );
      testSupport.registerFactory(factory);
      return factory;
   }

   public static void stopRegionFactory(InfinispanRegionFactory factory, CacheTestSupport testSupport) {
      factory.stop();
      testSupport.unregisterFactory(factory);
   }

   /**
    * Supports easy creation of a TestSuite where a subclass' "FailureExpected" version of a base
    * test is included in the suite, while the base test is excluded. E.g. test class FooTestCase
    * includes method testBar(), while test class SubFooTestCase extends FooTestCase includes method
    * testBarFailureExcluded(). Passing SubFooTestCase.class to this method will return a suite that
    * does not include testBar().
    * 
    * FIXME Move this to UnitTestCase
    */
   public static TestSuite createFailureExpectedSuite(Class testClass) {

      TestSuite allTests = new TestSuite(testClass);
      Set failureExpected = new HashSet();
      Enumeration tests = allTests.tests();
      while (tests.hasMoreElements()) {
         Test t = (Test) tests.nextElement();
         if (t instanceof TestCase) {
            String name = ((TestCase) t).getName();
            if (name.endsWith("FailureExpected"))
               failureExpected.add(name);
         }
      }

      TestSuite result = new TestSuite();
      tests = allTests.tests();
      while (tests.hasMoreElements()) {
         Test t = (Test) tests.nextElement();
         if (t instanceof TestCase) {
            String name = ((TestCase) t).getName();
            if (!failureExpected.contains(name + "FailureExpected")) {
               result.addTest(t);
            }
         }
      }

      return result;
   }

   /**
    * Prevent instantiation.
    */
   private CacheTestUtil() {
   }

}
