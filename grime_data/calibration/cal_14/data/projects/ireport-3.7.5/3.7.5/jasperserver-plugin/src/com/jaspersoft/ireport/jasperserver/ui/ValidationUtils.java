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
package com.jaspersoft.ireport.jasperserver.ui;

import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gtoffoli
 */
public class ValidationUtils {

    	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_LABEL = 100;
	public static final int MAX_LENGTH_DESC = 250;
        private static final Pattern PATTERN_NAME = Pattern.compile("(\\p{L}|\\p{N}|(\\_)|(\\.)|(\\-)|[;@])+");
        
        
        public static void validateName(String name) throws Exception
        {
            if (name==null || name.length() == 0) 
                throw new Exception(JasperServerManager.getString("resource.name.isEmpty","The name can not be empty"));
            Matcher mat = PATTERN_NAME.matcher(name.trim());
            if (!mat.matches()) 
                    throw new Exception(JasperServerManager.getString("resource.name.invalidCharacters","The name contains invalid characters"));
            if (name.trim().length() > MAX_LENGTH_NAME) throw new Exception(JasperServerManager.getFormattedString("resource.name.tooLong","The name can not be longer than {0,integer} characters",new Object[]{new Integer(MAX_LENGTH_NAME)}));
        }
        
        public static void validateLabel(String name) throws Exception
        {
            if (name==null || name.length() == 0) throw new Exception(JasperServerManager.getString("resource.label.isEmpty","The label can not be empty"));
            if (name.trim().length() > MAX_LENGTH_LABEL) throw new Exception(JasperServerManager.getFormattedString("resource.label.tooLong","The label can not be longer than {0,integer} characters",new Object[]{new Integer(MAX_LENGTH_LABEL)}));
        }
        
        public static void validateDesc(String name) throws Exception
        {
            if (name != null && name.trim().length() > MAX_LENGTH_DESC) throw new Exception(JasperServerManager.getFormattedString("resource.desc.tooLong","The description can not be longer than characters",new Object[]{new Integer(MAX_LENGTH_DESC)}));
        }
        
}
