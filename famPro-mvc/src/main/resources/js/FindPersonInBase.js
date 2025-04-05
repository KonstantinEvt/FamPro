function getPersonFromBase(cause) {
    loadStandardMainPanel();
    loadOnlineUser();
    document.getElementById("taskPart").innerHTML = `
        <br>
        <div style="font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: chocolate">Поиск по:</div>
        <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="width:100%; margin-left: 5px; margin-right: -5px">
            <input type="radio" class="btn-check" name="search-option" onclick="getPersonFromBaseById('${cause}')" id="search-radio1" autocomplete="off" checked>
            <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="search-radio1">ID</label>
            <input type="radio" class="btn-check" name="search-option" onclick="getPersonFromBaseByFio('${cause}')" id="search-radio2" autocomplete="off"> 
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
    getPersonFromBaseById();
}

function getPersonFromBaseById(cause) {

    document.getElementById("formOfSearch").innerHTML = `                            
        <form class="form-group" style="margin:2px; text-align: center" id="findPersonById">
            <label for="idFindFM" style="color: chocolate; padding-top: 5px">Id:</label>
            <input class="form-control" type="text" id="idFindFM" name="idFindFM" autocomplete="on" required> 
            <br>
            <button class="btn btn-outline-warning" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" onclick="findPerson(0,'${cause}')">Найти человека</button>
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
            <button class="btn btn-outline-warning" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" onclick="findPerson(1,'${cause}')">Найти человека</button>
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
    }).then(tokenUser => tokenUser.json()).then(familyMember => {
        paintingFindPerson();
        externId = familyMember.uuid.toString();
        tempSecurity=``;
        let textPhoto = ``
        let pictureUrl = ``
        let real = false;
        if (externId === ownLinkId) isContact = -2;
        else
            for (let z = 0; z < contacts.length; z++) {
                if (externId === contacts[z].externId) {
                    isContact = z;
                }
            }
        document.getElementById("resultListId").innerHTML = familyMember.id;

        if (isContact === -1 || isContact === -2) {
            textPhoto = '<div class="spinner-border" role="status">\n' +
                '  <span class="visually-hidden">Loading...</span>\n' +
                '</div>';
            pictureUrl = URL.createObjectURL(defaultPhotos.person);
            if (isContact !== -2 && familyMember.secretLevelPhoto === "CLOSE") {
                textPhoto = "Фото скрыто";
            } else if (!familyMember.primePhoto) {
                textPhoto = "Фото отсутствует";
            } else real = true;
            document.getElementById("resultPrimePhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${textPhoto}</div>
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
            textPhoto = ``;
            pictureUrl = '/file/get/' + familyMember.uuid;
            document.getElementById("resultPrimePhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${textPhoto}</div>
                        </div>
                    </div>`

        }
        if (cause !== 0 && (familyMember.secretLevelEdit !== "CLOSE"
            || familyMember.memberInfo.secretLevelPhone !== "CLOSE"
            || familyMember.memberInfo.secretLevelBiometric !== "CLOSE"
            || familyMember.memberInfo.secretLevelEmail !== "CLOSE"
            || familyMember.memberInfo.secretLevelAddress !== "CLOSE"
            || familyMember.secretLevelBurial !== "CLOSE"
            || familyMember.secretLevelBirth !== "CLOSE")) {

            tempSecurity = {
                owner:ownId,
                personId:familyMember.id,
                personUuid:familyMember.uuid,
                infoExist: familyMember.memberInfo !== null,
                secretLevelEdit: familyMember.secretLevelEdit,
                secretLevelPhone: familyMember.memberInfo.secretLevelPhone,
                secretLevelBiometric: familyMember.memberInfo.secretLevelBiometric,
                secretLevelEmail: familyMember.memberInfo.secretLevelEmail,
                secretLevelAddress: familyMember.memberInfo.secretLevelAddress,
                secretLevelBurial: familyMember.secretLevelBurial,
                secretLevelBirth: familyMember.secretLevelBirth,
                secretLevelRemove: familyMember.secretLevelRemove,
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
        } else document.getElementById("link-block").innerHTML = ``;
    }).catch(() => {document.getElementById("resultFindingPerson").innerHTML = `
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
        let tempPerson = {
            tempPhoto: document.getElementById('resultPrimePhoto').innerHTML,
        }

        switch (form.elements.namedItem('person-operation').value) {
            case "extended" :
            case "edit" :
            {
                const jsonData = JSON.stringify(tempSecurity);
                fetch('/family_members/database/get/extended', {
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
