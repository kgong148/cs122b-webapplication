let payment_form = $("#payment_form");


function handleResult(resultData) {

    console.log("handleResult: get payment info url");

    // populate the star info 1
    // find the empty h3 body by id "movie_info"
    //let returnListElement = jQuery("#return_list");
    //returnListElement.append("<h1>Shopping Cart" +
    //    '<a href='+resultData[0]["returnListUrl"]+' style=float:right;>' + "Return to MovieList" + '</a></h1>');
}


/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to main-page.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("main-page.html");
    } else {
        // If login fails, the web page will display 
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/payment", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: payment_form.serialize(),
            success: handleLoginResult
        }
    );

    jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
        url: "api/payment?", // Setting request url, which is mapped by MovieListServlet
        success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
    });
}

// Bind the submit action of the form to a handler function
payment_form.submit(submitLoginForm);

