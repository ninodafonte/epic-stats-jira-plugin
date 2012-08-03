package es.testingserver.atlassian.epicstats;

/**
 * Created with IntelliJ IDEA.
 * User: nino
 * Date: 3/08/12
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
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
