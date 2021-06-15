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
package org.ganttproject.impex.msproject;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.sourceforge.ganttproject.GPLogger;
import net.sourceforge.ganttproject.gui.options.GeneralOptionPanel;
import net.sourceforge.ganttproject.gui.options.TopPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;

class LocaleChooserPanel extends JPanel {

    private LocalePanel localePanel = null;

    private static GanttLanguage lang = GanttLanguage.getInstance();

    /** Constructor. */
    public LocaleChooserPanel() {
        super();

        JPanel result = new JPanel(new BorderLayout());
        result.setBorder(new EmptyBorder(0, 5, 0, 5));
        TopPanel topPanel = new TopPanel(lang.getText("mpxLanguageSettings"),
                lang.getText("mpxLanguageSettingsComment"));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        result.add(topPanel, BorderLayout.NORTH);
        localePanel = new LocalePanel();
        result.add(localePanel, BorderLayout.CENTER);

        add(result);
    }

    public Locale getSelectedLocale() {
        return localePanel.getSelectedLocale();
    }

    static class LocalePanel extends GeneralOptionPanel {

        private static final String LOCALE_FR = "Fran�ais";

        private static final String LOCALE_EN = "English";

        private static Map mapLocales = null;

        static {
            mapLocales = new HashMap();
            mapLocales.put(LOCALE_EN, Locale.US);
            mapLocales.put(LOCALE_FR, Locale.FRANCE);
        }

        private JComboBox combo = null;

        public LocalePanel() {
            super("", "");
            combo = new JComboBox(new Vector(mapLocales.keySet()));
            vb.add(combo);
            Locale currentLocale = GanttLanguage.getInstance().getLocale();
            try {
                combo.setSelectedItem(getString(currentLocale));
            } catch (Exception e) {
            	if (!GPLogger.log(e)) {
            		e.printStackTrace(System.err);
            	}
            }
        }

        public boolean applyChanges(boolean askForApply) {
            return false;
        }

        public void initialize() {
            // nothing
        }

        Locale getSelectedLocale() {
            return (Locale) mapLocales.get(combo.getSelectedItem());
        }

        private static Locale getLocale(String locale) {
            return (Locale) mapLocales.get(locale);
        }

        private static String getString(Locale locale) {
            String res = null;
            Iterator it = mapLocales.keySet().iterator();
            while (it.hasNext()) {
                res = (String) it.next();
                if (mapLocales.get(res).equals(locale))
                    break;
            }
            return res;
        }
    }

}
