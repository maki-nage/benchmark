package com.makinage.kafka.dsl.benchmark;

public class Data {

    public Data(Long field1, Double field2, Boolean field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public Long field1;
    public Double field2;
    public Boolean field3;

    @Override
    public String toString() {
        return "Data{" +
                "field1=" + field1 +
                ", field2=" + field2 +
                ", field3=" + field3 +
                '}';
    }
}
