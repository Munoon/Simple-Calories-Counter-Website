const mealsAjaxUrl = 'ajax/profile/meals/';
const startDate = $('#startDate');
const endDate = $('#endDate');
const startTime = $('#startTime');
const endTime = $('#endTime');

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealsAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

function initDateTimePickers() {
    $.datetimepicker.setLocale('ru');
    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function() {
            if (endDate.val())
                this.setOptions({ maxDate: endDate.val() });
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function() {
            if (startDate.val())
                this.setOptions({ minDate: startDate.val() });
        }
    });
    startTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function() {
            if (endTime.val())
                this.setOptions({ maxTime: endTime.val() });
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function() {
            if (startTime.val())
                this.setOptions({ minTime: startTime.val() });
        }
    });
    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
}

function saveMeal() {
    // сначала хотел изменить значение input и после вызова save() вернуть старое
    // но тогда у юзера это Т будет мигать
    let data = '';
    document.querySelectorAll('#editRow input').forEach(element => {
        let value = element.name === 'dateTime' ? element.value.replace(' ', 'T') : element.value;
        if (element.value !== '')
            data += `${element.name}=${value}&`;
    });
    save(data.substring(0, data.length - 1));
}

$(function () {
    makeEditable({
        ajaxUrl: mealsAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealsAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": (data, type, row) => {
                        if (type === 'display')
                            return data.substring(0, 16).replace('T', ' ');
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "createdRow": function ( row, data, index ) {
                $(row).attr("data-mealExcess", data.excess);
                // вот это я уже скопировал из демо,
                // у самого не получилось сделать именно 'data-mealExcess'
            },
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        }),
        updateTable: updateFilteredTable
    });

    initDateTimePickers();
});