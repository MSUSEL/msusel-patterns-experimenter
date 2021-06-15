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
package org.hibernate.envers.tools.query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.tools.MutableBoolean;
import org.hibernate.envers.tools.MutableInteger;

/**
 * Parameters of a query, built using {@link QueryBuilder}.
 * @author Adam Warski (adam at warski dot org)
 */
public class Parameters {
    public final static String AND = "and";
    public final static String OR = "or";
    
    /**
     * Main alias of the entity.
     */
    private final String alias;
    /**
     * Connective between these parameters - "and" or "or".
     */
    private final String connective;
    /**
     * For use by the parameter generator. Must be the same in all "child" (and parent) parameters.
     */
    private final MutableInteger queryParamCounter;

    /**
     * A list of sub-parameters (parameters with a different connective).
     */
    private final List<Parameters> subParameters;
    /**
     * A list of negated parameters.
     */
    private final List<Parameters> negatedParameters;
    /**
     * A list of complete where-expressions.
     */
    private final List<String> expressions;
    /**
     * Values of parameters used in expressions.
     */
    private final Map<String, Object> localQueryParamValues;

    Parameters(String alias, String connective, MutableInteger queryParamCounter) {
        this.alias = alias;
        this.connective = connective;
        this.queryParamCounter = queryParamCounter;

        subParameters = new ArrayList<Parameters>();
        negatedParameters = new ArrayList<Parameters>();
        expressions = new ArrayList<String>();
        localQueryParamValues = new HashMap<String, Object>();
    }

    private String generateQueryParam() {
        return "_p" + queryParamCounter.getAndIncrease();
    }

    /**
     * Adds sub-parameters with a new connective. That is, the parameters will be grouped in parentheses in the
     * generated query, e.g.: ... and (exp1 or exp2) and ..., assuming the old connective is "and", and the
     * new connective is "or".
     * @param newConnective New connective of the parameters.
     * @return Sub-parameters with the given connective.
     */
    public Parameters addSubParameters(String newConnective) {
        if (connective.equals(newConnective)) {
            return this;
        } else {
            Parameters newParams = new Parameters(alias, newConnective, queryParamCounter);
            subParameters.add(newParams);
            return newParams;
        }
    }

    /**
     * Adds negated parameters, by default with the "and" connective. These paremeters will be grouped in parentheses
     * in the generated query and negated, e.g. ... not (exp1 and exp2) ...
     * @return Negated sub paremters.
     */
    public Parameters addNegatedParameters() {
        Parameters newParams = new Parameters(alias, AND, queryParamCounter);
        negatedParameters.add(newParams);
        return newParams;
    }

    public void addWhere(String left, String op, String right) {
        addWhere(left, true, op, right, true);
    }

    /**
     * Adds <code>IS NULL</code> restriction.
     * @param propertyName Property name.
     * @param addAlias Positive if an alias to property name shall be added.
     */
    public void addNullRestriction(String propertyName, boolean addAlias) {
        addWhere(propertyName, addAlias, "is", "null", false);
    }

    /**
     * Adds <code>IS NOT NULL</code> restriction.
     * @param propertyName Property name.
     * @param addAlias Positive if an alias to property name shall be added.
     */
    public void addNotNullRestriction(String propertyName, boolean addAlias) {
        addWhere(propertyName, addAlias, "is not", "null", false);
    }

    public void addWhere(String left, boolean addAliasLeft, String op, String right, boolean addAliasRight) {
        StringBuilder expression = new StringBuilder();

        if (addAliasLeft) { expression.append(alias).append("."); }
        expression.append(left);

        expression.append(" ").append(op).append(" ");

        if (addAliasRight) { expression.append(alias).append("."); }
        expression.append(right);

        expressions.add(expression.toString());
    }

    public void addWhereWithParam(String left, String op, Object paramValue) {
        addWhereWithParam(left, true, op, paramValue);
    }

    public void addWhereWithParam(String left, boolean addAlias, String op, Object paramValue) {
        String paramName = generateQueryParam();
        localQueryParamValues.put(paramName, paramValue);

        addWhereWithNamedParam(left, addAlias, op, paramName);
    }

    public void addWhereWithNamedParam(String left, String op, String paramName) {
        addWhereWithNamedParam(left, true, op, paramName);
    }

    public void addWhereWithNamedParam(String left, boolean addAlias, String op, String paramName) {
        StringBuilder expression = new StringBuilder();

        if (addAlias) { expression.append(alias).append("."); }
        expression.append(left);
        expression.append(" ").append(op).append(" ");
        expression.append(":").append(paramName);

        expressions.add(expression.toString());
    }

    public void addWhereWithParams(String left, String opStart, Object[] paramValues, String opEnd) {
        StringBuilder expression = new StringBuilder();

        expression.append(alias).append(".").append(left).append(" ").append(opStart);

        for (int i=0; i<paramValues.length; i++) {
            Object paramValue = paramValues[i];
            String paramName = generateQueryParam();
            localQueryParamValues.put(paramName, paramValue);
            expression.append(":").append(paramName);

            if (i != paramValues.length-1) {
                expression.append(", ");
            }
        }

        expression.append(opEnd);

        expressions.add(expression.toString());
    }

    public void addWhere(String left, String op, QueryBuilder right) {
        addWhere(left, true, op, right);
    }

    public void addWhere(String left, boolean addAlias, String op, QueryBuilder right) {
        StringBuilder expression = new StringBuilder();

        if (addAlias) {
            expression.append(alias).append(".");
        }

        expression.append(left);
        
        expression.append(" ").append(op).append(" ");

        expression.append("(");
        right.build(expression, localQueryParamValues);
        expression.append(")");        

        expressions.add(expression.toString());
    }

    private void append(StringBuilder sb, String toAppend, MutableBoolean isFirst) {
        if (!isFirst.isSet()) {
            sb.append(" ").append(connective).append(" ");
        }

        sb.append(toAppend);

        isFirst.unset();
    }

    boolean isEmpty() {
        return expressions.size() == 0 && subParameters.size() == 0 && negatedParameters.size() == 0;
    }

    void build(StringBuilder sb, Map<String, Object> queryParamValues) {
        MutableBoolean isFirst = new MutableBoolean(true);

        for (String expression : expressions) {
            append(sb, expression, isFirst);
        }

        for (Parameters sub : subParameters) {
            if (!subParameters.isEmpty()) {
                append(sb, "(", isFirst);
                sub.build(sb, queryParamValues);
                sb.append(")");
            }
        }

        for (Parameters negated : negatedParameters) {
            if (!negatedParameters.isEmpty()) {
                append(sb, "not (", isFirst);
                negated.build(sb, queryParamValues);
                sb.append(")");
            }
        }

        queryParamValues.putAll(localQueryParamValues);
    }
}

