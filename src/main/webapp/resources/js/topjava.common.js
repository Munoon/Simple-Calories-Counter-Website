'use strict';

class AbstractCommon {
    #ajaxUrl;
    #datatableApi;
    #form;
    #failedNote;

    constructor({ ajaxUrl, datatableApi }) {
        this.#ajaxUrl = ajaxUrl;
        this.#datatableApi = datatableApi;
        this.#form = $('#detailsForm');

        document.querySelectorAll('.delete').forEach(element => {
            element.addEventListener('click', e => {
                if (confirm('Are you sure?'))
                    this.deleteRow(e.target.closest('a').dataset.id);
            });
        })

        $(document).ajaxError(function (event, jqXHR, options, jsExc) {
            this.failNoty(jqXHR);
        });

        // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
        $.ajaxSetup({cache: false});
    }

    add() {
        this.#form.find(":input").val("");
        $("#editRow").modal();
    }

    deleteRow(id) {
        $.ajax({
            url: this.#ajaxUrl + id,
            type: "DELETE"
        }).done(() => {
            this.updateTable();
            this.successNoty("Deleted");
        });
    }

    updateTable(url = this.#ajaxUrl) {
        $.get(url, (response) => {
            this.#datatableApi.clear().rows.add(response).draw();
        });
    }

    closeNoty() {
        if (this.#failedNote) {
            this.#failedNote.close();
            this.#failedNote = undefined;
        }
    }

    save(url = this.#ajaxUrl) {
        $.ajax({
            type: "POST",
            url: this.#ajaxUrl,
            data: this.#form.serialize()
        }).done(() => {
            $("#editRow").modal("hide");
            this.updateTable(url);
            this.successNoty("Saved");
        });
    }

    failNoty(jqXHR) {
        this.closeNoty(jqXHR);
        this.#failedNote = new Noty({
            text: `<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: ${jqXHR.status}`,
            type: "error",
            layout: "bottomRight"
        }).show();
    }

    successNoty(text) {
        this.closeNoty();
        new Noty({
            text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
            type: 'success',
            layout: "bottomRight",
            timeout: 1000
        }).show();
    }
}