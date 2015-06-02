#! /bin/bash

sudo apt-get update
sudo apt-get install unzip

set +e
# Install java 8
./scripts/installJavaDebian.sh
   
# Install tomcat 8
./scripts/installTomcat8.sh

# Install mongodb
sudo apt-get install -y mongodb

# PreInstallVirtuoso
sudo ./scripts/preVirtuosoDebian.sh

# Install virtuoso
./scripts/installVirtuoso.sh

# Deploy the war file
if [ -f "./Repository-RI/target/FiwareRepository.war" ]; then
    cp ./FiwareRepository.war $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository.war
else
    cp ./Repository-RI/target/FiwareRepository.war $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository.war
fi


#Modify Repository OAuth2
./scripts/oAuthConfig.sh

#Start Virtuoso
cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
$INSPWD/virtuoso7/bin/virtuoso-t -f &
cd $INSPWD

#Start Tomcat
cd $INSPWD/apache-tomcat-8.0.22/bin/
./shutdown.sh
./startup.sh
cd

#Create taks
sudo ./scripts/startupDebian.sh