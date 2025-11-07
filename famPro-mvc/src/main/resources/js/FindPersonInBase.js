function getPersonFromBase(cause) {
    loadStandardMainPanel();
    loadOnlineUser();
    document.getElementById("taskPart").innerHTML = `
        <br>
        <div style="font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: chocolate">Поиск по:</div>
        <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="width:100%; margin-left: 5px; margin-right: -5px">
            <input type="radio" class="btn-check" name="search-option" onclick="getPersonFromBaseById(${cause})" id="search-radio1" autocomplete="off" checked>
            <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="search-radio1">ID</label>
            <input type="radio" class="btn-check" name="search-option" onclick="getPersonFromBaseByFio(${cause})" id="search-radio2" autocomplete="off"> 
            <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; font-size: 14px; color: darkred" for="search-radio2">ФИО/ДР</label>
            <input type="radio" class="btn-check" name="search-option" id="search-radio3" autocomplete="off">           
            <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; font-size: 14px; color: darkred" for="search-radio3">ФИО</label>
            <input type="radio" class="btn-check" name="search-option" id="search-radio4" autocomplete="off">           
            <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; font-size: 14px; color: darkred" for="search-radio4">Any</label>
        </div>
        <div id="link-block"></div>
`
    if (cause === 0) document.getElementById("link-block").innerHTML = `       
        <div style="margin-top:50px; color: black;font-family: 'Times New Roman',serif; font-size: 12px; text-align: center; padding-left: 10px">Связать регистрацию с записью в базе</div>
        <br>
        <form id="linkForm" class="form-group" style="align-items: center; align-content: center; text-align: center; padding-left: 10px">
            <label for="idLink" style="color: chocolate; ">Id человека в базе</label>
            <input class="form-control" type="text" id="idLink" name="idLink" required>
            <br>
            <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="linkUser()">Связать</button>
        </form>`

    document.getElementById("resultPart").innerHTML = `
        <div class="accordion" style=" --bs-accordion-active-bg: #eaecbd;  --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgb(234 212 101 / 25%); padding-top: 4px" id="accordionExample" >
        <div class="accordion-item">
            <h2 class="accordion-header">
               <button class="accordion-button" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSearchForm" aria-expanded="true" aria-controls="collapseSearchForm">
                Форма поиска
                </button>
            </h2>
            <div id="collapseSearchForm" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                <div class="accordion-body" style="padding-right: 5px;padding-left: 10px">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
                            <div id="formOfSearch"></div>
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" aria-expanded="false" aria-controls="collapseResultForm">
                    Результат поиска
                </button>
            </h2>
            <div id="collapseResultForm" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div id="resultFindingPerson">
                        <div class="container-fluid row mh-100 no-gutters">
                            <span style="text-align: center; font-size: 18px; color: darkred; font-family: 'Times New Roman',serif" >Запрос не выполнен</span> 
                        </div> 
                    </div> 
                </div>
            </div>
        </div>
        </div>
            `;
    getPersonFromBaseById(cause);
}

function getPersonFromBaseById(cause) {

    document.getElementById("formOfSearch").innerHTML = `                            
        <form class="form-group" style="margin:2px; text-align: center" id="findPersonById">
            <label for="idFindFM" style="color: chocolate; padding-top: 5px">Id:</label>
            <input class="form-control" type="text" id="idFindFM" name="idFindFM" autocomplete="on" required> 
            <br>
            <button class="btn btn-outline-warning" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" onclick="findPerson(0,${cause})">Найти человека</button>
        </form> 
`
}

function getPersonFromBaseByFio(cause) {
    document.getElementById("formOfSearch").innerHTML = `
        <form class="form-group" style="margin:5px; text-align: center" id="findPersonById">
                       
            <label for="firstNameFindFM" style="color: chocolate; padding-top: 5px">Имя:</label>
            <input class="form-control" type="text" id="firstNameFindFM" name="firstNameFindFM" autocomplete="on" required>
            
            <label for="middleNameFindFM" style="color: chocolate; padding-top: 5px">Отчество:</label>
            <input class="form-control" type="text" id="middleNameFindFM" name="middleNameFindFM" autocomplete="on" required> 
                       
            <label for="lastNameFindFM" style="color: chocolate; padding-top: 5px">Фимилия:</label>
            <input class="form-control" type="text" id="lastNameFindFM" name="lastNameFindFM" autocomplete="on" required>             

            <label for="birthdayFindFM" style="color: chocolate; padding-top: 5px">День рождения:</label>
            <input class="form-control" type="date" id="birthdayFindFM" name="birthdayFindFM" required>               

            <br>
            <button class="btn btn-outline-warning" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" onclick="findPerson(1,${cause})">Найти человека</button>
        </form>
 `;
}


async function findPerson(choice, cause) {
    const form = document.getElementById('findPersonById');
    let isContact = -1;
    let formData;
    let urlId = `/base/family_member/`;
    if (choice === 0) {
        formData = {
            id: form.elements.idFindFM.value
        };
        urlId += formData.id;
    }
    if (choice === 1) {
        formData = {
            firstName: form.elements.firstNameFindFM.value,
            middleName: form.elements.middleNameFindFM.value,
            lastName: form.elements.lastNameFindFM.value,
            birthday: form.elements.birthdayFindFM.value
        }

    }
    const jsonData = JSON.stringify(formData);

    await fetch(urlId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(tokenUser => tokenUser.json()).then(async familyMember => {
        paintingFindPerson();
        externId = familyMember.uuid.toString();
        tempPerson = familyMember;
        tempPerson.primePhotoImj = defaultPhotos.person;
        tempSecurity = ``;
        tempTextPhoto = ``
        let pictureUrl = ``
        let real = false;
        if (externId === ownLinkId) isContact = -2;
        else
            for (let z = 0; z < contacts.length; z++) {
                if (externId === contacts[z].externId) {
                    isContact = z;
                    tempPerson.primePhotoImj = contacts[z].imj;
                }
            }
        document.getElementById("resultListId").innerHTML = familyMember.id;

        if (isContact === -1 || isContact === -2) {
            tempTextPhoto = '<div class="spinner-border" role="status">\n' +
                '  <span class="visually-hidden">Loading...</span>\n' +
                '</div>';
            pictureUrl = URL.createObjectURL(defaultPhotos.person);
            if (isContact !== -2 && familyMember.secretLevelPhoto === "CLOSE") {
                tempTextPhoto = "Фото скрыто";
            } else if (!familyMember.primePhoto) {
                tempTextPhoto = "Фото отсутствует";
            } else real = true;
            document.getElementById("resultPrimePhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                        </div>
                    </div>`
            URL.revokeObjectURL(defaultPhotos.person);
        } else {
            let pictureUrl = URL.createObjectURL(contacts[isContact].imj);
            let textPhoto;
            if (!contacts[isContact].primePhoto && !contacts[isContact].contactPhoto) {
                textPhoto = "Фото отсутствует";
            } else textPhoto = ``;
            document.getElementById("resultPrimePhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${textPhoto}</div>
                        </div>                        
                    </div>`;
            URL.revokeObjectURL(contacts[isContact].imj);
        }
        document.getElementById("resultListFullName").innerHTML = familyMember.fullName;
        document.getElementById("resultListFather").innerHTML = familyMember.fatherInfo;
        document.getElementById("resultListMother").innerHTML = familyMember.motherInfo;
        if (familyMember.memberInfo.secretLevelPhone !== "CLOSE") document.getElementById("resultListPhone").innerHTML = familyMember.memberInfo.mainPhone; else document.getElementById("resultListPhone").innerHTML = "Телефон скрыт";
        if (familyMember.memberInfo.secretLevelAddress !== "CLOSE") document.getElementById("resultListAddress").innerHTML = familyMember.memberInfo.mainAddress; else document.getElementById("resultListAddress").innerHTML = "Адрес скрыт";
        if (familyMember.memberInfo.secretLevelEmail !== "CLOSE") document.getElementById("resultListEmail").innerHTML = familyMember.memberInfo.mainEmail; else document.getElementById("resultListEmail").innerHTML = "Email скрыт";
        switch (familyMember.checkStatus) {
            case "CHECKED": {
                document.getElementById("status-finding-person").innerHTML = "защита";
                document.getElementById("agree-finding-person").innerHTML = ``;
                break;
            }
            case "UNCHECKED": {
                document.getElementById("status-finding-person").innerHTML = "общий";
                document.getElementById("agree-finding-person").innerHTML = ``;
                break;
            }
            case "LINKED": {
                document.getElementById("status-finding-person").innerHTML = "связан";
                if (isContact === -1 && (document.getElementById("nav2").innerHTML === "LinkedUser" || document.getElementById("nav2").innerHTML === "VIP"))
                    document.getElementById("agree-finding-person").innerHTML = `<button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="addContactBySearch('${externId}')">Контакт</button>`;
                else if (isContact === -2) document.getElementById("agree-finding-person").innerHTML = `<div style="color: green;margin-right:-12px; font-size: 18px">It's you!</div>`
                break;
            }
            case "MODERATE": {
                document.getElementById("status-finding-person").innerHTML = "на модерации";
                document.getElementById("agree-finding-person").innerHTML = ``;
                break;
            }
            default: {
                document.getElementById("status-finding-person").innerHTML = ``;
                document.getElementById("agree-finding-person").innerHTML = ``;
                break;
            }
        }
        if (real && (isContact === -1 || isContact === -2)) {
            tempTextPhoto = ``;
            pictureUrl = '/file/get/' + familyMember.uuid;
            tempPerson.primePhotoImj = await loadPicture(pictureUrl);
            let tempPhoto = URL.createObjectURL(tempPerson.primePhotoImj);
            document.getElementById("resultPrimePhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${tempPhoto}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                        </div>
                    </div>`;
            URL.revokeObjectURL(tempPerson.primePhotoImj);
        }
        if (cause !== 0 && (familyMember.secretLevelEdit !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelPhone !== "CLOSE" || familyMember.secretLevelPhone !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelBiometric !== "CLOSE" || familyMember.secretLevelBiometric !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelEmail !== "CLOSE" || familyMember.secretLevelEmail !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelAddress !== "CLOSE" || familyMember.secretLevelAddress !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelBurial !== "CLOSE" || familyMember.secretLevelBurial !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelBirth !== "CLOSE" || familyMember.secretLevelBirth !== "UNDEFINED"
            || familyMember.memberInfo.secretLevelDescription !== "CLOSE" || familyMember.secretLevelDescription !== "UNDEFINED")) {

            tempSecurity = {
                owner: ownId,
                personId: familyMember.id,
                personUuid: familyMember.uuid,
                lastUpdate: familyMember.lastUpdate,
                infoExist: familyMember.memberInfo !== null,
                otherNamesExist: familyMember.otherNamesExist,
                secretLevelEdit: familyMember.secretLevelEdit,
                secretLevelPhone: familyMember.memberInfo.secretLevelPhone,
                secretLevelBiometric: familyMember.memberInfo.secretLevelBiometric,
                secretLevelDescription: familyMember.memberInfo.secretLevelDescription,
                secretLevelEmail: familyMember.memberInfo.secretLevelEmail,
                secretLevelAddress: familyMember.memberInfo.secretLevelAddress,
                secretLevelBurial: familyMember.memberInfo.secretLevelBurial,
                secretLevelBirth: familyMember.memberInfo.secretLevelBirth,
                secretLevelRemove: familyMember.secretLevelRemove,
                secretLevelMainInfo: familyMember.secretLevelMainInfo,
            }
            let tempPanel = `<form id="changePersonForm" class="form-group">
            <div style="text-align: center; font-size: 12px; color: chocolate; padding-right: 0; padding-left: 0; margin-top:50px">Операции:</div>
            <div class="btn-group-vertical" role="group" aria-label="Choose group" style="width:100%; margin-left: 5px; margin-right: -5px; margin-bottom: 5px;  text-align: center;">
                <input type="radio" class="btn-check" name="person-operation" id="person-get" value="extended" autocomplete="off" checked>
                <label class="btn btn-outline-warning" style="padding-right: 6px; padding-left: 6px; font-size: 14px; color: darkred" for="person-get">Info</label>`;
            if (familyMember.secretLevelEdit !== "CLOSE") {
                tempPanel += `<input type="radio" class="btn-check" name="person-operation" id="person-edit" value="edit"
                       autoComplete="off">
                    <label class="btn btn-outline-warning"
                           style="padding-right: 6px; padding-left: 6px; font-size: 14px; color: darkred"
                           for="person-edit">Изменить</label>`
            }
            if (familyMember.secretLevelRemove !== "CLOSE") {
                tempPanel += `<input type="radio" class="btn-check" name="person-operation" id="person-remove" value="remove" autocomplete="off"> 
                <label class="btn btn-outline-warning" style="padding-right: 6px; padding-left: 6px; font-size: 14px; color: darkred" for="person-remove">Удалить</label>`
            }
            tempPanel += `</div>
             <div class="btn-group-vertical" role="group" aria-label="Confirm group" style="width:100%; margin-left: 5px; margin-right: -5px;  text-align: center;">            
                <input type="radio" class="btn-check" name="person-operation" id="person-info" value="none" disabled autocomplete="off"> 
                <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; font-size: 10px; color: gray" for="person-info">для подтверждения введите Id человека в базе</label>       
                <input class="form-control" type="number" style="font-size:12px; text-align:center; padding-left:0; padding-right: 0; border-radius:0;" id="idChoose" name="idChoose" placeholder="введите id" required>                
                <button class="btn btn-outline-warning" style="padding-right: 6px; padding-left: 6px; color: darkred" type="button" onclick="checkAndSendStorageRequest()">OK</button>
            </div>
            </form>`
            document.getElementById("link-block").innerHTML = tempPanel;
        } else if (cause !== 0) document.getElementById("link-block").innerHTML = ``;
    }).catch(() => {
            document.getElementById("resultFindingPerson").innerHTML = `
                 <div class="container-fluid row mh-100 no-gutters">
                    <span style="text-align: center; font-size: 18px; color: darkred; font-family: 'Times New Roman',serif" > Человек с данным Id не найден</span> 
                </div>
                `;
            document.getElementById("link-block").innerHTML = ``
        }
    )
}

function checkAndSendStorageRequest() {
    const form = document.getElementById('changePersonForm');
    if (form.elements.idChoose.value !== document.getElementById('resultListId').innerHTML)
        document.getElementById("resultFindingPerson").innerHTML = "Указанный ID не соответствует найденной персоне. Выполните поиск заново";
    else {
        switch (form.elements.namedItem('person-operation').value) {
            case "extended" : {
                const jsonData = JSON.stringify(tempSecurity);
                fetch('/base/family_member/get/extended', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: jsonData,
                }).then(response => response.json()).then(async person => {
                    console.log(person);
                    if (tempPerson.memberInfo.photoBirthExist && tempPerson.memberInfo.secretLevelBirth !== "CLOSE") tempPerson.memberInfo.birthImj = await loadPicture("/file/get/birth/" + tempPerson.uuid);
                    if (tempPerson.memberInfo.photoBurialExist && tempPerson.memberInfo.secretLevelBurial !== "CLOSE") tempPerson.memberInfo.burialImj = await loadPicture("/file/get/burial/" + tempPerson.uuid);
                    tempPerson.memberInfo.emails = person.memberInfo.emails;
                    tempPerson.memberInfo.biometric = person.memberInfo.biometric;
                    tempPerson.memberInfo.description = person.memberInfo.description;
                    tempPerson.memberInfo.burial = person.memberInfo.burial;
                    tempPerson.memberInfo.birth = person.memberInfo.birth;
                    tempPerson.memberInfo.phones = person.memberInfo.phones;
                    tempPerson.memberInfo.addresses = person.memberInfo.addresses;
                    tempPerson.fioDtos = person.fioDtos;
                }).then(() => paintExtendedPerson());
                break;
            }
            case "edit" : {
                const jsonData = JSON.stringify(tempSecurity);
                fetch('/base/family_member/get/extended', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: jsonData,
                }).then(response => response.json()).then(async person => {
                    console.log(person);
                    if (tempPerson.memberInfo.photoBirthExist && tempPerson.memberInfo.secretLevelBirth !== "CLOSE") tempPerson.memberInfo.birthImj = await loadPicture("/file/get/birth/" + tempPerson.uuid);
                    if (tempPerson.memberInfo.photoBurialExist && tempPerson.memberInfo.secretLevelBurial !== "CLOSE") tempPerson.memberInfo.burialImj = await loadPicture("/file/get/burial/" + tempPerson.uuid);
                    tempPerson.memberInfo.emails = person.memberInfo.emails;
                    tempPerson.memberInfo.biometric = person.memberInfo.biometric;
                    tempPerson.memberInfo.description = person.memberInfo.description;
                    tempPerson.memberInfo.burial = person.memberInfo.burial;
                    tempPerson.memberInfo.birth = person.memberInfo.birth;
                    tempPerson.memberInfo.phones = person.memberInfo.phones;
                    tempPerson.memberInfo.addresses = person.memberInfo.addresses;
                    tempPerson.fioDtos = person.fioDtos;
                }).then(() =>  editPersonInBase());
                break;
            }
            case "remove" : {
                const jsonData = JSON.stringify(tempSecurity);
                fetch("/family_members/database/remove", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: jsonData,
                }).then(async status => {
                    document.getElementById("resultListCreateFM").innerHTML = await status.text();
                });
                break;
            }
            default:
                console.log("unknown choose")
        }
    }
}

function paintExtendedPerson() {
    console.log(tempPerson);
    let birthDay = new Date(tempPerson.birthday);

    let deathday;
    let personDescription = ``;
    // let tempBirth = ``;
    // let tempBirthInfo = ``;
    // let tempBurial = ``;
    // let tempBurialInfo = ``;
    // let tempBiometric = ``;
    // let tempOldFio = ``;
    // let tempAddress = ``;
    // let tempEmail = ``;
    // let tempPhone = ``;

    if (tempPerson.deathday === null && tempPerson.memberInfo.burial === null && tempPerson.memberInfo.secretLevelBurial !== "CLOSE") deathday = "";
    else if (tempPerson.deathday !== null) deathday = new Date(tempPerson.deathday); else deathday = infoAbsent;
    let age = getAge(birthDay, deathday);
    if (tempPerson.memberInfo.description !== null) personDescription = tempPerson.memberInfo.description; else personDescription = infoAbsent;

    let pictureUrl = URL.createObjectURL(tempPerson.primePhotoImj);
    document.getElementById("mainPanel").innerHTML = `
<div>
    <nav class="navbar navbar-nav bg-light" style="padding: 0;margin: 0">
        <div class="container-fluid row mh-100 no-gutters" >
            <span class="navbar-brand col-3" style="color: black;font-family: 'Times New Roman',serif; font-size: 16px; margin: 0;padding: 0">${tempPerson.firstName}</span>
            <span class="navbar-brand col-3" style="color: black;font-family: 'Times New Roman',serif; font-size: 16px; margin: 0;padding: 0">${tempPerson.middleName}</span>
            <span class="navbar-brand col-6" style="color: black;font-family: 'Times New Roman',serif; font-size: 16px; margin: 0;padding: 0">${tempPerson.lastName}</span>
        </div>
    </nav>
    
    <div class="container-fluid row mh-100 no-gutters">
        <span class="col" id="placePhoto" style="width: 100px">
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                        </div>
                    </div>
        </span>
        <span class="col"> 
            <div style="text-align: center; "> <span>Дата рождения: <span>${birthDay}</span></span></div>
            <div style="text-align: center; "> <span>Возраст: <span>${age}</span></span></div>
            <div style="text-align: center; "> Родители:</div>       
            <div style="text-align: center; "> <span>Отец: <span>${tempPerson.fatherInfo}</span></span></div>
            <div style="text-align: center; "> <span>Мать: <span>${tempPerson.motherInfo}</span></span></div>
        </span>            
    </div>
     
    <div id="personDescription">Краткое описание: ${personDescription}</div>
    <div id="otherNameExt"></div>
    <table>
        <tbody>
            <tr>
                <td>Biometric</td>
                <td id="biometricExt"></td>
            </tr>
            <tr>
                <td>Addresses</td>
                <td id="addressExt"></td>
            </tr>
            <tr>
                <td>Emails</td>
                <td id="emailsExt"></td>
            </tr>            
            <tr>
                <td>Phones</td>
                <td id="phonesExt"></td>
            </tr>            
            <tr>
                <td>Burial</td>
                <td id="burialExt"></td>
            </tr>       
            <tr>
                <td>Birth</td>
                <td id="birthExt"></td>
            </tr>
        </tbody>
    </table>
</div>`;
    URL.revokeObjectURL(tempPerson.primePhotoImj);
    if (tempPerson.otherNamesExist === true) paintOtherNames(); else document.getElementById("otherNameExt").innerHTML=`Другие имена: `+ infoAbsent;

    if (tempPerson.memberInfo.biometric !== null && tempPerson.memberInfo.biometric !== undefined) paintBiometric();
    else if (tempPerson.memberInfo.secretLevelBiometric === "CLOSE") document.getElementById("biometricExt").innerHTML = infoClosed;
    else document.getElementById("biometricExt").innerHTML = infoAbsent;

    if (tempPerson.memberInfo.phones !== null && tempPerson.memberInfo.phones !== undefined) paintPhones();
    else if (tempPerson.memberInfo.secretLevelPhone === "CLOSE") document.getElementById("phonesExt").innerHTML = infoClosed;
    else document.getElementById("phonesExt").innerHTML = infoAbsent;

    if (tempPerson.memberInfo.emails !== null && tempPerson.memberInfo.emails !== undefined) paintEmails();
    else if (tempPerson.memberInfo.secretLevelEmail === "CLOSE") document.getElementById("emailsExt").innerHTML = infoClosed;
    else document.getElementById("emailsExt").innerHTML = infoAbsent;

    if (tempPerson.memberInfo.addresses !== null && tempPerson.memberInfo.addresses !== undefined) paintAddresses();
    else if (tempPerson.memberInfo.secretLevelAddress === "CLOSE") document.getElementById("addressExt").innerHTML = infoClosed;
    else document.getElementById("addressExt").innerHTML = infoAbsent;

    if (tempPerson.memberInfo.burial !== null && tempPerson.memberInfo.burial !== undefined) paintBurial();
    else if (tempPerson.memberInfo.secretLevelBurial === "CLOSE") document.getElementById("burialExt").innerHTML = infoClosed;
    else document.getElementById("burialExt").innerHTML = infoAbsent;

    if (tempPerson.memberInfo.birth !== null && tempPerson.memberInfo.birth !== undefined) paintBirth();
    else if (tempPerson.memberInfo.secretLevelBirth === "CLOSE") document.getElementById("birthExt").innerHTML = infoClosed;
    else document.getElementById("birthExt").innerHTML = infoAbsent;
}

function paintBiometric() {
    let tempBio = ``;
    if (tempPerson.memberInfo.biometric.height !== null && tempPerson.memberInfo.biometric.height !== undefined) tempBio += `<div> Рост: ${tempPerson.memberInfo.biometric.height}</div>`;
    else tempBio += `<div> Рост неизвестен</div>`;
    if (tempPerson.memberInfo.biometric.weight !== null && tempPerson.memberInfo.biometric.weight !== undefined) tempBio += `<div> Вес: ${tempPerson.memberInfo.biometric.weight}</div>`;
    else tempBio += `<div> Вес неизвестен</div>`;
    if (tempPerson.memberInfo.biometric.shirtSize !== null && tempPerson.memberInfo.biometric.shirtSize !== undefined) tempBio += `<div> Размер одежды: ${tempPerson.memberInfo.biometric.shirtSize}</div>`;
    else tempBio += `<div> Размер одежды неизвестен</div>`;
    if (tempPerson.memberInfo.biometric.footSize !== null && tempPerson.memberInfo.biometric.footSize !== undefined) tempBio += `<div> Размер обуви: ${tempPerson.memberInfo.biometric.footSize}</div>`;
    else tempBio += `<div> Размер обуви неизвестен</div>`;
    if (tempPerson.memberInfo.biometric.eyesColor !== null && tempPerson.memberInfo.biometric.eyesColor !== undefined) tempBio += `<div> Цвет глаз: ${tempPerson.memberInfo.biometric.eyesColor}</div>`;
    else tempBio += `<div> Цвет глаз неизвестен</div>`;
    if (tempPerson.memberInfo.biometric.hairColor !== null && tempPerson.memberInfo.biometric.hairColor !== undefined) tempBio += `<div> Цвет волос: ${tempPerson.memberInfo.biometric.hairColor}</div>`;
    else tempBio += `<div> Цвет волос неизвестен</div>`;
    document.getElementById("biometricExt").innerHTML = tempBio;
}

function paintOtherNames() {
    let tempOtherNames = `<div>Старые имена или псевдонимы</div>`;
    for (let index = 0; index < tempPerson.fioDtos.length; ++index) {
        {
            tempOtherNames += `<div>${(tempPerson.fioDtos)[index].firstName} ${(tempPerson.fioDtos)[index].middleName} ${(tempPerson.fioDtos)[index].lastName}</div>`
        }
        document.getElementById("otherNameExt").innerHTML = tempOtherNames;
    }
}

function paintPhones() {
    let tempPhones = ``;
    for (let index = 0; index < tempPerson.memberInfo.phones.length; ++index) {
        {
            tempPhones += `<div>${(tempPerson.memberInfo.phones)[index].internName}</div>`
        }
        document.getElementById("phonesExt").innerHTML = tempPhones;
    }
}

function paintEmails() {
    let tempEmails = ``;
    for (let index = 0; index < tempPerson.memberInfo.emails.length; ++index) {
        {
            tempEmails += `<div>${(tempPerson.memberInfo.emails)[index].internName}</div>`
        }
        document.getElementById("emailsExt").innerHTML = tempEmails;
    }
}

function paintAddresses() {
    let tempAddresses = ``;
    for (let index = 0; index < tempPerson.memberInfo.addresses.length; ++index) {
        {
            tempAddresses += `<div>${(tempPerson.memberInfo.addresses)[index].internName}</div>`
        }
        document.getElementById("addressExt").innerHTML = tempAddresses;
    }
}

function paintBurial() {
    let tempBurial = ``;
    if (tempPerson.memberInfo.photoBurialExist) {
        let pictureUrl = URL.createObjectURL(tempPerson.memberInfo.burialImj);
        tempBurial += `
            <div class="card text-bg-dark" style="width: 250px">
                <img src="${pictureUrl}" class="card-img" alt="loading...">
                <div class="card-img-overlay">
                    <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                </div>
            </div>
        `;
        URL.revokeObjectURL(tempPerson.memberInfo.burialImj);
    }
    tempBurial+=`<div>${tempPerson.memberInfo.burial.internName}</div>`;
    document.getElementById("burialExt").innerHTML = tempBurial;
}

function paintBirth() {
    let tempBirth = ``;
    if (tempPerson.memberInfo.photoBirthExist) {
        let pictureUrl = URL.createObjectURL(tempPerson.memberInfo.birthImj);
        tempBirth += `
            <div class="card text-bg-dark" style="width: 250px">
                <img src="${pictureUrl}" class="card-img" alt="loading...">
                <div class="card-img-overlay">
                    <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                </div>
            </div>
        `;
        URL.revokeObjectURL(tempPerson.memberInfo.birthImj);
    }
    tempBirth+=`<div>${tempPerson.memberInfo.birth.internName}</div>`;
    document.getElementById("birthExt").innerHTML = tempBirth;
}

function paintingFindPerson() {
    document.getElementById("resultFindingPerson").innerHTML = `
        <div class="container-fluid row mh-100 no-gutters">
            <span class="col" style="width: 30%"> 
                <div style="margin-top:0; font-size: 16px;text-align: center; color: darkred;">Статус: </div>
                <div id="status-finding-person" style="font-size: 18px; text-align: center; color: green;font-family: 'Times New Roman',serif"></div>
            </span>
            <span class="col-4"> 
                <div style="font-size: 14px;text-align: center; color: darkred;">ID: </div>
                <div style="margin-bottom:6px; font-size: 18px;text-align: center; color: green;font-family: 'Times New Roman',serif" id="resultListId"><br></div>
            </span>
            <span class="col" style="width: 30%">
                <div id="agree-finding-person" style="margin-top:6px; margin-bottom: 10px; font-size: 14px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
            </span>
        </div>
        <div class="container-fluid row mh-100 no-gutters">
            <span class="col" style="width: 20%"></span>
            <span class="col-6" style="width: 250px">
                <div id="resultPrimePhoto" style="margin-bottom:2px; font-size: 18px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
            </span>
            <span class="col" style="width: 20%"></span>
        </div>
        <div class="container-fluid row mh-100 no-gutters">
             <span class="col" style="width: 10%"></span>
             <span class="col-10" style="min-width: 220px; padding-left: 0;padding-right: 0">
    <em>
        <div style="color: chocolate; font-size: 16px">Полное имя</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListFullName"><br></div>
        <div style="color: chocolate; font-size: 16px">Mother info:</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListMother"><br></div>

        <div style="color: chocolate; font-size: 16px">Father info:</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black;font-family: 'Times New Roman',serif" id="resultListFather"><br></div>

        <div style="color: chocolate; font-size: 16px">Phone:</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListPhone"><br></div>

        <div style="color: chocolate; font-size: 16px">Email:</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListEmail"><br></div>

        <div style="color: chocolate; font-size: 16px">Address:</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListAddress"><br></div>
    </em>
            </span>
            <span class="col" style="width: 10%"></span>
        </div>
    `
}

// function getPrimePhoto(uuid) {
//     document.getElementById("resultPrimePhoto").innerHTML = `
//     <img src="/file/get/${uuid}" width="200px" alt="Prime photo of Person" />
//         `
// }
