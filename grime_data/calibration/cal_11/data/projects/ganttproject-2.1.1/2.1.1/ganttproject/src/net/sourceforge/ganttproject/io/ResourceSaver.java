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
package net.sourceforge.ganttproject.io;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import net.sourceforge.ganttproject.CustomProperty;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ProjectResource;
import net.sourceforge.ganttproject.resource.ResourceColumn;

class ResourceSaver extends SaverBase {

    void save(IGanttProject project, TransformerHandler handler) throws SAXException {
        final AttributesImpl attrs = new AttributesImpl();
        startElement("resources", handler);
        saveCustomColumnDefinitions(project, handler);
        ProjectResource[] resources = project.getHumanResourceManager().getResourcesArray();
        for (int i = 0; i < resources.length; i++) {
            HumanResource p = (HumanResource) resources[i];
            addAttribute("id", String.valueOf(p.getId()), attrs);
            addAttribute("name", p.getName(), attrs);
            addAttribute("function", p.getRole().getPersistentID(), attrs);
            addAttribute("contacts", p.getMail(), attrs);
            addAttribute("phone", p.getPhone(), attrs);
            startElement("resource", attrs, handler);
            {
            	saveCustomProperties(project, p, handler);
            }
            endElement("resource", handler);
        }
        endElement("resources", handler);
    }

	private void saveCustomProperties(IGanttProject project, HumanResource resource, TransformerHandler handler) throws SAXException {
		//CustomPropertyManager customPropsManager = project.getHumanResourceManager().getCustomPropertyManager();
		AttributesImpl attrs = new AttributesImpl();
		List properties = resource.getCustomProperties();
		for (int i=0; i<properties.size(); i++) {
			CustomProperty nextProperty = (CustomProperty) properties.get(i);
			CustomPropertyDefinition nextDefinition = nextProperty.getDefinition();
            if (nextProperty.getValue()!=null && !nextProperty.getValue().equals(nextDefinition.getDefaultValue())) {
    			addAttribute("definition-id", nextDefinition.getID(), attrs);
    			addAttribute("value", nextProperty.getValueAsString(), attrs);
    			emptyElement("custom-property", attrs, handler);
            }
		}
	}

	private void saveCustomColumnDefinitions(IGanttProject project, TransformerHandler handler) throws SAXException {
		CustomPropertyManager customPropsManager = project.getHumanResourceManager().getCustomPropertyManager();
		List/*<CustomPropertyDefinition>*/ definitions = customPropsManager.getDefinitions();
//		HumanResourceManager hrManager = (HumanResourceManager) project.getHumanResourceManager();
//		Map customFields = hrManager.getCustomFields();
//		if (customFields.size()==0) {
//			return;
//		}
        final AttributesImpl attrs = new AttributesImpl();
        //startElement("custom-properties-definition", handler);
		for (int i=0; i<definitions.size(); i++) {
			//ResourceColumn nextField = (ResourceColumn) fields.next();
			CustomPropertyDefinition nextDefinition = (CustomPropertyDefinition) definitions.get(i);
			addAttribute("id", nextDefinition.getID(), attrs);
			addAttribute("name", nextDefinition.getName(), attrs);
			addAttribute("type", nextDefinition.getTypeAsString(), attrs);
			addAttribute("default-value", nextDefinition.getDefaultValueAsString(), attrs);
			emptyElement("custom-property-definition", attrs, handler);
		}
		//endElement("custom-properties-definition", handler);
	}

	
}
