package edu.montana.gsoc.msusel.patterns.codetree;

import edu.montana.gsoc.msusel.codetree.INode;
import edu.montana.gsoc.msusel.codetree.node.StructuralNode;

public class PatternNode extends StructuralNode {

    public PatternNode(String qIdentifier) {
        super(qIdentifier);
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void update(INode iNode) {

    }

    @Override
    public INode cloneNoChildren() {
        return null;
    }
}
