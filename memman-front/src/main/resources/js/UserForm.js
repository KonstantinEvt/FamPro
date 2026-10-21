let counts = [0, 0, 0, 0, 0];
let mainUser = {
    "role": langMatrix.get("SIMPLE_USER"),
    "nickName": langMatrix.get("anonymous"),
    "username": langMatrix.get("unknown"),
    "birthday": '',
    "email": langMatrix.get("unknown"),
    "fullName": langMatrix.get("unknown"),
    "logName": langMatrix.get("unknown"),
    "ownLinkId": ``,
    "ownId": ``
};
// let numCards;
// let rawNews;
let externId;
let tempSecurity;
let tempPerson;
let tempTextPhoto;
const infoAbsent = "Информация отсутствует";
const infoClosed = "Информация закрыта";
const infoUncorrected = "Неверная информация"
let countPhone = 0;
let countEmail = 0;
let countOtherNames = 1;
let insertOther = [];
let insertEmail = [];
let insertPhone = [];
let countBio = 0;
let loadBio = 0;
let inputBio = [];
let activeBio = 0;
let clickOther = false;
let primePhotoExist = false;
let birthPhotoExist = false;
let burialPhotoExist = false;
let formOfRemove = new Map([]);
loadOnlineUser();
let delay = 5000;
setInterval(loadOnlineUser, delay);

function loadOnlineUser() {
    fetch(endPoints.get(`getInline`), {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
        }
    }).then(user => user.json()).then(user => {
        if (user.logName === null || user.logName === undefined) throw new Error("user without reg");
        mainUser.username = user.username;
        mainUser.nickName = user.nickName;
        mainUser.birthday = user.birthday;
        mainUser.email = user.email;
        mainUser.logName = user.logName;
        counts = user.newsCounts;
        if (user.fullName === null || user.fullName === undefined) mainUser.fullName = user.logName; else mainUser.fullName = user.fullName;
        mainUser.role = user.priorityRole;
        mainUser.ownId = user.externUuid;
        mainUser.ownLinkId = user.linkExternId;
        document.getElementById("nav0").innerHTML = mainUser.fullName;
        document.getElementById("nav1").innerHTML = mainUser.nickName;
        document.getElementById("nav2").innerHTML = langMatrix.get(mainUser.role);
        document.getElementById("onlineUsersCount").innerHTML = user.onlinePeopleCount;
        document.getElementById("usersInBaseCount").innerHTML = user.peopleInBase;
        document.getElementById("personsInBaseCount").innerHTML = user.personsInBase;
        if (counts !== undefined && counts !== null && counts[0] > 0) drawNewsCounts();
        if ((mainUser.role === "SIMPLE_USER" || mainUser.role === "BASE_USER") && document.getElementById("linkingButton").hidden === true) document.getElementById("linkingButton").hidden = false;
        if (mainUser.role !== "SIMPLE_USER" && mainUser.role !== "BASE_USER") {
            if (document.getElementById("linkingButton").hidden === false) document.getElementById("linkingButton").hidden = true;
            if (mainUser.ownLinkId == null) {
                fetch(endPoints.get(`getLinkGuard`), {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8"
                    }
                }).then(promise => promise.text()).then(x => {
                    mainUser.ownLinkId = x;
                    console.log(mainUser.ownLinkId)
                })
            }
            document.getElementById("mainMenu2400").hidden = false;
            // document.getElementById("mainMenu2400").innerHTML = `<a class="dropdown-item" style="color: chocolate;" href="#mainPanel" onclick="menuFunc2400()">${globalMenu.get(2400)}</a>`;
        }
    }).catch((reason) => {
        getLogout();
        console.log("something wrong");
        console.log(reason);
        window.location.href = endPoints.get("toMain");
    });
}

function getTime(datetime) {
    let hour = new Intl.NumberFormat("ru", {minimumIntegerDigits: 2}).format(datetime.getHours());
    let minutes = new Intl.NumberFormat("ru", {minimumIntegerDigits: 2}).format(datetime.getMinutes());
    return hour + ':' + minutes;
}

function getDate(datetime) {
    let year = datetime.getFullYear();
    let month = new Intl.NumberFormat("en", {minimumIntegerDigits: 2}).format(datetime.getMonth() + 1);
    let day = new Intl.NumberFormat("en", {minimumIntegerDigits: 2}).format(datetime.getDate());
    return (day + '.' + month + '.' + year);
}

function getDateToFull(datetime) {
    let year = datetime.getFullYear();
    let month = new Intl.NumberFormat("en", {minimumIntegerDigits: 2}).format(datetime.getMonth() + 1);
    let day = new Intl.NumberFormat("en", {minimumIntegerDigits: 2}).format(datetime.getDate());
    return (year + "-" + month + "-" + day);
}

async function loadPicture(url) {
    let blob = ``;
    await fetch(url, {
        method: "GET",
    }).then(r => r.blob()).then(cou => {
        if (cou.size === 0) throw new Error("empty")
        blob = cou;
    }).then(cou => console.log("Изображение загружено", cou));
    return blob;
}

async function loadDefaultPhotos() {
    let url = endPoints.get('fileSystem') + '/get/defaultPhotoPack'
    return await fetch(url, {
        method: "POST"
    }).then(r => r.json())
        .then(defaultPhotosMap => {
            console.log(defaultPhotosMap);
            return {
                person: base64ToBlob(defaultPhotosMap.person, "image/jpeg"),
                election: base64ToBlob(defaultPhotosMap.election, "image/jpeg"),
                approved: base64ToBlob(defaultPhotosMap.approved, "image/jpeg"),
                rejected: base64ToBlob(defaultPhotosMap.rejected, "image/jpeg"),
                groundTrees: base64ToBlob(defaultPhotosMap.groundTrees, "image/jpeg"),
                linking: base64ToBlob(defaultPhotosMap.linking, "image/jpeg"),
                contact: base64ToBlob(defaultPhotosMap.contact, "image/jpeg"),
                borderImage: base64ToBlob(defaultPhotosMap.borderImage, "image/jpeg"),
                borderImage5: base64ToBlob(defaultPhotosMap.borderImage5, "image/jpeg"),
                systemPhoto: base64ToBlob(defaultPhotosMap.systemPhoto, "image/jpeg"),
                photono: base64ToBlob(defaultPhotosMap.photono, "image/jpeg"),
                blackCircle: base64ToBlob(defaultPhotosMap.blackCircle, "image/jpeg"),
                greenCircle: base64ToBlob(defaultPhotosMap.greenCircle, "image/jpeg")
            }
        })
}

function base64ToBlob(base64, mime = 'application/octet-stream') {
    // Убираем префикс data URI, если он есть
    const clean = base64.replace(/^data:[^;,]*;base64,/, '');

    // Декодируем Base64 в двоичную строку
    const binary = atob(clean);

    // Создаём массив байтов нужного размера
    const bytes = new Uint8Array(binary.length);

    // Заполняем массив байтов
    for (let i = 0; i < binary.length; i++) {
        bytes[i] = binary.charCodeAt(i);
    }

    // Создаём и возвращаем объект Blob
    return new Blob([bytes], {type: mime});
}

function drawNewsCounts() {
    if (counts[0] > 0 && document.getElementById("badge0") !== null && document.getElementById("badge0") !== undefined) {
        document.getElementById("badge0").innerHTML =
            `<span id="newsCount" class="position-absolute top-1 start-1 translate-middle badge rounded-pill bg-danger" style="font-size: 12px" >
                <span>` + counts[0] + `</span>
            </span>`
        document.getElementById("newsCount").innerHTML = counts[0];
        for (let i = 1; i < numOfPart.get(1); i++) {
            if (counts[i] > 0 && document.getElementById(`badge${i}`) !== null && document.getElementById(`badge${i}`) !== undefined) {
                document.getElementById(`badge${i}`).innerHTML = `
         <span id="countNew${i}" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[i] + `</span>
            </span>`
            } else if (document.getElementById(`badge${i}`) !== null) document.getElementById(`badge${i}`).innerHTML = "";
        }
    } else {
        for (let i = 0; i < 5; i++) {
            if (document.getElementById(`badge${i}`) !== null) document.getElementById(`badge${i}`).innerHTML = "";
        }
    }
}

function loadStandardMainPanel() {
    document.getElementById("mainPanel").innerHTML = `
    <table style="padding: 0; margin-left: -2px" >
    <tbody>
    <tr>
    <td class="col-1" style=" vertical-align: top; padding-right:3px; margin-right: 10px; text-align: center ">
    <div id="taskPart" style="height: 81vh; min-width: 81px; text-align: center" role="tablist" >
    </div>
    </td>
    <td class="col" style="background-color:rgb(255,255,255,30%); horiz-align: center; text-align: center;vertical-align: top">
    <div id="resultPart" style="height: 81vh; margin: 10px 3px 0 10px;text-align: center;vert-align: top ">
    </div>
    </td>
    </tr>
    </tbody>
    </table>   
`
}

function getDialogFrame(func, subject, text) {
    document.getElementById("mainPanel").innerHTML = `
<div style="text-align: center;margin-top: 20vh">
    <div class="card" style="display: block; margin: 0 auto; max-height:400px; width: 350px; min-width: 290px;  max-width: 400px;">
        <div class="card-header" style="background: rgb(234, 236, 189)">
            ${subject}
        </div>
        <div class="card-body">    
            <p class="card-text">${text}</p>
            <button class="btn btn-warning" style="margin-bottom: 20px" onclick="${func}">ок</button>
        </div>
    </div>
</div>`
}


