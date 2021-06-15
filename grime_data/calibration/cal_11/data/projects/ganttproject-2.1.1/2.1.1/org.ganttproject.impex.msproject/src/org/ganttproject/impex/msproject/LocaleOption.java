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
 * Created on 07.12.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.ganttproject.impex.msproject;

import java.util.Locale;

import net.sourceforge.ganttproject.gui.options.model.DefaultEnumerationOption;
import net.sourceforge.ganttproject.language.GanttLanguage;

class LocaleOption extends DefaultEnumerationOption {
    private final String LOCALE_NAMES[] = new String[] {};
    
    private final Locale[] LOCALES = new Locale[] {Locale.FRANCE, Locale.US, new Locale("pt", "PT")};

	private Locale myLocale;

    LocaleOption() {
    	super("impex.msproject.mpx.language", new String[] {
    			Locale.FRANCE.getDisplayLanguage(GanttLanguage.getInstance().getLocale()), Locale.US.getDisplayLanguage(GanttLanguage.getInstance().getLocale()), new Locale("pt", "PT").getDisplayLanguage(GanttLanguage.getInstance().getLocale())    			
    	});
    }
    
    public void commit() {
        super.commit();
        setSelectedLocale(getValue());
    }

    Locale getSelectedLocale() {
    	return myLocale;
    }
    private void setSelectedLocale(String value) {
        for (int i=0; i<LOCALES.length; i++) {
            if (LOCALES[i].getDisplayLanguage(GanttLanguage.getInstance().getLocale()).equals(value)) {
                myLocale = LOCALES[i];
                break;
            }
        }
    }

	public void setSelectedLocale(Locale locale) {
		setValue(locale.getDisplayLanguage(GanttLanguage.getInstance().getLocale()));
	}
    
}
