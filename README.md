# Parser [![Build Status](https://travis-ci.org/HPI-BP2017N2/Parser.svg?branch=master)](https://travis-ci.org/HPI-BP2017N2/Parser)

The Parser is a microservice component, that extracts product attributes from a webpage using shop rules. 
The microservice is written in Java and uses the Spring framework. It has no REST interface.


## Getting started
### Prerequisites

To run the microservice it is required to set up the following:

1. MongoDB
<br />The MongoDB is used to store extracted product attributes (in the database named 'parsedOffers'
and collection named after shopID).

2. RabbitMQ
<br />The Parser consumes HTML pages from a RabbitMQ.

### Configuration

#### Environment variables
- MONGO_IP: The IP of the MongoDB instance.
- MONGO_PORT: The port of the MongoDB instance.
- MONGO_BRIDGE_USER: The username to access the MongoDB.
- MONGO_BRIDGE_PW: The password to access the MongoDB.
- RABBIT_IP: The IP of the RabbitMQ.
- RABBIT_PORT: The port of the RabbitMQ.
- RABBIT_USER: The username to access the Rabbit MQ.
- RABBIT_PW: The password to access the RabbitMQ.


#### Component properties

- waitIfThreadCapacityReachedInMilliseconds: The time that the Parser will wait before fetching next message 
from RabbitMQ (only if queue capacity of internal threadpool is exceeded).
- corePoolSize: Amount of CPU Cores which should be used during generation.
- maxPoolSize: Max amount of running threads at the same time.
- queueCapacity: Max amount of waiting tasks to ShopRulesGenerator.


## How it works

1. Parser get an HTML page with shop ID from the Rabbit MQ.

2. Parser send a request to ShopRulesGenerator to get shop rules for the current shop ID.

3. When Parser get shop rules from the ShopRulesGenerator, it can extract product attributes from the HTML.

4. After extracting product attributes Parser saves these in Mongo DB.

