let cart = $("#cart");

/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */


function handleSessionData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle session response");
    console.log(resultDataJson);
    console.log(resultDataJson["sessionID"]);
    // Return to movie list
    let returnListElement = jQuery("#return_list");
    returnListElement.append("<p>Shopping Cart</p>" )//+
        //'<a href='+(resultDataString[0]["return_url"])+' style=float:right;>' + "Return to MovieList" + '</a></p>');

    let paymentPageElement = jQuery("#payment_page");
    paymentPageElement.append(
        '<a href='+resultDataString["payment_url"]+' >' + "Payment Info" + '</a></p>');

    System.out.println(resultDataJson[0]["return_url"])
    //$("#sessionID").text("Session ID: " +
    //    '<a href='+resultDataString[0]["returnListUrl"]+' style=float:right;>' + "Return to MovieList" + '</a></h1>');
    //$("#lastAccessTime").text("Last access time: " + resultDataJson["lastAccessTime"]);

    // show cart information
    handleCartArray(resultDataJson["previousItems"]);
}

/**
 * Handle the items in item list
 * @param resultArray jsonObject, needs to be parsed to html
 */
function handleCartArray(resultArray) {
    console.log(resultArray);
    let item_list = $("#item_list");
    // change it to html list
    let res = "<ul>";
    for (let i = 0; i < resultArray.length; i++) {
        // each item will be in a bullet point
        res += "<li>" + resultArray[i] + "</li>";
    }
    res += "</ul>";

    // clear the old array and show the new array in the frontend
    item_list.html("");
    item_list.append(res);
}

/**
 * Submit form content with POST method
 * @param cartEvent
 */
function handleCartInfo(cartEvent) {
    console.log("submit cart form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    cartEvent.preventDefault();

    $.ajax("api/index", {
        method: "POST",
        data: cart.serialize(),
        success: resultDataString => {
            let resultDataJson = JSON.parse(resultDataString);
            handleCartArray(resultDataJson["previousItems"]);
        }
    });

    // clear input form
    cart[0].reset();
}

$.ajax("api/index", {
    method: "GET",
    success: handleSessionData
});
let movieId = getParameterByName('id');


// Bind the submit action of the form to a event handler function
cart.submit(handleCartInfo);
