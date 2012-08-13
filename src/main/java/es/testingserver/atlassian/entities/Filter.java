package es.testingserver.atlassian.entities;

public class Filter
{
    protected String name;
    protected String jql;

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
