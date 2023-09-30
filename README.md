# distance-between-points
<h2>Task:</h2>
Create an API to calculate the distance between two points on the earth's surface (points are specified by latitude/longitude coordinates). An important requirement is a limit of 10 calls per minute for each external user.

<h2>Solution:</h2>
To identify the user, an endpoint was added through which the user can obtain his unique key using the transmitted keyword. With this key, the API can recognize the user and not give more than 10 responses per minute.
To calculate the distance between points, a third-party API (open street map) and a simple formula for calculating the direct distance between points were used.

To test the API, you can use <a href="https://universal-flare-864500.postman.co/workspace/distance-between-points~74ab5cdf-2b20-4a04-bc56-bb317aac684c/collection/27144104-ac29279f-d23f-44be-b9f1-50e8f26874b9?action=share&creator=27144104">Postman collation</a>
