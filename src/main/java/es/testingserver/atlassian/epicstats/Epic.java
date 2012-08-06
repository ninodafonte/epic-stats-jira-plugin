package es.testingserver.atlassian.epicstats;

public class Epic
{
    protected String _summary;
    protected String _key;
    protected double _totalStoryPoints;
    protected double _burnedStoryPoints;

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

    public double getTotalStoryPoints() {
        return _totalStoryPoints;
    }

    public void setTotalStoryPoints(Double _totalStoryPoints) {
        this._totalStoryPoints = _totalStoryPoints;
    }

    public double getBurnedStoryPoints() {
        return _burnedStoryPoints;
    }

    public void setBurnedStoryPoints(Double _burnedStoryPoints) {
        this._burnedStoryPoints = _burnedStoryPoints;
    }

    public int getPercentComplete() {
        return (int) (( this._burnedStoryPoints / this._totalStoryPoints ) * 100);
    }
}
