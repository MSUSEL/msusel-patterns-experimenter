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
package org.archive.crawler.selftest;


import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.archive.io.arc.ARCRecordMetaData;


/**
 * Test the crawler can find background images in pages.
 *
 * @author stack
 * @version $Id: BackgroundImageExtractionSelfTestCase.java 4931 2007-02-21 18:48:17Z gojomo $
 */
public class BackgroundImageExtractionSelfTestCase
    extends SelfTestCase
{
    /**
     * The name of the background image the crawler is supposed to find.
     */
    private static final String IMAGE_NAME = "example-background-image.jpeg";

    private static final String JPEG = "image/jpeg";


    /**
     * Read ARC file for the background image the file that contained it.
     *
     * Look that there is only one instance of the background image in the
     * ARC and that it is of the same size as the image in the webapp dir.
     */
    public void stestBackgroundImageExtraction()
    {
        assertInitialized();
        String relativePath = getTestName() + '/' + IMAGE_NAME;
        String url = getSelftestURLWithTrailingSlash() + relativePath;
        File image = new File(getHtdocs(), relativePath);
        assertTrue("Image exists", image.exists());
        List [] metaDatas = getMetaDatas();
        boolean found = false;
        ARCRecordMetaData metaData = null;
        for (int mi = 0; mi < metaDatas.length; mi++) {
			List list = metaDatas[mi];
			for (final Iterator i = list.iterator(); i.hasNext();) {
				metaData = (ARCRecordMetaData) i.next();
				if (metaData.getUrl().equals(url)
						&& metaData.getMimetype().equalsIgnoreCase(JPEG)) {
					if (!found) {
						found = true;
					} else {
						fail("Found a 2nd instance of " + url);
					}
				}
			}
		}
    }
}