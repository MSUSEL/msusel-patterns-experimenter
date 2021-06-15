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
package com.jaspersoft.ireport.designer;

import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author gtoffoli
 */
public interface FieldsProvider {
    
    /**
     * Returns true if the provider supports the {@link #getFields(IReportConnection,JRDataset,Map) getFields} 
     * operation. By returning true in this method the data source provider indicates
     * that it is able to introspect the data source and discover the available fields.
     * 
     * @return true if the getFields() operation is supported.
     */
    public boolean supportsGetFieldsOperation();
    
    /**
     * Returns the fields that are available from a query of a specific language
     * The provider can use the passed in report to extract some additional
     * configuration information such as report properties. 
     * The IReportConnection object can be used to execute the query.
     * 
     * @param con the IReportConnection active in iReport. 
     * @param the JRDataset that will be filled using the data source created by this provider.
     * 	The passed in report can be null. That means that no compiled report is available yet.
     * @param parameters map containing the interpreted default value of each parameter
     * @return a non null fields array. If there are no fields then an empty array must be returned.
     * 
     * @throws UnsupportedOperationException is the method is not supported
     * @throws JRException if an error occurs.
     */
    public JRField[] getFields(IReportConnection con,  JRDataset reportDataset, Map parameters ) throws JRException, UnsupportedOperationException;
    
    /**
     * Returns true if the getFields can be run in a backgroiund thread each time the user changes the query.
     * This approach can not be valid for fieldsProviders that require much time to return the list of fields.
     */
    public boolean supportsAutomaticQueryExecution();
    
    /**
     * Returns true if the FieldsProvider can run an own query designer
     */
    public boolean hasQueryDesigner();
    
    /**
     * Returns true if the FieldsProvider can run an own editor
     */
    public boolean hasEditorComponent();
    
    /**
     * This method is used to run a query designer for the specific language.
     *
     * @param con the IReportConnection active in iReport. 
     * @param query the query to modify
     * @param reportQueryDialog the parent reportQueryDialog. It can be used to get all (sub)dataset informations
     * with reportQueryDialog.getSubDataset();
     *
     */
    public String designQuery(IReportConnection con,  String query, ReportQueryDialog reportQueryDialog ) throws JRException, UnsupportedOperationException;
    
    
    /**
     * The component that will stay on the right of the query panel. To listen for query changes, the component must implement
     * the interface FieldsProviderEditor. The component will be visible only when a queryCahnged is succesfully executed.
     * The component can store the reference to the report query dialog in which it will appear.
     *
     * The editor can 
     */
    public FieldsProviderEditor getEditorComponent( ReportQueryDialog reportQueryDialog );
    
}
