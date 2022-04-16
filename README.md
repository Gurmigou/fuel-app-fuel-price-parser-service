# **Fuel prices microservice**
### About this microservice
This microservice provides you with data of fuel information in Ukraine.
The information you can get is:
- id
- name of the fuel
- price
- region of the fuel station
- logo of the fuel station (base64 format)

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
        "gasStationId": "avias",
        "gasStationName": "Авиас",
        "region": "Киевская обл.",
        "fuelPrices": [
            {
                "fuelType": "А95+",
                "price": 34.0
            },
            {
                "fuelType": "А92",
                "price": 33.0
            },
            {
                "fuelType": "ДТ",
                "price": 37.5
            },
            {
                "fuelType": "Газ",
                "price": 27.37
            }
        ]
    },
    {
        "gasStationId": "brsm",
        "gasStationName": "БРСМ-Нафта",
        "region": "Киевская обл.",
        "fuelPrices": [
            {
                "fuelType": "ДТ",
                "price": 39.49
            }
        ]
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
http://DOMAIN_NAME/api/v1/fuel-info?gasStationId=wog
```
**Response example:**
```jsonc
[
    {
        "gasStationId": "wog",
        "gasStationName": "WOG",
        "region": "Одесская обл.",
        "fuelPrices": [
            {
                "fuelType": "А95+",
                "price": 33.87
            },
            {
                "fuelType": "А92",
                "price": 33.77
            },
            {
                "fuelType": "ДТ",
                "price": 37.65
            }
        ]
    },
    {
        "gasStationId": "wog",
        "gasStationName": "WOG",
        "region": "Закарпатская обл.",
        "fuelPrices": [
            {
                "fuelType": "А95+",
                "price": 33.87
            },
            {
                "fuelType": "А92",
                "price": 33.77
            },
            {
                "fuelType": "ДТ",
                "price": 37.65
            }
        ]
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
http://DOMAIN_NAME/api/v1/fuel-info?regionLatin=Kyivs'ka oblast&gasStationId=wog
```
**Response example:**
```jsonc
{
    "gasStationId": "wog",
    "gasStationName": "WOG",
    "region": "Киевская обл.",
    "fuelPrices": [
        {
            "fuelType": "А95+",
            "price": 33.87
        },
        {
            "fuelType": "А92",
            "price": 33.77
        },
        {
            "fuelType": "ДТ",
            "price": 37.65
        }
    ]
}  
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

### A list of gas station mapping:
<ol>
<li>auto_trans, Автотранс</li>
<li>avias. Авиас</li>
<li>brsm, БРСМ-Нафта</li>
<li>factor, Фактор</li>
<li>katral, Катрал</li>
<li>mango, Mango</li>
<li>market, Маркет</li>
<li>neftek, Нефтек</li>
<li>okko, ОККО</li>
<li>olas, Олас</li>
<li>rur, Рур груп</li>
<li>shell, Shell</li>
<li>socar, SOCAR</li>
<li>ukr_nafta, Укрнафта</li>
<li>upg, UPG</li>
<li>urk_gaz, Укргаздобыча</li>
<li>urk_petrol, Укр-Петроль</li>
<li>wog, WOG</li>
</ol>    
