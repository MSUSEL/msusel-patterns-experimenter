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
package org.hibernate.test.cache.infinispan.functional;

import org.hibernate.Session;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;

import static org.infinispan.test.TestingUtil.withTx;
import static org.junit.Assert.assertEquals;

/**
 * Parent tests for both transactional and
 * read-only tests are defined in this class.
 *
 * @author Galder Zamarreño
 * @since 4.1
 */
public abstract class AbstractFunctionalTestCase extends SingleNodeTestCase {

   static final Log log = LogFactory.getLog(AbstractFunctionalTestCase.class);

   @Test
   public void testEmptySecondLevelCacheEntry() throws Exception {
      sessionFactory().getCache().evictCollectionRegion( Item.class.getName() + ".items" );
      Statistics stats = sessionFactory().getStatistics();
      stats.clear();
      SecondLevelCacheStatistics statistics = stats.getSecondLevelCacheStatistics( Item.class.getName() + ".items" );
      Map cacheEntries = statistics.getEntries();
      assertEquals( 0, cacheEntries.size() );
   }

   @Test
   public void testInsertDeleteEntity() throws Exception {
      final Statistics stats = sessionFactory().getStatistics();
      stats.clear();

      final Item item = new Item( "chris", "Chris's Item" );
      withTx(tm, new Callable<Void>() {
         @Override
         public Void call() throws Exception {
            Session s = openSession();
            s.getTransaction().begin();
            s.persist(item);
            s.getTransaction().commit();
            s.close();
            return null;
         }
      });

      log.info("Entry persisted, let's load and delete it.");

      withTx(tm, new Callable<Void>() {
         @Override
         public Void call() throws Exception {
            Session s = openSession();
            s.getTransaction().begin();
            Item found = (Item) s.load(Item.class, item.getId());
            log.info(stats.toString());
            assertEquals(item.getDescription(), found.getDescription());
            assertEquals(0, stats.getSecondLevelCacheMissCount());
            assertEquals(1, stats.getSecondLevelCacheHitCount());
            s.delete(found);
            s.getTransaction().commit();
            s.close();
            return null;
         }
      });
   }

}
