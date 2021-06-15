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
package com.ivata.groupware.business.library.faq;

import com.ivata.groupware.business.library.faq.category.FAQCategoryDO;
import com.ivata.groupware.container.persistence.BaseDO;


/**
 * <p>Represents a single question/answer pair as stored in frequently asked
 * question library items.</p>
 *
 * @hibernate.class
 *      table="library_faq"
 */
public class FAQDO  extends BaseDO {

    /**
     * <p>The answer to the question.</p>
     */
    private String answer;

    /**
     * <p>The category which contains this question/answer.</p>
     */
    private FAQCategoryDO fAQCategory;
    /**
     * <p>The question.</p>
     */
    private String question;
    /**
     * <p>Get the answer to the question.</p>
     *
     * @return the answer to the question.
     * @hibernate.property
     */
    public final String getAnswer() {
       return answer;
    }

    /**
     * <p>Get the category which contains this question/answer.</p>
     *
     * @return the category which contains this question/answer.
     * @hibernate.many-to-one
     *      column="library_faq_category"
     */
    public final FAQCategoryDO getFAQCategory() {
        return fAQCategory;
    }
    /**
     * <p>Get the question.</p>
     *
     * @return the question being asked.
     * @hibernate.property
     */
    public final String getQuestion() {
        return question;
    }

    /**
     * <p>Set the answer to the question.</p>
     *
     * @param answer the answer to the question.
     */
    public final void setAnswer(final String answer) {
        this.answer = answer;
    }

    /**
     * <p>Set the category which contains this question/answer.</p>
     *
     * @param faqCategory the category which contains this question/answer.
     */
    public final void setFAQCategory(final FAQCategoryDO faqCategory) {
        this.fAQCategory = faqCategory;
    }

    /**
     * <p>Set the question.</p>
     *
     * @param question the question being asked.
     */
    public final void setQuestion(final String question) {
        this.question = question;
    }
}
