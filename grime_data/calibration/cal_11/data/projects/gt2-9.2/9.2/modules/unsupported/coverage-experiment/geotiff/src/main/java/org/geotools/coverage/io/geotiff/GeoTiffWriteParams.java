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
package org.geotools.coverage.io.geotiff;


import java.util.Locale;

import javax.imageio.ImageWriteParam;

import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;

/**
 * Subclass of {@link GeoToolsWriteParams} the allows the user to specify
 * parameters to control the process of writing out a GeoTiff file through
 * standards {@link ImageWriteParam} (with possible extensions).
 * 
 * <p>
 * This class allows the user to control the output tile size for the GeoTiff
 * file we are going to create as well as the possible compression.
 * 
 * <p>
 * An example of usage of this parameters is as follows:
 * 
 * <pre>
 * <code>
 *       		//getting a format
 *       		final GeoTiffAccessFactory format = new GeoTiffAccessFactory();
 *       
 *      		//getting the write parameters
 *       		final GeoTiffWriteParams wp = new GeoTiffWriteParams();
 *       		
 *       		//setting compression to LZW
 *       		wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
 *       		wp.setCompressionType(&quot;LZW&quot;);
 *       		wp.setCompressionQuality(0.75F);
 *       		
 *       		//setting the tile size to 256X256
 *       		wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
 *       		wp.setTiling(256, 256);
 *       
 *       		//setting the write parameters for this geotiff
 *       		final ParameterValueGroup params = format.getWriteParameters();
 *       		params.parameter(
 *       				AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
 *       				.setValue(wp);
 *       
 *       		//get a reader to the input File
 *       		GridCoverageReader reader = new GeoTiffReader(inFile, null);
 *       		GridCoverageWriter writer = null;
 *       		GridCoverage2D gc = null;
 *       		if (reader != null) {
 *       
 *       			// reading the coverage
 *       			gc = (GridCoverage2D) reader.read(null);
 *   				if (gc != null) {
 *       					final File writeFile = new File(new StringBuffer(writedir
 *       							.getAbsolutePath()).append(File.separatorChar)
 *       							.append(gc.getName().toString()).append(&quot;.tiff&quot;)
 *       							.toString());
 *       					writer = format.getWriter(writeFile);
 *       					writer.write(gc, (GeneralParameterValue[]) params.values()
 *        			.toArray(new GeneralParameterValue[1]));
 *        		}
 * </code>
 * </pre>
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3.x
 * 
 */
class GeoTiffWriteParams extends GeoToolsWriteParams {

	/**
	 * Default constructor.
	 */
	public GeoTiffWriteParams() {
		super(new TIFFImageWriteParam(Locale.getDefault()));
	}

}
