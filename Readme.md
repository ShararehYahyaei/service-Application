**Service API**

****Introduction****

This is a REST API for a service management system.
The API allows users to manage services, handle requests, 
and track service statuses.


***Technologies Used***

- Java 23
- Spring Boot 
- Maven
- PostgresSQL
- JPA & Hibernate



**Requirements**

Before running the project, ensure you have:
Java  installed,
Maven installed,
PostgresSQL running

**API Workflow**

The API follows a step-by-step process to set up and manage services:

**Step 1: Create User**

_Users must be created first. The system supports different roles:_
**Customer** - Can request services.
**Specialist** - Can provide services.
**Admin** - Manages users and services.

**Step 2: Adding Service Categories **

_The admin can add different service categories to the system._

**Step 3: Creating Sub Service Category Services
And assign a sub service
Category to Specialist**

_Specific services can be added under each service category._

****Step 5: Submitting a Service Request (Customer)****
_A customer can submit a request for a specific service.
The request contains details such as the service type and
any specific requirements._

**Step 6: Specialists Viewing Available Requests**

_Specialists can view all available service requests based
on their skills.
They can choose the ones they want to provide an offer for._

**Step 7: Sending an Offer (Specialist)**

_A specialist selects a service request and submits an offer to 
provide the requested service.
The offer includes the price and estimated time for completion._

**Step 8: Viewing Offers (Customer)**

_The customer can see all offers submitted by
different specialists for their service request._

**Step 9: Choosing an Offer and Creating an Order**
_The customer selects one offer from the available options.
An order is created based on the selected offer._

**step 10: leave a review for that order**
_after completing an order specialist can change the status for that and then customer
can leave a comment and
review for that_

**Create a new user**
POST http://localhost:8081/api/v1/user/create
_Just one point only Specialist should add a picture for himself_
```json
{
  "address": "123 Main St",
  "phone": "+1234567890",
  "name": "John",
  "lastName": "Doe",
  "userName": "johndoe",
  "email": "johndoe@example.com",
  "password": "securePassword123",
  "role": "CUSTOMER",
  "profileImage": "(Binary File)"
}
```


**Response**
```json
{
"message": "User registered successfully"
}
```

_**can be different based on role**_


**Create a new Category**
POST http://localhost:8081/api/v1/category/create
 _any user can add category for system_
`{
"name": ""
}
`

**Create a new SubServiceCategory**
POST http://localhost:8081/api/v1/subCategory/create
 _any uer can create sub Service category and when you are creating a new sub Service
you have to add it  to one category_
```json
{
"name": "خدمات نظافت منزل",
"description": "تمیزکاری کامل منزل با مواد شوینده باکیفیت",
"price": 300.0,
"categoryId": 6
}
```

**Get All SubService that already there are in system**
GET http://localhost:8081/api/v1/specialist/getAllSubServiceCategories
```json
{
"id": 2,
"name": "دکوراسیون داخلی",
"description": "طراحی و اجرای دکوراسیون داخلی منزل و محل کار",
"price": 1500.0,
"categoryName": "\"بهداشششت\""
}
```
```json
{
"id": 3,
"name": "تعمیرات لوله‌کشی",
"description": "رفع نشتی و مشکلات لوله‌کشی ساختمان",
"price": 800.0,
"categoryName": "تاسیسات ساختمان"
}
```


**Add a sub service category to a specialist**
PUT http://localhost:8081/api/v1/user/addCategoryToUser/{{userId}}/{{subCategoryId}}
_by this Url you can add a new Sub  service category to a user specialist based on him expertise_



**create a new request**
POST http://localhost:8081/api/v1/customer/addRequest

_in this system first a customer based on what it needed can submit a request 
and then any specialist according their expertise can see all requests_
```json
{
"userId": 2,
"subServiceCategory": 1,
"price": 1111.0,
"description": "Cleaning service",
"deadLineTime": "2025-03-30",
"address": "123 Main St, Amsterdam"
}
 ```
**Create An Offer for Specialist**
POST http://localhost:8081/api/v1/specialist/addOffer
_With this Url you can send an offer based on your skills_ 

```json
{
"specialistId": 7,
"offerPrice": 2222.50,
"offerDate": "2025-03-25",
"estimationTime": 7,
"customerRequestId":10
}
```
**See All Offers for Customer's Request** 
GET http://localhost:8081/api/v1/customer/getAllOffers?
user_id={{$random.integer(100)}}&
customer_request_id={{$random.integer(100)}}
_With This URl a customer can see all
Offers that already
are for him or her request_


**Create an Order**
POST http://localhost:8081/api/v1/customer/createOrder
_With this url can choose on of the offers and  create an order
in this step change all status for request and offer and order_ 


```json
{
"userId": 2,
"offerId": 11,
"customerRequestId": 9
}
```


**leave a comment and review**
POST http://localhost:8081/api/v1/customer/giveReview
_after completing all orders by specialist customer can add comment 
and put rate and reviews for specialist_

```json
{
"customerId": 8,
"userId": 6,
"orderId": 1,
"rating": 4,
"comment": "Great product!"
}
```


`All steps should be passed step by step,
otherwise can get suitable errors`




1. User Registration
   Users can sign up as Customer or Specialist via the /service page.
Specialists can select their Sub-Service Categories during sign-up.
Sub-Service Categories can also be added later by:
The Admin in the admin panel.
The Specialist in their own profile.

2. Email Verification
   After registration, a verification link is sent to the user's email.
Users must click the link to verify their email before logging in.

3. Admin Approval for Specialists
   After email verification, Specialists must be manually activated by the Admin in the admin panel.
Only activated specialists can access their profile and work with requests.

4. Customer Request Submission
   Customers can create a service request from their profile.
While submitting, they:
Choose service and sub-service.
Fill in request details.
Select an address using an interactive map.

5. Specialist Viewing Requests
   Specialists can view available service requests related to their selected sub-service category.
Requests are only visible if the specialist is active and verified.

6. Specialist Sending Offers
   Specialists can create offers for the available requests.
Offers include proposed price and estimated duration


7. Customer Viewing Offers
   Customers can view all received offers for their submitted requests.
They choose one offer, which creates an Order with the initial status
set to "Awaiting Specialist Arrival".


8. Specialist Starting the Job
   Once selected, the specialist can:
Change the order status to "In Progress".
The system shows a timer for the customer
indicating the remaining time based on the offer’s duration.

9. Order Completion & Payment
   When the specialist finishes the work, they change the status to "Completed".

The customer must then pay using:
Credit
or by adding a Card and paying with it.
After payment:
70% of the offer price is added to the Specialist's credit.
The specialist can view their current credit in their profile.


10. Prevent Duplicate Payments
    Once the order status is "Completed" and payment is done:
The system blocks re-payment.
The status cannot be changed anymore.


11. Customer Reviews
 After the payment:
Customers can leave a review for the specialist.
The specialist's rating becomes visible in their profile.

✅ Key Features
Role-based access: Customer, Specialist, Admin.

Email verification and admin approval.

Interactive address selection via map.

Offer and order management with real-time tracking.

Secure payment system (credit and card).

Review and rating system.




