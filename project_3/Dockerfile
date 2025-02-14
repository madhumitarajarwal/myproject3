# Use the official Tomcat 9 image from the Docker Hub
FROM tomcat:9

# Set the working directory (optional, but good practice)
WORKDIR /usr/local/tomcat

# Copy the WAR file to the Tomcat webapps directory
COPY target/project_3.war /usr/local/tomcat/webapps/project_3.war

# Expose the application port (Tomcat default port)
EXPOSE 8080

# Start Tomcat server
CMD ["catalina.sh", "run"]