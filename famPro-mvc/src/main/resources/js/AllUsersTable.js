loadUserTable();
function loadUserTable() {
    let temp1 = '';
    fetch("http://localhost:8080/api/users", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*"
        },
    }).then(response => response.json()).then(users => users.forEach(userTab => {
        temp1 += `
            <tr >
                <th style="font-weight:normal">${userTab.id} </th>
                <th style="font-weight:normal">${userTab.username} </th>
                <th style="font-weight:normal">${userTab.lastName}</th>
                <th style="font-weight:normal">${userTab.age} </th>
                <th style="font-weight:normal">${userTab.email} </th>
                <th style="font-weight:normal">${userTab.roles.map(role => role.role).join(' ')}</th>                 
                <th style="font-weight:normal">
                    <button type="button" class="btn btn-info" data-toggle="modal" data-target="#edit${userTab.id}">Edit
                    </button>
                                <!-- модальное окно Edit -->
                    <div class="modal fade" id="edit${userTab.id}" tabindex="-1" role="dialog"
                                             aria-labelledby="editModalWindowTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editModalLongTitle">Edit user</h5>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                <div class="modal-body">
                                    <div class="container-fluid row">
                                        <span class="col-3"></span>
                                        <span class="col-7">
                                        <form style="text-align: center" class="form-group">
                                            <label style="margin-bottom: 0" class="font-weight-bold" for="idEdit">ID</label>
                                            <input class="form-control" name="idEdit${userTab.id}" value="${userTab.id}" type="number" id="idEdit${userTab.id}" readonly/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="nameEdit">FirstName</label>
                                            <input class="form-control" name="usernameEdit${userTab.id}" value="${userTab.username}" type="text" id="nameEdit${userTab.id}"/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="lastNameEdit${userTab.id}">LastName</label>
                                            <input class="form-control" name="lastNameEdit${userTab.id}" value="${userTab.lastName}" type="text" id="lastNameEdit${userTab.id}"/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="ageEdit">Age</label>
                                            <input class="form-control" name="ageEdit${userTab.id}" value="${userTab.age}" type="number" id="ageEdit${userTab.id}"/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="emailEdit${userTab.id}">Email</label>
                                            <input class="form-control" name="emailEdit${userTab.id}" value="${userTab.email}" type="text" id="emailEdit${userTab.id}"/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="passwordEdit">password</label>
                                            <input class="form-control" name="passwordEdit${userTab.id}" type="text" id="passwordEdit${userTab.id}"/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="rolesEdit">Role</label>
                                            <select class="custom-select" id="rolesEdit${userTab.id}" size="2" name="rolesEdit${userTab.id}" multiple="multiple" > 
                                                <option value = 1>ADMIN</option>
                                                <option value = 2>USER</option>
                                            </select>
                                            <span class="col-2"></span>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                                </button>
                                                <button type="submit" class="btn btn-primary" data-dismiss="modal" onclick="editUser(${userTab.id})">Edit</button>
                                            </div>
                                        </form>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </th>
                <td>
                    <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#delete${userTab.id}">Delete
                    </button>
                                        <!-- модальное окно delete -->
                    <div class="modal fade" id="delete${userTab.id}" tabindex="-1" role="dialog"
                                             aria-labelledby="deleteModalWindowTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="DeleteModalLongTitle">Delete user</h5>
                                    <button type="button" class="close" data-dismiss="modal"
                                                                aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <div class="container-fluid row">
                                        <span class="col-3"></span>
                                        <span class="col-7">
                                        <form style="text-align: center" class="form-group"">
                                            <label style="margin-bottom: 0" class="font-weight-bold" for="idUser">ID</label>
                                            <input class="form-control" name="id" value="${userTab.id}" type="text" id="idUser" readonly/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="nameDelete">FirstName</label>
                                            <input class="form-control" value="${userTab.username}" type="text" id="nameDelete" readonly/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="lastNameDelete">LastName</label>
                                            <input class="form-control" value="${userTab.lastName}" type="text" id="lastNameDelete" readonly/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="ageDelete">Age</label>
                                            <input class="form-control" value="${userTab.age}" type="number" id="ageDelete" readonly/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="emailDelete">Email</label>
                                            <input class="form-control" value="${userTab.email}" type="text" id="emailDelete" readonly/>
                                            <label style="padding-top: 10px; margin-bottom: 0; font-weight: bold" for="rolesDelete">Role</label>
                                            <select class="custom-select" id="rolesDelete" size="2" name="rolesDelete" multiple="multiple" >
                                                <option value = 1>ADMIN</option>
                                                <option value = 2>USER</option>
                                            </select>
                                            <span class="col-2"></span>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                                </button>
                                                <button type="submit" class="btn btn-danger" data-dismiss="modal" onclick="deleteUser(${userTab.id})">Delete
                                                </button>
                                            </div>
                                        </form>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>
            `;
        document.getElementById("usersTable").innerHTML = `${temp1}`;
    }))

}
