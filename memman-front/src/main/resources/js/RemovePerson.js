function loadMenuRemove() {
    let memberInfo = tempPerson.firstName + ' ' + tempPerson.middleName + ' ' + tempPerson.lastName;
    document.getElementById("mainPanel").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Remove Person ***</div>
        <div style="color: green;font-family: 'Times New Roman',serif; font-size: 20px;text-align: center"><em>${memberInfo}</em></div>
  <div style="color: green;font-family: 'Times New Roman',serif; font-size: 20px;text-align: center">
    <span style="color: chocolate;">Рожденный:&nbsp;</span><em>${tempPerson.birthday}</em>
  </div>
  <div id="chooseRemoveMode">        
        <button class="btn btn-outline-warning" id="fullButton" type="button" style="margin-top: 5px" data-bs-toggle="modal" data-bs-target="#selectToFullRemove">Удалить полностью</button>
        <button class="btn btn-outline-warning" id="partButton" type="button" style="margin-top: 5px" onclick="loadSelectMenuRemove()">Выбрать удаляемый контент</button>
         </div>    
<div class="modal fade" id="selectToFullRemove" tabindex="-1" aria-labelledby="attentionToRemove" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <div class="modal-title" id="attentionToRemove" style="color: darkred; text-align: center">Предупреждение</div>
<!--        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>-->
      </div>
      <div class="modal-body">
        Выбранная персона будет полностью удалена из базы без вожможности восстановления.
        Вы уверены, что хотите это сделать.
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-danger" onclick="fullRemovePerson()">Удалить</button>
        <button type="button" class="btn btn-outline-success" data-bs-dismiss="modal" aria-label="Close">Отмена</button>
      </div>
    </div>
  </div>
</div>
<div>Удалить можно только то, что Вы можете увидеть. Если кнопка удаления неактивна - персона содержит Вам недоступные поля </div>
<div id="resultListRemove"></div>
`;
    if ((tempPerson.secretLevelMainInfo === "CLOSE" ||
            (tempPerson.fatherInfo === infoAbsent
                && tempPerson.motherInfo === infoAbsent
                && tempPerson.fioDtos === null))
        && (tempPerson.secretLevelPhoto === "CLOSE" || !tempPerson.photoExist)
        && (tempPerson.memberInfo.secretLevelPhone === "CLOSE" || tempPerson.secretLevelPhone === "UNDEFINED")
        && (tempPerson.memberInfo.secretLevelBiometric === "CLOSE" || tempPerson.secretLevelBiometric === "UNDEFINED")
        && (tempPerson.memberInfo.secretLevelEmail === "CLOSE" || tempPerson.secretLevelEmail === "UNDEFINED")
        && (tempPerson.memberInfo.secretLevelAddress === "CLOSE" || tempPerson.secretLevelAddress === "UNDEFINED")
        && (tempPerson.memberInfo.secretLevelBurial === "CLOSE" || tempPerson.secretLevelBurial === "UNDEFINED")
        && (tempPerson.memberInfo.secretLevelBirth === "CLOSE" || tempPerson.secretLevelBirth === "UNDEFINED")
        && (tempPerson.memberInfo.secretLevelDescription === "CLOSE" || tempPerson.secretLevelDescription === "UNDEFINED")) {
        document.getElementById("partButton").disabled = true;
    }
    if (tempPerson.secretLevelMainInfo === "CLOSE"
        || tempPerson.secretLevelPhoto === "CLOSE"
        || tempPerson.memberInfo.secretLevelPhone === "CLOSE"
        || tempPerson.memberInfo.secretLevelBiometric === "CLOSE"
        || tempPerson.memberInfo.secretLevelEmail === "CLOSE"
        || tempPerson.memberInfo.secretLevelAddress === "CLOSE"
        || tempPerson.memberInfo.secretLevelBurial === "CLOSE"
        || tempPerson.memberInfo.secretLevelBirth === "CLOSE"
        || tempPerson.memberInfo.secretLevelDescription === "CLOSE")
        document.getElementById("fullButton").disabled = true;

}

function loadSelectMenuRemove() {
    const jsonData = JSON.stringify(tempSecurity);
    fetch('/base/family_member/get/extended', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(response => response.json()).then(async person => {
        await assignPersonInfoToPerson(person);
    }).then(() => drawSelectMenuRemove());
}

function fullRemovePerson() {
    const jsonData = JSON.stringify(tempSecurity);
    fetch("/family_members/database/remove", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListRemove").innerHTML = await status.text();
    });
}

function drawSelectMenuRemove() {
    let temp = `
    <form class="form-group" style="margin:5px; text-align: center;" id="baseFormAddFM">
        <div class="accordion"
             style=" --bs-accordion-active-bg: #eaecbd;  --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgb(234 212 101 / 25%);"
             id="accordionRemove"> `
    if ((tempPerson.secretLevelPhoto !== "CLOSE" && !tempPerson.photoExist)
        || (tempPerson.secretLevelMainInfo !== "CLOSE")
        && ((tempPerson.fatherInfo !== infoAbsent)
            || (tempPerson.motherInfo !== infoAbsent)))
        temp += drawAccordionPage(`Main`, 'Основная информация');
    if (tempPerson.memberInfo.secretLevelPhone !== "CLOSE" && tempPerson.memberInfo.phones != null)
        temp += drawAccordionPage(`Phones`, 'Удаление телефонов');
    if (tempPerson.memberInfo.secretLevelEmail !== "CLOSE" && tempPerson.memberInfo.emails != null)
        temp += drawAccordionPage(`Mails`, 'Удаление электронной почты');
    if (tempPerson.memberInfo.secretLevelAddress !== "CLOSE" && tempPerson.memberInfo.addresses != null)
        temp += drawAccordionPage(`Addresses`, 'Удаление адресов');
    if (tempPerson.memberInfo.secretLevelBiometric !== "CLOSE" && tempPerson.memberInfo.biometric != null)
        temp += drawAccordionPage(`Biometrics`, 'Удаление биометрий');
    if (tempPerson.memberInfo.secretLevelBirth !== "CLOSE" && tempPerson.memberInfo.birth != null)
        temp += drawAccordionPage(`Birth`, 'Удаление места рождения');
    if (tempPerson.memberInfo.secretLevelBurial !== "CLOSE" && tempPerson.memberInfo.burial != null)
        temp += drawAccordionPage(`Burial`, 'Удаление места захоронения');
    if (tempPerson.memberInfo.secretLevelDescription !== "CLOSE" && tempPerson.memberInfo.description != null)
        temp += drawAccordionPage(`Description`, 'Удаление комментария');
    temp += `</div></form>` 

    document.getElementById("chooseRemoveMode").innerHTML = temp;
    formOfRemove.clear();
    completionFormOfRemove();
}

function drawAccordionPage(part, text) {
    return `<div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button" style="padding-bottom:8px; padding-top:8px; color: darkred"
                            type="button" data-bs-toggle="collapse" data-bs-target="#remove${part}" aria-expanded="true"
                            aria-controls="collapseOne">
                        ${text}
                    </button>
                </h2>
                <div id="remove${part}" class="accordion-collapse collapse" data-bs-parent="#accordionRemove">
                    <div class="accordion-body">
                        <div class="container-fluid row mh-100 no-gutters">
                            <span class="col-1" style="width: 5%"></span>
                            <div id="delete${part}"></div>
                            </div></div></div></div>`
}

function completionFormOfRemove() {
    let temp = ``;
    if (document.getElementById("deleteMain") !== undefined) {
        if (tempPerson.secretLevelPhoto !== "CLOSE" && !tempPerson.photoExist) {
            formOfRemove.set(`PrimePhoto`, false);
            let pictureUrl = URL.createObjectURL(tempPerson.primePhotoImj);
            temp += `<div class="container-fluid row mh-100 no-gutters">
            <div class="col-1" style="width: 10%"></div>
            <div class="col" style="min-width: 80%; text-align: center; align-content: center;align-items: center  ">
                    <div class="card text-bg-dark" style="width: 250px;">
                        <img src="${pictureUrl}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" id="textPrimePhoto" style="padding-top: 45%; font-size:24px; color: darkred; font-family:'Times New Roman',serif"></div>
                        </div>
                    </div>
            </div>
            <div class="col-1" style="width: 10%"></div>
            </div>
            <button class="btn btn-outline-warning" id="removePrimePhotoButton" type="button" style="margin-top: 5px" onclick="removeObject('PrimePhoto', 'основное фото', '')">Удалить основное фото</button>
            `;
            URL.revokeObjectURL(tempPerson.primePhotoImj);
        }
        if (tempPerson.secretLevelMainInfo !== "CLOSE") {
            if (tempPerson.fatherInfo !== infoAbsent) {
                formOfRemove.set(`Father`,false)
                temp += `<div>
            <span style="text-align: center; "> <span style="color: darkred;font-family: 'Times New Roman',serif">Отец:&nbsp;</span><span id="textFather">${tempPerson.fatherInfo}</span></span>
            <button class="btn btn-outline-warning" id="removeFatherButton" type="button" style="margin-left:15px; margin-top: 5px" onclick="removeObject('Father', 'отца', tempPerson.fatherInfo)">Удалить отца</button></div>`}
            if (tempPerson.motherInfo !== infoAbsent) {
                formOfRemove.set(`Mother`,false)
                temp += `<div>
            <span style="text-align: center; "> <span style="color: darkred;font-family: 'Times New Roman',serif">Мать:&nbsp;</span><span id="textMother">${tempPerson.motherInfo}</span></span>
            <button class="btn btn-outline-warning" id="removeMotherButton" type="button" style="margin-left:15px; margin-top: 5px" onclick="removeObject('Mother', 'мать', tempPerson.motherInfo)">Удалить мать</button></div>`
            }
            if (tempPerson.fioDtos != null) temp += `<button class="btn btn-outline-warning" type="button" style="margin-top: 5px">Удалить другие имена</button>`
        }
    }
    document.getElementById("deleteMain").innerHTML = temp;
}

function removeObject(place, text, removing) {
    let value = formOfRemove.get(place);
    if (value === false || value === true) {
        value = !value;
        formOfRemove.set(place, value);
        if (value) {
            document.getElementById(`text${place}`).innerHTML = `Будет удалено`;
            document.getElementById(`remove${place}Button`).innerHTML = `Вернуть&nbsp;` + text;
        }else {document.getElementById(`text${place}`).innerHTML = removing;
            document.getElementById(`remove${place}Button`).innerHTML = `Удалить&nbsp;` + text;}
    }
}