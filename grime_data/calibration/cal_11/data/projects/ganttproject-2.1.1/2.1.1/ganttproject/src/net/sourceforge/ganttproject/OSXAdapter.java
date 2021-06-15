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

import java.io.IOException;

import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.gui.about.AboutDialog;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

public class OSXAdapter extends ApplicationAdapter {
	private static OSXAdapter osxAdapter;
	private static com.apple.eawt.Application theApp;
	private GanttProject myProj;

	private OSXAdapter(GanttProject myProj) {
		this.myProj = myProj;
	}

	/*
	 * This method handles the case when a file in the Finder is dropped onto
	 * the app, or GanttProject is selected via the open-with menu option. The
	 * event argument contains the path of the file in either case.
	 */
	public void handleOpenFile(ApplicationEvent event) {
		String file;
		Document myDocument;

		if (myProj.checkCurrentProject() == true) {
			file = event.getFilename();
			myDocument = myProj.getDocumentManager().getDocument(file);
			try {
				myProj.getProjectUIFacade().openProject(myDocument,
						myProj.getProject());
			} catch (IOException e) {
				myProj.getUIFacade().showErrorDialog(e);
			}
		}
		event.setHandled(true);
	}

	/*
	 * Handle the Mac OSX "about" menu option.
	 */

	public void handleAbout(ApplicationEvent event) {
		AboutDialog abd = new AboutDialog(myProj);
		abd.setVisible(true);
		/* Indicate we've handled this event ourselves */
		event.setHandled(true);
	}

	/*
	 * Handles the quit menu option (defaults to command-q) the same way
	 * choosing Project->Quit does.
	 */
	public void handleQuit(ApplicationEvent event) {
		myProj.quitApplication();
		/*
		 * Not a typo. Must set handled to false else the app will still quit
		 * even if we say "cancel" on confirmation.
		 */
		event.setHandled(false);
	}

	public static void registerMacOSXApplication(GanttProject myProj) {
		if (theApp == null) {
			theApp = new com.apple.eawt.Application();
		}

		if (osxAdapter == null) {
			osxAdapter = new OSXAdapter(myProj);
		}

		theApp.addApplicationListener(osxAdapter);
	}
}
