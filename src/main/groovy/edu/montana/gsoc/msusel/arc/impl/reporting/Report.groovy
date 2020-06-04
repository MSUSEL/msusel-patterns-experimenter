package edu.montana.gsoc.msusel.arc.impl.reporting

import com.google.common.collect.BiMap
import com.google.common.collect.HashBasedTable
import com.google.common.collect.HashBiMap
import com.google.common.collect.Queues
import com.google.common.collect.Table
import edu.isu.isuese.datamodel.Measure
import edu.isu.isuese.datamodel.Pattern
import edu.isu.isuese.datamodel.PatternChain
import edu.isu.isuese.datamodel.PatternInstance
import edu.montana.gsoc.msusel.arc.impl.quamoco.QuamocoConstants
import edu.montana.gsoc.msusel.arc.impl.td.TechDebtConstants
import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.lang3.tuple.Pair
import org.javalite.activejdbc.Model
import org.mariadb.jdbc.internal.ColumnType

import java.lang.reflect.Method
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class Report {

    List<String> columns
    List<String> tables
    Table<String, String, List<String>> joinsOnTable = HashBasedTable.create() // left, right, clause
    BiMap<String, String> tableVars = HashBiMap.create()
    char lastVar = 'a'
    String firstTable
    String projects
    String measures
    String findings
    Table<String, String, String> data = HashBasedTable.create()
    ReportWriter writer

    String select = ""

    String reportFileName

    Report on(Class<? extends Model> type) {
        String tableName = getTableName(type)
        if (!firstTable)
            firstTable = tableName

        this
    }

    private String getTableName(Class<? extends Model> table) {
        Method m = table.getMethod("getTableName()")
        String tableName = m.invoke(null)
        tableName
    }

    Report column(Class<? extends Model> table, String name) {
        String tableName = getTableName(table)

        if (!tableVars[tableName])
            tableVars.put(tableName, "${lastVar++}")

        select += "${tableVars.get(tableName)}.${name}"

        this
    }

    Report to(String fileName) {
        reportFileName = fileName

        this
    }

    Report since(Date date) {


        this
    }

    Report within(String ... projectKeys) {
        if (projectKeys) {
            projects = projectKeys.join(",")
        }

        this
    }

    Report measures(String ... measures) {
        if (measures) {
            measures = measures.join(",")

            tableVars.put("measures", "${lastVar++}")

            select += " ${tableVars.get("measures")}.value"
            select += " ${tableVars.get("measures")}.key"

            tableVars.put("refs", "${lastVar++}")
            this.where()
        }

        this
    }

    Report findings(String ... findings) {
        if (findings) {
            findings = findings.join(",")
        }

        this
    }

    Report where(Pair<Class<? extends Model>, String> first, Pair<Class<? extends Model>, String> second) {
        String leftTable = getTableName(first.getKey())
        String leftVar = tableVars.get(leftTable)
        String leftCol = first.getValue()

        String rightTable = getTableName(second.getKey())
        String rightVar = tableVars.get(rightTable)
        String rightCol = second.getValue()

        if (joinsOnTable.get(leftTable, rightTable)) {
            joinsOnTable.get(leftTable, rightTable) << ("${leftVar}.${leftCol} = ${rightVar}.${rightCol}" as String)
        } else {
            joinsOnTable.put(leftTable, rightTable, [("${leftVar}.${leftCol} = ${rightVar}.${rightCol}" as String)])
        }

        this
    }

    Report writeWith(ReportWriter writer) {
        this.writer = writer

        this
    }

    private String getJoins() {
        Queue<String> que = Queues.newArrayDeque()
        String joins = ""

        que.offer(firstTable)
        while (!que.isEmpty()) {
            String left = que.poll()
            Map<String, List<String>> row = joinsOnTable.row(left)
            row.each { right, clauses ->
                joins += "JOIN $right ${tableVars.get(right)} on (${clauses.join(' AND ')})\n"
                que.offer(right)
            }
        }

        joins
    }

    private String getWhere() {
        String where = "WHERE"
        if (measures) {
            where += " ${tableVars['measures']}.key IN ($measures)"
        }
        if (projects) {
            if (where != "WHERE")
                where += "\nAND "
            where += "${tableVars[firstTable]}.project_id IN ($projects)"
        }

        if (where == "WHERE")
            where = ""

        where
    }

    void generate() {
        // 1. generate join
        String query = """\
SELECT $select
FROM ($firstTable ${tableVars[firstTable]} ${getJoins()})
${getWhere()};
"""
        // 2. execute join against db
        Map<String, Object> dbConnParams = [:]
        Sql.withInstance(dbConnParams) { Sql sql ->
            // 3. process results into  appropriate output format
            //    3.1 for each element of measures and findings there will be a separate column
            sql.eachRow(query) { GroovyResultSet rs ->
                columns.each {
                    data.put(columns[0], it, rs[rs.findColumn()].toString())
                }
                if (findings) {
                    def val = rs[rs.findColumn('findingKey')]
                    if (val)
                        data.put(columns[0], val.toString(), '1')
                }
                if (measures) {
                    def key = rs[rs.findColumn('measureKey')]
                    def val = rs[rs.findColumn('value')]
                    data.put(columns[0], key.toString(), val.toString())
                }
            }
        }
    }
}
