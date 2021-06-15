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
package com.jaspersoft.ireport.designer.data.fieldsproviders;

import com.jaspersoft.ireport.designer.FieldsProvider;
import com.jaspersoft.ireport.designer.FieldsProviderEditor;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.JDBCNBConnection;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRQueryChunk;
import net.sf.jasperreports.engine.design.JRDesignField;

/**
 *
 * @author gtoffoli
 */
public class SQLFieldsProvider implements FieldsProvider {
    
    public static boolean useVisualDesigner = true;
    
//    static {
//        java.util.Properties p = new java.util.Properties();
//        try {
//            //java.io.InputStream is = SQLFieldsProvider.class.getClass().getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties");
//            //java.io.InputStream is = SQLFieldsProvider.class.getResourceAsStream("/it/businesslogic/ireport/data/fieldsprovider.properties");
//            //p.load(  is );
//            //if (p.getProperty("sql").equals("0"))
//            //{
//            //    useVisualDesigner = false;
//            //}
//        } catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
    
    
    /** Creates a new instance of SQLFieldsProvider */
    public SQLFieldsProvider() {
        
        
    }
    
    /**
     * Returns true if the provider supports the {@link #getFields(IReportConnection,JRDataset,Map) getFields} 
     * operation. By returning true in this method the data source provider indicates
     * that it is able to introspect the data source and discover the available fields.
     * 
     * @return true if the getFields() operation is supported.
     */
    public boolean supportsGetFieldsOperation() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public JRField[] getFields(IReportConnection irConn, JRDataset reportDataset, Map parameters) throws JRException, UnsupportedOperationException {
        
        if (irConn == null || !irConn.isJDBCConnection()) {
             throw new JRException("The active connection is not of type JDBC. Activate a JDBC connection first.");
        }
        
        String query = "";
        String error_msg = "";
        Connection con = null;
        PreparedStatement ps = null;

        if (reportDataset.getQuery() == null || reportDataset.getQuery().getText() == null || reportDataset.getQuery().getText().length() == 0)
        {
           return new JRField[0];
        }

        try {


            StringBuffer queryBuf = new StringBuffer("");

            JRQueryChunk[] chunks = reportDataset.getQuery().getChunks();

            java.util.List queryParams = new ArrayList();

            for (int i=0; i<chunks.length; ++i)
            {
                switch (chunks[i].getType())
                {
                    case JRQueryChunk.TYPE_TEXT:
                        queryBuf.append(chunks[i].getText());
                        break;
                    case JRQueryChunk.TYPE_PARAMETER_CLAUSE:
                    {
                        // adding a parameter...
                        String paramName = chunks[i].getText();


                        if(!parameters.containsKey(paramName) ) {
                            throw new IllegalArgumentException("The parameter '" + paramName + "' is not defined.");
                        }

                        Object defValue = parameters.get(paramName);
                        if( defValue==null ) {
                            throw new IllegalArgumentException("Please set a " +
                            "default value for the parameter '" +
                            paramName + "'" );
                        }
                        queryBuf.append(defValue.toString());
                        break;
                    }
                    case JRQueryChunk.TYPE_PARAMETER:
                    {
                        // adding a parameter...
                        String paramName = chunks[i].getText();

                        if(!parameters.containsKey(paramName) ) {
                            throw new IllegalArgumentException("The parameter '" + paramName + "' is not defined.");
                        }
                        
                        Object defValue = parameters.get(paramName);
                        queryBuf.append("?");
                        queryParams.add(defValue);
                        break;
                    }
                    case JRQueryChunk.TYPE_CLAUSE_TOKENS:
                    {
                        String[] tokens = chunks[i].getTokens();

                        if (tokens.length == 3)
                        {
                            String clauseText = "";
                            clauseText =  tokens[1].trim() + " " + tokens[0].trim() + " (";

                            String paramName = tokens[2].trim();

                            if (parameters.containsKey(paramName))
                            {
                                Object defValue = parameters.get(paramName);
                                if (defValue == null)
                                {
                                    clauseText = "0 = 0";
                                }
                                else
                                {
                                    List items = new ArrayList();
                                    if (defValue.getClass().isArray())
                                    {
                                        items = Arrays.asList((Object[])defValue);
                                    }
                                    else if (defValue instanceof Collection)
                                    {
                                        items.addAll((Collection)defValue);
                                    }
                                    else
                                    {
                                        items.add(defValue);
                                    }

                                    Iterator iter = items.iterator();
                                    if (iter.hasNext())
                                    {
                                        Object itemVal = iter.next();
                                        queryBuf.append("?");
                                        queryParams.add(itemVal);

                                        while (iter.hasNext())
                                        {
                                            itemVal = iter.next();
                                            clauseText += ",?";
                                            queryParams.add(itemVal);
                                        }

                                        clauseText += ")";
                                    }
                                    else
                                    {
                                        clauseText = "0 = 0";
                                    }
                                }

                                queryBuf.append(clauseText);
                            }
                            else
                            {
                                throw new IllegalArgumentException("The parameter '" + paramName + "' is not defined.");
                            }
                        }
                        else
                        {
                           throw new IllegalArgumentException("Invalid $X{} clause");
                        }
                        break;
                    }
                }
            }

            query = queryBuf.toString();


            con = irConn.getConnection();
            System.out.println("Connection: " + con + "  Query: " + query);
            System.out.flush();
            ps = con.prepareStatement( query );

            for(int pc=0; pc<queryParams.size(); pc++ ) {

                if (queryParams.get(pc) == null)
                {
                    ps.setNull(pc+1, Types.BIT);
                    continue;
                }

                Class parameterType = queryParams.get(pc).getClass();
                if (java.lang.Boolean.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.BIT);
                    }
                    else
                    {
                        ps.setBoolean(pc+1, ((Boolean)queryParams.get(pc)).booleanValue());
                    }
                }
                else if (java.lang.Byte.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.TINYINT);
                    }
                    else
                    {
                        ps.setByte(pc+1, ((Byte)queryParams.get(pc)).byteValue());
                    }
                }
                else if (java.lang.Double.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.DOUBLE);
                    }
                    else
                    {
                        ps.setDouble(pc+1, ((Double)queryParams.get(pc)).doubleValue());
                    }
                }
                else if (java.lang.Float.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.FLOAT);
                    }
                    else
                    {
                        ps.setFloat(pc+1, ((Float)queryParams.get(pc)).floatValue());
                    }
                }
                else if (java.lang.Integer.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.INTEGER);
                    }
                    else
                    {
                        ps.setInt(pc+1, ((Integer)queryParams.get(pc)).intValue());
                    }
                }
                else if (java.lang.Long.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.BIGINT);
                    }
                    else
                    {
                        ps.setLong(pc+1, ((Long)queryParams.get(pc)).longValue());
                    }
                }
                else if (java.lang.Short.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.SMALLINT);
                    }
                    else
                    {
                        ps.setShort(pc+1, ((Short)queryParams.get(pc)).shortValue());
                    }
                }
                else if (java.math.BigDecimal.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.DECIMAL);
                    }
                    else
                    {
                        ps.setBigDecimal(pc+1, (BigDecimal)queryParams.get(pc));
                    }
                }
                else if (java.lang.String.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.VARCHAR);
                    }
                    else
                    {
                        ps.setString(pc+1, queryParams.get(pc).toString());
                    }
                }
                else if (java.sql.Timestamp.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.TIMESTAMP);
                    }
                    else
                    {
                        ps.setTimestamp( pc+1, (java.sql.Timestamp)queryParams.get(pc) );
                    }
                }
                else if (java.sql.Time.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.TIME);
                    }
                    else
                    {
                        ps.setTime( pc+1, (java.sql.Time)queryParams.get(pc) );
                    }
                }
                else if (java.util.Date.class.isAssignableFrom(parameterType))
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.DATE);
                    }
                    else
                    {
                        ps.setDate( pc+1, new java.sql.Date( ((java.util.Date)queryParams.get(pc)).getTime() ) );
                    }
                }
                else
                {
                    if (queryParams.get(pc) == null)
                    {
                        ps.setNull(pc+1, Types.JAVA_OBJECT);
                    }
                    else
                    {
                        ps.setObject(pc+1, queryParams.get(pc));
                    }
                }
            }



            // Some JDBC drivers don't supports this method...
            try { ps.setFetchSize(0); } catch(Exception e ) {}


            ResultSet rs = ps.executeQuery();

            //if (in < num) return;

            ResultSetMetaData rsmd = rs.getMetaData();

            //if (in < num) return;

            List columns = new ArrayList();
            for (int i=1; i <=rsmd.getColumnCount(); ++i) {
                JRDesignField field = new JRDesignField();
                field.setName( rsmd.getColumnLabel(i) );
                field.setValueClassName( Misc.getJdbcTypeClass(rsmd, i) );
                field.setDescription(null);
                columns.add( field );
            }

            JRField[] final_fields = new JRField[columns.size()];
            for (int i=0; i<final_fields.length; ++i)
            {
                final_fields[i] = (JRField)columns.get(i);
            }

            return final_fields;

        } catch( IllegalArgumentException ie ) {
            throw new JRException( ie.getMessage() );
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
            error_msg = "NoClassDefFoundError!!\nCheck your classpath!";
            throw new JRException( error_msg );
        } catch (java.sql.SQLException ex) {
            error_msg = "SQL problems:\n"+ex.getMessage();
            throw new JRException( error_msg );
        } catch (Exception ex) {
            ex.printStackTrace();
            error_msg = "General problem:\n"+ex.getMessage()+
                "\n\nCheck username and password; is the DBMS active ?!";
            throw new JRException( error_msg );
        } catch (Throwable t)
        {
            t.printStackTrace();
          throw new JRException( t.getMessage() );
        } finally {
            if(ps!=null) try { ps.close(); } catch(Exception e ) {}
            if(con !=null && !(irConn instanceof JDBCNBConnection)) try { con.close(); } catch(Exception e ) {}
        }
    }

    public boolean supportsAutomaticQueryExecution() {
        return true;
    }

    public boolean hasQueryDesigner() {
        return useVisualDesigner;
    }

    public boolean hasEditorComponent() {
        return true;
    }

    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog) throws JRException, UnsupportedOperationException {
        // Start FREE QUERY BUILDER....
            QueryBuilderDialog qbd = new QueryBuilderDialog( (reportQueryDialog != null) ? reportQueryDialog : new JDialog(), true);

            if (con.isJDBCConnection())
            {
                qbd.setConnection(  con.getConnection() );
            }
        
        try {
            
            if (query != null && query.length() > 0)
            {
                qbd.setQuery(query);
            }
        } catch (Throwable ex)
        {
            if (reportQueryDialog != null)
            {
                reportQueryDialog.getJLabelStatusSQL().setText("I'm sorry, I'm unable to parse the query...");
                ex.printStackTrace();
            }
            ex.printStackTrace();
            return null;
        }
        qbd.setVisible(true);
        
        if (qbd.getDialogResult() == JOptionPane.OK_OPTION)
        {
            return qbd.getQuery();
        }
        return null;
    }

    public FieldsProviderEditor getEditorComponent(ReportQueryDialog reportQueryDialog) {

        SQLFieldsProviderEditor dpe = new SQLFieldsProviderEditor();
        dpe.setReportQueryDialog( reportQueryDialog );
        return dpe;
    }
    
}
