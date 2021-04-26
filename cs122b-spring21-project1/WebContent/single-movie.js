/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {
    let checkoutButtonElement = jQuery("#checkout-button");
    checkoutButtonElement.append('<input type="button" value="+" onclick="handleSingleAddButton(\''+resultData[0]["movie_id"]+'\')">');

    console.log("handleResult: populating star info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "movie_info"
    let movieInfoElement = jQuery("#movie_info");

    let genre_str = "<a href = movie-list.html?genre="+resultData[0]["movie_genres_0"] +"> " +resultData[0]["movie_genres_0"] +" </a>";
    for(let i = 1; i < resultData[0]["genre_size"]; i++)
    {
        genre_str += ", <a href = movie-list.html?genre="+resultData[0]["movie_genres_"+i] +"> " +resultData[0]["movie_genres_"+i] +" </a>";
    }

    // append two html <p> created to the h3 body, which will refresh the page
    movieInfoElement.append("<p>Movie title: " + resultData[0]["movie_title"] +
        "<p>Year: " + resultData[0]["movie_year"] + "</p>" +
        "<p>Director: " + resultData[0]["movie_director"] + "</p>"+
        "<p>Genres: " + (resultData[0]["genre_size"] > 0 ? genre_str  : "N/A") + "</p>"+
        "<p>Rating: " + resultData[0]["movie_rating"] + "</p>"+
        "<p>Price: $" + resultData[0]["movie_price"] + "</p>"+
        '<a href='+resultData[0]["return_url"]+'>Return to MovieList</a>');
    console.log("handleResult: populating movie table from resultData");



    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#star_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < resultData.length; i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>"
            +'<a href="single-star.html?id=' + resultData[i]['star_id'] + '">'
            + resultData[i]["star_name"] +
            '</a>' +
            "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }

}

function handleSingleAddButton(movieId)
{
    $.ajax(
        "api/movie-list", {
            method: "POST",
            data: "movieId="+encodeURIComponent(movieId),
            success: handleSingleAddResult("Success!")
        }
    );
}

function handleSingleAddResult(text)
{
    $("#single_add_message").text(text);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let movieId = getParameterByName('id');
let indexId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
        dataType: "json",  // Setting return data type
        method: "GET",// Setting request method
    url: "api/single-movie?id=" + movieId, // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});

jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/index?", // Setting request url, which is mapped by indexServlet
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});