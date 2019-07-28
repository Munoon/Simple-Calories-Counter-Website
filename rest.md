# Crud Rest Meals requests examples
Description | Method | URL | Body
----------- | ------ | --- | ----
Get | GET | /rest/meals/100002 | -
Delete | DELETE | /rest/meals/100002 | -
Create | POST | /rest/meals/ | *Create Body*
Update | PUT | /rest/meals/100009 | *Update Body*

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