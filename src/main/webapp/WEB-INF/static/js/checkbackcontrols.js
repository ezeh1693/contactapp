/**
 * Created by tolet on 1/22/16.
 */
$('.checkStatus').change(function() {
    //alert("Check status has changed");

    var exitedControl = $(this).attr("data-exitP");
    var issuesControl = $(this).attr("data-issueP");

    //hide all visible controls
    $('.checkStatus ~ p').hide();
    //Reset all other select boxes to default
    $(".checkStatus ~ p").find("select option").prop("selected", false);

    //Reset and hide all select boxes in issues control to default
    $('.checkStatus ~ div').hide();
    $(".checkStatus ~ div").find("select option").prop("selected", false);

    var value = $(this).val();

    if (value === "NO_ISSUES") {

    }
    else if (value === "ISSUES") {
        $(issuesControl).show();
    }
    else if (value === "EXITED") {

        handleExited( exitedControl);//Swal plugin must be loaded on the page before this can work
        //$(exitedControl).show();
    }
    else if (value === "DETECT_DEAL") {
        $('#dealDetectModal').show();

    }

});


$('.exitType').click(function() {

    var closedControl = $(this).attr("data-closedType");

    //reset the form
    $(closedControl).hide();

    var value = $(this).val();
    if (value === "RETAINED") {

    }
    else if (value === "CLOSED") {
        $(closedControl).show();
    }

});




$('.issueType').click(function() {
    var execControl = $(this).attr("data-execType");
    var nonSolvableControl = $(this).attr("data-nonSolvable");

    //hide all visible controls
    $('.issueType ~ p').hide();
    //Reset all other select boxes to default
    $(".issueType ~ p").find("select option").prop("selected", false);

    var value = $(this).val();

    if (value === "UNSOLVABLE") {
        $(nonSolvableControl).show();
    }
    else if (value === "SOLVABLE") {
        $(execControl).show();
    }

});

$('.closedType').click(function() {
    var value = $(this).val();
    var exitReasonControl = $(this).attr("data-exitReason");

    if (value === "OUTSIDE") {
        $(exitReasonControl).show();
    }

});

$(".deal-detect-close").click(function(){
    $('#dealDetectModal').hide();
});


/*$('.execStatus').click(function() {
    var value = $(this).val();

    if (value === "NO_ISSUES") {

    }
    else if (value === "ISSUES") {
        $(issuesControl).show();
    }
    else if (value === "EXITED") {
        $(exitedControl).show();
    }
    else if (value === "DETECT_DEAL") {

    }

});*/


function handleExited(exitedControl){
    swal({
        title: "Are you sure?",
        text: "Make sure you enter the problem encountered, before exiting this Client!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, Exit Client!",
        closeOnConfirm: false
    }, function (isConfirm) {
        if (isConfirm) {
            $(exitedControl).show();
            $('#exitCheckBox').append("<input type='hidden' name='makeExit' value=' true ' />");
            swal("Action in Progress!", "The client will be exited when you submit the form.", "success");
        }
    });
}
