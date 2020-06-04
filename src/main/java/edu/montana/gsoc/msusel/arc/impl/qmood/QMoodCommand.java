package edu.montana.gsoc.msusel.arc.impl.qmood;

import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class QMoodCommand extends SecondaryAnalysisCommand {

    QMoodMetricProvider provider;

    public QMoodCommand(QMoodMetricProvider provider) {
        super(QMoodConstants.QMOOD_CMD_NAME);
        this.provider = provider;
    }

    @Override
    public void execute(ArcContext context) {
        context.logger().atInfo().log("Starting QMOOD Analysis");

        provider.getRegistrar().getPrimaryEvaluators().forEach(metricEvaluator ->
                metricEvaluator.measure(context.getProject()));

        provider.getRegistrar().getSecondaryEvaluators().forEach(metricEvaluator ->
                metricEvaluator.measure(context.getProject()));

        context.logger().atInfo().log("Finished QMOOD Analysis");
    }
}
