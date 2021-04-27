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
