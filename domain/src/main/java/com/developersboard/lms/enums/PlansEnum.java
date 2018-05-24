package com.developersboard.lms.enums;

/**
 * Created by Eric on 3/29/2018.
 *
 * @author Eric
 */
public enum PlansEnum {

    BASIC(1, "Basic"),
    PRO(2, "Pro");

    private int id;

    private String plan;

    PlansEnum(int id, String plan) {
        this.id = id;
        this.plan = plan;
    }

    public int getId() {
        return id;
    }

    public String getPlan() {
        return plan;
    }
}
