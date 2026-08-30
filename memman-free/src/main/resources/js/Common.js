const langEn = new Map([
    ['langButton', '&nbsp;&#x1F1FA;&#x1F1F8;&nbsp;'],
    ['mainTitle', 'Memman'],
    ['secondaryTitle', 'Memory of Mankind'],
    ['aboutTitle', 'About'],
    ['postAnn', 'The ancestors are watching you'],
    ['langTabLabel', 'Languish'],
    ['selectLang', 'Select'],
    ['serviceButton', 'Service'],
    ['aboutButton', 'About'],
    ['manButton', 'Man'],
    ['preAnn', 'A tree without roots - firewood'],
    [`chapter`, `Chapter`],
    [`infoMain`, `Info`],
    [`back`, `Return`],
    ['onWork', 'This content is under development.']
]);
const langRu = new Map([
    ['langButton', '&nbsp;&#x1F1F7;&#x1F1FA;&nbsp;'],
    ['mainTitle', 'Мемман'],
    ['secondaryTitle', 'Родовая память'],
    ['aboutTitle', 'О проекте'],
    ['postAnn', 'Предки следят за тобой'],
    ['langTabLabel', 'Язык'],
    ['selectLang', 'Выбрать'],
    ['serviceButton', 'Служба'],
    ['aboutButton', 'О&nbsp;проекте'],
    ['manButton', 'Человек'],
    ['preAnn', 'Дерево без корней - дрова'],
    [`chapter`, `Раздел`],
    [`infoMain`, `Инфо`],
    [`back`, `Вернуться`],
    ['onWork', 'Данный контент находится в разработке']
]);

let langMatrix;
let mainMenu;
let globalMenu=new Map([]);
let globalTexts=new Map([]);
let localMenu=new Map([]);
let numbersOfParts = new Map([]);
let loadedParts = new Map([]);

changeLang(currentLang, true, `main`);

function setLanguishMatrix() {
    if (currentLang === `ru`) {
        mainMenu = mainMenuRu;
        globalTexts = globalTextsRu;
    } else {
        mainMenu = mainMenuEn;
        globalTexts = globalTextsEn;
    }
    globalMenu.clear();
    addMatrixContent(globalMenu, mainMenu);
}

function selectLangMatrix() {
    return (currentLang === `ru`) ? langRu : langEn
}

function changeLang(lang, needReload, page) {
    if (currentLang !== lang || needReload) {
        currentLang = lang;
        langMatrix = selectLangMatrix();

        for (let langElement of langMatrix.keys()) {
            let temp = document.getElementById(langElement);
            if (temp !== undefined && temp !== null) temp.innerHTML = langMatrix.get(langElement);
        }
        if (document.getElementById("mainPanel")!==undefined&& document.getElementById("mainPanel")!==null&& page === "about") {
            setLanguishMatrix();
            clearLoads();
            for (let i = 1; i < 5; i++) {
                document.getElementById(`mainMenu${i}`).innerHTML = mainMenu.get(i * 1000);
            }
            if (document.getElementById("mainPanel").innerHTML.trim() === ``) getCommon(0); else {
                getCommon(1);
                if (localStorage.getItem('justChange') !== null && (Date.now() - localStorage.getItem('justChange')) / 60000 < 10) setRegisterLang(lang).then(() => console.log("Запрос на установку языка отправлен", currentLang));
            }
            loadLanguishContent1();
            console.log(numbersOfParts);
            document.querySelector('title').textContent = `${langMatrix.get("aboutTitle")}`
        } else document.querySelector('title').textContent = `${langMatrix.get("mainTitle")}`;
        document.getElementById(`${currentLang}Loc`).checked = true;
        localStorage.setItem('currentLang', currentLang);
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

async function setRegisterLang(lang) {
    let url = `/front/languish/set?loc=${lang}`;
    await fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(response =>
        response.text())
        .then(t => console.log("Результат установки языка:", t));
}
function addMatrixContent(matrix, freshContent) {
    for (let [key, value] of freshContent) {
        if (!matrix.has(key)) matrix.set(Number(key), value);
    }}
function clearLoads() {
    loadedParts.set(1000, false);
    loadedParts.set(2000, false);
    loadedParts.set(3000, false);
    loadedParts.set(4000, false);
}