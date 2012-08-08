package es.testingserver.atlassian.epicstats;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.project.Project;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.google.common.collect.Maps;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EpicStatsAdmin extends HttpServlet
{
    private final UserManager userManager;
    private final TemplateRenderer renderer;
    private final LoginUriProvider loginUriProvider;
    private static final String ADMIN_TEMPLATE = "/templates/admin.vm";
    private final PluginSettingsFactory pluginSettingsFactory;

    public EpicStatsAdmin(
            UserManager userManager,
            LoginUriProvider loginUriProvider,
            TemplateRenderer renderer,
            PluginSettingsFactory pluginSettingsFactory
    )
    {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        String username = userManager.getRemoteUsername(request);
        if (username == null || !userManager.isSystemAdmin(username))
        {
            redirectToLogin(request, response);
            return;
        }

        // Get projects from system:
        List<Project> projects = ComponentAccessor.getProjectManager().getProjectObjects();

        // Get issue types:
        List<IssueType> issueTypes = new ArrayList<IssueType>(
            ComponentAccessor.getConstantsManager().getAllIssueTypeObjects()
        );

        // Get issue fields:
        List<CustomField> issueFields = ComponentAccessor.getCustomFieldManager().getCustomFieldObjects();

        // Get issue status:
        List<Status> issueStatuses = new ArrayList<Status>(ComponentAccessor.getConstantsManager().getStatusObjects());

        // Get saved config:
        PluginSettings settings =
                this.pluginSettingsFactory.createGlobalSettings();
        String pluginNameSpace = ConfigResource.Config.class.getName();

        // Set vars for template:
        Map<String, Object> context = Maps.newHashMap();
        context.put( "projects", projects );
        context.put( "issueTypes", issueTypes );
        context.put( "issueFields", issueFields );
        context.put( "issueStatuses", issueStatuses );

        context.put( "selProject", settings.get(
                pluginNameSpace + ".project"
        ).toString());
        context.put( "selEpicIssueType", settings.get(
                pluginNameSpace + ".epicIssueType"
        ).toString());
        context.put( "selStoryIssueType", settings.get(
                pluginNameSpace + ".storyIssueType"
        ).toString());
        context.put( "selStoryPointsField", settings.get(
                pluginNameSpace + ".storyPointsField"
        ).toString());
        context.put( "selEpicField", settings.get(
                pluginNameSpace + ".epicField"
        ).toString());
        context.put( "selDoneStatus", settings.get(
                pluginNameSpace + ".doneStatus"
        ).toString());
        context.put( "selRoadmapLabel", settings.get(
                pluginNameSpace + ".roadmapLabel"
        ).toString());

        response.setContentType("text/html;charset=utf-8");
        renderer.render( ADMIN_TEMPLATE, context, response.getWriter());
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request)
    {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null)
        {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }
}