package es.testingserver.atlassian.epicstats;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.builder.JqlClauseBuilder;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EpicStats extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(EpicStats.class);
    private IssueService issueService;
    private ProjectService projectService;
    private SearchService searchService;
    private UserManager userManager;
    private TemplateRenderer templateRenderer;
    private com.atlassian.jira.user.util.UserManager jiraUserManager;
    private static final String LIST_BROWSER_TEMPLATE = "/templates/list.vm";

    public EpicStats(IssueService issueService, ProjectService projectService,
                     SearchService searchService, UserManager userManager,
                     com.atlassian.jira.user.util.UserManager jiraUserManager,
                     TemplateRenderer templateRenderer) {
        this.issueService = issueService;
        this.projectService = projectService;
        this.searchService = searchService;
        this.userManager = userManager;
        this.templateRenderer = templateRenderer;
        this.jiraUserManager = jiraUserManager;
    }


    private User getCurrentUser(HttpServletRequest req)
	{
        return jiraUserManager.getUser(userManager.getRemoteUsername(req));
    }

    private List<Issue> getEpics(HttpServletRequest req) {
        // User is required to carry out a search
        User user = getCurrentUser(req);

        // The search interface requires JQL clause... so let's build one
        JqlClauseBuilder jqlClauseBuilder = JqlQueryBuilder.newClauseBuilder();

        // JQL Clause:
        com.atlassian.query.Query query = jqlClauseBuilder.project("WEB").
                and().issueTypeIsStandard().
                and().issueType().in("Epic").
                buildQuery();

        // A page filter is used to provide pagination. Let's use an unlimited filter to
        // to bypass pagination.
        PagerFilter pagerFilter = PagerFilter.getUnlimitedFilter();
        com.atlassian.jira.issue.search.SearchResults searchResults = null;

        try
		{
            // Perform search results
            searchResults = searchService.search(user, query, pagerFilter);
        }
		catch (SearchException e)
		{
            e.printStackTrace();
        }

        return searchResults.getIssues();
    }

    private List<Epic> processEpics( List<Issue> issues )
    {
        List<Epic> processedIssues = new ArrayList<Epic>();
        for( Issue item : issues )
        {
            Epic temp = new Epic();
            temp.setKey(item.getKey());
            temp.setSummary(item.getSummary());
            processedIssues.add( temp );
        }

        return processedIssues;
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException
    {
        Map<String, Object> context = Maps.newHashMap();

        // Get Epics Info:
        List<Issue> issues = getEpics(req);
        List<Epic> processedEpics = this.processEpics( issues );

        // Set template context:
        context.put( "issues", processedEpics );
        resp.setContentType("text/html;charset=utf-8");

        // Pass in the list of issues as the context
        templateRenderer.render(
                LIST_BROWSER_TEMPLATE,
                context,
                resp.getWriter()
        );
    }
}