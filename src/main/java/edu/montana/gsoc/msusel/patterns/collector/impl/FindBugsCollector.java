package edu.montana.gsoc.msusel.patterns.collector.impl;

import edu.montana.gsoc.msusel.patterns.collector.AbstractCollector;
import edu.montana.gsoc.msusel.patterns.collector.impl.fb.BugCollection;
import edu.montana.gsoc.msusel.patterns.collector.impl.fb.SourceLine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class FindBugsCollector extends AbstractCollector {

    public FindBugsCollector(String resultsFile) {
        super(resultsFile, "FindBugs Collector");
    }

    @Override
    public void execute() {
        try {
            JAXBContext context = JAXBContext.newInstance(BugCollection.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            BugCollection bugColl = (BugCollection) unmarshaller.unmarshal(new File(resultsFile));
            System.out.println("Instances Found: " + bugColl.getBugInstance().size());

            bugColl.getBugInstance().forEach(inst -> {
                String type = inst.getType();
                short priority = inst.getPriority();
                String cat = inst.getCategory();
                long rank = inst.getRank();
                inst.getClazzOrTypeOrMethod().forEach(obj -> {
                    if (obj instanceof BugCollection.BugInstance.Class) {

                    }
                    else if (obj instanceof BugCollection.BugInstance.Type) {

                    }
                    else if (obj instanceof BugCollection.BugInstance.Method) {

                    }
                    else if (obj instanceof SourceLine) {

                    }
                    else if (obj instanceof BugCollection.BugInstance.LocalVariable) {

                    }
                    else if (obj instanceof BugCollection.BugInstance.Field) {

                    }
                    else if (obj instanceof BugCollection.BugInstance.Property) {

                    }
                });
            });
        } catch (JAXBException e) {

        }
    }

    public static void main(String args[]) {
        new FindBugsCollector("/home/git/msusel/msusel-patterns/data/fbresults/fbresults.xml").execute();
    }
}
