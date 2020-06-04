package edu.montana.gsoc.msusel.arc.provider;

import edu.isu.isuese.datamodel.RuleRepository;
import edu.montana.gsoc.msusel.arc.ArcContext;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public abstract class AbstractRuleProvider implements RuleProvider {

    protected RuleRepository repository;
    protected ArcContext context;

    public AbstractRuleProvider(ArcContext context) {
        this.context = context;
    }

    @Override
    public void load() {
        context.logger().atInfo().log("%s: loading data", this.getClass().getSimpleName());
        loadData();
        context.logger().atInfo().log("%s: updating the database", this.getClass().getSimpleName());
        updateDatabase();
    }
}
