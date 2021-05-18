let search_form = $("#search_form");
let autocomplete_form = $("#title_search")
let myStorage = window.sessionStorage;

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
    let rowHTML = "";
    rowHTML += "<li>";
    rowHTML +=
        '<li> <a href="movie-list.html?startsWith=' + "*" + '">'
        + "*" +
        '</a> </li>';

    // Append the row created to the table body, which will refresh the page
    genreListElement.append(rowHTML);
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);

/*
 * This function is called by the library when it needs to lookup a query.
 *
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    // TODO: if you want to check past query results first, you can do it here
    if(myStorage.getItem(query) != null)
    {
        console.log("query in sessionStorage");
        handleLookupAjaxSuccess(myStorage.getItem(query), query, doneCallback);
        return;
    }
    console.log("sending AJAX request to backend Java Servlet")
    // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
    // with the query data
    jQuery.ajax({
        "method": "GET",
        // generate the request url from the query.
        // escape the query string to avoid errors caused by special characters
        "url": "title-suggestion?title=" + escape(query),
        "success": function(data) {
            // pass the data, query, and doneCallback function into the success handler
            handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 *
 * data is the JSON data string you get from your Java Servlet
 *
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    var jsonData = JSON.parse(data);
    console.log(jsonData)

    myStorage.setItem(query, data);

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion

    console.log("you select " + suggestion["value"]+ " with ID " + suggestion["data"]);
    window.location.replace("single-movie.html?id="+suggestion["data"]);
}

/*
 * This statement binds the autocomplete library with the input box element and
 *   sets necessary parameters of the library.
 *
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 *
 */
autocomplete_form.autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    deferRequestBy: 300,
    minChars: 3
});


/*
 * do normal full text search if no suggestion is selected
 */
function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
}

// bind pressing enter key to a handler function
autocomplete_form.keypress(function(event) {
    // keyCode 13 is the enter key
    if (event.keyCode == 13) {
        // pass the value of the input box to the handler function
        handleNormalSearch($('#autocomplete').val())
    }
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button


jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/main-page", // Setting request url, which is mapped by MovieListServlet
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});

