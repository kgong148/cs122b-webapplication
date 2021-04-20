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
function handleMovieResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries

    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +     // display movie_name for the link text
            '</a>' +
            "</th>";

        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";

        rowHTML += "<th>";
        rowHTML += resultData[i]["movie_genre_0"];
        if (resultData[i]["movie_genre_1"] != "") rowHTML += (", " + resultData[i]["movie_genre_1"]);
        if (resultData[i]["movie_genre_2"] != "") rowHTML += (", " + resultData[i]["movie_genre_2"]);
        rowHTML += "</th>";

        rowHTML += "<th>"
        rowHTML += ('<a href="single-star.html?id=' + resultData[i]['movie_stars_id_0'] + '">'
            + resultData[i]["movie_stars_0"] + '</a>');
        if(resultData[i]["movie_stars_1"] != "")
            rowHTML+= ", "+('<a href="single-star.html?id=' + resultData[i]['movie_stars_id_1'] + '">' + resultData[i]["movie_stars_1"] + '</a>');
        if(resultData[i]["movie_stars_2"] != "")
            rowHTML+= ", "+('<a href="single-star.html?id=' + resultData[i]['movie_stars_id_2'] + '">' + resultData[i]["movie_stars_2"] + '</a>');
        rowHTML+= "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

let searchTitle = getParameterByName('title');
let searchYear = getParameterByName('year');
let searchDirector = getParameterByName('director');
let searchStars = getParameterByName('stars');

let params = "";
if(searchTitle != "" && searchTitle != null) params += "title="+searchTitle;
if(searchYear != "" && searchYear != null)
{
    if(params.length != "") url += "&";
    params += "year="+searchYear;
}
if(searchDirector != "" && searchDirector != null)
{
    if(params.length != "") params += "&";
    params += "director="+searchDirector;
}
if(searchStars != "" && searchStars != null)
{
    if(params.length != "") params += "&";
    params += "stars="+searchStars;
}

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list?" + params, // Setting request url
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
});