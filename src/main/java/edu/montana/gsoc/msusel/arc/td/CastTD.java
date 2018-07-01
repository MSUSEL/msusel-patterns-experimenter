/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2018 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory
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
package edu.montana.gsoc.msusel.arc.td;

import edu.montana.gsoc.msusel.arc.td.param.TDParams;

/**
 * CastTD - Calculates Technical Debt using the Cast Method defined by Sappidi
 * et al.
 * 
 * @author Isaac Griffith
 */
public class CastTD extends TechnicalDebtCalcStrategy {

    /**
     * 
     */
    private static final String COST_LOW_SEVERITY         = "costLowSeverity";
    /**
     * 
     */
    private static final String COST_MED_SEVERITY         = "costMedSeverity";
    /**
     * 
     */
    private static final String COST_HIGH_SEVERITY        = "costHighSeverity";
    /**
     * 
     */
    private static final String PERCENT_LOW_SEVERITY      = "percentLowSeverity";
    /**
     * 
     */
    private static final String PERCENT_MED_SEVERITY      = "percentMedSeverity";
    /**
     * 
     */
    private static final String PERCENT_HIGH_SEVERITY     = "percentHighSeverity";
    /**
     * 
     */
    private static final String TIME_TO_FIX_LOW_SEVERITY  = "timeToFixLowSeverity";
    /**
     * 
     */
    private static final String TIME_TO_FIX_MED_SEVERITY  = "timeToFixMedSeverity";
    /**
     * 
     */
    private static final String TIME_TO_FIX_HIGH_SEVERITY = "timeToFixHighSeverity";
    /**
     * 
     */
    private static final String COUNT_LOW_SEVERITY        = "countLowSeverity";
    /**
     * 
     */
    private static final String COUNT_MED_SEVERITY        = "countMedSeverity";
    /**
     * 
     */
    private static final String COUNT_HIGH_SEVERITY       = "countHighSeverity";

    /**
     * {@inheritDoc}
     */
    public double calculate(TDParams params)
    {
        double countHighSeverity = params.getNumericParam(COUNT_HIGH_SEVERITY);
        double countMedSeverity = params.getNumericParam(COUNT_MED_SEVERITY);
        double countLowSeverity = params.getNumericParam(COUNT_LOW_SEVERITY);

        double timeToFixHighSeverity = params.getNumericParam(TIME_TO_FIX_HIGH_SEVERITY);
        double timeToFixMedSeverity = params.getNumericParam(TIME_TO_FIX_MED_SEVERITY);
        double timeToFixLowSeverity = params.getNumericParam(TIME_TO_FIX_LOW_SEVERITY);

        double percentHighSeverity = params.getNumericParam(PERCENT_HIGH_SEVERITY);
        double percentMedSeverity = params.getNumericParam(PERCENT_MED_SEVERITY);
        double percentLowSeverity = params.getNumericParam(PERCENT_LOW_SEVERITY);

        double costHighSeverity = params.getNumericParam(COST_HIGH_SEVERITY);
        double costMedSeverity = params.getNumericParam(COST_MED_SEVERITY);
        double costLowSeverity = params.getNumericParam(COST_LOW_SEVERITY);

        double principal = (countHighSeverity * percentHighSeverity * timeToFixHighSeverity * costHighSeverity)
                + (countMedSeverity * percentMedSeverity * timeToFixMedSeverity * costMedSeverity)
                + (countLowSeverity * percentLowSeverity * timeToFixLowSeverity * costLowSeverity);

        return principal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TDParams generateParams()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
