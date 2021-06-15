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

import java.awt.image.BufferedImage;

import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.event.IIOReadWarningListener;

import org.opengis.util.ProgressListener;
/**
 * This class provide a means to wrap a GeoTools {@link ProgressListener}
 * and have it control an {@link ImageReader} while it is actually
 * doing a {@link ImageReader#read(int)} operation.
 * 
 * We also give user the ability to cancel the reading process
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class GridCoverageReaderProgressAdapter extends BaseGridCoverageProgressAdapter implements IIOReadProgressListener,
        IIOReadUpdateListener, IIOReadWarningListener {

    public GridCoverageReaderProgressAdapter(ProgressListener monitor, int numImages) {
        super(monitor, numImages);    }

    public GridCoverageReaderProgressAdapter(ProgressListener monitor) {
        this(monitor, 1);
    }

    @Override
    public void warningOccurred(ImageReader source, String warning) {
        monitor.warningOccurred(source.getInput().toString(), "Warning writing image:"+lastImageIndex, warning);

    }

    @Override
    public void passStarted(ImageReader source, BufferedImage theImage, int pass, int minPass,
            int maxPass, int minX, int minY, int periodX, int periodY, int[] bands) {
     // ignore

    }

    @Override
    public void imageUpdate(ImageReader source, BufferedImage theImage, int minX, int minY,
            int width, int height, int periodX, int periodY, int[] bands) {
     // ignore

    }

    @Override
    public void passComplete(ImageReader source, BufferedImage theImage) {
     // ignore

    }

    @Override
    public void thumbnailPassStarted(ImageReader source, BufferedImage theThumbnail, int pass,
            int minPass, int maxPass, int minX, int minY, int periodX, int periodY, int[] bands) {
     // ignore

    }

    @Override
    public void thumbnailUpdate(ImageReader source, BufferedImage theThumbnail, int minX, int minY,
            int width, int height, int periodX, int periodY, int[] bands) {
     // ignore

    }

    @Override
    public void thumbnailPassComplete(ImageReader source, BufferedImage theThumbnail) {
     // ignore

    }

    @Override
    public void sequenceStarted(ImageReader source, int minIndex) {
     // ignore

    }

    @Override
    public void sequenceComplete(ImageReader source) {
     // ignore

    }

    @Override
    public void imageStarted(ImageReader source, int imageIndex) {
        if(imageIndex==0)
            monitor.started();
        
        // register progress
        lastImageIndex=imageIndex;
        float progress = lastImageIndex*progressStep*100;
        
        // report progress and check stop
        reportProgress(progress, source);   

    }

    @Override
    public void imageProgress(ImageReader source, float percentageDone) {
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
    public void imageComplete(ImageReader source) {
        
        // register progress
        float progress = (lastImageIndex+1)*progressStep*100;
        
        // report progress and check stop
        reportProgress(progress, source);  
        
        // are we done?
        if(lastImageIndex==(numImages-1))
            monitor.complete();
    }

    @Override
    public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) {
     // ignore

    }

    @Override
    public void thumbnailProgress(ImageReader source, float percentageDone) {
     // ignore

    }

    @Override
    public void thumbnailComplete(ImageReader source) {
     // ignore

    }

    @Override
    public void readAborted(ImageReader source) {
        // we should not do anything as this is triggered
        // mostly by us using #setCancel

    }

    /**
     * @param progress 
     * @param writer 
     * 
     */
    private void reportProgress(float progress, ImageReader reader) {
        monitor.progress(progress);
        if(monitor.isCanceled())
            reader.abort();
    }

}
