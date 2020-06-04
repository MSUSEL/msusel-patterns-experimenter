package edu.montana.gsoc.msusel.arc.impl.ghsearch;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface GitHubSearchProperties {

    String GIT_HUB_USER     = "arc.ghsearch.github.username";
    String GIT_HUB_TOKEN    = "arc.ghsearch.github.token";
    String SEARCH_MAX_PROJ  = "arc.ghsearch.projects.max";
    String SEARCH_MIN_SIZE  = "arc.ghsearch.size.min";
    String SEARCH_MAX_SIZE  = "arc.ghsearch.size.max";
    String SEARCH_MIN_STARS = "arc.ghsearch.stars.min";
    String SEARCH_MIN_TAGS  = "arc.ghsearch.tags.min";
}
