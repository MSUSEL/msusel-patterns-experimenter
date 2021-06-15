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
package org.hibernate.cache.ehcache.internal.nonstop;

import org.hibernate.cache.ehcache.internal.regions.EhcacheCollectionRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheEntityRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheNaturalIdRegion;
import org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

/**
 * Implementation of {@link org.hibernate.cache.ehcache.internal.strategy.EhcacheAccessStrategyFactory} that takes care of Nonstop cache exceptions using
 * {@link HibernateNonstopCacheExceptionHandler}
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class NonstopAccessStrategyFactory implements EhcacheAccessStrategyFactory {

    private final EhcacheAccessStrategyFactory actualFactory;

    /**
     * Constructor accepting the actual factory
     *
     * @param actualFactory
     */
    public NonstopAccessStrategyFactory(EhcacheAccessStrategyFactory actualFactory) {
        this.actualFactory = actualFactory;
    }

    /**
     * {@inheritDoc}
     */
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(EhcacheEntityRegion entityRegion, AccessType accessType) {
        return new NonstopAwareEntityRegionAccessStrategy(
                actualFactory.createEntityRegionAccessStrategy( entityRegion, accessType ),
                HibernateNonstopCacheExceptionHandler.getInstance()
        );
    }

    @Override
	public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(EhcacheNaturalIdRegion naturalIdRegion,
			AccessType accessType) {
        return new NonstopAwareNaturalIdRegionAccessStrategy(
        		actualFactory.createNaturalIdRegionAccessStrategy( 
        				naturalIdRegion, 
        				accessType
				), HibernateNonstopCacheExceptionHandler.getInstance()
        );
	}

	/**
     * {@inheritDoc}
     */
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(EhcacheCollectionRegion collectionRegion,
                                                                               AccessType accessType) {
        return new NonstopAwareCollectionRegionAccessStrategy(
                actualFactory.createCollectionRegionAccessStrategy(
                        collectionRegion,
                        accessType
                ), HibernateNonstopCacheExceptionHandler.getInstance()
        );
    }

}
