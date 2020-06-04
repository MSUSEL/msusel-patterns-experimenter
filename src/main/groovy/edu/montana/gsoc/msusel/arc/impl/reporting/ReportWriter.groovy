package edu.montana.gsoc.msusel.arc.impl.reporting

import com.google.common.collect.Table

interface ReportWriter {

    void write(String reportFileName, List<String> columns, String measures, String findings, Table<String, String, String> data)
}
