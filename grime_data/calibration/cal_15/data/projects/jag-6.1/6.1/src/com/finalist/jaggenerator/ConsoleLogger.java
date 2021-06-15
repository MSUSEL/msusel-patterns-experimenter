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
package com.finalist.jaggenerator;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.lf5.LogLevel;

/**
 * Yet another logger implementation... ;) This logger logs to the JTextArea in
 * the JAG gui.
 * 
 * @author Michael O'Connor - Finalist IT Group
 */
public class ConsoleLogger {

    public static final HashMap styleMap = new HashMap();
    
    static SimpleAttributeSet BLACK = new SimpleAttributeSet();    
    static SimpleAttributeSet YELLOW = new SimpleAttributeSet();
    static SimpleAttributeSet RED = new SimpleAttributeSet();

    static {
        styleMap.put(LogLevel.INFO, BLACK);
        styleMap.put(LogLevel.DEBUG, YELLOW);
        styleMap.put(LogLevel.ERROR, RED);
        
        StyleConstants.setForeground(BLACK, Color.BLACK);
        StyleConstants.setFontFamily(BLACK, "Lucida");
        StyleConstants.setFontSize(BLACK, 10);
        
        StyleConstants.setForeground(YELLOW, Color.YELLOW);
        StyleConstants.setFontFamily(YELLOW, "Lucida");
        StyleConstants.setFontSize(YELLOW, 10);
        
        StyleConstants.setForeground(RED, Color.RED);
        StyleConstants.setFontFamily(RED, "Lucida");
        StyleConstants.setFontSize(RED, 10);
    }

    private boolean colored;
    
    private JTextComponent genericConsole;
    /**
     * Creates a ConsoleLogger.
     * 
     * @param console
     */
    public ConsoleLogger(JTextArea console) {
        setGenericConsole(console);
        setColored(false);
    }
    /**
     * Creates a ConsoleLogger with color support.
     * 
     * @param console
     */
    public ConsoleLogger(JTextPane console) {
        setGenericConsole(console);
        setColored(true);        
    }

    /**
     * Appends a log to the end of the console.
     * 
     * @param message
     *            the log message.
     */
    public void log(String message) {
        log(message, LogLevel.INFO);
    }

    /**
     * Appends a log to the end of the console.
     * 
     * @param message
     *            the log message.
     * @param level
     *            the level, log4j style. 
     */
    public void log(String message, LogLevel level) {        
        if(isColored())
        {            
            insertText(message, getSyle4Level(level));
        } else
            insertText(message, BLACK);
        insertText("\n", BLACK);
        setEndSelection();        
    }

    private SimpleAttributeSet getSyle4Level(LogLevel level) {
        SimpleAttributeSet c = (SimpleAttributeSet ) ConsoleLogger.styleMap.get(level);
        SimpleAttributeSet  ret = BLACK;
        if (c != null)
            ret = c;

        return ret;
    }
    public boolean isColored() {
        return colored;
    }
    public void setColored(boolean colored) {
        this.colored = colored;
    }
    public JTextComponent getGenericConsole() {
        return genericConsole;
    }
    public void setGenericConsole(JTextComponent genericConsole) {
        this.genericConsole = genericConsole;
    }
    
    protected void insertText(String text, AttributeSet set) {
        try {
          genericConsole.getDocument().insertString(
                  genericConsole.getDocument().getLength(), text, set);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }
      }

    // Needed for inserting icons in the right places
    protected void setEndSelection() {
      genericConsole.setSelectionStart(genericConsole.getDocument().getLength());
      genericConsole.setSelectionEnd(genericConsole.getDocument().getLength());
    }
    
    /**
     * Appends an red error log to the end of the console. 
     * 
     * @param message
     *            the log message.
     */
    public void error(String message) {
        log(message, LogLevel.ERROR);
    }
    
    /**
     * Appends an yellow debug log to the end of the console. 
     * 
     * @param message
     *            the log message.
     */
    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }
}
