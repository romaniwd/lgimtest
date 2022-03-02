# LGIM Test Assignment

### Coin Vending Machine API

An API has been created to satisfy the requirement of InterviewAssignment#2.doc

The API has 4 endpoints
* /initialise - Initialises the in memory state of the vending machine
* /update - update the state of the vending machine
* /dispense - dispense coins for the provided value if possible
* /get - get the current state of the vending machine

To test, please start the application using intellij or other editor start facility, 
or run a gradle clean build and execute the created jar in the /build/libs directory.

The application will run on port 8080.

A talend chrome HTTP test file has been provided in the test/resources folder called lgimtest-talend.json which has example requests for all endpoints.

The chrome plugin can be found here: 
https://chrome.google.com/webstore/detail/talend-api-tester-free-ed/aejoelaoggembcahagimdiliamlcdmfm?hl=en

