package com.aboutcoders.atlassian.helpers;

import com.atlassian.jira.charts.Chart;
import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.jira.charts.jfreechart.PieChartGenerator;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.util.I18nHelper;
import com.google.common.collect.Maps;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class ChartGenerator {

    public static Chart getChart( double totalSp, double burnedSp )
    {
        Chart chart;

        try
        {
            Map<String, Object> params = Maps.newHashMap();
            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue( "To do", totalSp - burnedSp );
            dataset.setValue( "Done", burnedSp );
            I18nHelper i18nBean = ComponentAccessor.getJiraAuthenticationContext().getI18nHelper();
            final ChartHelper helper = new PieChartGenerator(dataset, i18nBean).generateChart();

            PiePlot plot = (PiePlot) helper.getChart().getPlot();

            plot.setSectionPaint( "To do", new Color( 255, 85, 85 ) );
            plot.setSectionPaint( "Done", new Color( 0, 164, 128 ) );
            plot.setNoDataMessage("No data to display");
            helper.generate(400, 100);

            params.put("chart", helper.getLocation());
            params.put("chartDataset", dataset);
            params.put("imagemap", helper.getImageMap());
            params.put("imagemapName", helper.getImageMapName());
            chart = new Chart(
                    helper.getLocation(),
                    helper.getImageMap(),
                    helper.getImageMapName(),
                    params
            );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            throw new RuntimeException("Error generating chart", e);
        }

        return chart;
    }

}
