let i_star_form = $("#star_insert_form")

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
            method: "GET",
            // Serialize the login form to the data sent by POST request
            data: i_star_form.serialize(),
            success: handleStarResult
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
        console.log(resultDataJson["message"]);
        $("#insert_star_message").text(resultDataJson["message"]);
    }
}

// Bind the submit action of the form to a handler function
i_star_form.submit(submitStarInsertForm);


