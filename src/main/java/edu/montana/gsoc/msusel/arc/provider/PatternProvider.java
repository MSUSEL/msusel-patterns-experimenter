package edu.montana.gsoc.msusel.arc.provider;

import edu.montana.gsoc.msusel.arc.Provider;

public interface PatternProvider extends Provider {

    void loadData();

    void updateDatabase();
}
