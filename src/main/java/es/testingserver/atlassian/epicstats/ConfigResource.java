package es.testingserver.atlassian.epicstats;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/")
public class ConfigResource
{
    private final UserManager userManager;
    private final PluginSettingsFactory pluginSettingsFactory;
    private final TransactionTemplate transactionTemplate;

    public ConfigResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory, TransactionTemplate transactionTemplate)
    {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    @XmlRootElement
    public static final class Config
    {
        @XmlElement
        private String project;

        @XmlElement
        private String epicIssueType;

        @XmlElement
        private String storyIssueType;

        @XmlElement
        private String storyPointsField;

        @XmlElement
        private String epicField;

        @XmlElement
        private String doneStatus;

        @XmlElement
        private String filterName1;

        @XmlElement
        private String filterJql1;

        @XmlElement
        private String filterName2;

        @XmlElement
        private String filterJql2;

        @XmlElement
        private String filterName3;

        @XmlElement
        private String filterJql3;

        @XmlElement
        private String filterName4;

        @XmlElement
        private String filterJql4;

        @XmlElement
        private String filterName5;

        @XmlElement
        private String filterJql5;

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public String getEpicIssueType() {
            return epicIssueType;
        }

        public void setEpicIssueType(String epicIssueType) {
            this.epicIssueType = epicIssueType;
        }

        public String getStoryIssueType() {
            return storyIssueType;
        }

        public void setStoryIssueType(String storyIssueType) {
            this.storyIssueType = storyIssueType;
        }

        public String getStoryPointsField() {
            return storyPointsField;
        }

        public void setStoryPointsField(String storyPointsField) {
            this.storyPointsField = storyPointsField;
        }

        public String getEpicField() {
            return epicField;
        }

        public void setEpicField(String epicField) {
            this.epicField = epicField;
        }

        public String getDoneStatus() {
            return doneStatus;
        }

        public void setDoneStatus(String doneStatus) {
            this.doneStatus = doneStatus;
        }

        public String getFilterName1() {
            return filterName1;
        }

        public void setFilterName1(String filterName1) {
            this.filterName1 = filterName1;
        }

        public String getFilterJql1() {
            return filterJql1;
        }

        public void setFilterJql1(String filterJql1) {
            this.filterJql1 = filterJql1;
        }

        public String getFilterName2() {
            return filterName2;
        }

        public void setFilterName2(String filterName2) {
            this.filterName2 = filterName2;
        }

        public String getFilterJql2() {
            return filterJql2;
        }

        public void setFilterJql2(String filterJql2) {
            this.filterJql2 = filterJql2;
        }

        public String getFilterName3() {
            return filterName3;
        }

        public void setFilterName3(String filterName3) {
            this.filterName3 = filterName3;
        }

        public String getFilterJql3() {
            return filterJql3;
        }

        public void setFilterJql3(String filterJql3) {
            this.filterJql3 = filterJql3;
        }

        public String getFilterName4() {
            return filterName4;
        }

        public void setFilterName4(String filterName4) {
            this.filterName4 = filterName4;
        }

        public String getFilterJql4() {
            return filterJql4;
        }

        public void setFilterJql4(String filterJql4) {
            this.filterJql4 = filterJql4;
        }

        public String getFilterName5() {
            return filterName5;
        }

        public void setFilterName5(String filterName5) {
            this.filterName5 = filterName5;
        }

        public String getFilterJql5() {
            return filterJql5;
        }

        public void setFilterJql5(String filterJql5) {
            this.filterJql5 = filterJql5;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response get(@Context HttpServletRequest request)
    {
        String username = userManager.getRemoteUsername(request);
        if (username == null || !userManager.isSystemAdmin(username))
        {
           return Response.noContent().build();
        }

        return Response.ok(transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction() {
                PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
                Config config = new Config();
                config.setProject((String) settings.get(Config.class.getName() + ".project"));
                config.setEpicIssueType((String) settings.get(Config.class.getName() + ".epicIssueType"));
                config.setStoryIssueType((String) settings.get(Config.class.getName() + ".storyIssueType"));
                config.setStoryPointsField((String) settings.get(Config.class.getName() + ".storyPointsField"));
                config.setEpicField((String) settings.get(Config.class.getName() + ".epicField"));
                config.setDoneStatus((String) settings.get(Config.class.getName() + ".doneStatus"));
                config.setFilterName1((String) settings.get(Config.class.getName() + ".filterName1"));
                config.setFilterJql1((String) settings.get(Config.class.getName() + ".filterJql1"));
                config.setFilterName2((String) settings.get(Config.class.getName() + ".filterName2"));
                config.setFilterJql2((String) settings.get(Config.class.getName() + ".filterJql2"));
                config.setFilterName3((String) settings.get(Config.class.getName() + ".filterName3"));
                config.setFilterJql3((String) settings.get(Config.class.getName() + ".filterJql3"));
                config.setFilterName4((String) settings.get(Config.class.getName() + ".filterName4"));
                config.setFilterJql4((String) settings.get(Config.class.getName() + ".filterJql4"));
                config.setFilterName5((String) settings.get(Config.class.getName() + ".filterName5"));
                config.setFilterJql5((String) settings.get(Config.class.getName() + ".filterJql5"));
                return config;
            }
        })).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response put(final Config config, @Context HttpServletRequest request)
    {
        String username = userManager.getRemoteUsername(request);
        if (username == null || !userManager.isSystemAdmin(username))
        {
            return Response.noContent().build();
        }

        transactionTemplate.execute(new TransactionCallback()
        {
            public Object doInTransaction()
            {
                PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
                pluginSettings.put(Config.class.getName() + ".project", config.getProject());
                pluginSettings.put(Config.class.getName()  +".epicIssueType", config.getEpicIssueType());
                pluginSettings.put(Config.class.getName()  +".storyIssueType", config.getStoryIssueType());
                pluginSettings.put(Config.class.getName()  +".storyPointsField", config.getStoryPointsField());
                pluginSettings.put(Config.class.getName()  +".epicField", config.getEpicField());
                pluginSettings.put(Config.class.getName()  +".doneStatus", config.getDoneStatus());
                pluginSettings.put(Config.class.getName()  +".filterName1", config.getFilterName1());
                pluginSettings.put(Config.class.getName()  +".filterJql1", config.getFilterJql1());
                pluginSettings.put(Config.class.getName()  +".filterName2", config.getFilterName2());
                pluginSettings.put(Config.class.getName()  +".filterJql2", config.getFilterJql2());
                pluginSettings.put(Config.class.getName()  +".filterName3", config.getFilterName3());
                pluginSettings.put(Config.class.getName()  +".filterJql3", config.getFilterJql3());
                pluginSettings.put(Config.class.getName()  +".filterName4", config.getFilterName4());
                pluginSettings.put(Config.class.getName()  +".filterJql4", config.getFilterJql4());
                pluginSettings.put(Config.class.getName()  +".filterName5", config.getFilterName5());
                pluginSettings.put(Config.class.getName()  +".filterJql5", config.getFilterJql5());
                return null;
            }
        });

        return Response.noContent().build();
    }

}