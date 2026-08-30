let counts = [0, 0, 0, 0, 0];
let cards;
let numCards;
let rawNews;
let externId;
let ownLinkId;
let ownId;
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
// background: url('/front/imj/ground') repeat;
// background-color:rgb(255,255,255,60%);
loadOnlineUser();
// loadStandardMainPanel();
// loadNewsCounts();
let delay = 5000;
setInterval(loadOnlineUser, delay);

function loadOnlineUser() {
    let username;
    let nickName;
    let birthday;
    let email;
    let role;
    let fullName;
    fetch(endPoints.get(`getInline`), {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
        }
    }).then(user => user.json()).then(user => {
        if (user.logName === null || user.logName === undefined) throw new Error("user without reg");
        username = user.username;
        nickName = user.nickName;
        birthday = user.birthday;
        email = user.email;
        counts = user.newsCounts;
        if (user.fullName === null || user.fullName === undefined) fullName = user.logName; else fullName = user.fullName;
        role = user.priorityRole;
        ownId = user.externUuid;
        ownLinkId = user.linkExternId;
        document.getElementById("nav0").innerHTML = fullName;
        document.getElementById("nav1").innerHTML = nickName;
        document.getElementById("nav2").innerHTML = langMatrix.get(role);
        document.getElementById("onlineUsersCount").innerHTML = user.onlinePeopleCount;
        document.getElementById("usersInBaseCount").innerHTML = user.peopleInBase;
        document.getElementById("personsInBaseCount").innerHTML = user.personsInBase;
        if (counts[0] > 0) loadNewsCounts();
        if ((role === "SIMPLE_USER" || role === "BASE_USER") && document.getElementById("linkingButton").hidden === true) document.getElementById("linkingButton").hidden = false;
        if (role !== "SIMPLE_USER" && role !== "BASE_USER") {
            if (document.getElementById("linkingButton").hidden === false) document.getElementById("linkingButton").hidden = true;
            if (ownLinkId == null) {
                fetch(endPoints.get(`getLinkGuard`), {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json; charset=UTF-8"
                    }
                }).then(promise => promise.text()).then(x => {
                    ownLinkId = x;
                    console.log(ownLinkId)
                })
            }
            document.getElementById("mainMenu2400").innerHTML = `<a class="dropdown-item" style="color: chocolate;" href="#mainPanel" onclick="getPersonalPage()">${globalMenu.get(2400)}</a>`;
        }
    }).catch(() => {
        getLogout();
        window.href = endPoints.get("toMain")
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

async function loadPicture(url) {
    let array = [];
    await fetch(url, {
        method: "GET",
    }).then(r => r.blob()).then(cou => {
        array = cou;
    }).then(cou => console.log("Изображение загружено", cou));
    return array;
}

async function loadDefaultPhotos() {
    let url = endPoints.get('fileSystem') + '/defaultPhotoPack'
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
                groundTrees: base64ToBlob(defaultPhotosMap.groundTrees,"image/jpeg"),
                linking: base64ToBlob(defaultPhotosMap.linking, "image/jpeg"),
                contact: base64ToBlob(defaultPhotosMap.contact, "image/jpeg"),
                photono: base64ToBlob(defaultPhotosMap.photono, "image/jpeg")
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

function loadNewsCounts() {
    if (counts[0] !== 0 && document.getElementById("badge0") !== null && document.getElementById("badge0") !== undefined) {
        document.getElementById("badge0").innerHTML =
            `<span id="newsCount" class="position-absolute top-1 start-1 translate-middle badge rounded-pill bg-danger" style="font-size: 12px" >
                <span>` + counts[0] + `</span>
            </span>`
        document.getElementById("newsCount").innerHTML = counts[0];
        for (let i = 1; i < numOfPart.get(1); i++) {
            if (counts[i] !== 0 && document.getElementById(`badge${i}`) !== null && document.getElementById(`badge${i}`) !== undefined) {
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
function drawStandardMainPanel() {
    document.getElementById("mainPanel").innerHTML = `
    <table style="padding: 0; margin-left: -2px; height: 82vh" >
        <tbody>
            <tr>
                <td class="col-1" style="vertical-align: top; padding-right:7px; margin-right: 10px; margin-left: 5px;text-align: center ">
                    <div class="list-group list-group-flush" id="taskPart" style="background-color:rgb(255,255,255,70%); height: 82vh; min-width: 80px; text-align: center" role="tablist" ><span style="color:white">Hello</span></div>
                </td>
                <td class="col" style="horiz-align: center; margin-right: 5px; margin-left: 5px ; text-align: center">
                    <div  class="tab-content col" id="resultPart" style="background-color:rgb(240,240,240,70%);height: 82vh; padding-left: 5px;text-align: center"><span style="color:lightgray ">.</span></div>
                </td>
            </tr>
        </tbody>
    </table>`
}

// function drawNavPanelNotes() {
//     let temp = ``;
//     for (let i = 1; i < 5; i++) {
//         temp += `
//         <li class="nav-item">
//             <a class="nav-link " style="color: darkred" aria-current="page" id="mainMenu${i}" href="#" onClick="getCommonInfo(${i}*1000)">${mainMenu.get(i * 1000)}</a>
//         </li>`
//     }
//     return temp;
// }
//
// function getPortalInfo(chose) {
//     let choseChapter = (chose / 1000 | 0) * 1000;
//     let chosePart = (chose / 100 | 0) * 100;
//     let temp = `<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** ${globalMenu.get(choseChapter)} ***</div>`
//     if ((chose - choseChapter) !== 0) {
//         if ((chose - chosePart) === 0) chose++;
//         temp += drawAccordion(chosePart, chose);
//         document.getElementById("resultPart").innerHTML = temp;
//     } else {
//         temp += `${globalTexts.get(chose)}`
//         document.getElementById("resultPart").innerHTML = temp;
//         if (!loadedParts.get(chose)) loadLanguishContent2(chosePart);
//     }
// }
//
// function getCommonInfo(page) {
//     drawStandardMainPanel();
//     let temp = `<br>
//         <div style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: chocolate">${langMatrix.get('chapter')}:</div>`
//     if (globalMenu.get(page + 100) !== null) temp += `<div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px; margin-right: -5px">`
//         + drawMenu(page) + `</div>`
//     document.getElementById("taskPart").innerHTML = temp;
//     getPortalInfo(page);
// }
//
// function drawMenu(page) {
//     let temp = ``
//     let numberChapter = (page / 1000 | 0) * 1000;
//     if (numbersOfParts.get(numberChapter) === 0
//         || numbersOfParts.get(numberChapter) === null
//         || globalMenu.get(numberChapter + 100) === null
//         || globalMenu.get(numberChapter + 100) === undefined) temp += globalTexts.get(2);
//     else
//         for (let i = 1; i <= numbersOfParts.get(numberChapter); i++) {
//             temp += `<input type="radio" class="btn-check" name="news-radio" onclick="getPortalInfo(${numberChapter + i * 100})" id="news-radio${i}" autoComplete="off">
//         <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio${i}">${localMenu.get(numberChapter + i * 100)}</label>`
//         }
//     return temp;
// }
//
// function drawAccordion(chosePart, chose) {
//     let number = numbersOfParts.get(chosePart)
//     let temp = `
//         <div class="accordion"
//              style="
//              --bs-accordion-btn-bg: rgba(234,154,50,0.35);
//              --bs-accordion-bg: rgba(234,154,50,0.15);
//              --bs-accordion-active-bg: rgba(234,154,50,0.75);
//              --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgba(234,154,50,0.5);"
//              id="accordionInfo"> `
//     for (let i = 1; i <= number; i++) {
//         temp += drawAccordionPage(chosePart + i, chose);
//     }
//     temp += `</div>`
//     return temp;
// }
//
// function drawAccordionPage(part, chose) {
//     return (part === chose) ? `<div class="accordion-item">
//                 <h2 class="accordion-header">
//                     <button class="accordion-button show" style="padding-bottom:8px; padding-top:8px; color: darkred"
//                             type="button" data-bs-toggle="collapse" data-bs-target="#page${part}" aria-expanded="true"
//                             aria-controls="#page${part}">
//                         ${globalMenu.get(part)}
//                     </button>
//                 </h2>
//                 <div id="page${part}" class="accordion-collapse collapse show" data-bs-parent="#accordionInfo">
//                     <div class="accordion-body">
//                         <div class="container-fluid row mh-100 no-gutters">
//                             <span class="col-1" style="width: 5%"></span>
//                             <div>${globalTexts.get(part)}</div>
//                             </div></div></div></div>` :
//         `<div class="accordion-item">
//                 <h2 class="accordion-header">
//                     <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred"
//                             type="button" data-bs-toggle="collapse" data-bs-target="#page${part}" aria-expanded="false"
//                             aria-controls="#page${part}">
//                         ${globalMenu.get(part)}
//                     </button>
//                 </h2>
//                 <div id="page${part}" class="accordion-collapse collapse" data-bs-parent="#accordionInfo">
//                     <div class="accordion-body">
//                         <div class="container-fluid row mh-100 no-gutters">
//                             <span class="col-1" style="width: 5%"></span>
//                             <div style="text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 16px">${globalTexts.get(part)}</div>
//                             </div></div></div></div>`
// }

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


