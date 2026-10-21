let alternateLanguish = new Map([]);
let activeAlt = '';

async function menuFunc3100(uuidUser, name) {
    alternateLanguish.clear();
    console.log("Contacts are painted");
    document.getElementById("mainPanel").innerHTML = `<div style="vertical-align: top;"><div style="text-shadow: -1px -1px 0 #f1f1f1,
                            1px -1px 0 #f1f1f1,
                            -1px  1px 0 #f1f1f1,
                            1px  1px 0 #f1f1f1,
                            0 -1px 0 #f1f1f1,
                            0  1px 0 #f1f1f1,
                            -1px  0 0 #f1f1f1,
                            1px  0 0 #f1f1f1;
                            padding-right: 0;padding-left:0;text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px; font-style:italic" id="commun-title">${globalTexts.get(3101)}</div>
        <div class="container-fluid row" style=" margin: 0; padding: 5px">
            <span class="col-2" style=" text-align: left; padding-left:0; padding-right: 0"> 
                <button class="btn btn-outline-warning" aria-current="page" id="contactButton" 
                    style="background:rgb(255,255,255,50%); font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: darkred; margin: 0;padding: 2px 5px 2px 5px;"
                    type="button" data-bs-toggle="offcanvas" data-bs-target="#contactTab" 
                    onmouseover="backgroundChange('contactButton','#ffc107')"
                    onmouseout="backgroundChange('contactButton','rgb(255,255,255,50%)')"
                    aria-controls="offcanvasRight">${langMatrix.get("contacts")}
                </button>  
            </span>
            <span class="col" style="padding-left:0; padding-right: 0"></span>
            <span class="col-7" style="padding-left:0; padding-right: 0;text-align: right">
                <span  style="font-family: 'Times New Roman', serif; font-size: 14px; color: chocolate;text-shadow: -1px -1px 0 #f1f1f1,
                1px -1px 0 #f1f1f1,
                -1px  1px 0 #f1f1f1,
                1px  1px 0 #f1f1f1,
                0 -1px 0 #f1f1f1,
                0  1px 0 #f1f1f1,
                -1px  0 0 #f1f1f1,
                1px  0 0 #f1f1f1;padding-left:0; padding-right: 0">${langMatrix.get("destination")}:</span>      
                <span class="btn-group-horizontal" role="group" aria-label="commun group" style="margin-left: 5px; text-align: right">
                    <input type="radio" class="btn-check" name="news-choice"  id="commun-choice1" autoComplete="off" checked>
                    <label class="btn btn-outline-warning" style="padding: 2px 5px 2px 5px; text-align:center; font-size: 14px; color: darkred" for="commun-choice1" onclick="menuFunc3110('${uuidUser}', '${name}')">${localMenu.get(3101)}</label>
                    <input type="radio" class="btn-check" name="news-choice"  id="commun-choice2" autoComplete="off" >
                    <label class="btn btn-outline-warning" style="padding: 2px 5px 2px 5px; text-align:center; font-size: 14px; color: darkred" for="commun-choice2" onclick="menuFunc3120()">${localMenu.get(3102)} </label>
                </span>
            </span>        
        </div>   
      
    <div class="offcanvas offcanvas-end" data-bs-scroll="true" tabindex="-1" id="contactTab" style="width: 250px"
     aria-labelledby="contactTab">
        <div class="offcanvas-header">
            <h5 class="offcanvas-title" id="contactTabLabel">${langMatrix.get("contacts")}</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body" style="text-align: left; font-size: 10px" id="contactTabForm">${globalTexts.get(3109)}
        </div>
    </div>
    <div id="form-place" style="padding:0;margin:0"></div></div>
`;
    drawNewsCounts();
    menuFunc3110(uuidUser, name);
    await getContacts();
}

function menuFunc3110(uuidUser, name) {
    document.getElementById("commun-title").innerHTML = globalTexts.get(3101);
    let pictureUrl = URL.createObjectURL(defaultPhotos.borderImage);
    let temp = `
    <form class="form-group" style="margin:5px; text-align: center" id="messageForm">
    <div class="container-fluid row mh-100 no-gutters" style="padding:0;margin:0">        
        <div class="col" style="min-width: 95%; padding:0;margin:15px 1% 0 2%; font-family: 'Times New Roman', serif; font-size: 16px;">
        <div class="accordion" style="            
             --bs-accordion-btn-bg: rgba(255,255,255,1);
             --bs-accordion-bg: rgba(255,255,255,0.75); 
             --bs-accordion-active-bg: #eaecbd;  
             --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem #ffc107; padding-top: 4px" id="messageAccord" >
        <div class="accordion-item">
            <div class="accordion-header">
               <button class="accordion-button" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapseReceiver" aria-expanded="true" aria-controls="collapseReceiver">
                ${langMatrix.get("receiver")}
                </button>
            </div>
            <div id="collapseReceiver" class="accordion-collapse collapse show" data-bs-parent="#messageAccord">
                <div class="accordion-body" style="padding: 0 5px 16px 10px">
                    <div class="container-fluid row mh-100 no-gutters">
                    <label for="sendingTo" style="color: chocolate; padding-top: 5px">${langMatrix.get("whom")}</label>
                        <textarea style="margin-left:4%; width:95%" rows="2" cols="50" class="form-control" type="text" id="sendingTo" name="sendingTo" placeholder="${globalTexts.get(3112)}"></textarea>
                        <button id="contactCarouselButton" class="btn btn-outline-warning" style="margin: 10px 0 0 36%; width:28%; color: darkred" type="button" style="background:rgb(255,255,255,50%); font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: darkred; margin: 12px 0 0 0;padding: 2px 5px 2px 5px;"
                            onmouseover="backgroundChange('contactCarouselButton','#ffc107')" onmouseout="backgroundChange('contactCarouselButton','rgb(255,255,255,50%)')"  data-bs-toggle="modal" data-bs-target="#selectWhom" >${langMatrix.get("carousel")}</button>     
                        <div class="modal fade" id="selectWhom" tabindex="-1" aria-labelledby="selectContacts" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color:#eaecbd;">
                                        <div class="modal-title" id="selectContacts" style="color: darkred; text-align: center">${globalTexts.get(3113)}</div>
                                    </div>
                                    <div class="modal-body">        
                                        <div id="contactCarouselPlace"></div>
                                    </div>
                                    <div class="modal-footer">
        <button type="button" class="btn btn-outline-warning" id="acceptReceiversButton" onmouseover="backgroundChange('acceptReceiversButton','#ffc107')" onmouseout="backgroundChange('acceptReceiversButton','rgb(255,255,255,50%)')" data-bs-dismiss="modal" onclick="addAlternate()">${langMatrix.get("select")}</button>
        <button type="button" class="btn btn-outline-warning" id="declineReceiversButton" onmouseover="backgroundChange('declineReceiversButton','#ffc107')" onmouseout="backgroundChange('declineReceiversButton','rgb(255,255,255,50%)')" data-bs-dismiss="modal" aria-label="Close">${langMatrix.get("cansel")}</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <label for="chooseCategory" style="color: chocolate; padding-top: 5px">${langMatrix.get("category")}</label>
                        <select class="form-select" style="margin-left: 20%; width: 60%" id="chooseCategory" aria-label="chooseCategory">            
                            <option value="PRIVATE" selected>${globalMenu.get(1400)}</option>`
    if (mainUser.role === "LINKED_USER" || mainUser.role === "VIP" || mainUser.role === "ADMIN") temp += `<option value="FAMILY">${globalMenu.get(1300)}</option>`
    if (mainUser.role === "ADMIN") temp += `<option value="SYSTEM">${globalMenu.get(1100)}</option>                
                            <option value="COMMON">${globalMenu.get(1200)}</option>`
    temp += `</select></div></div></div></div>
        <div class="accordion-item">
            <div class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapseMessage" aria-expanded="false" aria-controls="collapseMessage">
                    ${langMatrix.get("message")}
                </button>
            </div>
            <div id="collapseMessage" class="accordion-collapse collapse" data-bs-parent="#messageAccord">
                <div class="accordion-body" style="padding: 0 5px 16px 10px">
                    <label for="subjectCreat" style="color: chocolate; padding-top: 5px">${langMatrix.get("subject")}</label>
                    <input class="form-control" type="text" id="subjectCreat" name="subjectCreat" placeholder="${globalTexts.get(3103)}">            
                    <label for="sendingText" style="color: chocolate; padding-top: 5px">${langMatrix.get("message")}</label>
                    <textarea rows="5" cols="50" class="form-control" type="text" id="sendingText" name="sendingText" placeholder="${globalTexts.get(3104)}"></textarea>
                    </div></div></div>
        <div class="accordion-item">
            <div class="accordion-header">
               <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapseLocals" aria-expanded="false" aria-controls="collapseLocals">
                ${globalTexts.get(3105)}
                </button>
            </div>
            <div id="collapseLocals" class="accordion-collapse collapse" data-bs-parent="#messageAccord">
                <div class="accordion-body" style="padding-right: 5px;padding-left: 10px">
                    <div style="margin: 0 5% 0 5%; align-content: center; align-items: center">
                        <button id="addAlternateButton" class="btn btn-outline-warning" style="color: darkred" type="button" style="background:rgb(255,255,255,50%); font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: darkred; margin: 12px 0 0 0;padding: 2px 5px 2px 5px;"
                            onmouseover="backgroundChange('addAlternateButton','#ffc107')" onmouseout="backgroundChange('addAlternateButton','rgb(255,255,255,50%)')"  data-bs-toggle="modal" data-bs-target="#selectAlternate" onclick="saveIfExist(activeAlt)">${langMatrix.get("add")}</button>     
                        <div class="modal fade" id="selectAlternate" tabindex="-1" aria-labelledby="selectLangAlter" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header" style="background-color:#eaecbd;">
                                        <div class="modal-title" id="selectLangAlter" style="color: darkred; text-align: center">${globalTexts.get(3106)}</div>
                                    </div>
                                    <div class="modal-body">        
                                        <div id="selectAlterMenu"></div>
                                    </div>
                                    <div class="modal-footer">
        <button type="button" id="acceptAlternateButton" class="btn btn-outline-warning" onmouseover="backgroundChange('acceptAlternateButton','#ffc107')" onmouseout="backgroundChange('acceptAlternateButton','rgb(255,255,255,50%)')" data-bs-dismiss="modal" onclick="addAlternate()">${langMatrix.get("add")}</button>
        <button type="button" id="declineAlternateButton" class="btn btn-outline-warning" onmouseover="backgroundChange('declineAlternateButton','#ffc107')" onmouseout="backgroundChange('declineAlternateButton','rgb(255,255,255,50%)')" data-bs-dismiss="modal" aria-label="Close">${langMatrix.get("cansel")}</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="alternateMessage"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <div class="accordion-header">
               <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred;text-align: center" type="button" data-bs-toggle="collapse" data-bs-target="#collapsePicture" aria-expanded="false" aria-controls="collapsePicture">
                ${globalTexts.get(5)}
                </button>
            </div>
            <div id="collapsePicture" class="accordion-collapse collapse" data-bs-parent="#messageAccord">
                <div class="accordion-body" style="padding-right: 5px;padding-left: 10px">
                    <div class="card" id="messageCard" style="max-height:700px; align-items:center; border:0; margin: 0">
                     <div class="container-fluid row">
                     <span class="col-10" style="text-align: center;padding: 12px 0 0 18%" >
                        <label for="news" id="label_news" style="display:inline-block; border: 1px solid #000000; border-radius: 4px; cursor: pointer; background-color: RGB(255,255,255,90%);padding:4px; color: grey;" >${globalTexts.get(6)}</label>
                        <input class="form-control" type="file" id="news" name="news" accept="image/*" style="display:none" onchange="selectInputImage('news', 600,600)" >
                    </span>
                    <span class="col" style="text-align: left; align-content: center">
                        <button type="button" class="btn btn-outline-danger" style="padding: 3px" onclick="removeEntered('news')">X</button>
                    </span>
                    </div>
                    <div id="imageDefault_news" style="align-content: center; align-items: center; text-align: center; min-height:300px; min-width: 300px">
                    <img style="max-width: 95% ;max-height:40vh;" id="pictureDef_news" src=${pictureUrl} class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="margin-top:40px">
                        <div class="card-text" style="margin-top: 100px">${globalTexts.get(3)}</div>
                    </div>                   
                    </div>
                    <div class="card-body" style="text-align: center; padding: 0">                        
                        <div id="canvasBlock_news" hidden="hidden" style="align-content: center; align-items: center; text-align: center; min-height:300px; min-width: 300px" > 
                        <div style="padding-top:5px; color: chocolate;">${globalTexts.get(4)}</div>
                        <canvas  style="max-width: 95% ;max-height:40vh;" id="canvas_news"></canvas>
                        </div>
                    </div>                  
                    </div>

                    
<!--                    <div style="font-size: larger; font-family: 'Times New Roman',serif; text-align: center">Photo Result: -->
<!--        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveNewsPhoto"></span></div>-->
                    
                </div>
            </div>
        </div>
        </div>
    </div>
    </div>
    <button id="submitButton" class="btn btn-outline-warning" type="button" style="background:rgb(255,255,255,50%); font-family: 'Times New Roman', serif; font-size: 18px; text-align: center; color: darkred; margin: 12px 0 0 0;padding: 2px 5px 2px 5px;"
onmouseover="backgroundChange('submitButton','#ffc107')" onmouseout="backgroundChange('submitButton','rgb(255,255,255,50%)')" onclick="submitMessage('${uuidUser}')">${langMatrix.get("send")}</button>     
    
    </form>
    </div>
    <div id="resultListCreateMessage"></div>
`

    URL.revokeObjectURL(defaultPhotos.borderImage);
    document.getElementById("form-place").innerHTML = temp;
    if (uuidUser !== undefined && uuidUser !== "undefined" && uuidUser !== null && name !== undefined) {
        document.getElementById(`sendingTo`).value = name;
        document.getElementById(`sendingTo`).disabled = true;
        document.getElementById(`chooseCategory`).disabled = true;
        document.getElementById(`contactCarouselButton`).hidden = true;
    }
    drawSelectForm();
    drawContactCarousel();
}

function drawContactCarousel() {
    document.getElementById("contactCarouselPlace").innerHTML = ``
    //     `<div id="carouselExampleCaptions" class="carousel slide" data-bs-ride="carousel">
    //     <div class="carousel-indicators">
    //         <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="0" class="active"
    //                 aria-current="true" aria-label="Slide 1"></button>
    //         <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="1"
    //                 aria-label="Slide 2"></button>
    //         <button type="button" data-bs-target="#carouselExampleCaptions" data-bs-slide-to="2"
    //                 aria-label="Slide 3"></button>
    //     </div>
    //     <div class="carousel-inner">
    //         <div class="carousel-item active">
    //             <img src="..." class="d-block w-100" alt="...">
    //                 <div class="carousel-caption d-none d-md-block">
    //                     <h5>Метка первого слайда</h5>
    //                     <p>Некоторый репрезентативный заполнитель для первого слайда.</p>
    //                 </div>
    //         </div>
    //         <div class="carousel-item">
    //             <img src="..." class="d-block w-100" alt="...">
    //                 <div class="carousel-caption d-none d-md-block">
    //                     <h5>Метка второго слайда</h5>
    //                     <p>Некоторый репрезентативный заполнитель для второго слайда.</p>
    //                 </div>
    //         </div>
    //         <div class="carousel-item">
    //             <img src="..." class="d-block w-100" alt="...">
    //                 <div v="carousel-caption d-none d-md-block">
    //                     <h5>Метка третьего слайда</h5>
    //                     <p>Некоторый репрезентативный заполнитель для третьего слайда.</p>
    //                 </div>
    //         </div>
    //     </div>
    //     <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleCaptions"
    //             data-bs-slide="prev">
    //         <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    //         <span class="visually-hidden">Предыдущий</span>
    //     </button>
    //     <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleCaptions"
    //             data-bs-slide="next">
    //         <span class="carousel-control-next-icon" aria-hidden="true"></span>
    //         <span class="visually-hidden">Следующий</span>
    //     </button>
    // </div>`
}

function drawSelectForm() {
    let temp = `
        <label for="chooseAlternate" style="color: chocolate; padding-top: 5px">${globalTexts.get(3107)}</label>
        <select class="form-select" id="chooseAlternate" aria-label="chooseAlternate">`
    for (let i = 0; i < localisation.length; i++) {
        if (localisation[i].name !== currentLang && (alternateLanguish.get(localisation[i].name.toUpperCase()) === undefined || alternateLanguish.get(localisation[i].name.toUpperCase()) === null)) temp += `<option value="${localisation[i].name.toUpperCase()}">${localisation[i].value}</option>`
    }
    temp += `</select>`;
    document.getElementById("selectAlterMenu").innerHTML = temp;
    console.log(alternateLanguish);
}

function saveIfExist(prev) {
    if (prev !== '' && document.getElementById("chooseAlternate") !== undefined && document.getElementById("chooseAlternate") !== null) {
        backgroundChange(`altLoc${prev}`, 'rgb(255,255,255,50%)')
        alternateLanguish.set(prev, {
            localisation: prev,
            subject: document.getElementById("subjectCreatAlter").value,
            textInfo: document.getElementById("sendingTextAlter").value
        })
    }
}

function addAlternate() {
    let lang = document.getElementById("messageForm").elements.chooseAlternate.value;
    document.getElementById("alternateMessage").innerHTML = `
    <label for="subjectCreatAlter" style="color: chocolate; padding-top: 5px">${langMatrix.get("subject")}</label>
    <input class="form-control" type="text" id="subjectCreatAlter" name="subjectCreatAlter"
           placeholder="${globalTexts.get(3103) + lang}">
        <label for="sendingTextAlter" style="color: chocolate; padding-top: 5px">${langMatrix.get("message")}</label>
        <textarea rows="5" cols="50" class="form-control" type="text" id="sendingTextAlter" name="sendingTextAlter"
                  placeholder="${globalTexts.get(3104)}"></textarea>
        <button id="removeActiveAlternate" class="btn btn-outline-warning" style="color: darkred; margin-top:12px" type="button" style="background:rgb(255,255,255,50%); font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: darkred; margin: 12px 0 0 0;padding: 2px 5px 2px 5px;"
                            onmouseover="backgroundChange('removeActiveAlternate','#ffc107')" onmouseout="backgroundChange('removeActiveAlternate','rgb(255,255,255,50%)')" onclick="removeAlternate()">${langMatrix.get("remove")}</button>     
    <div id="pagLoc" style="color: chocolate; margin-top: 12px"></div>      
    `
    alternateLanguish.set(lang, '');
    creatLocPagination(lang);
    drawSelectForm()
}

function removeAlternate() {
    alternateLanguish.delete(activeAlt);
    let lang = true;
    for (let langElement of alternateLanguish.keys()) {
        creatLocPagination(langElement);
        activeAlt = langElement;
        document.getElementById("subjectCreatAlter").value = alternateLanguish.get(langElement).subject;
        document.getElementById("sendingTextAlter").value = alternateLanguish.get(langElement).textInfo;
        lang = false;
        break;
    }
    drawSelectForm();
    if (lang) {
        document.getElementById("alternateMessage").innerHTML = null;
        activeAlt = '';
    }
}

function getAlternate(prev, loc) {
    saveIfExist(prev);
    document.getElementById("subjectCreatAlter").value = alternateLanguish.get(loc).subject;
    document.getElementById("sendingTextAlter").value = alternateLanguish.get(loc).textInfo;
    backgroundChange(`altLoc${prev}`, 'rgb(255,255,255,50%)')
    creatLocPagination(loc);
}

function creatLocPagination(lang) {
    let tempPagLoc = `
        <div>${globalTexts.get(3108)}</div>
        <nav aria-label="LocNavigation">
            <ul class="pagination">`
    for (let locOf of alternateLanguish.keys()) {
        tempPagLoc += ` <li class="page-item">
                                <button class="btn btn-outline-warning" style="color: darkred" id="altLoc${locOf}" type="button" onclick="getAlternate('${lang}','${locOf}')">${locOf}</button>
        </li>`
    }
    tempPagLoc += `</ul></nav>`
    document.getElementById("pagLoc").innerHTML = tempPagLoc;
    backgroundChange(`altLoc${lang}`, '#ffc107');
    activeAlt = lang;
}

function menuFunc3120() {
    document.getElementById("commun-title").innerHTML = globalTexts.get(3102);

    let pictureUrl = URL.createObjectURL(defaultPhotos.groundTrees);
    let temp = `<div style="background: url(${pictureUrl}) repeat; height:80%">${globalTexts.get(2)}
 </div>
`
    URL.revokeObjectURL(defaultPhotos.groundTrees);
    document.getElementById("form-place").innerHTML = temp;
}

async function getContacts() {
    let i = 0;
    await fetch(endPoints.get('contacts') + "/get", {
        method: "GET",
        headers: {}
    }).then(response => response.json()).then(contactsFetch => {
        let contactList = [];
        if (contactsFetch !== null && !contactsFetch.isEmpty) {
            contactsFetch.forEach(contact => {
                    let fetchData = {
                        name: contact.name,
                        info: contact.info,
                        ownerID: contact.ownerID,
                        externId: contact.personUuid,
                        linkPersonUuid: contact.linkPersonUuid,
                        contactPhoto: contact.contactPhoto,
                        preferContact: contact.preferContact,
                        primePhoto: contact.primePhoto,
                        relation: contact.relation,
                        localisation: contact.localisation,
                        personLocalisation: contact.personLocalisation,
                        specify: contact.specify,
                        email: contact.email,
                        phone: contact.phone,
                        secretLevelPhoto: contact.secretLevelPhoto,
                        secretLevelPhone: contact.secretLevelPhone,
                        secretLevelEmail: contact.secretLevelEmail,
                        online: contact.online,
                        lastOnline: contact.lastOnline,
                        change: contact.change,
                        img: ``,
                        primePhotoText: ``
                    }
                    i += 1;
                    contactList.push(fetchData);
                }
            )
        }
        return contactList;
    }).then(contactList => {
        console.log("Contacts is loaded");
        console.log("Кол-во контактов: ", i);
        console.log("Kонтакты: ", contactList);
        let tempUuids = [];
        let tempAdd = [];
        for (let j = 0; j < i; j++) {
            tempUuids.push(contactList[j].externId);
            let con = contacts.get(contactList[j].externId);
            if (con === undefined || con === null || contactList[j].change) {
                con = contactList[j];
                tempAdd.push(con);
            }
            if (con.img !== ``) contactList[j].img = con.img;
            contacts.set(contactList[j].externId, contactList[j]);
        }
        if (i < contacts.size) {
            console.log("some contacts are go away");
            let removeContact = [];
            for (let personUuid of contacts.keys()) {
                if (!tempUuids.includes(personUuid)) removeContact.push(personUuid);
            }
            for (let removeContactElement of removeContact) {
                contacts.delete(removeContactElement);
            }
        }
        if (i !== 0 && document.getElementById("contactTabForm") !== undefined
            && document.getElementById("contactTabForm") !== null) drawContactCards(i, contactList);
        else if (i === 0 && document.getElementById("contactTabForm") !== null) document.getElementById("contactTabForm").innerHTML = globalTexts.get(3109);
        return tempAdd;
    }).then(updateCon => {
        for (let updateConElement of updateCon) {
            if (updateConElement.contactPhoto && updateConElement.preferContact)
                loadPicture(endPoints.get("contactPhoto") + "/get/" + updateConElement.externId)
                    .then(r => {
                        updateConElement.img = r;
                        updateContactPhoto(updateConElement.externId, updateConElement.img)
                    })
            else if (updateConElement.primePhoto && updateConElement.relation >= updateConElement.secretLevelPhoto)
                loadPicture(endPoints.get("contactPhoto") + "/getPrivate/" + updateConElement.externId + "/" + updateConElement.linkPersonUuid).then(r => {
                    updateConElement.img = r;
                    updateContactPhoto(updateConElement.externId, updateConElement.img)
                });

        }
    });
}

function updateContactPhoto(personId, updateImj) {
    if (updateImj !== null && document.getElementById("contactTabForm") !== undefined
        && document.getElementById("contactTabForm") !== null) {
        document.getElementById(`contact_image${personId}`).src = URL.createObjectURL(updateImj);
        URL.revokeObjectURL(updateImj);
    }
}

function drawContactCards(i, contactList) {
    let temp1 = "";
    let activeContacts = [];
    let passiveContacts = [];
    let activeUser = 0;
    contactList.forEach(x => {
        if (x.online) {
            activeContacts.push(x);
            activeUser++;
        } else passiveContacts.push(x);

    });
    if ((i - activeUser) > 1) passiveContacts.sort((x, y) => x.lastOnline.getTime() - y.lastOnline.getTime());
    let defaultPerson = URL.createObjectURL(defaultPhotos.person);
    if (activeUser > 0) {
        let circle = URL.createObjectURL(defaultPhotos.greenCircle);
        temp1 += drawContact(activeContacts, defaultPerson, circle, true);
        URL.revokeObjectURL(defaultPhotos.greenCircle);
    }
    if (i - activeUser > 0) {
        let circle = URL.createObjectURL(defaultPhotos.blackCircle);
        temp1 += drawContact(passiveContacts, defaultPerson, circle, false);
        URL.revokeObjectURL(defaultPhotos.blackCircle);
    }
    URL.revokeObjectURL(defaultPhotos.person);
    document.getElementById("contactTabForm").innerHTML = temp1;
}

function drawContact(contactList, defaultPhoto, circle, active) {
    let temp = '';
    for (let contactListElement of contactList) {
        let tempImg;
        console.log(contactListElement.img)
        if (contactListElement.img === "") tempImg = defaultPhoto; else tempImg = URL.createObjectURL(contactListElement.img);
        temp += `<button style="border-color: rgb(0,0,0,0); background-color: rgb(0,0,0,0)" onclick="editContact(${contactListElement.externId})">
                    <div class="card" style="vertical-align:middle; height:80px; min-width:100px; max-width: 250px; margin: 0; padding: 0">
                    <div class="row g-0 no-gutters" style="vertical-align:middle; align-items:center; height:100px;">
                        <div class="col-1">
                            <img src="${circle}" class="img-fluid rounded-start" width="25px" alt="tytCircle">                                               
                        </div>
                        <div class="col-4" style="padding: 7px 0 5px 5px">
                            <img src="${tempImg}" id="contact_image${contactListElement.externId}"  class="img-fluid rounded-start" width="80px" alt="tytPhoto">                    
                            <div>${contactListElement.specify}</div> 
                        </div>
                        <div class="col" style="vertical-align: top">
                            <div class="card-body" style="text-align: center;padding: 5px">
                                <div class="card-title" style="margin: 0;font-size: 14px; color: chocolate">${contactListElement.name}</div>
                                <div class="card-text" style="font-size: 10px">${contactListElement.info}</div>
                                <hr style="color:#a12121; width: 70%; margin:0 0 0 15%">`
        if (active) temp += `<div>${globalTexts.get(3110)}</div>`;
        else temp += `<div>${globalTexts.get(3110)}:</div>
                      <div>${contactListElement.lastOnline}</div>`;
        temp += ` </div></div></div></div></button>`;
        if (contactListElement.img !== "") URL.revokeObjectURL(contactListElement.img);
    }
    return temp;
}

function addContact() {
    document.getElementById("resultPart").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Добавить новый контакт ***</div>
<form class="form-group" style="margin:5px; text-align: center" id="ContactForm">
    <div class="container-fluid row mh-100 no-gutters">
        <span class="col" style="width: 20%"></span>
        <span class="col-6" style="min-width: 250px">
            <label for="contactExternId" style="color: chocolate; padding-top: 5px">UUID человека:</label>
            <input class="form-control" type="text" id="contactExternId" name="contactExternId" required>
            <label for="contactName" style="color: chocolate; padding-top: 5px">Имя контакта:</label>
            <input class="form-control" type="text" id="contactName" name="contactName" required>
            <label for="contactText" style="color: chocolate; padding-top: 5px">Аннотация контакта:</label>
            <textarea rows="5" cols="50" class="form-control" type="text" id="contactText" name="contactText"></textarea>
        </span>
        <span class="col" style="width: 20%"></span>
    </div>  
    <br>
    <label for="contactPhoto" style="color: chocolate;">Файл фото</label>
    <input class="form-control" type="file" style="font-size: 10px" id="contactPhoto" name="contactPhoto" value="DDD">
    <br>
    <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitAddContact()">Отправить</button>
</form>
<div style="font-size: 20px; text-align: center">Статус операции: <span id="resultListCreateContact"></span></div>
<div style="font-size: larger; font-family: 'Times New Roman',serif; text-align: center">Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveContactPhoto"></span></div>
    <br>
`
}

function submitAddContact() {
    const form = document.getElementById('ContactForm');
    let formData = {
        externId: form.elements.contactExternId.value,
        info: form.elements.contactText.value,
        name: form.elements.contactName.value,
        urlPhoto: null,
    }
    if (document.getElementById("contactPhoto").files[0] != null) {
        formData.urlPhoto = form.elements.contactExternId.value;
        let contactPhoto = document.getElementById("contactPhoto").files[0];
        let file = new FormData()
        file.append("contactPhoto", contactPhoto);
        file.append("externId", document.getElementById("contactExternId").value)
        fetch(endPoints.get('photoContact') + `/saveContactPhoto`, {
            method: 'POST',
            headers: {},
            body: file,
        }).then(async status => {
            document.getElementById("resultSaveContactPhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSaveContactPhoto").innerHTML = 'Contact photo not selected!';

    const jsonData = JSON.stringify(formData);
    fetch(endPoints.get('recipient') + "/contact/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateContact").innerHTML = await status.text();
    });
}


function editContact(externID) {
    document.getElementById("resultPart").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Редактирование контакта ***</div>
// <form class="form-group" style="margin:5px; text-align: center" id="ContactForm">
//     <div class="container-fluid row mh-100 no-gutters">
//         <span class="col" style="width: 20%"></span>
//         <span class="col-6" style="min-width: 250px">
//             <label for="contactExternId" style="color: chocolate; padding-top: 5px">UUID человека:</label>
//             <input class="form-control" type="text" id="contactExternId" name="contactExternId" required>
//             <label for="contactName" style="color: chocolate; padding-top: 5px">Имя контакта:</label>
//             <input class="form-control" type="text" id="contactName" name="contactName" required>
//             <label for="contactText" style="color: chocolate; padding-top: 5px">Аннотация контакта:</label>
//             <textarea rows="5" cols="50" class="form-control" type="text" id="contactText" name="contactText"></textarea>
//         </span>
//         <span class="col" style="width: 20%"></span>
//     </div>  
//     <br>
//     <label for="contactPhoto" style="color: chocolate;">Файл фото</label>
//     <input class="form-control" type="file" style="font-size: 10px" id="contactPhoto" name="contactPhoto" value="DDD">
//     <br>
//     <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitEditContact(${externID})">Отправить</button>
// </form>
// <div style="font-size: 20px; text-align: center">Статус операции: <span id="resultListCreateContact"></span></div>
// <div style="font-size: larger; font-family: 'Times New Roman',serif; text-align: center">Photo Result: 
//         <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveContactPhoto"></span></div>
//     <br>
`
}

function submitEditContact(externId) {
    const form = document.getElementById('ContactForm');
    let formData = {
        externId: externId,
        info: form.elements.contactText.value,
        name: form.elements.contactName.value,
        urlPhoto: null,
    }
    if (document.getElementById("contactPhoto").files[0] != null) {
        formData.urlPhoto = form.elements.contactExternId.value;
        let contactPhoto = document.getElementById("contactPhoto").files[0];
        let file = new FormData()
        file.append("contactPhoto", contactPhoto);
        file.append("externId", document.getElementById("contactExternId").value)
        fetch(endPoints.get('contactPhoto') + `/saveContactPhoto`, {
            method: 'POST',
            headers: {},
            body: file,
        }).then(async status => {
            document.getElementById("resultSaveContactPhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSaveContactPhoto").innerHTML = 'Contact photo not selected!';

    const jsonData = JSON.stringify(formData);
    fetch(endPoints.get('recipient') + "/contact/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateContact").innerHTML = await status.text();
    });
}

function submitMessage(uuidUser) {
    const form = document.getElementById('messageForm');
    saveIfExist(activeAlt);
    let alt = [];
    let sendTo = [];
    if (uuidUser !== undefined && uuidUser !== null&&uuidUser !== "undefined") sendTo.push(uuidUser);
    else if (form.elements.sendingTo.value !== null) sendTo.push(form.elements.sendingTo.value);
    alternateLanguish.forEach(x => alt.push(x));
    console.log(alt);
    let formData = {
        subject: form.elements.subjectCreat.value,
        textInfo: form.elements.sendingText.value,
        letterLocals: (alt.length !== 0) ? alt : null,
        addressList: sendTo,
        category: form.elements.chooseCategory.value,
        existPrimePhoto: (document.getElementById("news").files[0] !== undefined && document.getElementById("news").files[0] !== null)
    }

    const jsonData = JSON.stringify(formData);
    fetch(endPoints.get('newsPrivate') + "/send", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(r => r.text())
        .then((imgUrl) => {
            if (getExistToProfileMember("news")
                && imgUrl !== null&&imgUrl !== '') {
                let category = document.getElementById("chooseCategory").value;
                dropBlobToServer(document.getElementById("canvas_news"), "news", category, imgUrl)
            }
            // else document.getElementById("resultSaveNewsPhoto").innerHTML = 'News photo not selected!';
        })
        .then(() => {console.log("dialog Success");
            getDialogFrame(`menuFunc3100()`, langMatrix.get("success"), globalTexts.get(3114))
        })
        .catch(() => document.getElementById("resultListCreateMessage").innerHTML = "Letter is corrupt");
}

function addContactBySearch(a) {
    fetch(endPoints.get('newsPrivate') + "/contact/addRequest/" + a, {
        method: 'GET',
    }).then(async status => {
        document.getElementById("footer-main").innerHTML = await status.text();
    });
}