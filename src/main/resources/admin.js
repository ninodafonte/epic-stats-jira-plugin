AJS.toInit(function() {
    var baseUrl = AJS.$("meta[name='application-base-url']").attr("content");

    function populateForm() {
        AJS.$.ajax({
            url: baseUrl + "/rest/epicstats-admin/1.0/",
            dataType: "json",
            success: function(config) {
                AJS.$("#project").attr("value", config.project);
                AJS.$("#epicIssueType").attr("value", config.epicIssueType);
                AJS.$("#storyIssueType").attr("value", config.storyIssueType);
                AJS.$("#storyPointsField").attr("value", config.storyPointsField);
                AJS.$("#epicField").attr("value", config.epicField);
                AJS.$("#doneStatus").attr("value", config.doneStatus);
                AJS.$("#roadmapLabel").attr("value", config.roadmapLabel);
            }
        });
    }

    function updateConfig() {
        AJS.$.ajax({
            url: baseUrl + "/rest/epicstats-admin/1.0/",
            type: "PUT",
            contentType: "application/json",
            data: '{ "project": "' + AJS.$("#project").attr("value") + '"' +
                ', "epicIssueType": "' +  AJS.$("#epicIssueType").attr("value") + '"' +
                ', "storyIssueType": "' +  AJS.$("#storyIssueType").attr("value") + '"' +
                ', "storyPointsField": "' +  AJS.$("#storyPointsField").attr("value") + '"' +
                ', "epicField": "' +  AJS.$("#epicField").attr("value") + '"' +
                ', "doneStatus": "' +  AJS.$("#doneStatus").attr("value") + '"' +
                ', "roadmapLabel": "' +  AJS.$("#roadmapLabel").attr("value") + '"' +
                ' }',
            processData: false
        });
    }

    populateForm();

    AJS.$("#admin").submit(function(e) {
        e.preventDefault();
        updateConfig();
    });

});
