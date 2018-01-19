package edu.montana.gsoc.msusel.patterns.collector.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import edu.montana.gsoc.msusel.patterns.datamodel.Pattern;
import edu.montana.gsoc.msusel.patterns.datamodel.PatternInstance;
import edu.montana.gsoc.msusel.patterns.datamodel.Project;
import edu.montana.gsoc.msusel.patterns.datamodel.Role;
import edu.montana.gsoc.msusel.patterns.collector.AbstractCollector;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Slf4j
public class Pattern4Collector extends AbstractCollector {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    @Setter
    private String projName;
    @Setter
    private String projVersion;

    @Builder(buildMethodName = "create")
    public Pattern4Collector(String projName, String projVersion, String resultFile) {
        super(resultFile, "Pattern4 Collector");
        this.projName = projName;
        this.projVersion = projVersion;
    }

    @Override
    public void execute() {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(Project.class);
        xstream.processAnnotations(Pattern.class);
        xstream.processAnnotations(PatternInstance.class);
        xstream.processAnnotations(Role.class);

        Project proj = (Project) xstream.fromXML(new File(this.resultsFile));


        proj.setDate(new Timestamp(System.currentTimeMillis()).toString());
        proj.setName(projName);
        proj.setVersion(projVersion);

        System.out.println(proj);
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File("msusel-patterns/data/test/pattern_detector13-2.xml")))) {
            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xstream.toXML(proj, pw);
        } catch(IOException e) {
            log.error(e.getMessage());
        }
    }
}
