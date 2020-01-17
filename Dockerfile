FROM tomcat:8.5-alpine
COPY target/projectmanagerapi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/app.war
COPY tomcat/tomcat-users.xml /usr/local/tomcat/conf/
COPY tomcat/context.xml /usr/local/tomcat/webapps/manager/META-INF/
CMD ["catalina.sh", "run"]
EXPOSE 8080