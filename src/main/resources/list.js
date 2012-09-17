var ViewModel = new Object();

ViewModel.hideEpicsDone = function () {
    AJS.$(".done").hide();
};

AJS.$(document).ready(function() {
    AJS.$("#epic-filter-dropdown").dropDown("Standard");

    ViewModel.hideEpicsDone();

    AJS.$("#hideDoneEpics").click( function() {
        if ( this.checked )
        {
            AJS.$(".done").hide();
        }
        else
        {
            AJS.$(".done").show();
        }
    });
});