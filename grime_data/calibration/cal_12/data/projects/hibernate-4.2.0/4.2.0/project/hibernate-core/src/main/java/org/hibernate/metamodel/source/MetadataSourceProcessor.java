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
package org.hibernate.metamodel.source;

import java.util.List;

import org.hibernate.metamodel.MetadataSources;

/**
 * Handles the processing of metadata sources in a dependency-ordered manner.
 *
 * @author Steve Ebersole
 */
public interface MetadataSourceProcessor {
	/**
	 * Prepare for processing the given sources.
	 *
	 * @param sources The metadata sources.
	 */
	public void prepare(MetadataSources sources);

	/**
	 * Process the independent metadata.  These have no dependency on other types of metadata being processed.
	 *
	 * @param sources The metadata sources.
	 *
	 * @see #prepare
	 */
	public void processIndependentMetadata(MetadataSources sources);

	/**
	 * Process the parts of the metadata that depend on type information (type definitions) having been processed
	 * and available.
	 *
	 * @param sources The metadata sources.
	 *
	 * @see #processIndependentMetadata
	 */
	public void processTypeDependentMetadata(MetadataSources sources);

	/**
	 * Process the mapping (entities, et al) metadata.
	 *
	 * @param sources The metadata sources.
	 * @param processedEntityNames Collection of any already processed entity names.
	 *
	 * @see #processTypeDependentMetadata
	 */
	public void processMappingMetadata(MetadataSources sources, List<String> processedEntityNames);

	/**
	 * Process the parts of the metadata that depend on mapping (entities, et al) information having been
	 * processed and available.
	 *
	 * @param sources The metadata sources.
	 *
	 * @see #processMappingMetadata
	 */
	public void processMappingDependentMetadata(MetadataSources sources);
}
