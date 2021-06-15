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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols;

import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.ui.ReportUnitRunDialog;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.BasicInputControl;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

/**
 *
 * @version $Id: InputControlsUpdater.java 0 2009-11-04 18:34:15 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class InputControlsUpdater implements Runnable {

    JServer server = null;
    BasicInputControl ic = null;
    Map parameters = null;
    String reportUnitUri = null;
    ReportUnitRunDialog dialog = null;

    public InputControlsUpdater(JServer server, BasicInputControl ic, String reportUnitUri, Map parameters, ReportUnitRunDialog dialog)
    {
        this.server = server;
        this.ic = ic;
        this.parameters = parameters;
        this.reportUnitUri = reportUnitUri;
        this.dialog = dialog;
    }


    public void run() {

        SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    if (dialog != null)
                    {
                        dialog.setBusy(true);
                    }
                }
            });
            
        java.util.List args = new java.util.ArrayList();
        args.add(new Argument( Argument.IC_GET_QUERY_DATA, ""));
        args.add(new Argument( "RU_REF_URI", reportUnitUri)); // TODO: use the constant....

        ic.getInputControl().getParameters().clear();
        ic.getInputControl().setResourceProperty(ResourceDescriptor.PROP_QUERY_DATA, null);

        String cacheKey = ic.getInputControl().getName();

        for (Iterator i= parameters.keySet().iterator(); i.hasNext() ;)
        {

            String key = ""+i.next();
            Object value = parameters.get( key );


            cacheKey += key + "=" + value + "\n";

            if (value instanceof java.util.Collection)
            {
                Iterator cIter = ((Collection)value).iterator();
                while (cIter.hasNext())
                {
                    String item = ""+cIter.next();
                    ListItem l = new ListItem(key+"",item);
                    l.setIsListItem(true);
                    ic.getInputControl().getParameters().add( l );
                }
            }
            else
            {
                ic.getInputControl().getParameters().add( new ListItem(key+"",parameters.get( key )));
            }
        }

        ResourceDescriptor newic = null;
        if (dialog.getValuesCache().containsKey(cacheKey))
        {
            ic.getInputControl().setQueryData((List)dialog.getValuesCache().get(cacheKey));
            newic = ic.getInputControl();
        }
        else
        {
            try {
                newic = server.getWSClient().get(ic.getInputControl(), null, args);
                if (newic.getQueryData() != null)
                {
                    dialog.getValuesCache().put(cacheKey, newic.getQueryData());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                    newic = null;
                try {

                    SwingUtilities.invokeAndWait(new Runnable() {

                        public void run() {
                            if (dialog != null) {
                                dialog.setBusy(false);
                            }
                        }
                    });
                } catch (Exception ex1) {
                }
            }

        }

        if (newic != null)
        {
            try {
                final ResourceDescriptor newic2 = newic;
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        ic.setInputControl(newic2, newic2.getQueryData());
                        if (dialog != null) {
                            dialog.setBusy(false);
                        }
                    }
                });
            } catch (InterruptedException ex) {
            } catch (InvocationTargetException ex) {
            }
        }

    }

}
