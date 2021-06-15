/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.apache.hadoop.mapreduce.lib.fieldsel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

/**
 * This class implements a mapper class that can be used to perform
 * field selections in a manner similar to unix cut. The input data is treated
 * as fields separated by a user specified separator (the default value is
 * "\t"). The user can specify a list of fields that form the map output keys,
 * and a list of fields that form the map output values. If the inputformat is
 * TextInputFormat, the mapper will ignore the key to the map function. and the
 * fields are from the value only. Otherwise, the fields are the union of those
 * from the key and those from the value.
 * 
 * The field separator is under attribute "mapreduce.fieldsel.data.field.separator"
 * 
 * The map output field list spec is under attribute 
 * "mapreduce.fieldsel.map.output.key.value.fields.spec". 
 * The value is expected to be like
 * "keyFieldsSpec:valueFieldsSpec" key/valueFieldsSpec are comma (,) separated
 * field spec: fieldSpec,fieldSpec,fieldSpec ... Each field spec can be a 
 * simple number (e.g. 5) specifying a specific field, or a range (like 2-5)
 * to specify a range of fields, or an open range (like 3-) specifying all 
 * the fields starting from field 3. The open range field spec applies value
 * fields only. They have no effect on the key fields.
 * 
 * Here is an example: "4,3,0,1:6,5,1-3,7-". It specifies to use fields
 * 4,3,0 and 1 for keys, and use fields 6,5,1,2,3,7 and above for values.
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class FieldSelectionMapper<K, V>
    extends Mapper<K, V, Text, Text> {

  private String mapOutputKeyValueSpec;

  private boolean ignoreInputKey;

  private String fieldSeparator = "\t";

  private List<Integer> mapOutputKeyFieldList = new ArrayList<Integer>();

  private List<Integer> mapOutputValueFieldList = new ArrayList<Integer>();

  private int allMapValueFieldsFrom = -1;

  public static final Log LOG = LogFactory.getLog("FieldSelectionMapReduce");

  public void setup(Context context) 
      throws IOException, InterruptedException {
    Configuration conf = context.getConfiguration();
    this.fieldSeparator = 
      conf.get(FieldSelectionHelper.DATA_FIELD_SEPERATOR, "\t");
    this.mapOutputKeyValueSpec = 
      conf.get(FieldSelectionHelper.MAP_OUTPUT_KEY_VALUE_SPEC, "0-:");
    try {
      this.ignoreInputKey = TextInputFormat.class.getCanonicalName().equals(
        context.getInputFormatClass().getCanonicalName());
    } catch (ClassNotFoundException e) {
      throw new IOException("Input format class not found", e);
    }
    allMapValueFieldsFrom = FieldSelectionHelper.parseOutputKeyValueSpec(
      mapOutputKeyValueSpec, mapOutputKeyFieldList, mapOutputValueFieldList);
    LOG.info(FieldSelectionHelper.specToString(fieldSeparator,
      mapOutputKeyValueSpec, allMapValueFieldsFrom, mapOutputKeyFieldList,
      mapOutputValueFieldList) + "\nignoreInputKey:" + ignoreInputKey);
  }

  /**
   * The identify function. Input key/value pair is written directly to output.
   */
  public void map(K key, V val, Context context) 
      throws IOException, InterruptedException {
    FieldSelectionHelper helper = new FieldSelectionHelper(
      FieldSelectionHelper.emptyText, FieldSelectionHelper.emptyText);
    helper.extractOutputKeyValue(key.toString(), val.toString(),
      fieldSeparator, mapOutputKeyFieldList, mapOutputValueFieldList,
      allMapValueFieldsFrom, ignoreInputKey, true);
    context.write(helper.getKey(), helper.getValue());
  }
}
