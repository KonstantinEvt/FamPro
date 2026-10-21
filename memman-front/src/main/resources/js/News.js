let tempCount;

clearNewsLoads();

function clearNewsLoads() {
    for (let i = 1; i < 5; i++) {
        tempChoice.set(i, new Map([[false, false], [true, false]]));
        rawNewsMap.set(i, [])
    }
}

async function getNews() {
    clearNewsLoads();
    await getContacts();
    tempCount = counts;
    loadStandardMainPanel();
    let temp = `
        <div class="main_select" style="margin-top:24px;" >${langMatrix.get("category")}</div>
        <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="background-color:rgb(255,255,255,40%);margin-left: 7px; margin-right: 4px; min-width: 80%">`
    for (let i = 1; i < numOfPart.get(1); i++) {
        temp += `<input type="radio" class="btn-check" name="news-radio" onclick="menuFunc${1000}(${i})" id="news-radio${i}" autoComplete="off" >
        <label class="btn btn-outline-warning" style="padding-right: 3px; padding-left: 3px; text-align:center; font-size: 14px; color: darkred" for="news-radio${i}">${localMenu.get(1000 + i * 100)} 
            <span id="badge${i}"></span> 
        </label>`;
    }
    temp += `</div>`
    document.getElementById("taskPart").innerHTML = temp;
    document.getElementById("news-radio1").checked = true;
    await menuFunc1000(1);
    drawNewsCounts();
}

function getCategory(chapter) {
    let category;
    switch (chapter) {
        case 1:
            category = "system";
            break;
        case 2:
            category = "common";
            break;
        case 3:
            category = "family";
            break
        default:
            category = "private"
    }
    return category;
}

async function menuFunc1000(chapter) {
    let category = getCategory(chapter);
    drawHatOfNews(chapter, category);
    if (counts[chapter] !== null && counts[chapter] !== 0) {
        drawPlaceOfNews(chapter, category, true);
        await refreshGlobalNews(chapter, category, false)
    } else drawPlaceOfNews(chapter, category, false)
}

function drawHatOfNews(chapter, category) {
    document.getElementById("resultPart").innerHTML = `<div class="container-fluid row" style=" margin-right: 0; margin-bottom: 10px; padding:0">
        <span class="col main_title">${globalTexts.get(1001 + chapter * 100)}</span>
        <span class="col-1" style="padding-right: 0;padding-left:0"></span>
        <span class="col-4" style="padding-left:0; padding-right: 0">
           <div class="btn-group-horizontal" role="group" aria-label="Vertical button group" style="margin-left: 5px; text-align: right">
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice1" autoComplete="off">
                <label class="btn btn-outline-warning" style="padding: 2px 5px 2px 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice1" onclick="refreshGlobalNews(${chapter}, '${category}', true)">${langMatrix.get("all")}</label>
                <input type="radio" class="btn-check" name="news-choice"  id="news-choice2" autoComplete="off" checked>
                <label class="btn btn-outline-warning" style="padding: 2px 5px 2px 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice2" onclick="refreshGlobalNews(${chapter},'${category}', false)">${langMatrix.get("new")} </label>
           </div>
        </span>
    </div>
    <div id="${category}-place" style="height: 74vh;color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center;text-shadow: -1px -1px 0 #f1f1f1,
        1px -1px 0 #f1f1f1,
        -1px  1px 0 #f1f1f1,
        1px  1px 0 #f1f1f1,
        0 -1px 0 #f1f1f1,
        0  1px 0 #f1f1f1,
        -1px  0 0 #f1f1f1,
        1px  0 0 #f1f1f1;"></div>`;
}

function drawPlaceOfNews(chapter, category, existNews) {
    if (existNews) {
        let pictureUrl = URL.createObjectURL(defaultPhotos.groundTrees);
        document.getElementById(category + "-place").innerHTML = `             
            <div style="background: url(${pictureUrl}) repeat">
            <nav id="navbar-${category}" class="navbar no-gutters" style="background: rgb(255,255,255,40%)">
                <ul class="nav nav-pills" >
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" role="button" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" href="#first-message">${langMatrix.get('begin')}</a>
                    </li>  
                    <li class="nav-item dropdown" >
                        <a class="nav-link dropdown-toggle" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">${langMatrix.get('list')}</a>
                        <ul class="dropdown-menu"  style="max-height:350px;overflow: auto; webkit-scrollbar: " ="background-color: ">
                             <span id="dropdown-message-list"> 
                            </span>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 12px;text-align: center" href="#last-message">${langMatrix.get('end')}</a></li> 
                        </ul>
                    </li>    
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" role="button" href="#last-message">${langMatrix.get('end')}</a>
                    </li>
                </ul>
            </nav>            
            <div data-bs-spy="scroll" style="background: rgb(255,255,255,40%); height: 71vh; overflow: auto" data-bs-target="#navbar-${category}" data-bs-smooth-scroll="true" class="scrollspy-example" tabindex="0">
                <div id="first-message"></div>
                <div class="card-group" id="message-list"></div>
                <div id="last-message"></div>
            </div>
            </div>`;
        URL.revokeObjectURL(defaultPhotos.groundTrees);
    } else document.getElementById(category + "-place").innerHTML = globalTexts.get(1002 + chapter * 100);
}

async function refreshGlobalNews(chapter, category, choice) {
    console.log("Выбор (расшифровка): ", (choice) ? "Все" : "Новые");
    console.log("Кол-во загруженных новостей до начала просмотра: ", rawNewsMap.get(chapter).length);
    console.log("Счетчик новых новостей в просмотре: ", tempCount[chapter]);
    console.log("Счетчик новых новостей в общем: ", counts[chapter]);
    console.log("Загружены ли новости выбора до начала просмотра: ", tempChoice.get(chapter).get(choice));
    if ((!choice && (counts[chapter] === null || counts[chapter] === 0))
        || (choice && tempChoice.get(chapter).get(true) && rawNewsMap.get(chapter).length === 0)) {
        drawPlaceOfNews(chapter, category, false)
    } else {
        let newsCards;
        if (tempCount[chapter] === counts[chapter] && tempChoice.get(chapter).get(choice) && rawNewsMap.get(chapter).length !== 0) {
            drawPlaceOfNews(chapter, category, true);
            drawNewsGlobal(rawNewsMap.get(chapter), chapter, drawCategoryCards(rawNewsMap.get(chapter), chapter, choice), choice);
            await loadPicturesToNews(rawNewsMap.get(chapter), category)
        } else {
            tempChoice.get(chapter).set(choice, false);
            console.log("Loading news from notify server")
            loadCategoryNews(category, choice)
                .then((rawNews) => {
                    rawNewsMap.set(chapter, rawNews);
                    newsCards = drawCategoryCards(rawNews, chapter, choice);
                    return rawNews
                })
                .then(() => {
                    if (rawNewsMap.get(chapter).length !== 0) {
                        drawPlaceOfNews(chapter, category, true);
                        drawNewsGlobal(rawNewsMap.get(chapter), chapter, newsCards, choice)
                    } else drawPlaceOfNews(chapter, category, false)
                }).then(() => {
                if (rawNewsMap.get(chapter).length !== 0) loadPicturesToNews(rawNewsMap.get(chapter), category);
                tempChoice.get(chapter).set(choice, true);
            })
        }
    }
}

function drawNewsGlobal(rawNews, chapter, cards, choice) {
    let temp = "";
    let tempNav = "";
    for (let i = 0; i < cards.length; i++) {
        if (cards[i] !== undefined) {
            temp += cards[i];
            tempNav += `<div><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 12px;text-align: center" href="#card${i}">${rawNews[i].subject}</a></div>`
        }
    }
    document.getElementById("message-list").innerHTML = temp;
    document.getElementById("dropdown-message-list").innerHTML = tempNav;
    for (let i = 0; i < cards.length; i++) {
        if (cards[i] !== undefined) {
            drawNewsButtons(rawNews[i], i, chapter, choice)
        }
    }
}

function drawCategoryCards(rawNews, chapter, choice) {
    let cards;
    switch (chapter) {
        case 1:
        case 2:
            cards = drawSystemCards(rawNews, chapter, choice);
            break;
        default:
            cards = drawPrivateCards(rawNews, chapter, choice)
    }
    return cards;
}

async function loadCategoryNews(category, choice) {
    let url = endPoints.get('newsPortal') + "/get/" + category + "/" + choice + "/" + currentLang;
    let promise = await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
        }
    }).then(response => response.json()).then(letters => {
        let newsForm = [];
        if (letters !== null && !letters.isEmpty) {
            letters.forEach(aloneNew => {
                    let creationDate = new Date(aloneNew.creationDate);
                    let fetchData = {
                        externId: aloneNew.externId,
                        timeMessage: getTime(creationDate),
                        dateMessage: getDate(creationDate),
                        sendingFrom: aloneNew.sendingFrom,
                        sendingFromAlt: aloneNew.sendingFromAlt,
                        sendingTo: aloneNew.sendingTo,
                        subject: aloneNew.subject,
                        textInfo: aloneNew.textInfo,
                        category: aloneNew.category,
                        attention: aloneNew.attention,
                        alreadyRead: aloneNew.alreadyRead,
                        localisation: aloneNew.localisation,
                        imageUrl: aloneNew.imageUrl,
                        existPrimePhoto: aloneNew.existPrimePhoto,
                        hidden: aloneNew.hidden,
                        primePhoto: ``,
                        primePhotoText: ``,
                        img: ``,
                        imgText: ``,
                        imgLoading: false,
                        primeLoading: false
                    }
                    newsForm.push(fetchData);
                }
            )
        }
        return newsForm;
    })
    console.log("news begin loading");
    return promise;
}

async function loadPicturesToNews(rawNews, category) {
    console.log(rawNews);
    let sysPhoto = URL.createObjectURL(defaultPhotos.systemPhoto);
    let defPhoto = URL.createObjectURL(defaultPhotos.person);
    let noPhoto = URL.createObjectURL(defaultPhotos.borderImage);
    for (let j = 0; j < rawNews.length; j++) {
        let place = `prime_img_container${j}`;
        let textPlace = `prime_over_text${j}`;
        if (document.getElementById(place) !== undefined && document.getElementById(place) !== null)
            if (category !== "system" && category !== "common") {
                if (rawNews[j].primePhoto !== '' && !rawNews[j].primeLoading) {
                    document.getElementById(place).src = URL.createObjectURL(rawNews[j].primePhoto);
                    URL.revokeObjectURL(rawNews[j].primePhoto);
                    document.getElementById(`prime_over_text${j}`).innerHTML = rawNews[j].primePhotoText;
                } else if (rawNews[j].sendingFrom === systemRecipientID || rawNews[j].sendingFrom === informerRecipientID) {
                    directNewImageToPlace(place, textPlace, sysPhoto, ``)
                } else if (contacts.get(rawNews[j].sendingFrom) !== undefined && contacts.get(rawNews[j].sendingFrom) !== null && contacts.get(rawNews[j].sendingFrom).img !== "") {
                    directNewImageToPlace(place, textPlace, URL.createObjectURL(contacts.get(rawNews[j].sendingFrom).img), contacts.get(rawNews[j].sendingFrom).primePhotoText);
                    URL.revokeObjectURL(contacts.get(rawNews[j].sendingFrom).img);
                } else if (rawNews[j].existPrimePhoto) {
                    if (rawNews[j].hidden === null || rawNews[j].hidden) {
                        directNewImageToPlace(place, textPlace, defPhoto, globalTexts.get(8));
                    } else if (!rawNews[j].primeLoading) {
                        rawNews[j].primeLoading = true;
                        loadPicture(endPoints.get("filePrivate") + "/get/little/" + rawNews[j].externId)
                            .then(r => {
                                if (r != null && r.length !== 0) {
                                    rawNews[j].primePhoto = r;
                                    rawNews[j].primePhotoText = ``;
                                    directNewImageToPlace(place, textPlace, URL.createObjectURL(rawNews[j].primePhoto), ``);
                                    URL.revokeObjectURL(rawNews[j].primePhoto);
                                }
                            })
                            .catch((reason) => {
                                if (reason.message === "empty") {
                                    directNewImageToPlace(place, textPlace, defPhoto, globalTexts.get(9));
                                    console.log("catch error");
                                } else
                                    console.log(reason)
                            })
                    } else directNewImageToPlace(place, textPlace, defPhoto, globalTexts.get(9));
                } else {
                    directNewImageToPlace(place, textPlace, defPhoto, globalTexts.get(3));
                }
            }
        let placeMain = `img-container${j}`;
        let textPlaceMain = `overText${j}`;
        if (document.getElementById(placeMain) !== undefined && document.getElementById(placeMain) !== null)
            if (rawNews[j].imageUrl !== null && rawNews[j].imageUrl !== '' && rawNews[j].img === `` && !rawNews[j].imgLoading) {
                rawNews[j].imgLoading = true;
                let path;
                if (category === "system" || category === "common") path = endPoints.get("fileSystem") + "/get/" + category + "/" + rawNews[j].imageUrl;
                else path = endPoints.get("filePrivate") + "/getNews/" + rawNews[j].imageUrl + "/" + rawNews[j].sendingFrom;
                await loadPicture(path)
                    .then((pic) => {
                        rawNews[j].img = pic;
                        rawNews[j].imgText = ``;
                        directNewImageToPlace(placeMain, textPlaceMain, URL.createObjectURL(rawNews[j].img), '');
                        return pic;
                    }).then(pic => URL.revokeObjectURL(pic))
                    .catch((reason) => {
                        if (reason.message === "empty") {
                            rawNews[j].img = "";
                            directNewImageToPlace(placeMain, textPlaceMain, noPhoto, globalTexts.get(9));
                            console.log("catch error");
                        } else
                            console.log(reason)
                    })
            } else if (rawNews[j].imageUrl !== null && rawNews[j].imageUrl !== '' && rawNews[j].img !== "") {
                directNewImageToPlace(placeMain, textPlaceMain, URL.createObjectURL(rawNews[j].img), rawNews[j].imgText);
                URL.revokeObjectURL(rawNews[j].img);
            } else if (!rawNews[j].imgLoading) {
                rawNews[j].img = ``;
                rawNews[j].imgText = globalTexts.get(3);
                directNewImageToPlace(placeMain, textPlaceMain, noPhoto, globalTexts.get(3));
            } else directNewImageToPlace(placeMain, textPlaceMain, noPhoto, globalTexts.get(9));
    }
    URL.revokeObjectURL(defaultPhotos.systemPhoto);
    URL.revokeObjectURL(defaultPhotos.person);
    URL.revokeObjectURL(defaultPhotos.borderImage);
    console.log("pictures to raw news begin loading");
}

function directNewImageToPlace(place, placeText, photoSrc, text) {
    document.getElementById(place).src = photoSrc;
    document.getElementById(placeText).innerHTML = text;
}

function drawSystemCards(rawNews, chapter, choice) {
    let category = getCategory(chapter);
    let cards = [];
    let tempTextPhoto = `<div class="spinner-border" role="status"> 
        <span class="visually-hidden">Loading...</span></div>`;
    let pictureUrl = URL.createObjectURL(defaultPhotos.borderImage);
    let pictureUrl5 = URL.createObjectURL(defaultPhotos.borderImage5);
    for (let i = 0; i < rawNews.length; i++) {
        if (choice || !rawNews[i].alreadyRead) {
            cards[i] = `
                <div class="card" id="card${i}" style="max-height:730px; width: 290px; min-width: 290px;  max-width: 350px; margin: 0 5px 15px 5px">
                <div style="font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: chocolate; font-style: italic">${langMatrix.get("date")}:&nbsp;${rawNews[i].dateMessage}</div>
                    <img style="padding: 5px; max-height:50vh; object-fit: contain" src=${pictureUrl} id="img-container${i}" class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="height: 50%">
                            <div class="card-text" id="overText${i}" style="padding-top: 40%">${tempTextPhoto}</div>
                        </div>                   
                        
                    <div class="card-body" style="background: url(${pictureUrl5}) right top 0/ contain no-repeat; text-align: center; padding: 0 5px 0 0">
                        <h5 class="card-title" style="text-align: left; margin: 10px 50px 8px 10px">${rawNews[i].subject}</h5>
                        <hr style="color:#a12121; width: 40%; margin:0 0 0 10px" >
                        <p class="card-text" style="font-size: 14px; width: 80%; min-height:120px; margin: 10px 5px 5px 5px">${rawNews[i].textInfo}</p>
                    </div> 
                    <div class="card-footer">
                        <div class="container-fluid row mh-100 no-gutters" id="cardButtons${i}">                           
                        </div>
                    </div>                    
                </div> `
        }
    }
    URL.revokeObjectURL(defaultPhotos.borderImage);
    URL.revokeObjectURL(defaultPhotos.borderImage5)
    return cards;
}

function drawNewsButtons(rawNew, i, chapter, choice) {
    let temp = ``
    if (!rawNew.alreadyRead) {
        temp += `<span class="col-5" style="color: darkred; text-shadow: none"><a style="padding: 3px 6px 3px 6px;" type="button" class="btn btn-outline-warning" onclick="readOrRemoveCategoryMessage('${rawNew.sendingFrom}',${i},${chapter},true,${choice})">${langMatrix.get("read")}</a></span>`
        temp += `<span class="col-1"></span>`
    }
    temp += `<span class="col"  style="color: darkred; text-shadow: none"><a style="padding: 3px 6px 3px 6px;" type="button" class="btn btn-outline-danger" href="#" onclick="readOrRemoveCategoryMessage('${rawNew.sendingFrom}',${i},${chapter},false,${choice})">${langMatrix.get("remove")}</a></span>`
    document.getElementById(`cardButtons${i}`).innerHTML = temp;
}

async function readOrRemoveCategoryMessage(id, i, chapter, operation, choice) {
    let stringOperation = operation ? 'read' : 'remove'
    let url = endPoints.get('newsPortal') + "/" + stringOperation + "/" + getCategory(chapter).toUpperCase() + "/" + id
    fetch(url, {
        method: 'GET',
        headers: {},
    }).then(r => r).then(() => {
            if (!rawNewsMap.get(chapter)[i].alreadyRead) {
                counts[0] = counts[0] - 1;
                counts[chapter] = counts[chapter] - 1;
                tempCount = counts;
                drawNewsCounts();
            }
            if (operation) {
                rawNewsMap.get(chapter)[i].alreadyRead = true;
                console.log("reading")
            } else {
                rawNewsMap.get(chapter).splice(i, 1);
                console.log("removing")
            }
            refreshGlobalNews(chapter, getCategory(chapter), choice);

        }
    )
    console.log("do: {}", stringOperation)
}

function drawPrivateCards(rawNews, chapter, choice) {
    let unknown=false;
    let cards = [];
    let tempTextPhoto = `<div class="spinner-border" role="status"> 
        <span class="visually-hidden">Loading...</span></div>`;
    let pictureUrl = URL.createObjectURL(defaultPhotos.borderImage);
    let pictureUrl5 = URL.createObjectURL(defaultPhotos.borderImage5);
    let personPhoto = URL.createObjectURL(defaultPhotos.person);
    let relation;
    let nick;
    let lastTimeOnline = null;
    for (let i = 0; i < rawNews.length; i++) {
        if (choice || !rawNews[i].alreadyRead) {
            let con = contacts.get(rawNews[i].sendingFrom);
            if (con !== undefined && con !== null) {
                relation = con.specify;
                nick = con.name;
                if (con.online) lastTimeOnline = langMatrix.get("online"); else lastTimeOnline = '' + con.lastOnline;
            } else if (rawNews[i].sendingFrom === systemRecipientID) {
                nick = globalMenu.get(1100);
                relation = globalMenu.get(1100);
            } else if (rawNews[i].sendingFrom === informerRecipientID) {
                nick = globalMenu.get(1100);
                relation = langMatrix.get("informer");
            } else {
                nick = rawNews[i].sendingFromAlt;
                relation = langMatrix.get("unknown");
                unknown=true;
            }
            cards[i] = `
                <div class="card" id="card${i}" style="max-height:730px; width: 290px; min-width: 290px;  max-width: 350px; margin: 0 5px 15px 5px">
                <div class="container-fluid row" style=" margin-right: 0; margin-bottom: 10px; font-size:16px; padding:0">
                <div class="col-5" style="padding-right: 0">
                <div>${nick}</div>
                <div id="load_picture${i}">
                <img style="padding: 5px; max-height:50vh; object-fit: contain" src=${personPhoto} id="prime_img_container${i}" class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="z-index:100; padding:0">
                            <div class="card-text" id="prime_over_text${i}" style="margin-top:10%;margin-right:64%;padding-top: 40px; font-size:10px">${tempTextPhoto}</div>
                    <div class="card-text" id="overText${i}" style="margin-top: 60%">${tempTextPhoto}</div>    
                        </div>
                </div>          
                <div>${relation}</div>        
                        </div>
                <div class="col" style="z-index: 200; padding:0; font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: chocolate; font-style: italic">
                <div>${langMatrix.get("date")}:&nbsp;${rawNews[i].dateMessage}</div>
                <div>${langMatrix.get("time")}:&nbsp;${rawNews[i].timeMessage}</div>`
            if (unknown) cards[i] += `<div style="margin-top: 20px"><button class="btn btn-outline-warning" style="color: darkred;padding:6px; width:90px; font-size:14px" type="button" onclick="addUserToBan('${rawNews[i].sendingFrom}')">${langMatrix.get("ban")}</button>
                <br><button class="btn btn-outline-warning" style="color: darkred;padding:6px; margin-top:6px;width:90px; font-size:14px" type="button" onclick="menuFunc3100('${rawNews[i].sendingFrom}', '${rawNews[i].sendingFromAlt}')">${langMatrix.get("answer")}</button></div>`;
            cards[i] += `</div>
                    </div>
                    
                    <img style="padding: 10px; height:30vh;max-height:35vh; object-fit: contain" src=${pictureUrl} id="img-container${i}" class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-body" style="background: url(${pictureUrl5}) right top 0/ contain no-repeat; text-align: center; padding: 0 5px 0 0">
                        <h5 class="card-title" style="text-align: left; margin: 10px 50px 8px 10px">${rawNews[i].subject}</h5>
                        <hr style="color:#a12121; width: 40%; margin:0 0 0 10px" >
                        <p class="card-text" style="font-size: 14px; width: 80%; min-height:120px; margin: 10px 5px 5px 5px">${rawNews[i].textInfo}</p>
                    </div> 
                    <div class="card-footer" style="z-index: 300;">
                        <div class="container-fluid row mh-100 no-gutters" id="cardButtons${i}">                           
                        </div>
                    </div>                    
                </div> `
        }
    }
    URL.revokeObjectURL(defaultPhotos.borderImage);
    URL.revokeObjectURL(defaultPhotos.borderImage5)
    URL.revokeObjectURL(defaultPhotos.person)
    return cards;
}

// Вниз это исправлять ------------------------------------------------------------------------------------
function addUserToBan(userUuid) {
}


function ifLoadPictureError(i, defPhoto) {
    document.getElementById(`load_picture${i}`).innerHTML = `
    <img style="padding: 5px; max-height:50vh; object-fit: contain" src=${defPhoto} id="prime_img_container${i}" class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="padding:0">
                            <div class="card-text" id="prime_over_text${i}" style="margin-top:10%;margin-right:68%;padding-top: 40px; font-size:10px">${globalTexts.get(9)}</div>
                        <div class="card-text" id="overText${i}" style="padding-top: 40%"></div>
                        </div>`
}

function drawPrivateCard2(rawNews, chapter, choice) {

    let pictureUrl = URL.createObjectURL(defaultPhotos.photono);
    let cards = [];
    let operation = choice ? 'Remove' : 'Read'
    for (let i = 0; i < newsForm.length; i++) {
        cards[i] = `
                <div class="card" style="max-height:700px; min-width:290px; max-width: 290px; margin-bottom: 10px">
                    <img src=${pictureUrl} id="img-conteiner${i}" class="card-img-top" width="290px" alt="tytPhoto">                    
                    <div class="card-body" style="text-align: center">
                        <h5 class="card-title">${newsForm[i].subjectJS}</h5>
                        <p class="card-text">${newsForm[i].textInfoJS}</p>
                        <br>                        
                        <a class="btn btn-outline-warning" onclick="readOrRemoveCategoryMessage('${newsForm[i].sendingFromJS}','${i}','${chapter}','${operation}')" href="#">${operation}</a>   
                        
                    </div> 
                    <div class="card-footer">
                        <small class="text-body-secondary">
                           ${newsForm[i].dateMessage}
                        </small>
                    </div>                    
                </div>
                <div id="mar${newsForm[i].sendingFromJS}" class="card" style="border:0;max-height:0; min-width:10px; max-width: 10px">
                </div>                
               `
        URL.revokeObjectURL(defaultPhotos.photono)
    }
    return cards;
}


async function loadingIndividualNews(category, choice) {
    let num = 0;
    let lets = [];
    let url = endPoints.get('newsPortal') + "/" + category;
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

                if ((fetchData.attention === "VOTING_POSITIVE" || fetchData.attention === "POSITIVE" || fetchData.subjectJS === "Contact added" || fetchData.subjectJS === "Accept change") && defaultPhotos.approved !== null) fetchData.imj = defaultPhotos.approved;
                if ((fetchData.attention === "VOTING_NEGATIVE" || fetchData.attention === "NEGATIVE" || fetchData.subjectJS === "Reject change" || fetchData.subjectJS === "Contact reject") && defaultPhotos.rejected !== null) fetchData.imj = defaultPhotos.rejected;
                if ((fetchData.attention === "VOTING" || fetchData.attention === "VOTING_REQUESTER") && defaultPhotos.election !== null) fetchData.imj = defaultPhotos.election;
                if ((fetchData.subjectJS === "Request for contact" || fetchData.subjectJS === "Contact request") && defaultPhotos.contact !== null) fetchData.imj = defaultPhotos.contact;
                if ((fetchData.attention === "LINK") && defaultPhotos.linking !== null) fetchData.imj = defaultPhotos.linking;
                if (fetchData.imj === ``) fetchData.imj = contactImage;
                if (fetchData.imj === `` && defaultPhotos.person !== null) fetchData.imj = defaultPhotos.person;

                if (fetchData.externIdJS === null) fetchData.externIdJS = fetchData.idJS;

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
                case "VOTING_REQUESTER":
                    temp = `<span class="col-4"></span>
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
            <div data-bs-spy="scroll" style="height: 72vh; overflow: auto" data-bs-target="#navbar-${category}" data-bs-smooth-scroll="true" class="scrollspy-example bg-body-tertiary" tabindex="0">
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
    fetch(endPoints.get('newsPrivate') + "/accept/" + id, {method: "GET"}).then(r => r.text())
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
    fetch(endPoints.get('newsPrivate') + "/reject/" + id, {method: "GET"}).then(r => r.text())
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
    let url = endPoints.get('newsPrivate') + "/readResult/" + id;
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
    if (choice === 0) url = endPoints.get('newsPrivate') + "/individual"; else url = endPoints.get("newsPrivate") + "/individualAll";
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
    let url = endPoints.get('newsPrivate') + "/readMessage/" + id;
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
    let url = endPoints.get('newsPrivate') + "/deleteMessage/" + id;
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