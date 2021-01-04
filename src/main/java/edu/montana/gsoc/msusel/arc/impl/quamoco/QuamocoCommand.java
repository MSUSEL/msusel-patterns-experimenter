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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.graph.Network;
import edu.isu.isuese.datamodel.Measure;
import edu.isu.isuese.datamodel.MetricRepository;
import edu.montana.gsoc.msusel.arc.ArcContext;
import edu.montana.gsoc.msusel.arc.FlowPhase;
import edu.montana.gsoc.msusel.arc.anot.Phase;
import edu.montana.gsoc.msusel.arc.command.SecondaryAnalysisCommand;
import edu.montana.gsoc.msusel.arc.impl.metrics.MetricsConstants;
import edu.montana.gsoc.msusel.quamoco.distiller.ModelDistiller;
import edu.montana.gsoc.msusel.quamoco.distiller.ModelManager;
import edu.montana.gsoc.msusel.quamoco.distiller.QuamocoContext;
import edu.montana.gsoc.msusel.quamoco.graph.edge.Edge;
import edu.montana.gsoc.msusel.quamoco.graph.node.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Phase(FlowPhase.SECONDARY_ANALYSIS)
public class QuamocoCommand extends SecondaryAnalysisCommand {

    ArcContext context;
    Network<Node, Edge> graph;

    public QuamocoCommand() {
        super(QuamocoConstants.QUAMOCO_CMD_NAME);
    }

    @Override
    public void execute(ArcContext context) {
        context.logger().atInfo().log("Executing Quamoco Analysis");
        // preliminaries
        this.context = context;
        loadConfig();

        QuamocoContext.instance().setProject(context.getProject());
        QuamocoContext.instance().setMetricRepoKey(MetricsConstants.METRICS_REPO_KEY);

        context.open();

        // Build graph
        graph = buildGraph();

        // Connect Values
        //connectValues(); Not needed as these are all normalization measures and handled by the extents classes

        // Connect Issues
        connectFindings();

        // Execute Quamoco
        executeQuamoco();

        // Store Results
        storeResults();

        context.close();

        context.logger().atInfo().log("Finished Quamoco Analysis");
    }

    private Network<Node, Edge> buildGraph() {
        context.logger().atInfo().log("Creating Quamoco Processing Graph");
        String baseDir = context.getArcProperty(QuamocoConstants.QM_HOME_PROP_KEY);

        String lang = context.getLanguage();

        String[] qmFiles = getQMFiles(lang.toLowerCase());
        for (int i = 0; i < qmFiles.length; i++) {
            System.out.println("QMFile[" + i + "]: " + qmFiles[i]);
            System.out.println("baseDir: " + baseDir);
            qmFiles[i] = Paths.get(baseDir, qmFiles[i]).toAbsolutePath().toString();
            System.out.println("QMFile[" + i + "]: " + qmFiles[i]);
        }
        ModelDistiller md = new ModelDistiller(new ModelManager());
        md.readInQualityModels(qmFiles);
        md.buildGraph();

        context.logger().atInfo().log("Quamoco Processing Graph Created");
        return md.getGraph();
    }

//    private void connectValues() {
//        context.logger().atInfo().log("Connecting Values to Quamoco Processing Graph");
//
//        graph.nodes().forEach(node -> {
//            if (node instanceof ValueNode) {
//                ValueNode vn = (ValueNode) node;
//
//                MetricsRegistrar reg = MetricsRegistrar.instances.get(QuamocoContext.instance().getMetricRepoKey())
//                String handle = reg.getHandle(vn.getMetric());
//                String repo = QuamocoContext.instance().getMetricRepoKey();
//
//                List<Double> values = Measure.getAllClassValues(context.getProject(), repo, handle);
//                values.forEach(vn::addValue);
//            }
//        });
//
//        context.logger().atInfo().log("Values connected to Quamoco Processing Graph");
//    }

    private void connectFindings() {
        context.logger().atInfo().log("Connecting Findings to Quamoco Processing Graph");
        graph.nodes().forEach(node -> {
            if (node instanceof FindingNode) {
                FindingNode fn = (FindingNode) node;

                String rule = fn.getRuleName();
                String repo = fn.getToolName();

                List<edu.isu.isuese.datamodel.Finding> findings = context.getProject().getFindings(rule);

                findings.forEach(v -> {
                    fn.addFinding(createFinding(v));
                });
            }
        });
        context.logger().atInfo().log("Findings connected to Quamoco Processing Graph");
    }

    private void executeQuamoco() {
        context.logger().atInfo().log("Excecuting Quamoco Analysis Engine");
        String root = context.getArcProperty(QuamocoProperties.QUAMOCO_METRICS_ROOT);

        FactorNode rootNode = null;
        for (Node n : graph.nodes()) {
            if (n instanceof FactorNode) {
                if (n.getName().equals(root)) {
                    rootNode = (FactorNode) n;
                    break;
                }
            }
        }

        if (rootNode != null) {
            rootNode.getValue();
        }
        context.logger().atInfo().log("Quamoco Analysis Engine Finished");
    }

    private void storeResults() {
        context.logger().atInfo().log("Storing Quamoco Results");
        Map<String, FactorNode> map = Maps.newHashMap();
        List<String> keys = getQualityAspects();

        for (Node n : graph.nodes()) {
            if (n instanceof FactorNode && keys.contains(n.getName())) {
                Measure.of(QuamocoConstants.QUAMOCO_REPO_KEY + ":" + n.getName())
                        .on(context.getProject())
                        .withValue(n.getValue());
            }
        }
    }

    private Finding createFinding(edu.isu.isuese.datamodel.Finding finding) {
        System.out.println("arg1: " + finding.getReferences().get(0).getReferencedComponent(context.getProject()));
        System.out.println("arg1 name: " + finding.getReferences().get(0).getRefKey());
        System.out.println("arg2: " + finding.getParentRule().getKey());
        System.out.println("arg3: " + finding.getParentRule().getName());
        return new ComponentFinding(finding.getReferences().get(0).getReferencedComponent(context.getProject()), finding.getParentRule().getKey(), finding.getParentRule().getName());
    }

    private void loadConfig() {
        context.logger().atInfo().log("Loading Quamoco Configuration");
        try(FileInputStream fis = new FileInputStream(new File(QuamocoConstants.QUAMOCO_LANG_MODELS_FILE))) {
            Properties props = new Properties();
            props.load(fis);

            props.forEach((key, value) -> context.addArcProperty((String) key, (String) value));
        } catch (Exception e) {
            context.logger().atError().withThrowable(e).log(e.getMessage());
        }
        try(FileInputStream fis = new FileInputStream(new File(QuamocoConstants.QUAMOCO_METRICS_FILE))) {
            Properties props = new Properties();
            props.load(fis);

            props.forEach((key, value) -> context.addArcProperty((String) key, (String) value));
        } catch (Exception e) {
            context.logger().atError().withThrowable(e).log(e.getMessage());
        }
        context.logger().atInfo().log("Finished Loading Quamoco Configuration");
    }

    private String[] getQMFiles(String lang) {
        String models = context.getArcProperty(String.format(QuamocoProperties.QUAMOCO_LANG_MODELS, lang));
        String[] files = models.split(",");
        for (int i = 0; i < files.length; i++)
            files[i] = files[i] + ".qm";

        return files;
    }

    private List<String> getQualityAspects() {
        MetricRepository repo = MetricRepository.findFirst("repoKey = ?", QuamocoConstants.QUAMOCO_REPO_KEY);
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        repo.getMetrics().forEach(metric -> builder.add(metric.getName()));
        return builder.build();
    }
}
