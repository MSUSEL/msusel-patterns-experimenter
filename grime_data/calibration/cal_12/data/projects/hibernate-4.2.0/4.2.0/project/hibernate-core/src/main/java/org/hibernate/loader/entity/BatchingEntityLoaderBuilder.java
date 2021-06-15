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
package org.hibernate.loader.entity;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.entity.OuterJoinLoadable;

/**
 * The contract for building {@link UniqueEntityLoader} capable of performing batch-fetch loading.  Intention
 * is to build these instances, by first calling the static {@link #getBuilder}, and then calling the appropriate
 * {@link #buildLoader} method.
 *
 * @author Steve Ebersole
 *
 * @see org.hibernate.loader.BatchFetchStyle
 */
public abstract class BatchingEntityLoaderBuilder {
	public static BatchingEntityLoaderBuilder getBuilder(SessionFactoryImplementor factory) {
		switch ( factory.getSettings().getBatchFetchStyle() ) {
			case PADDED: {
				return PaddedBatchingEntityLoaderBuilder.INSTANCE;
			}
			case DYNAMIC: {
				return DynamicBatchingEntityLoaderBuilder.INSTANCE;
			}
			default: {
				return LegacyBatchingEntityLoaderBuilder.INSTANCE;
			}
		}
	}

	/**
	 * Builds a batch-fetch capable loader based on the given persister, lock-mode, etc.
	 *
	 * @param persister The entity persister
	 * @param batchSize The maximum number of ids to batch-fetch at once
	 * @param lockMode The lock mode
	 * @param factory The SessionFactory
	 * @param influencers Any influencers that should affect the built query
	 *
	 * @return The loader.
	 */
	public UniqueEntityLoader buildLoader(
			OuterJoinLoadable persister,
			int batchSize,
			LockMode lockMode,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers) {
		if ( batchSize <= 1 ) {
			// no batching
			return new EntityLoader( persister, lockMode, factory, influencers );
		}
		return buildBatchingLoader( persister, batchSize, lockMode, factory, influencers );
	}

	protected abstract UniqueEntityLoader buildBatchingLoader(
			OuterJoinLoadable persister,
			int batchSize,
			LockMode lockMode,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers);

	/**
	 * Builds a batch-fetch capable loader based on the given persister, lock-options, etc.
	 *
	 * @param persister The entity persister
	 * @param batchSize The maximum number of ids to batch-fetch at once
	 * @param lockOptions The lock options
	 * @param factory The SessionFactory
	 * @param influencers Any influencers that should affect the built query
	 *
	 * @return The loader.
	 */
	public UniqueEntityLoader buildLoader(
			OuterJoinLoadable persister,
			int batchSize,
			LockOptions lockOptions,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers) {
		if ( batchSize <= 1 ) {
			// no batching
			return new EntityLoader( persister, lockOptions, factory, influencers );
		}
		return buildBatchingLoader( persister, batchSize, lockOptions, factory, influencers );
	}

	protected abstract UniqueEntityLoader buildBatchingLoader(
			OuterJoinLoadable persister,
			int batchSize,
			LockOptions lockOptions,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers influencers);
}
