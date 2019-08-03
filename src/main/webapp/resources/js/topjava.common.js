'use strict';

let context, form;

function makeEditable(ctx) {
    context = ctx;
    form = $('#detailsForm');
    $(".delete").click(function (e) {
        if (confirm('Are you sure?')) {
            deleteRow(e.target.closest('a').dataset.id);
        }
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});

    document.querySelectorAll('.user-enabled').forEach(element => {
        element.addEventListener('click', e => {
            let target = e.target;
            let id = target.id;
            let checkbox = target.checked;

            $.ajax({
                url: `${context.ajaxUrl}${id}?enabled=${checkbox}`,
                type: 'POST'
            }).done(() => {
                updateTable();
                successNoty('Updated user active status');
            })
        });
    });
}

function add() {
    form.find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: context.ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("Deleted");
    });
}

function updateTable(url = context.ajaxUrl) {
    $.get(url, (response) => {
        context.datatableApi.clear().rows.add(response).draw();
    });
}

function save(url = context.ajaxUrl) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable(url);
        successNoty("Saved");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}