package edu.montana.gsoc.msusel.arc.impl.quamoco

import edu.isu.isuese.datamodel.MetricRepository
import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.provider.AbstractRepoProvider

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class QuamocoRepoProvider extends AbstractRepoProvider {

    QuamocoRepoProvider(ArcContext context) {
        super(context)
    }

    @Override
    def loadData() {
    }

    @Override
    def updateDatabase() {
        MetricRepository.findOrCreateIt("repoKey", QuamocoConstants.QUAMOCO_REPO_KEY, "name", QuamocoConstants.QUAMOCO_REPO_NAME, "toolName", QuamocoConstants.QUAMOCO_TOOL_NAME)
    }
}
