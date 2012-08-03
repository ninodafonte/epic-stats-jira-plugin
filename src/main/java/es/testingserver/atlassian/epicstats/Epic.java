package es.testingserver.atlassian.epicstats;

public class Epic
{
    protected String _summary;
    protected String _key;

    public String getSummary() {
        return _summary;
    }

    public void setSummary(String _summary) {
        this._summary = _summary;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String _key) {
        this._key = _key;
    }

}
