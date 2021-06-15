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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.BasicInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.DateTimeInputControlUI;
import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl.InputControlUI;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import java.text.SimpleDateFormat;

/**
 *
 * @author gtoffoli
 */
public class BasicInputControl {
    
    /*
     * This map stores the "recent" list of values for each control name...
     * As key, the control URI is used...
     * Each key stores a List.
     *
     */
    public static java.util.Map valueHistories = new java.util.HashMap();

    public void addActionListener(ActionListener listener)
    {
        getInputControlUI().addActionListener(listener);
    }
    
    protected ResourceDescriptor inputControl = null;
    private ResourceDescriptor dataType = null;
    private InputControlUI inputControlUI;

    private Object defaultValue = null;

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        getInputControlUI().setValue(defaultValue);
    }
    
    /** Creates a new instance of BasicInputControl */
    public BasicInputControl() {
         setInputControlUI(new BasicInputControlUI());
    }
    
    public JComponent getUI()
    {
        return (JComponent)getInputControlUI();
    }
    
    public Object getValue() throws InputValidationException
    {
        return validate();
    }

    public ResourceDescriptor getInputControl() {
        return inputControl;
    }

    public void setInputControl(ResourceDescriptor inputControl, java.util.List values)
    {
        setInputControl(inputControl, (ResourceDescriptor)null);
    }
    
    public void setInputControl(ResourceDescriptor inputControl)
    {
        setInputControl(inputControl, (ResourceDescriptor)null);
    }
            
    public void setInputControl(ResourceDescriptor inputControl, ResourceDescriptor dataType) {
        this.inputControl = inputControl;
        this.dataType = dataType;

        if (dataType != null && (
                    dataType.getDataType() == dataType.DT_TYPE_DATE ||
                    dataType.getDataType() == ResourceDescriptor.DT_TYPE_DATE_TIME))
        {
            setInputControlUI(new DateTimeInputControlUI());
        }

        String label = inputControl.getLabel() + ((inputControl.isMandatory()) ? "*" : "");
        getInputControlUI().setLabel(label);
        getInputControlUI().setReadOnly( inputControl.isReadOnly() );
        
        java.util.List history = getHistory(inputControl.getUriString());
        java.util.List revisedHistory = new java.util.ArrayList();
        if (dataType != null && dataType.getDataType() == dataType.DT_TYPE_DATE)
        {
            /*
            SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("dateformat", "d/M/y"));
            for (int i=0; i<history.size(); ++i)
            {
                Object obj = history.get(i);
                if (obj instanceof java.util.Date)
                {
                    revisedHistory.add(  format.format(obj));
                }
                else
                    revisedHistory.add(  obj);
            }
            */
        }
        else if (dataType != null && dataType.getDataType() == dataType.DT_TYPE_DATE_TIME)
        {
            /*
            SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("timeformat", "d/M/y H:m:s"));
            for (int i=0; i<history.size(); ++i)
            {
                Object obj = history.get(i);
                if (obj instanceof java.util.Date)
                {
                    revisedHistory.add(  format.format(obj));
                }
                else
                    revisedHistory.add(  obj);
            }
            */
        }
        else
        {
            revisedHistory = history;
        }
        getInputControlUI().setHistory( revisedHistory );
        
        
        
    }
    
    public void addHistoryValue(String controlUri, Object value)
    {
        
        if (controlUri == null) return;
        java.util.List list = getHistory(controlUri);
        if (list.contains( value)) list.remove(value );
        list.add(0,value);
    }
    
    public java.util.List getHistory(String controlUri)
    {
        if (controlUri == null) return null;
        
        java.util.List list = null;
        if ( valueHistories.get(controlUri) == null)
        {
            list = new java.util.ArrayList();
            valueHistories.put(controlUri, list);
            return list;
        }
        
        return (java.util.List)valueHistories.get(controlUri);
    }

    public InputControlUI getInputControlUI() {
        return inputControlUI;
    }

    public void setInputControlUI(InputControlUI inputControlUI) {
        this.inputControlUI = inputControlUI;
    }
    
    public Object validate() throws InputValidationException
    {
        Object val = getInputControlUI().getValue();
        if (getInputControl().getControlType() == ResourceDescriptor.IC_TYPE_SINGLE_VALUE)
        {
            // Look for the datatype....
            if (getDataType() != null)
            {
                if (val == null) return null;
                
                String strVal = ""+val;
                
                
                
                switch(getDataType().getDataType())
		{
			case ResourceDescriptor.DT_TYPE_TEXT :
			{
				//if (
				//	getDataType().getMaxValue()getMaxLength() != null
				//	&& getDataType().getMaxLength().intValue() < (val+"").length()
				//	)
				//{
				//	wrapper.setErrorMessage(messages.getMessage("fillParameters.error.invalidType", null, Locale.getDefault()));
				//}
				//else 
                                if (getDataType().getPattern() != null
					&& getDataType().getPattern().trim().length() > 0
					&& !Pattern.matches(getDataType().getPattern(), (strVal)))
				{
					throw new InputValidationException(
                                                JasperServerManager.getFormattedString("basicInputControl.patternError","{0} does not match the pattern ({1})",new Object[]{getInputControl().getLabel(), getDataType().getPattern()}));

                                }
                                val = strVal;
				break;
			}
			case ResourceDescriptor.DT_TYPE_NUMBER :
			{
				//FIXME take care of input mask
				try
				{
                                    if (strVal.trim().length() == 0) return null;
				    val = new java.math.BigDecimal(strVal);
				}
				catch(NumberFormatException e)
				{
                                    throw new InputValidationException(
                                            JasperServerManager.getFormattedString("basicInputControl.invalidNumber","Invalid number set for {0}",new Object[]{getInputControl().getLabel()}));
				}
                                break;
			}
			case ResourceDescriptor.DT_TYPE_DATE_TIME :
                        {
                            if (!(val instanceof java.util.Date))
                            {
                                SimpleDateFormat format = new SimpleDateFormat(IReportManager.getInstance().getProperty("timeformat", "d/M/y H:m:s"));
                                try
                                { 
                                        if (strVal.trim().length() == 0) return null;
                                        val = format.parse(strVal);
                                }
                                catch (ParseException e)
                                {
                                        throw new InputValidationException(
                                                JasperServerManager.getFormattedString("basicInputControl.invalidDatetime","Invalid value set for {0} is not a valid datetime in the form {1}",new Object[]{getInputControl().getLabel(),IReportManager.getInstance().getProperty("timeformat", "d/M/y H:m:s")}));
                                }
                            }
                            break;
                        }
                            
                            
			case ResourceDescriptor.DT_TYPE_DATE :
			{
                            if (!(val instanceof java.util.Date))
                            {
				SimpleDateFormat format = new SimpleDateFormat( IReportManager.getInstance().getProperty("dateformat", "d/M/y") );

				try
				{
                                        if (strVal.trim().length() == 0) return null;
					val = format.parse(strVal);
				}
				catch (ParseException e)
				{
					throw new InputValidationException(
                                                JasperServerManager.getFormattedString("basicInputControl.invalidDate","Invalid value set for {0} is not a valid date in the form {1}",new Object[]{getInputControl().getLabel(),IReportManager.getInstance().getProperty("dateformat", "d/M/y")}));
                                                
				}

                            }
                            break;
			}
		}
            }
        }
        
        return val;
    }

    public ResourceDescriptor getDataType() {
        return dataType;
    }

    public void setDataType(ResourceDescriptor dataType) {
        this.dataType = dataType;
    }
}
