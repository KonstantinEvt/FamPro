async function getNews() {
    loadOnlineUser();
    loadStandardMainPanel();
    document.getElementById("taskPart").innerHTML = `    
    <br>
        <div style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: chocolate">News:</div>
    <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px; margin-right: -5px">
        <input type="radio" class="btn-check" name="news-radio" onclick="getSystemNews()" id="news-radio1" autoComplete="off" checked>
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio1">System 
            <span id="badge1"></span> 
        </label>
        <input type="radio" class="btn-check" name="news-radio" onclick="getCommonNews()" id="news-radio2" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio2">Common 
            <span id="badge2"> </span> 
        </label>
        <input type="radio" class="btn-check" name="news-radio" onclick="getPrivateNews('family')" id="news-radio3" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center;font-size: 14px; color: darkred" for="news-radio3">Family
            <span id="badge3"> </span> 
                </label>
        <input type="radio" class="btn-check" name="news-radio" onclick="getPrivateNews('private')" id="news-radio4" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio4">Private
            <span id="badge4"> </span> 
        </label>
    </div>`
    loadNewsCounts();
    await getSystemNews()
}

async function getSystemNews() {
    cards = [];
    numCards = [];
    document.getElementById("resultPart").innerHTML = `
    <div class="container-fluid row" style=" margin-right: 0">
        <span class="col" style="padding-right: 0;padding-left:0;text-align: right; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">SYSTEM NEWS</span>
        <span class="col-1" style="padding-right: 0;padding-left:0"></span>
        <span class="col-5" style="padding-left:0; padding-right: 0">
           <div class="btn-group-horizontal" role="group" aria-label="Vertical button group" style="margin-left: 5px; text-align: right">
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice1" autoComplete="off">
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice1" onclick="refreshGlobalNews('system',true)">Все</label>
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice2" autoComplete="off" checked>
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice2" onclick="refreshGlobalNews('system',false)">Новые </label>
           </div>
        </span>
    </div>        
    <div id="system-empty" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">
        <div id="system-list" class="card-group" style="height: 84vh; max-width:100%; overflow-x: auto"></div>
    </div>
`
    if (counts[1] !== null && counts[1] !== 0) {
        await loadCategoryNews("system", false)
    } else {
        document.getElementById("system-empty").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">We are glad to see you</div>`;
    }
}

async function refreshGlobalNews(category, choice) {
    document.getElementById(category+'-empty').innerHTML=``;
    await loadCategoryNews(category, choice)
}


async function loadCategoryNews(category, choice) {
    let temp1 = ``;
    let i = 0;
    // cards = [];
    // numCards = [];
    let linkOn = category + "-list";
    if (document.getElementById(linkOn) === undefined || document.getElementById(linkOn) === null) document.getElementById(category + "-empty").innerHTML = `<div id='${linkOn}' class="card-group" style="height: 84vh; max-width:100%; overflow-x: auto"></div>`
    // console.log(document.getElementById(linkOn).innerHTML);
    if (!document.getElementById(linkOn).innerHTML || /^\s*$/.test(document.getElementById(linkOn).innerHTML)) {
        let url = "/news/" + category;
        if (choice) url += "All";
        cards = [];
        numCards = [];
        let newsForm = []
        let promise = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
                // "Access-Control-Allow-Origin": "*"
            }
        }).then(response => response.json()).then(letters => {
            if (letters !== null && !letters.isEmpty) {
                letters.forEach(aloneNew => {
                        let creationDateJS = new Date(aloneNew.creationDate);
                        let fetchData = {
                            timeMessage: getTime(creationDateJS),
                            dateMessage: getDate(creationDateJS),
                            sendingFromJS: aloneNew.sendingFrom,
                            subjectJS: aloneNew.subject,
                            textInfoJS: aloneNew.textInfo,
                            categoryJS: aloneNew.category,
                            attention: aloneNew.attention,
                            imj: ``
                        }
                        i += 1;
                        newsForm.push(fetchData);
                    }
                )
            }
        });
        console.log("new is received", promise);

        for (let j = 0; j < i; j++) {
            let imjUrl = "/file/" + category + "/" + newsForm[j].subjectJS;
            newsForm[j].imj = await loadPicture(imjUrl);
            console.log("picture is loading", newsForm);
            numCards[j] = true;
            let operation=choice?'Remove':'Read'
            let pictureUrl = URL.createObjectURL(newsForm[j].imj)
            cards[j] = `
                <div class="card" style="max-height:700px; min-width:290px; max-width: 290px; margin-bottom: 10px">
                    <img src=${pictureUrl} id="img-conteiner${i}" class="card-img-top" width="290px" alt="tytPhoto">                    
                    <div class="card-body" style="text-align: center">
                        <h5 class="card-title">${newsForm[j].subjectJS}</h5>
                        <p class="card-text">${newsForm[j].textInfoJS}</p>
                        <br>                        
                        <a class="btn btn-outline-warning" onclick="readCategoryMessage('${newsForm[j].sendingFromJS}','${j}','${category}','${operation}')" href="#">${operation}</a>   
                        
                    </div> 
                    <div class="card-footer">
                        <small class="text-body-secondary">
                           ${newsForm[j].dateMessage}
                        </small>
                    </div>                    
                </div>
                <div id="mar${newsForm[j].sendingFromJS}" class="card" style="border:0;max-height:0; min-width:10px; max-width: 10px">
                </div>                
               `
            URL.revokeObjectURL(newsForm[j].imj)
            temp1 += cards[j];
            document.getElementById(linkOn).innerHTML = temp1;
        }
        console.log(newsForm, promise);
    } else {
        console.log("News is already loaded")
        for (let j = 0; j < numCards.length; j++) {
            if (numCards[j]) temp1 += cards[j];
        }
        document.getElementById(linkOn).innerHTML = temp1;
    }

}

async function readCategoryMessage(id, i, category, operation) {
    numCards[i] = false;
    let url = "/news/globalNews" + operation + "/" + category.toUpperCase() + "/" + id
    fetch(url, {
        method: 'GET',
        headers: {},
    }).then(r => r).then(() => {
            if (operation==='Read') {
                counts[0] = counts[0] - 1;
                if (counts[0] !== 0) document.getElementById("newsCount").innerHTML = counts[0] + "";
                else document.getElementById("badge0").innerHTML = "";
                if (category === "system") {
                    counts[1] = counts[1] - 1;
                    if (counts[1] !== 0) document.getElementById("countNew1").innerHTML = counts[1] + "";
                    else {
                        document.getElementById("badge1").innerHTML = "";
                        document.getElementById("system-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">Больше системных сообщений нет</div>`;
                    }
                } else if (category === "common") {
                    counts[2] = counts[2] - 1;
                    if (counts[2] !== 0) document.getElementById("countNew2").innerHTML = counts[2] + "";
                    else {
                        document.getElementById("badge2").innerHTML = "";
                        document.getElementById("common-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">Больше общих сообщений нет</div>`;
                    }
                }
            }
        }
    )
    loadNewsCounts();
    redrawGlobalCards(category);
    // await loadCategoryNews(category, false)
}

function redrawGlobalCards(category){
    let temp1=``;
    for (let j = 0; j < numCards.length; j++) {
        if (numCards[j]) temp1 += cards[j];
    }
    document.getElementById(category+'-list').innerHTML = temp1;
}
async function getCommonNews() {
    cards = [];
    numCards = [];
    document.getElementById("resultPart").innerHTML = `
                    <div style="padding-right: 0;padding-left:0;text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">COMMON NEWS </div>
                    <div id="common-empty" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">
            <div class="card-group" id="common-list" style="height: 84vh; max-width: 100%; overflow-x: auto" >           
            </div> </div>`;
    if (counts[2] !== null && counts[2] !== 0) {
        await loadCategoryNews("common", false);
    } else {
        document.getElementById("common-empty").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">Common messages are absent</div>`;
    }
}

async function getFamilyNews() {
    cards = [];
    rawNews = null;
    let category = "family";
    document.getElementById("resultPart").innerHTML = `
            <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Family News ***</div>
            <div class="btn-group-horizontal" role="group" aria-label="Vertical button group" style="margin-left: 5px; text-align: right">
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice1" autoComplete="off">
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice1" onclick="refreshNews('${category}',true)">Все</label>
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice2" autoComplete="off" checked>
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice2" onclick="refreshNews('${category}',false)">Новые </label>
            </div>
            <nav id="navbar-family" class="navbar bg-body-tertiary no-gutters">
                <ul class="nav nav-pills" >
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" role="button" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" href="#first-message">Начало</a>
                    </li>  
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">Список</a>
                        <ul class="dropdown-menu ">
                             <span id="dropdown-message-list"> 
                            </span>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 12px" href="#last-message">Last</a></li> 
                        </ul>
                    </li>    
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" role="button" href="#last-message">Конец</a>
                    </li>
                </ul>
            </nav>
            <div id="family-list" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center"> </div>
            <div data-bs-spy="scroll" style="height:750px; overflow: auto" data-bs-target="#navbar-family" data-bs-smooth-scroll="true" class="scrollspy-example bg-body-tertiary" tabindex="0">
                <div id="first-message"></div>
                <div id="message-list">  </div>
                <div id="last-message"></div>
            </div> `
    if (counts[3] !== null && counts[3] !== 0) {
        await loadNewNews("family", false);
    } else {
        document.getElementById("family-list").innerHTML = "Messages are absent";
    }
}

async function loadingIndividualNews(category, choice) {
    let num = 0;
    let lets = [];
    let url = "/news/" + category;
    if (choice) url += "All";
    await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(response => response.json()).then(letters => {
        if (letters !== null && !letters.isEmpty) {
            let temp0 = ``;
            let temp1 = ``;
            letters.forEach(aloneNew => {
                let from = "Anonymous";
                let contactImage = ``;
                let i = 0;
                if (contacts !== null) {
                    while (i < contacts.length) {
                        if (contacts[i].externId === aloneNew.sendingFrom) {
                            from = contacts[i].name;
                            i = contacts.length;
                            if (contacts.imj !== null && contacts.imj !== ``) contactImage = contacts.imj;
                        }
                        i++;
                    }
                }
                if (from === "Anonymous" && aloneNew.sendingFromAlt !== null) from = aloneNew.sendingFromAlt;

                let creationDateJS = new Date(aloneNew.creationDate);
                let fetchData = {
                    idJS: aloneNew.id,
                    externIdJS: aloneNew.externId,
                    timeMessage: getTime(creationDateJS),
                    dateMessage: getDate(creationDateJS),
                    sendingFromJS: from,
                    subjectJS: aloneNew.subject,
                    textInfoJS: aloneNew.textInfo,
                    attention: aloneNew.attention,
                    categoryJS: aloneNew.category.toLowerCase(),
                    imj: ``,
                    alreadyRead: aloneNew.alreadyRead,
                    num: num
                }

                if ((fetchData.attention === "VOTING_POSITIVE" || fetchData.attention === "POSITIVE" ||fetchData.subjectJS === "Contact added" || fetchData.subjectJS === "Accept change") && defaultPhotos.approved !== null) fetchData.imj = defaultPhotos.approved;
                if ((fetchData.attention === "VOTING_NEGATIVE" || fetchData.attention === "NEGATIVE" || fetchData.subjectJS === "Reject change" || fetchData.subjectJS === "Contact reject") && defaultPhotos.rejected !== null) fetchData.imj = defaultPhotos.rejected;
                if ((fetchData.attention === "VOTING"||fetchData.attention === "VOTING_REQUESTER") && defaultPhotos.election !== null) fetchData.imj = defaultPhotos.election;
                if ((fetchData.subjectJS === "Request for contact" || fetchData.subjectJS === "Contact request") && defaultPhotos.contact !== null) fetchData.imj = defaultPhotos.contact;
                if ((fetchData.attention === "LINK") && defaultPhotos.linking !== null) fetchData.imj = defaultPhotos.linking;
                if (fetchData.imj === ``) fetchData.imj = contactImage;
                if (fetchData.imj === `` && defaultPhotos.person !== null) fetchData.imj = defaultPhotos.person;

                if (fetchData.externIdJS===null) fetchData.externIdJS=fetchData.idJS;

                lets[num] = fetchData;
                cards[num] = drawCard(fetchData, choice);
                temp0 += cards[num].top;
                temp1 += cards[num].body;
                num += 1;
            });
            rawNews = Array.from(lets);
            paintIndividualNews(category, temp0, temp1);


        }
    });
    return lets;
}

function changeCountsByRead(category) {
    counts[0] = counts[0] - 1;
    if (counts[0] !== 0) document.getElementById("newsCount").innerHTML = counts[0] + "";
    else document.getElementById("badge0").innerHTML = "";
    if (category === "family") {
        counts[3] = counts[3] - 1;
        if (counts[3] !== 0) document.getElementById("countNew3").innerHTML = counts[3] + "";
        else {
            document.getElementById("badge3").innerHTML = "";
            document.getElementById("message-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">Семейных сообщений нет</div>`;
        }
    } else if (category === "private") {
        counts[4] = counts[4] - 1;
        if (counts[4] !== 0) document.getElementById("countNew4").innerHTML = counts[4] + "";
        else {
            document.getElementById("badge4").innerHTML = "";
            document.getElementById("message-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">Messages are absent</div>`;
        }
    }
}

function paintIndividualNews(category, temp0, temp1) {
    document.getElementById("dropdown-message-list").innerHTML = temp0;
    document.getElementById("message-list").innerHTML = temp1;
    if (temp1 === ``) document.getElementById("message-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">Messages are absent</div>`;
    return "ok";
}

function drawCardBody(letter, choice) {
    let pictureUrl = URL.createObjectURL(letter.imj);
    let temp = ` 
    <div class="card" id="message${letter.idJS}" style="vertical-align:middle; horiz-align: center; margin-bottom: 10px; padding: 0">        
        <div class="card-header" style="text-align: center; color: darkred;font-family: 'Times New Roman',serif">${letter.subjectJS}
        <div class="flex-row" style="text-align: center;font-family: 'Times New Roman',serif; font-size: 12px;color: black;margin-right: 0;padding-right: 10px">
                                <span class="col-6" style="text-align:left; min-width: 50px;margin: 0;padding: 0">${letter.dateMessage}  </span>                         
                                <span class="col" style="margin: 0;padding: 0"></span>
                                <span class="col-5" style="text-align:right; min-width: 50px;margin: 0;padding: 0">${letter.timeMessage}</span>
                        </div>
        </div>
        <div class="row g-0 no-gutters" style="vertical-align:middle; align-items:center;">
             <div class="col-3" style="padding: 10px 10px 0;max-width: 150px">
                <img src=${pictureUrl} class="card-img" width="100px" alt="tytPhoto">
                <div style="text-align: center; font-size: 14px; color: darkgreen;padding-left: 5px;padding-right: 0;margin: 0"> ${letter.sendingFromJS}</div>
            </div>
            <div class="col-9" >
                <div class="card-body" style="vertical-align: top; text-align: center;padding: 5px">
                    <div class="card-title" style="margin: 0">                        
                    </div>
                    <div class="card-text" style="text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 14px">${letter.textInfoJS} </div>
                </div>
            </div>
        </div>
        <div class="card-footer" style=" text-align: center">            
                <div class="container-fluid row" style="font-size: 16px; margin-right: 0; text-align: center">
                    ` + drawButtons(letter, choice) + `
                </div>            
        </div>      
    </div>`;
    URL.revokeObjectURL(letter.imj);
    return temp;
}

function drawButtons(letter, choice) {
    let temp;
    if (choice && letter.alreadyRead && letter.sendingFromJS !== "Informer") temp = `<span class="col" style=" text-align: center;color:chocolate">Letter is already read"</span>
                <span class="col-2" style="text-align:right; padding-right: 10px">
                    <a href="#" type="button" class="btn btn-outline-danger" onClick="deleteMessage('${letter.idJS}','${letter.categoryJS}', ${letter.num}, ${choice})">Delete</a></span>`;
    else if (choice && letter.alreadyRead) {
        temp = `<span class="col" style=" text-align: center;color:chocolate">Letter is already read"</span>`
    } else {
        if (letter.sendingFromJS === "Informer")
            switch (letter.attention) {
                case "VOTING":
                case "LINK":
                    temp = `<span class="col-2">
                            <a type="button" class="btn btn-outline-success"
                                onClick="acceptMessage('${letter.externIdJS}', '${letter.num}', '${letter.categoryJS}')" href="#">Accept</a></span>
                        <span class="col"></span>
                        <span class="col-2" style="text-align:right; padding-right: 10px">
                            <a type="button" class="btn btn-outline-danger" onClick="rejectMessage('${letter.externIdJS}', '${letter.num}', '${letter.categoryJS}')"
                                href="#">Reject</a></span>`;
                    break;
                case "RIGHTS":
                case "MODERATE":
                case "NEGATIVE":
                case "DATE":
                case "POSITIVE":
                    temp = `<span class="col-4"></span>
                        <span class="col">
                            <a type="button" class="btn btn-outline-warning"
                                onClick="seeMessage('${letter.externIdJS}', '${letter.num}', '${letter.categoryJS}')" href="#">See</a></span>
                        <span class="col-4"></span>`;
                    break;
                case "VOTING_NEGATIVE":
                case "VOTING_POSITIVE":
                case "VOTING_REQUESTER":temp = `<span class="col-4"></span>
                        <span class="col">
                            <a type="button" class="btn btn-outline-warning"
                                onClick="readResult('${letter.externIdJS}', '${letter.num}', '${letter.categoryJS}')" href="#">See</a></span>
                        <span class="col-4"></span>`;
                    break;
                default:
                    temp = "This is poltergeist";
            }
        else temp = `<span class="col-2">
                    <a href="#" type="button" class="btn btn-outline-success" onClick="seeMessage('${letter.idJS}','${letter.categoryJS}', ${letter.num}, ${choice})">See</a></span>
                <span class="col"></span>
                <span class="col-2" style="text-align:right; padding-right: 10px">
                    <a href="#" type="button" class="btn btn-outline-danger" onClick="deleteMessage('${letter.idJS}','${letter.categoryJS}', ${letter.num}, ${choice})">Delete</a></span>`
    }
    return temp;
}

// function drawUsual(letter, choice) {
//     return `
//                         <div style="text-align: center;font-size: 20px">------ ***** ------</div>
//                         <div id="message${letter.idJS}">
//                             <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">${letter.subjectJS}</div>
//                             <div class="container-fluid row" style="text-align: center; color: darkred;font-family: 'Times New Roman',serif; font-size: 16px">
//                                 <span class="col-2">${letter.dateMessage}  </span>
//                                 <span class="col"></span>
//                                 <span class="col-2">${letter.timeMessage}</span>
//                             </div>
//                             <div style="text-align: center; color: darkred;">Отправитель: </div>
//                             <div style="text-align: center; color: darkgreen"> ${letter.sendingFromJS}</div>
//                             <br>
//                             <div style="text-align: center; color: darkred">Сообщение:</div>
//                             <div style=" text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 14px">${letter.textInfoJS} </div>
//                             <br>
//                             <div class="container-fluid row" style="font-size: 16px">
//                             <span class="col-2">
//                                 <button type="button" class="btn btn-outline-success" data-toggle="modal" onclick="seeMessage('${letter.idJS}', '${choice}')" data-target="#see${letter.idJS}">See</button></span>
//                             <span class="col"></span>
//                             <span class="col-2" ><button type="button" class="btn btn-outline-danger" data-toggle="modal" onclick="deleteMessage('${letter.idJS}', '${choice}')" data-target="#delete${letter.idJS}">Delete</button></span>
//                             <span class="col-1" style="width: 40px"></span>
//                             </div>
//                             <br>
//                         </div>`;
// }

function drawCard(letter, choice) {
    let letterBody = drawCardBody(letter, choice);

    return {
        top: `<li><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 10px" href="#message${letter.idJS}">
                      <div class="container-fluid row" style="width: 99%; margin: 0;padding: 0">
                         <span class="col-5" style="text-align: left;padding: 0;margin-left: 5px">${letter.dateMessage}</span>
                         <span class="col-6" style="text-align: right;padding: 0;margin-right: 5px">${letter.sendingFromJS}</span>
                      </div></a></li>`,
        body: letterBody
    }
}

async function refreshNews(category, choice) {
    rawNews = null;
    cards = [];
    await loadNewNews(category, choice)
}

async function loadNewNews(category, choice) {
    if (rawNews === null) {
        let raw = await loadingIndividualNews(category, choice);
        console.log(raw);
    } else {
        console.log(rawNews);
        let temp0 = ``;
        let temp1 = ``;
        for (let i = 0; i < rawNews.length; i++) {
            if (rawNews[i] !== null && rawNews[i] !== undefined && (choice || !rawNews[i].alreadyRead)) {
                temp0 += cards[i].top;
                temp1 += cards[i].body;
            }
        }
        console.log("prepare to painting");
        paintIndividualNews(category, temp0, temp1);
    }
}

async function getPrivateNews(category) {
    cards = [];
    rawNews = null;
    let categoryUp = (category + '').toUpperCase();
    document.getElementById("resultPart").innerHTML = `
            <div class="container-fluid row" style=" margin-right: 0">
            <span class="col" style="padding-right: 0;padding-left:0;text-align: right; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">${categoryUp} NEWS</span>
            <span class="col-1" style="padding-right: 0;padding-left:0"></span>
           <span class="col-5" style="padding-left:0; padding-right: 0">
           <div class="btn-group-horizontal" role="group" aria-label="Vertical button group" style="margin-left: 5px; text-align: right">
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice1" autoComplete="off">
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice1" onclick="refreshNews('${category}',true)">Все</label>
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice2" autoComplete="off" checked>
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice2" onclick="refreshNews('${category}',false)">Новые </label>
            </div></span>
            </div>
            <nav id="navbar-${category}" class="navbar bg-body-tertiary no-gutters">
                <ul class="nav nav-pills" >
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" role="button" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" href="#first-message">Начало</a>
                    </li>  
                    <li class="nav-item dropdown" >
                        <a class="nav-link dropdown-toggle" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">Список</a>
                        <ul class="dropdown-menu" >
                             <span id="dropdown-message-list"> 
                            </span>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 12px;text-align: center" href="#last-message">Last</a></li> 
                        </ul>
                    </li>    
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" role="button" href="#last-message">Конец</a>
                    </li>
                </ul>
            </nav>            
            <div data-bs-spy="scroll" style="height: 73vh; overflow: auto" data-bs-target="#navbar-${category}" data-bs-smooth-scroll="true" class="scrollspy-example bg-body-tertiary" tabindex="0">
                <div id="first-message"></div>
                <div id="message-list">  </div>
                <div id="last-message"></div>
            </div>`
    if ((category === "private" && counts[4] !== null && counts[4] !== 0) || (category === "family" && counts[3] !== null && counts[3] !== 0)) {
        await loadNewNews(category, false);
    } else {
        document.getElementById(`message-list`).innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">Messages are absent</div>`;
    }
}

async function acceptMessage(id, num, category) {
    fetch("/message/accept/" + id, {method: "GET"}).then(r => r.text())
        .then(r => console.log(r));
    rawNews.forEach(letter => {
        if (letter.externIdJS === id) letter.alreadyRead = true;
    })
    changeCountsByRead(category);
    delete rawNews[num];
    delete cards[num];
    await loadNewNews(category, false);
}

async function rejectMessage(id, num, category) {
    fetch("/message/reject/" + id, {method: "GET"}).then(r => r.text())
        .then(r => console.log(r));
    rawNews.forEach(letter => {
        if (letter.externIdJS === id) letter.alreadyRead = true;
    })
    changeCountsByRead(category);
    delete rawNews[num];
    delete cards[num];
    await loadNewNews(category, false);
}

async function readResult(id, num, category) {
    let url = "/message/readResult/" + id;
    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(r => console.log("Answer:", r))
    changeCountsByRead(category);
    delete rawNews[num];
    delete cards[num];
    await loadNewNews(category, false);
}

// private String sendingFrom;
// private String sendingTo;
// private Date creationDate;
// private String subject;
// private byte[] image;
// private String textInfo;
// private NewsCategory category;
// private boolean alreadyRead;
function loadPrivateNews(choice) {
    let temp0 = ``;
    let temp1 = ``;
    let url;
    if (choice === 0) url = "/news/individual"; else url = "/news/individualAll";
    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(response => response.json()).then(letters => {
        if (letters !== null && !letters.isEmpty) {
            letters.forEach(aloneNew => {
                let creationDate = new Date(aloneNew.creationDate);
                let timeMessage = getTime(creationDate);
                let dateMessage = getDate(creationDate);
                let from = "Anonymous";
                let i = 0;
                if (contacts !== null) {
                    while (i < contacts.length) {
                        if (contacts[i].externId === aloneNew.sendingFrom) {
                            from = contacts[i].name;
                            i = contacts.length;
                        }
                        i++;
                    }
                }
                if (from === "Anonymous" && aloneNew.sendingFromAlt !== null) from = aloneNew.sendingFromAlt;
                temp0 += `
                         <li >
                         <a class="dropdown-item" style="width: 95%; color: goldenrod;font-family: 'Times New Roman',serif; font-size: 11px; margin: 2px;padding: 2px" href="#message${aloneNew.id}">
                         <div class="container-fluid row" style="width: 99%; margin: 0;padding: 0">
                         <span class="col-5" style="text-align: left;padding: 0;margin-left: 5px">${dateMessage}</span>
                         <span class="col-6" style="text-align: right;padding: 0;margin-right: 5px">${from}</span>
                         </div>
                         </a>
                         </li>`

                temp1 += `
                        <div style="text-align: center;font-size: 20px">------ ***** ------</div>
                        <div id="message${aloneNew.id}">           
                            <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">${aloneNew.subject}</div>
                            <div class="container-fluid row" style="text-align: center; color: darkred;font-family: 'Times New Roman',serif; font-size: 16px">
                                <span class="col-2">${dateMessage}  </span>                         
                                <span class="col"></span>
                                <span class="col-2">${timeMessage}</span>
                            </div>
                            <div style="text-align: center; color: darkred;">Отправитель: </div>
                            <div style="text-align: center; color: darkgreen"> ${from}</div>
                            <br>
                            <div style="text-align: center; color: darkred">Сообщение:</div> 
                            <div style=" text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 14px">${aloneNew.textInfo} </div>
                            <br>
                            <div class="container-fluid row" style="font-size: 16px">
                            <span class="col-2">
                                <button type="button" class="btn btn-outline-success" data-toggle="modal" onclick="seeMessage('${aloneNew.id}', '${choice}')" data-target="#see${aloneNew.id}">See</button></span>
                            <span class="col"></span>
                            <span class="col-2" ><button type="button" class="btn btn-outline-danger" data-toggle="modal" onclick="deleteMessage('${aloneNew.id}', '${choice}')" data-target="#delete${aloneNew.id}">Delete</button></span>
                            <span class="col-1" style="width: 40px"></span>
                            </div>
                            <br>
                        </div>`;
                document.getElementById("dropdown-message-list").innerHTML = temp0;
                document.getElementById("message-list").innerHTML = temp1;

            })
        } else {
            document.getElementById("dropdown-message-list").innerHTML = 'пусто';
            document.getElementById("message-list").innerHTML = "Больше сообщений нет";
        }
    });
    console.log("Value", temp1)
}

async function seeMessage(id, category, num, choice) {
    // choice = choice === "true";
    let url = "/message/readMessage/" + id;
    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(r => console.log("Answer:", r))
    changeCountsByRead(category);
    if (choice) {
        rawNews[num].alreadyRead = true;
        cards[num] = drawCard(rawNews[num], choice);
    } else {
        delete rawNews[num];
        delete cards[num];
    }
    await loadNewNews(category, choice)
}

async function deleteMessage(id, category, num, choice) {
    // choice = choice === "true";
    let url = "/message/deleteMessage/" + id;
    await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    });
    if (!rawNews[num].alreadyRead) changeCountsByRead(category);
    delete rawNews[num];
    delete cards[num];
    await loadNewNews(category, choice)
}