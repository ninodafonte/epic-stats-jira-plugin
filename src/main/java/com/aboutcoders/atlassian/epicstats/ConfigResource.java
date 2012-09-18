package com.aboutcoders.atlassian.epicstats;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;
import com.aboutcoders.atlassian.entities.EpicStatsConfig;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
class ConfigResource
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
                EpicStatsConfig epicStatsConfig = new EpicStatsConfig();
                epicStatsConfig.setEpicIssueType((String) settings.get(EpicStatsConfig.class.getName() + ".epicIssueType"));
                epicStatsConfig.setStoryIssueType((String) settings.get(EpicStatsConfig.class.getName() + ".storyIssueType"));
                epicStatsConfig.setStoryPointsField((String) settings.get(EpicStatsConfig.class.getName() + ".storyPointsField"));
                epicStatsConfig.setEpicField((String) settings.get(EpicStatsConfig.class.getName() + ".epicField"));
                epicStatsConfig.setDoneStatus((String) settings.get(EpicStatsConfig.class.getName() + ".doneStatus"));
                epicStatsConfig.setFilterName1((String) settings.get(EpicStatsConfig.class.getName() + ".filterName1"));
                epicStatsConfig.setFilterJql1((String) settings.get(EpicStatsConfig.class.getName() + ".filterJql1"));
                epicStatsConfig.setFilterName2((String) settings.get(EpicStatsConfig.class.getName() + ".filterName2"));
                epicStatsConfig.setFilterJql2((String) settings.get(EpicStatsConfig.class.getName() + ".filterJql2"));
                epicStatsConfig.setFilterName3((String) settings.get(EpicStatsConfig.class.getName() + ".filterName3"));
                epicStatsConfig.setFilterJql3((String) settings.get(EpicStatsConfig.class.getName() + ".filterJql3"));
                epicStatsConfig.setFilterName4((String) settings.get(EpicStatsConfig.class.getName() + ".filterName4"));
                epicStatsConfig.setFilterJql4((String) settings.get(EpicStatsConfig.class.getName() + ".filterJql4"));
                epicStatsConfig.setFilterName5((String) settings.get(EpicStatsConfig.class.getName() + ".filterName5"));
                epicStatsConfig.setFilterJql5((String) settings.get(EpicStatsConfig.class.getName() + ".filterJql5"));
                return epicStatsConfig;
            }
        })).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response put(final EpicStatsConfig epicStatsConfig, @Context HttpServletRequest request)
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
                pluginSettings.put(EpicStatsConfig.class.getName()  +".epicIssueType", epicStatsConfig.getEpicIssueType());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".storyIssueType", epicStatsConfig.getStoryIssueType());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".storyPointsField", epicStatsConfig.getStoryPointsField());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".epicField", epicStatsConfig.getEpicField());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".doneStatus", epicStatsConfig.getDoneStatus());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterName1", epicStatsConfig.getFilterName1());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterJql1", epicStatsConfig.getFilterJql1());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterName2", epicStatsConfig.getFilterName2());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterJql2", epicStatsConfig.getFilterJql2());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterName3", epicStatsConfig.getFilterName3());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterJql3", epicStatsConfig.getFilterJql3());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterName4", epicStatsConfig.getFilterName4());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterJql4", epicStatsConfig.getFilterJql4());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterName5", epicStatsConfig.getFilterName5());
                pluginSettings.put(EpicStatsConfig.class.getName()  +".filterJql5", epicStatsConfig.getFilterJql5());
                return null;
            }
        });

        return Response.noContent().build();
    }

}