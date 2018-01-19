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
package edu.montana.gsoc.msusel.patterns.td;

import com.sparqline.techdebt.param.TDParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * NugrohoTD - Calculates Technical Debt using the method from Nugroho et al,
 * "An Empirical Model of Technical Debt and Interest", MTD'11, 2011.
 * 
 * @author Isaac Griffith
 */
public class NugrohoTD extends TechnicalDebtCalcStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(NugrohoTD.class);
    private static final String LANGUAGE = null;
    private static final String PERCENT_YEARLY_MAINT_GEN_TD = null;
    private static final String SYSTEM_SIZE = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculate(TDParams param)
    {

        double refactoringAdj = 0.0;

        double systemSize = param.getNumericParam(SYSTEM_SIZE);
        double techFactor = calcTechFactor(param);
        double reworkFactor = 0.0;
        double maintenanceFactor = 0.0;
        double qualityLevel = 0;
        double percentYearlyMaintGenTD = param.getNumericParam(PERCENT_YEARLY_MAINT_GEN_TD);
        double projectionYears = 0.0;

        double rebuildValue = systemSize * Math.pow((1 + percentYearlyMaintGenTD), projectionYears) * techFactor;
        double repairEffort = reworkFactor * rebuildValue * refactoringAdj;
        double qualityFactor = Math.pow(2, ((qualityLevel - 3) / 2));
        double maintenanceEffort = (maintenanceFactor * rebuildValue) / qualityFactor;

        return 0.0;
    }

    /**
     * @param param
     * @return
     */
    private double calcTechFactor(TDParams param)
    {
        String language = param.getStringParam(LANGUAGE);
        Properties props = new Properties();
        Properties prop = new Properties();
        double factor = 0;
        try
        {
            props.load(this.getClass().getResourceAsStream("java-metrics.properties"));
            String temp = prop.getProperty(language);
            if (temp != null && temp.contains(","))
                factor = Double.parseDouble(temp.substring(temp.lastIndexOf(',') + 1));
        }
        catch (final IOException e)
        {
            LOG.warn("A problem occurred while loading the metric properties file.", e);
        }
        catch (final NumberFormatException e)
        {
            LOG.warn("There was a problem loading the tech factor from the properties file.", e);
        }

        return factor;
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
