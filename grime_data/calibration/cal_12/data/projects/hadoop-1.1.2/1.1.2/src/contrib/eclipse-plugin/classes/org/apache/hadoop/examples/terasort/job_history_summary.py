#!/usr/bin/env python
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

import re
import sys

pat = re.compile('(?P<name>[^=]+)="(?P<value>[^"]*)" *')
counterPat = re.compile('(?P<name>[^:]+):(?P<value>[^,]*),?')

def parse(tail):
  result = {}
  for n,v in re.findall(pat, tail):
    result[n] = v
  return result

mapStartTime = {}
mapEndTime = {}
reduceStartTime = {}
reduceShuffleTime = {}
reduceSortTime = {}
reduceEndTime = {}
reduceBytes = {}

for line in sys.stdin:
  words = line.split(" ",1)
  event = words[0]
  attrs = parse(words[1])
  if event == 'MapAttempt':
    if attrs.has_key("START_TIME"):
      mapStartTime[attrs["TASKID"]] = int(attrs["START_TIME"])/1000
    elif attrs.has_key("FINISH_TIME"):
      mapEndTime[attrs["TASKID"]] = int(attrs["FINISH_TIME"])/1000
  elif event == 'ReduceAttempt':
    if attrs.has_key("START_TIME"):
      reduceStartTime[attrs["TASKID"]] = int(attrs["START_TIME"]) / 1000
    elif attrs.has_key("FINISH_TIME"):
      reduceShuffleTime[attrs["TASKID"]] = int(attrs["SHUFFLE_FINISHED"])/1000
      reduceSortTime[attrs["TASKID"]] = int(attrs["SORT_FINISHED"])/1000
      reduceEndTime[attrs["TASKID"]] = int(attrs["FINISH_TIME"])/1000
  elif event == 'Task':
    if attrs["TASK_TYPE"] == "REDUCE" and attrs.has_key("COUNTERS"):
      for n,v in re.findall(counterPat, attrs["COUNTERS"]):
        if n == "File Systems.HDFS bytes written":
          reduceBytes[attrs["TASKID"]] = int(v)

runningMaps = {}
shufflingReduces = {}
sortingReduces = {}
runningReduces = {}
startTime = min(reduce(min, mapStartTime.values()),
                reduce(min, reduceStartTime.values()))
endTime = max(reduce(max, mapEndTime.values()),
              reduce(max, reduceEndTime.values()))

reduces = reduceBytes.keys()
reduces.sort()

print "Name reduce-output-bytes shuffle-finish reduce-finish"
for r in reduces:
  print r, reduceBytes[r], reduceShuffleTime[r] - startTime,
  print reduceEndTime[r] - startTime

print

for t in range(startTime, endTime):
  runningMaps[t] = 0
  shufflingReduces[t] = 0
  sortingReduces[t] = 0
  runningReduces[t] = 0

for map in mapStartTime.keys():
  for t in range(mapStartTime[map], mapEndTime[map]):
    runningMaps[t] += 1
for reduce in reduceStartTime.keys():
  for t in range(reduceStartTime[reduce], reduceShuffleTime[reduce]):
    shufflingReduces[t] += 1
  for t in range(reduceShuffleTime[reduce], reduceSortTime[reduce]):
    sortingReduces[t] += 1
  for t in range(reduceSortTime[reduce], reduceEndTime[reduce]):
    runningReduces[t] += 1

print "time maps shuffle merge reduce"
for t in range(startTime, endTime):
  print t - startTime, runningMaps[t], shufflingReduces[t], sortingReduces[t], 
  print runningReduces[t]
