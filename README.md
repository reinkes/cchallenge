# Coding Challenge
##### https://github.com/aacml/Recruiting/wiki/Cloud-Software-Engineer-Code-Challenge

### Gradle Project
#### Windows
./gradlew.bat build
#### Linux
./gradlew.sh build

### Docker
Multistage dockerfile:
- uses gradle(5.6) and java 8 to build/test the project
- builds the image
-- publishes port 8088

Build docker image with:
docker build -t apiservice:latest .

Run docker image with:
docker run -p 7777:8088 apiservice:latest

## Endpoints
### Project Endpoint
http://localhost:7777/links
Optional-Parameters: 
- parent
- sort

### Swagger-Documentation
Swagger-UI Endpoint: http://localhost:7777/swagger-ui.html
Swagger JSON API: http://localhost:7777/v2/api-docs
