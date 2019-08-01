'use strict';

class Meals {
    constructor({ ajaxUrl, createModal, table }) {
        this.ajaxUrl = ajaxUrl;
        this.createModal = createModal;
        this.table = table;

        this._addEventListeners();
    }

    create() {
        let data = this._getCreateData();
        fetch(this._getUrl(data), {method: 'POST'})
            .then(response => {
                if (!response.ok) throw new Error(response.status);
                this.updateTable();
                this._successNoty('Saved');
                this.closeModal();
                this._clearInputs();
            })
            .catch(error => this._errorNoty(error.message));
    }

    delete(id) {
        fetch(this.ajaxUrl + id, {method: 'DELETE'})
            .then(response => {
               this.updateTable();
            });
    }

    updateTable() {
        fetch(this.ajaxUrl)
            .then(response => response.json())
            .then(response => this._drawTable(response));
    }

    showModal() {
        $(this.createModal).modal();
    }

    closeModal() {
        $(this.createModal).modal('hide');
    }

    _drawTable(data) {
        this.table.innerHTML = data.map(meal => `
            <tr data-mealExcess="${meal.excess}" data-id="${meal.id}">
                <td>${meal.dateTime.replace('T', ' ')}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals/update?id=${meal.id}"><span class="fa fa-pencil"></span></a></td>
                <td><a><span class="fa fa-remove"></span></a></td>
            </tr>
        `).join('');
        this._addEventListeners();
    }

    _addEventListeners() {
        this.table.addEventListener('click', e => {
            if (!e.target.classList.contains('fa-remove')) return;
            e.preventDefault();
            let tr = e.target.closest('tr');
            let id = tr.dataset.id;

            if (confirm('Are you sure?'))
                this.delete(id);
        })
    }

    _clearInputs() {
        this.createModal.querySelector('#dateTime').value = "";
        this.createModal.querySelector('#description').value = "";
        this.createModal.querySelector('#calories').value = "";
    }

    _successNoty(msg) {
        this._closeNoty();
        new Noty({
            text: `<span class='fa fa-lg fa-check'></span> &nbsp;${msg}`,
            type: 'success',
            layout: "bottomRight",
            timeout: 1000
        }).show();
    }

    _errorNoty(msg) {
        this._closeNoty();
        new Noty({
            text: `<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: ${msg}`,
            type: 'error',
            layout: "bottomRight",
            timeout: 1000
        }).show();
    }

    _closeNoty() {
        if (this._noty) {
            this._noty.close();
            this._noty = undefined;
        }
    }

    _getCreateData() {
        return {
            dateTime: this.createModal.querySelector('#dateTime').value,
            description: this.createModal.querySelector('#description').value,
            calories: +this.createModal.querySelector('#calories').value
        };
    }

    _getUrl(data) {
        return `${this.ajaxUrl}?dateTime=${data.dateTime}&description=${data.description}&calories=${data.calories}`;
    }
}