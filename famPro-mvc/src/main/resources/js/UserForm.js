let counts = [0, 0, 0, 0, 0];
let cards;
let numCards;
let rawNews;
let externId;
let ownLinkId;
let ownId;
let tempSecurity;
let tempPerson;
let tempTextPhoto;
const infoAbsent = "Информация отсутствует";
const infoClosed = "Информация закрыта";
const infoUncorrected="Неверная информация"

let tempPrimePhoto;
let tempBirthPhoto;
let tempBurialPhoto;

loadOnlineUser();
loadStandardMainPanel();
loadNewsCounts();
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
        ownId = user.id;
        document.getElementById("nav0").innerHTML = fullName;
        document.getElementById("nav1").innerHTML = nickName;
        document.getElementById("nav2").innerHTML = role;
        if (role !== "LinkedUser") document.getElementById("linking-knopa").innerHTML = `<a class="nav-link" style="color: red;" href="#mainPanel"
                           onclick="getPersonFromBase(0)">Связать</a>`;
        else {
            fetch("/guard/getLinkGuard", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                    // "Access-Control-Allow-Origin": "*"
                }
            }).then(promise => promise.text()).then(x => {
                ownLinkId = x;
                console.log(ownLinkId)
            })

            document.getElementById("linking-knopa").innerHTML = ``;
            document.getElementById("getLinkedPerson").innerHTML = `
                <a class="dropdown-item" style="color: chocolate;" href="#mainPanel" onclick="getPersonalPage()">Ты в базе</a>`;
        }
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

function getAge(birthday, deathday) {
    if (deathday===infoAbsent) return deathday;
    let age;
    let alife = false;
    if (deathday === undefined || deathday === null || deathday === '') {
        deathday = new Date();
        alife = true;
    }
    let fullYear = deathday.getFullYear() - birthday.getFullYear();
    if ((fullYear > 120 && alife) || deathday === infoAbsent) age = "не известно";
    else if (fullYear > 120) age = "Столько не живут";
    else {
        let mouthDelta = deathday.getMonth() - birthday.getMonth();
        let dayDelta = deathday.getDay() - birthday.getDay();
        age = fullYear + " year " + mouthDelta + " month "
        if (mouthDelta === 0 && dayDelta === 0) age += " (birthday now)"
        // age = new Intl.NumberFormat("en", {minimumIntegerDigits: 3}).format(((new Date(tempPerson.birthday) - new Date()) / 86400000-fullYear/4)/365);
        // if ((vis % 4 === 0 && vis % 100 !== 100) || vis % 400 === 0)
    }
    return age;
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

function loadNewsCounts() {
    fetch("/news/counts", {
        method: "GET",
        headers: {
            "Content-Type": "text/javascript; charset=UTF-8"
            // "Access-Control-Allow-Origin": "*"
        }
    }).then(r => r.json()).then(cou => {
        if (document.getElementById("message-list") !== null && document.getElementById("family-list") !== undefined && counts[3] !== cou[3]) loadingIndividualNews("family", false);
        if (document.getElementById("message-list") !== null && document.getElementById("private-list") !== undefined && counts[4] !== cou[4]) loadingIndividualNews("private", false);
        counts = cou;
    })
    if (document.getElementById("badge0") !== null && document.getElementById("badge0") !== undefined && counts[0] !== 0) {
        document.getElementById("badge0").innerHTML =
            `<span id="newsCount" class="position-absolute top-1 start-1 translate-middle badge rounded-pill bg-danger" style="font-size: 12px" >
                
                <span class="visually-hidden">unread messages</span>
            </span>`
        document.getElementById("newsCount").innerHTML = counts[0];


        if (document.getElementById("badge1") !== null && document.getElementById("badge1") !== undefined && counts[1] !== 0) {
            document.getElementById("badge1").innerHTML = `
         <span id="countNew1" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[1] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge1") !== null) document.getElementById("badge1").innerHTML = "";
        if (document.getElementById("badge2") !== null && document.getElementById("badge2") !== undefined && counts[2] !== 0) {
            document.getElementById("badge2").innerHTML = `
          <span id="countNew2" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[2] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge2") !== null) document.getElementById("badge2").innerHTML = "";
        if (document.getElementById("badge3") !== null
            && document.getElementById("badge3") !== undefined
            && counts[3] !== 0) {
            document.getElementById("badge3").innerHTML = `
          <span id="countNew3" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[3] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge3") !== null) document.getElementById("badge3").innerHTML = "";
        if (document.getElementById("badge4") !== null && document.getElementById("badge4") !== undefined && counts[4] !== 0) {
            document.getElementById("badge4").innerHTML = `
          <span id="countNew4" class="position-absolute top-1 start-1 translate-small badge rounded-pill bg-danger" style="font-size: 10px">        
                <span>` + counts[4] + `</span>
                <span class="visually-hidden">unread messages</span>
            </span>`
        } else if (document.getElementById("badge4") !== null) document.getElementById("badge4").innerHTML = "";
    } else document.getElementById("badge0").innerHTML = "";
}

