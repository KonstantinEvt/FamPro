setLanguishMatrix();
drawNavPanel(callbackPage);
changeLang(currentLang, true, 'about');

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

function drawNavPanel(callbackPage) {
    let temp = `<ul class="navbar-nav">`
    temp += drawNavPanelNotes();
    temp += ` <li class="nav-item"> <a class="nav-link " style="color: darkred" id="back" aria-current="page" href="${callbackPage}">${langMatrix.get("back")}</a></li>`;
    temp += `</ul>`;
    document.getElementById("navbarNavDropdown").innerHTML = temp;
}

function drawNavPanelNotes() {
    let temp = ``;
    for (let i = 1; i < 5; i++) {
        temp += `  
        <li class="nav-item">
            <a class="nav-link " style="color: darkred" aria-current="page" id="mainMenu${i}" href="#" onClick="getCommonInfo(${i}*1000)">${mainMenu.get(i * 1000)}</a>
        </li>`
    }
    return temp;
}

function getPortalInfo(chose) {
    let choseChapter = (chose / 1000 | 0) * 1000;
    let chosePart = (chose / 100 | 0) * 100;
    let temp = `<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** ${globalMenu.get(choseChapter)} ***</div>`
    if ((chose - choseChapter) !== 0) {
        if ((chose - chosePart) === 0) chose++;
        temp += drawAccordion(chosePart, chose);
        document.getElementById("resultPart").innerHTML = temp;
    } else {
        temp += `${globalTexts.get(chose)}`
        document.getElementById("resultPart").innerHTML = temp;
        if (!loadedParts.get(chose)) loadLanguishContent2(chosePart);
    }
}

function getCommonInfo(page) {
    drawStandardMainPanel();
    let temp = `<br>
        <div style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: chocolate">${langMatrix.get('chapter')}:</div>`
    if (globalMenu.get(page + 100) !== null) temp += `<div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px; margin-right: -5px">`
        + drawMenu(page) + `</div>`
    document.getElementById("taskPart").innerHTML = temp;
    getPortalInfo(page);
}

function drawMenu(page) {
    let temp = ``
    let numberChapter = (page / 1000 | 0) * 1000;
    if (numbersOfParts.get(numberChapter) === 0
        || numbersOfParts.get(numberChapter) === null
        || globalMenu.get(numberChapter + 100) === null
        || globalMenu.get(numberChapter + 100) === undefined) temp += globalTexts.get(2);
    else
        for (let i = 1; i <= numbersOfParts.get(numberChapter); i++) {
            temp += `<input type="radio" class="btn-check" name="news-radio" onclick="getPortalInfo(${numberChapter + i * 100})" id="news-radio${i}" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio${i}">${localMenu.get(numberChapter + i * 100)}</label>`
        }
    return temp;
}

function drawAccordion(chosePart, chose) {
    let number = numbersOfParts.get(chosePart)
    let temp = `
        <div class="accordion"
             style=" 
             --bs-accordion-btn-bg: rgba(234,154,50,0.35);
             --bs-accordion-bg: rgba(234,154,50,0.15); 
             --bs-accordion-active-bg: rgba(234,154,50,0.75);  
             --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgba(234,154,50,0.5);"
             id="accordionInfo"> `
    for (let i = 1; i <= number; i++) {
        temp += drawAccordionPage(chosePart + i, chose);
    }
    temp += `</div>`
    return temp;
}

function drawAccordionPage(part, chose) {
    return (part === chose) ? `<div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button show" style="padding-bottom:8px; padding-top:8px; color: darkred"
                            type="button" data-bs-toggle="collapse" data-bs-target="#page${part}" aria-expanded="true"
                            aria-controls="#page${part}">
                        ${globalMenu.get(part)}
                    </button>
                </h2>
                <div id="page${part}" class="accordion-collapse collapse show" data-bs-parent="#accordionInfo">
                    <div class="accordion-body">
                        <div class="container-fluid row mh-100 no-gutters">
                            <span class="col-1" style="width: 5%"></span>
                            <div>${globalTexts.get(part)}</div>
                            </div></div></div></div>` :
        `<div class="accordion-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred"
                            type="button" data-bs-toggle="collapse" data-bs-target="#page${part}" aria-expanded="false"
                            aria-controls="#page${part}">
                        ${globalMenu.get(part)}
                    </button>
                </h2>
                <div id="page${part}" class="accordion-collapse collapse" data-bs-parent="#accordionInfo">
                    <div class="accordion-body">
                        <div class="container-fluid row mh-100 no-gutters">
                            <span class="col-1" style="width: 5%"></span>
                            <div style="text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 16px">${globalTexts.get(part)}</div>
                            </div></div></div></div>`
}

function getCommon(choice) {
    if (document.getElementById("infoMain") !== undefined && document.getElementById("infoMain") !== null) {
        document.getElementById("infoMain").innerHTML = `${langMatrix.get("infoMain")}`;
        document.getElementById("mainPanel").innerHTML = `<div style="background-color:rgb(255,255,255,60%);  text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** ${globalMenu.get(choice)} ***</div>
                <div style="background-color:rgb(255,255,255,60%); height: 100%; text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 14px">${globalTexts.get(choice)}</div>`;
        // document.querySelector('title').textContent = `${langMatrix.get("aboutTitle")}`
    }
        // else document.querySelector('title').textContent = `${langMatrix.get("mainTitle")}`
}

function getInfoPage(chose) {
    document.getElementById("resultPart").innerHTML = `${globalTexts.get(chose)}`;

}
async function loadPicture(url) {
    let array = [];
    await fetch(url, {
        method: "GET"
    }).then(r => r.blob()).then(cou => {
        array = cou;
    }).then(cou => console.log("Изображение загружено", cou));
    return array;
}

async function loadDefaultPhotos() {
    let url = "/file/defaultPhoto/"
    return {
        person: await loadPicture(url + "person.jpg"),
        election: await loadPicture(url + "election.jpg"),
        approved: await loadPicture(url + "approved.jpg"),
        rejected: await loadPicture(url + "rejected.jpg"),
        linking: await loadPicture(url + "linking.jpg"),
        contact: await loadPicture(url + "contact.jpg"),
        photono: await loadPicture(url + "photono.jpg"),
    };
}



