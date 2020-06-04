package edu.montana.gsoc.msusel.arc.impl.patterns;

import com.google.common.collect.ImmutableList;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.Provider;
import edu.montana.gsoc.msusel.arc.provider.RepoProvider;
import edu.montana.gsoc.msusel.arc.tool.MetricOnlyTool;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ArcPatternTool extends MetricOnlyTool {

    public ArcPatternTool(ArcContext context) {
        super(context);
    }

    @Override
    public RepoProvider getRepoProvider() {
        return new PatternSizeRepoProvider(context);
    }

    @Override
    public List<Provider> getOtherProviders() {
        return ImmutableList.of(new PatternSizeMetricProvider(context));
    }

    @Override
    public void init() {
        context.registerCommand(new PatternSizeCommand());
        context.registerCommand(new PatternChainingCommand());
        context.registerCommand(new PatternCoalescenceCommand());
    }
}
