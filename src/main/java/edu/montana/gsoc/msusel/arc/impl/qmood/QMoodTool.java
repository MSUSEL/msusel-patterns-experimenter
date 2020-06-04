package edu.montana.gsoc.msusel.arc.impl.qmood;

import com.google.common.collect.ImmutableList;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.Provider;
import edu.montana.gsoc.msusel.arc.provider.MetricProvider;
import edu.montana.gsoc.msusel.arc.provider.RepoProvider;
import edu.montana.gsoc.msusel.arc.tool.MetricOnlyTool;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class QMoodTool extends MetricOnlyTool {

    QMoodMetricProvider metricProvider;
    QMoodRepoProvider repoProvider;

    public QMoodTool(ArcContext context) {
        super(context);
        repoProvider = new QMoodRepoProvider(context);
        metricProvider = new QMoodMetricProvider(context);
    }

    @Override
    public RepoProvider getRepoProvider() {
        return repoProvider;
    }

    @Override
    public List<Provider> getOtherProviders() {
        return ImmutableList.of(metricProvider);
    }

    @Override
    public void init() {
        context.registerCommand(new QMoodCommand(metricProvider));
    }
}
