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
        save(this._getUrl());
    }

    updateTable() {
        updateTable(this._getUrl());
    }

    clearFilter() {
        this.filter.reset();
    }

    _getUrl() {
        return this.ajaxUrl  + this._getFilterData();
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
    ajaxUrl: 'ajax/meals/filter',
    table: document.getElementById('datatable'),
    filter: document.getElementById('filter')
});