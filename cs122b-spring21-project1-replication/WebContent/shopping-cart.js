/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleCartResult(resultData) {
    console.log("handleCartResult: populating cart table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#cart_table_body");

    // Iterate through resultData

    for (let i = 0; i < resultData.length; i++) {

        // Concatenate the html tags with resultData jsonObject

        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_qty"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_price"] + "</th>";

        rowHTML += "<th>";
        rowHTML += '<input type="button" value="+" onclick="handleCartAddButton(\''+resultData[i]["movie_id"]+'\')">';
        rowHTML += "</th>";

        rowHTML += "<th>";
        rowHTML += '<input type="button" value="-" onclick="handleCartSubtractButton(\''+resultData[i]["movie_id"]+'\')">';
        rowHTML += "</th>";

        rowHTML += "<th>";
        rowHTML += '<input type="button" value="X" onclick="handleCartRemoveButton(\''+resultData[i]["movie_id"]+'\')">';
        rowHTML += "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

function handleCartAddButton(movieId)
{
    $.ajax(
        "api/shopping-cart", {
            method: "POST",
            data: "movieId="+encodeURIComponent("+"+movieId),
            success: function() {
                $("#cart_table_body").html("");
                jQuery.ajax({
                    dataType: "json", // Setting return data type
                    method: "GET", // Setting request method
                    url: "api/shopping-cart", // Setting request url
                    success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
                });
            }
        }
    );
}

function handleCartSubtractButton(movieId)
{
    $.ajax(
        "api/shopping-cart", {
            method: "POST",
            data: "movieId="+encodeURIComponent("-"+movieId),
            success: function() {
                $("#cart_table_body").html("");
                jQuery.ajax({
                    dataType: "json", // Setting return data type
                    method: "GET", // Setting request method
                    url: "api/shopping-cart", // Setting request url
                    success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
                });
            }

        }
    );
}

function handleCartRemoveButton(movieId)
{
    $.ajax(
        "api/shopping-cart", {
            method: "POST",
            data: "movieId="+encodeURIComponent("x"+movieId),
            success: function() {
                $("#cart_table_body").html("");
                jQuery.ajax({
                    dataType: "json", // Setting return data type
                    method: "GET", // Setting request method
                    url: "api/shopping-cart", // Setting request url
                    success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
                });
            }
        }
    );
}

function handlePaymentButton()
{
    window.location.replace("payment-page.html");
}
// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/shopping-cart", // Setting request url
    success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
});

