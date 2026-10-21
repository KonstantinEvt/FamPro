console.log(localStorage.getItem(`currentLang`));
let currentLang = (localStorage.getItem(`currentLang`) === null) ? "ru" : localStorage.getItem('currentLang');
const localisation = [{name: 'ru', value: 'Русский'},
    {name: 'en', value: 'English'},
    {name: 'bu', value: 'Белорусский'}
];
const langEn = new Map([
    ['langTabLabel', 'Languish'],
    ['selectLang', 'Select'],
    ['ADMIN', 'Admin'],
    ['LINKED_USER', 'Linked'],
    ['SIMPLE_USER', 'Simple'],
    ['BASE_USER', 'Base'],
    ['VIP', 'VIP']
]);
const langRu = new Map([
    ['langTabLabel', 'Язык'],
    ['selectLang', 'Выбрать'],
    ['ADMIN', 'Админ'],
    ['LINKED_USER', 'Связанный'],
    ['SIMPLE_USER', 'Простой'],
    ['BASE_USER', 'Базовый'],
    ['VIP', 'VIP']
]);
const numOfPart = new Map([
    [10, 6],
    [1, 5],
    [2, 5],
    [3, 4],
    [4, 0],
    [5, 3],
    [22,5]
]);
const globalMenuEn = new Map([
    [0, 'Welcome'],
    [1, 'Language change'],
    [1000, 'News'],
    [2000, 'Person base'],
    [3000, 'Communication'],
    [4000, 'Journal'],
    [5000, 'Move']
]);
const globalMenuRu = new Map([
    [0, 'Привет'],
    [1, 'Смена языка'],
    [1000, 'Новости'],
    [2000, 'База персон'],
    [3000, 'Коммуникация'],
    [4000, 'Дневник'],
    [5000, 'Перейти']
]);
let langMatrix = new Map([[``, ``]]);
let globalTexts = new Map([[0, ``]]);
let globalMenu = new Map([[0, ``]]);
let localMenu = new Map([[0, ``]]);
let tempLoad;
let tempChoice = new Map([]);
let rawNewsMap = new Map([]);
let systemRecipientID = "4825cb05-9a8c-373b-9c8a-1ff422af5c2f";
let informerRecipientID="ba275e26-9a88-3cd9-bf6a-5507ca85053a";

const endPoints = new Map([
        [`getLang`, `/front/languish/get`],
        [`setLang`, `/front/languish/set`],
        [`toAbout`, '/free/page/about'],
        [`toMain`, '/free/page/main'],
        [`languishBeginLoad`, '/front/languish/beginLoad'],
        [`home`, `/front/welcome`],
        [`filePrivate`, `/file/private`],
        [`contactPhoto`, `/file/contact`],
        [`fileSystem`, `/file/standard`],
        [`getLinkGuard`, `/family/guard/getLinkGuard`],
        [`newsPortal`, `/notify/news`],
        [`newsPrivate`, `/notify/message`],
        [`recipient`, `/notify/recipient`],
        [`getInline`, `/inline/get`],
        [`setOffline`, `/inline/out`],
        [`guard`, `/family/guard`],
        [`base`, `/frontAPI/base`],
        [`sso`, `/frontAPI/sso`],
        [`contacts`, `/contact/contact`]

    ]
);
drawLocalisationList()
clearLoads(currentLang)
setupLang()

function drawLocalisationList() {
    let temp = ``;
    for (let i = 0; i < localisation.length; i++) {
        temp += `<div>
            <input type="radio" id="${localisation[i].name}Loc" name="loc" value=${localisation[i].name}>
            <label for="${localisation[i].name}Loc">${localisation[i].value} </label>
        </div>`
    }
    document.getElementById('localisationList').innerHTML = temp;
}

function clearLoads(lang) {
    if (lang === undefined) lang = 'ru';
    if (lang === 'en') {
        globalMenu = globalMenuEn;
        langMatrix = langEn
    } else {
        globalMenu = globalMenuRu;
        langMatrix=langRu
    }
    globalTexts.clear();
    localMenu.clear();
}

function setupLang() {
    drawNavPanel();
    let justChange = localStorage.getItem('justChange');
    const now = Date.now();
    if (justChange !== null && justChange !== undefined && ((now - justChange) / 60000 < 10)) {
        switch (localStorage.getItem(`currentLang`)) {
            case "en":
                changeLang("en", true);
                break;
            default:
                changeLang("ru", true);
        }
        console.log((now - justChange) / 60000)
    } else getRegisterLang();
    localStorage.setItem('justChange', now + ``);
}

function getRegisterLang() {
    fetch(endPoints.get('getLang'), {
        method: 'GET',
        headers: {},
    }).then(response =>
        response.text())
        .then(lang => {
                console.log("Try to receive register languish", lang);
                if (lang !== "en" && lang !== "ru") {
                    currentLang = "ru";
                    setRegisterLang(currentLang).then(() => {
                        console.log("Установлен русский язык по-умолчвнию");
                        changeLang(currentLang, true);
                    })
                } else {
                    console.log("languish setup", lang);
                    localStorage.setItem('currentLang', lang);
                    changeLang(lang, true);
                }
            }
        )
}

async function setRegisterLang(lang) {
    let url = endPoints.get(`setLang`) + `?loc=${lang}`;
    await fetch(url, {
        method: 'GET',
        headers: {},
    }).then(response =>
        response.text())
        .then(t => console.log("Результат установки языка:", t));
}

function changeLang(lang, first) {
    if (currentLang !== lang || first) {
        currentLang = lang;
        clearLoads(lang);
        loadLanguishContent().then(() => {
            for (let i = 2; i < numOfPart.get(10); i++) {
                document.getElementById(`mainMenu${i * 1000}`).innerHTML = globalMenu.get(i * 1000);
                for (let j = 1; j < numOfPart.get(i); j++) {
                    let num = i * 1000 + j * 100;
                    document.getElementById(`mainMenu${num}`).innerHTML = globalMenu.get(num);
                }
            }
            for (let langElement of langMatrix.keys()) {
                let temp = document.getElementById(langElement);
                if (temp !== undefined && temp !== null) temp.innerHTML = langMatrix.get(langElement);
            }
            if (first) getCommon(0); else {
                getCommon(1);
                setRegisterLang(lang).then(() => console.log("Запрос на установку языка отправлен", currentLang));
                loadOnlineUser();
            }
            document.getElementById(`${currentLang}Loc`).checked = true;
            localStorage.setItem('currentLang', currentLang);
            localStorage.setItem('justChange', Date.now() + ``);
        })
    }
}

function changeLangMenu(lang) {
    if (lang === 'ru') {
        document.getElementById('langTabLabel').innerHTML = langRu.get('langTabLabel');
        document.getElementById('selectLang').innerHTML = langRu.get('selectLang');
    } else {
        document.getElementById('langTabLabel').innerHTML = langEn.get('langTabLabel');
        document.getElementById('selectLang').innerHTML = langEn.get('selectLang');
    }
}

function drawNavPanel() {
    let temp = `<ul class="navbar-nav">`;
    for (let i = 2; i < 6; i++) {
        temp += `<li class="nav-item dropdown">
                <a id="mainMenu${i * 1000}" class="nav-link dropdown-toggle" style="color: darkred" href="#" role="button"
                    data-bs-toggle="dropdown" aria-expanded="false">${globalMenu.get(i * 1000)}                
                </a>
                <ul class="dropdown-menu"
                style="background-color: #eaecbd; color: chocolate;font-family: 'Times New Roman',serif; font-size: 16px; padding:0; align-content: center">
            `
        for (let j = 1; j < numOfPart.get(i); j++) {
            let num = i * 1000 + j * 100;
            temp += `<li><a id="mainMenu${num}" class="dropdown-item" style="color: chocolate;"
                       data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" 
                       aria-label="Toggle navigation"
                       onClick="menuFunc${num}()"></a></li>`;
        }
        temp += `</ul>
        </li>`
    }
    temp += `<li class="nav-item"><a class="nav-link " id="linkingButton" style="color: red;" href="#mainPanel"
                           onclick="menuFunc2200(0)" hidden="hidden">${langMatrix.get("linkingButton")}</a></li>  
            </ul>`
    document.getElementById('navbarNavDropdown').innerHTML = temp;
    document.getElementById(`mainMenu2400`).hidden=true;
}

function getCommon(choice) {
    document.getElementById("newsFront").innerHTML = `${langMatrix.get("newsFront")}`;
    document.getElementById("mainPanel").innerHTML = `<div class="main_title" style="max-height: 25%">*** ${globalMenu.get(choice)} ***</div>
                <div class="main_under" style="margin-bottom:40%">${globalTexts.get(choice)}</div>`;
    document.querySelector('title').textContent = `${langMatrix.get("mainTitle")}`
}

function getLogout() {
    let confirmNote=document.getElementById("logoutText");
    if (confirmNote!==undefined&&confirmNote!==null) confirmNote.innerHTML=`<div class="spinner-border" role="status"> 
        <span class="visually-hidden">Logout...</span></div>`;
    fetch(endPoints.get("setOffline"), {
        method: 'GET',
        headers: {},
    }).then(() => localStorage.removeItem("justChange"))
}

function menuFunc5100() {
    localStorage.setItem("callbackPage", endPoints.get("home"))
    window.location.href = endPoints.get("toAbout")
}

function menuFunc5200() {
    localStorage.setItem("callbackPage", endPoints.get("home"))
    window.location.href = endPoints.get("toMain")
}

function addMatrixContent(matrix, freshContent) {
    for (let [key, value] of freshContent) {
        if (!matrix.has(key)) matrix.set(Number(key), value);
    }
}

function addMatrixContentString(matrix, freshContent) {
    for (let [key, value] of freshContent) {
        if (!matrix.has(key)) matrix.set(key, value);
    }
}

async function loadLanguishContent() {
    let url = endPoints.get("languishBeginLoad") + `/${currentLang}`
    await fetch(url, {
        method: 'POST',
        headers: {},
    }).then(response => response.json())
        .then(async loadLanguishPack => {
            console.log("first languish pack loaded", loadLanguishPack);
            tempLoad = {
                globalMenu: (new Map(Object.entries(loadLanguishPack.globalMenu))),
                globalTexts: (new Map(Object.entries(loadLanguishPack.globalTexts))),
                langMatrix: (new Map(Object.entries(loadLanguishPack.langMatrix))),
                localMenu: (new Map(Object.entries(loadLanguishPack.localMenu)))
            };
        }).then(() => {
            addMatrixContent(globalMenu, tempLoad.globalMenu);
            addMatrixContent(globalTexts, tempLoad.globalTexts);
            addMatrixContentString(langMatrix, tempLoad.langMatrix);
            addMatrixContent(localMenu, tempLoad.localMenu);
        });
}
