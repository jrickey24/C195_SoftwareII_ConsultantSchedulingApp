# C195_SoftwareII_ConsultantSchedulingApp
#Application Version: C195-Software II Advanced Java Concepts-PRFA—QAM1

#IDE: IntelliJ Community Edition 2020.3.3
#JDK: Java SE 11.0.10, JavaFX-SDK-11.0.2
#MYSQL Driver: mysql-connector-java-8.0.22

#Purpose:
This application provides a UI for a Software Consultant Group through which the authenticated end-user can schedule,
modify, or delete Customers and customer Appointments. It handles the many timezones where the company's offices
and their clients reside and presents them to the end-user in their local time.

#Measures are taken throughout the program to help limit undesirable or invalid user input.
*-Limiting the character length of the username and password TextFields processed.
*-Verifying that the provided credentials match the stored credentials.
*-Validating that the phone number's format matches a regular expression.
*-Checks that provided scheduling times are within the business hours of 8AM-10PM EST.
*-Uses ComboBoxes and a DatePicker for collecting and updating Appointment information.

#The applications provides additional features such as:
*-Providing tableviews of all Customers & Appointments that exists in the system.
*-Providing tableviews for all listed points of Contact associated with an Appointment in the system.
*-Providing the count of Appointments by type for any given Month the user selects from a drop-down.
*-Reporting and Piechart

#Description of Chosen A3f Report - "SoftwareAppointmentsByOffice.java" :
The report of choice "SoftwareAppointmentsByOffice.java" generates the percentage of appointments
scheduled of type: "Software Development" by office location: "White Plains, Phoenix, Montreal, London".
This data is presented in the form of a pie chart. This provides an element of data visualization to the application
which serves to assist with making informed business decisions regrading additional staffing requirements, and
profitable markets as the business expands. Lambdas are used to help retrieve this data.

#Directions:
The application is launched through the Main.java class. The properties files are used to assign the login screen
values to French or English based on the user's system settings. All times are presented to the user in their local
time & validations are enforced to handle time conversions between zones throughout the program.
If login is successful, the user is notified of any appointments scheduled within the next 15 minutes of authentication
in their local time & redirected to the Main/Home menu. The results of each login attempt are logged to:
"C:\Users\{UserEnvironment}\ConsultantSchedulingApp\login_activity.txt".
The main menu is divided into three sections: "Customers, Appointments, Reports". To navigate to a specific section,
the button corresponding to the chosen selection is clicked or pressed. All screens within the application will either
redirect the user to the Home/Main screen upon completed action/confirmation, or provide the user with a "Home" button
option.
