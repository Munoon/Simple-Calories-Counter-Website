'use strict';

class Meals {
    constructor({ ajaxUrl, createModal, table, filter }) {
        this.ajaxUrl = ajaxUrl;
        this.table = table;
        this.filter = filter;

        this.filter.addEventListener('click', e => {
            e.preventDefault();
            switch (e.target.dataset.action) {
                case 'clear':
                    this.clearFilter();
                case 'filter':
                    this.updateTable();
            }
        });

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
                            "desc"
                        ]
                    ]
                })
            }
        );
    }

    save() {
        save(this._getFilterUrl());
    }

    updateTable() {
        updateTable(this._getFilterUrl());
    }

    clearFilter() {
        this.filter.reset();
    }

    _getFilterUrl() {
        return this.ajaxUrl + 'filter' + this._getFilterData();
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
    table: document.getElementById('datatable'),
    filter: document.getElementById('filter')
});