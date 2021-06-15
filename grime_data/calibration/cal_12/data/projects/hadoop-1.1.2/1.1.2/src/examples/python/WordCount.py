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

from org.apache.hadoop.fs import Path
from org.apache.hadoop.io import *
from org.apache.hadoop.mapred import *

import sys
import getopt

class WordCountMap(Mapper, MapReduceBase):
    one = IntWritable(1)
    def map(self, key, value, output, reporter):
        for w in value.toString().split():
            output.collect(Text(w), self.one)

class Summer(Reducer, MapReduceBase):
    def reduce(self, key, values, output, reporter):
        sum = 0
        while values.hasNext():
            sum += values.next().get()
        output.collect(key, IntWritable(sum))

def printUsage(code):
    print "wordcount [-m <maps>] [-r <reduces>] <input> <output>"
    sys.exit(code)

def main(args):
    conf = JobConf(WordCountMap);
    conf.setJobName("wordcount");
 
    conf.setOutputKeyClass(Text);
    conf.setOutputValueClass(IntWritable);
    
    conf.setMapperClass(WordCountMap);        
    conf.setCombinerClass(Summer);
    conf.setReducerClass(Summer);
    try:
        flags, other_args = getopt.getopt(args[1:], "m:r:")
    except getopt.GetoptError:
        printUsage(1)
    if len(other_args) != 2:
        printUsage(1)
    
    for f,v in flags:
        if f == "-m":
            conf.setNumMapTasks(int(v))
        elif f == "-r":
            conf.setNumReduceTasks(int(v))
    conf.setInputPath(Path(other_args[0]))
    conf.setOutputPath(Path(other_args[1]))
    JobClient.runJob(conf);

if __name__ == "__main__":
    main(sys.argv)
