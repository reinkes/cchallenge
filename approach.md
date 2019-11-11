# Approach to the coding challenge
First of all I read the documentation to understand the use-cases of the service and the limits of the service.
After the initial setup of the current spring and gradle version I started with a simple prototype to call the API 
and read the data to an useful structure. I didn't want to run through hash-maps and settled with Jacksons JSON parser. 
With the parsing done I implemented the "Filter" use-case first and the "Sorting" as the second use-case.
Since the API response, the filter and sorting parameter were external values I decided to extract all the transformers into single classes and write tests to check them.
I also created a intermediate class to filter the results and a value object to return as the webservice result. This is best practice to separate the output (or the input) from the classes used in the business logic. (extensibility)
While the business logic increased I kept adding REST-tests to the basic test I did earlier.
As a cleanup I added logging, exception handling and the dockerfile.

Nothing was mentioned about the extensibility of the service - I tried to keep an extensible. (i.e. more filter options)
I also thought about performance and memory issues but ignored them for the moment. There are several caching solutions at hand. (i.e. Caching the external API-Response for a few minutes, caching our own responses)
There are few options to reduce the memory-optimization of the application but in many cases it trades of with the maintainability.
Adding Swagger to Spring Boot was basically just a dependency - but I stopped after including it (I didn't add any annotations to describe the service better)
The spring actuator plugin (i.e. for healthchecks) is not included, it's not going to be a production service.

## Short
I started with a minimum value product and added the features top-down. When needed I added the test to speed up the testing process and increase the reusability of the tests.
