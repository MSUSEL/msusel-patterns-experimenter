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
package org.lnicholls.galleon.winamp;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.awt.Image;

import com.tivo.hme.sdk.Resource;
import com.tivo.hme.sdk.View;

public class PositionControl extends View {

    public PositionControl(View parent, int x, int y, int width, int height, Image barImage, Image selectedImage,
            Image unselectedImage) {
        super(parent, x, y, width, height);
        mBarImage = barImage;
        mSelectedImage = selectedImage;
        mUnselectedImage = unselectedImage;
        setResource(createImage(mBarImage));
        mBar = new View(this, 0, 0, selectedImage.getWidth(null), selectedImage.getHeight(null));
        mBar.setResource(createImage(selectedImage));
    }

    private int mapPosition(int pos) {
        int current = (int) Math.round((getWidth() - mBar.getWidth()) * pos / 100);
        if (current < 0)
            current = 0;
        else if (current > (getWidth() - mBar.getWidth()))
            current = (getWidth() - mBar.getWidth());
        return current;
    }

    public void setPosition(int position) {
        if (position != mPosition) {
            int previous = mapPosition(mPosition);
            mPosition = position;
            int current = mapPosition(mPosition);
            
            Resource anim = getResource("*500,1");
            mBar.setLocation(current, 0, anim);

            /*
            if (current < previous) {
                Resource anim = getResource("*500,1");
                mBar.setLocation(current, 0, anim);
            } else {
                int diff = current - previous;
                if (mAverageDiff != 0)
                    mAverageDiff = (int) Math.round((diff + mAverageDiff) / 2.0);
                else
                    mAverageDiff = diff;

                Resource anim = getResource("*2000,0");
                mBar.setLocation(previous + mAverageDiff, 0, anim);
            }
            */
        }
    }

    public int getPosition() {
        return mPosition;
    }

    private View mBar;

    private int mPosition;

    private Image mBarImage;

    private Image mSelectedImage;

    private Image mUnselectedImage;

    private int mAverageDiff;
}