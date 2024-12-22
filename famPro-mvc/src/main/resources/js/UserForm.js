loadOnlineUser();
loadStandardMainPanel()
// let countPhone = 0;
// let countEmail = 0;
// let countOtherNames = 0;

function loadOnlineUser()
{
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
function loadStandardMainPanel(){
    document.getElementById("mainPanel").innerHTML=`
    <div class="list-group list-group-flush col-2" id="taskPart" style="height: 1200px; min-width: 100px; margin-left: 10px" role="tablist" >
    </div>
    <div style="height: 1200px" class="tab-content col bg-light" id="resultPart"></div>
    `
}


