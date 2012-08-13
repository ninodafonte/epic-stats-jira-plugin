package es.testingserver.atlassian.epicstats;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.label.Label;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.builder.JqlClauseBuilder;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.google.common.collect.Maps;
import es.testingserver.atlassian.entities.Epic;
import es.testingserver.atlassian.entities.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EpicStats extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(EpicStats.class);
    private SearchService searchService;
    private UserManager userManager;
    private TemplateRenderer templateRenderer;
    private com.atlassian.jira.user.util.UserManager jiraUserManager;
    private JqlClauseBuilder jqlClauseBuilder = null;
    private CustomField epicField = null;
    private CustomField storyPoints = null;
    private double globalTotalStoryPoints = 0;
    private double globalBurnedStoryPoints = 0;
    private String filterJql = null;
    private String project = null;
    private String epicIssueType = null;
    private String storyIssueType = null;
    private String doneStatus = null;
    private String filtered = null;
    private static final String LIST_BROWSER_TEMPLATE = "/templates/list.vm";
    private final PluginSettingsFactory pluginSettingsFactory;

    public EpicStats (
            SearchService searchService,
            UserManager userManager,
            com.atlassian.jira.user.util.UserManager jiraUserManager,
            TemplateRenderer templateRenderer,
            PluginSettingsFactory pluginSettingsFactory
    )
    {
        this.searchService = searchService;
        this.userManager = userManager;
        this.templateRenderer = templateRenderer;
        this.jiraUserManager = jiraUserManager;
        this.jqlClauseBuilder = null;
        this.pluginSettingsFactory = pluginSettingsFactory;
    }


    private User getCurrentUser(HttpServletRequest req)
	{
        return jiraUserManager.getUser(userManager.getRemoteUsername(req));
    }

    private List<Issue> getEpics(HttpServletRequest req) {
        // User is required to carry out a search
        User user = getCurrentUser(req);

        // The search interface requires JQL clause... so let's build one
        jqlClauseBuilder = JqlQueryBuilder.newClauseBuilder();

        com.atlassian.query.Query query;
        jqlClauseBuilder = jqlClauseBuilder.project( this.project ).
                and().issueTypeIsStandard().
                and().issueType().in( this.epicIssueType );

        if ( ( this.filtered != null ) && ( this.filterJql.length() > 0 ) )
        {
            // JQL Filter Clause:
            String jqlQuery = this.filterJql;
            SearchService.ParseResult parseResult = searchService.parseQuery(
                    user,
                    jqlQuery
            );

            if (parseResult.isValid()){
                Query extraQuery = parseResult.getQuery();
                jqlClauseBuilder = jqlClauseBuilder.and().addClause(
                        extraQuery.getWhereClause()
                );
            }
        }

        query = jqlClauseBuilder.buildQuery();

        // A page filter is used to provide pagination.
        // Let's use an unlimited filter to bypass pagination.
        PagerFilter pagerFilter = PagerFilter.getUnlimitedFilter();
        com.atlassian.jira.issue.search.SearchResults searchResults;

        List<Issue> result = null;

        try
		{
            // Perform search results
            searchResults = searchService.search(user, query, pagerFilter);
            result = searchResults.getIssues();
        }
		catch (SearchException e)
		{
            e.printStackTrace();
        }

        return result;
    }


    @SuppressWarnings("unchecked")
    private List<Epic> processEpics( List<Issue> issues, HttpServletRequest req )
    {
        User user = getCurrentUser(req);
        List<Epic> processedIssues = new ArrayList<Epic>();
        this.globalTotalStoryPoints = 0;
        this.globalBurnedStoryPoints = 0;
        for( Issue item : issues )
        {
            Object epicValue = item.getCustomFieldValue( epicField );
            if ( epicValue != null )
            {
                List<Label> epics = new ArrayList<Label>(
                        (Collection<Label>) epicValue
                );

                String epicLabel = epics.get(0).getLabel();

                // JQL Clause:
                jqlClauseBuilder = JqlQueryBuilder.newClauseBuilder();


                // JQL Clause:
                com.atlassian.query.Query query = jqlClauseBuilder.project( this.project ).
                        and().issueTypeIsStandard().
                        and().issueType().in(this.storyIssueType).
                        and().customField(epicField.getIdAsLong()).eq(epicLabel).
                        buildQuery();

                PagerFilter pagerFilter = PagerFilter.getUnlimitedFilter();
                com.atlassian.jira.issue.search.SearchResults searchResults;

                List<Issue> result = new ArrayList<Issue>();

                try
                {
                    // Perform search results
                    searchResults = searchService.search(user, query, pagerFilter);
                    result = searchResults.getIssues();
                }
                catch (SearchException e)
                {
                    e.printStackTrace();
                }

                double totalStoryPoints = 0;
                double burnedStoryPoints = 0;

                List<Issue> stories = result;
                for ( Issue story : stories )
                {
                    Double spValue = (Double) story.getCustomFieldValue( storyPoints );
                    if ( spValue != null )
                    {
                        totalStoryPoints += spValue;
                        if ( story.getStatusObject().getName().equals( this.doneStatus ) )
                        {
                            burnedStoryPoints += spValue;
                        }
                    }
                }

                Epic temp = new Epic();
                temp.setKey(item.getKey());
                temp.setSummary(item.getSummary());
                temp.setTotalStoryPoints(totalStoryPoints);
                temp.setBurnedStoryPoints(burnedStoryPoints);
                if ( totalStoryPoints > 0 )
                {
                    temp.generateChart( totalStoryPoints, burnedStoryPoints );
                }
                processedIssues.add( temp );

                // Sum all the epics:
                globalTotalStoryPoints += totalStoryPoints;
                globalBurnedStoryPoints += burnedStoryPoints;
            }
        }

        return processedIssues;
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException
    {
        // Configuration read from Admin Plugin Section in Jira:
        this.readPluginConfiguration( req );

        // Get Epics Info:
        List<Issue> issues = getEpics(req);
        List<Epic> processedEpics = this.processEpics( issues, req );

        // Set template context:
        Map<String, Object> context = Maps.newHashMap();

        context.put( "cfEpic", epicField.getIdAsLong() );
        context.put( "issues", processedEpics );
        context.put( "filtered", this.filtered );
        context.put( "filterJql", this.filterJql );
        context.put( "totalStoryPoints", globalTotalStoryPoints );
        context.put( "burnedStoryPoints", globalBurnedStoryPoints );
        context.put( "filters", loadFilters() );
        resp.setContentType("text/html;charset=utf-8");


        // Pass in the list of issues as the context
        templateRenderer.render(
                LIST_BROWSER_TEMPLATE,
                context,
                resp.getWriter()
        );
    }

    private List<Filter> loadFilters() throws IOException
    {
        List<Filter> filters = new ArrayList<Filter>();
        String name, jql;

        for ( int i = 1; i < 6; i++ )
        {
            name = loadSetting(".filterName".concat(Integer.toString(i)));
            jql = loadSetting(".filterJql".concat(Integer.toString(i)));
            if ( (name != null) && (jql != null) && !name.isEmpty() && !jql.isEmpty() )
            {
                Filter temp = new Filter();
                temp.setName( name );
                temp.setJql(URLEncoder.encode(jql, "UTF-8"));
                filters.add( temp );
            }
        }

        return filters;
    }

    private void readPluginConfiguration( HttpServletRequest req )
    {
        PluginSettings settings =
                this.pluginSettingsFactory.createGlobalSettings();

        CustomFieldManager customFieldManager =
                ComponentAccessor.getCustomFieldManager();

        String pluginNameSpace = ConfigResource.Config.class.getName();
        this.filtered = req.getParameter("filtered");

        this.project = settings.get(
                pluginNameSpace + ".project"
        ).toString();

        this.epicIssueType = settings.get(
            pluginNameSpace + ".epicIssueType"
        ).toString();

        this.storyIssueType = settings.get(
            pluginNameSpace + ".storyIssueType"
        ).toString();

        String storyPointsField = settings.get(
            pluginNameSpace + ".storyPointsField"
        ).toString();

        String epicRelatedField = settings.get(
            pluginNameSpace + ".epicField"
        ).toString();

        this.doneStatus = settings.get(
            pluginNameSpace + ".doneStatus"
        ).toString();

        this.filterJql = req.getParameter( "filterJql" );

        this.epicField = customFieldManager.getCustomFieldObjectByName(
            epicRelatedField
        );

        this.storyPoints = customFieldManager.getCustomFieldObjectByName(
            storyPointsField
        );
    }

    // TODO: Refactor - Duplicated in EpicStatsAdmin:
    private String loadSetting( String name )
    {
        // Get saved config:
        PluginSettings settings =
                this.pluginSettingsFactory.createGlobalSettings();
        String pluginNameSpace = ConfigResource.Config.class.getName();

        String content = null;
        try
        {
            content = settings.get(
                    pluginNameSpace + name
            ).toString();
        }
        catch ( NullPointerException e )
        {
            settings.put( pluginNameSpace + name, "" );
        }

        return content;
    }
}