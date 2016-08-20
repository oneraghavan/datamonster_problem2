At Indix we do a lot of crawling to get the product data, along with their latest prices. There are benefits in being able to process and consume this data in realtime. For eg.

Customers would like to get notified immediately if any particular store carries a product that is on promotion or is out-of-stock
The data captured can be erroneous due to crawl/parse issues and internal Engineering would like to get notified if any basic contract fails. For eg.
If minSalePrice captured for a product is less than or equals zero
If minSalePrice happens to be greater than minListPrice
If storeId assigned for a record is assigned to be less than 1
We would like to build a generic notification system that supports applying a given set of rules on the incoming stream of data and invoke the specific handler if any rule matches. The system should be generic in the sense incoming data, rules and their handler types can vary based on the requirement.

Data Source

We have hosted a Kafka queue, the source of incoming records, the JSON schema of each record is as follows:

storeId (integer)
minSalePrice (double)
minListPrice (double)
title (String)
priceType (String)
availability (String)
Sample JSON (prettified json)

{
  "storeId": 220,
  "minSalePrice": 1560,
  "minListPrice": 1950,
  "title": "Men's 1/2 CT. T.W. Princess-Cut Diamond Wedding Band in 14K White Gold",
  "currencyType": "USD",
  "timestamp": 1471151752531,
  "availability": "IN_STOCK",
  "priceType": "NORMAL",
  "countryCode": "US"
}


System Requirements

System should be able to consume data from the queue as soon as it's available.
System should be able to notify the validation errors through a pluggable notification mechanism
The notifications can be sent on any asynchronous messaging system
The system should be scalable to -- large number of price records -- support large number of notification configurations -- support high throughput and low latency while sending notifications
Technology

Programming Language: Any language of your choice
