AJS.toInit(function() {
    var baseUrl = AJS.$("meta[name='application-base-url']").attr("content");

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
            processData: false,
            success: function(data) {
                var message = AJS.messages.success(
                    '#aui-message-bar',
                    {
                        title:"Epic Dashboard Configuration",
                        body: "<p>Config saved!</p>"
                    }
                );

                setTimeout( function()
                {
                    AJS.$('#aui-message-bar').html('');
                }, 5000 );
            }
        });
    }

    AJS.$("#admin").submit(function(e) {
        e.preventDefault();
        updateConfig();
    });
});
