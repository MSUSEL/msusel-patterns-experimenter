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
package com.jaspersoft.ireport.designer.data.queryexecuters;

/**
 *
 * @author  Administrator
 */
public class QueryExecuterDef {
    
    private String language="";
    private String className="";
    private String fieldsProvider="";

    private boolean builtin = false;

    public QueryExecuterDef(String language, String className, String fieldsProvider) {

            this(language, className, fieldsProvider, false);
    }
    /** Creates a new instance of JRProperty */
    public QueryExecuterDef(String language, String className, String fieldsProvider, boolean isBuiltin) {
        this.language = language;
        this.className = className;
        this.fieldsProvider = fieldsProvider;
        this.builtin = isBuiltin;
    }
    
    /** Creates a new instance of JRProperty */
    public QueryExecuterDef(String language, String className) {
        this(language , className, "");
    }
    
    /** Creates a new instance of JRProperty */
    public QueryExecuterDef(){
    }
   
    
    @Override
    public String toString()
    {
        return (getLanguage() == null) ? "" : getLanguage();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldsProvider() {
        return fieldsProvider;
    }

    public void setFieldsProvider(String fieldsProvider) {
        this.fieldsProvider = fieldsProvider;
    }

    /**
     * @return the builtin
     */
    public boolean isBuiltin() {
        return builtin;
    }

    /**
     * @param builtin the builtin to set
     */
    public void setBuiltin(boolean builtin) {
        this.builtin = builtin;
    }

    public QueryExecuterDef cloneMe()
    {
        QueryExecuterDef copy = new QueryExecuterDef();
        copy.setLanguage(this.language);
        copy.setBuiltin(builtin);
        copy.setClassName(className);
        copy.setFieldsProvider(fieldsProvider);

        return copy;
    }
}
