const langEn = new Map([
    ['langTabLabel', 'Languish'],
    ['selectLang', 'Select']
]);
const langRu = new Map([
    ['langTabLabel', 'Язык'],
    ['selectLang', 'Выбрать']
]);
const numOfPart = new Map([
    [10, 6],
    [1, 5],
    [2, 5],
    [3, 4],
    [31, 3],
    [4, 0],
    [5, 3]
]);
const globalMenuEn = new Map([
    [0, 'Welcome'],
    [1, 'Language change'],
]);
const globalMenuRu = new Map([
    [0, 'Привет'],
    [1, 'Смена языка']
]);
let langMatrix = new Map([[``, ``]]);
let globalTexts = new Map([[0, ``]]);
let globalMenu = new Map([[0, ``]]);
let localMenu = new Map([[0, ``]]);
let tempLoad;

const endPoints = new Map([
        [`getLang`, `/front/languish/get`],
        [`setLang`, `/front/languish/set`],
        [`toAbout`, '/free/page/about'],
        [`toMain`, '/free/page/main'],
        [`languishBeginLoad`, '/front/languish/beginLoad'],
        [`home`, `/front/welcome`],
        [`filePrivate`, `/file/private`],
        [`contactPhoto`, `/file/photoContact`],
        [`fileSystem`, `/file/system`],
        [`getLinkGuard`, `/family/guard/getLinkGuard`],
        [`newsPortal`, `/notify/news`],
        [`newsPrivate`, `/notify/message`],
        [`recipient`, `/notify/recipient`],
        [`getInline`, `/inline/get`],
        [`setOffline`, `/inline/out`]

    ]
);

setupLang()

function clearLoads(lang) {
    if (lang === 'en') {
        globalMenu = globalMenuEn;
    } else {
        globalMenu = globalMenuRu;
    }
    globalTexts.clear();
    localMenu.clear();
    langMatrix.clear()
}

function setupLang() {
    drawNavPanel();
    let justChange = localStorage.getItem('justChange');
    const now = Date.now();
    if (justChange !== null) {
        if ((now - justChange) / 60000 < 10) {
            switch (localStorage.getItem(`currentLang`)) {
                case "en":
                    changeLang("en", true);
                    break;
                default:
                    changeLang("ru", true);
            }
            console.log((now - justChange) / 60000)
        } else getRegisterLang();
    } else getRegisterLang();

    console.log("languish setup resume: ", currentLang);
    console.log("Current time delay", (now - justChange) / 60000);
    localStorage.setItem('justChange', now + ``);
}

function getRegisterLang() {
    fetch(endPoints.get('getLang'), {
        method: 'GET',
        headers: {},
    }).then(response =>
        response.text())
        .then(lang => {
                console.log("Try to receive register langvish", lang);
                if (lang !== "en" || lang !== "ru") {
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
                    data-bs-toggle="dropdown" aria-expanded="false">                
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
}

function getCommon(choice) {
    document.getElementById("newsFront").innerHTML = `${langMatrix.get("newsFront")}`;
    document.getElementById("mainPanel").innerHTML = `<div style="max-height: 25%; text-align: center; color: chocolate; 
        text-shadow: -1px -1px 0 #d1d1d1,
        1px -1px 0 #d1d1d1,
        -1px  1px 0 #d1d1d1,
        1px  1px 0 #d1d1d1,
        0 -1px 0 #d1d1d1,
        0  1px 0 #d1d1d1,
        -1px  0 0 #d1d1d1,
        1px  0 0 #d1d1d1;
        font-family: 'Times New Roman',serif; font-weight:bold; font-style: italic; font-size: 22px">*** ${globalMenu.get(choice)} ***</div>
                <div style="text-align: center; color: white; text-shadow: -1px -1px 0 #000,
       1px -1px 0 #000,
      -1px  1px 0 #000,
       1px  1px 0 #000,
       0 -1px 0 #000,
       0  1px 0 #000,
      -1px  0 0 #000,
       1px  0 0 #000; font-family: 'Times New Roman',serif; margin-bottom:40%; font-weight:500; font-size: 18px">${globalTexts.get(choice)}</div>`;
    document.querySelector('title').textContent = `${langMatrix.get("mainTitle")}`
}

function getLogout() {
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
