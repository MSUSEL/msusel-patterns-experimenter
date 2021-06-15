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
queryMap["rightByAccessDetailTargetId"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right." +
    "RightDO userRight " +
    "WHERE " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail " +
    "AND " +
    "userRight.targetId=:targetId "
queryArgumentsMap["rightByAccessDetailTargetId"] = ["access", "detail",
    "targetId"]
queryMap["rightByGroupIdAccessDetail"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right." +
    "RightDO userRight " +
    "WHERE " +
    "userRight.group.id=:groupId " +
    "AND " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail"
queryArgumentsMap["rightByGroupIdAccessDetail"] = ["groupId", "access",
    "detail"]
queryMap["rightByUserNameAccessDetail"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right." +
    "RightDO AS userRight " +
    "INNER JOIN " +
    "com.ivata.groupware.admin.security.user.UserDO AS person_user " +
    "WHERE " +
    "person_user.name=:userName " +
    "AND " +
    "person_user in elements(userRight.group.users) " +
    "AND " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail"
queryArgumentsMap["rightByUserNameAccessDetail"] = ["userName", "access",
    "detail"]
queryMap["rightByUserNameAccessDetailTargetId"] =
    "SELECT userRight " +
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right." +
    "RightDO AS userRight, " +
    "com.ivata.groupware.admin.security.user.UserDO AS person_user " +
    "WHERE " +
    "person_user.name=:userName " +
    "AND " +
    "person_user in elements(userRight.group.users) " +
    "AND " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail " +
    "AND " +
    "userRight.targetId=:targetId "
queryArgumentsMap["rightByUserNameAccessDetailTargetId"] = ["userName",
    "access", "detail", "targetId"]
queryMap["rightGroupIdByAccessDetailTargetId"] =
    "SELECT group.id " +
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right." +
    "RightDO AS userRight " +
    "WHERE " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail" +
    "AND " +
    "userRight.targetId=:targetId "
queryArgumentsMap["rightGroupIdByAccessDetailTargetId"] = ["access",
    "detail", "targetId"]
queryMap["rightTargetIdByGroupIdAccessDetail"] =
    "SELECT targetId " +
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right" +
    ".RightDO AS userRight " +
    "WHERE " +
    "userRight.group.id = groupId " +
    "AND " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail"
queryArgumentsMap["rightTargetIdByGroupIdAccessDetail"] = ["groupId",
    "access", "detail"]
queryMap["rightTargetIdByUserNameAccessDetail"] =
    "SELECT userRight.targetId " +
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.right" +
    ".RightDO AS userRight, " +
    "com.ivata.groupware.admin.security.user.UserDO AS " +
    "person_user " +
    "WHERE " +
    "person_user.name=:userName " +
    "AND " +
    "person_user in elements(userRight.group.users) " +
    "AND " +
    "userRight.access=:access " +
    "AND " +
    "userRight.detail=:detail"
queryArgumentsMap["rightTargetIdByUserNameAccessDetail"] = ["userName",
    "access", "detail"]
