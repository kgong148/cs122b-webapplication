function handleSessionData() {}


$.ajax("api/main-page", {
    method: "GET",
    success: handleSessionData
});