# Rest Meals requests examples
Description | Method | URL | Body
----------- | ------ | --- | ----
Get | GET | /rest/meals/100002 | -
Get All | GET | /rest/meals/ | -
Get Between | GET | /rest/meals/filter?startDate=2015-05-31&endDate=2015-05-31&startTime=10:00&endTime=13:00 | -
Delete | DELETE | /rest/meals/100002 | -
Create | POST | /rest/meals/ | *Create Body*
Update | POST | /rest/meals/100009 | *Update Body*

## Create Body
```json
{
    "id": null,
    "dateTime": "2019-07-25T13:00:00",
    "description": "New Meal",
    "calories": 500 
}
```

## Update Body
```json
{
    "id": 100009,
    "dateTime": "2015-05-31T20:00:00",
    "description": "Updated Description",
    "calories": 700
}
```