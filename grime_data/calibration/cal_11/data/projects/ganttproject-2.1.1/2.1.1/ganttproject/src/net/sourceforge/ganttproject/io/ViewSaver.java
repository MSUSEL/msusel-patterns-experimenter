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
 * Created on 06.03.2005
 */
package net.sourceforge.ganttproject.io;

import javax.xml.transform.sax.TransformerHandler;

import net.sourceforge.ganttproject.gui.TableHeaderUIFacade;
import net.sourceforge.ganttproject.gui.UIFacade;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author bard
 */
class ViewSaver extends SaverBase {
    public void save(UIFacade facade, TransformerHandler handler)
            throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        addAttribute("zooming-state", facade.getZoomManager().getZoomState()
                .getPersistentName(), attrs);
        addAttribute("id", "gantt-chart", attrs);
        emptyElement("view", attrs, handler);
        
    	addAttribute("id", "resource-table", attrs);
    	startElement("view", attrs, handler);
    	writeColumns(facade.getResourceTree().getVisibleFields(), handler);
    	
    	endElement("view", handler);
        
    }

    protected void writeColumns(TableHeaderUIFacade visibleFields, TransformerHandler handler) throws SAXException {
    	AttributesImpl attrs = new AttributesImpl();
    	int totalWidth = 0;
    	for (int i=0; i<visibleFields.getSize(); i++) {
    		if (visibleFields.getField(i).isVisible()) {
    			totalWidth += visibleFields.getField(i).getWidth();
    		}
    	}    	
    	for (int i=0; i<visibleFields.getSize(); i++) {
    		TableHeaderUIFacade.Column field = visibleFields.getField(i);
    		if (field.isVisible()) {
	    		addAttribute("id", field.getID(), attrs);
	    		addAttribute("name", field.getName(), attrs);
	    		addAttribute("width", field.getWidth()*100/totalWidth, attrs);
	    		addAttribute("order", field.getOrder(), attrs);
	    		emptyElement("field", attrs, handler);
    		}
    	}    	
    }
    
}
