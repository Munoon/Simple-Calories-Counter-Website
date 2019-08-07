'use strict';

class Meals extends AbstractCommon{
    #ajaxUrl;
    #filter;

    constructor({ ajaxUrl, datatableApi, filter }) {
        super({ ajaxUrl, datatableApi });
        this.#ajaxUrl = ajaxUrl;
        this.#filter = filter;

        filter.addEventListener('click', e => {
            e.preventDefault();
            switch (e.target.dataset.action) {
                case 'clear':
                    this.clearFilter();
                case 'filter':
                    this.updateTable();
            }
        });
    }

    save() {
        super.save(this.#getFilterUrl());
    }

    updateTable() {
        super.updateTable(this.#getFilterUrl());
    }

    clearFilter() {
        this.#filter.reset();
    }

    delete(id) {
        super.deleteRow(id);
    }

    #getFilterUrl = function() {
        return this.#ajaxUrl + 'filter' + this.#getFilterData();
    }

    #getFilterData = function() {
        let result = '?';
        this.#filter.querySelectorAll('input').forEach(element => {
            result += `${element.name}=${element.value}&`;
        });
        return result.substring(0, result.length - 1);
    }
}

let meals = new Meals({
    ajaxUrl: 'ajax/meals/',
    filter: document.getElementById('filter'),
    datatableApi: $('#datatable').DataTable({
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
});