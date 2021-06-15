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
package org.hibernate.test.cache.infinispan;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.infinispan.collection.CollectionRegionImpl;
import org.hibernate.cache.infinispan.entity.EntityRegionImpl;
import org.hibernate.cache.infinispan.util.Caches;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.internal.StandardServiceRegistryImpl;
import org.hibernate.test.cache.infinispan.util.CacheTestUtil;
import org.infinispan.context.Flag;

/**
 * Defines the environment for a node.
 *
 * @author Steve Ebersole
 */
public class NodeEnvironment {
	private final Configuration configuration;

	private StandardServiceRegistryImpl serviceRegistry;
	private InfinispanRegionFactory regionFactory;

	private Map<String,EntityRegionImpl> entityRegionMap;
	private Map<String,CollectionRegionImpl> collectionRegionMap;

	public NodeEnvironment(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public StandardServiceRegistryImpl getServiceRegistry() {
		return serviceRegistry;
	}

	public EntityRegionImpl getEntityRegion(String name, CacheDataDescription cacheDataDescription) {
		if ( entityRegionMap == null ) {
			entityRegionMap = new HashMap<String, EntityRegionImpl>();
			return buildAndStoreEntityRegion( name, cacheDataDescription );
		}
		EntityRegionImpl region = entityRegionMap.get( name );
		if ( region == null ) {
			region = buildAndStoreEntityRegion( name, cacheDataDescription );
		}
		return region;
	}

	private EntityRegionImpl buildAndStoreEntityRegion(String name, CacheDataDescription cacheDataDescription) {
		EntityRegionImpl region = (EntityRegionImpl) regionFactory.buildEntityRegion(
				name,
				configuration.getProperties(),
				cacheDataDescription
		);
		entityRegionMap.put( name, region );
		return region;
	}

	public CollectionRegionImpl getCollectionRegion(String name, CacheDataDescription cacheDataDescription) {
		if ( collectionRegionMap == null ) {
			collectionRegionMap = new HashMap<String, CollectionRegionImpl>();
			return buildAndStoreCollectionRegion( name, cacheDataDescription );
		}
		CollectionRegionImpl region = collectionRegionMap.get( name );
		if ( region == null ) {
			region = buildAndStoreCollectionRegion( name, cacheDataDescription );
			collectionRegionMap.put( name, region );
		}
		return region;
	}

	private CollectionRegionImpl buildAndStoreCollectionRegion(String name, CacheDataDescription cacheDataDescription) {
		CollectionRegionImpl region;
		region = (CollectionRegionImpl) regionFactory.buildCollectionRegion(
				name,
				configuration.getProperties(),
				cacheDataDescription
		);
		return region;
	}

	public void prepare() throws Exception {
		serviceRegistry = (StandardServiceRegistryImpl) new ServiceRegistryBuilder()
				.applySettings( configuration.getProperties() )
				.buildServiceRegistry();
		regionFactory = CacheTestUtil.startRegionFactory( serviceRegistry, configuration );
	}

	public void release() throws Exception {
		if ( entityRegionMap != null ) {
			for ( final EntityRegionImpl region : entityRegionMap.values() ) {
				Caches.withinTx(region.getTransactionManager(), new Callable<Void>() {
               @Override
               public Void call() throws Exception {
                  region.getCache().withFlags(Flag.CACHE_MODE_LOCAL).clear();
                  return null;
               }
            });
				region.getCache().stop();
			}
			entityRegionMap.clear();
		}
		if ( collectionRegionMap != null ) {
			for ( final CollectionRegionImpl collectionRegion : collectionRegionMap.values() ) {
            Caches.withinTx(collectionRegion.getTransactionManager(), new Callable<Void>() {
               @Override
               public Void call() throws Exception {
                  collectionRegion.getCache().withFlags(Flag.CACHE_MODE_LOCAL).clear();
                  return null;
               }
            });
				collectionRegion.getCache().stop();
			}
			collectionRegionMap.clear();
		}
		if ( regionFactory != null ) {
// Currently the RegionFactory is shutdown by its registration with the CacheTestSetup from CacheTestUtil when built
			regionFactory.stop();
		}
		if ( serviceRegistry != null ) {
			serviceRegistry.destroy();
		}
	}
}
