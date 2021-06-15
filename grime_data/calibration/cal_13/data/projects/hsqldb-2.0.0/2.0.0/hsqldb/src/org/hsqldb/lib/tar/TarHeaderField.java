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

/**
 * Purely static structure defining our interface to the Tar Entry Header.
 *
 * The fields controlled here are fields for the individual tar file entries
 * in an archive.  There is no such thing as a Header Field at the top archive
 * level.
 * <P>
 * We use header field names as they are specified in the FreeBSD man page for
 * tar in section 5 (Solaris and Linux have no such page in section 5).
 * Where we use a constant, the constant name is just the FreeBSD field name
 * capitalized.
 * Since a single field is known as either "linkflag" or "typeflag", we are
 * going with the UStar name typeflag for this field.
 * </P> <P>
 * We purposefully define no variable for this list of fields, since
 * we DO NOT WANT TO access or change these values, due to application
 * goals or JVM limitations:<UL>
 *   <LI>gid
 *   <LI>uid
 *   <LI>linkname
 *   <LI>magic (UStar ID),
 *   <LI>magic version
 *   <LI>group name
 *   <LI>device major num
 *   <LI>device minor num
 * </UL>
 * Our application has no use for these, or Java has no ability to
 * work with them.
 * </P> <P>
 * This class will be very elegant when refactored as an enum with enumMap(s)
 * and using generics with auto-boxing instead of the ugly and non-validating
 * casts.
 * </P>
 *
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 */
@SuppressWarnings("boxing")
public enum TarHeaderField {
    // 1 PAST last position (in normal Java substring fashion).
    /* Note that (with one exception), there is always 1 byte
     * between a numeric field stop and the next start.  This is
     * because null byte must occupy the intervening position.
     * This is not true for non-numeric fields (which includes the
     * link-indicator/type-flag field, which is used as a code,
     * and is not necessarily numeric with UStar format).
     *
     * As a consequence, there may be NO DELIMITER after
     * non-numerics, which may occupy the entire field segment.
     *
     * Arg.  man page for "pax" says that both original and ustar
     * headers must be <= 100 chars. INCLUDING the trailing \0
     * character.  ???  GNU tar certainly does not honor this.
     */
    name(0, 100),
    mode(100, 107),
    uid(108, 115),
    gid(116, 123),
    size(124, 135),
    mtime(136, 147),  // (File.lastModified()|*.getTime())/1000
    checksum(148, 156),// "Queer terminator" in original code.  ???
                      // Pax UStore does not follow spec and delimits this
                      // field like any other numeric, skipping the space byte.
    typeflag(156, 157), // 1-byte CODE
        // With current version, we are never doing anything with this
        // field.  In future, we will support x and/or g type here.
        // N.b. Gnu Tar does not honor this Stop.

    // The remaining are from UStar format:
    magic(257, 263),
    uname(265, 296),
    gname(297, 328),
    prefix(345, 399),
    ;

    private TarHeaderField(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }
    private int start, stop;

    // The getters below throw RuntimExceptions instead of
    // TarMalformatExceptions because these errors indicate a dev problem,
    // not some problem with a Header, or generating or reading a Header.
    public int getStart() { return start; }
    public int getStop() { return stop; }
}
