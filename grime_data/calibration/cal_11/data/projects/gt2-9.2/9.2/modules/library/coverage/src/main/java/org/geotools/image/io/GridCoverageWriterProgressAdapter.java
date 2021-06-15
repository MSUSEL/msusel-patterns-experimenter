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
 *    (C) 2001-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.event.IIOWriteWarningListener;

import org.opengis.util.ProgressListener;
/**
 * This class provide a means to wrap a GeoTools {@link ProgressListener}
 * and have it control an {@link ImageWriter} while it is actually
 * doing a {@link ImageWriter#write(javax.imageio.IIOImage)} operation.
 * 
 * We also give user the ability to cancel the writing process
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class GridCoverageWriterProgressAdapter extends BaseGridCoverageProgressAdapter implements
        IIOWriteProgressListener, IIOWriteWarningListener {
    
    public GridCoverageWriterProgressAdapter(final ProgressListener monitor,int numImages) {
        super(monitor,numImages);
    }
    
    public GridCoverageWriterProgressAdapter(final ProgressListener monitor) {
       this(monitor, 1);
    }

    @Override
    public void warningOccurred(ImageWriter source, int imageIndex, String warning) {
        monitor.warningOccurred(source.getOutput().toString(), "Warning writing image:"+imageIndex, warning);
        
    }

    @Override
    public void imageStarted(ImageWriter source, int imageIndex) {
        if(imageIndex==0)
            monitor.started();
        
        // register progress
        lastImageIndex=imageIndex;
        float progress = lastImageIndex*progressStep*100;
        
        // report progress and check stop
        reportProgress(progress, source);      
        
    }

    /**
     * @param progress 
     * @param writer 
     * 
     */
    private void reportProgress(float progress, ImageWriter writer) {
        monitor.progress(progress);
        if(monitor.isCanceled())
            writer.abort();
    }

    @Override
    public void imageProgress(ImageWriter source, float percentageDone) {
        // register progress
        float tempProgress = lastImageIndex*progressStep*100+percentageDone*progressStep;
        if(tempProgress-progress>5.0){
            // report progress and check stop
            reportProgress(tempProgress, source); 
       
            // update
            progress=tempProgress;
        }
    }

    @Override
    public void imageComplete(ImageWriter source) {
        
        // register progress
        float progress = (lastImageIndex+1)*progressStep*100;
        
        // report progress and check stop
        reportProgress(progress, source);  
        
        // are we done?
        if(lastImageIndex==(numImages-1))
            monitor.complete();
        
    }

    @Override
    public void thumbnailStarted(ImageWriter source, int imageIndex, int thumbnailIndex) {
     // ignore
        
    }

    @Override
    public void thumbnailProgress(ImageWriter source, float percentageDone) {
        // ignore
        
    }

    @Override
    public void thumbnailComplete(ImageWriter source) {
     // ignore
        
    }

    @Override
    public void writeAborted(ImageWriter source) {
        // we should not do anything as this is triggered
        // mostly by us using #setCancel
        
    }


}
