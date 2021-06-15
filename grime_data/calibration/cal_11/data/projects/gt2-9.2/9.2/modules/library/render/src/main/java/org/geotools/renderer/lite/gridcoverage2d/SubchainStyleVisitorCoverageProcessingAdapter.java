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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer.lite.gridcoverage2d;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.Hints;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.InternationalString;
/**
 * Helper class that implements a subchain.
 * @author      Simone Giannecchini, GeoSolutions.
 *
 *
 *
 * @source $URL$
 */
public abstract class SubchainStyleVisitorCoverageProcessingAdapter extends
		StyleVisitorCoverageProcessingNodeAdapter {

	/**
	 * Logger for this class. 
	 */
	private final static Logger LOGGER = Logging
			.getLogger(RasterSymbolizerHelper.class.getName());
	/**
	 * sink for the internal graph of {@link CoverageProcessingNode}s created
	 * to satisfy the provided SLD.
	 */
	private CoverageProcessingNode sink;

	public SubchainStyleVisitorCoverageProcessingAdapter(
			CoverageProcessingNode adaptee) {
		super(adaptee);
	}

	public SubchainStyleVisitorCoverageProcessingAdapter(
			InternationalString name, InternationalString description) {
		super(name, description);
	}

	public SubchainStyleVisitorCoverageProcessingAdapter(int maxSources,
			InternationalString name, InternationalString description) {
		super(maxSources, name, description);
	}

	public SubchainStyleVisitorCoverageProcessingAdapter(int maxSources,
			Hints hints, InternationalString name,
			InternationalString description) {
		super(maxSources, hints, name, description);
	}

	public synchronized void dispose(boolean force) {
		// dispose the graph of operations we set up
		dispose(sink, force);

		super.dispose(force);

	}

	private void dispose(CoverageProcessingNode node, boolean force) {

		if (node == null)
			return;
		// dispose the sources
		final List<CoverageProcessingNode> sources = node.getSources();
		if (sources != null && sources.size() > 0) {
			for (CoverageProcessingNode source:sources)
				dispose(source, force);
		}

		// dispose node
		node.dispose(force);

	}

	/**
     * Sets the sink   {@link CoverageProcessingNode}   for this chain.
     * @param  sink
     * @uml.property  name="sink"
     */
	protected synchronized void setSink(CoverageProcessingNode sink) {
		ensureNotNull(sink, "sink");
		if (this.sink != null)
			throw new IllegalStateException(Errors
					.format(ErrorKeys.SINK_ALREADY_SET));
		this.sink = sink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.renderer.lite.gridcoverage2d.StyleVisitorCoverageProcessingNodeAdapter#execute()
	 */
	public synchronized GridCoverage execute() {
		if (sink != null) {
			return sink.getOutput();
		}
		//log an helper message
		if(LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Sink for this chain is null, It was probably not set.");

		// something bad happened
		return null;
	}

}
