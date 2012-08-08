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
        private String roadmapLabel;

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

        public String getRoadmapLabel() {
            return roadmapLabel;
        }

        public void setRoadmapLabel(String roadmapLabel) {
            this.roadmapLabel = roadmapLabel;
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
                config.setRoadmapLabel((String) settings.get(Config.class.getName() + ".roadmapLabel"));
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
                pluginSettings.put(Config.class.getName()  +".roadmapLabel", config.getRoadmapLabel());
                return null;
            }
        });

        return Response.noContent().build();
    }

}