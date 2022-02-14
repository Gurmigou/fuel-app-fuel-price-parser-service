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
http://DOMAIN_NAME/api/v1/fuel-info?regionLatin="region_name_example"
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
]    
```

---

### A list of available regions:
<ol>
    <li>Винницкая обл.</li>
<li>Волынская обл.</li>
<li>Днепропетровская обл.</li>
<li>Донецкая обл.</li>
<li>Житомирская обл.</li>
<li>Закарпатская обл.</li>
<li>Запорожская обл.</li>
<li>Ивано-Франковская обл.</li>
<li>Киевская обл.</li>
<li>Кировоградская обл.</li>
<li>Луганская обл.</li>
<li>Львовская обл.</li>
<li>Николаевская обл.</li>
<li>Одесская обл.</li>
<li>Полтавская обл.</li>
<li>Ровенская обл.</li>
<li>Сумская обл.</li>
<li>Тернопольская обл.</li>
<li>Харьковская обл.</li>
<li>Херсонская обл.</li>
<li>Хмельницкая обл.</li>
<li>Черкасская обл.</li>
<li>Черниговская обл.</li>
<li>Черновицкая обл.</li>
</ol>    
