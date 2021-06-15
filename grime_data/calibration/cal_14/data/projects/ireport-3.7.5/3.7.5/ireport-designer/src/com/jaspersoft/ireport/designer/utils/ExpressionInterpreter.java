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
package com.jaspersoft.ireport.designer.utils;

import bsh.EvalError;
import bsh.Interpreter;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.data.ReportQueryDialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.openide.util.Exceptions;

/**
 *
 * @author gtoffoli
 */
public class ExpressionInterpreter {

    JRDesignDataset dataset = null;
    Interpreter interpreter = null;
    JasperDesign jasperDesign = null;


    private boolean convertNullParams = false;

    public ExpressionInterpreter(JRDesignDataset dataset, ClassLoader classLoader)
    {
        this(dataset, classLoader, null);
    }
    public ExpressionInterpreter(JRDesignDataset dataset, ClassLoader classLoader, JasperDesign jasperDesign)
    {
        try {
            this.dataset = dataset;
            this.jasperDesign = jasperDesign;
            if (jasperDesign == null && IReportManager.getInstance().getActiveReport() != null)
            {
                jasperDesign = IReportManager.getInstance().getActiveReport();
            }
            prepareExpressionEvaluator(classLoader);
        } catch (EvalError ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    /**
     * Try to interpret the java expression passed as argument.
     * If dataset is provided, the parameters are recursively interpreted.
     * If a classloader is specified, it is used to load classes referred
     * in the expression.
     */
    public Object interpretExpression(String expression)
    {
        try {
                if (interpreter == null) return null;
                
                java.util.List queryParams = new ArrayList();
                Iterator iterParams = null;
                if (dataset != null)
                    iterParams = dataset.getParametersList().iterator();
                while(iterParams != null && iterParams.hasNext() ) {
                    JRDesignParameter parameter = (JRDesignParameter)iterParams.next();

                    String p1 = "$P{" + parameter.getName() + "}";
                    //String p2 = "$P!{" + parameter.getName() + "}";

                    // evaluate the Default expression value
                    
                    // Integer expID = (Integer)parameterNameToExpressionID.get(parameter.getName());

                    int ip1 = expression.indexOf(p1);

                    if (ip1 < 0) continue;

                    Object defValue;
                    if(  parameter.getDefaultValueExpression() != null &&  !parameter.getDefaultValueExpression().equals("") ) {
                        String expText = "";
                        if (parameter.getDefaultValueExpression() != null)
                        {
                            expText = parameter.getDefaultValueExpression().getText();
                        }
                        defValue = recursiveInterpreter( interpreter, expText , dataset.getParametersList(),0,parameter.getName());
                        // interpreter.eval("bshCalculator.evaluate(" + expID.intValue() + ")");
                    } else {
                        // this param does not have a default value.
                        defValue = null;
                        if (isConvertNullParams())
                        {
                            if (parameter.getValueClassName().equals("java.lang.String"))
                            {
                                defValue = "";
                            }
                        }
                    }


                    
                    while( ip1 != -1 ) {
                        // String replacement...
                        //if( defValue==null ) {
                        //    return null;
                        //}

                        String before = expression.substring(0, ip1);
                        String after = expression.substring(ip1+p1.length());
                        
                        String param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getLiteral(parameter.getName()); 

                        expression = before + param_name_literal + after;
                        // set the value...
                        interpreter.set(param_name_literal, defValue);

                        /*
                        if (parameter.getValueClassName().equals("java.lang.String"))
                        {
                            String stt = defValue.toString();
                            //stt = Misc.string_replace("''","'", stt);
                            expression = before + stt + after;
                        }
                        else expression = before + "" + defValue.toString() + "" + after;
                        */
                        ip1 = expression.indexOf(p1);
                    }

                    /*
                    int ip2 = expression.indexOf(p2);
                    while( ip2!=-1 ) {
                        // String replacement, Altering the SQL statement.
                        if( defValue==null ) {
                            throw new IllegalArgumentException("Please set a " +
                                "default value for the parameter '" 
                                + parameter.getName() + "'" );
                        }

                        String before = expression.substring(0, ip2);
                        String after = expression.substring(ip2+p2.length());
                        expression = before + "" + defValue.toString() + "" + after;
                        ip2 = expression.indexOf(p2);
                    }
                    */
                }
                
                //System.out.println("Evaluating exp: " + expression);

             return interpreter.eval(expression);
        
        } catch (EvalError error)
        {
            
        }
        
        return null;
    }
    
    
    private void prepareExpressionEvaluator(ClassLoader cl ) throws bsh.EvalError {
        
        interpreter = new Interpreter();
        // I need to add to the classpath the document directory...
        
        // Look for the JrxmlVisualView parent of the scene...
        if (cl != null)
        {
            interpreter.setClassLoader( cl );
        }
        
        // Staring patch from rp4
        
        interpreter.eval("String tmp;");
        List<String> paths = IReportManager.getInstance().getClasspath();
        for (String path : paths)
        {
            interpreter.set("tmp", path);
            interpreter.eval("addClassPath(tmp);");
        }        
       
        // Add report import directives to the bsh interpreter
        interpreter.eval("import net.sf.jasperreports.engine.*;");
        interpreter.eval("import net.sf.jasperreports.engine.fill.*;");
        interpreter.eval("import java.util.*;");
        interpreter.eval("import java.math.*;");
        interpreter.eval("import java.text.*;");
        interpreter.eval("import java.io.*;");
        interpreter.eval("import java.net.*;");
        interpreter.eval("import java.util.*;");
        interpreter.eval("import net.sf.jasperreports.engine.*;");
        interpreter.eval("import net.sf.jasperreports.engine.data.*;");

        if (jasperDesign != null)
        {
            String[] imports =  jasperDesign.getImports();
            for (int i=0; imports != null && i<imports.length; ++i)
            {
                interpreter.eval("import " + imports[i] + ";");
            }
        }
   }
    
    
    
    public static Object recursiveInterpreter(Interpreter interpreter, String expression, List parameters) throws EvalError
    {
        return recursiveInterpreter(interpreter, expression, parameters, 0);
    }
    
    public static  Object recursiveInterpreter(Interpreter interpreter, String expression, List parameters, int recursion_level) throws EvalError
    {
        return recursiveInterpreter(interpreter, expression, parameters, 0, null);
    }
    
    public static  Object recursiveInterpreter(Interpreter interpreter, String expression, List parameters, int recursion_level, String this_param_name) throws EvalError
    {
        ++recursion_level;
        
        if (expression == null || expression.length() == 0) return null;
        
        //System.out.println("Valuto ["+ recursion_level +"]: " + expression);
        if (recursion_level > 100) return null;
        if (expression != null && expression.trim().length() > 0)
        {
            // for each parameter, we have to calc the real value...
            while (expression.indexOf("$P{") >= 0)
            {
                int start_index = expression.indexOf("$P{")+3;
                String param_name = expression.substring(start_index, expression.indexOf("}", start_index) );
                String param_expression = "";
                for (int i=0; i<parameters.size(); ++i)
                {
                    JRDesignParameter p = (JRDesignParameter)parameters.get(i);
                    if (p.getName().equals( param_name))
                    {
                        param_expression = p.getDefaultValueExpression().getText();
                        break;
                    }
                }
                
                String param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getLiteral(param_name); 
                
                expression = Misc.string_replace( param_name_literal, "$P{"+param_name+"}", expression);
                //interpreter.set( param_name_literal, recursiveInterpreter(interpreter, param_expression, parameters, recursion_level));
            
                // If the parameter was never evaluated before, that can happen is some cases,
                // evaluate it now!
                if (interpreter.get(param_name_literal) == null)
                {
                    Object paramValue = recursiveInterpreter(interpreter, param_expression, parameters, recursion_level, this_param_name);
                    interpreter.set(param_name_literal, paramValue);
                }
            }
            
            String this_param_name_literal = "param_unknow";
            
            if (this_param_name!= null) 
            {
                this_param_name_literal = "param_" + net.sf.jasperreports.engine.util.JRStringUtil.getLiteral(this_param_name);
            } 
            //System.out.println("interpreto ["+ recursion_level +"]: " + expression);
            //System.out.flush();
            Object res = interpreter.eval(expression);
            interpreter.set(this_param_name_literal, res);
            //System.out.println("Result: " + res);
            //System.out.flush();
            return res;
        }
        return null;
    }

    /**
     * @return the convertNullParams
     */
    public boolean isConvertNullParams() {
        return convertNullParams;
    }

    /**
     * @param convertNullParams the convertNullParams to set
     */
    public void setConvertNullParams(boolean convertNullParams) {
        this.convertNullParams = convertNullParams;
    }
}
