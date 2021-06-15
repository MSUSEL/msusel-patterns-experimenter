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
/**
 * 
 */
package org.geotools.coverage.io.geotiff;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffException;
import org.geotools.coverage.io.CoverageCapabilities;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.CoverageStore;
import org.geotools.coverage.io.CoverageUpdateRequest;
import org.geotools.coverage.io.CoverageResponse.Status;
import org.geotools.coverage.io.geotiff.GeoTiffAccess.Info;
import org.geotools.coverage.io.impl.DefaultCoverageResponseImpl;
import org.geotools.data.Parameter;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

/**
 * @author simone
 * 
 *
 *
 *
 * @source $URL$
 */
public class GeoTiffStore extends GeoTiffSource implements CoverageStore {

	GeoTiffStore(final GeoTiffAccess geotiff, final Name name) {
		super( geotiff, name );
		capabilities.addAll( EnumSet.of(
				CoverageCapabilities.WRITE_HORIZONTAL_DOMAIN_SUBSAMBLING,
				CoverageCapabilities.WRITE_RANGE_SUBSETTING,
				CoverageCapabilities.WRITE_SUBSAMPLING
		));		
	}
	public Map<String, Parameter<?>> getUpdateParameterInfo() {
		return Collections.emptyMap();
	}
	
	public CoverageResponse update(CoverageUpdateRequest writeRequest,
			ProgressListener progress) {
		ensureNotDisposed();
		access.globalLock.writeLock().lock();

		final DefaultCoverageResponseImpl response = new DefaultCoverageResponseImpl();
		response.setRequest(writeRequest);
		try {
			// reader
			final GeoTiffWriter writer = new GeoTiffWriter(this.access.input);

			// get the data
			final GridCoverage2D coverage = (GridCoverage2D) writeRequest.getData().iterator().next();
			writer.write(coverage, null);
			writer.dispose();
			response.addResult(coverage);
			response.setStatus(Status.SUCCESS);

			// update the access
			Info info = getInfo(null);
			info.setExtent( (GeneralEnvelope) coverage.getGridGeometry().getEnvelope() );
			info.setGeometry( coverage.getGridGeometry() );
		} catch (Throwable e) {
			response.addException(new GeoTiffException(null, "IO error", e));
		} finally {
			this.access.globalLock.writeLock().unlock();
		}

		return response;
	}

}
