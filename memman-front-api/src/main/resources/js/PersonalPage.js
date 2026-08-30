function getPersonalPage() {
    document.getElementById("mainPanel").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Personal Page ***</div>
<div style="text-align: center;" id="yourselfMain"></div>
<div style="text-align: center;" id="yourselfBiometric"></div>
<div style="text-align: center;" id="yourselfBirth"></div>
<div style="text-align: center;" id="yourselfContacts"></div>
<div style="text-align: center;" id="yourselfDescription"></div>
<div style="text-align: center;" id="yourselfOtherNames"></div>
<div style="text-align: center;" id="yourselfSecurity"></div>
`
    loadYourself();
}

function loadYourself() {
    let url = `/base/family_member/i`
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    }).then(tokenUser => tokenUser.json()).then(async familyMember => {
        tempPerson = familyMember;
        console.log(familyMember);
        paintYourself()
    }).catch(() => {
        document.getElementById("personalPage").innerHTML = `Something wrong`;
    })
}

function paintYourself() {
    document.getElementById("yourselfMain").innerHTML = `<div>Id:${tempPerson.id}</div>
<div>you are : ${tempPerson.fullName}</div>
<div>father info : ${tempPerson.fatherInfo}</div>
    <div>mother info : ${tempPerson.motherInfo} </div>`;
}