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
// find a country by its two letter code
queryMap["addressBookCountryByCode"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.address.country." +
    "CountryDO country " +
    "WHERE " +
    "country.code=:code "
queryArgumentsMap["addressBookCountryByCode"] = ["code"]

// find all child groups in a parent group
queryMap["addressBookGroupsInGroup"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.GroupDO " +
    "person_group " +
    "WHERE " +
    "person_group.parent.id=:parentId "
queryArgumentsMap["addressBookGroupsInGroup"] = ["parentId"]

// find one group in a parent group by its name
queryMap["addressBookGroupsInGroupByName"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.group.GroupDO " +
    "person_group " +
    "WHERE " +
    "person_group.name=:name " +
    "AND " +
    "person_group.parent.id=:parentId "
queryArgumentsMap["addressBookGroupsInGroupByName"] = ["parentId", "name"]
queryMap["addressBookPersonByAddressBookId"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.PersonDO " +
    "person " +
    "WHERE " +
    "person.group.addressBook.id=:addressBookId"
queryArgumentsMap["addressBookPersonByAddressBookId"] = ["addressBookId"]
queryMap["addressBookPersonByUserName"] =
    "FROM " +
    "com.ivata.groupware.business.addressbook.person.PersonDO " +
    "person " +
    "WHERE " +
    "person.user.name=:userName"
queryArgumentsMap["addressBookPersonByUserName"] = ["userName"]
////////////////////////////////////////////////////////////////////////////////
