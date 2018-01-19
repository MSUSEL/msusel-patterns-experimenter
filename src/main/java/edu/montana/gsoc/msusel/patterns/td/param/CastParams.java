/**
 * The MIT License (MIT)
 * 
 * Sonar Technical Asset Plugin
 * Copyright (c) 2015 Isaac Griffith, SiliconCode, LLC
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
package com.sparqline.techdebt.param;

/**
 * CastParams - 
 * 
 * @author Isaac Griffith
 *
 */
public interface CastParams {

    String COST_LOW_SEVERITY         = "costLowSeverity";
    String COST_MED_SEVERITY         = "costMedSeverity";
    String COST_HIGH_SEVERITY        = "costHighSeverity";
    String PERCENT_LOW_SEVERITY      = "percentLowSeverity";
    String PERCENT_MED_SEVERITY      = "percentMedSeverity";
    String PERCENT_HIGH_SEVERITY     = "percentHighSeverity";
    String TIME_TO_FIX_LOW_SEVERITY  = "timeToFixLowSeverity";
    String TIME_TO_FIX_MED_SEVERITY  = "timeToFixMedSeverity";
    String TIME_TO_FIX_HIGH_SEVERITY = "timeToFixHighSeverity";
    String COUNT_LOW_SEVERITY        = "countLowSeverity";
    String COUNT_MED_SEVERITY        = "countMedSeverity";
    String COUNT_HIGH_SEVERITY       = "countHighSeverity";
}
