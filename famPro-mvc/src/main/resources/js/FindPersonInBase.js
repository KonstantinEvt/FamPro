function getPersonFromBase(){
    loadStandardMainPanel();

    document.getElementById("taskPart").innerHTML= `
        <br>
        <div style="font-family: 'Times New Roman', serif; font-size: 14px; text-align: center; color: chocolate">Поиск по:</div>
        <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px">
            <input type="radio" class="btn-check" name="vbtn-radio" onclick="getPersonFromBaseById()" id="vbtn-radio1" autocomplete="off" checked>
            <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="vbtn-radio1">ID</label>
            <input type="radio" class="btn-check" name="vbtn-radio" onclick="getPersonFromBaseByFio()" id="vbtn-radio2" autocomplete="off"> 
            <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="vbtn-radio2">ФИО/ДР</label>
            <input type="radio" class="btn-check" name="vbtn-radio" id="vbtn-radio3" autocomplete="off">           
            <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="vbtn-radio3">ФИО</label>
            <input type="radio" class="btn-check" name="vbtn-radio" id="vbtn-radio4" autocomplete="off">           
            <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="vbtn-radio4">Any</label>
        </div>
`
    document.getElementById("resultPart").innerHTML=`
            <div id="formOfSearch" style="height: 350px"></div>
            <br>
            <div style="margin-bottom:10px; font-size: x-large; font-family: 'Times New Roman',serif">Result of search: </div>                               
            <div id="resultFindingPerson" style="font-family: 'Times New Roman', serif; font-size: 18px; text-align: center; color: chocolate">
            <em>    <div>ID: </div>
                <div style="margin-bottom:10px; font-size: larger;text-align: center; color: black;font-family: 'Times New Roman',serif" id="resultListId"><br></div>
                <div> Фото</div>
                <div id="resultPrimePhoto" style="margin-bottom:10px; font-size: larger; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
                <div>Полное имя</div>
                <div style="margin-bottom:10px; font-size: larger; text-align: left; color: black; font-family: 'Times New Roman',serif" id="resultListFullName"><br></div>
                <div>Mother info:</div>
                <div style="margin-bottom:10px; font-size: larger; text-align: left; color: black; font-family: 'Times New Roman',serif" id="resultListMother"><br></div>
            
                <div>Father info:</div>
                <div style="margin-bottom:10px; font-size: larger; text-align: left; color: black;font-family: 'Times New Roman',serif" id="resultListFather"><br></div>
               
                <div>Phone:</div>
                <div style="margin-bottom:10px; font-size: larger; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListPhone"><br></div>
            
                <div>Email:</div>
                <div style="margin-bottom:10px; font-size: larger; text-align: center; color: black; font-family: 'Times New Roman',serif" id="resultListEmail"><br></div>
                
                <div>Address:</div>
                <div style="margin-bottom:10px; font-size: larger; text-align: center; color: black; font-family: ,'Times New Roman',serif" id="resultListAddress"><br></div>
                                
            </div></em>
    `;
    getPersonFromBaseById();
}
function getPersonFromBaseById(){

    document.getElementById("formOfSearch").innerHTML=`                            
        <div class="container-fluid row mh-100 no-gutters" style="padding-top: 100px">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-6" style="min-width: 200px">
        <form class="form-group" style="margin:5px; text-align: center" id="findPersonById">

            <label for="idFindFM" style="color: chocolate; padding-top: 5px">Id:</label>
            <input class="form-control" type="text" id="idFindFM" name="idFindFM" autocomplete="on" required> 
            <br>
            <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="findPersonById()">Найти человека</button>
        </form> 
                        </span>
                        <span class="col" style="width: 20%"></span>
        </div>

`
}
function getPersonFromBaseByFio(){
    document.getElementById("formOfSearch").innerHTML=`
        <br> 
        <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-6" style="min-width: 200px">
        <form class="form-group" style="margin:5px; text-align: center" id="findPersonById">
                       
            <label for="firstNameFindFM" style="color: chocolate; padding-top: 5px">Имя:</label>
            <input class="form-control" type="text" id="firstNameFindFM" name="firstNameFindFM" autocomplete="on" required>
            
            <label for="middleNameFindFM" style="color: chocolate; padding-top: 5px">Отчество:</label>
            <input class="form-control" type="text" id="middleNameFindFM" name="middleNameFindFM" autocomplete="on" required> 
                       
            <label for="lastNameFindFM" style="color: chocolate; padding-top: 5px">Фимилия:</label>
            <input class="form-control" type="text" id="lastNameFindFM" name="lastNameFindFM" autocomplete="on" required>             

            <label for="birthdayFindFM" style="color: chocolate; padding-top: 5px">День рождения:</label>
            <input class="form-control" type="date" id="birthdayFindFM" name="birthdayFindFM" required>               

            <br>
            <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="findPersonByFio()">Найти человека</button>
        </form>
                        </span> 
                        <span class="col" style="width: 20%"></span>
        </div>
 `;
}


async function findPersonById() {
    document.getElementById("resultListId").innerHTML = `<br>`;
    document.getElementById("resultPrimePhoto").innerHTML = `<br>`;
    document.getElementById("resultListFullName").innerHTML = `<br>`;
    document.getElementById("resultListFather").innerHTML = `<br>`;
    document.getElementById("resultListMother").innerHTML = `<br>`;
    document.getElementById("resultListPhone").innerHTML = `<br>`;
    document.getElementById("resultListAddress").innerHTML = `<br>`;
    document.getElementById("resultListEmail").innerHTML = `<br>`;
    const form = document.getElementById('findPersonById');
    let formData = {
        id: form.elements.idFindFM.value
    };
    const jsonData = JSON.stringify(formData);
    await fetch("/base/family_member/" + formData.id, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(tokenUser => tokenUser.json())
        .then(familyMember => {
        document.getElementById("resultListId").innerHTML = familyMember.id;
            if (familyMember.primePhoto) getPrimePhoto(familyMember.uuid); else document.getElementById("resultPrimePhoto").innerHTML = "Фото отсутствует";
        document.getElementById("resultListFullName").innerHTML = familyMember.fullName;
        document.getElementById("resultListFather").innerHTML = familyMember.fatherInfo;
        document.getElementById("resultListMother").innerHTML = familyMember.motherInfo;
        document.getElementById("resultListPhone").innerHTML = familyMember.memberInfo.mainPhone;
        document.getElementById("resultListAddress").innerHTML = familyMember.memberInfo.mainAddress;
        document.getElementById("resultListEmail").innerHTML = familyMember.memberInfo.mainEmail;
    });

}
async function findPersonByFio() {
    document.getElementById("resultListId").innerHTML = `<br>`;
    document.getElementById("resultPrimePhoto").innerHTML = `<br>`;
    document.getElementById("resultListFullName").innerHTML = `<br>`;
    document.getElementById("resultListFather").innerHTML = `<br>`;
    document.getElementById("resultListMother").innerHTML = `<br>`;
    document.getElementById("resultListPhone").innerHTML = `<br>`;
    document.getElementById("resultListAddress").innerHTML = `<br>`;
    document.getElementById("resultListEmail").innerHTML = `<br>`;
    const form = document.getElementById('findPersonById');
    let formData = {
        firstName: form.elements.firstNameFindFM.value,
        middleName: form.elements.middleNameFindFM.value,
        lastName: form.elements.lastNameFindFM.value,
        birthday: form.elements.birthdayFindFM.value
    };
    const jsonData = JSON.stringify(formData);
    await fetch("/base/family_member/", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(tokenUser => tokenUser.json()).then(familyMember => {
        document.getElementById("resultListId").innerHTML = familyMember.id;
        if (familyMember.primePhoto) getPrimePhoto(familyMember.uuid); else document.getElementById("resultPrimePhoto").innerHTML = "Фото отсутствует";
        document.getElementById("resultListFullName").innerHTML = familyMember.fullName;
        document.getElementById("resultListFather").innerHTML = familyMember.fatherInfo;
        document.getElementById("resultListMother").innerHTML = familyMember.motherInfo;
        document.getElementById("resultListPhone").innerHTML = familyMember.memberInfo.mainPhone;
        document.getElementById("resultListAddress").innerHTML = familyMember.memberInfo.mainAddress;
        document.getElementById("resultListEmail").innerHTML = familyMember.memberInfo.mainEmail;
    });

}
function getPrimePhoto(uuid){
    document.getElementById("resultPrimePhoto").innerHTML =`
    <img src="/file/get/${uuid}" alt="Prime photo of Person" width="250px"/>
        `
}
