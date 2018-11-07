# VehicleNFC Rest Api
# Usage
### Order
---
- To add new Order
`localhost:8080/order/create`

Method used: `POST`

Return: True (Success), False (Failed)

Sample JSON:
```
{
  "checkInDate": 159321874,
  "userId": 
    {
      "id": 1,
      "phone": 094291429993
    },
  "locationId":
    {
      "location": "Quang Trung"
    },
}
```
- get order by id

`localhost:8080/order/get-order/{id}`
		
```

- get all order 

`localhost:8080/order/get-order`

```
### User

- Login

`localhost:8080/user/login`

Method used: `POST`

Params: "phone","password"

Return: User Object (Success), null (Failed)

Sample URL:
```
localhost:8080/user/login?phone=+541422223&password=123
```

- Sign up

`localhost:8080/user/create-user`

Method used: `POST`

Return: UserId (Success), False (Failed)

Sample JSON:
```
{
  "phoneNumber": 159321874,
  "password": 123,
  "firstName": cuong,
  "lastName": mai,
  "vehicleNumber": 84A-41424,
  "licensePlateId": 4381249327
}
```
### Vehicle

- Replace

`localhost:8080/vehicle/replace-vehicle`

Method used: `POST`

Params: "phoneNumber","vehicleNumber","licenseId"

Return: true (Success), false (Failed)

Sample URL:
```
localhost:8080/vehicle/replace-vehicle?phoneNumber=+541422223&vehicleNumber=21A2-21392&licenseId=KC-1234982
```
