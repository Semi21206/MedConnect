## Patient Portal ##
## *Description:*
The Patient Portal is a web application that allows patients to register, log in, view their medical reports, and schedule appointments with their doctors. Doctors can upload medical reports for patients and view the appointments booked by the patients.

## *Features* ##
## Patient Features:
Register and log in.
Book appointments with doctors.
View and download medical reports uploaded by doctors.

## Doctor Features:
Upload medical reports for patients.
View appointments scheduled by patients.

## *Tech Stack* ##
Backend: Java Spring Boot
Frontend: HTML5, Thymeleaf, TailwindCSS
Database: MySQL
Build Tool: Maven
IDE: IntelliJ IDEA

## *Installation* ##
## Prerequisites
Java 17 or higher
Maven
MySQL
Node.js (for TailwindCSS compilation)
IntelliJ IDEA

## Installation Steps ##
Clone the project: Open IntelliJ IDEA, go to File -> New -> Project from Version Control, and enter the Git repository URL:
git clone https://github.com/username/patient-portal.git

Set up the MySQL database: Create a MySQL database and configure the connection in the src/main/resources/application.properties file:

## properties:
spring.datasource.url=jdbc:mysql://localhost:3306/patientportal
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
Install backend dependencies: Open the pom.xml file in IntelliJ IDEA, and Maven will automatically download all the required dependencies. Alternatively, you can run the following command in the terminal:
mvn clean install

Configure TailwindCSS: Ensure that Node.js is installed, then run the following commands in the project root directory:
npm install
npm run build:css

Run the application: In IntelliJ IDEA, click the Run button or execute the following command in the terminal:
mvn spring-boot:run
The application will run by default at http://localhost:8080.

## *Usage*
## Patients
Registration: New patients can register by filling out the registration form.
Login: After registration, patients can log in using their email and password.
Book an appointment: Patients can view available appointment slots and schedule an appointment with their doctor.
View medical reports: Patients can view and download their medical reports as PDFs once uploaded by the doctor.

## Doctors
Login: Doctors can log in with their provided credentials.
View appointments: Doctors can see a list of appointments booked by patients.
Upload medical reports: Doctors can upload medical reports for their patients
