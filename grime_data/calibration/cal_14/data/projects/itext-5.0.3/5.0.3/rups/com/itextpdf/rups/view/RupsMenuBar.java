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
package com.itextpdf.rups.view;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.itextpdf.rups.io.FileChooserAction;
import com.itextpdf.rups.io.FileCloseAction;
import com.itextpdf.rups.io.filters.PdfFilter;

public class RupsMenuBar extends JMenuBar implements Observer {

	/** Caption for the file menu. */
	public static final String FILE_MENU = "File";
	/** Caption for "Open file". */
	public static final String OPEN = "Open";
	/** Caption for "Close file". */
	public static final String CLOSE = "Close";
	/** Caption for the help menu. */
	public static final String HELP_MENU = "Help";
	/** Caption for "Help about". */
	public static final String ABOUT = "About";
	/**
	 * Caption for "Help versions".
	 * @since iText 5.0.0 (renamed from VERSIONS)
	 */
	public static final String VERSION = "Version";
	
	/** The Observable object. */
	protected Observable observable;
	/** The action needed to open a file. */
	protected FileChooserAction fileChooserAction;
	/** The HashMap with all the actions. */
	protected HashMap<String, JMenuItem> items;
	
	/**
	 * Creates a JMenuBar.
	 * @param observable	the controller to which this menu bar is added
	 */
	public RupsMenuBar(Observable observable) {
		this.observable = observable;
		items = new HashMap<String, JMenuItem>();
		fileChooserAction = new FileChooserAction(observable, "Open", PdfFilter.INSTANCE, false);
		MessageAction message = new MessageAction();
		JMenu file = new JMenu(FILE_MENU);
		addItem(file, OPEN, fileChooserAction);
		addItem(file, CLOSE, new FileCloseAction(observable));
		add(file);
        add(Box.createGlue());
        JMenu help = new JMenu(HELP_MENU);
        addItem(help, ABOUT, message);
        addItem(help, VERSION, message);
        add(help);
		enableItems(false);
	}
	
	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		if (OPEN.equals(obj)) {
			enableItems(true);
			return;
		}
		if (CLOSE.equals(obj)) {
			enableItems(false);
			return;
		}
		if (FILE_MENU.equals(obj)) {
			fileChooserAction.actionPerformed(null);
		}
	}
	
	/**
	 * Create an item with a certain caption and a certain action,
	 * then add the item to a menu.
	 * @param menu	the menu to which the item has to be added
	 * @param caption	the caption of the item
	 * @param action	the action corresponding with the caption
	 */
	protected void addItem(JMenu menu, String caption, ActionListener action) {
		JMenuItem item = new JMenuItem(caption);
		item.addActionListener(action);
		menu.add(item);
		items.put(caption, item);
	}
	
	/**
	 * Enables/Disables a series of menu items.
	 * @param enabled	true for enabling; false for disabling
	 */
	protected void enableItems(boolean enabled) {
		enableItem(CLOSE, enabled);
	}
	
	/**
	 * Enables/disables a specific menu item
	 * @param caption	the caption of the item that needs to be enabled/disabled
	 * @param enabled	true for enabling; false for disabling
	 */
	protected void enableItem(String caption, boolean enabled) {
		items.get(caption).setEnabled(enabled);
	}
	
	/** A Serial Version UID. */
	private static final long serialVersionUID = 6403040037592308742L;
}