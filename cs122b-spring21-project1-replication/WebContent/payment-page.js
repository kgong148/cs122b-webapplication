let card_info_form = $("#card_info_form");
let payment_body = $("#payment_body")

function handlePaymentResult(resultData)
{
    let totalPriceElement = jQuery("#total_price");

    totalPriceElement.append("<p>Total Price: " + resultData[0]["total_price"] + "</p>");
}

function handleCardResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle card response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If login succeeds, it will redirect the user to main-page.html
    if (resultDataJson["status"] === "success") {
        payment_body.html("");
        payment_body.append('<div>Success!</div>');
    } else {
        // If login fails, the web page will display
        // error messages on <div> with id "login_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#payment_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitCardForm(formSubmitEvent) {
    console.log("submit card form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/payment-page", {
            method: "POST",
            // Serialize the login form to the data sent by POST request
            data: card_info_form.serialize(),
            success: handleCardResult
        }
    );
}

// Bind the submit action of the form to a handler function
card_info_form.submit(submitCardForm);

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/payment-page", // Setting request url
    success: (resultData) => handlePaymentResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
});