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

import java.awt.Color;

import com.tivo.hme.bananas.BButton;
import com.tivo.hme.bananas.BEvent;

public class DefaultOptionsScreen extends DefaultScreen {

	public DefaultOptionsScreen(DefaultApplication app) {
		super(app, "Options", false);

		BButton leftButton = new BButton(getNormal(), SAFE_TITLE_H, (getHeight() - SAFE_TITLE_V) - 35, (int) Math
				.round((getWidth() - (SAFE_TITLE_H * 2)) / 2.5), 35);
		leftButton.setBarAndArrows(BAR_HANG, BAR_DEFAULT, "pop", null, H_UP, H_DOWN, true);
		leftButton.getHighlights().get(H_UP).setVisible(H_VIS_FALSE);
		leftButton.getHighlights().get(H_DOWN).setVisible(H_VIS_FALSE);
		leftButton.setResource(createText("default-24.font", Color.white, "Return"));
		setFocus(leftButton);
	}

	public boolean handleKeyPress(int code, long rawcode) {
		switch (code) {
		case KEY_SELECT:
		case KEY_LEFT:
			postEvent(new BEvent.Action(this, "pop"));
			return true;
		}
		return super.handleKeyPress(code, rawcode);
	}
}