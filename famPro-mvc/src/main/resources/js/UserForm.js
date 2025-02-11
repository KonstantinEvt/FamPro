let counts = [0, 0, 0, 0, 0];
loadOnlineUser();
loadStandardMainPanel();
loadNewsCounts()
let delay = 5000;
setInterval(loadNewsCounts, delay);

// let countPhone = 0;
// let countEmail = 0;
// let countOtherNames = 0;

function loadOnlineUser() {
    let username;
    let nickName;
    let firstName;
    let middleName;
    let lastName;
    let birthday;
    let email;
    let role;
    let fullName;


    fetch("/onlineUserAPI/info", {
        method: "GET",
        headers: {
            "Content-Type": "application/json; charset=UTF-8"
            // "Access-Control-Allow-Origin": "*"
        }
    }).then(user => user.json()).then(user => {
        username = user.username;
        nickName = user.nickName;
        firstName = user.firstName;
        middleName = user.middleName;
        lastName = user.lastName;
        birthday = user.birthday;
        email = user.email;
        fullName = user.fullName;
        role = user.role;
        document.getElementById("nav0").innerHTML = fullName
        document.getElementById("nav1").innerHTML = nickName;
        document.getElementById("nav2").innerHTML = role;
    });

// document.getElementById("locForm1").addEventListener("Language", loadOnlineUser)
}

function loadStandardMainPanel() {
    document.getElementById("mainPanel").innerHTML = `
    <div class="list-group list-group-flush col-2" id="taskPart" style="height: 300px; min-width: 100px; margin-left: 10px" role="tablist" >
    </div>
    <div style="height: 1200px" class="tab-content col bg-light" id="resultPart"></div>
    `
}
function getTime(datetime){
    let hour = new Intl.NumberFormat("en",{minimumIntegerDigits:2}).format(datetime.getUTCHours());
    let minutes = new Intl.NumberFormat("en",{minimumIntegerDigits:2}).format(datetime.getUTCMinutes());
    return hour + ':' + minutes;
}
function getDate(datetime){
    let year = datetime.getUTCFullYear();
    let month = new Intl.NumberFormat("en",{minimumIntegerDigits:2}).format(datetime.getUTCMonth()+1);
    let day = new Intl.NumberFormat("en",{minimumIntegerDigits:2}).format(datetime.getUTCDate());
    return (day + '.' + month + '.' + year);
}
function loadNewsCounts() {
    fetch("/news/counts", {
        method: "GET",
        headers: {
            "Content-Type": "text/javascript; charset=UTF-8"
            // "Access-Control-Allow-Origin": "*"
        }
    }).then(r => r.json()).then(cou => {
        counts = cou;
    })
    if (document.getElementById("badge0")!==null&&counts[0] !== 0) {
        document.getElementById("badge0").innerHTML =
            `<span id="newsCount" class="position-absolute top-1 start-1 translate-middle badge rounded-pill bg-danger" style="font-size: 12px" >
                
                <span class="visually-hidden">unread messages</span>
            </span>`
        document.getElementById("newsCount").innerHTML = counts[0];


        if (document.getElementById("badge1")!==null&&counts[1] !== 0) {
            document.getElementById("badge1").innerHTML = `
         <span class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[1] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
            // document.getElementById("newsCount1").innerHTML = counts[1];
        }
        if (document.getElementById("badge2")!==null&&counts[2] !== 0) {
            document.getElementById("badge2").innerHTML = `
          <span class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[2] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        }
        if (document.getElementById("badge3")!==null&&counts[3] !== 0) {
            document.getElementById("badge3").innerHTML = `
          <span class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[3] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        }
        if (document.getElementById("badge4")!==null&&counts[4] !== 0) {
            document.getElementById("badge4").innerHTML = `
          <span class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[4] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        }
    }
}


