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
package org.geotools.referencing.operation.transform;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.geotools.referencing.factory.gridshift.DataUtilities;
import org.geotools.referencing.factory.gridshift.NADCONGridShiftFactory;
import org.geotools.referencing.factory.gridshift.NADConGridShift;
import org.junit.Test;
import org.opengis.referencing.FactoryException;

public class NADCONGridShiftFactoryTest {

	@Test 
	public void testReleaseGrids() throws IOException, FactoryException {
		File gridShifts = new File("src/test/resources/org/geotools/referencing/factory/gridshift");
		File las = new File(gridShifts, "stpaul.las");
		File los = new File(gridShifts, "stpaul.los");
		
		File tlas = new File("./target/stpaul.las");
		File tlos = new File("./target/stpaul.los");
		copyFile(las, tlas);
		copyFile(los, tlos);
		
		NADCONGridShiftFactory factory = new NADCONGridShiftFactory();
		NADConGridShift shift = factory.loadGridShift(DataUtilities.fileToURL(tlas), DataUtilities.fileToURL(tlos));
		// minor checks on the grid
		assertNotNull(shift);
		
		// now the good part, try to delete the files, on windows this will fail
		// unless the sources were properly closed
		assertTrue(tlas.delete());
		assertTrue(tlos.delete());
	}

	private void copyFile(File src, File dst) throws IOException  {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(src);
			fos = new FileOutputStream(dst);
			byte[] buffer = new byte[4096];
			int read = 0;
			while((read = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, read);
			}
		} finally {
			if(fis != null) {
				fis.close();
			}
			if(fos != null) {
				fos.close();
			}
		}
		
	}
}

