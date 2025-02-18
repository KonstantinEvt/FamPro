function getNews() {

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
        <input type="radio" class="btn-check" name="news-radio" onclick="getFamilyNews()" id="news-radio3" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center;font-size: 14px; color: darkred" for="news-radio3">Family
            <span id="badge3"> </span> 
                </label>
        <input type="radio" class="btn-check" name="news-radio" onclick="getPrivateNews()" id="news-radio4" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio4">Private
            <span id="badge4"> </span> 
        </label>
    </div>`
    loadNewsCounts();
    getSystemNews()
}

async function getSystemNews() {
    cards = [];
    numCards = [];
    document.getElementById("resultPart").innerHTML = `
        <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">
            *** System News ***
        </div>
        <div id="system-list" class="card-group" style="height: 750px; max-width:100%; overflow-x: auto"></div>`
    if (counts[1] !== null && counts[1] !== 0) {
        await loadCategoryNews("system")
    } else {
        document.getElementById("system-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">We are glad to see you</div>`;
    }
}

// private String sendingFrom;
// private String sendingTo;
// private Date creationDate;
// private String subject;
// private byte[] image;
// private String textInfo;
// private NewsCategory category;
// private boolean alreadyRead;
async function loadCategoryNews(category) {

    let temp1=``;
    let i=0;
    let linkOn=category+"-list";
    console.log(document.getElementById(linkOn).innerHTML);
    if (!document.getElementById(linkOn).innerHTML || /^\s*$/.test(document.getElementById(linkOn).innerHTML)){
        let url = "/news/" + category;
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
                    let fetchData= {
                        timeMessage: getTime(creationDateJS),
                        dateMessage: getDate(creationDateJS),
                        sendingFromJS: aloneNew.sendingFrom,
                        subjectJS: aloneNew.subject,
                        textInfoJS: aloneNew.textInfo,
                        categoryJS: aloneNew.category,
                        imj:``
                    }
                    i+=1;
                    newsForm.push(fetchData);
                }
                )
            }
        });
        console.log("new is received", promise);

        for (let j = 0; j < i; j++) {
            let imjUrl = "/file/" + category + "/" + newsForm[j].subjectJS;
            newsForm[j].imj = await loadNewsPicture(imjUrl);
            console.log("picture is loading",newsForm);
            numCards[j] = true;
            let pictureUrl=URL.createObjectURL(newsForm[j].imj)
            cards[j] = `
                <div class="card" style="max-height:700px; min-width:290px; max-width: 290px; margin-bottom: 10px">
                    <img src=${pictureUrl} id="img-conteiner${i}" class="card-img-top" width="290px" alt="tytPhoto">                    
                    <div class="card-body" style="text-align: center">
                        <h5 class="card-title">${newsForm[j].subjectJS}</h5>
                        <p class="card-text">$${newsForm[j].textInfoJS}</p>
                        <a class="btn btn-outline-success" onclick="readCategoryMessage('${newsForm[j].sendingFromJS}','${j}','${category}')" href="#">See</a>   
                        <br>                   
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

async function readCategoryMessage(id, i, category) {
    numCards[i] = false;
    let url = "/news/globalNewsRead/" + id
    fetch(url, {
        method: 'GET',
        headers: {},
    }).then(r => r).then(r => {
            counts[0] = counts[0] - 1;
            if (counts[0] !== 0) document.getElementById("newsCount").innerHTML = counts[0] + "";
            else document.getElementById("badge0").innerHTML = "";
            if (category==="system"){
            counts[1] = counts[1] - 1;
            if (counts[1] !== 0) document.getElementById("countNew1").innerHTML = counts[1] + "";
            else {
                document.getElementById("badge1").innerHTML = "";
                document.getElementById("system-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">Больше системных сообщений нет</div>`;
            }}else if (category==="common") {
                counts[2] = counts[2] - 1;
                if (counts[2] !== 0) document.getElementById("countNew2").innerHTML = counts[2] + "";
                else {
                    document.getElementById("badge2").innerHTML = "";
                    document.getElementById("common-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">Больше общих сообщений нет</div>`;
                }
            }
        }
    )
    loadNewsCounts();
    loadCategoryNews(category)
}

function getCommonNews() {
    cards = [];
    numCards = [];
    document.getElementById("resultPart").innerHTML = `
            <div style=" text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Common News ***</div>
            <div class="card-group" id="common-list" style="height: 750px; max-width: 100%; overflow-x: auto" >           
            </div> `;
    if (counts[2] !== null && counts[2] !== 0) {
        loadCategoryNews("common");
    } else {
        document.getElementById("common-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">Блок общих сообщений пуст</div>`;
    }
}

// function loadCommonNews() {
//     let temp1 = ``;
//     let creationDate;
//     let timeMessage;
//     let dateMessage;
//     fetch("/news/common", {
//         method: "GET",
//         headers: {
//             "Content-Type": "application/json; charset=UTF-8"
//             // "Access-Control-Allow-Origin": "*"
//         }
//     }).then(response => response.json()).then(letters => {
//         if (letters !== null && !letters.isEmpty) {
//             letters.forEach(aloneNew => {
//                 creationDate = new Date(aloneNew.creationDate);
//                 timeMessage = getTime(creationDate);
//                 dateMessage = getDate(creationDate);
//                 temp1 += `
//                 <div class="card" style="max-height:700px; min-width:290px; max-width: 290px; margin-bottom: 10px">
//                     <img src="/file/common/${aloneNew.subject}" class="card-img-top" width="290px" alt="tytPhoto">
//                     <div class="card-body" style="text-align: center">
//                         <h5 class="card-title">${aloneNew.subject}</h5>
//                         <p class="card-text">${aloneNew.textInfo}</p>
//                         <a class="btn btn-outline-success" onclick="readCommonMessage('${aloneNew.sendingFrom}')" href="#">See</a>
//                         <br>
//                     </div>
//                     <div class="card-footer">
//                         <small class="text-body-secondary">
//                            ${dateMessage}
//                         </small>
//                     </div>
//                 </div>
//                 <div class="card" style="border:0;max-height:0; min-width:10px;max-width: 10px">
//                 </div>`
//                 document.getElementById("common-list").innerHTML = temp1;
//             });
//         }
//     });
// }

// function readcommonMessage(id) {
//     let url = `/news/globalNewsRead/` + id
//     fetch(url, {
//         method: 'GET',
//         headers: {},
//     }).then(r => {
//         counts[0] = counts[0] - 1;
//         if (counts[0] !== 0) document.getElementById("newsCount").innerHTML = counts[0] + "";
//         else document.getElementById("badge0").innerHTML = "";
//         counts[2] = counts[2] - 1;
//         if (counts[2] !== 0) document.getElementById("countNew2").innerHTML = counts[2] + "";
//         else {
//             document.getElementById("badge2").innerHTML = "";
//             document.getElementById("common-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 18px;text-align: center">Больше общих сообщений нет</div>`;
//         }
//     });
//     loadNewsCounts()
//     loadCategoryNews("common");
// }

async function getFamilyNews() {
    document.getElementById("resultPart").innerHTML = `
            <div style=" text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Family News ***</div>`
}

async function getPrivateNews() {
    document.getElementById("resultPart").innerHTML = `
            <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Private News ***</div>
            <div class="btn-group-horizontal" role="group" aria-label="Vertical button group" style="margin-left: 5px; text-align: right">
                <input type="radio" class="btn-check" name="news-choice" onclick="" id="news-choice1" autoComplete="off">
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice1">Все</label>
                <input type="radio" class="btn-check" name="news-choice" onclick="" id="news-choice2" autoComplete="off" checked>
                <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice2">Новые </label>
            </div>
            <nav id="navbar-private" class="navbar bg-body-tertiary no-gutters">
                <ul class="nav nav-pills" >
                    <li class="nav-item">
                        <a class="nav-link bg-dark-red" role="button" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" href="#first-message">Начало</a>
                    </li>  
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" style="color: darkred;font-family: 'Times New Roman',serif; font-size: 12px" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false">Список</a>
                        <ul class="dropdown-menu">
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
            <span id="mess-list">  </span>
            <div data-bs-spy="scroll" style="height:750px; overflow: auto" data-bs-target="#navbar-private" data-bs-smooth-scroll="true" class="scrollspy-example bg-body-tertiary" tabindex="0">
                <div id="first-message"></div>
                <div id="message-list">  </div>
                <div id="last-message"></div>
            </div>`
    if (counts[4] !== null && counts[4] !== 0) {
        loadPrivateNews()
    } else {
        document.getElementById("mess-list").innerHTML = `<div style="color: darkred;font-family: 'Times New Roman',serif; font-size: 24px;text-align: center">New messages are absent</div>`;
    }
}

// private String sendingFrom;
// private String sendingTo;
// private Date creationDate;
// private String subject;
// private byte[] image;
// private String textInfo;
// private NewsCategory category;
// private boolean alreadyRead;
function loadPrivateNews() {
    let temp0 = ``;
    let temp1 = ``;
    let timeMessage = ``;
    let dateMessage = ``;
    let creationDate;

    fetch("/news/individual", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(response => response.json()).then(letters => {
        if (letters !== null && !letters.isEmpty) {
            letters.forEach(aloneNew => {
                creationDate = new Date(aloneNew.creationDate);
                timeMessage = getTime(creationDate);
                dateMessage = getDate(creationDate);
                temp0 += `
                         <li><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 8px" href="#message${aloneNew.id}">${dateMessage}---${aloneNew.sendingFrom}</a></li>`

                temp1 += `
                        <div style="text-align: center;font-size: 20px">------ ***** ------</div>
                        <div id="${aloneNew.id}">           
                            <div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">${aloneNew.subject}</div>
                            <div class="container-fluid row" style="text-align: center; color: darkred;font-family: 'Times New Roman',serif; font-size: 16px">
                                <span class="col-2">${dateMessage}  </span>                         
                                <span class="col"></span>
                                <span class="col-2">${timeMessage}</span>
                            </div>
                            <div style="text-align: center; color: darkred;">Отправитель: </div>
                            <div style="text-align: center; color: darkgreen"> ${aloneNew.sendingFrom}</div>
                            <br>
                            <div style="text-align: center; color: darkred">Сообщение:</div> 
                            <div style=" text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 14px">${aloneNew.textInfo} </div>
                            <br>
                            <div class="container-fluid row" style="font-size: 16px">
                            <span class="col-2">
                                <button type="button" class="btn btn-outline-success" data-toggle="modal" onclick="seeMessage('${aloneNew.id}')" data-target="#see${aloneNew.id}">See</button></span>
                            <span class="col"></span>
                            <span class="col-2" ><button type="button" class="btn btn-outline-danger" data-toggle="modal" onclick="deleteMessage('${aloneNew.id}')" data-target="#delete${aloneNew.id}">Delete</button></span>
                            <span class="col-1" style="width: 40px"></span>
                            </div>
                            <br>
                        </div>`;
                document.getElementById("dropdown-message-list").innerHTML = temp0;
                document.getElementById("message-list").innerHTML = temp1;
            })
        }
    });
}

function seeMessage(id) {

}

function deleteMessage(id) {

}