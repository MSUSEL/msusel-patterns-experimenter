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
 * Created on 29.09.2003
 *
 */
package net.sourceforge.ganttproject.document;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 * @author Michael Haeusler (michael at akatose.de)
 */
public class OpenDocumentAction extends AbstractAction {

    private Document document;

    private ActionListener listener;

    private static final int MENU_MASK = Toolkit.getDefaultToolkit()
            .getMenuShortcutKeyMask();

    private static final int[] SHORTCUT_KEYS = { KeyEvent.VK_F1,
            KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5,
            KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9,
            KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12 };

    private static final int[] MNEMONIC_KEYS = { KeyEvent.VK_1, KeyEvent.VK_2,
            KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6,
            KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9 };

    /**
     * Creates a new action, that stores the specified document and invokes the
     * specified listener.
     */
    public OpenDocumentAction(int index, Document document,
            ActionListener listener) {
        super(index + ". " + document.getDescription());
        this.document = document;
        this.listener = listener;

        if (index < MNEMONIC_KEYS.length) {
            putValue(MNEMONIC_KEY, new Integer(MNEMONIC_KEYS[index - 1]));
        }

        if (index < SHORTCUT_KEYS.length)
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                    SHORTCUT_KEYS[index - 1], MENU_MASK));
    }

    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed(new ActionEvent(document, e.getID(), e
                .getActionCommand()));
    }

}
