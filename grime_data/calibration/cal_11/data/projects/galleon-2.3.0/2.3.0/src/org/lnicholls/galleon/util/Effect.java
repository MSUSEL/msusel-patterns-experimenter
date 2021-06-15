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
package org.lnicholls.galleon.util;

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

import org.lnicholls.galleon.widget.Callback;
import org.lnicholls.galleon.widget.DefaultApplication;

import com.tivo.hme.sdk.Application;
import com.tivo.hme.sdk.HmeEvent;
import com.tivo.hme.sdk.Resource;
import com.tivo.hme.sdk.View;

public abstract class Effect implements Callback {
	public abstract void apply(View view, Image image);

	public void wait(View view, Resource anim) {
		mCurrentView = view;

		HmeEvent evt = new HmeEvent.Key(mCurrentView.getApp().getID(), 0, Application.KEY_TIVO, mCurrentView.getID());
		mCurrentView.getApp().sendEvent(evt, anim);
		mCurrentView.getApp().flush();
		((DefaultApplication) mCurrentView.getApp()).addCallback(this);
		try {
			// System.gc();
			wait();
		} catch (Exception ex) {

		}
	}

	public synchronized boolean handleEvent(HmeEvent event) {
		if (mCurrentView != null) {
			switch (event.getOpCode()) {
			case Application.EVT_KEY: {
				HmeEvent.Key e = (HmeEvent.Key) event;
				switch (e.getCode()) {
				case Application.KEY_TIVO:
					int code = (int) e.getRawCode();
					if ((code) == mCurrentView.getID()) {
						if (mCurrentView.getResource() != null)
						{
							mCurrentView.getResource().flush();
							mCurrentView.getResource().remove();
						}
						Application app = mCurrentView.getApp();
						mCurrentView.flush();
						mCurrentView.remove();
						mCurrentView = null;
						app.flush();
						notifyAll();
						return true;
					}
				}
				break;
			}
			}
		}
		return false;
	}

	public int getDelay() {
		return mDelay;
	}

	public void setDelay(int value) {
		mDelay = value;
	}

	private int mDelay = 5000;

	private View mCurrentView;

}