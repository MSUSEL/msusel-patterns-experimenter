package edu.montana.gsoc.msusel.arc.impl.qmood;

import com.google.common.collect.ImmutableList;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.Provider;
import edu.montana.gsoc.msusel.arc.provider.RepoProvider;
import edu.montana.gsoc.msusel.arc.tool.MetricOnlyTool;

import java.util.List;

public class QMoodTool extends MetricOnlyTool {

    public QMoodTool(ArcContext context) {
        super(context);
    }

    @Override
    public RepoProvider getRepoProvider() {
        return new QMoodRepoProvider(context);
    }

    @Override
    public List<Provider> getOtherProviders() {
        return ImmutableList.of(new QMoodMetricProvider(context));
    }

    @Override
    public void init() {
        context.registerCommand(new QMoodCommand());
    }
}
