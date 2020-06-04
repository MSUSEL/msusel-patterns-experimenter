package edu.montana.gsoc.msusel.arc.impl.reporting

import com.google.common.collect.Table
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter

class CSVReportWriter implements ReportWriter {

    @Override
    void write(String reportFileName, List<String> columns, String measures, String findings, Table<String, String, String> data) {
        String[] headers = columns
        headers += measures.split(", ")
        headers += findings.split(", ")
        columns

        FileWriter out = new FileWriter(reportFileName)
        try {
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))
            Map<String, Map<String, String>> rows = data.rowMap()
            rows.each { String key, Map<String, String> cols ->
                String[] row = []
                headers.each {
                    row << cols[it]
                }
                printer.printRecord(row)
            }
        } catch (Exception e) {

        }
    }
}
