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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.image;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class WorldImageBaseTestCase extends TestCase {
	public WorldImageBaseTestCase(String name) {
		super(name);
	}

	/**
	 * Helper class used to filter files basing the decision on the file
	 * extensions. It is not so smart I know.... :-(
	 * 
	 * @author giannecchini
	 */
	class MyFileFilter implements FilenameFilter {
		private HashSet extensions = new HashSet();

		public MyFileFilter() {
			String[] extensions = new String[] { ".gif", ".jpg", ".jpeg",
					".tif", ".tiff", ".png", ".bmp" };
			this.setExtensions(extensions);
		}

		public MyFileFilter(String[] extensions) {
			this.setExtensions(extensions);
		}

		/**
		 * Sets the extensions that are allowed
		 * 
		 * @param extensions
		 */
		private void setExtensions(String[] extensions) {
			if (extensions != null) {
				this.extensions.clear();

				for (int i = 0; i < extensions.length; i++)
					this.extensions.add(extensions[i]);
			}
		}

		/**
		 * Checks whether or not a file is acceptable following the conditions
		 * stated by the given extensions.
		 * 
		 * @param file
		 *            DOCUMENT ME!
		 * @param name
		 *            DOCUMENT ME!
		 * 
		 * @return DOCUMENT ME!
		 */
		public boolean accept(File file, String name) {
			if (this.extensions.size() > 0) {
				Iterator it = this.extensions.iterator();

				while (it.hasNext())

					if (name.endsWith((String) it.next())) {
						return true;
					}
			}

			return false;
		}
	}

}
