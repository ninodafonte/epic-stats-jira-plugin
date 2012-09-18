package com.aboutcoders.atlassian.entities;

public class Filter
{
    private String name;
    private String jql;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJql() {
        return jql;
    }

    public void setJql(String jql) {
        this.jql = jql;
    }
}
