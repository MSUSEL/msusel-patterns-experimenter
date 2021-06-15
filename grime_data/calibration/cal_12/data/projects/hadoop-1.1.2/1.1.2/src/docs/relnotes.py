#!/usr/bin/python
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


# Run this command as:
#
# jira.sh -s https://issues.apache.org/jira -u $user -p $pw \
#   -a getIssueList --search \
#   "project in (HADOOP,HDFS,MAPREDUCE) and fixVersion = '$vers' and resolution = Fixed" \
#   | ./relnotes.py > $vers.html

import csv
import re
import subprocess
import sys

namePattern = re.compile(r' \([0-9]+\)')
htmlSpecialPattern = re.compile(r'[&<>\'"\n]')
quotes = {'<' : '&lt;', '>': '&gt;', '"': '&quot;', "'": '&apos;',
          '&': '&amp;', '\n': '<br>'}

def clean(str):
  return re.sub(namePattern, "", str)

def formatComponents(str):
  str = re.sub(namePattern, '', str).replace("'", "")
  if str != "":
    return "(" + str + ")"
  else:
    return ""
    
def quoteHtmlChar(m):
  return quotes[m.group(0)]

def quoteHtml(str):
  return re.sub(htmlSpecialPattern, quoteHtmlChar, str)

def readReleaseNote(id, default):
  cmd = ['jira.sh', '-s', 'https://issues.apache.org/jira', '-u', user, 
         '-p', password, '-a', 'getFieldValue', '--issue', id, '--field',
         'Release Note']
  proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=sys.stderr)
  lines = proc.stdout.readlines()
  # throw away first line
  if len(lines) < 2 or len(lines[1]) < 2:
    return default
  else:
    return "\n".join(lines[1:])[1:-2]

user = sys.argv[1]
password = sys.argv[2]
vers = sys.argv[3]

cmd = ['jira.sh', '-s', 'https://issues.apache.org/jira', '-u', user, '-p',
       password, '-a', 'getIssueList', '--search',
       "project in (HADOOP,HDFS,MAPREDUCE) and fixVersion = '" + vers + 
        "' and resolution = Fixed"]
proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=sys.stderr)

reader = csv.reader(proc.stdout, skipinitialspace=True)

# throw away number of issues
reader.next()

# read the columns
columns = reader.next()

key = columns.index('Key')
type = columns.index('Type')
priority = columns.index('Priority')
assignee = columns.index('Assignee')
reporter = columns.index('Reporter')
summary = columns.index('Summary')
description = columns.index('Description')
components = columns.index('Components')

print "<html><body><ul>"

for row in reader:
  row_descr = readReleaseNote(row[key], row[description])
  print \
    '<li> <a href="https://issues.apache.org/jira/browse/%s">%s</a>.\n' \
    '     %s %s reported by %s and fixed by %s %s<br>\n' \
    '     <b>%s</b><br>\n' \
    '     <blockquote>%s</blockquote></li>\n' \
    % (row[key], row[key], clean(row[priority]), clean(row[type]).lower(), 
       row[reporter], row[assignee], formatComponents(row[components]),
       quoteHtml(row[summary]), quoteHtml(row_descr))

print "</ul>\n</body></html>"
