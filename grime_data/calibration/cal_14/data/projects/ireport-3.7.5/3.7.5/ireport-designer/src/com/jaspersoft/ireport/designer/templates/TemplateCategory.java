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
package com.jaspersoft.ireport.designer.templates;

/**
 *
 * @author gtoffoli
 */
public class TemplateCategory implements Comparable<TemplateCategory> {
    
    public static final String CATEGORY_ALL_REPORTS = "All_reports";
    public static final String CATEGORY_OTHER_REPORTS = "Other_reports";

    private String category = CATEGORY_OTHER_REPORTS;
    private String subCategory = "";

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category == null ? CATEGORY_OTHER_REPORTS : category;
    }

    /**
     * @return the subCategory
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     * @param subCategory the subCategory to set
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory == null ? "" : subCategory;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof TemplateCategory)
        {
            TemplateCategory cat = (TemplateCategory)obj;
            if (cat.getCategory().equals(getCategory()) &&
                cat.getSubCategory().equals(getSubCategory()))
            {
                    return true;
            }
            return false;
        }

        return super.equals(obj);
    }

    public int compareTo(TemplateCategory o) {

        int c = getCategory().compareTo(getCategory());
        if (c == 0) return getSubCategory().compareTo(o.getSubCategory());
        return c;
    }



}
