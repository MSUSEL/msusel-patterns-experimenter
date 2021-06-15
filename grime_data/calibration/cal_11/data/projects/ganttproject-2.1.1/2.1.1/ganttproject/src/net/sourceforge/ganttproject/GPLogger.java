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
package net.sourceforge.ganttproject;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.ganttproject.gui.UIFacade;


public class GPLogger {
	private static Logger ourLogger = Logger.getLogger("org.ganttproject");
	private static Handler ourHandler;
	private static UIFacade ourUIFacade;

	public static void setup() {
        ourHandler = new ConsoleHandler();
		ourLogger.addHandler(ourHandler);
		ourLogger.setLevel(Level.ALL);
		ourHandler.setFormatter(new java.util.logging.SimpleFormatter());
	}

	public static boolean log(Throwable e) {
		if (ourHandler == null) {
			return false;
		}
		ourLogger.log(Level.WARNING, e.getMessage(), e);
		if (ourUIFacade != null) {
			ourUIFacade.logErrorMessage(e);
		}
		return true;
	}

	public static void log(String message) {
		ourLogger.log(Level.INFO, message);
	}

	public static void setUIFacade(UIFacade uifacade) {
		ourUIFacade = uifacade;
	}
}
