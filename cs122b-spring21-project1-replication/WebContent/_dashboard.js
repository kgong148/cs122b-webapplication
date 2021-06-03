let i_star_form = $("#star_insert_form")
let i_movie_form = $("#movie_insert_form")

function submitStarInsertForm(formSubmitEvent)
{
    console.log("submit star insert form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/_dashboard", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: i_star_form.serialize(),
            success: handleStarResult,
            error: handleStarResult
        }
    );
}

function handleStarResult(resultDataString)
{
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle insert star response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to main-page.html
    if (resultDataJson["status"] === "success") {
        $("#insert_star_message").text(resultDataJson["message"]);
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["errorMessage"]);
        $("#insert_star_message").text(resultDataJson["errorMessage"]);
    }
}

function handleMetadataResult(resultData)
{
    let dashboard_body = jQuery("#dashboard_body");
    let rowHTML = ""
    for(let i = 0; i < resultData.length; i++)
    {
        rowHTML += '<h2>'+resultData[i]["tablename"]+'</h2>';
        rowHTML += '<table class="table table-striped">';
        rowHTML += '<thead>';
        rowHTML += '<tr>';
        rowHTML += '<th>Attribute</th>';
        rowHTML += '<th>Type</th>'
        rowHTML += '</tr>';
        rowHTML += '</thead>';
        rowHTML += '<tbody>';
        for(let j = 0; j < resultData[i]["length"]; j++)
        {
            rowHTML += '<tr>';
            rowHTML += '<th>' + resultData[i]["table_attribute_name_"+j] + '</th>';
            rowHTML += '<th>' + resultData[i]["table_attribute_type_"+j] + '</th>';
            rowHTML += '</tr>';
        }
        rowHTML += '</tbody>';
        rowHTML += '</table>';
    }
    dashboard_body.append(rowHTML);
}

function submitMovieInsertForm(formSubmitEvent)
{
    console.log("submit movie insert form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/_dashboard", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: i_movie_form.serialize(),
            success: handleMovieResult
        }
    );
}

function handleMovieResult(resultDataString)
{
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle insert movie response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to main-page.html
    if (resultDataJson["status"] === "success") {
        $("#insert_movie_message").text(resultDataJson["message"]);
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#insert_movie_message").text(resultDataJson["message"]);
    }
}

// Bind the submit action of the form to a handler function
i_star_form.submit(submitStarInsertForm);
i_movie_form.submit(submitMovieInsertForm);

// Makes the HTTP GET request and registers on success callback function handleMetadataResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/_dashboard", // Setting request url
    success: (resultData) => handleMetadataResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
});


