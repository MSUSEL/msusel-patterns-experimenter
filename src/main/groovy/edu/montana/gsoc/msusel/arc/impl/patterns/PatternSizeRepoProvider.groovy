package edu.montana.gsoc.msusel.arc.impl.patterns

import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractRepoProvider

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class PatternSizeRepoProvider extends AbstractRepoProvider {

    PatternSizeRepoProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {
    }

    @Override
    void updateDatabase() {
        MetricRepository repo = MetricRepository.findFirst("repoKey = ? AND name = ?", ArcPatternConstants.PATTERN_SIZE_REPO_KEY, ArcPatternConstants.PATTERN_SIZE_REPO_NAME)
        if (!repo)
            MetricRepository.builder()
                    .key(ArcPatternConstants.PATTERN_SIZE_REPO_KEY)
                    .name(ArcPatternConstants.PATTERN_SIZE_REPO_NAME)
                    .create()
    }
}
