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
package edu.montana.gsoc.msusel.arc.impl.quamoco;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.graph.Network;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.FlowPhase;
import edu.montana.gsoc.msusel.arc.anot.Phase;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;
import edu.montana.gsoc.msusel.arc.datamodel.Violation;
import edu.montana.gsoc.msusel.quamoco.distiller.ModelDistiller;
import edu.montana.gsoc.msusel.quamoco.distiller.ModelManager;
import edu.montana.gsoc.msusel.quamoco.graph.edge.Edge;
import edu.montana.gsoc.msusel.quamoco.graph.node.FactorNode;
import edu.montana.gsoc.msusel.quamoco.graph.node.FindingNode;
import edu.montana.gsoc.msusel.quamoco.graph.node.Node;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Phase(FlowPhase.SECONDARY_ANALYSIS)
public class QuamocoCommand extends SecondaryAnalysisCommand {

    ArcContext context;
    Network<Node, Edge> graph;
    QuamocoConfig config;

    public QuamocoCommand() {
        super("Quamoco");
    }

    @Override
    public void execute(ArcContext context) {
        // preliminaries
        this.context = context;
        String configFile = context.getArcProperty(QuamocoConstants.QUAMOCO_CONFIG_FILE);
        config = QuamocoConfig.loadConfig(configFile);

        // Build graph
        graph = buildGraph();

        // Connect Issues
        connectFindings();

        // Execute Quamoco
        executeQuamoco();

        // Store Results
        storeResults();
    }

    private Network<Node, Edge> buildGraph() {
        String baseDir = context.getArcProperty(QuamocoConstants.QM_HOME_PROP_KEY);


        String lang = context.getLanguage();

        String[] qmFiles = config.getQMFiles(lang);
        for (int i = 0; i < qmFiles.length; i++)
            qmFiles[i] = Paths.get(baseDir, qmFiles[i]).toAbsolutePath().toString();
        ModelDistiller md = new ModelDistiller(new ModelManager());
        md.readInQualityModels(qmFiles);
        md.buildGraph();
        return md.getGraph();
    }

    private void connectFindings() {
        graph.nodes().forEach(node -> {
            if (node instanceof FindingNode) {
                FindingNode fn = (FindingNode) node;

                String rule = fn.getRuleName();
                String repo = fn.getToolName();

                List<Violation> violations = context.getFindings(repo, rule);

                violations.forEach(v -> {
                    fn.addFinding(v.createFinding());
                });
            }
        });
    }

    private void executeQuamoco() {
        String root = config.getRootQualityAspect(context.getLanguage());

        FactorNode rootNode = null;
        for (Node n : graph.nodes()) {
            if (n instanceof FactorNode) {
                if (n.getName().equals(root))
                {
                    rootNode = (FactorNode) n;
                    break;
                }
            }
        }

        if (rootNode != null) {
            rootNode.getValue();
        }
    }

    private void storeResults() {
        Map<String, FactorNode> map = Maps.newHashMap();
        List<String> keys = Lists.newArrayList(config.getQualityAspects(context.getLanguage()));

        for (Node n : graph.nodes()) {
            if (n instanceof FactorNode && keys.contains(n.getName())) {

            }
        }
    }
}
