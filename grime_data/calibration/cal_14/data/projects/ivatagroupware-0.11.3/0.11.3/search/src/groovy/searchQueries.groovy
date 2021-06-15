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
queryMap["searchIndexByContentId"] =
    "SELECT index " +
    "FROM " +
    "com.ivata.groupware.business.search.index.SearchIndexDO " +
    "index, " +
    "com.ivata.groupware.business.search.item.content." +
    "SearchItemContentDO content " +
    "WHERE " +
    "content.id=:contentId" +
    "AND " +
    "content in elements(index.contents) "
queryArgumentsMap["searchIndexByContentId"] = ["contentId"]
queryMap["searchIndexByItemId"] =
    "SELECT index " +
    "FROM " +
    "com.ivata.groupware.business.search.index.SearchIndexDO " +
    "index, " +
    "com.ivata.groupware.business.search.item.content." +
    "SearchItemContentDO content " +
    "WHERE " +
    "content.item.id=:itemId" +
    "AND " +
    "content in elements(index.contents) "
queryArgumentsMap["searchIndexByItemId"] = ["itemId"]
queryMap["searchIndexByWord"] =
    "FROM " +
    "com.ivata.groupware.business.search.index.SearchIndexDO " +
    "index " +
    "WHERE " +
    "index.word=:word "
queryArgumentsMap["searchIndexByWord"] = ["word"]
queryMap["searchItemByTargetIdTypeCategory"] =
    "FROM " +
    "com.ivata.groupware.business.search.item.SearchItemDO item " +
    "WHERE " +
    "item.targetId=:targetId " +
    "AND " +
    "item.type=:type " +
    "AND " +
    "item.category=:category "
queryArgumentsMap["searchItemByTargetIdTypeCategory"] = ["targetId", "type",
    "category"]
queryMap["searchItemContentByTargetIdType"] =
    "FROM " +
    "com.ivata.groupware.business.search.item.content." +
    "SearchItemContentDO content " +
    "WHERE " +
    "content.targetId=:targetId " +
    "AND " +
    "content.type=:type " +
    "AND " +
    "item.category=:category "
queryArgumentsMap["searchItemContentByTargetIdType"] = ["targetId", "type"]
