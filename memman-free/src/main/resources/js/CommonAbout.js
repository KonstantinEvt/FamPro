

let tempLoad;



function loadLanguishContent1() {
    fetch(`/free/languish/beginLoad/${currentLang}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(response => response.json())
        .then(async loadChapter => {
            console.log("first languish pack loaded");
            tempLoad = {
                globalMenu: (new Map(Object.entries(loadChapter.globalMenu))),
                globalTexts: (new Map(Object.entries(loadChapter.globalTexts))),
                numbersOfParts: (new Map(Object.entries(loadChapter.numbersOfParts))),
                localMenu: (new Map(Object.entries(loadChapter.localMenu)))
            };
        }).then(() => {
        addMatrixContent(globalMenu, tempLoad.globalMenu);
        addMatrixContent(globalTexts, tempLoad.globalTexts);
        addMatrixContent(numbersOfParts, tempLoad.numbersOfParts);
        addMatrixContent(localMenu, tempLoad.localMenu);
    });
}

function loadLanguishContent2(part) {
    fetch(`/free/languish/parts/${currentLang}/${part}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(response => response.json())
        .then(async loadChapter => {
            console.log("second languish pack loaded", part);
            tempLoad = {
                globalTexts: (new Map(Object.entries(loadChapter.globalTexts)))
            };
        }).then(() => {
        loadedParts.set(part, true);
        addMatrixContent(globalTexts, tempLoad.globalTexts);
    });
}