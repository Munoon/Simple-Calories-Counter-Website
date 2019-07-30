**Replace `{URL}` WITH YOU MAIN PAGE URL**

*For example `{URL}` -> `http://localhost:8080/topjava`*

# Curl Rest Meals requests examples
Description | Command
----------- | -------
Get by Id | `$ curl {URL}/rest/meals/10000`
Delete | `$ curl -X "DELETE" {URL}/rest/meals/100003`
Create | `$ curl -d "@rest\create.json" -H "Content-Type: application/json" -X POST {URL}/rest/meals`
Update | `$ curl -d "@rest\update.json" -H "Content-Type: application/json" -X PUT {URL}/rest/meals/100007`
Get All | `$ curl {URL}/rest/meals`
Get Between | `$ curl {URL}/rest/meals/filter?startDate=2015-05-31`

# URL Rest Meals requests examples
Description | Method | URL | Body File
----------- | ------ | --- | ---------
Get | GET | {URL}/rest/meals/100002 | -
Delete | DELETE | {URL}/rest/meals/100002 | -
Create | POST | {URL}/rest/meals/ | rest/create.json
Update | PUT | {URL}/rest/meals/100007 | rest/update.json
Get All | GET | {URL}/rest/meals | -
Get Between | GET | {URL}/rest/meals/filter?startDate=2015-05-31 | -