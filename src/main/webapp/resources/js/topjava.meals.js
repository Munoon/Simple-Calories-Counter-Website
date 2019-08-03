'use strict';

class Meals {
    constructor({ ajaxUrl, createModal, table, filter }) {
        this.ajaxUrl = ajaxUrl;
        this.createModal = createModal;
        this.table = table;
        this.filter = filter;

        this.createModal.querySelectorAll('input')
            .forEach(element => element.addEventListener('input', () => this._updateSaveButton(!this._validateInput())));

        makeEditable({
                ajaxUrl: this.ajaxUrl,
                datatableApi: $(this.table).DataTable({
                    "paging": false,
                    "info": true,
                    "columns": [
                        {
                            "data": "dateTime"
                        },
                        {
                            "data": "description"
                        },
                        {
                            "data": "calories"
                        },
                        {
                            "defaultContent": "Edit",
                            "orderable": false
                        },
                        {
                            "defaultContent": "Delete",
                            "orderable": false
                        }
                    ],
                    "order": [
                        [
                            0,
                            "asc"
                        ]
                    ]
                })
            }
        );
    }

    save() {
        if (this._checkFilter()) {
            save(this.ajaxUrl + 'filter' + this._getFilterData());
        } else {
            console.log('here 2');
            save();
        }
    }

    clearFilter() {
        this.filter.querySelectorAll('input').forEach(element => {
            element.value = '';
        });
    }

    _updateSaveButton(state) {
        this.createModal.querySelector('#saveButton').disabled = state;
    }

    _validateInput() {
        let datePattern = /\d{2}-\d{2}-\d{2}T\d{2}:\d{2}/g;
        let date = this.createModal.querySelector('#dateTime').value;
        let descriptionLength = this.createModal.querySelector('#description').value.length;
        let calories = +this.createModal.querySelector('#calories').value;

        if (!datePattern.test(date)) return false;
        if (calories < 10) return false;
        if (calories > 5000) return false;
        if (descriptionLength < 2) return false;
        if (descriptionLength > 120) return false;
        return true;
    }

    _checkFilter() {
        let result = false;
        this.filter.querySelectorAll('input').forEach(element => {
            if (element.value !== '')
                result = true;
        });
        return result;
    }

    _getFilterData() {
        let result = '?';
        this.filter.querySelectorAll('input').forEach(element => {
            result += `${element.name}=${element.value}&`;
        });
        return result.substring(0, result.length - 1);
    }
}

let meals = new Meals({
    ajaxUrl: 'ajax/meals/',
    createModal: document.getElementById('editRow'),
    table: document.getElementById('datatable'),
    filter: document.getElementById('filter')
});