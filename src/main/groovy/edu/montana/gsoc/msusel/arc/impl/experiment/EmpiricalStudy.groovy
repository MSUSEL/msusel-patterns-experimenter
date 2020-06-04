package edu.montana.gsoc.msusel.arc.impl.experiment

import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.command.Workflow
import edu.montana.gsoc.msusel.arc.impl.reporting.Report

abstract class EmpiricalStudy {

    String name
    String description
    Workflow workflow
    Report report

    void execute(ArcContext context) {
        context.logger().atInfo().log("Running Empirical Study: $name")
        context.logger().atInfo().log("Initializing study workflow")
        initWorkflow()

        context.logger().atInfo().log("Initializing study report")
        initReport()

        context.logger().atInfo().log("Starting empirical study workflow")
        workflow.execute()
        context.logger().atInfo().log("Empirical study workflow complete")

        context.logger().atInfo().log("Generating report for Empirical Study: $name")
        report.generate()
        context.logger().atInfo().log("Report generated in ${report.getReportFileName()}")
    }

    abstract void initWorkflow()

    abstract void initReport()
}
