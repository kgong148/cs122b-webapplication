- # General
    - #### Team#: 87
    
    - #### Names: Kevin Cui Gong
    
    - #### Project 5 Video Demo Link:

    - #### Instruction of deployment: In order to deploy the scaled version of Fabflix we have to have to run multiple instances in order to distribute the query workload. In this case we deployed 3 instances on AWS and 1 instance on GCP. Two of the instances on AWS have a master/slave relationship for sql queries and the other instance on AWS acts as a load balancer using apache2. The 1 instance on GCP acts as a load balancer too, again using apache2. The way to access Fabflix using these load balancer is simple. Use the URL http://public-ip:80/cs122b-spring21-project1/login.html the public ip address is of either the AWS instance that acts as a load balancer or the GCP instance. 

    - #### Collaborations and Work Distribution:
    Kgong148: 
    - Enabled JDBC connection pooling
    - Set up Master/Slave AWS instances
    - Measure preformance
    - Create logging script
    - Demo project 5
 
    adiaz18011:
    - Completed readme file
    - Set up GCP environment

- # Connection Pooling
    - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
    
    - #### Explain how Connection Pooling is utilized in the Fabflix code.
    - Connection pooling is utilized in our Fabflix application by configuiring it when we defined our data source connection in contex.xml. Our configuration created a set of prepared connections that a query can use. The set contained the max amount of 100 connections but only allowed a max of 30 to be idle, otherwise resources would be reallocated. There is also a max wait time of 10 seconds before a connection is closed.
    
    - #### Explain how Connection Pooling works with two backend SQL.
    - With two backend servers connecting to the database, they each have a set of prepared connections that they use for the queries they request.  
    

- # Master/Slave
    - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

    - #### How read/write requests were routed to Master/Slave SQL?
    

- # JMeter TS/TJ Time Logs
    - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.
If there is only one log, use the process_log method.  Replace the file name with your file.  If there is two logs, use the process_logs method.  Replace the two file names with the two files.

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**          | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 107                         | 3.75724770                                  | 3.69252745                        | Should be faster than case 4           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 114                         | 4.44558967828723                                  | 4.411849183781736                        | Should be faster than case 1, since 10 threads are querying at the same time           |
| Case 3: HTTPS/10 threads                       | ![](path to image in img/)   | 120                         | 4.6549440852272725                                  | 4.634220925                        | Due to the added security it should take longer than case 2           |
| Case 4: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |

| **Scaled Version Test Plan**                   | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
|------------------------------------------------|------------------------------|----------------------------|-------------------------------------|---------------------------|--------------|
| Case 1: HTTP/1 thread                          | ![](path to image in img/)   | 113                         | 3.5230113647370413                                  | 3.445580302686341                        | Should be similar to the single-instance case 1 since it will only make use of one instance           |
| Case 2: HTTP/10 threads                        | ![](path to image in img/)   | 108                         | 3.0553226446969695                                  | 2.9973560643939395                        | Should be faster than case 1, since the workload of each instance is halved           |
| Case 3: HTTP/10 threads/No connection pooling  | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
 # cs122b-spring21-team-87 - Project 4
 
 ## Demo Video URL
 
https://drive.google.com/file/d/1uowX-pGwi73UcTIgFnpp494Uvfyne5xJ/view?usp=sharing

 ## How to deploy applicaion with with Tomcat
 In order to deploy the application on aws with Tomcat you need to have MySQL, Tomcat and maven installed on the aws instance and have it running. 
 Once that is done you will create a directory that will contain the clone of this project. In this case the comand will be git clone https://git
 hub.com/UCI-Chenli-teaching/cs122b-spring21-team-87.git. Afterwards, you will go to the directory that contains the pom.xml file and run the command mvn package. This will create a target file that contains the war file that needs to be deployed on Tomcat. In order to deploy this war file you need to copy it into the directory /var/lib/tomcat9/webapps/, this can be done with the command cp ./target/*.war /var/lib/tomcat9/webapps/. Once the war file is in this directory it will be deployed through Tomcat. You can view this through a webpage by going to the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/manager". The application can then be viewed with the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/cs122b-spring21-project1". You then need to run the files createtable.sql, createEmployeeTable.sql, stored-procedure.sql and addPriceCol.sql. The first file fist creates and populates all the schemas needed for this application the second file creates a table for employee information so they can access the dashboard, the third file processes the data added through the dashboard, and last file  adds a column to the movies page that has the initial price of each movie. You also need to encrypt the passwords of the customers table. The last thing you need to do is add the secret key in RecaptchaConstant.java. 

 ## How to deploy mobile application
 To deploy the mobile application, you need to open the the project in android studio and download an emulator. The emulator we use is the the Pixle 3a API 30. Once you have an emulator you can run the program and the application will automatically open on the emulator. It will first take you to the login page. After you login, you can search for a movie and a list of movies will be provided, depending on your search.

## Member Contribution

Kgong148: 
 - Implemented full-text search and auto complete
 - Contribute to android application
 
adiaz18011:
- Completed demo and readme file
- Contribute to android application
 
 # cs122b-spring21-team-87 - Project 3
 
 ## Demo Video URL
 
https://drive.google.com/file/d/1hmab_42zfDl186Nd4ZkZIPTcR-N_panc/view?usp=sharing

 ## How to deploy applicaion with with Tomcat
 In order to deploy the application on aws with Tomcat you need to have MySQL, Tomcat and maven installed on the aws instance and have it running. 
 Once that is done you will create a directory that will contain the clone of this project. In this case the comand will be git clone https://git
 hub.com/UCI-Chenli-teaching/cs122b-spring21-team-87.git. Afterwards, you will go to the directory that contains the pom.xml file and run the command mvn package. This will create a target file that contains the war file that needs to be deployed on Tomcat. In order to deploy this war file you need to copy it into the directory /var/lib/tomcat9/webapps/, this can be done with the command cp ./target/*.war /var/lib/tomcat9/webapps/. Once the war file is in this directory it will be deployed through Tomcat. You can view this through a webpage by going to the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/manager". The application can then be viewed with the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/cs122b-spring21-project1". You then need to run the files createtable.sql, createEmployeeTable.sql, stored-procedure.sql and addPriceCol.sql. The first file fist creates and populates all the schemas needed for this application the second file creates a table for employee information so they can access the dashboard, the third file processes the data added through the dashboard, and last file  adds a column to the movies page that has the initial price of each movie. You also need to encrypt the passwords of the customers table. The last thing you need to do is add the secret key in RecaptchaConstant.java.

 ## Queries with Prepared Statements
  - DashboardLoginServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-87/blob/3dc2f8ff42b3b79bfb6e12a46af23d90b65340ad/cs122b-spring21-project1/src/DashboardLoginServlet.java
  - DashboardServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-87/blob/3dc2f8ff42b3b79bfb6e12a46af23d90b65340ad/cs122b-spring21-project1/src/DashboardServlet.java
  - MovieListServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-87/blob/3dc2f8ff42b3b79bfb6e12a46af23d90b65340ad/cs122b-spring21-project1/src/MovieListServlet.java
  - PaymentPageServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-87/blob/3dc2f8ff42b3b79bfb6e12a46af23d90b65340ad/cs122b-spring21-project1/src/PaymentPageServlet.java
  - ShoppingCartServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-87/blob/3dc2f8ff42b3b79bfb6e12a46af23d90b65340ad/cs122b-spring21-project1/src/ShoppingCartServlet.java
 - SingleMovieServlet.java: https://github.com/UCI-Chenli-teaching/cs122b-spring21-team-87/blob/3dc2f8ff42b3b79bfb6e12a46af23d90b65340ad/cs122b-spring21-project1/src/SingleMovieServlet.java

## Two parsing time optimization strategies
The parsing strategies we took to optimize the parsing was to load the data into memory to check for duplicates then append the data to a .csv file which we later used LOAD DATA with the .csv file in mysql to add the data to the database. These strategies are better to use than the naive approach because it in the naive approach you first need to execute a query to see if the record is a duplicate, this can be costly, especially if it is done for every record. It is better to check for duplicates in memory so you don't have to execute so many querys just to check for duplicates. Then, in the naive approach, after the record is checked if it's a duplicate a querey is parsed and executed to add that record to the table. Again this is costly so a better way to do this is to append the data to a file and load that file into the database with the LOAD DATA method. In this meathod, each record from the file is added without executing it's own query, which is less costly.

## Inconsistent data report from parsing
The main inconsitency from parsing the data was the genre format. Many of the genres were worded in a different format, such as different cases for letters and different acronyms for certain genres.

## Member Contribution

Kgong148: 
 - Implemented functionality of reCAPTCHA, and Employee Dashboard, and setup the prepared statements
 - Conntributed to https redirection
 - Comtributed to readme file
 - encrpyted passwords
 
adiaz18011:
- Completed demo and readme file
- Implemented XML parser
- Contributed to https redirection
- encrpyted passwords

 # cs122b-spring21-team-87 - Project 2
 
 ## Demo Video URL
 
 https://drive.google.com/file/d/1ptzb1vuQHXYOFPDIAvrxDIb4ANQU0Zt4/view?usp=sharing
 
 ## How to deploy applicaion with with Tomcat
 In order to deploy the application on aws with Tomcat you need to have MySQL, Tomcat and maven installed on the aws instance and have it running. 
 Once that is done you will create a directory that will contain the clone of this project. In this case the comand will be git clone https://git
 hub.com/UCI-Chenli-teaching/cs122b-spring21-team-87.git. Afterwards, you will go to the directory that contains the pom.xml file and run the command mvn package. This will create a target file that contains the war file that needs to be deployed on Tomcat. In order to deploy this war file you need to copy it into the directory /var/lib/tomcat9/webapps/, this can be done with the command cp ./target/*.war /var/lib/tomcat9/webapps/. Once the war file is in this directory it will be deployed through Tomcat. You can view this through a webpage by going to the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/manager". The application can then be viewed with the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/cs122b-spring21-project1". You then need to run the files createtable.sql and addPriceCol.sql. The first file fist creates and populates all the schemas needed for this application the second file  adds a column to the movies page that has the initial price of each movie.

 ## Substring Matching Design
 For the search section we used the SQL keyword LIKE with '%' which tests whether a string contains a certain pattern. We took the submitted string and added '%' to both sides. What this did is allow the selection of strings that had the submitted word as a substring, is didn't necessarily have to start or end with the submitted string. We didn't do this with the year however since it is an INT. It also doesn't seem necessary to have this feature with the year.

 ## Member Contribution

Kgong148: 
 - Implemented functionality of login page, main page, and movie list
 - Conntributed to funtionality of shopping cart
 - Complete payment info page
 - Developed string matching design

adiaz18011:
- Completed demo and readme file
- Partialy implemented shopping cart feature
- Add sql file for prices
- Contribute to linking pages to shopping cart
  
 
 # cs122b-spring21-team-87 - Project 1
 
 ## Demo Video URL
 
 https://drive.google.com/file/d/1TH7lSQ18SnLdxaN8vSR6Ct37heRXt2t0/view?usp=sharing
 
 ## How to deploy applicaion with with Tomcat
 In order to deploy the application on aws with Tomcat you need to have MySQL, Tomcat and maven installed on the aws instance and have it running. 
 Once that is done you will create a directory that will contain the clone of this project. In this case the comand will be git clone https://git
 hub.com/UCI-Chenli-teaching/cs122b-spring21-team-87.git. Afterwards, you will go to the directory that contains the pom.xml file and run the command mvn package. This will create a target file that contains the war file that needs to be deployed on Tomcat. In order to deploy this war file you need to copy it into the directory /var/lib/tomcat9/webapps/, this can be done with the command cp ./target/*.war /var/lib/tomcat9/webapps/. Once the war file is in this directory it will be deployed through Tomcat. You can view this through a webpage by going to the URL "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/manager". The application can then be viewed with the URL 
  "http://ec2-<ip address>.us-east-2.compute.amazonaws.com:8080/cs122b-spring21-project1"

 ## Member Contribution

Kgong148: 
 - Initiated aws instance and installed MySQL, Maven, and Tomcat on it
 - Created sql file to create files for the database. 
 - Worked on functionality and set up of movie list, single movie page, and single star page

adiaz18011:
- Initiated aws instance and installed MySQL, Maven, and Tomcat on it
- Worked on functionality of movie list, single movie page, and single star page
- Worked on README and demoed the application
