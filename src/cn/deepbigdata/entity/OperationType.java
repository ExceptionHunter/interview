package cn.deepbigdata.entity;

public enum OperationType {
    SPLIT(0,"split"),
    MERGE(1,"merge"),
    ROOT(2, "root");

    private int value;
    private String desc;

    OperationType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
