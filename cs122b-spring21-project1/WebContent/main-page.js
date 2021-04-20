let search_form = $("#search_form");

/**
 * Handle the data returned by MainPageServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle search response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    let url = "movie-list.html?";

    let title = resultDataJson["title"];
    let year = resultDataJson["year"];
    let director = resultDataJson["director"];
    let stars = resultDataJson["stars"];

    if(title != "") url += "title="+title;
    if(year != "")
    {
        if(url.length > 15) url += "&";
        url += "year="+year;
    }
    if(director != "")
    {
        if(url.length > 15) url += "&";
        url += "director="+director;
    }
    if(stars != "")
    {
        if(url.length > 15) url += "&";
        url += "stars="+stars;
    }

    window.location.replace(url);
    // If search succeeds, it will redirect the user to main-page.html
    // if (resultDataJson["status"] === "success") {
    //     window.location.replace("main-page.html");
    // } else {
    //     // If login fails, the web page will display
    //     // error messages on <div> with id "login_error_message"
    //     console.log("show error message");
    //     console.log(resultDataJson["message"]);
    //     $("#login_error_message").text(resultDataJson["message"]);
    // }
}

function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/main-page", {
            method: "GET",
            // Serialize the login form to the data sent by POST request
            data: search_form.serialize(),
            success: handleSearchResult
        }
    );
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);