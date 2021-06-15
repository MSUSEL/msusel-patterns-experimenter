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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;
import org.geotools.util.Utilities;

/**
 * Enum that can be use to distinguish between relative paths and absolute paths
 * when trying to load a granuleDescriptor for a mosaic.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * 
 *
 *
 * @source $URL$
 */
public enum PathType {
	RELATIVE{

		@Override
		URL resolvePath(final String parentLocation,final  String location) {
			// initial checks
			Utilities.ensureNonNull("parentLocation", parentLocation);
			Utilities.ensureNonNull("location", location);
			if(LOGGER.isLoggable(Level.FINE))
			{
				final StringBuilder builder = new StringBuilder();
				builder.append("Trying to resolve path:").append("\n");
				builder.append("type:").append(this.toString()).append("\n");
				builder.append("parentLocation:").append(parentLocation).append("\n");
				builder.append("location:").append(location);
				LOGGER.fine(builder.toString());
			}
			// create a URL for the provided location, relative to parent location
			try {
				URL rasterURL= DataUtilities.extendURL(new URL(parentLocation), location);
			if(!Utils.checkURLReadable(rasterURL))
			{		
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info("Unable to read image for file "+ rasterURL);

				return null;

			}		
			return rasterURL;
			} catch (MalformedURLException e) {
				return null;
			}
		}
		
	},
	
	ABSOLUTE{

		@Override
		URL resolvePath(final String parentLocation,final  String location) {

			Utilities.ensureNonNull("location", location);
			if(LOGGER.isLoggable(Level.FINE))
			{
				final StringBuilder builder = new StringBuilder();
				builder.append("Trying to resolve path:").append("\n");
				builder.append("type:").append(this.toString()).append("\n");
				if(parentLocation!=null)
					builder.append("parentLocation:").append(parentLocation).append("\n");
				LOGGER.fine(builder.toString());	
			}
			// create a file for the provided location ignoring the parent type
			// create a URL for the provided location, relative to parent location
			try {
			    File rasterFile= new File(location);
	                      if(!Utils.checkFileReadable(rasterFile)){               
	                          URL rasterURL= new URL(location);
	                          if(!Utils.checkURLReadable(rasterURL))
	                          {               
	                                  if (LOGGER.isLoggable(Level.INFO))
	                                          LOGGER.info("Unable to read image for file "+ rasterURL);

	                                  return null;

	                          }               
	                          return rasterURL;
	                              
	                      } else {
	                          return DataUtilities.fileToURL(rasterFile);
	                      }
			
			} catch (MalformedURLException e) {
				return null;
			}
		}
		
	};
	
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(PathType.class);

	/**
	 * Resolve a path for a granuleDescriptor given the parent location and location
	 * itself.
	 * 
	 * <p>
	 * the location can never be null, while the parent location could be null,
	 * as an instance when the path is relative.
	 * 
	 * @param parentLocation
	 * @param location
	 * @return a {@link File} instance that points to a location which could be
	 *         relative or absolute depending on the flavor of the enum where
	 *         this method is applied. This method might return <code>null</code>
	 *         in case something bad happens.
	 */
	abstract URL resolvePath(
			final String parentLocation,
			final String location);
	
}
