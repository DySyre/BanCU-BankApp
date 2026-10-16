# BanCU-BankApp
Java-Springboot Console Base Banking app

cd C:\Users\Dysyre\IdeaProjects\BanCU

<!-- Enable console base -->
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081 --bancu.console.enabled=true"

RESTful Endpoints

BanCU Banking API
Postman collection for the BanCU Spring Boot REST API. Uses account numbers returned by Create Account. Set the baseUrl collection variable to your local or deployed API URL. These requests match the controllers shared by the user.

﻿

Accounts
Add folder description…

﻿

POST
Create Account
{{baseUrl}}/api/accounts
Creates an account. On success, copy accountNumber from the response into the collection variable accountNumber.

﻿

Body
raw (json)
json
{
  "accountHolder": "Juan Dela Cruz",
  "initialDeposit": 1000.0
}
GET
Check Balance
{{baseUrl}}/api/accounts/{{accountNumber}}/balance
Retrieves the balance for the selected account.

﻿

POST
Deposit
{{baseUrl}}/api/accounts/{{accountNumber}}/deposit
Deposits money into the selected account.

﻿

Body
raw (json)
json
{
  "amount": 500.0
}
POST
Withdraw
{{baseUrl}}/api/accounts/{{accountNumber}}/withdraw
Withdraws money from the selected account.

﻿

Body
raw (json)
json
{
  "amount": 100.0
}
GET
Transaction History
{{baseUrl}}/api/accounts/{{accountNumber}}/transactions?page=0&size=10
Returns the first page of transaction history. Adjust page and size as needed.

﻿

Query Params
page
0
size
10
GET
List All Accounts
{{baseUrl}}/api/accounts
Add request description…

﻿

Transfers
Add folder description…

﻿

POST
Transfer
{{baseUrl}}/api/transfers
Transfers money between two different active accounts. Set sourceAccountNumber and destinationAccountNumber collection variables first.

﻿

Body
raw (json)
json
{
  "sourceAccountNumber": "{{sourceAccountNumber}}",
  "destinationAccountNumber": "{{destinationAccountNumber}}",
  "amount": 200.0
}