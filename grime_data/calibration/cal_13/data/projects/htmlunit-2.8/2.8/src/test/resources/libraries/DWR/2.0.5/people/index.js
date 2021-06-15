/*
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

function init() {
  dwr.util.useLoadingMessage();
  Tabs.init('tabList', 'tabContents');
  fillTable();
}

var peopleCache = { };
var viewed = -1;

function fillTable() {
  People.getAllPeople(function(people) {
    // Delete all the rows except for the "pattern" row
    dwr.util.removeAllRows("peoplebody", { filter:function(tr) {
      return (tr.id != "pattern");
    }});
    // Create a new set cloned from the pattern row
    var person, id;
    people.sort(function(p1, p2) { return p1.name.localeCompare(p2.name); });
    for (var i = 0; i < people.length; i++) {
      person = people[i];
      id = person.id;
      dwr.util.cloneNode("pattern", { idSuffix:id });
      dwr.util.setValue("tableName" + id, person.name);
      dwr.util.setValue("tableSalary" + id, person.salary);
      dwr.util.setValue("tableAddress" + id, person.address);
      $("pattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
      peopleCache[id] = person;
    }
  });
}

function editClicked(eleid) {
  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
  var person = peopleCache[eleid.substring(4)];
  dwr.util.setValues(person);
}

function deleteClicked(eleid) {
  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
  var person = peopleCache[eleid.substring(6)];
  if (confirm("Are you sure you want to delete " + person.name + "?")) {
    dwr.engine.beginBatch();
    People.deletePerson({ id:person.id });
    fillTable();
    dwr.engine.endBatch();
  }
}

function writePerson() {
  var person = { id:viewed, name:null, address:null, salary:null };
  dwr.util.getValues(person);

  dwr.engine.beginBatch();
  People.setPerson(person);
  fillTable();
  dwr.engine.endBatch();
}

function clearPerson() {
  viewed = -1;
  dwr.util.setValues({ id:-1, name:null, address:null, salary:null });
}
