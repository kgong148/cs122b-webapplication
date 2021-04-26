/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */

let amount_form = $("#amount_form");
let order_form = $("#order_form");

let lastPage = false;

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
function handleMovieResult(resultData)
{
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData

    for (let i = 0; i < resultData.length-1; i++) {

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
        rowHTML += '<a href="movie-list.html?genre=' + resultData[i]['movie_genre_0'] + '">' + resultData[i]['movie_genre_0'] +'</a>';
        if (resultData[i]["movie_genre_1"] != "") rowHTML += '<a href="movie-list.html?genre=' + resultData[i]['movie_genre_1'] + '">' + (", " + resultData[i]["movie_genre_1"]) +'</a>';
        if (resultData[i]["movie_genre_2"] != "") rowHTML += '<a href="movie-list.html?genre=' + resultData[i]['movie_genre_2'] + '">' + (", " + resultData[i]["movie_genre_2"]) +'</a>';
        rowHTML += "</th>";

        rowHTML += "<th>"
        rowHTML += ('<a href="single-star.html?id=' + resultData[i]['movie_stars_id_0'] + '">'
            + resultData[i]["movie_stars_0"] + '</a>');
        if(resultData[i]["movie_stars_1"] != "")
            rowHTML+= ", "+('<a href="single-star.html?id=' + resultData[i]['movie_stars_id_1'] + '">' + resultData[i]["movie_stars_1"] + '</a>');
        if(resultData[i]["movie_stars_2"] != "")
            rowHTML+= ", "+('<a href="single-star.html?id=' + resultData[i]['movie_stars_id_2'] + '">' + resultData[i]["movie_stars_2"] + '</a>');
        rowHTML+= "</th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "<th>";
        rowHTML += '<input type="button" value="+" onclick="handleAddButton(\''+resultData[i]["movie_id"]+'\')">';
        rowHTML += "<th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
    lastPage = resultData[resultData.length-1]["EndOfQuery"];
}

function handleAddButton(movieId)
{
    $.ajax(
        "api/movie-list", {
            method: "POST",
            data: "movieId="+encodeURIComponent(movieId),
            success: handleAddResult("Success!")
        }
    );
}

function handleAddResult(text)
{
    $("#add_message").text(text);
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

let searchTitle = getParameterByName('title');
let searchYear = getParameterByName('year');
let searchDirector = getParameterByName('director');
let searchStars = getParameterByName('stars');
let searchGenre = getParameterByName('genre');
let searchStart = getParameterByName('startsWith');
let searchAmount = getParameterByName('amount');
let searchOrder = getParameterByName('order');
let searchPage = getParameterByName('page');

let params = "";
if(searchGenre != null || searchStart != null)
{
    if(searchGenre != null)
    {
        params += "genre="+searchGenre;
    }
    else
    {
        params += "startsWith="+searchStart;
    }
}
else {
    if (searchTitle != "" && searchTitle != null) params += "title=" + searchTitle;
    if (searchYear != "" && searchYear != null) {
        if (params.length > 0) params += "&";
        params += "year=" + searchYear;
    }
    if (searchDirector != "" && searchDirector != null) {
        if (params.length > 0) params += "&";
        params += "director=" + searchDirector;
    }
    if (searchStars != "" && searchStars != null) {
        if (params.length > 0) params += "&";
        params += "stars=" + searchStars;
    }
}
let amountBool = false;
if (searchAmount != "" && searchAmount != null)
{
    amountBool = true;
    if (params.length > 0) params += "&";
    params += "amount=" + searchAmount;
}

let orderBool = false;
if (searchOrder != "" && searchOrder != null)
{
    orderBool = true;
    if (params.length > 0) params += "&";
    params += "order=" + searchOrder;
}

pageBool = false;
if (searchPage != "" && searchPage != null)
{
    pageBool = true;
    if (params.length > 0) params += "&";
    params += "page=" + searchPage;
}
else {searchPage = "1";}

if(params.length > 0)
    params = "?"+params;


function submitAmountForm(formSubmitEvent)
{
    console.log("submit amount form");

    formSubmitEvent.preventDefault();

    let e = document.getElementById("amount_list");
    let amount = e.options[e.selectedIndex].text;

    console.log("Amount selected = " + amount);

    let temp = params
    if (!amountBool) {
        temp = params + (params.length > 0 ? ("&amount=" + amount) : ("?amount=" + amount));
    } else {
        temp = temp.replace(/amount=[0-9]*/, "amount=" + amount);
    }

    if(pageBool) temp = temp.replace(/page=[0-9]*/, "page=" + 1);

    window.location.replace("movie-list.html"+temp);
}

function submitOrderForm(formSubmitEvent)
{
    console.log("submit order form");

    formSubmitEvent.preventDefault();

    let e = document.getElementById("order_list");
    let order = e.options[e.selectedIndex].text;

    if(order == "rating, title") order = "rating";
    else order = "title";

    console.log("Order selected = " + order);

    let temp = params
    if (!orderBool) {
        temp = params + (params.length > 0 ? ("&order=" + order) : ("?order=" + order));
    } else {
        temp = temp.replace(/order=[a-z]*/, "order=" + order);
    }

    if(pageBool) temp = temp.replace(/page=[0-9]*/, "page=" + 1);

    window.location.replace("movie-list.html"+temp);
}

function handlePrevButton ()
{
    console.log("prev button pressed");

    let page = parseInt(searchPage);

    if(page <= 1) return;
    page -= 1;

    let temp = params
    if (!pageBool) {
        temp = params + (params.length > 0 ? ("&page=" + page) : ("?page=" + page));
    } else {
        temp = temp.replace(/page=[0-9]*/, "page=" + page);
    }

    window.location.replace("movie-list.html"+temp);
}

function handleNextButton ()
{
    console.log("next button pressed");

    let page = parseInt(searchPage);

    if(lastPage) return;
    page += 1;

    let temp = params
    if (!pageBool) {
        temp = params + (params.length > 0 ? ("&page=" + page) : ("?page=" + page));
    } else {
        temp = temp.replace(/page=[0-9]*/, "page=" + page);
    }

    window.location.replace("movie-list.html"+temp);
}

amount_form.submit(submitAmountForm);
order_form.submit(submitOrderForm);

// Makes the HTTP GET request and registers on success callback function handleMovieResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movie-list" + params, // Setting request url
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the MovieListServlet
});

