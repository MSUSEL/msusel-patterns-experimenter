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

from org.apache.hadoop.abacus import *

from java.util import *;

import sys

class AbacusMapper(ValueAggregatorMapper):
    def map(self, key, value, output, reporter):
        ValueAggregatorMapper.map(self, key, value, output, reporter);

class AbacusReducer(ValueAggregatorReducer):
    def reduce(self, key, values, output, reporter):
        ValueAggregatorReducer.reduce(self, key, values, output, reporter);

class AbacusCombiner(ValueAggregatorCombiner):
    def reduce(self, key, values, output, reporter):
        ValueAggregatorCombiner.reduce(self, key, values, output, reporter);

def printUsage(code):
    print "Abacus <input> <output> <numOfReducers> <inputformat> <specfile>"
    sys.exit(code)

def main(args):
    if len(args) < 6:
        printUsage(1);

    inDir = args[1];
    outDir = args[2];
    numOfReducers = int(args[3]);
    theInputFormat = args[4];
    specFile = args[5];
                                        
    print "numOfReducers: ", numOfReducers, "theInputFormat: ", theInputFormat, "specFile: ", specFile

    conf = JobConf(AbacusMapper);
    conf.setJobName("recordcount");
    conf.addDefaultResource(Path(specFile));
 
    if theInputFormat=="textinputformat":
        conf.setInputFormat(TextInputFormat);
    else:
        conf.setInputFormat(SequenceFileInputFormat);
    conf.setOutputFormat(TextOutputFormat);
    conf.setMapOutputKeyClass(Text);
    conf.setMapOutputValueClass(Text);
    conf.setOutputKeyClass(Text);
    conf.setOutputValueClass(Text);
    conf.setNumMapTasks(1);
    conf.setNumReduceTasks(numOfReducers);

    conf.setMapperClass(AbacusMapper);        
    conf.setCombinerClass(AbacusCombiner);
    conf.setReducerClass(AbacusReducer);
    conf.setInputPath(Path(args[1]))
    conf.setOutputPath(Path(args[2]))

    JobClient.runJob(conf);

if __name__ == "__main__":
    main(sys.argv)
