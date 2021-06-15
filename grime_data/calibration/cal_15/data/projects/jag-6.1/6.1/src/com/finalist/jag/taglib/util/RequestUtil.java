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
package com.finalist.jag.taglib.util;

import com.finalist.jag.*;
import com.finalist.jag.skelet.*;

import java.util.*;


/**
 * Class RequestUtil
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class RequestUtil {

    /**
     * Method lookupString
     *
     *
     * @param pageContext
     * @param name
     * @param property
     *
     * @return
     *
     */
    public static String lookupString(PageContext pageContext, String name,
                                      String property)
    {
        Object object = pageContext.getAttribute(name);

        if (object == null) {
            object = pageContext.getSessionContext().getAttribute(name);
        }

        String returnValue = null;

        if ((object != null) && (object instanceof ModuleData))
        {
            ModuleData moduleData = (ModuleData) object;
            if (moduleData.isValueCollection())
            {
                Collection col      = (Collection) moduleData.getValue();
                Iterator   iterator = col.iterator();

                while (iterator.hasNext())
                {
                    moduleData = (ModuleData) iterator.next();
                    if (moduleData != null && moduleData.getName().equals(property)
                            && moduleData.isValueString())
                    {
                        returnValue = (String) moduleData.getValue();
                        break;
                    }
                }
            }
        }
        else if ((object != null) && (object instanceof String))
        {
            returnValue = (String) object;
        }
        else
        {
        	String colProperty = "";
        	String colName = "";
        	StringTokenizer tokens = new StringTokenizer(name, ".");
        	while(tokens.hasMoreTokens())
        	{
        		String token = tokens.nextToken();
        		if(!tokens.hasMoreTokens())
        		{
        			colProperty = token;
        		}
        		else
        		{
        			if(colName.length() > 0) colName += ".";
        			colName += token;
        		}
        	}
			Collection collection = lookupCollection(pageContext,colName,colProperty);
			if(collection != null)
			{
				Iterator iterator = collection.iterator();
				if(iterator.hasNext())
				{
					object = iterator.next();
					String hashcode = ""+object.hashCode();
					pageContext.setAttribute(hashcode, object);
					returnValue = lookupString(pageContext, hashcode, property);
					pageContext.removeAttribute(hashcode);
				}
			}
        }
        return returnValue;
    }

    /**
     * Method lookupCollection
     *
     *
     * @param pageContext
     * @param name
     * @param property
     *
     * @return
     *
     */
    public static Collection lookupCollection(PageContext pageContext,
                                              String name, String property)
    {
        SessionContext session = pageContext.getSessionContext();
        Object         object  = pageContext.getAttribute(name);

        if (object == null) {
            object = session.getAttribute(name);
        }

        Collection returnValue = null;

        if ((object != null) && (object instanceof Collection))
        {
            returnValue = (Collection) object;
        }
        else
        {
            Collection dataModules = null;
            ArrayList  tmpList     = new ArrayList();

            if ((object != null) && (object instanceof ModuleData))
            {
                ModuleData dataModule = (ModuleData) object;

                if (dataModule.isValueCollection()) {
                    dataModules = (Collection) dataModule.getValue();
                }
            }
            else
            {
                SkeletDataObj   skelet = session.getSkelet();
                StringTokenizer tokens = new StringTokenizer(name, ".");

                dataModules = (Collection) skelet.getValue();

                if (name.indexOf(skelet.getName()) == 0) {
                    tokens.nextToken();
                }

                while (tokens.hasMoreTokens())
                {
                    String   label    = tokens.nextToken();
                    Iterator iterator = dataModules.iterator();

                    while (iterator.hasNext())
                    {
                        ModuleData moduleData = (ModuleData) iterator.next();

                        if (moduleData.isValueCollection()
                                && moduleData.getName().equals(label))
                        {
                            if (tokens.hasMoreTokens())
                            {
                                dataModules =(Collection) moduleData.getValue();
                                break;
                            }
                        }
                    }
                    dataModules = null;
                    break;
                }
            }

            if (dataModules != null)
            {
                Iterator iterator = dataModules.iterator();
                while ((property != null) && iterator.hasNext())
                {
                    ModuleData moduleData = (ModuleData) iterator.next();

                    if (moduleData != null && moduleData.isValueCollection()
                            && moduleData.getName().equals(property)) {
                        tmpList.add(moduleData);
                    }
                }
                returnValue = tmpList;
            }
        }
        return returnValue;
    }
}
