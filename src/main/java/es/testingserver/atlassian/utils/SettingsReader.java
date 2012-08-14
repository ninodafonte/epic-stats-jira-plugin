package es.testingserver.atlassian.utils;


import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import es.testingserver.atlassian.entities.EpicStatsConfig;

public class SettingsReader {

    protected PluginSettingsFactory pluginSettings;

    public SettingsReader( PluginSettingsFactory pluginSettings )
    {
        this.pluginSettings = pluginSettings;
    }

    public String loadSetting( String name )
    {
        // Get saved config:
        PluginSettings settings =
                this.pluginSettings.createGlobalSettings();
        String pluginNameSpace = EpicStatsConfig.class.getName();

        String content = "";
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
