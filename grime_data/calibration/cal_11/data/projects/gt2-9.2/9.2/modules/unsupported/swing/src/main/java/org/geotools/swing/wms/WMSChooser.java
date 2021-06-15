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
package org.geotools.swing.wms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 * 
 *
 * @source $URL$
 */
public class WMSChooser {
    public static URL showChooseWMS() {
        return showChooseWMS(deafultServers());
    }

    /**
     * Prompt for a URL to a Web Map Server, providing a list of recommended options.
     * 
     * @param args
     * @return
     * @throws MalformedURLException
     */
    public static URL showChooseWMS(List<String> servers) {
        if (servers == null) {
            servers = deafultServers();
        }
        JComboBox combo = new JComboBox(servers.toArray());
        combo.setEditable(true);

        Object message[] = new Object[] { "Choose a WMS Server", combo };
        do {
            int done = JOptionPane.showConfirmDialog(null, message, "Web Map Server",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (done == JOptionPane.CANCEL_OPTION) {
                return null;
            }
            Object input = combo.getSelectedItem();
            try {
                return new URL((String) input);
            } catch (Throwable t) {
                message = new Object[] { "Choose a WMS Service", combo, t.getMessage() };
            }
        } while (true);
    }

    private static List<String> deafultServers() {
        List<String> servers = new ArrayList<String>();
        servers
                .add("http://wms.jpl.nasa.gov/wms.cgi?Service=WMS&Version=1.1.1&Request=GetCapabilities");
        servers.add("http://localhost:8080/geoserver/wms?service=WMS&request=GetCapabilities");
        servers
                .add("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?Service=WMS&VERSION=1.1.0&REQUEST=GetCapabilities");
        servers
                .add("http://giswebservices.massgis.state.ma.us/geoserver/wms?service=WMS&request=GetCapabilities");
        servers
                .add("http://wms.cits.rncan.gc.ca/cgi-bin/cubeserv.cgi?VERSION=1.1.0&REQUEST=GetCapabilities");
        servers
                .add("http://atlas.gc.ca/cgi-bin/atlaswms_en?VERSION=1.1.1&Request=GetCapabilities&Service=WMS");

        return servers;
    }
}
