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
    getSystemNews();
}

async function getSystemNews() {
    document.getElementById("resultPart").innerHTML = `
<div style="margin:10px; text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">*** System News ***</div>

`
}

function loadSystemNews() {
    let sysNews;
    fetch("/news/system", {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
            // "Access-Control-Allow-Origin": "*"
        }
    }).then(news => news.json()).then(news => {
        sysNews = news;
    });
}

function getCommonNews() {
    document.getElementById("resultPart").innerHTML = `
<div style="margin:10px; text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">*** Common News ***</div>
`
}

async function getFamilyNews() {
    document.getElementById("resultPart").innerHTML = `
<div style="margin:10px; text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">*** Family News ***</div>
`
}

async function getPrivateNews() {
    document.getElementById("resultPart").innerHTML = `
<div style="margin:10px; text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 20px">*** Private News ***</div>
                <div class="btn-group-horizontal" role="group" aria-label="Vertical button group" style="margin-left: 5px; text-align: right">
        <input type="radio" class="btn-check" name="news-choice" onclick="" id="news-choice1" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice1">Все 
        </label>
        <input type="radio" class="btn-check" name="news-choice" onclick="" id="news-choice2" autoComplete="off" checked>
        <label class="btn btn-outline-warning" style="padding: 5px; text-align:center; font-size: 14px; color: darkred" for="news-choice2">Новые 
        
        </label>
</div>
<nav id="navbar-private" class="navbar bg-body-tertiary no-gutters" >
  <ul class="nav nav-pills">
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
<div data-bs-spy="scroll" style="height: 800px; overflow: auto" data-bs-target="#navbar-private" data-bs-smooth-scroll="true" class="scrollspy-example bg-body-tertiary" tabindex="0">
  <div id="first-message"></div>
  <div id="message-list">  </div>
  <div id="last-message"></div>
</div>
`
    loadPrivateNews()
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
    let temp0=``;
    let temp1 = ``;
    let timeMessage=``;
    let dateMessage=``;
    let sendingFrom;
let sendingTo;
    let creationDate;
    let  subject;
    let  image;
    let textInfo;
    let category;
    let alreadyRead;

    fetch("/news/individual", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        },
    }).then(response => response.json()).then(letters => {if (letters!==null &&!letters.isEmpty) {letters.forEach(aloneNew => {
        temp0 +=`
        <li><a class="dropdown-item" style="color: goldenrod;font-family: 'Times New Roman',serif; font-size: 12px" href="#message${aloneNew.id}">${aloneNew.id}</a></li>
         `
        creationDate=new Date(aloneNew.creationDate);
        timeMessage=getTime(creationDate);
        dateMessage=getDate(creationDate);
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
                <div style="text-align: center; color: darkgreen"> ${aloneNew.sendingFrom}</div>
                <br>
                <div style="text-align: center; color: darkred">Сообщение:</div> 
                <div  style=" text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 14px">${aloneNew.textInfo} </div>
                <br>
                <div class="container-fluid row" style="font-size: 16px">
                    <span class="col-2"><button type="button" class="btn btn-outline-success" data-toggle="modal" data-target="#see${aloneNew.id}">See</button></span>
                    <span class="col"></span>
                    <span class="col-2" ><button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#delete${aloneNew.id}">Delete</button></span>
                <span class="col-1" style="width: 40px"></span>
                </div>
                <br>
          </div>    
`;

        document.getElementById("dropdown-message-list").innerHTML=temp0;
        document.getElementById("message-list").innerHTML = temp1;
    })} else {document.getElementById("mess-list").innerHTML = `<div style="font-size: 24px">Message is absent</div>>`;}});
}

