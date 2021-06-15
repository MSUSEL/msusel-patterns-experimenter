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
package com.itextpdf.rups;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.itextpdf.rups.controller.RupsController;

/**
 * iText RUPS is a tool that allows you to inspect the internal structure
 * of a PDF file.
 */
public class Rups {
	
	// main method
	/**
	 * Main method. Starts the RUPS application.
	 * @param	args	no arguments needed
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(
		        new Runnable(){
		            public void run() {
		                startApplication();
		            }
		        }
		        );
	}
	
	// methods
	
    /**
     * Initializes the main components of the Rups application.
     */
    public static void startApplication() {
    	// creates a JFrame
    	JFrame frame = new JFrame();
        // defines the size and location
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int)(screen.getWidth() * .90), (int)(screen.getHeight() * .90));
        frame.setLocation((int)(screen.getWidth() * .05), (int)(screen.getHeight() * .05));
        frame.setResizable(true);
        // title bar
        frame.setTitle("iText RUPS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // the content
        RupsController controller = new RupsController(frame.getSize());
        frame.setJMenuBar(controller.getMenuBar());
        frame.getContentPane().add(controller.getMasterComponent(), java.awt.BorderLayout.CENTER);
		frame.setVisible(true);
    }
	
	// other member variables
	
	/** Serial Version UID. */
	private static final long serialVersionUID = 4386633640535735848L;

}