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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;

/**
 * Calculates a selector's specificity.
 * @see <a href="http://www.w3.org/TR/CSS2/cascade.html#specificity">W3C CSS2</a>
 * @version $Revision: 5563 $
 * @author Marc Guillemot
 */
class SelectorSpecificity implements Comparable<SelectorSpecificity> {
    private static final Log LOG = LogFactory.getLog(SelectorSpecificity.class);
    /**
     * The specificity for declarations made in the style attributes of an element.
     */
    public static final SelectorSpecificity FROM_STYLE_ATTRIBUTE = new SelectorSpecificity(1, 0, 0, 0);

    private int fieldA_;
    private int fieldB_;
    private int fieldC_;
    private int fieldD_;

    SelectorSpecificity(final Selector selector) {
        readSelectorSpecificity(selector);
    }

    private SelectorSpecificity(final int a, final int b, final int c, final int d) {
        fieldA_ = a;
        fieldB_ = b;
        fieldC_ = c;
        fieldD_ = d;
    }

    void readSelectorSpecificity(final Selector selector) {
        switch (selector.getSelectorType()) {
            case Selector.SAC_ANY_NODE_SELECTOR:
                return;
            case Selector.SAC_DESCENDANT_SELECTOR:
                final DescendantSelector ds = (DescendantSelector) selector;
                readSelectorSpecificity(ds.getAncestorSelector());
                readSelectorSpecificity(ds.getSimpleSelector());
                return;
            case Selector.SAC_CONDITIONAL_SELECTOR:
                final ConditionalSelector conditional = (ConditionalSelector) selector;
                final Condition condition = conditional.getCondition();
                readSelectorSpecificity(conditional.getSimpleSelector());
                readSelectorSpecificity(condition);
                return;
            case Selector.SAC_ELEMENT_NODE_SELECTOR:
                final ElementSelector es = (ElementSelector) selector;
                final String name = es.getLocalName();
                if (name != null) {
                    fieldD_++;
                }
                return;
            case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
                final SiblingSelector ss = (SiblingSelector) selector;
                readSelectorSpecificity(ss.getSelector());
                readSelectorSpecificity(ss.getSiblingSelector());
                return;
            default:
                LOG.warn("Unhandled CSS selector type for specificity computation: '"
                        + selector.getSelectorType() + "'.");
                return;
        }
    }

    private void readSelectorSpecificity(final Condition condition) {
        switch (condition.getConditionType()) {
            case Condition.SAC_ID_CONDITION:
                fieldB_++;
                return;
            case Condition.SAC_CLASS_CONDITION:
                fieldC_++;
                return;
            case Condition.SAC_AND_CONDITION:
                final CombinatorCondition cc1 = (CombinatorCondition) condition;
                readSelectorSpecificity(cc1.getFirstCondition());
                readSelectorSpecificity(cc1.getSecondCondition());
                return;
            case Condition.SAC_ATTRIBUTE_CONDITION:
                final AttributeCondition ac1 = (AttributeCondition) condition;
                if ("id".equalsIgnoreCase(ac1.getLocalName())) {
                    fieldB_++;
                }
                else {
                    fieldC_++;
                }
                return;
            case Condition.SAC_PSEUDO_CLASS_CONDITION:
                fieldD_++;
                return;
            default:
                LOG.warn("Unhandled CSS condition type for specifity computation: '"
                        + condition.getConditionType() + "'.");
                return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return fieldA_ + "," + fieldB_ + "," + fieldC_ + "," + fieldD_;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(final SelectorSpecificity other) {
        if (fieldA_ != other.fieldA_) {
            return fieldA_ - other.fieldA_;
        }
        else if (fieldB_ != other.fieldB_) {
            return fieldB_ - other.fieldB_;
        }
        else if (fieldC_ != other.fieldC_) {
            return fieldC_ - other.fieldC_;
        }
        else if (fieldD_ != other.fieldD_) {
            return fieldD_ - other.fieldD_;
        }
        return 0;
    }
}
