# Project 4 Android Example

This is an example Android project to demonstrate how to:

- Change between Android Activities
- Make RESTFul HTTPS requests
- Maintain cookies
- Use ListView with custom row layout
- HTTP is supported by checking /app/src/main/AndroidManifest.xml

## Running this project

This Android app depends on [3.15.192.28:8443/cs122b-spring21-project1/login.html] as the backend server to work.
 

You can change the url on Intellij IDEA -> Edit Run/Debug Configurations for Tomcat -> Deployment.

## About NukeSSLCerts.java

When you access a website through https, the server will send a SSL certificate to the browser for it to verify. We are using self-signed certificates, which will not be trusted by Android applications by default. The application will throw exceptions once it receives such certificate. NukeSSLCerts.java allows us to bypass SSL by accepting all certificates. If you want to learn more about this, see [this blog](https://www.codeproject.com/Articles/826045/Android-security-Implementation-of-Self-signed-SSL)
