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
package org.geotools.renderer3d.utils;

/**
 * An utlity class for counting number of frames per second.
 * <p/>
 * Usage: Create an instance, then every frame call onFrame().
 * The frames per second value can be read at any time using getFps().
 * <p/>
 * Thread safe, as long as onFrame is only called from a single thread.
 *
 * @author Hans H�ggstr�m
 */
public final class FpsCounter
{

    //======================================================================
    // Private Fields

    private final Object mySyncLock = new Object();

    // IDEA: Also calculate average over a number of frames, if a smoother value is needed, or if trend information is needed.
    private double myFramesPerSecond = -1;
    private double mySecondsBetweenFrames = -1;
    private long myFrameStartTime_ns = -1;
    private boolean myHasPreviousSample = false;

    //======================================================================
    // Private Constants

    private static final double TICKS_PER_SECOND = 1000000000.0;  // Nanoseconds

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * Creates a new FpsCounter.
     * Initially the frames per second property will be a negative value, until onFrame() is called (twice).
     */
    public FpsCounter()
    {
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * @return the number of frames rendered per second, or a negative value if the counter has not yet been started or has been stopped.
     */
    public double getFramesPerSecond()
    {
        synchronized ( mySyncLock )
        {
            return myFramesPerSecond;
        }
    }


    /**
     * @return number of seconds between the most recent frame and the frame before that,
     *         or a negative value if there were no earlier frames.
     *         Uses the averaged frames per second instead of the latest measurement.
     */
    public double getSecondsBetweenFrames()
    {
        synchronized ( mySyncLock )
        {
            return mySecondsBetweenFrames;
        }
    }


    /**
     * Call this method once each frame, to allow the counter to compute the frames per second property.
     * <p/>
     * NOTE: Should be called from a single thread, or wrapped in a syncronized object.  If called from two different
     * threads, there could be frames with negative fps or secondsBetweenFrames.
     */
    public void onFrame()
    {
        // Get start time of current frame
        final long now_ns = System.nanoTime();

        synchronized ( mySyncLock )
        {
            // Only update the fps property if we have two values already, so the duration is valid.
            if ( myHasPreviousSample )
            {
                // Calculate the duration since the last frame
                long duration_ns = now_ns - myFrameStartTime_ns;

                // Avoid division by zero if the frame rate is very high
                if ( duration_ns == 0 )
                {
                    duration_ns = 1;
                }

                // Update the frames per second and time since last frame property
                mySecondsBetweenFrames = ( 1.0 * duration_ns ) / TICKS_PER_SECOND;
                myFramesPerSecond = TICKS_PER_SECOND / duration_ns;
            }
            else
            {
                // Now we have one sample already, so next time we can update the property
                myHasPreviousSample = true;
            }

            // Remember current frame start time
            myFrameStartTime_ns = now_ns;
        }
    }


    /**
     * Sets the frames per second and seconds since last frame value to negative values, until onFrame() is called again.
     */
    public void stopCounting()
    {
        synchronized ( mySyncLock )
        {
            myFramesPerSecond = -1;
            mySecondsBetweenFrames = -1;
            myHasPreviousSample = false;
        }
    }

}


