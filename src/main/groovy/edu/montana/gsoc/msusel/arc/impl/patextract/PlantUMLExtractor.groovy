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
package edu.montana.gsoc.msusel.arc.impl.patextract

import com.google.common.collect.Sets
import edu.isu.isuese.datamodel.Type

class PlantUMLExtractor {

    Set<Type> types
    Set<Type> related

    PlantUMLExtractor(Set<Type> types, Set<Type> related) {
        this.types = types
        this.related = related
    }

    String generateClassDiagram() {
        StringBuilder builder = new StringBuilder()

        builder << "@startuml\n"
        types.each {
            String val = plantUmlForType(it, true)
            if (val) builder << "${val}\n"
        }
        related.each {
            String val = plantUmlForType(it, false)
            if (val) builder << "${val}\n"
        }
        generateRelationships(builder)
        builder << "@enduml"
    }

    private void generateRelationships(StringBuilder builder) {
        types.each {first ->
            first.getGeneralizedBy().each { second -> builder << "${first.getFullName()} --|> ${second.getFullName()}" }
            first.getGeneralizes().each { second -> builder << "${first.getFullName()} <|-- ${second.getFullName()}" }
            first.getAssociatedFrom().each { second -> builder << "${first.getFullName()} --> ${second.getFullName()}" }
            first.getAssociatedTo().each { second -> builder << "${first.getFullName()} <-- ${second.getFullName()}" }
            first.getComposedFrom().each { second -> builder << "${first.getFullName()} *--> ${second.getFullName()}" }
            first.getComposedTo().each { second -> builder << "${first.getFullName()} <--* ${second.getFullName()}" }
            first.getAggregatedFrom().each { second -> builder << "${first.getFullName()} o--> ${second.getFullName()}" }
            first.getAggregatedTo().each { second -> builder << "${first.getFullName()} <--o ${second.getFullName()}" }
            first.getUseFrom().each { second -> builder << "${first.getFullName()} ..> ${second.getFullName()}" }
            first.getUseTo().each { second -> builder << "${first.getFullName()} <.. ${second.getFullName()}" }
            first.getDependencyFrom().each { second -> builder << "${first.getFullName()} ..> ${second.getFullName()}" }
            first.getDependencyTo().each { second -> builder << "${first.getFullName()} <.. ${second.getFullName()}" }
            first.getRealizedBy().each { second -> builder << "${first.getFullName()} ..|> ${second.getFullName()}" }
            first.getRealizes().each { second -> builder << "${first.getFullName()} <|.. ${second.getFullName()}" }
        }
    }

    private String plantUmlForType(Type type, boolean patternType) {
        String style = patternType ? "#aliceblue ##[bold]blue" : ""

        switch (type.getType()) {
            case Type.CLASS:
                if (type.isAbstract()) return "abstract class ${type.getFullName()}  ${style}"
                else return "class ${type.getFullName()}  ${style}"
            case Type.INTERFACE:
                return "interface ${type.getFullName()}  ${style}"
            case Type.ENUM:
                return "enum ${type.getFullName()}  ${style}"
            case Type.ANNOTATION:
                return "annotation ${type.getFullName()}  ${style}"
            default:
                return null
        }
    }
}
