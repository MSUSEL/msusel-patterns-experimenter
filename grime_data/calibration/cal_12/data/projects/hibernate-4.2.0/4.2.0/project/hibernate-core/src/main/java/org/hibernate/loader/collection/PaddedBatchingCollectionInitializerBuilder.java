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

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.loader.Loader;
import org.hibernate.persister.collection.QueryableCollection;

/**
 * A batch-fetch capable CollectionInitializer that performs batch-fetching using the padded style.  See
 * {@link org.hibernate.loader.BatchFetchStyle} for a discussion of the different styles.
 *
 * @author Steve Ebersole
 *
 * @see org.hibernate.loader.BatchFetchStyle#PADDED
 */
public class PaddedBatchingCollectionInitializerBuilder extends BatchingCollectionInitializerBuilder {
	public static final PaddedBatchingCollectionInitializerBuilder INSTANCE = new PaddedBatchingCollectionInitializerBuilder();

	@Override
	public CollectionInitializer createRealBatchingCollectionInitializer(
			QueryableCollection persister,
			int maxBatchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		int[] batchSizes = ArrayHelper.getBatchSizes( maxBatchSize );
		Loader[] loaders = new Loader[ batchSizes.length ];
		for ( int i = 0; i < batchSizes.length; i++ ) {
			loaders[i] = new BasicCollectionLoader( persister, batchSizes[i], factory, loadQueryInfluencers );
		}
		return new PaddedBatchingCollectionInitializer( persister, batchSizes, loaders );
	}

	@Override
	public CollectionInitializer createRealBatchingOneToManyInitializer(
			QueryableCollection persister,
			int maxBatchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		final int[] batchSizes = ArrayHelper.getBatchSizes( maxBatchSize );
		final Loader[] loaders = new Loader[ batchSizes.length ];
		for ( int i = 0; i < batchSizes.length; i++ ) {
			loaders[i] = new OneToManyLoader( persister, batchSizes[i], factory, loadQueryInfluencers );
		}
		return new PaddedBatchingCollectionInitializer( persister, batchSizes, loaders );
	}


	private static class PaddedBatchingCollectionInitializer extends BatchingCollectionInitializer {
		private final int[] batchSizes;
		private final Loader[] loaders;

		public PaddedBatchingCollectionInitializer(QueryableCollection persister, int[] batchSizes, Loader[] loaders) {
			super( persister );

			this.batchSizes = batchSizes;
			this.loaders = loaders;
		}

		@Override
		public void initialize(Serializable id, SessionImplementor session)	throws HibernateException {
			final Serializable[] batch = session.getPersistenceContext()
					.getBatchFetchQueue()
					.getCollectionBatch( collectionPersister(), id, batchSizes[0] );
			final int numberOfIds = ArrayHelper.countNonNull( batch );
			if ( numberOfIds <= 1 ) {
				loaders[batchSizes.length-1].loadCollection( session, id, collectionPersister().getKeyType() );
				return;
			}

			// Uses the first batch-size bigger than the number of actual ids in the batch
			int indexToUse = batchSizes.length-1;
			for ( int i = 0; i < batchSizes.length-1; i++ ) {
				if ( batchSizes[i] >= numberOfIds ) {
					indexToUse = i;
				}
				else {
					break;
				}
			}

			final Serializable[] idsToLoad = new Serializable[ batchSizes[indexToUse] ];
			System.arraycopy( batch, 0, idsToLoad, 0, numberOfIds );
			for ( int i = numberOfIds; i < batchSizes[indexToUse]; i++ ) {
				idsToLoad[i] = id;
			}

			loaders[indexToUse].loadCollectionBatch( session, idsToLoad, collectionPersister().getKeyType() );
		}
	}
}
