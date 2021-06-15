#!/bin/sh
#
# The MIT License (MIT)
#
# MSUSEL Arc Framework
# Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
# Software Engineering Laboratory and Idaho State University, Informatics and
# Computer Science, Empirical Software Engineering Laboratory
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

scriptDir=`dirname $0`

if [ "x$scriptDir" == "x." ]; then
    scriptDir=`pwd`"/$scriptDir"
fi

projectDir=`echo $scriptDir | sed "s/\/build\/src\/sh//"`
hibernateDir="$projectDir/hibernate"
exportDir="$projectDir/hibernate/export"
startdbDir="$projectDir/package/startdb"
installDir="$projectDir/package/install"
srcDBDir="$installDir/src/db"

# make maven in the hibernate dir to make sure it is up to date
echo ""
echo "---------------------------"
echo "BUILD HIBERNATE FILES"
cd "$hibernateDir"
maven

generateDB() {
    db=$1
    torqueDB=$2
    dialect=$3

    # first create the target directory, if it doesn't already exist
    dbDir="$srcDBDir/$db"
    if test ! -e "$dbDir"; then
        echo ""
        echo "---------------------------"
        echo "MAKE DIR $dbDir"
        mkdir -pv "$dbDir"
    fi

    echo ""
    echo "---------------------------"
    echo "GENERATING SCHEMA $db  ($dialect)"


    # go to the hibernate directory and run maven
    echo CHANGE DIR to $exportDir
    cd $exportDir

    echo maven -D"hibernate.dialect=$dialect" hibernate:schema-export
    maven -D"hibernate.dialect=$dialect" hibernate:schema-export || exit -1

    schemaSource="$exportDir/target/schema/schema-current.sql"
    schemaTarget="$dbDir/schema-current.sql"
    if test ! -e "$schemaSource"; then
        echo "No schema source '$schemaSource'"
        exit -1;
    fi

    echo ""
    echo "---------------------------"
    echo "GENERATING DATA $db  ($torqueDB)"

    # go to the startdb directory and run maven
    echo CHANGE DIR to $startdbDir
    cd $startdbDir
    echo maven -D "torque.database=$torqueDB" -D "torque.database.adaptor=$torqueDB" torque:sql torque:datasql || exit -1
    maven -D "torque.database=$torqueDB" -D "torque.database.adaptor=$torqueDB" torque:sql torque:datasql || exit -1

    # copy the files over
    dataSource="$startdbDir/target/sql/ivatagroupware-data.sql"
    dataTarget="$dbDir/data-current.sql"
    if test ! -e "$dataSource"; then
        echo "No data source '$dataSource'"
        exit -1;
    fi
    if [ "$db" == hypersonic ]; then
        # don't know why this is necessary
        echo "Replacing quotes in hypersonic data"
        perl -p -i -e  "s/\\\'/\'\'/g" "$dataSource"
    fi
    mv -v "$dataSource" "$dataTarget"
    mv -v "$schemaSource" "$schemaTarget"
}



generateDB db2 db2 net.sf.hibernate.dialect.DB2Dialect
generateDB db2as400 db2400 net.sf.hibernate.dialect.DB2400Dialect
generateDB hypersonic hypersonic net.sf.hibernate.dialect.HSQLDialect
generateDB sqlserver mssql net.sf.hibernate.dialect.SQLServerDialect
generateDB mysql mysql net.sf.hibernate.dialect.MySQLDialect
generateDB oracle oracle net.sf.hibernate.dialect.OracleDialect
generateDB pgsql postgresql net.sf.hibernate.dialect.PostgreSQLDialect
generateDB sapdb sapdb net.sf.hibernate.dialect.SAPDBDialect
generateDB sybase sybase net.sf.hibernate.dialect.SybaseDialect

