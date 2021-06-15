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
package org.hibernate.cache.ehcache.internal.strategy;

import org.jboss.logging.Logger;

import org.hibernate.cache.ehcache.EhCacheMessageLogger;
import org.hibernate.cache.ehcache.internal.regions.EhcacheCollectionRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheEntityRegion;
import org.hibernate.cache.ehcache.internal.regions.EhcacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

/**
 * Class implementing {@link EhcacheAccessStrategyFactory}
 *
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class EhcacheAccessStrategyFactoryImpl implements EhcacheAccessStrategyFactory {

    private static final EhCacheMessageLogger LOG = Logger.getMessageLogger(
            EhCacheMessageLogger.class,
            EhcacheAccessStrategyFactoryImpl.class.getName()
    );

    /**
     * {@inheritDoc}
     */
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(EhcacheEntityRegion entityRegion, AccessType accessType) {
        switch ( accessType ) {
            case READ_ONLY:
                if ( entityRegion.getCacheDataDescription().isMutable() ) {
                    LOG.readOnlyCacheConfiguredForMutableEntity( entityRegion.getName() );
                }
                return new ReadOnlyEhcacheEntityRegionAccessStrategy( entityRegion, entityRegion.getSettings() );
            case READ_WRITE:
                return new ReadWriteEhcacheEntityRegionAccessStrategy( entityRegion, entityRegion.getSettings() );

            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteEhcacheEntityRegionAccessStrategy(
                        entityRegion,
                        entityRegion.getSettings()
                );

            case TRANSACTIONAL:
                return new TransactionalEhcacheEntityRegionAccessStrategy(
                        entityRegion,
                        entityRegion.getEhcache(),
                        entityRegion.getSettings()
                );
            default:
                throw new IllegalArgumentException( "unrecognized access strategy type [" + accessType + "]" );

        }

    }

    /**
     * {@inheritDoc}
     */
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(EhcacheCollectionRegion collectionRegion,
                                                                               AccessType accessType) {
        switch ( accessType ) {
            case READ_ONLY:
                if ( collectionRegion.getCacheDataDescription().isMutable() ) {
                    LOG.readOnlyCacheConfiguredForMutableEntity( collectionRegion.getName() );
                }
                return new ReadOnlyEhcacheCollectionRegionAccessStrategy(
                        collectionRegion,
                        collectionRegion.getSettings()
                );
            case READ_WRITE:
                return new ReadWriteEhcacheCollectionRegionAccessStrategy(
                        collectionRegion,
                        collectionRegion.getSettings()
                );
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteEhcacheCollectionRegionAccessStrategy(
                        collectionRegion,
                        collectionRegion.getSettings()
                );
            case TRANSACTIONAL:
                return new TransactionalEhcacheCollectionRegionAccessStrategy(
                        collectionRegion, collectionRegion.getEhcache(), collectionRegion
                        .getSettings()
                );
            default:
                throw new IllegalArgumentException( "unrecognized access strategy type [" + accessType + "]" );
        }
    }

	@Override
	public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(EhcacheNaturalIdRegion naturalIdRegion,
			AccessType accessType) {
        switch ( accessType ) {
        case READ_ONLY:
            if ( naturalIdRegion.getCacheDataDescription().isMutable() ) {
                LOG.readOnlyCacheConfiguredForMutableEntity( naturalIdRegion.getName() );
            }
            return new ReadOnlyEhcacheNaturalIdRegionAccessStrategy(
                    naturalIdRegion,
                    naturalIdRegion.getSettings()
            );
        case READ_WRITE:
            return new ReadWriteEhcacheNaturalIdRegionAccessStrategy(
                    naturalIdRegion,
                    naturalIdRegion.getSettings()
            );
        case NONSTRICT_READ_WRITE:
            return new NonStrictReadWriteEhcacheNaturalIdRegionAccessStrategy(
                    naturalIdRegion,
                    naturalIdRegion.getSettings()
            );
        case TRANSACTIONAL:
            return new TransactionalEhcacheNaturalIdRegionAccessStrategy(
                    naturalIdRegion, naturalIdRegion.getEhcache(), naturalIdRegion
                    .getSettings()
            );
        default:
            throw new IllegalArgumentException( "unrecognized access strategy type [" + accessType + "]" );
    }
	}

    
}
