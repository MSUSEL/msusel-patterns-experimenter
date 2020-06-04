package edu.montana.gsoc.msusel.arc.provider;

import edu.montana.gsoc.msusel.arc.Provider;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public interface PatternProvider extends Provider {

    void loadData();

    void updateDatabase();
}
