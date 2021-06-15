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
package com.jaspersoft.ireport.designer.editor.functions;

import com.jaspersoft.ireport.designer.sheet.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @version $Id: FunctionsUtils.java 0 2010-08-10 11:45:23 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class FunctionsUtils {

    public static final String ALL = "ALL";
    public static List<Function> functions = null;


    public static List<Function> getFunctionsByCategory(String category)
    {

        // we may decide to cache this lists...
        List<Function> funsInCategory = new ArrayList<Function>();

        if (category == null) return funsInCategory;
        if (category.equals(ALL)) return getFunctions();

        List<Function> funs = getFunctions();

        for (Function f : funs)
        {
            if (f.getCategory().equals(category))
            {
                funsInCategory.add(f);
            }
        }

        return funsInCategory;
    }

    public static List<Function> getFunctions()
    {
        if (functions == null)
        {
            functions = new ArrayList<Function>();

            // Add all the functions here...
            functions.add(new DefaultFunction("replace", "Replace s1 with s2 in s3", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));

            functions.add(new DefaultFunction("concat","Return concatenated string", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3"), new StringParameter("s4"), new StringParameter("s5")}));
            functions.add(new DefaultFunction("lower","Return the argument in lowercase", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("Text") }));
            functions.add(new DefaultFunction("left","Return the leftmost number of characters as specified", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("Text"), new IntegerParameter("Len") }));
            functions.add(new DefaultFunction("length","Return the length of a string", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("lpad","Return the string argument, left-padded with the specified string", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("ltrim","Remove leading spaces", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("replace","Replace occurrences of a specified string", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("right","Return the specified rightmost number of characters", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("rpad","Append string the specified number of times", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("rtrim","Remove trailing spaces", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("space","Return a string of the specified number of spaces", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("strcmp","Compare two strings", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2"), new StringParameter("s3") }));
            functions.add(new DefaultFunction("substr","Return the substring as specified", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1") }));
            functions.add(new DefaultFunction("substring","Return the substring as specified", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1") }));
            functions.add(new DefaultFunction("trim","Remove leading and trailing spaces", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1") }));
            functions.add(new DefaultFunction("lower","Convert to uppercase", "TEXT", "java.lang.String",
                        new Parameter[] { new StringParameter("s1"), new StringParameter("s2") }));

        }
        return functions;
    }


    public static List<Tag> getFunctionCategories()
    {
        List<Tag> categories = new ArrayList<Tag>();

        categories.add(new Tag("TEXT","Text"));
        categories.add(new Tag("DATETIME","Date & Time"));
        categories.add(new Tag("MATH","Numeric / Mathematical"));
        categories.add(new Tag("LOGICAL","Logical"));
        categories.add(new Tag("INFORMATION","Information"));
        categories.add(new Tag("FINANCIAL","Financial"));
        categories.add(new Tag("DATASOURCES","Datasources"));

        categories.add(new Tag("ALL","<All>"));

        return categories;
    }

}
