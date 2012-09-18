package com.aboutcoders.atlassian.utils;


import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.aboutcoders.atlassian.entities.EpicStatsConfig;

public class SettingsReader {

    private final PluginSettingsFactory pluginSettings;

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
