package cn.deepbigdata.entity;

import java.math.BigDecimal;
import java.util.List;

public class Node {
    private List<Node> parents;
    private OperationType operationType;
    private BigDecimal value;
    private int xIndex;
    private List<Node> children;
    private int level;
    private BigDecimal base;

    public Node(List<Node> parents, OperationType operationType, BigDecimal value, int xIndex, int level) {
        this.parents = parents;
        this.operationType = operationType;
        if (OperationType.SPLIT == operationType) {
            BigDecimal parentsValue = new BigDecimal("0");
            for (Node parent : parents) {
                parentsValue = parentsValue.add(parent.getValue());
            }
            this.value = value;
            this.base = parentsValue;
        }

        if (OperationType.ROOT == operationType) {
            this.value = value;
        }

        this.xIndex = xIndex;
        this.level = level;
    }

    public BigDecimal getValue() {
        BigDecimal realValue = new BigDecimal("0");
        BigDecimal parentsValue = new BigDecimal("0");
        for (Node parent : parents) {
            parentsValue = parentsValue.add(parent.getValue());
        }
        switch (operationType) {
            case ROOT:
                realValue = value;
                break;
            case MERGE:
                realValue = parentsValue;
                break;
            case SPLIT:
                realValue = parentsValue.multiply(value).divide(base,BigDecimal.ROUND_HALF_UP);
                break;
        }
        return realValue;
    }

    public List<Node> getParents() {
        return parents;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public int getxIndex() {
        return xIndex;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getLevel() {
        return level;
    }

    public void setValue(BigDecimal value) {
        if (OperationType.SPLIT == operationType) {
            BigDecimal parentsValue = new BigDecimal("0");
            for (Node parent : parents) {
                parentsValue = parentsValue.add(parent.getValue());
            }
            this.value = value.divide(parentsValue);
        }

        if (OperationType.ROOT == operationType) {
            this.value = value;
        }
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}
