class User extends AbstractCommon {
    constructor({ ajaxUrl, datatableApi }) {
        super({ ajaxUrl, datatableApi });

        document.querySelectorAll('.user-enabled').forEach(element => {
            element.addEventListener('click', e => {
                let target = e.target;
                let id = target.id;
                let checkbox = target.checked;
                let tr = target.closest('tr');

                $.ajax({
                    url: `${ajaxUrl}${id}?enabled=${checkbox}`,
                    type: 'POST',
                    error: error => {
                        target.checked = !checkbox;
                    }
                }).done(() => {
                    tr.style.color = checkbox ? 'blue' : 'red';
                    super.successNoty(`Setted user profile activity ${checkbox ? 'enabled' : 'disabled'}`);
                });
            });
        });
    }
}

let user;
user = new User({
        ajaxUrl: "ajax/admin/users/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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