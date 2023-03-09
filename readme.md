Changes done in this commit : 
 - Required validation is applied to the Employee API. 
 - Controller advice for common exeception has been added . 
 - Custom exception has been added
 - General test cases for the employee api got added . 
 - For protecting the endpoints Spring security + JWT tokens are added . 
 - For testing purpose User endpoint is added through which new user can be added  and also get authenticated with required token .
   The token is then uses for accessing employee api . 
   Also if the token is expired user will not able to access the employee api . 
   Currenlty the jwt secret key is saved in application.yml but can be saved in system environment variables for production use . 
 - Spring Caching has been added. 
 - Logging is added using log4j. 
 - Updated some comments for better readability of code and also the syntax . 
 - Sysout lines has been removed

Things which can be done if more time is there : 
 - Test cases for user api should be added. 
 - Test data can be added to some json file . 
 - Can run the test cases with "SprinRunner.class" for also testing spring bean injection and its life cycle.
 - Logging can be be used in more relative places so that debugging and log readability will get increased.

