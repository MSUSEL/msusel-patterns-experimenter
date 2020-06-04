package edu.montana.gsoc.msusel.arc.impl.qmood

import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractRepoProvider

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class QMoodRepoProvider extends AbstractRepoProvider {

    QMoodRepoProvider(ArcContext context) {
        super(context)
    }

    @Override
    void loadData() {

    }

    @Override
    void updateDatabase() {
        MetricRepository.findOrCreateIt("repoKey", QMoodConstants.QMOOD_REPO_KEY, "name", QMoodConstants.QMOOD_REPO_NAME, "toolName", QMoodConstants.QMOOD_TOOL_NAME)
    }
}
