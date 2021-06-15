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

package org.hsqldb.lib.tar;

import org.hsqldb.lib.ValidatingResourceBundle;
import org.hsqldb.lib.RefCapableRBInterface;

/* $Id: RB.java 3481 2010-02-26 18:05:06Z fredt $ */

/**
 * Resource Bundle for Tar classes
 * <P>
 * Purpose of this class is to wrap a RefCapablePropertyResourceBundle to
 *  reliably detect any possible use of a missing property key as soon as
 *  this class is clinitted.
 * The reason for this is to allow us developers to detect all such errors
 *  before end-users ever use this class.
 * </P> <P>
 * IMPORTANT:  To add a new ResourceBundle element, add two new lines, one
 * like <PRE>
 *    static public final int NEWKEYID = keyCounter++;
 * </PRE> and one line <PRE>
 *      new Integer(KEY2), "key2",
 * </PRE>
 * Both should be inserted right after all of the other lines of the same type.
 * NEWKEYID is obviously a new constant which you will use in calling code
 * like RB.NEWKEYID.
 * </P>
 */
public enum RB implements RefCapableRBInterface {
    DbBackup_syntax,
    DbBackup_syntaxerr,
    TarGenerator_syntax,
    pad_block_write,
    cleanup_rmfail,
    TarReader_syntax,
    unsupported_entry_present,
    bpr_write,
    stream_buffer_report,
    write_queue_report,
    file_missing,
    modified_property,
    file_disappeared,
    file_changed,
    file_appeared,
    pif_malformat,
    pif_malformat_size,
    zero_write,
    pif_toobig,
    read_denied,
    compression_unknown,
    insufficient_read,
    decompression_ranout,
    move_work_file,
    cant_overwrite,
    cant_write_dir,
    no_parent_dir,
    bad_block_write_len,
    illegal_block_boundary,
    workfile_delete_fail,
    unsupported_ext,
    dest_exists,
    parent_not_dir,
    cant_write_parent,
    parent_create_fail,
    tar_field_toobig,
    missing_supp_path,
    nonfile_entry,
    read_lt_1,
    data_changed,
    unexpected_header_key,
    tarreader_syntaxerr,
    unsupported_mode,
    dir_x_conflict,
    pif_unknown_datasize,
    pif_data_toobig,
    data_size_unknown,
    extraction_exists,
    extraction_exists_notfile,
    extraction_parent_not_dir,
    extraction_parent_not_writable,
    extraction_parent_mkfail,
    write_count_mismatch,
    header_field_missing,
    checksum_mismatch,
    create_only_normal,
    bad_header_value,
    bad_numeric_header_value,
    listing_format,
    ;

    private static ValidatingResourceBundle vrb =
            new ValidatingResourceBundle(
                    RB.class.getPackage().getName() + ".rb", RB.class);
    static {
        vrb.setMissingPosValueBehavior(
                ValidatingResourceBundle.NOOP_BEHAVIOR);
        vrb.setMissingPropertyBehavior(
                ValidatingResourceBundle.NOOP_BEHAVIOR);
    }

    public String getString() {
        return vrb.getString(this);
    }
    public String toString() {
        return ValidatingResourceBundle.resourceKeyFor(this);
    }
    public String getExpandedString() {
        return vrb.getExpandedString(this);
    }
    public String getExpandedString(String... strings) {
        return vrb.getExpandedString(this, strings);
    }
    public String getString(String... strings) {
        return vrb.getString(this, strings);
    }
    public String getString(int i1) {
        return vrb.getString(this, i1);
    }
    public String getString(int i1, int i2) {
        return vrb.getString(this, i1, i2);
    }
    public String getString(int i1, int i2, int i3) {
        return vrb.getString(this, i1, i2, i3);
    }
    public String getString(int i1, String s2) {
        return vrb.getString(this, i1, s2);
    }
    public String getString(String s1, int i2) {
        return vrb.getString(this, s1, i2);
    }
    public String getString(int i1, int i2, String s3) {
        return vrb.getString(this, i1, i2, s3);
    }
    public String getString(int i1, String s2, int i3) {
        return vrb.getString(this, i1, s2, i3);
    }
    public String getString(String s1, int i2, int i3) {
        return vrb.getString(this, s1, i2, i3);
    }
    public String getString(int i1, String s2, String s3) {
        return vrb.getString(this, i1, s3, s3);
    }
    public String getString(String s1, String s2, int i3) {
        return vrb.getString(this, s1, s2, i3);
    }
    public String getString(String s1, int i2, String s3) {
        return vrb.getString(this, s1, i2, s3);
    }
}
