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
package org.hibernate.loader.collection;

import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.QueryableCollection;

/**
 * Contract for building {@link CollectionInitializer} instances capable of performing batch-fetch loading.
 *
 * @author Steve Ebersole
 *
 * @see org.hibernate.loader.BatchFetchStyle
 */
public abstract class BatchingCollectionInitializerBuilder {
	public static BatchingCollectionInitializerBuilder getBuilder(SessionFactoryImplementor factory) {
		switch ( factory.getSettings().getBatchFetchStyle() ) {
			case PADDED: {
				return PaddedBatchingCollectionInitializerBuilder.INSTANCE;
			}
			case DYNAMIC: {
				return DynamicBatchingCollectionInitializerBuilder.INSTANCE;
			}
			default: {
				return LegacyBatchingCollectionInitializerBuilder.INSTANCE;
			}
		}
	}

	/**
	 * Builds a batch-fetch capable CollectionInitializer for basic and many-to-many collections (collections with
	 * a dedicated collection table).
	 *
	 * @param persister THe collection persister
	 * @param maxBatchSize The maximum number of keys to batch-fetch together
	 * @param factory The SessionFactory
	 * @param influencers Any influencers that should affect the built query
	 *
	 * @return The batch-fetch capable collection initializer
	 */
	public CollectionInitializer createBatchingCollectionInitializer(
			QueryableCollection persister,
			int maxBatchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers) {
		if ( maxBatchSize <= 1 ) {
			// no batching
			return new BasicCollectionLoader( persister, factory, influencers );
		}

		return createRealBatchingCollectionInitializer( persister, maxBatchSize, factory, influencers );
	}

	protected abstract CollectionInitializer createRealBatchingCollectionInitializer(
			QueryableCollection persister,
			int maxBatchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers);


	/**
	 * Builds a batch-fetch capable CollectionInitializer for one-to-many collections (collections without
	 * a dedicated collection table).
	 *
	 * @param persister THe collection persister
	 * @param maxBatchSize The maximum number of keys to batch-fetch together
	 * @param factory The SessionFactory
	 * @param influencers Any influencers that should affect the built query
	 *
	 * @return The batch-fetch capable collection initializer
	 */
	public CollectionInitializer createBatchingOneToManyInitializer(
			QueryableCollection persister,
			int maxBatchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers) {
		if ( maxBatchSize <= 1 ) {
			// no batching
			return new OneToManyLoader( persister, factory, influencers );
		}

		return createRealBatchingOneToManyInitializer( persister, maxBatchSize, factory, influencers );
	}

	protected abstract CollectionInitializer createRealBatchingOneToManyInitializer(
			QueryableCollection persister,
			int maxBatchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers);
}
