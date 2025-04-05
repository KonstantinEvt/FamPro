function creatUserSSO() {
    loadStandardMainPanel()
    document.getElementById("taskPart").innerHTML=`<br> 
<div style="color: black;font-family: 'Times New Roman',serif; font-size: 12px; text-align: center">
создать нового пользователя получится только у Админа
</div>`;
    document.getElementById("resultPart").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px" >*** Adding user to SSO ***</div>
<div class="container-fluid row mh-100 no-gutters">
    <span class="col" style="width: 20%"></span>
    <span class="col" style="min-width: 200px">
        <form class="form-group" style="align-items: center; align-content: center; text-align: center;" id="userFormAdd">
            <label for="username" style="color: darkred">Username:</label>
            <input class="form-control" type="text" id="usernameAddSSO" name="username" required>
            <br>
            <label for="password" style="color: darkred">Password:</label>
            <input class="form-control" type="password" id="passwordAddSSO" name="password" required>
            <br>
            <button class="btn btn-outline-warning" type="button" onclick="submitAddForm()">Create User</button>
        </form>
    </span>    
    <span class="col" style="width: 35%"></span>     
</div>
<br>
<div style="font-size: larger; font-family: 'Times New Roman',serif">Result:</div>
<span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultListAddUser"></span>
    <br>`
}

function submitAddForm() {
    let form = document.getElementById('userFormAdd');
    let formData = {
        username: form.elements.usernameAddSSO.value,
        password: form.elements.passwordAddSSO.value
    };
    let jsonData = JSON.stringify(formData);
    fetch("/onlineUserAPI/create", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    }).then(async status => {
        document.getElementById("resultListAddUser").innerHTML = await status.text();
    });
}

function editUserSSO() {
    loadStandardMainPanel();
    document.getElementById("taskPart").innerHTML=`<br>
        <div style="color: black;font-family: 'Times New Roman',serif; font-size: 12px; text-align: center; padding-left: 10px">Связать регистрацию с записью в базе</div>
        <br>
        <form id="linkForm" class="form-group" style="align-items: center; align-content: center; text-align: center; padding-left: 10px">
            <label for="idLink" style="color: chocolate; ">Id человека в базе</label>
            <input class="form-control" type="text" id="idLink" name="idLink" required>
            <br>
            <button class="btn btn-outline-warning" type="button" onclick="linkUser()">Связать</button>
        </form>`;
    document.getElementById("resultPart").innerHTML = `
    <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px" >*** Editing user in SSO ***</div>
    <div class="container-fluid row mh-100 no-gutters">
    <span class="col" style="width: 20%"></span>
    <span class="col" style="min-width: 200px">
    <form class="form-group" style="align-items: center; align-content: center; text-align: center" id="userFormEditSSO">
        <label for="nickName" style="color: darkred">NickName:</label>
               <input class="form-control" type="text" id="nickNameEditSSO"  name="nickName" autocomplete="on" required>
                  <label for="email" style="color: darkred; padding-top: 5px">Email:</label>
                <input class="form-control form-control-border-color--warning" type="text" id="emailEditSSO" name="email" autocomplete="on" required>             
                                <label for="password" style="color: darkred; padding-top: 5px">Password:</label>
                                <input class="form-control" type="password" id="passwordEditSSO" name="password" required>
                                        <label for="firstNameEdit" style="color: darkred; padding-top: 5px">FirstName:</label>
                                        <input class="form-control" type="text" id="firstNameEditSSO" name="firstNameEdit" required>
                                                <label for="middleNameEdit" style="color: darkred; padding-top: 5px">MiddleName:</label>
                                                <input class="form-control" type="text" id="middleNameEditSSO" name="middleNameEdit" required>
                                                        <label for="lastNameEdit" style="color: darkred; padding-top: 5px">LastName:</label>
                                                        <input class="form-control" type="text" id="lastNameEditSSO" name="lastNameEdit"
                                                               required>
                                                                <label for="birthday" style="color: darkred; padding-top: 5px">Birthday:</label>
                                                                <input class="form-control" type="date" id="birthdayEditSSO"
                                                                       name="birthday" required>
                                                                    <br>
                                                                        <button class="btn btn-outline-warning" type="button"
                                                                                onclick="submitEditFormSSO()">Edit User
                                                                        </button>
    </form></span>
    <span class="col" style="width: 35%"></span>
    </div>
<br>
<div style="font-size: larger; font-family: 'Times New Roman',serif">Result:</div>
<span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultListEditUserSSO"></span>
    <br>
        <div>Если результатом будет "HHTP Conflict 409" - вероятние всего email уже зарезервирован в SSO</div>
        <div>Если поля будут не заполнены, то сохранятся прежние значения</div>  
        <div>Часть внесенных изменений (например, ваша роль в системе) произойдут с течением времени.</div>     
        <div>Для немедленного их применения выйдете из системы и войдите в нее.</div>`;
    fetch("/onlineUserAPI/info", {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
            // "Access-Control-Allow-Origin": "*"
        }
    }).then(user => user.json()).then(user => {
        document.getElementById("nickNameEditSSO").value=user.nickName;
        document.getElementById("emailEditSSO").value =user.email;
        document.getElementById("firstNameEditSSO").value =user.firstName;
        document.getElementById("middleNameEditSSO").value =user.middleName;
        document.getElementById("lastNameEditSSO").value =user.lastName;
        document.getElementById("birthdayEditSSO").value =user.birthday;
    });

}
function linkUser() {
    var form = document.getElementById('linkForm');
    var uri="/onlineUserAPI/link/"+form.elements.idLink.value
    fetch(uri, {
        method: 'GET',
        headers: {'Content-Type': 'application/json'}

    }).then(async status => {
        document.getElementById("footer-main").innerHTML = await status.text();
        loadOnlineUser();
        });

}
function submitEditFormSSO() {
    let form = document.getElementById('userFormEditSSO');
    let formData = {
        email: form.elements.emailEditSSO.value,
        password: form.elements.passwordEditSSO.value,
        firstName: form.elements.firstNameEditSSO.value,
        middleName: form.elements.middleNameEditSSO.value,
        lastName: form.elements.lastNameEditSSO.value,
        nickName: form.elements.nickNameEditSSO.value,
        birthday: form.elements.birthdayEditSSO.value
    };
    let jsonData = JSON.stringify(formData);
    fetch("/onlineUserAPI/edit", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    }).then(async status => {
        document.getElementById("resultListEditUserSSO").innerHTML = await status.text();
        document.getElementById("nav1").innerHTML = formData.nickName;
        document.getElementById("nav0").innerHTML = formData.firstName + " " + formData.lastName
    });

}