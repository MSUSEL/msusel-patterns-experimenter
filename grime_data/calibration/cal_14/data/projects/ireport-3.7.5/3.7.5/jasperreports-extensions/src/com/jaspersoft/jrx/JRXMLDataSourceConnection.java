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
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import com.jaspersoft.ireport.designer.utils.*;
import com.jaspersoft.ireport.locale.I18n;
import javax.swing.*;

import com.jaspersoft.jrx.query.JRXPathQueryExecuterFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import org.w3c.dom.Document;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import java.util.Locale;
import java.util.TimeZone;
/**
 *
 * @author  Administrator
 */
public class JRXMLDataSourceConnection extends IReportConnection {
    
    private String name;
    
    private String filename;
    
    private String selectExpression;
    
    private boolean useConnection = false;
    
    private Locale locale = null;
    private String datePattern = null;
    private String numberPattern = null;
    private TimeZone timeZone = null;
    
    /** Creates a new instance of JDBCConnection */   
    public JRXMLDataSourceConnection() {
    }
    
    /**  This method return an instanced connection to the database.
     *  If isJDBCConnection() return false => getConnection() return null
     *
     */
    @Override
    public java.sql.Connection getConnection() {       
            return null;
    }
    @Override
    public boolean isJDBCConnection() {
        return false;
    }
    @Override
    public boolean isJRDataSource() {
        return !isUseConnection();
    }
    
    /*
     *  This method return all properties used by this connection
     */
    @Override
    public java.util.HashMap getProperties()
    {    
        java.util.HashMap map = new java.util.HashMap();
        map.put("Filename", Misc.nvl(this.getFilename() ,"") );  
        map.put("SelectExpression", Misc.nvl(this.getSelectExpression() ,"") ); 
        map.put("UseConnection",  Misc.nvl(""+this.isUseConnection() ,"false") ); 
        
        if (getLocale() != null )
        {
            map.put("Locale_language", Misc.nvl(getLocale().getLanguage(),"") );
            map.put("Locale_country", Misc.nvl(getLocale().getCountry(),"") );
            map.put("Locale_variant", Misc.nvl(getLocale().getVariant(),"") );
        }
        
        map.put("DatePattern",  Misc.nvl(this.getDatePattern(),"") ); 
        map.put("NumberPattern",   Misc.nvl(this.getNumberPattern(),"") ); 
        
        if (getTimeZone() != null)
        {
            map.put("timeZone",  Misc.nvl(this.getTimeZone().getID(),"") ); 
        }
        
        return map;
    }

    @Override
    public void loadProperties(java.util.HashMap map)
    {
        this.setFilename( (String)map.get("Filename"));
        this.setSelectExpression( (String)map.get("SelectExpression"));
        this.setUseConnection( Boolean.valueOf( Misc.nvl(map.get("UseConnection"),"false") ).booleanValue() );
        
        String language = (String)map.get("Locale_language");
        String country = (String)map.get("Locale_country");
        String variant = (String)map.get("Locale_variant");
        
        if (language != null && language.trim().length() > 0)
        {
            if (country != null && country.trim().length() > 0)
            {
                if (variant != null && variant.trim().length() > 0)
                {
                    setLocale( new Locale(language, country, variant));
                }
                else
                {
                    setLocale( new Locale(language, country));
                }
            }
            else
            {
                setLocale( new Locale(language));
            }
        }
        
        String datePatternValue = (String)map.get("DatePattern");
        if (datePatternValue != null && datePatternValue.trim().length() > 0)
        {
            this.setDatePattern(datePatternValue);
        }
        
        String numberPatternValue = (String)map.get("NumberPattern");
        if (numberPatternValue != null && numberPatternValue.trim().length() > 0)
        {
            this.setNumberPattern(numberPatternValue);
        }
        
        String timezoneId = (String)map.get("timeZone");
        if (timezoneId != null && timezoneId.trim().length() > 0)
        {
            this.setTimeZone( TimeZone.getTimeZone(timezoneId) );
        }
        
    }
    
    
    /**
     * Getter for property filename.
     * @return Value of property filename.
     */
    public java.lang.String getFilename() {
        return filename;
    }    
   
    /**
     * Setter for property filename.
     * @param filename New value of property filename.
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }    
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    @Override
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    @Override
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     *  This method return an instanced JRDataDource to the database.
     *  If isJDBCConnection() return true => getJRDataSource() return false
     */
    public net.sf.jasperreports.engine.JRDataSource getJRDataSource() { 
        try  {
            
           net.sf.jasperreports.engine.data.JRXmlDataSource ds = new net.sf.jasperreports.engine.data.JRXmlDataSource(filename, getSelectExpression() ); 
           
           if (getLocale()!=null)
           {
               ds.setLocale( getLocale());
           }
           
           if (getTimeZone() != null)
           {
               ds.setTimeZone( getTimeZone());
           }
           
           if (getDatePattern() != null && getDatePattern().trim().length()>0)
           {
               ds.setDatePattern( getDatePattern());
           }
           
           if (getNumberPattern() != null && getNumberPattern().trim().length()>0)
           {
               ds.setNumberPattern( getNumberPattern());
           }
           
           return ds; 
        } catch (Exception ex){}
        return null;
    }

    public String getSelectExpression() {
        return selectExpression;
    }

    public void setSelectExpression(String selectExpression) {
        this.selectExpression = selectExpression;
    }

    public boolean isUseConnection() {
        return useConnection;
    }

    public void setUseConnection(boolean useConnection) {
        this.useConnection = useConnection;
    }

    
    /**
     * This method is call before the datasource is used and permit to add special parameters to the map
     *
     */
    public java.util.Map getSpecialParameters(java.util.Map map) throws net.sf.jasperreports.engine.JRException
    {
        if (isUseConnection())
        {
            
            System.out.println("Running against: " + this.getFilename());
            System.out.flush();
            if (this.getFilename().toLowerCase().startsWith("https://") ||
                this.getFilename().toLowerCase().startsWith("http://") ||
                this.getFilename().toLowerCase().startsWith("file:"))
            {
            	
                map.put(JRXPathQueryExecuterFactory.XML_URL, this.getFilename());
            }
            else
            {
            
                Document document = JRXmlUtils.parse(new File( this.getFilename()));
                map.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
            }
            
            
            if (getLocale()!=null)
           {
               map.put(JRXPathQueryExecuterFactory.XML_LOCALE, getLocale());
           }
           
           if (getTimeZone() != null)
           {
               map.put(JRXPathQueryExecuterFactory.XML_TIME_ZONE, getTimeZone());
           }
           
           if (getDatePattern() != null && getDatePattern().trim().length()>0)
           {
               map.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, getDatePattern());
           }
           
           if (getNumberPattern() != null && getNumberPattern().trim().length()>0)
           {
               map.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, getNumberPattern());
           }
            
        }
        return map;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
    
     public String getDescription(){ return "Remote XML file datasource"; }
     
     public IReportConnectionEditor getIReportConnectionEditor()
    {
        return new JRXMLDataSourceConnectionEditor();
    }
     
    public void test() throws Exception
    {
            URL url = null;
            InputStream is = null;
            try {


                url = new URL(getFilename());

                if (getFilename().startsWith("file://"))
                {
                    is = url.openStream();
                }
                JOptionPane.showMessageDialog(Misc.getMainFrame(),I18n.getString("messages.connectionDialog.connectionTestSuccessful"),"",JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            catch (Exception ex)
            {
                //JOptionPane.showMessageDialog(Misc.getMainFrame(),ex.getMessage(),I18n.getString("message.title.error","Error"),JOptionPane.ERROR_MESSAGE);
		//ex.printStackTrace();

                JOptionPane.showMessageDialog(Misc.getMainWindow(),ex.getMessage(),
                        "Error",JOptionPane.ERROR_MESSAGE);		

                throw ex;
            } finally {

                if (is != null)
                {
                    try { is.close(); } catch (Exception ex){}
                }
            }
    }
}

