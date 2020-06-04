package edu.montana.gsoc.msusel.arc.impl.findbugs;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface FindBugsConstants {

    String FB_CMD_NAME = "FindBugs";
    String FB_COLL_NAME = "FindBugs Collector";
    int FB_CMD_EXIT_VALUE = 0;

    String REPORT_FILE_NAME = "findbugs_results.xml";

    String FB_CONFIG_PATH = "/edu/montana/gsoc/msusel/arc/impl/findbugs/rules-findbugs.xml";
    String FB_REPO_NAME = "findbugs";
    String FB_REPO_KEY = "findbugs";

    String FB_CONTRIB_CONFIG_PATH = "/edu/montana/gsoc/msusel/arc/impl/findbugs/rules-fbcontrib.xml";
    String FB_CONTRIB_REPO_NAME = "fbcontrib";
    String FB_CONTRIB_REPO_KEY = "fbcontrib";

    String FB_SEC_CONFIG_PATH = "/edu/montana/gsoc/msusel/arc/impl/findbugs/rules-findsecbugs.xml";
    String FB_SEC_REPO_NAME = "findsecbugs";
    String FB_SEC_REPO_KEY = "findsecbugs";
}
