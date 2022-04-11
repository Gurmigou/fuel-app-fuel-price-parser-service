# **Fuel prices microservice**
### About this microservice
This microservice provides you with data of fuel information in Ukraine.
The information you can get is:
- name of the fuel
- price
- region of the fuel station

### Source
The source of the data is [there](https://index.minfin.com.ua/markets/fuel/detail/).

# Actions (API)
This section shows a list of possible requests and their example responses.
### 1. Get information about the fuel in the specified region
**HTTP method**
```
GET
```
**URL**
```
http://DOMAIN_NAME/api/v1/fuel-info?regionLatin=Kyivs'ka oblast
```
**Response example:**
```jsonc
[
    {
        "id": 1,
        "region": "Киевская обл.",
        "fuelType": "А95+",
        "gasStation": "KLO",
        "price": 34.09,
        
        "logo": [12, 45, 91, 57, 83, ...],
        "logoContentType": "image/jpg"
    },
    ... 
]    
```
### 2. Get information about the fuel of all regions by specified gas station name 
**HTTP method**
```
GET
```
**URL**
```
http://DOMAIN_NAME/api/v1/fuel-info?gasStation=WOG
```
**Response example:**
```jsonc
[
    {
        "id": 1,
        "region": "Киевская обл.",
        "fuelType": "А95+",
        "gasStation": "WOG",
        "price": 36.85,
        
        "logo": [12, 45, 91, 57, 83, ...],
        "logoContentType": "image/jpg"
    },
    ... 
]    
```
### 3. Get information about the fuel of the specified gas station and in the specified region
**HTTP method**
```
GET
```
**URL**
```
http://DOMAIN_NAME/api/v1/fuel-info?regionLatin=Kyivs'ka oblast&gasStation=WOG
```
**Response example:**
```jsonc
[
    {
        "id": 1,
        "region": "Киевская обл.",
        "fuelType": "А95+",
        "gasStation": "WOG",
        "price": 36.85,
        
        "logo": [12, 45, 91, 57, 83, ...],
        "logoContentType": "image/jpg"
    },
    ... 
]    
```
### Warning!
If you don't specify any request parameters, you will recieve the following error:
```
Parameters regionLatin and gasStation cannot be both null
```
---

### A list of available regions:
<ol>
<li>Vinnyts'ka oblast, Винницкая обл.</li>
<li>Volyns'ka oblast, Волынская обл.</li>
<li>Dnipropetrovs'ka oblast, Днепропетровская обл.</li>
<li>Donets'ka oblast, Донецкая обл.</li>
<li>Zhytomyrs'ka oblast, Житомирская обл.</li>
<li>Zakarpats'ka oblast', Закарпатская обл.</li>
<li>Zaporiz'ka oblast, Запорожская обл.</li>
<li>Ivano-Frankivs'ka oblast, Ивано-Франковская обл.</li>
<li>Kyivs'ka oblast, Киевская обл.</li>
<li>Kirovohrads'ka oblast, Кировоградская обл.</li>
<li>Luhans'ka oblast, Луганская обл.</li>
<li>L'vivs'ka oblast, Львовская обл.</li>
<li>Mykolaivs'ka oblast, Николаевская обл.</li>
<li>Odes'ka oblast, Одесская обл.</li>
<li>Poltavs'ka oblast, Полтавская обл.</li>
<li>Rivnens'ka oblast, Ровенская обл.</li>
<li>Sums'ka oblast, Сумская обл.</li>
<li>Ternopil's'ka oblast, Тернопольская обл.</li>
<li>Kharkivs'ka oblast, Харьковская обл.</li>
<li>Khersons'ka oblast, Херсонская обл.</li>
<li>Khmel'nyts'ka oblast, Хмельницкая обл.</li>
<li>Cherkas'ka oblast, Черкасская обл.</li>
<li>Chernihivs'ka oblast, Черниговская обл.</li>
<li>Chernivets'ka oblast, Черновицкая обл.</li>
</ol>    
