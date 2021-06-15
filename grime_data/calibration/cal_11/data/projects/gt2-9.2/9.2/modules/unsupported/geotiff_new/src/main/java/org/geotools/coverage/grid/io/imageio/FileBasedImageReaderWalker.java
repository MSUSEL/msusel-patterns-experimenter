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
package org.geotools.coverage.grid.io.imageio;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.geotools.image.io.ImageIOExt;
import org.geotools.util.Utilities;


/**
 * 
 *
 * @source $URL$
 */
public abstract class FileBasedImageReaderWalker extends ImageReaderWalker {

    /** Logger for the {@link FileBasedImageReaderWalker} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FileBasedImageReaderWalker.class.toString());

    /**
     * url to the  file.
     */
    public static final Parameter<URL> URLP = new Parameter<URL>("url", URL.class);
    
    protected final URL sourceURL;
    
    protected final boolean hasExternalOverviews;
    
    protected final URL externalOverviewsURL;

    protected final File sourceFile;

    protected final File externalOverviewsFile;
    
    public FileBasedImageReaderWalker(
            URL sourceURL, 
            Hints hints,
            ImageReaderSpi readerSPI) throws IOException {
//        TODO avoid SPI creation
        super(URLToParams(sourceURL), hints, ImageIOExt.getImageInputStreamSPI(DataUtilities.urlToFile(sourceURL), true), readerSPI);
      this.sourceURL = sourceURL;
      
      // look for external levels
      sourceFile= DataUtilities.urlToFile(getSource());
      externalOverviewsFile= new File(sourceFile.getCanonicalPath()+".ovr");
      hasExternalOverviews=externalOverviewsFile.exists()&&externalOverviewsFile.isFile()&&externalOverviewsFile.canRead();
      externalOverviewsURL= externalOverviewsFile.toURI().toURL();
      
      // now do the real work
      initialize();
    }


    private final static Map<String, Serializable> URLToParams(URL url) {
        Utilities.ensureNonNull("SourceURL", url);
        Map<String, java.io.Serializable> params = new HashMap<String, java.io.Serializable>();
        params.put(URLP.key, url);
        return params;
    }

    public URL getSource() {
        return sourceURL;
    }

    // TODO we should try to avoid this lookup as much as possible
    @Override
    protected ImageInputStream createImageInputStream() throws IOException {
        return ImageIOExt.createImageInputStream(DataUtilities.urlToFile(sourceURL));
    }


    @Override
    protected ImageReaderSource<?> getSource(int i) throws IOException {
        return ImageReaderSource.wrapFile(i,sourceFile, super.inStreamSPI, super.readerSPI);
    }
}
