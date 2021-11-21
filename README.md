# WGU C195 - Schedule Keeper
>A GUI-based scheduling application for managing customer appointments.  

**Author:** William Gunn\
**Email:** wgunn@my.wgu.edu\
**Version:** 1.0\
**Date:** 2021-11-15\
**IDE:** IntelliJ IDEA 2021.2.2 (Community Edition)\
**JDK:** OpenJDK 11.0.12\
**JavaFX:** JavaFX SDK 17.0.0.1

## Usage
1. Set the database connection details by editing src/utils/JDBC.java and setting the following properties:
   1. location - The hostname and port. Default: `//localhost:3306/`
   2. database - The database name. Default: `client_schedule`
   3. user     - The database username. Default `sqlUser`
   4. password - The database password. Default `Passw0rd!`
2. launch IntelliJ and open the project
3. In the navigation tree, find the Main package at src/main/Main
4. Right-click on the Main package and select `Run 'Main.main()`

## Additional Report 
The additional report is called **User Schedule by Location**. This report provides the following data:

- User
- Location
- Appointment ID
- Title
- Description
- Start
- End
- Customer ID

## MySQL Connector driver
> mysql-connector-java-8.0.25