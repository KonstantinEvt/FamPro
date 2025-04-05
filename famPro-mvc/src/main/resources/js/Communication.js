async function getCommunication() {
    loadStandardMainPanel();
    let contactCards = await paintContacts(contacts);
    console.log("Contacts are painted", contactCards);
    document.getElementById("taskPart").innerHTML = `    
    <br>
    <div style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: chocolate">Написать:</div>
    <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px; margin-right: -5px">
        <input type="radio" class="btn-check" name="news-radio" onclick="sendMessage()" id="news-radio1" autoComplete="off" checked>
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio1">Портал
        </label>
        <input type="radio" class="btn-check" name="news-radio" onclick="" id="news-radio2" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio2">Email           
        </label>            
                <input type="radio" class="btn-check" name="news-radio" onclick="addContact()" id="news-radio3" autoComplete="off" >
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio3">Новый контакт
        </label>
                <input type="radio" class="btn-check" name="news-radio" onclick="getContacts(1)" id="news-radio4" autoComplete="off" >
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio4">Refresh Contacts
        </label>
                <input type="radio" class="btn-check" name="news-radio" onclick="editContact()" id="news-radio5" autoComplete="off" >
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio5">Edit contact
        </label>
    </div>
    <br><br>
    <button class="btn btn-outline-warning" aria-current="page"
                style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: darkred; margin-left: 5px; margin-right: -5px"
                type="button" data-bs-toggle="offcanvas" data-bs-target="#contactTab" 
                aria-controls="offcanvasRight">Список контактов
    </button>    
    <div class="offcanvas offcanvas-end" data-bs-scroll="true" tabindex="-1" id="contactTab" style="width: 250px"
     aria-labelledby="contactTab">
        <div class="offcanvas-header">
            <h5 class="offcanvas-title" id="contactTabLabel">Контакты</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body" style="text-align: left; font-size: 10px" id="contactTabForm">${contactCards}
        </div>
    </div>
`
    loadNewsCounts();
    sendMessage();
}

async function getContacts(state) {
    let i = 0;
    let contactsFresh = [];
    let promise = await fetch("/recipient/contact/get", {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
            // "Access-Control-Allow-Origin": "*"
        }
    }).then(response => response.json()).then(contactsFetch => {
        if (contactsFetch !== null && !contactsFetch.isEmpty) {
            contactsFetch.forEach(contact => {
                    let fetchData = {
                        name: contact.name,
                        info: contact.info,
                        externId: contact.externId,
                        ownerID: contact.ownerID,
                        contactPhoto: contact.contactPhoto,
                        primePhoto: contact.primePhoto,
                        imj: ``
                    }
                    i += 1;
                    contactsFresh.push(fetchData);
                }
            )
        }
    });
    console.log("Contacts is loaded", promise);
    console.log("Кол-во контактов: ", i);
    if (state !== 0) {
        contacts = contactsFresh;
        contactImages = await loadContactsImage(contacts, defaultPhotos);
        document.getElementById("contactTabForm").innerHTML = await paintContacts(contacts);
    }
    return contactsFresh;
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
        fetch(`/photoContact/saveContactPhoto`, {
            method: 'POST',
            headers: {},
            body: file,
        }).then(async status => {
            document.getElementById("resultSaveContactPhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSaveContactPhoto").innerHTML = 'Contact photo not selected!';

    const jsonData = JSON.stringify(formData);
    fetch("/recipient/contact/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateContact").innerHTML = await status.text();
    });
}

// private String name;
// private String info;
// private String urlPhoto;

async function loadContactsImage(contacts, defaultPhotos) {
    let get;
    let url = []
    let imjUrl = '';
    for (let j = 0; j < contacts.length; j++) {
        url[j] = contacts[j].externId + "";
        if (contacts[j].contactPhoto) imjUrl = "/photoContact/get/" + url[j];
        else if (contacts[j].primePhoto) imjUrl = "/photoContact/getPrime/" + url[j];
        else {
            contacts[j].imj = defaultPhotos.person;
        }
        if (imjUrl !== '') {
            get = await loadPicture(imjUrl).then(r => contacts[j].imj = r
            ).catch(() => contacts[j].imj = defaultPhotos.person);
            console.log("Photo:", get)
        }
        imjUrl = '';
    }
}

async function paintContacts(contactsPaint) {
    let temp1 = "";
    for (let j = 0; j < contactsPaint.length; j++) {
        let pictureUrl = '';
        if (contactsPaint[j].imj !== '') {
            pictureUrl = URL.createObjectURL(contactsPaint[j].imj);
        }
        temp1 += `
                <div class="card" style="vertical-align:middle; height:60px; min-width:100px; max-width: 250px; margin: 0; padding: 0">
                    <div class="row g-0 no-gutters" style="vertical-align:middle; align-items:center; height:60px;">
                        <div class="col-3" style="padding-left: 5px">
                            <img src=${pictureUrl} id="img-contact${j}"  class="img-fluid rounded-start" width="50px" alt="tytPhoto">                    
                        </div>
                        <div class="col-6" style="vertical-align: top">
                            <div class="card-body" style="text-align: center;padding: 5px">
                                <div class="card-title" style="margin: 0;font-size: 14px; color: chocolate">${contactsPaint[j].name}</div>
                                <div class="card-text" style="font-size: 10px">${contactsPaint[j].info}</div>
                        </div>                           
                        </div>
                        <div class="col-3" >
                                  <button class="btn btn-outline-success" style="vertical-align:middle;font-size: 10px; margin: 1px" onclick="editContact('${contactsPaint[j].externId}')" href="#">Edit</button> 
                 
                        </div>
                    </div>
                </div>           
               `
        if (contactsPaint[j].imj !== '') {
            URL.revokeObjectURL(contactsPaint[j].imj);
        }
    }
    if (contactsPaint.length === 0) temp1 = `<div style="font-family: 'Times New Roman',serif;font-size: 16px;color:darkred;text-align: center">Contact list is empty</div>`
    return temp1;
}

function editContact(externID) {
    document.getElementById("resultPart").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Редактирование контакта ***</div>
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
    <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitEditContact(${externID})">Отправить</button>
</form>
<div style="font-size: 20px; text-align: center">Статус операции: <span id="resultListCreateContact"></span></div>
<div style="font-size: larger; font-family: 'Times New Roman',serif; text-align: center">Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveContactPhoto"></span></div>
    <br>
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
        fetch(`/photoContact/saveContactPhoto`, {
            method: 'POST',
            headers: {},
            body: file,
        }).then(async status => {
            document.getElementById("resultSaveContactPhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSaveContactPhoto").innerHTML = 'Contact photo not selected!';

    const jsonData = JSON.stringify(formData);
    fetch("/recipient/contact/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateContact").innerHTML = await status.text();
    });
}

function sendMessage() {
    document.getElementById("resultPart").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Отправка сообщения ***</div>
<form class="form-group" style="margin:5px; text-align: center" id="messageForm">
    <div class="container-fluid row mh-100 no-gutters">
        <span class="col" style="width: 20%"></span>
        <span class="col-6" style="min-width: 250px">
            <label for="subjectCreat" style="color: chocolate; padding-top: 5px">Тема:</label>
            <input class="form-control" type="text" id="subjectCreat" name="subjectCreat" required>
            <label for="sendingTo" style="color: chocolate; padding-top: 5px">Получатель:</label>
            <input class="form-control" type="text" id="sendingTo" name="sendingTo" required>
            <label for="sendingText" style="color: chocolate; padding-top: 5px">text:</label>
            <textarea rows="5" cols="50" class="form-control" type="text" id="sendingText" name="sendingText" required></textarea>
            <label for="chooseCategory" style="color: chocolate; padding-top: 5px">Category:</label>
            <select class="form-select" id="chooseCategory" aria-label="chooseCategory">            
                <option value="PRIVATE" selected>Private</option>
                <option value="SYSTEM">System</option>
                <option value="FAMILY">Family</option>
                <option value="COMMON">Common</option>
            </select>
        </span>
        <span class="col" style="width: 20%"></span>
    </div>  
    <br>
    <label for="newsPhoto" style="color: chocolate;">Файл фото</label>
    <input class="form-control" type="file" style="font-size: 10px" id="newsPhoto" name="newsPhoto" value="DDD">
    <br>
    <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitMessage()">Отправить</button>
</form>
<div style="font-size: 20px; text-align: center">Статус операции: <span id="resultListCreateMessage"></span></div>
<div style="font-size: larger; font-family: 'Times New Roman',serif; text-align: center">Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveNewsPhoto"></span></div>
    <br>
`
}

function submitMessage() {
    const form = document.getElementById('messageForm');
    if (document.getElementById("newsPhoto").files[0] != null) {
        let newsPhoto = document.getElementById("newsPhoto").files[0];
        let file = new FormData()
        let category = document.getElementById("chooseCategory").value;
        file.append("newsPhoto", newsPhoto);
        file.append("name", document.getElementById("subjectCreat").value)
        file.append("bucket", category)
        fetch(`/file/saveNewsPhoto`, {
            method: 'POST',
            headers: {},
            body: file,
        }).then(async status => {
            document.getElementById("resultSaveNewsPhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSaveNewsPhoto").innerHTML = 'News photo not selected!';
    let formData = {
        subject: form.elements.subjectCreat.value,
        textInfo: form.elements.sendingText.value,
        sendingTo: form.elements.sendingTo.value,
        category: form.elements.chooseCategory.value
    }

    const jsonData = JSON.stringify(formData);
    fetch("/message/sendMessage", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateMessage").innerHTML = await status.text();
    });
}

function addContactBySearch(a) {
    fetch("/message/contact/addRequest/" + a, {
        method: 'GET',
    }).then(async status => {
        document.getElementById("footer-main").innerHTML = await status.text();
    });
}