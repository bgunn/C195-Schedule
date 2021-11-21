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
3. Set the Project SDK by navigating to `File -> Project Structure -> Project`
   1. Select SDK 11 from the **Project SDK** dropdown
   2. Select `SDK default` from the **Project language level** dropdown
4. Add the MySQL connector driver by navigating to `File -> Project Structure -> Libraries`
   1. Click the + icon and select `From Maven`
   2. Enter mysql:mysql-connector-java:8.0.25
5. If necessary, download the JavaFX 17 SDK from https://gluonhq.com/products/javafx/
6. Add the JavFX library by navigating to `File -> Project Structure -> Libraries`
   1. Click the + icon and select Java
   2. Browse to the lib folder of the JavaFX SDK 17 installed on your system and select `Ok`
7. Add the JavaFX module path and modules to the run configuration by navigating to `Run -> Edit configuration`
   1. Add the following VM options:
      1. --module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics
8. Add the PATH_TO_FX Path Variable by navigating to `File -> Settings -> Path Variables`
   1. If PATH_TO_FX is already set, verify it is pointing to JavaFX SDK. If it isn't, select PATH_TO_FX and click the pencil icon to edit the value.
   2. If PATH_TO_FX is not set, click the + icon and set the name to PATH_TO_FX and for the value, browse to the lib folder of the JavaFX 17 SDK install location
9. In the navigation tree, find the Main package at src/main/Main
10. Right-click on the Main package and select `Run 'Main.main()`

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