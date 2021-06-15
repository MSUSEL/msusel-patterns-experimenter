#!/usr/bin/env bash
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

GRID_DIR=`dirname "$0"`
GRID_DIR=`cd "$GRID_DIR"; pwd`
source $GRID_DIR/gridmix-env-2

# Smaller data set is used by default.
COMPRESSED_DATA_BYTES=2147483648
UNCOMPRESSED_DATA_BYTES=536870912

# Number of partitions for output data
NUM_MAPS=100

# If the env var USE_REAL_DATASET is set, then use the params to generate the bigger (real) dataset.
if [ ! -z ${USE_REAL_DATASET} ] ; then
  echo "Using real dataset"
  NUM_MAPS=492
  # 2TB data compressing to approx 500GB
  COMPRESSED_DATA_BYTES=2147483648000
  # 500GB
  UNCOMPRESSED_DATA_BYTES=536870912000
fi

## Data sources
export GRID_MIX_DATA=/gridmix/data
# Variable length key, value compressed SequenceFile
export VARCOMPSEQ=${GRID_MIX_DATA}/WebSimulationBlockCompressed
# Fixed length key, value compressed SequenceFile
export FIXCOMPSEQ=${GRID_MIX_DATA}/MonsterQueryBlockCompressed
# Variable length key, value uncompressed Text File
export VARINFLTEXT=${GRID_MIX_DATA}/SortUncompressed
# Fixed length key, value compressed Text File
export FIXCOMPTEXT=${GRID_MIX_DATA}/EntropySimulationCompressed

${HADOOP_HOME}/bin/hadoop jar \
  ${EXAMPLE_JAR} randomtextwriter \
  -D test.randomtextwrite.total_bytes=${COMPRESSED_DATA_BYTES} \
  -D test.randomtextwrite.bytes_per_map=$((${COMPRESSED_DATA_BYTES} / ${NUM_MAPS})) \
  -D test.randomtextwrite.min_words_key=5 \
  -D test.randomtextwrite.max_words_key=10 \
  -D test.randomtextwrite.min_words_value=100 \
  -D test.randomtextwrite.max_words_value=10000 \
  -D mapred.output.compress=true \
  -D mapred.map.output.compression.type=BLOCK \
  -outFormat org.apache.hadoop.mapred.SequenceFileOutputFormat \
  ${VARCOMPSEQ} &


${HADOOP_HOME}/bin/hadoop jar \
  ${EXAMPLE_JAR} randomtextwriter \
  -D test.randomtextwrite.total_bytes=${COMPRESSED_DATA_BYTES} \
  -D test.randomtextwrite.bytes_per_map=$((${COMPRESSED_DATA_BYTES} / ${NUM_MAPS})) \
  -D test.randomtextwrite.min_words_key=5 \
  -D test.randomtextwrite.max_words_key=5 \
  -D test.randomtextwrite.min_words_value=100 \
  -D test.randomtextwrite.max_words_value=100 \
  -D mapred.output.compress=true \
  -D mapred.map.output.compression.type=BLOCK \
  -outFormat org.apache.hadoop.mapred.SequenceFileOutputFormat \
  ${FIXCOMPSEQ} &


${HADOOP_HOME}/bin/hadoop jar \
  ${EXAMPLE_JAR} randomtextwriter \
  -D test.randomtextwrite.total_bytes=${UNCOMPRESSED_DATA_BYTES} \
  -D test.randomtextwrite.bytes_per_map=$((${UNCOMPRESSED_DATA_BYTES} / ${NUM_MAPS})) \
  -D test.randomtextwrite.min_words_key=1 \
  -D test.randomtextwrite.max_words_key=10 \
  -D test.randomtextwrite.min_words_value=0 \
  -D test.randomtextwrite.max_words_value=200 \
  -D mapred.output.compress=false \
  -outFormat org.apache.hadoop.mapred.TextOutputFormat \
  ${VARINFLTEXT} &


