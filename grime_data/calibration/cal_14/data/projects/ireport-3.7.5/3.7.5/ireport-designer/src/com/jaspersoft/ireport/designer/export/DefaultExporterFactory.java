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
package com.jaspersoft.ireport.designer.export;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.util.prefs.Preferences;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.util.JRProperties;

/**
 * This is a generic exporter used internally by iReport for the default
 * export formats.
 * @author gtoffoli
 */
public class DefaultExporterFactory implements ExporterFactory {

    String format = "";
    public DefaultExporterFactory(String format)
    {
        this.format = format;
    }

    public String getExportFormat() {
        return format;
    }

    public String getExporterFileExtension() {
        if (format.equals("xhtml")) return "html";
        if (format.equals("xml")) return "jrpxml";
        if (format.equals("xls2")) return "xls";
        return format;
    }

    public JRExporter createExporter() {

       JRExporter exporter = null;

       if (format.equalsIgnoreCase("pdf"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRPdfExporter();
          configurePdfExporter(exporter);
       }
       else if (format.equalsIgnoreCase("csv"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRCsvExporter();
          configureCsvExporter(exporter);
       }
       else if (format.equalsIgnoreCase("html"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRHtmlExporter();
          configureHtmlExporter(exporter);
       }
       else if (format.equalsIgnoreCase("xhtml"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXhtmlExporter();
          configureXHtmlExporter(exporter);
       }
       else if (format.equalsIgnoreCase("xls"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXlsExporter();
          configureXlsExporter(exporter);
       }
       else if (format.equalsIgnoreCase("xls2"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JExcelApiExporter();
          configureXlsExporter(exporter);
       }
       else if (format.equalsIgnoreCase("xlsx"))
       {
          exporter = new  net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
          configureXlsExporter(exporter);
       }
       else if (format.equalsIgnoreCase("java2D"))
       {
          // no exporter will be returned.
       }
       else if (format.equalsIgnoreCase("txt"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRTextExporter();
          configureTextExporter(exporter);
       }
       else if (format.equalsIgnoreCase("rtf"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRRtfExporter();
       }
       else if (format.equalsIgnoreCase("odt"))
       {
          exporter = new  net.sf.jasperreports.engine.export.oasis.JROdtExporter();
       }
       else if (format.equalsIgnoreCase("ods"))
       {
          exporter = new  net.sf.jasperreports.engine.export.oasis.JROdsExporter();
       }
       else if (format.equalsIgnoreCase("docx"))
       {
          exporter = new  net.sf.jasperreports.engine.export.ooxml.JRDocxExporter();
       }
       else if (format.equalsIgnoreCase("xml"))
       {
          exporter = new  net.sf.jasperreports.engine.export.JRXmlExporter();
          configureXmlExporter(exporter);
       }
       else if (format.equalsIgnoreCase("pptx"))
       {
          exporter = new  net.sf.jasperreports.engine.export.ooxml.JRPptxExporter();
          //configureXmlExporter(exporter);
       }
       return exporter;
    }

    public String getExportFormatDisplayName() {
       return I18n.getString("export.format.name." + format);
    }

    public String getViewer() {

       if (format.equalsIgnoreCase("pdf"))
       {
          return IReportManager.getInstance().getProperty("ExternalPDFViewer");
       }
       else if (format.equalsIgnoreCase("csv"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalCSVViewer"), "");
       }
       else if (format.equalsIgnoreCase("html") || format.equalsIgnoreCase("xhtml") )
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalHTMLViewer"), "");
       }
       else if (format.equalsIgnoreCase("xls") ||
                format.equalsIgnoreCase("xls2") ||
                format.equalsIgnoreCase("xlsx"))
       {
            return Misc.nvl( IReportManager.getInstance().getProperty("ExternalXLSViewer"), "");
       }
       else if (format.equalsIgnoreCase("java2D"))
       {
          return null;
       }
       else if (format.equalsIgnoreCase("txt") || format.equalsIgnoreCase("xml"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalTXTViewer"), "");
       }
       else if (format.equalsIgnoreCase("rtf"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalRTFViewer"), "");
       }
       else if (format.equalsIgnoreCase("odt"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalODFViewer"), "");
       }
       else if (format.equalsIgnoreCase("ods"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalODSViewer"), "");
       }
       else if (format.equalsIgnoreCase("docx"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalDOCXViewer"), "");
       }
       else if (format.equalsIgnoreCase("pptx"))
       {
          return Misc.nvl( IReportManager.getInstance().getProperty("ExternalPPTXViewer"), "");
       }

       return null;
    }
    
    private void configureTextExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        float floatVal = pref.getFloat(JRTextExporterParameter.PROPERTY_CHARACTER_HEIGHT, 0);
        if (floatVal > 0) exporter.setParameter( JRTextExporterParameter.CHARACTER_HEIGHT, new Float(floatVal));

        floatVal = pref.getFloat(JRTextExporterParameter.PROPERTY_CHARACTER_WIDTH, 0);
        if (floatVal > 0) exporter.setParameter( JRTextExporterParameter.CHARACTER_WIDTH, new Float(floatVal));

        int val = pref.getInt(JRTextExporterParameter.PROPERTY_PAGE_HEIGHT, 0);
        if (val > 0) exporter.setParameter( JRTextExporterParameter.PAGE_HEIGHT, new Integer(val));

        val = pref.getInt(JRTextExporterParameter.PROPERTY_PAGE_WIDTH, 0);
        if (val > 0) exporter.setParameter( JRTextExporterParameter.PAGE_WIDTH, new Integer(val));

        String s = null;
        if (pref.getBoolean(JRProperties.PROPERTY_PREFIX + "export.txt.nothingBetweenPages", false))
        {
            exporter.setParameter( JRTextExporterParameter.BETWEEN_PAGES_TEXT, "");
        }
        else
        {
            s = pref.get(JRProperties.PROPERTY_PREFIX + "export.txt.betweenPagesText", "");
            if (s.length() > 0) exporter.setParameter( JRTextExporterParameter.BETWEEN_PAGES_TEXT, s);
        }
        
        s = pref.get(JRProperties.PROPERTY_PREFIX + "export.txt.lineSeparator", "");
        if (s.length() > 0) exporter.setParameter( JRTextExporterParameter.LINE_SEPARATOR, s);

    }

    private void configureXlsExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        exporter.setParameter( JExcelApiExporterParameter.CREATE_CUSTOM_PALETTE , new Boolean(pref.getBoolean(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE, JRProperties.getBooleanProperty(JExcelApiExporterParameter.PROPERTY_CREATE_CUSTOM_PALETTE))));

        String password = pref.get(JExcelApiExporterParameter.PROPERTY_PASSWORD, JRProperties.getProperty(JExcelApiExporterParameter.PROPERTY_PASSWORD));
        if (password != null && password.length() > 0)
        {
            exporter.setParameter( JExcelApiExporterParameter.PASSWORD ,password);
        }

        exporter.setParameter( JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_COLLAPSE_ROW_SPAN))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_DETECT_CELL_TYPE))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_FONT_SIZE_FIX_ENABLED , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_FONT_SIZE_FIX_ENABLED))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BORDER , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BORDER))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_IGNORE_CELL_BACKGROUND , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_CELL_BACKGROUND))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_IGNORE_GRAPHICS , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IGNORE_GRAPHICS))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_IMAGE_BORDER_FIX_ENABLED , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_IMAGE_BORDER_FIX_ENABLED))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_ONE_PAGE_PER_SHEET))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS))));
        exporter.setParameter( JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND , new Boolean(pref.getBoolean(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, JRProperties.getBooleanProperty(JRXlsAbstractExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND))));

        int maxRowsPerSheet = pref.getInt(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET, JRProperties.getIntegerProperty(JRXlsAbstractExporterParameter.PROPERTY_MAXIMUM_ROWS_PER_SHEET));
        if (maxRowsPerSheet > 0)
        {
            exporter.setParameter( JRXlsAbstractExporterParameter.MAXIMUM_ROWS_PER_SHEET, new Integer(maxRowsPerSheet));
        }
        if (pref.getBoolean(JRProperties.PROPERTY_PREFIX + "export.xls.useSheetNames", false))
        {
            String sheetNames = pref.get(JRProperties.PROPERTY_PREFIX + "export.xls.sheetNames", "");
            exporter.setParameter( JRXlsAbstractExporterParameter.SHEET_NAMES,  sheetNames.split("\n"));
        }
    }

    private void configurePdfExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        String pdfVersion = pref.get(JRPdfExporterParameter.PROPERTY_PDF_VERSION, null);
        if (pdfVersion != null && pdfVersion.length()==1) exporter.setParameter( JRPdfExporterParameter.PDF_VERSION  , new Character(pdfVersion.charAt(0)));

        boolean b = pref.getBoolean(JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS));
        exporter.setParameter( JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS , new Boolean(b));

        exporter.setParameter( JRPdfExporterParameter.IS_COMPRESSED , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_COMPRESSED, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_COMPRESSED))));
        exporter.setParameter( JRPdfExporterParameter.FORCE_LINEBREAK_POLICY , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_FORCE_LINEBREAK_POLICY, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_FORCE_LINEBREAK_POLICY))));
        exporter.setParameter( JRPdfExporterParameter.FORCE_SVG_SHAPES , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_FORCE_SVG_SHAPES))));
        exporter.setParameter( JRPdfExporterParameter.IS_TAGGED , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_TAGGED, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_TAGGED))));
        exporter.setParameter( JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_COMPRESSED, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_COMPRESSED))));
        exporter.setParameter( JRPdfExporterParameter.IS_ENCRYPTED , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_ENCRYPTED, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_ENCRYPTED))));
        exporter.setParameter( JRPdfExporterParameter.IS_128_BIT_KEY , new Boolean(pref.getBoolean(JRPdfExporterParameter.PROPERTY_128_BIT_KEY, JRProperties.getBooleanProperty(JRPdfExporterParameter.PROPERTY_128_BIT_KEY))));

        if (pref.get("export.pdf.METADATA_AUTHOR", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.METADATA_AUTHOR , pref.get("export.pdf.METADATA_AUTHOR", ""));
        }
        if (pref.get("export.pdf.METADATA_CREATOR", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.METADATA_CREATOR , pref.get("export.pdf.METADATA_CREATOR", ""));
        }
        if (pref.get("export.pdf.METADATA_KEYWORDS", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.METADATA_KEYWORDS , pref.get("export.pdf.METADATA_KEYWORDS", ""));
        }
        if (pref.get("export.pdf.METADATA_SUBJECT", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.METADATA_SUBJECT , pref.get("export.pdf.METADATA_SUBJECT", ""));
        }
        if (pref.get("export.pdf.METADATA_TITLE", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.METADATA_TITLE , pref.get("export.pdf.METADATA_TITLE", ""));
        }
        if (pref.get("export.pdf.OWNER_PASSWORD", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.OWNER_PASSWORD , pref.get("export.pdf.OWNER_PASSWORD", ""));
        }
        if (pref.get("export.pdf.USER_PASSWORD", "").length() > 0)
        {
            exporter.setParameter( JRPdfExporterParameter.USER_PASSWORD , pref.get("export.pdf.USER_PASSWORD", ""));
        }
        if (pref.get("export.pdf.TAG_LANGUAGE", JRProperties.getProperty(JRPdfExporterParameter.PROPERTY_TAG_LANGUAGE)) != null)
        {
            exporter.setParameter( JRPdfExporterParameter.TAG_LANGUAGE ,pref.get("export.pdf.TAG_LANGUAGE", JRProperties.getProperty(JRPdfExporterParameter.PROPERTY_TAG_LANGUAGE)));
        }
        if (pref.get("export.pdf.PDF_JAVASCRIPT", JRProperties.getProperty(JRPdfExporterParameter.PROPERTY_PDF_JAVASCRIPT)) != null)
        {
            exporter.setParameter( JRPdfExporterParameter.PDF_JAVASCRIPT ,pref.get("export.pdf.PDF_JAVASCRIPT", JRProperties.getProperty(JRPdfExporterParameter.PROPERTY_PDF_JAVASCRIPT)));
        }
        if (pref.getInt("export.pdf.PERMISSIONS",0) != 0)
        {
            exporter.setParameter( JRPdfExporterParameter.PERMISSIONS ,pref.getInt("export.pdf.PERMISSIONS",0));
        }
    }


    private void configureXHtmlExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, pref.getBoolean(JRProperties.PROPERTY_PREFIX + "export.html.saveImages", true));
        exporter.setParameter( JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND)));
        exporter.setParameter( JRHtmlExporterParameter.IS_WRAP_BREAK_WORD, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD)));

        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesDirectory","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.IMAGES_DIR_NAME , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesDirectory",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesUri","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.IMAGES_URI , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesUri",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlHeader","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.HTML_HEADER , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlHeader",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlBetweenPages","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.BETWEEN_PAGES_HTML , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlBetweenPages",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlFooter","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.HTML_FOOTER , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlFooter",""));
        }
        if (pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, JRProperties.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)).length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.SIZE_UNIT , pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, JRProperties.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)));
        }

    }


    private void configureHtmlExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        exporter.setParameter( JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_FRAMES_AS_NESTED_TABLES)));
        exporter.setParameter( JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS)));
        exporter.setParameter( JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, pref.getBoolean(JRProperties.PROPERTY_PREFIX + "export.html.saveImages", true));
        exporter.setParameter( JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_USING_IMAGES_TO_ALIGN)));
        exporter.setParameter( JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WHITE_PAGE_BACKGROUND)));
        exporter.setParameter( JRHtmlExporterParameter.IS_WRAP_BREAK_WORD, pref.getBoolean(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD, JRProperties.getBooleanProperty(JRHtmlExporterParameter.PROPERTY_WRAP_BREAK_WORD)));

        //FIXME these properties do not actually exist!!!!!!!..... check all properties
        
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesDirectory","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.IMAGES_DIR_NAME , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesDirectory",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesUri","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.IMAGES_URI , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.imagesUri",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlHeader","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.HTML_HEADER , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlHeader",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlBetweenPages","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.BETWEEN_PAGES_HTML , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlBetweenPages",""));
        }
        if (pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlFooter","").length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.HTML_FOOTER , pref.get(JRProperties.PROPERTY_PREFIX + "export.html.htmlFooter",""));
        }
        if (pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, JRProperties.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)).length() > 0)
        {
            exporter.setParameter( JRHtmlExporterParameter.SIZE_UNIT , pref.get(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT, JRProperties.getProperty(JRHtmlExporterParameter.PROPERTY_SIZE_UNIT)));
        }

    }


    private void configureCsvExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        exporter.setParameter( JRCsvExporterParameter.FIELD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER, JRProperties.getProperty(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER)));
        exporter.setParameter( JRCsvExporterParameter.RECORD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER, JRProperties.getProperty(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER)));
    }

    private void configureXmlExporter(JRExporter exporter) {

        Preferences pref = IReportManager.getPreferences();

        //exporter.setParameter( JRCsvExporterParameter.FIELD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER, JRProperties.getProperty(JRCsvExporterParameter.PROPERTY_FIELD_DELIMITER)));
        //exporter.setParameter( JRCsvExporterParameter.RECORD_DELIMITER, pref.get(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER, JRProperties.getProperty(JRCsvExporterParameter.PROPERTY_RECORD_DELIMITER)));
    }


}
