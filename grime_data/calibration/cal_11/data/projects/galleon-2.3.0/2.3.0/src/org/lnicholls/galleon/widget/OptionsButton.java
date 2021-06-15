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
package org.lnicholls.galleon.widget;

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

import java.awt.*;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.tivo.hme.bananas.BHighlight;
import com.tivo.hme.bananas.BHighlights;
import com.tivo.hme.bananas.BScreen;
import com.tivo.hme.bananas.BText;
import com.tivo.hme.bananas.BView;
import com.tivo.hme.bananas.*;
import com.tivo.hme.sdk.Resource;

import org.lnicholls.galleon.util.*;

/*
 * Based on TiVo HME Bananas BList by Adam Doppelt
 */

public class OptionsButton extends BButton {

    /**
     * Creates a new BButton instance.
     *
     * @param parent parent
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     */
    public OptionsButton(BView parent, int x, int y, int width, int height)
    {
        this(parent, x, y, width, height, true);
    }
    
    /**
     * Creates a new BButton instance.
     *
     * @param parent parent
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     * @param visible if true, make the view visibile
     */
    public OptionsButton(BView parent, int x, int y, int width, int height, boolean visible)
    {
    	this(parent, x, y, width, height, visible, new NameValue[0]);
    }
    
    /**
     * Creates a new BButton instance.
     *
     * @param parent parent
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     * @param visible if true, make the view visibile
     * @param nameValues array of NameValues for options 
     */
    public OptionsButton(BView parent, int x, int y, int width, int height, boolean visible, NameValue[] nameValues)
    {
    	this(parent, x, y, width, height, visible, nameValues, null);
    }
    
    /**
     * Creates a new BButton instance.
     *
     * @param parent parent
     * @param x x
     * @param y y
     * @param width width
     * @param height height
     * @param visible if true, make the view visibile
     * @param nameValues array of NameValues for options 
     * @param defaultValue default value 
     */
    public OptionsButton(BView parent, int x, int y, int width, int height, boolean visible, NameValue[] nameValues, String defaultValue)
    {
    	super(parent, x, y, width, height, visible);
    	mNameValues = nameValues;
    	setBarAndArrows(BAR_DEFAULT, BAR_DEFAULT, "pop", H_RIGHT, H_UP, H_DOWN, true);
    	getHighlights().get(H_UP).setVisible(H_VIS_FALSE);
    	getHighlights().get(H_DOWN).setVisible(H_VIS_FALSE);
    	setName(defaultValue);
    }    
    
    public boolean handleKeyPress(int code, long rawcode) {
    	switch (code) {
    	case KEY_LEFT:
            if (mPos==0)
            	mPos = mNameValues.length-1;
            else
            	mPos = (mPos - 1)%mNameValues.length;
            setName();
            getApp().play("left.snd");
            getApp().flush();
            return true;
        case KEY_RIGHT:
            mPos = (mPos + 1)%mNameValues.length;
            setName();
            getApp().play("right.snd");
            getApp().flush();
            return true;
        }
    	return super.handleKeyPress(code, rawcode);
    }
    
    private void setName(String defaultValue)
    {
    	for (int i=0;i<mNameValues.length;i++)
    	{
    		if (mNameValues[i].getValue().equals(defaultValue))
    		{
    			mPos = i;
    			break;
    		}
    	}
    	setName();
    }
    
    private void setName()
    {
        setResource(createText("default-24.font", Color.white, mNameValues[mPos].getName()));
    }
    
    public String getValue()
    {
    	return mNameValues[mPos].getValue();
    }
    
    private int mPos;
    
    private NameValue[] mNameValues;
}