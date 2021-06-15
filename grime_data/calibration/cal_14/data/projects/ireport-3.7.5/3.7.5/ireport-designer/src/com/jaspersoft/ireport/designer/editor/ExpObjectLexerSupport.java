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
package com.jaspersoft.ireport.designer.editor;


import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import org.netbeans.api.languages.ASTToken;
import org.netbeans.api.languages.CharInput;
import org.netbeans.api.languages.Language;
import org.netbeans.api.languages.LanguageDefinitionNotFoundException;
import org.netbeans.api.languages.LanguagesManager;

/**
 *
 * @author gtoffoli
 */
public class ExpObjectLexerSupport {

    private static final String MIME_TYPE = "text/jrxml-expression";

    public static Object[] parseExpObject (CharInput input) {
        int start = input.getIndex ();

        try {

        if (input.next() == '$')
        {
            input.read();
            if (input.next() == 'F' ||
                input.next() == 'V' ||
                input.next() == 'P')
            {
                char objType = input.read();
                if (input.next() == '{')
                {
                    input.read();
                    String objName = "";
                    while (!input.eof() && input.next() != '}')
                    {
                        objName += input.read();
                    }
                    if (input.next() == '}')
                    {
                        input.read();
                        Language language;

                            try {
                                language = LanguagesManager.get().getLanguage(MIME_TYPE);
                            } catch (LanguageDefinitionNotFoundException ex) {
                                ex.printStackTrace();
                                return null;
                            }

                        // Check is it is a valid objct
                        if (isValidObject(objName,objType))
                        {
                            return new Object[] {
                                ASTToken.create (language, "object_" + objType, "object_" + objType, start, objName.length() + 4, null),
                                "DEFAULT"
                            };
                        }
                        else
                        {
                            return new Object[] {
                                ASTToken.create (language, "invalid_object", "invalid_object", start, objName.length() + 4, null),
                                "DEFAULT"
                            };
                        }
                    }
                }
            }
        }
        
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        input.setIndex(start);
        return new Object[]{null,null};

    }



    private static boolean isValidObject(String objName, char type) {
        ExpressionContext expressionContext = ExpressionContext.getGlobalContext();
        if (expressionContext != null)
        {
            return expressionContext.findObjectClassName(objName, type) != null;
        }
        return false;
    }

    

}
