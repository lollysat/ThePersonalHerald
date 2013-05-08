ThePersonalHerald
=================

Due to copyright issues we havent included the dependent jars in our web-app. 

Make sure you download and place the following set of jars in the webapp/WebContent/WEB-INF/lib folder:

commons-beanutils-1.8.3.jar
commons-fileupload-1.2.1.jar
commons-io-1.3.2.jar
commons-lang-2.1.jar
commons-logging-1.1.jar
freemarker-2.3.13.jar
jsonplugin-0.34.jar
log4j-1.2.15.jar
ognl-2.6.11.jar
struts2-core-2.1.6.jar
xwork-2.1.2.jar


In addition if any of the core modules fail to compile you might want to explicitly download the following set of jars:

boilerpipe-1.1.0.jar
Classifier4J-0.6.jar
nekohtml-1.9.13.jar
xerces-2.9.1.jar

We used MYSQL database for the project. Make sure you execute the script in the SQL folder and change the properties in the webapp
and the core java files to reflect the instance specific properties.
