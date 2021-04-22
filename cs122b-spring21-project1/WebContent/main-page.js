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

    let url = "";

    let title = resultDataJson["title"];
    let year = resultDataJson["year"];
    let director = resultDataJson["director"];
    let stars = resultDataJson["stars"];

    if(title != "") url += "title="+title;
    if(year != "")
    {
        if(url.length > 0) url += "&";
        url += "year="+year;
    }
    if(director != "")
    {
        if(url.length > 0) url += "&";
        url += "director="+director;
    }
    if(stars != "")
    {
        if(url.length > 0) url += "&";
        url += "stars="+stars;
    }
    if(url.length > 0) url = "movie-list.html?" + url;
    else url = "movie-list.html";

    window.location.replace(url);
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

function handleResult(resultData)
{
    console.log("handleResult: populating genre_list from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let genreListElement = jQuery("#genre_list");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < resultData["size"]; i++) {
        let rowHTML = "";
        rowHTML += "<li>";
        rowHTML +=
            '<li> <a href="movie-list.html?genre=' + resultData['genre_'+i] + '">'
            + resultData["genre_"+i] +
            '</a> </li>';

        // Append the row created to the table body, which will refresh the page
        genreListElement.append(rowHTML);
    }

    let titleListElement = jQuery("#title_list");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < 26; i++) {
        let rowHTML = "";
        rowHTML += "<li>";
        rowHTML +=
            '<li> <a href="movie-list.html?startsWith=' + String.fromCharCode(65+i) + '">'
            + String.fromCharCode(65+i) +
            '</a> </li>';

        // Append the row created to the table body, which will refresh the page
        genreListElement.append(rowHTML);
    }
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);

jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/main-page", // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});