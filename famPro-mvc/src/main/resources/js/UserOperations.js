function addUser() {
    event.preventDefault();
    let username = document.getElementById("nameAdd").value;
    let lastName = document.getElementById("lastNameAdd").value;
    let age = document.getElementById("ageAdd").value;
    let email = document.getElementById("emailAdd").value;
    let password = document.getElementById("passwordAdd").value;
    let roles = $("#rolesAdd").val();

    fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            "Access-Control-Allow-Origin": "*",
        },
        body: JSON.stringify({
            "username": username,
            "lastName": lastName,
            "age": age,
            "email": email,
            "password": password,
            "roles": roles
        }),
    })
        .then(() => {
            document.getElementById("userTableTab").click()
            document.userFormAdd.reset()
            document.getElementById("usersTable").innerHTML
            loadUserTable()
        });
}

function editUser(id) {
    let url = "http://localhost:8080/api/users/" + id;
    event.preventDefault();
    let username = document.getElementById("nameEdit" + id).value;
    let lastName = document.getElementById("lastNameEdit" + id).value;
    let age = document.getElementById("ageEdit" + id).value;
    let email = document.getElementById("emailEdit" + id).value;
    let password = document.getElementById("passwordEdit" + id).value;
    let roles = $(document.getElementById("rolesEdit" + id)).val();

    fetch(url, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            "Access-Control-Allow-Origin": "*",
        },
        body: JSON.stringify({
            "id": id,
            "username": username,
            "lastName": lastName,
            "age": age,
            "email": email,
            "password": password,
            "roles": roles
        }),
    })
        .then(() => {
            document.getElementById("userTableTab").click()
            document.getElementById("usersTable").innerHTML
            loadUserTable()
        });
}

function deleteUser(id) {
    let url = "http://localhost:8080/api/users/" + id;
    event.preventDefault();
    fetch(url, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json;charset=UTF-8",
            "Access-Control-Allow-Origin": "*",

        }
    })
        .then(() => {
            document.getElementById("userTableTab").click()
            document.getElementById("usersTable").innerHTML
            loadUserTable()
        });
}