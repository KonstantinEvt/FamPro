let countPhone = 0;
let countEmail = 0;
let countOtherNames = 1;
let insertOther = [];
let insertEmail = [];
let insertPhone = [];

function addFamilyMember() {
    countPhone = 0;
    countEmail = 0;
    countOtherNames = 1;
    insertOther = [];
    document.getElementById("mainPanel").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Adding Person ***</div>
<form class="form-group" style="margin:5px; text-align: center" id="baseFormAddFM">
        <div class="accordion"  id="accordionExample" >
        <div class="accordion-item">
            <h2 class="accordion-header">
               <button class="accordion-button" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                Основная информация
                </button>
            </h2>
            <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
        <label for="firstNameAddFM" style="color: chocolate; padding-top: 5px">FirstName:</label>
        <input class="form-control" type="text" id="firstNameAddFM" name="firstNameAddFM" required>
        <label for="middleNameAddFM" style="color: chocolate; padding-top: 5px">MiddleName:</label>
        <input class="form-control" type="text" id="middleNameAddFM" name="middleNameAddFM" required>
        <label for="lastNameAddFM" style="color: chocolate; padding-top: 5px">LastName:</label>
        <input class="form-control" type="text" id="lastNameAddFM" name="lastNameAddFM" required>
        <label for="birthdayAddFM" style="color: chocolate; padding-top: 5px">Birthday:</label>
        <input class="form-control" type="date" id="birthdayAddFM" name="birthdayAddFM" required>
        <label for="deathdayAddFM" style="color: chocolate; padding-top: 5px">Date of death:</label>
        <input class="form-control" type="date" id="deathdayAddFM" name="deathdayAddFM">
        <label for="chooseSexAddFM" style="color: chocolate; padding-top: 5px">Gender:</label>
        <select class="form-select" id="chooseSexAddFM" aria-label="chooseSexAddFM">            
            <option value="MALE" selected>MALE</option>
            <option value="FEMALE">FEMALE</option>
        </select>
        <div style="margin-top: 15px">  Внимание: без заполненных полей "Имя", "Отчество", "Фамилия", "День рождения"</div> 
        <div>НИКАКАЯ информация в базу внесена не будет!</div>
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
                <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseParents" aria-expanded="false" aria-controls="collapseParents">
                    Информация о родителях
                </button>
            </h2>
            <div id="collapseParents" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 50%">
        <div style="color: darkred; padding-top: 5px; padding-bottom: 10px">Mother</div>                        
        <label for="firstNameMotherAddFM" style="color: chocolate; padding-top: 5px">FirstName:</label>
        <input class="form-control" type="text" id="firstNameMotherAddFM" name="firstNameMotherAddFM">
        <label for="middleNameMotherAddFM" style="color: chocolate; padding-top: 5px">MiddleName:</label>
        <input class="form-control" type="text" id="middleNameMotherAddFM" name="middleMotherAddFM" >
        <label for="lastNameMotherAddFM" style="color: chocolate; padding-top: 5px">LastName:</label>
        <input class="form-control" type="text" id="lastNameMotherAddFM" name="lastNameMotherAddFM">
        <label for="birthdayMotherAddFM" style="color: chocolate; padding-top: 5px">Birthday:</label>
        <input class="form-control" type="date" id="birthdayMotherAddFM" name="birthdayMotherAddFM">
                        </span>
                        <span class="col" style="width: 50%">
        <div style="color: darkred; padding-top: 5px; padding-bottom: 10px">Father</div>                        
        <label for="firstNameFatherAddFM" style="color: chocolate; padding-top: 5px">FirstName:</label>
        <input class="form-control" type="text" id="firstNameFatherAddFM" name="firstNameFatherAddFM">
        <label for="middleNameFatherAddFM" style="color: chocolate; padding-top: 5px">MiddleName:</label>
        <input class="form-control" type="text" id="middleNameFatherAddFM" name="middleFatherAddFM">
        <label for="lastNameFatherAddFM" style="color: chocolate; padding-top: 5px">LastName:</label>
        <input class="form-control" type="text" id="lastNameFatherAddFM" name="lastNameFatherAddFM">
        <label for="birthdayFatherAddFM" style="color: chocolate; padding-top: 5px">Birthday:</label>
        <input class="form-control" type="date" id="birthdayFatherAddFM" name="birthdayFatherAddFM">
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item" >
            <h2 class="accordion-header">
                <button class="accordion-button collapsed"  style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOtherName" aria-expanded="false" aria-controls="collapseOtherName">
                    Псевдонимы/Девичьи/Другие имена
                </button>
            </h2>
            <div id="collapseOtherName" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body" >
                    <div id="otherAddFM0" class="container-fluid row mh-100 no-gutters" style="font-size:14px; color: chocolate; padding: 0; margin-top: 5px">
                        <div class="col-3" >
        <label for="firstNameOtherAddFM0">FirstName:</label>
        <input class="form-control form-control-border-color--warning" style="font-size:14px" type="text" id="firstNameOtherAddFM0" name="firstNameOtherAddFM0" autocomplete="on" >
                        
                        </div>
                        <div class="col-3" >
        <label for="middleNameOtherAddFM0" >MiddleName:</label>
        <input class="form-control" type="text" style="font-size:14px" id="middleNameOtherAddFM0" name="middleNameOtherAddFM0" >
                        </div>
                         <div class="col" >
        <label for="lastNameOtherAddFM0">LastName:</label>
        <input class="form-control" type="text" style="font-size:14px" id="lastNameOtherAddFM0" name="lastNameOtherAddFM0" >
                        </div>
                        <div id="addOtherAddFM" class="col-1" style="width: 8%">
                                              <br> 
                        <button class="btn btn-outline-success" type="button" style="font-size:14px" onclick="addOtherField()">+</button>  
                        <span id="addedButtonsOtherAddFM"></span>
                        </div>
                    </div>
                    <div id="otherAddFM1" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM2" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM3" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM4" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <br>
                    <div>Количество попыток введения дополнительных данных ограничено</div>
                    <div>Осталось попыток:</div>
                    <span>OtherNames <span id="tryOther">4</span></span></span>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                    Контактная информация
                </button>
            </h2>
            <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="text-align: right; width: 20%"> 
                            <div id="deleteButtonsEmailAddFM" style="margin-bottom: 45px; margin-top: 45px;"><br>
                            </div>
                            <div id="deleteButtonsPhoneAddFM" style=" margin-top: 0"><br>
                            </div>  
                        </span>
                        <span class="col-6" style="min-width: 200px">
        <label for="mainEmailAddFM" style="color: chocolate; padding-top: 5px">Main Email:</label>
        <input class="form-control form-control-border-color--warning" type="text" id="mainEmailAddFM" name="mainEmailAddFM" autocomplete="on" >
        <div id="addedEmailAddFM0"></div>
        <div id="addedEmailAddFM1"></div>
        <div id="addedEmailAddFM2"></div>
        <div id="addedEmailAddFM3"></div>
        <div id="addedEmailAddFM4"></div>
        
        <label for="mainPhoneAddFM" style="color: chocolate; padding-top: 5px">Main phone:</label>
        <input class="form-control" type="text" id="mainPhoneAddFM" name="mainPhoneAddFM" >
        <div id="addedPhoneAddFM0" ></div>
        <div id="addedPhoneAddFM1" ></div>
        <div id="addedPhoneAddFM2" ></div>
        <div id="addedPhoneAddFM3" ></div>
        <div id="addedPhoneAddFM4" ></div>
        
                        </span>
                        <span id="addButtons" class="col" style="text-align: left; width: 20%">                        
                        <br> 
                        <button class="btn btn-outline-success" type="button" style="margin-top: 5px" onclick="addEmailsField()">+</button>  
                        <span id="addedButtonsEmailAddFM"></span>
                        <br>
                        <button class="btn btn-outline-success" type="button" style="margin-top: 29px" onclick="addPhonesField()">+</button>
                        <span id="addedButtonsPhoneAddFM"></span>  
</span>
                    </div>
                    <br>
                    <div>Количество попыток введения дополнительных данных ограничено</div>
                    <div>Осталось попыток:</div>
                    <span>Email: <span id="tryEmail">5</span> Phone: <span id="tryPhone">5</span></span>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                    Main address
                </button>
            </h2>
            <div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
        <label for="indexAddFM" style="color: chocolate; padding-top: 5px">Почтовый индекс:</label>
        <input class="form-control" type="text" id="indexAddFM" name="indexAddFM" >                  
        <label for="countryAddFM" style="color: chocolate; padding-top: 5px">Страна:</label>
        <input class="form-control" type="text" id="countryAddFM" name="countryAddFM" >
        <label for="regionAddFM" style="color: chocolate; padding-top: 5px">Регион / область:</label>
        <input class="form-control" type="text" id="regionAddFM" name="regionAddFM" >
        <label for="cityAddFM" style="color: chocolate; padding-top: 5px">Город / Населенный пункт:</label>
        <input class="form-control" type="text" id="cityAddFM" name="cityAddFM" >
        <label for="streetAddFM" style="color: chocolate; padding-top: 5px">Улица / Проспект / Аллея:</label>
        <input class="form-control" type="text" id="streetAddFM" name="streetAddFM" >        
        <label for="houseAddFM" style="color: chocolate; padding-top: 5px">Номер дома:</label>
        <input class="form-control" type="text" id="houseAddFM" name="houseAddFM" >
        <label for="buildingAddFM" style="color: chocolate; padding-top: 5px">Корпус здания:</label>
        <input class="form-control" type="text" id="buildingAddFM" name="buildingAddFM" >
        <label for="flatAddFM" style="color: chocolate; padding-top: 5px">Номер квартиры:</label>
        <input class="form-control" type="text" id="flatAddFM" name="flatAddFM" >  
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#birthAddressAddFM" aria-expanded="false" aria-controls="birthAddressAddFM">
                    Place of birth
                </button>
            </h2>
            <div id="birthAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
        <label for="birthCountryAddFM" style="color: chocolate; padding-top: 5px">Страна:</label>
        <input class="form-control" type="text" id="birthCountryAddFM" name="birthCountryAddFM" >
        <label for="birthRegionAddFM" style="color: chocolate; padding-top: 5px">Регион / область:</label>
        <input class="form-control" type="text" id="birthRegionAddFM" name="birthRegionAddFM" >
        <label for="birthCityAddFM" style="color: chocolate; padding-top: 5px">Город / Населенный пункт:</label>
        <input class="form-control" type="text" id="birthCityAddFM" name="birthCityAddFM" >
        <label for="birthStreetAddFM" style="color: chocolate; padding-top: 5px">Улица / Проспект / Аллея:</label>
        <input class="form-control" type="text" id="birthStreetAddFM" name="birthStreetAddFM" >        
        <label for="birthHouseAddFM" style="color: chocolate; padding-top: 5px">Роддом:</label>
        <input class="form-control" type="text" id="birthHouseAddFM" name="birthHouseAddFM" >
        <label for="birthRegisterAddFM" style="color: chocolate; padding-top: 5px">Регистрационный орган:</label>
        <input class="form-control" type="text" id="birthRegisterAddFM" name="birthRegisterAddFM" >

                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#burialAddressAddFM" aria-expanded="false" aria-controls="burialAddressAddFM">
                    Place of burial
                </button>
            </h2>
            <div id="burialAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
        <label for="burialCountryAddFM" style="color: chocolate; padding-top: 5px">Страна:</label>
        <input class="form-control" type="text" id="burialCountryAddFM" name="burialCountryAddFM" >
        <label for="burialRegionAddFM" style="color: chocolate; padding-top: 5px">Регион / область:</label>
        <input class="form-control" type="text" id="burialRegionAddFM" name="burialRegionAddFM" >
        <label for="burialCityAddFM" style="color: chocolate; padding-top: 5px">Город / Населенный пункт:</label>
        <input class="form-control" type="text" id="burialCityAddFM" name="burialCityAddFM" >
        <label for="burialCemeteryAddFM" style="color: chocolate; padding-top: 5px">Кладбище:</label>
        <input class="form-control" type="text" id="burialCemeteryAddFM" name="burialCemeteryAddFM" >  
        <label for="burialStreetAddFM" style="color: chocolate; padding-top: 5px">Улица / Проспект / Аллея:</label>
        <input class="form-control" type="text" id="burialStreetAddFM" name="burialStreetAddFM" >        
        <label for="burialHouseAddFM" style="color: chocolate; padding-top: 5px">Массив / Раздел:</label>
        <input class="form-control" type="text" id="burialHouseAddFM" name="burialHouseAddFM" >
        <label for="burialBuildingAddFM" style="color: chocolate; padding-top: 5px">Номер участка:</label>
        <input class="form-control" type="text" id="burialBuildingAddFM" name="burialBuildingAddFM" >
        <label for="burialFlatAddFM" style="color: chocolate; padding-top: 5px">Номер могилы:</label>
        <input class="form-control" type="text" id="burialFlatAddFM" name="burialFlatAddFM" >  
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br>
    <label for="primePhoto" style="color: chocolate;">Файл фото</label>
    <input class="form-control" type="file" id="primePhoto" name="primePhoto" value="DDD">
    <br>
    <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitBaseFormAddFM()">Add Person</button>
    <br><br>    
        <span style="font-size: larger; font-family: 'Times New Roman',serif; text-align: left">Common Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultListCreateFM"></span></span>
            <br><br>    
        <span style="font-size: larger; font-family: 'Times New Roman',serif; text-align: left">Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSavePrimePhoto"></span></span>
    <br>
</form>
 `
}

function addPhonesField() {
    if (countPhone <= 4) {
        document.getElementById("deleteButtonsPhoneAddFM").innerHTML += `
        <div id="leftPhoneAddFM${countPhone}">            
            <br> 
            <button class="btn btn-outline-danger" style=" margin-top: 5px" onclick="deletePhoneField(${countPhone})">X</button>  
        </div>     
`
        document.getElementById("addedPhoneAddFM" + countPhone).innerHTML = ` 
        <span id="centrePhoneAddFM${countPhone}">
            <label for="phoneAddFM${countPhone}" style="color: black; padding-top: 5px">Additional phone:</label>
            <input class="form-control" type="text" id="phoneAddFM${countPhone}" name="phoneAddFM${countPhone}" autoComplete="off">
        </span>
`
        document.getElementById("addedButtonsPhoneAddFM").innerHTML += `
        <span id="rightPhoneAddFM${countPhone}">
            <br>
            <div style="margin-top: 19px"><br></div>
        </span>`
        insertPhone[countPhone] = 'yes'
        countPhone++;
        document.getElementById("tryPhone").innerHTML = (5 - countPhone) + ``;
    }
}

function addEmailsField() {
    if (countEmail <= 4) {
        document.getElementById("deleteButtonsEmailAddFM").innerHTML += `          
        <div id="leftEmailAddFM${countEmail}">            
            <br> 
            <button class="btn btn-outline-danger" style="margin-top: 5px" onclick="deleteEmailField(${countEmail})">X</button>  
        </div>     
`
        document.getElementById("addedEmailAddFM" + countEmail).innerHTML = `
        <span id="centerEmailAddFM${countEmail}">
            <label for="emailAddFM${countEmail}" style="color: black; padding-top: 5px" >Additional Email:</label>
            <input class="form-control" type="text" id="emailAddFM${countEmail}" name="emailAddFM${countEmail}" autoComplete="off">
        </span>
`
        document.getElementById("addedButtonsEmailAddFM").innerHTML += `
        <span id="rightEmailAddFM${countEmail}">
            <br>
            <div style="margin-top: 19px"><br></div>
        </span>`
        insertEmail[countEmail] = 'yes'
        countEmail++;
        document.getElementById("tryEmail").innerHTML = (5 - countEmail) + ``;
    }
}

function deletePhoneField(phoneString) {
    document.getElementById("leftPhoneAddFM" + phoneString).innerHTML = ``
    document.getElementById("centrePhoneAddFM" + phoneString).innerHTML = ``
    document.getElementById("rightPhoneAddFM" + phoneString).innerHTML = ``
    insertPhone[phoneString] = null;
}

function deleteEmailField(emailString) {
    document.getElementById("leftEmailAddFM" + emailString).innerHTML = ``
    document.getElementById("centerEmailAddFM" + emailString).innerHTML = ``
    document.getElementById("rightEmailAddFM" + emailString).innerHTML = ``
    insertEmail[emailString] = null;
}

function addOtherField() {
    if (countOtherNames <= 4) {
        document.getElementById("otherAddFM" + countOtherNames).innerHTML = `
        <div id="deleteOtherAddFM0" class="col-1" style="width: 12%">
            <br> 
                <button class="btn btn-outline-danger" style="font-size:14px" onclick="deleteOtherField(${countOtherNames})">X</button>
        </div>
        <div class="col-3" >
            <label for="firstNameOtherAddFM${countOtherNames}" >FirstName:</label>
            <input class="form-control form-control-border-color--warning" style="font-size:14px" type="text" id="firstNameOtherAddFM${countOtherNames}" name="firstNameOtherAddFM${countOtherNames}" autocomplete="off" ></div>
        <div class="col-3" >
            <label for="middleNameOtherAddFM${countOtherNames}" >MiddleName:</label>
            <input class="form-control" style="font-size:14px" type="text" id="middleNameOtherAddFM${countOtherNames}" name="middleNameOtherAddFM${countOtherNames}" autocomplete="off" >
        </div>
        <div class="col" >
            <label for="lastNameOtherAddFM${countOtherNames}" >LastName:</label>
            <input class="form-control" style="font-size:14px" type="text" id="lastNameOtherAddFM${countOtherNames}" name="lastNameOtherAddFM${countOtherNames}" autocomplete="off">
        </div>
        `;
        insertOther[countOtherNames] = 'yes';
        countOtherNames++;

        document.getElementById("tryOther").innerHTML = (5 - countOtherNames) + ``;
    }
}

function deleteOtherField(otherNamesString) {
    document.getElementById("otherAddFM" + otherNamesString).innerHTML = ``;
    insertOther[otherNamesString] = null;
}

function submitBaseFormAddFM() {
    const form = document.getElementById('baseFormAddFM');
    let other = [];
    let firstOther1 = "firstNameOtherAddFM";
    let secondOther1 = "middleNameOtherAddFM";
    let thirdOther1 = "lastNameOtherAddFM";
    let otherPhones = [];
    let otherEmails = [];
    let primePhotoExist=false;

    if (document.getElementById("primePhoto").files[0] != null) {
        primePhotoExist=true;
        let primePhoto = document.getElementById("primePhoto").files[0];
        let file = new FormData()
        file.append("primePhoto", primePhoto);
        fetch("/file/savePrimePhoto", {
            method: 'POST',
            headers: {
            },
            body: file,
        }).then(async status => {
            document.getElementById("resultSavePrimePhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSavePrimePhoto").innerHTML = 'Prime photo not selected!';

    insertOther[0] = 'yes';
    for (let i = 0; i < 5; i++) {
        if (insertOther[i] === 'yes') {
            other.push({
                firstName: document.getElementById(firstOther1 + i).value,
                middleName: document.getElementById(secondOther1 + i).value,
                lastName: document.getElementById(thirdOther1 + i).value
            });
        }
    }
    for (let i = 0; i < 5; i++) {
        if (insertPhone[i] === 'yes') {
            otherPhones.push({internName: document.getElementById("phoneAddFM" + i).value}
            );
        }
    }
    for (let i = 0; i < 5; i++) {
        if (insertEmail[i] === 'yes') {
            otherEmails.push({internName: document.getElementById("emailAddFM" + i).value}
            );
        }
    }
    let formData = {
        primePhoto:primePhotoExist,
        firstName: form.elements.firstNameAddFM.value,
        middleName: form.elements.middleNameAddFM.value,
        lastName: form.elements.lastNameAddFM.value,
        birthday: form.elements.birthdayAddFM.value,
        deathday: form.elements.deathdayAddFM.value,
        sex: form.elements.chooseSexAddFM.value,
        memberInfo: {
            mainPhone: form.elements.mainPhoneAddFM.value,
            mainEmail: form.elements.mainEmailAddFM.value,
            phones: otherPhones,
            emails: otherEmails,
            addresses: [
                {
                    country: form.elements.countryAddFM.value,
                    region: form.elements.regionAddFM.value,
                    city: form.elements.cityAddFM.value,
                    index: form.elements.indexAddFM.value,
                    street: form.elements.streetAddFM.value,
                    house: form.elements.houseAddFM.value,
                    building: form.elements.buildingAddFM.value,
                    flat: form.elements.flatAddFM.value
                }
            ]
        },
        motherFio: {
            firstName: form.elements.firstNameMotherAddFM.value,
            middleName: form.elements.middleNameMotherAddFM.value,
            lastName: form.elements.lastNameMotherAddFM.value,
            birthday: form.elements.birthdayMotherAddFM.value
        },
        fatherFio: {
            firstName: form.elements.firstNameFatherAddFM.value,
            middleName: form.elements.middleNameFatherAddFM.value,
            lastName: form.elements.lastNameFatherAddFM.value,
            birthday: form.elements.birthdayFatherAddFM.value
        },
        fioDtos: other,
        birth: {
            country: form.elements.birthCountryAddFM.value,
            region: form.elements.birthRegionAddFM.value,
            city: form.elements.birthCityAddFM.value,
            street: form.elements.birthStreetAddFM.value,
            birthHouse: form.elements.birthHouseAddFM.value,
            registration: form.elements.birthRegisterAddFM.value
        },
        burial: {
            country: form.elements.burialCountryAddFM.value,
            region: form.elements.burialRegionAddFM.value,
            city: form.elements.burialCityAddFM.value,
            cemetery: form.elements.burialCemeteryAddFM.value,
            street: form.elements.burialStreetAddFM.value,
            chapter: form.elements.burialHouseAddFM.value,
            square: form.elements.burialBuildingAddFM.value,
            grave: form.elements.burialFlatAddFM.value
        }
    };
    const jsonData = JSON.stringify(formData);
    fetch("/base/family_member/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateFM").innerHTML = await status.text();
    });
    // var input = document.querySelector('input[type="file"]')
    // let file = new FormData()
    // file.append('primePhoto', input.files[0])
    // file.append('user', 'id')

}