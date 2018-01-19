package edu.montana.gsoc.msusel.patterns.collector.impl;

import edu.montana.gsoc.msusel.patterns.collector.AbstractCollector;
import edu.montana.gsoc.msusel.patterns.collector.impl.pmd.Pmd;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
public class PMDCollector extends AbstractCollector {

    public PMDCollector(String resultsFile) {
        super(resultsFile, "PMD Collector");
    }

    @Override
    public void execute() {
        try {
            JAXBContext context = JAXBContext.newInstance(Pmd.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Pmd pmd = (Pmd) unmarshaller.unmarshal(new File(resultsFile));
            System.out.println("Files: " + pmd.getFile().size());
            pmd.getFile().forEach(f -> System.out.println("File: " + f.getName() + " has " + f.getViolation().size() + " violations"));


        } catch (JAXBException e) {
            log.error(e.getMessage());
        }
    }

    public static void main(String args[]) {
        new PMDCollector("/home/git/msusel/msusel-patterns/data/pmdresults/pmdreport.xml").execute();
    }
}
