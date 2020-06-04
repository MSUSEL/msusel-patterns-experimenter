package edu.montana.gsoc.msusel.arc.impl.reporting

import com.google.common.collect.Table

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
interface ReportWriter {

    void write(String reportFileName, List<String> columns, String measures, String findings, Table<String, String, String> data)
}
