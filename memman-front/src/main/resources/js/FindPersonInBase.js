function menuFunc2200(cause) {
    loadStandardMainPanel();
    loadOnlineUser();
    let temp = `
        <div class="main_select" style="margin-top:24px;" >${langMatrix.get("criteria")}</div>
        <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="background-color:rgb(255,255,255,40%);margin-left: 7px; margin-right: 4px; min-width: 80%">`
    for (let i = 1; i < numOfPart.get(22); i++) {
        temp += `<input type="radio" class="btn-check" name="search-radio" onclick="menuFunc${2200 + i}(${cause})" id="search-radio${i}" autoComplete="off" >
        <label class="btn btn-outline-warning" style="padding-right: 3px; padding-left: 3px; text-align:center; font-size: 14px; color: darkred" for="search-radio${i}">${localMenu.get(2000 + i * 100)}             
        </label>`;
    }
    temp += `</div><div id="link-block"></div>`
    document.getElementById("taskPart").innerHTML = temp;
    document.getElementById(`search-radio1`).checked = true;
    document.getElementById(`search-radio3`).disabled = true;
    document.getElementById(`search-radio4`).disabled = true;
    if (cause === 0) document.getElementById("link-block").innerHTML = `       
        <div style="margin-top:50px; color: darkred;font-family: 'Times New Roman',serif; font-size: 12px; text-align: center; padding-left: 10px">${globalTexts.get(2153)}</div>
        <br>
        <form id="linkForm" class="form-group" style="align-items: center; align-content: center; text-align: center; padding-left: 10px">
            <label for="idLink" style="color: chocolate; ">${globalTexts.get(2152)}</label>
            <input class="form-control" type="text" id="idLink" name="idLink" required>
            <button class="btn btn-outline-warning" style="color: darkred; margin-top: 24px" type="button" onclick="linkUser()">${langMatrix.get("link")}</button>
        </form>`
    temp = (cause === 0) ? globalTexts.get(2154) : globalTexts.get(2146);
    document.getElementById("resultPart").innerHTML = `
        <div class="main_title" >${temp}</div>
        <div class="accordion" style=" --bs-accordion-active-bg: #eaecbd;  --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgb(234 212 101 / 25%); padding-top: 4px" id="accordionSearch" >
        <div class="accordion-item">
            <h2 class="accordion-header">
               <button class="accordion-button" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSearchForm" aria-expanded="true" aria-controls="collapseSearchForm">
                ${globalTexts.get(2147)}
                </button>
            </h2>
            <div id="collapseSearchForm" class="accordion-collapse collapse show" data-bs-parent="#accordionSearch">
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
                    ${globalTexts.get(2148)}
                </button>
            </h2>
            <div id="collapseResultForm" class="accordion-collapse collapse" data-bs-parent="#accordionSearch">
                <div class="accordion-body" style="padding: 3px">
                    <div id="resultFindingPerson">
                        <div class="container-fluid row mh-100 no-gutters">
                            <span style="text-align: center; font-size: 18px; color: darkred; font-family: 'Times New Roman',serif" >${globalTexts.get(2149)}</span> 
                        </div> 
                    </div> 
                </div>
            </div>
        </div>
        </div>`;
    menuFunc2201(cause);
}

function menuFunc2201(cause) {
    document.getElementById("formOfSearch").innerHTML = `                            
        <form class="form-group" style="margin:2px; text-align: center" id="findPersonById">
            <label for="idFindFM" style="color: chocolate; padding-top: 5px">${localMenu.get(2100)}</label>
            <input class="form-control" type="text" id="idFindFM" name="idFindFM" autocomplete="on" required> 
            <button class="btn btn-outline-warning" style="color: darkred; margin-top:24px" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" onclick="findPerson(0,${cause})">${globalTexts.get(2150)}</button>
        </form> 
`
}

function menuFunc2202(cause) {
    document.getElementById("formOfSearch").innerHTML = `
        <form class="form-group" style="margin:5px; text-align: center" id="findPersonById">                       
            <label for="firstNameFindFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("name")}</label>
            <input class="form-control" type="text" id="firstNameFindFM" name="firstNameFindFM" autocomplete="on" required>
            
            <label for="middleNameFindFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("patronymic")}</label>
            <input class="form-control" type="text" id="middleNameFindFM" name="middleNameFindFM" autocomplete="on" required> 
                       
            <label for="lastNameFindFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("surname")}</label>
            <input class="form-control" type="text" id="lastNameFindFM" name="lastNameFindFM" autocomplete="on" required>             

            <label for="birthdayFindFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("birthday")}</label>
            <input class="form-control" type="date" id="birthdayFindFM" name="birthdayFindFM" required>               

            <button class="btn btn-outline-warning" style="color: darkred; margin-top: 24px" type="button" data-bs-toggle="collapse" data-bs-target="#collapseResultForm" onclick="findPerson(1,${cause})">${globalTexts.get(2150)}</button>
        </form>
 `;
}

async function simplePaintingOfPerson(choice, cause, uuidIn) {
    loadStandardMainPanel();
    document.getElementById("taskPart").innerHTML = `
        <div id="link-block"></div>`
    let back = URL.createObjectURL(defaultPhotos.groundTrees)
    document.getElementById("resultPart").innerHTML = `
                    <div id="resultFindingPerson" style="background: url(${back}) repeat">
                        <div class="container-fluid row mh-100 no-gutters" >
                            <span style="text-align: center; padding:6px;font-size: 18px; color: darkred; font-family: 'Times New Roman',serif" >${globalTexts.get(2151)}</span> 
                        </div> 
                    </div> `
    findPerson(choice, cause, uuidIn).then(x => console.log(x));
    URL.revokeObjectURL(defaultPhotos.groundTrees);
}

async function menuFunc2300() {
    let members = await findCreatedMembers();
    if (members.length !== 0) {
        let temp = drawFindingMembers(members, "create");
        let back = URL.createObjectURL(defaultPhotos.groundTrees)
        document.getElementById("mainPanel").innerHTML = `<div class="main_title">${globalTexts.get(2400)}</div>`
        document.getElementById("mainPanel").innerHTML += `
        <div class="container-fluid row mh-100 no-gutters" style="margin-top: 10px" >
            <div class="col"><span class="main_under" style="width: 70%; text-align: left;font-size: 16px">
                            ${langMatrix.get("name")}&nbsp;${langMatrix.get("patronymic")}&nbsp;${langMatrix.get("surname")}</span></div>
            <div class="col-1" style="width: 70px"><span class="main_under" style="font-size: 16px">${langMatrix.get("birthday")}</span></div>
            <div class="col-1" style="width: 70px"><span class="main_under" style="font-size: 16px">${langMatrix.get("status")}</span></div>
            <div class="col-1" style="width: 2%"></div>
        </div><div style="height: 84vh; max-width:100%; overflow-x: auto; background: url('${back}') repeat">` + temp + `</div>`;
        URL.revokeObjectURL(defaultPhotos.groundTrees);
    } else {
        document.getElementById("mainPanel").innerHTML = `<div class="main_title" style="text-align: center">${globalTexts.get(2401)}</div>`;
    }
}

function drawFindingMembers(members, choice) {
    let rows;
    let temp;
    let status;
    let birthday;
    let rowAnc = ``;
    let rowRel = ``;
    let rowBro = ``;
    let rowGrandCh = ``;
    let rowGrand = ``;
    let rowChild = ``;
    let rowParent = ``;
    let rowOther = ``;
    let rowDes = ``;
    let rowPer = ``;
    for (let tempMember of members) {
        let memberInfoTemp = tempMember.firstName + ' ' + tempMember.middleName + ' ' + tempMember.lastName;
        if (choice === "create") tempMember.relationString = "<strong>" + langMatrix.get("created") + ":</strong> " + getDate(new Date(tempMember.createTime));
        status = langMatrix.get(tempMember.checkStatus);
        if (tempMember.birthday === null) birthday = langMatrix.get("hidden")
        else birthday = getDate(new Date(tempMember.birthday));
        temp = `<div class="container-fluid row mh-100 no-gutters" style="margin-top: 15px; background-color: rgb(200, 200, 180, 60%)">
        <span class="navbar-brand col-9 "
              style="color: darkred;font-family: 'Times New Roman',serif; font-size: 16px; width: 70%;margin: 5px 0 5px 0;padding: 0;font-style: italic">${tempMember.firstName}&nbsp;
                            ${tempMember.middleName}&nbsp;                            
                            ${tempMember.lastName}</span>
                            <span class="col"></span>
                            </div>
        <div class="container-fluid row mh-100 no-gutters" style="background-color: rgb(230, 230, 200, 40%); margin-left: -12px;padding-left: 12px"> 
                <span class="navbar-brand col" style="text-align:left; padding-left:0;color: black;font-family: 'Times New Roman',serif; font-size: 14px;font-style: italic">${tempMember.relationString}</span>         
                <span class="navbar-brand col-2"
                    style="padding:0; color: black;font-family: 'Times New Roman',serif; font-size: 14px; margin-right: 10px; width: 70px">${birthday}</span>
                <span class="navbar-brand col-1" style="color: black;font-family: 'Times New Roman',serif; font-size: 14px;width: 70px">${status}</span>
        </div>`
        if (choice === "create") temp += `
        <div class="container-fluid row mh-100 no-gutters" style="background-color: rgb(230, 230, 200, 40%); margin-left: -12px;padding-left: 12px"> 
            <div style="color: black;font-family: 'Times New Roman',serif; padding-left:0; font-size: 14px;font-style: italic;text-align: left;"><strong>${langMatrix.get("changed")}:</strong> ${getDate(new Date(tempMember.lastUpdate))}</div></div>`
        temp += `
        <div style="color: green;margin: 0;font-size: 14px; text-align: center">${globalTexts.get(2155)}</div>
                <div class="container-fluid row mh-100 no-gutters" style="margin-bottom: 5px; text-align: center">
                    <span class="btn-group-horizontal" role="group"  style="width:100%; margin: 0; padding:0">                     
                        <button class="btn btn-outline-warning" style="color: darkred; font-size: 12px " type="button" onclick="simplePaintingOfPerson(2,1,'${tempMember.uuid}')">${langMatrix.get("find")}</button>
                        <button class="btn btn-outline-warning" style="color: darkred; font-size: 12px" type="button" onclick="getGeneticTree('${tempMember.uuid}', 'GENETIC_TREE', '${memberInfoTemp}')">${langMatrix.get("tree")}</button>
                        <button class="btn btn-outline-warning" style="color: darkred; font-size: 12px" type="button" onclick="getGeneticTree('${tempMember.uuid}', 'ANCESTOR','${memberInfoTemp}')">${langMatrix.get("ancestors")}</button>
                        <button class="btn btn-outline-warning" style="color: darkred; font-size: 12px" type="button" onclick="getGeneticTree('${tempMember.uuid}', 'STRAIGHT_BLOOD', '${memberInfoTemp}')">${langMatrix.get("descendants")}</button></span>
                             </div>`
        switch (tempMember.relationString) {
            case "Предок":
                rowAnc += temp;
                break;
            case "Дедушка":
            case "Бабушка" :
                rowGrand += temp;
                break;
            case "Мать":
            case "Отец"   :
                rowParent += temp;
                break;
            case "Брат":
            case "Сестра":
                rowBro += temp;
                break;
            case "Сын":
            case "Дочь"  :
                rowChild += temp;
                break;
            case "Внук":
            case "Внучка":
                rowGrandCh += temp;
                break;
            case "Потомок":
                rowDes += temp;
                break;
            case "Родственник"  :
                rowRel += temp;
                break;
            case "Персона":
                rowPer += temp;
                break;
            default:
                rowOther += temp;
        }
    }
    rows = rowAnc + rowGrand + rowParent + rowBro + rowPer + rowChild + rowGrandCh + rowDes + rowOther + rowRel;
    return rows;
}

async function getGeneticTree(member, choice, memberInfo) {
    let members = await findGeneticTree(member, choice);
    let title;
    let text;
    if (choice === 'GENETIC_TREE') {
        title = globalTexts.get(2402);
        text = globalTexts.get(2403)
    } else if (choice === 'ANCESTOR') {
        title = globalTexts.get(2404);
        text = globalTexts.get(2405)
    } else {
        title = globalTexts.get(2406);
        text = globalTexts.get(2405)
    }
    if (members.length !== 0) {
        let back = URL.createObjectURL(defaultPhotos.groundTrees)
        let temp = drawFindingMembers(members, "tree");
        document.getElementById("mainPanel").innerHTML = `<div class="main_title">${title}</div>
            <div class="main_under" style="font-style: italic">${memberInfo}</div>`;

        document.getElementById("mainPanel").innerHTML += `
        <div class="container-fluid row mh-100 no-gutters" style="margin-top: 10px" >
            <div class="col"><span class="main_under" style="width: 70%; text-align: left;font-size: 16px">
                            ${langMatrix.get("name")}&nbsp;${langMatrix.get("patronymic")}&nbsp;${langMatrix.get("surname")}</span></div>
            <div class="col-1" style="width: 70px"><span class="main_under" style="font-size: 16px">${langMatrix.get("birthday")}</span></div>
            <div class="col-1" style="width: 70px"><span class="main_under" style="font-size: 16px">${langMatrix.get("status")}</span></div>
            <div class="col-1" style="width: 2%"></div>
        </div><div style="height: 84vh; max-width:100%; overflow-x: auto; background: url('${back}') repeat">` + temp + `</div>`;
        URL.revokeObjectURL(defaultPhotos.groundTrees);
    } else {
        document.getElementById("mainPanel").innerHTML = `<div class="main_title">${title}</div>
            <div class="main_under" style="font-style: italic">${memberInfo}</div>
            <div class="main_title" style="color: darkred;">${text}</div>`;
    }
}

async function findCreatedMembers() {
    let members = [];
    let url = endPoints.get("base") + `/getFirstCreator/` + currentLang.toUpperCase();
    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(setUsers => setUsers.json()).then(familyMembers => {
        members = familyMembers;
    })
    return members;
}

async function findGeneticTree(member, choice) {
    let members = [];
    let url = endPoints.get("base") + `/getFamilyTree/${member}/${choice}/${currentLang.toUpperCase()}`;
    await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(setUsers => setUsers.json()).then(familyMembers => {
        members = familyMembers;
    })
    return members;
}

async function findPerson(choice, cause, externId) {
    const form = document.getElementById('findPersonById');
    let isContact = -1;
    let formData;
    let urlId = endPoints.get("base") + `/`;
    if (choice === 0) {
        formData = {
            id: form.elements.idFindFM.value
        };
        urlId += formData.id + "/" + currentLang.toUpperCase();
    }
    if (choice === 1) {
        formData = {
            firstName: form.elements.firstNameFindFM.value,
            middleName: form.elements.middleNameFindFM.value,
            lastName: form.elements.lastNameFindFM.value,
            birthday: form.elements.birthdayFindFM.value,
            localisation: currentLang.toUpperCase()
        }
    }
    if (choice === 2 && externId !== undefined && externId !== null) {
        formData = {uuid: externId, localisation: currentLang.toUpperCase()}
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
        externId = familyMember.creator;
        tempPerson = familyMember;
        console.log(tempPerson);
        tempPerson.primePhotoImj = defaultPhotos.person;
        tempSecurity = ``;
        tempTextPhoto = ``
        let pictureUrl = ``
        if (contacts.get(externId) !== undefined || externId === mainUser.ownId)
            if (externId === mainUser.ownId) isContact = -2; else isContact = externId;
        document.getElementById("resultListId").innerHTML = familyMember.id;
        let real = false;
        tempTextPhoto = '<div class="spinner-border" role="status"> <span class="visually-hidden">Loading...</span></div>';
        pictureUrl = URL.createObjectURL(defaultPhotos.person);
        if (isContact !== -2 && familyMember.secretLevelPhoto === "CLOSE") {
            tempTextPhoto = globalTexts.get(8);
        } else if (!familyMember.primePhoto) {
            tempTextPhoto = globalTexts.get(3);
        } else real = true;
        document.getElementById("result_prime").innerHTML = `
                    <div class="card text-bg-dark" style="display: block; margin: 0 auto; width: 70%;min-width:200px; max-width: 350px">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" id="textOverPrime" style="padding-top: 35%">${tempTextPhoto}</div>
                        </div>
                    </div>`
        URL.revokeObjectURL(defaultPhotos.person);

        let motherInfo = familyMember.motherInfo.split(".");
        let fatherInfo = familyMember.fatherInfo.split(".");
        let fullName = familyMember.fullName.split(".");
        document.getElementById("resultListFullName").innerHTML = `<div style="font-size: 16px">${fullName[0]}</div>` + `<div>${fullName[1]}</div>`;
        document.getElementById("resultListMother").innerHTML = `<div style="font-size: 16px">${motherInfo[0]}</div>`;
        document.getElementById("resultListFather").innerHTML = `<div style="font-size: 16px">${fatherInfo[0]}</div>`;
        if (motherInfo.length > 1) document.getElementById("resultListMother").innerHTML += `<div>${motherInfo[1]}</div>`;
        if (fatherInfo.length > 1) document.getElementById("resultListFather").innerHTML += `<div>${fatherInfo[1]}</div>`;

        if (familyMember.memberInfo.secretLevelPhone !== "CLOSE") document.getElementById("resultListPhone").innerHTML = familyMember.memberInfo.mainPhone; else document.getElementById("resultListPhone").innerHTML = langMatrix.get("phone") + "&nbps;" + langMatrix.get("hidden");
        if (familyMember.memberInfo.secretLevelAddress !== "CLOSE") document.getElementById("resultListAddress").innerHTML = familyMember.memberInfo.mainAddress; else document.getElementById("resultListAddress").innerHTML = langMatrix.get("address") + "&nbps;" + langMatrix.get("hidden");
        if (familyMember.memberInfo.secretLevelEmail !== "CLOSE") document.getElementById("resultListEmail").innerHTML = familyMember.memberInfo.mainEmail; else document.getElementById("resultListEmail").innerHTML = langMatrix.get("email") + "&nbps;" + langMatrix.get("hidden");
        document.getElementById("status-finding-person").innerHTML = langMatrix.get(familyMember.checkStatus);
        if (familyMember.checkStatus === `LINKED`) if (isContact === -1 && (mainUser.role !== "SIMPLE_USER" && mainUser.role !== "BASE_USER"))
            document.getElementById("agree-finding-person").innerHTML += `<div><button class="btn btn-outline-warning" style="color: darkred;padding:3px; width:90px; font-size:14px" type="button" onclick="addContactBySearch('${familyMember.creator}')">${langMatrix.get("contact")}</button>
                <br><button class="btn btn-outline-warning" style="color: darkred;padding:3px; margin-top:6px;font-size:14px; width:90px;" type="button" onclick="menuFunc3100('${familyMember.creator}', '${fullName[0]}')">${langMatrix.get("message")}</button></div>`;
        else if (isContact === -2) document.getElementById("agree-finding-person").innerHTML += `<div style="color: green;margin-right:-12px; font-size: 18px">${globalTexts.get(2157)}</div>`;
        else if (isContact === -1) document.getElementById("agree-finding-person").innerHTML += `<button class="btn btn-outline-warning" style="color: darkred;padding:3px; margin-top:6px;font-size:14px" type="button" onclick="menuFunc3100('${familyMember.creator}', '${fullName[0]}')">${langMatrix.get("message")}</button></div>`;
        else document.getElementById("agree-finding-person").innerHTML += globalTexts.get(2159);
        if (real) {
            pictureUrl = endPoints.get("filePrivate") + '/get/prime/' + familyMember.uuid;
            tempPerson.primePhotoImj = await loadPicture(pictureUrl).then((pic) => {
                tempTextPhoto = "";
                return pic;
            }).catch(() => {
                tempPerson.primePhotoImj = defaultPhotos.person;
                tempTextPhoto = globalTexts.get(9)
            })
            if (tempPerson.primePhotoImj === undefined) {
                tempPerson.primePhotoImj = defaultPhotos.person;
                tempTextPhoto = globalTexts.get(9);
            }
            let tempPhoto = URL.createObjectURL(tempPerson.primePhotoImj);
            document.getElementById("result_prime").innerHTML = `<div class="card text-bg-dark" style="display: block; margin: 0 auto; width: 70%;min-width:200px; max-width: 350px">
                        <img src="${tempPhoto}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                        </div>
                    </div>`;
            URL.revokeObjectURL(tempPerson.primePhotoImj);
        }
        if (cause !== 0 && (familyMember.secretLevelEdit !== "UNDEFINED"
            || (familyMember.memberInfo.secretLevelPhone !== "CLOSE" && familyMember.secretLevelPhone !== "UNDEFINED")
            || (familyMember.memberInfo.secretLevelBiometric !== "CLOSE" && familyMember.secretLevelBiometric !== "UNDEFINED")
            || (familyMember.memberInfo.secretLevelEmail !== "CLOSE" && familyMember.secretLevelEmail !== "UNDEFINED")
            || (familyMember.memberInfo.secretLevelAddress !== "CLOSE" && familyMember.secretLevelAddress !== "UNDEFINED")
            || (familyMember.memberInfo.secretLevelBurial !== "CLOSE" && familyMember.secretLevelBurial !== "UNDEFINED")
            || (familyMember.memberInfo.secretLevelBirth !== "CLOSE" && familyMember.secretLevelBirth !== "UNDEFINED")
            || (familyMember.memberInfo.secretLevelDescription !== "CLOSE" && familyMember.secretLevelDescription !== "UNDEFINED"))) {

            tempSecurity = {
                owner: mainUser.ownId,
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
                localisation: currentLang.toUpperCase()
            }
            let tempPanel = `<form id="changePersonForm" class="form-group">
                <div class="main_select" style="padding:0 0 0 5px; margin-top:20px">${globalTexts.get(2155)}</div>
                <div class="btn-group-vertical" role="group" aria-label="Choose group" style="background-color:rgb(255,255,255,40%); width:85%; margin: 6px 0 0 3%;  text-align: center;">
                <input type="radio" class="btn-check" name="person-operation" id="person-get" value="extended" autocomplete="off" checked>
                <label class="btn btn-outline-warning" style="padding:3px; font-size: 14px; color: darkred" for="person-get">${langMatrix.get("info")}</label>`;
            if (familyMember.secretLevelEdit !== "CLOSE") {
                tempPanel += `<input type="radio" class="btn-check" name="person-operation" id="person-edit" value="edit"
                       autoComplete="off">
                    <label class="btn btn-outline-warning"
                           style="padding:3px; font-size: 14px; color: darkred"
                           for="person-edit">${langMatrix.get("edit")}</label>`
            }
            if (familyMember.secretLevelRemove !== "CLOSE") {
                tempPanel += `<input type="radio" class="btn-check" name="person-operation" id="person-remove" value="remove" autocomplete="off"> 
                <label class="btn btn-outline-warning" style="; padding:3px; font-size: 14px; color: darkred" for="person-remove">${langMatrix.get("remove")}</label>`
            }
            tempPanel += `</div>
                             <button class="btn btn-outline-warning" style="background-color:rgb(255,255,255,40%); padding: 3px; margin-top: 6px; color: darkred" type="button" onclick="checkAndSendStorageRequest()">${langMatrix.get("ok")}</button>
                            </form>`
            if (familyMember.secretLevelBirthday !== "UNDEFINED" && familyMember.secretLevelBirthday !== "CLOSE") {
                let tempMemberInfo = familyMember.firstName + ' ' + familyMember.middleName + ' ' + familyMember.lastName;
                tempPanel += `<div class="main_select" style="padding:0 0 0 5px; margin-top:20px">${langMatrix.get("relations")}</div>
                    <div class="btn-group-vertical" role="group"  style="background-color:rgb(255,255,255,40%); width:85%; margin: 6px 0 0 3%; text-align: center;">
                        <button class="btn btn-outline-warning" style="color: darkred;padding:3px; font-size:12px" type="button"
                            onclick="getGeneticTree('${familyMember.uuid}', 'GENETIC_TREE', '${tempMemberInfo}')">${langMatrix.get("tree")}</button>
                        <button class="btn btn-outline-warning" style="color: darkred; padding:3px; font-size:12px" type="button"
                            onclick="getGeneticTree('${familyMember.uuid}', 'ANCESTOR', '${tempMemberInfo}')">${langMatrix.get("ancestors")}</button>
                        <button class="btn btn-outline-warning" style="color: darkred;padding:3px; font-size:12px" type="button"
                            onclick="getGeneticTree('${familyMember.uuid}', 'STRAIGHT_BLOOD', '${tempMemberInfo}')">${langMatrix.get("descendants")}</button></div>`
            }
            document.getElementById("link-block").innerHTML = tempPanel;
        } else if (cause !== 0) document.getElementById("link-block").innerHTML = ``;
    }).catch(() => {
            document.getElementById("resultFindingPerson").innerHTML = `
                 <div class="container-fluid row mh-100 no-gutters">
                    <span style="text-align: center; font-size: 18px; color: darkred; font-family: 'Times New Roman',serif" > ${globalTexts.get(2156)}</span> 
                </div>
                `;
            document.getElementById("link-block").innerHTML = ``
        }
    )
}

function checkAndSendStorageRequest() {
    const form = document.getElementById('changePersonForm');
    // if (form.elements.idChoose.value !== document.getElementById('resultListId').innerHTML)
    //     document.getElementById("resultFindingPerson").innerHTML = "Указанный ID не соответствует найденной персоне. Введите правильный ID";
    // else {
    switch (form.elements.namedItem('person-operation').value) {
        case "extended" : {
            const jsonData = JSON.stringify(tempSecurity);
            fetch(endPoints.get("base") + '/get/extended', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData,
            }).then(response => response.json()).then(async person => {
                await assignPersonInfoToPerson(person);
            }).then(() => paintExtendedPerson());
            break;
        }
        case "edit" : {
            const jsonData = JSON.stringify(tempSecurity);
            fetch(endPoints.get("base") + '/get/extended', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: jsonData,
            }).then(response => response.json()).then(async person => {
                await assignPersonInfoToPerson(person);
            }).then(() => editPerson());
            break;
        }
        case "remove" : {
            loadMenuRemove();
            break;
        }
        default:
            console.log("unknown choose")
    }
    // }
}

function paintExtendedPerson() {
    console.log(tempPerson);
    let birthDay = new Date(tempPerson.birthday);

    let deathday;
    let personDescription = ``;

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
    if (tempPerson.otherNamesExist === true) paintOtherNames(); else document.getElementById("otherNameExt").innerHTML = `Другие имена: ` + infoAbsent;

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

    if ((tempPerson.memberInfo.burial !== null && tempPerson.memberInfo.burial !== undefined) || (tempPerson.memberInfo.photoBurialExist && tempPerson.memberInfo.secretLevelBurial !== "CLOSE")) paintBurial();
    else if (tempPerson.memberInfo.secretLevelBurial === "CLOSE") document.getElementById("burialExt").innerHTML = infoClosed;
    else document.getElementById("burialExt").innerHTML = infoAbsent;

    if ((tempPerson.memberInfo.birth !== null && tempPerson.memberInfo.birth !== undefined) || (tempPerson.memberInfo.photoBirthExist && tempPerson.memberInfo.secretLevelBirth !== "CLOSE")) paintBirth();
    else if (tempPerson.memberInfo.secretLevelBirth === "CLOSE") document.getElementById("birthExt").innerHTML = infoClosed;
    else document.getElementById("birthExt").innerHTML = infoAbsent;
}

function paintBiometric() {
    let tempBio = ``;
    if (tempPerson.memberInfo.biometric[0].age !== null && tempPerson.memberInfo.biometric[0].age !== undefined) tempBio += `<div> Возраст: ${tempPerson.memberInfo.biometric[0].age}</div>`;
    else tempBio += `<div> Возраст неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].height !== null && tempPerson.memberInfo.biometric[0].height !== undefined) tempBio += `<div> Рост: ${tempPerson.memberInfo.biometric[0].height}</div>`;
    else tempBio += `<div> Рост неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].weight !== null && tempPerson.memberInfo.biometric[0].weight !== undefined) tempBio += `<div> Вес: ${tempPerson.memberInfo.biometric[0].weight}</div>`;
    else tempBio += `<div> Вес неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].shirtSize !== null && tempPerson.memberInfo.biometric[0].shirtSize !== undefined) tempBio += `<div> Размер одежды: ${tempPerson.memberInfo.biometric[0].shirtSize}</div>`;
    else tempBio += `<div> Размер одежды неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].footSize !== null && tempPerson.memberInfo.biometric[0].footSize !== undefined) tempBio += `<div> Размер обуви: ${tempPerson.memberInfo.biometric[0].footSize}</div>`;
    else tempBio += `<div> Размер обуви неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].eyesColor !== null && tempPerson.memberInfo.biometric[0].eyesColor !== undefined) tempBio += `<div> Цвет глаз: ${tempPerson.memberInfo.biometric[0].eyesColor}</div>`;
    else tempBio += `<div> Цвет глаз неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].hairColor !== null && tempPerson.memberInfo.biometric[0].hairColor !== undefined) tempBio += `<div> Цвет волос: ${tempPerson.memberInfo.biometric[0].hairColor}</div>`;
    else tempBio += `<div> Цвет волос неизвестен</div>`;
    if (tempPerson.memberInfo.biometric[0].description !== null && tempPerson.memberInfo.biometric[0].description !== undefined) tempBio += `<div> Описание: ${tempPerson.memberInfo.biometric[0].description}</div>`;
    else tempBio += `<div> Описание отсутствует</div>`;
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
    if (tempPerson.memberInfo.burial !== null && tempPerson.memberInfo.burial.internName !== null) {
        tempBurial += `<div>${tempPerson.memberInfo.burial.internName}</div>`;
    }
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
    if (tempPerson.memberInfo.birth !== null && tempPerson.memberInfo.birth.internName !== null) {
        tempBirth += `<div>${tempPerson.memberInfo.birth.internName}</div>`;
    }
    document.getElementById("birthExt").innerHTML = tempBirth;

}

function paintingFindPerson() {
    document.getElementById("resultFindingPerson").innerHTML = `
        <div class="container-fluid row mh-100 no-gutters">
            <span class="col" style="width: 30%; text-align: center"> 
                <div style="margin-top:6px; font-size: 18px; color: darkred;">${langMatrix.get("status")}</div>
                <div id="status-finding-person" style="font-size: 18px; color: green;font-family: 'Times New Roman',serif"></div>
            </span>
            <span class="col-4"> 
                <div style="font-size: 18px;text-align: center; color: darkred;">ID: </div>
                <div style="margin-bottom:6px; font-size: 18px;text-align: center; color: green;font-family: 'Times New Roman',serif" id="resultListId"><br></div>
            </span>
            <span class="col" style="width: 30%;">
                <div id="agree-finding-person" style="margin-top:6px; margin-bottom: 10px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
            </span>
        </div>
         <div id="result_prime" style="margin-bottom:2px; font-size: 18px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
    <em>
        <div style="color: chocolate; font-size: 16px">${langMatrix.get("fullName")}</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListFullName"><br></div>
        <div style="color: chocolate; font-size: 16px">${langMatrix.get("mother")}</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListMother"><br></div>

        <div style="color: chocolate; font-size: 16px">${langMatrix.get("father")}</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black;font-family: 'Times New Roman',serif" id="resultListFather"><br></div>

        <div style="color: chocolate; font-size: 16px">${langMatrix.get("phone")}</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListPhone"><br></div>

        <div style="color: chocolate; font-size: 16px">${langMatrix.get("email")}</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListEmail"><br></div>

        <div style="color: chocolate; font-size: 16px">${langMatrix.get("address")}</div>
        <div style="margin-bottom:2px; font-size: 14px; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListAddress"><br></div>
    </em>
    `
}