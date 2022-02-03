# **Fuel prices microservice**
### About this microservice
This microservice provides you with data of fuel information in Ukraine.
The information you can get is:
- name of the fuel
- price
- region of the fuel station

### Source
The source of the data is [there](https://index.minfin.com.ua/markets/fuel/detail/).

### Actions (API)
This section shows a list of possible requests and their example responses.
#### Get information about the fuel in the specified region
**URL**
```
GET  http://DOMAIN_NAME/api/v1/fuel-info?regoin="region_name_example"
```
**Response example:**
```jsonc
[
    {
        "region": "Киевская обл.",
        "fuelType": "А95+",
        "gasStation": "KLO",
        "price": 34.09
    },
    ... 
```