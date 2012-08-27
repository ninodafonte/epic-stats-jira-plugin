AJS.toInit(function() {
    var baseUrl = AJS.$("meta[name='application-base-url']").attr("content");

    function updateConfig() {
        AJS.$.ajax({
            url: baseUrl + "/rest/epicstats-admin/1.0/",
            type: "PUT",
            contentType: "application/json",
            data: '{ "epicIssueType": "' +  AJS.$("#epicIssueType").attr("value") + '"' +
                ', "storyIssueType": "' +  AJS.$("#storyIssueType").attr("value") + '"' +
                ', "storyPointsField": "' +  AJS.$("#storyPointsField").attr("value") + '"' +
                ', "epicField": "' +  AJS.$("#epicField").attr("value") + '"' +
                ', "doneStatus": "' +  AJS.$("#doneStatus").attr("value") + '"' +
                ', "filterJql1": "' +  encodeURIComponent(AJS.$("#filterJql1").attr("value")) + '"' +
                ', "filterJql2": "' +  encodeURIComponent(AJS.$("#filterJql2").attr("value")) + '"' +
                ', "filterJql3": "' +  encodeURIComponent(AJS.$("#filterJql3").attr("value")) + '"' +
                ', "filterJql4": "' +  encodeURIComponent(AJS.$("#filterJql4").attr("value")) + '"' +
                ', "filterJql5": "' +  encodeURIComponent(AJS.$("#filterJql5").attr("value")) + '"' +
                ', "filterName1": "' +  AJS.$("#filterName1").attr("value") + '"' +
                ', "filterName2": "' +  AJS.$("#filterName2").attr("value") + '"' +
                ', "filterName3": "' +  AJS.$("#filterName3").attr("value") + '"' +
                ', "filterName4": "' +  AJS.$("#filterName4").attr("value") + '"' +
                ', "filterName5": "' +  AJS.$("#filterName5").attr("value") + '"' +
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
