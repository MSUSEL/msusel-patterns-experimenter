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
package org.hibernate.cache.infinispan;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

import org.hibernate.cache.CacheException;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.internal.util.jndi.JndiHelper;

/**
 * A {@link org.hibernate.cache.spi.RegionFactory} for <a href="http://www.jboss.org/infinispan">Infinispan</a>-backed cache
 * regions that finds its cache manager in JNDI rather than creating one itself.
 * 
 * @author Galder Zamarreño
 * @since 3.5
 */
public class JndiInfinispanRegionFactory extends InfinispanRegionFactory {

   private static final Log log = LogFactory.getLog(JndiInfinispanRegionFactory.class);

   /**
    * Specifies the JNDI name under which the {@link EmbeddedCacheManager} to use is bound.
    * There is no default value -- the user must specify the property.
    */
   public static final String CACHE_MANAGER_RESOURCE_PROP = "hibernate.cache.infinispan.cachemanager";

   public JndiInfinispanRegionFactory() {
      super();
   }

   public JndiInfinispanRegionFactory(Properties props) {
      super(props);
   }

   @Override
   protected EmbeddedCacheManager createCacheManager(Properties properties) throws CacheException {
      String name = ConfigurationHelper.getString(CACHE_MANAGER_RESOURCE_PROP, properties, null);
      if (name == null)
         throw new CacheException("Configuration property " + CACHE_MANAGER_RESOURCE_PROP + " not set");
      return locateCacheManager(name, JndiHelper.extractJndiProperties(properties));
   }

   private EmbeddedCacheManager locateCacheManager(String jndiNamespace, Properties jndiProperties) {
      Context ctx = null;
      try {
          ctx = new InitialContext(jndiProperties);
          return (EmbeddedCacheManager) ctx.lookup(jndiNamespace);
      } catch (NamingException ne) {
          String msg = "Unable to retrieve CacheManager from JNDI [" + jndiNamespace + "]";
          log.info(msg, ne);
          throw new CacheException( msg );
      } finally {
          if (ctx != null) {
              try {
                  ctx.close();
              } catch( NamingException ne ) {
                  log.info("Unable to release initial context", ne);
              }
          }
      }
   }

   @Override
   public void stop() {
      // Do not attempt to stop a cache manager because it wasn't created by this region factory.
   }
}
