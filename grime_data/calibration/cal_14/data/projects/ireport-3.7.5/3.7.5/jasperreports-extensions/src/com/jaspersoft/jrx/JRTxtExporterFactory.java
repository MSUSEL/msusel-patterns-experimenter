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
package com.jaspersoft.jrx;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.export.ExporterFactory;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.JRExporter;
import com.jaspersoft.jrx.export.JRTxtExporter;
import com.jaspersoft.jrx.export.JRTxtExporterParameter;
import java.util.prefs.Preferences;

/**
 *
 * @author gtoffoli
 */
public class JRTxtExporterFactory implements ExporterFactory {

    public String getExportFormat() {
        return "irtxt";
    }

    public String getExportFormatDisplayName() {
        return I18n.getString("format.irtxt");
    }

    public String getExporterFileExtension() {
        return "txt";
    }

    public String getViewer() {
        return Misc.nvl( IReportManager.getInstance().getProperty("ExternalTXTViewer"), "");
    }

    public JRExporter createExporter() {
        JRTxtExporter exporter = new JRTxtExporter();

        // configuring the exporter...
        Preferences pref = IReportManager.getPreferences();


        int pageHeight = pref.getInt( "irtext.pageHeight", 0);
        if (pageHeight > 0)
        {
            exporter.setParameter( JRTxtExporterParameter.PAGE_ROWS, "" + pageHeight);
        }

        int pageWidth = pref.getInt( "irtext.pageWidth", 0);
        if (pageWidth > 0)
        {
            exporter.setParameter( JRTxtExporterParameter.PAGE_COLUMNS, "" + pageWidth);
        }

        boolean addFormFeed = pref.getBoolean("irtext.addFormFeed", true);
        exporter.setParameter( JRTxtExporterParameter.ADD_FORM_FEED, "" + addFormFeed);

        String bidi = pref.get("irtext.bidi", "");
        if (bidi.length() > 0)
        {
           exporter.setParameter( JRTxtExporterParameter.BIDI_PREFIX, bidi);

        }

        String displaywidthProviderFactory = pref.get("irtext.displaywidthProviderFactory", "");
        if (displaywidthProviderFactory.length() > 0)
        {
           exporter.setParameter( JRTxtExporterParameter.DISPLAY_WIDTH_PROVIDER_FACTORY, displaywidthProviderFactory);
        }

        return exporter;

    }

}
