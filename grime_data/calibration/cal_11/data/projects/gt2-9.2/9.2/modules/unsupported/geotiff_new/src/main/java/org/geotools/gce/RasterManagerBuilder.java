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
package org.geotools.gce;

import java.io.IOException;
import java.util.List;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import org.geotools.coverage.grid.io.imageio.ImageReaderSource;
import org.geotools.factory.Hints;
import org.geotools.util.Utilities;


/**
 * 
 *
 * @source $URL$
 */
public abstract class RasterManagerBuilder<T extends ImageReader> {

    protected final Hints hints;

    public abstract void parseStreamMetadata(IIOMetadata streamMetadata)throws IOException ;
    
    public RasterManagerBuilder(Hints hints) {
        Utilities.ensureNonNull("hints", hints);
        this.hints = hints;
    }

    /**
     * Uses the provided {@link ImageReader} to get relevant information about this element.
     * This usually means, getting basic info from the reader itself as well as geting the 
     * {@link IIOMetadata} and parsing it.
     * 
     * <p>
     * Notice that we should <strong>NOT</strong> take ownership of this provided reader.
     * It is duty of the caller to close it properly.
     * 
     * @param i element position in the {@link ImageReader} indexese range.
     * @param reader
     * @throws IOException
     */
    public abstract void addElement(int i, T reader, ImageReaderSource<?> source) throws IOException;
    
    public boolean needsStreamMetadata() {
        return false;
    }

    public abstract List<RasterManager> create() ;

    public abstract void dispose() ;

}
    
