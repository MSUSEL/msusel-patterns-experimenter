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
package edu.montana.gsoc.msusel.arc.impl.quality.sigmain

import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractMetricProvider
import edu.montana.gsoc.msusel.metrics.MetricEvaluator;
import edu.montana.gsoc.msusel.metrics.MetricsRegistrar;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class SigMainMetricProvider extends AbstractMetricProvider {

    MetricsRegistrar registrar

    SigMainMetricProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
        registerMetrics()
    }

    @Override
    void updateDatabase() {
        registrar.getPrimaryEvaluators().each {
            context.open()
            it.toMetric(repository)
            context.close()
        }

        registrar.getSecondaryEvaluators().each {
            context.open()
            it.toMetric(repository)
            context.close()
        }
    }

    @Override
    void initRepository() {
        context.open()
        repository = MetricRepository.findFirst("repoKey = ?", SigMainConstants.SIGMAIN_REPO_KEY)
        registrar = new MetricsRegistrar(repository)
        context.close()
    }

    void registerMetrics() {
        // basic metrics
        registrar.registerPrimary(new Volume())
        registrar.registerPrimary(new Duplication())
        registrar.registerPrimary(new UnitComplexity())
        registrar.registerPrimary(new UnitSize())
        registrar.registerPrimary(new UnitInterfacing())
        registrar.registerPrimary(new ModuleCoupling())
        registrar.registerPrimary(new ComponentBalance())
        registrar.registerPrimary(new ComponentIndependence())
        registrar.registerPrimary(new ComponentEntanglement())

        // quality characteristics
        registrar.registerSecondary(new AnalyzabilityEvaluator())
        registrar.registerSecondary(new ModifiabilityEvaluator())
        registrar.registerSecondary(new TestabilityEvaluator())
        registrar.registerSecondary(new ModularityEvaluator())
        registrar.registerSecondary(new ReusabilityEvaluator())
    }

    MetricEvaluator getMetricEvaluator(String handle) {
        registrar.getEvaluator(handle)
    }
}
