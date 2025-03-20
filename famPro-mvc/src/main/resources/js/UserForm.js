let counts = [0, 0, 0, 0, 0];
let cards;
let numCards;
let rawNews;

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
    <table style="padding: 0; margin-left: -2px; height: 82vh" >
    <tbody>
    <tr>
    <td class="col-1" style="vertical-align: top; padding-right:7px; margin-right: 10px; margin-left: 5px;text-align: center ">
    <div class="list-group list-group-flush" id="taskPart" style="height: 82vh; min-width: 80px; text-align: center" role="tablist" ><span style="color:white">Hello</span>
    </div>
    </td>
    <td class="col" style="horiz-align: center; margin-right: 5px; margin-left: 5px ; text-align: center">
    <div  class="tab-content col bg-light" id="resultPart" style=" height: 82vh; padding-left: 5px;text-align: center">
    <span style="color:lightgray">.</span>
</div>
    </td>
    </tr>
    </tbody>
    </table>    
`
}

function getTime(datetime) {
    let hour = new Intl.NumberFormat("ru", {minimumIntegerDigits: 2}).format(datetime.getHours());
    let minutes = new Intl.NumberFormat("ru", {minimumIntegerDigits: 2}).format(datetime.getMinutes());
    return hour + ':' + minutes;
}

function getDate(datetime) {
    let year = datetime.getFullYear();
    let month = new Intl.NumberFormat("en", {minimumIntegerDigits: 2}).format(datetime.getMonth() + 1);
    let day = new Intl.NumberFormat("en", {minimumIntegerDigits: 2}).format(datetime.getDate());
    return (day + '.' + month + '.' + year);
}

// async function loadNewsPicture2(url) {
//     let picture;
//     let array;
//     let load= await fetch(url, {
//         method: "GET",
//        // headers: {
//          //   "Content-Type": "image/jpeg"
//             // "Access-Control-Allow-Origin": "*"
//         //}
//     }).then(r => r.blob()).then(cou => {
//         picture=URL.createObjectURL(cou);
//     }).then(cou=>console.log("Изображение загружено",cou));
//     console.log("Изображение точно загружено",load);
//      // picture = btoa(array);
//     // return `data:image/jpeg;base64,${picture}`;
//     return picture;
// }
async function loadPicture(url) {
    let array=[];
    await fetch(url, {
        method: "GET"
    }).then(r => r.blob()).then(cou => {
        array=cou;
    }).then(cou=>console.log("Изображение загружено",cou));
    return array;
}
async function loadDefaultPhotos(){
    let url="/file/defaultPhoto/"
    return {
        person: await loadPicture(url + "person.jpg"),
        election: await loadPicture(url+"election.jpg"),
        approved: await loadPicture(url+"approved.jpg"),
        rejected: await loadPicture(url+"rejected.jpg"),
        linking: await loadPicture(url+"linking.jpg")
    };
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
    if (document.getElementById("badge0") !== null && counts[0] !== 0) {
        document.getElementById("badge0").innerHTML =
            `<span id="newsCount" class="position-absolute top-1 start-1 translate-middle badge rounded-pill bg-danger" style="font-size: 12px" >
                
                <span class="visually-hidden">unread messages</span>
            </span>`
        document.getElementById("newsCount").innerHTML = counts[0];


        if (document.getElementById("badge1") !== null && counts[1] !== 0) {
            document.getElementById("badge1").innerHTML = `
         <span id="countNew1" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[1] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge1") !== null) document.getElementById("badge1").innerHTML = "";
        if (document.getElementById("badge2") !== null && counts[2] !== 0) {
            document.getElementById("badge2").innerHTML = `
          <span id="countNew2" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[2] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge2") !== null) document.getElementById("badge2").innerHTML = "";
        if (document.getElementById("badge3") !== null && counts[3] !== 0) {
            document.getElementById("badge3").innerHTML = `
          <span id="countNew3" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[3] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge3") !== null) document.getElementById("badge3").innerHTML = "";
        if (document.getElementById("badge4") !== null && counts[4] !== 0) {
            document.getElementById("badge4").innerHTML = `
          <span id="countNew4" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[4] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge4") !== null) document.getElementById("badge4").innerHTML = "";
    } else document.getElementById("badge0").innerHTML = "";
}

