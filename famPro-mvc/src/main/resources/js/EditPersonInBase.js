function editPersonInBase() {
    clearMainVariable();
    document.getElementById("mainPanel").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Edit Person ***</div>
<div style="text-align: center; color: black;font-family: 'Times New Roman',serif; font-size: 18px">ID: <span style="color: darkred">${tempPerson.id}</span></div>
<form class="form-group" style="margin:5px; text-align: center;" id="baseFormAddFM">
        <div class="accordion" style=" --bs-accordion-active-bg: #eaecbd;  --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgb(234 212 101 / 25%);" id="accordionExample" >
        <div class="accordion-item">
            <h2 class="accordion-header">
               <button class="accordion-button" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                Основная информация
                </button>
            </h2>
            <div id="collapseOne" class="accordion-collapse collapse show" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
                            <div class="container-fluid row mh-100 no-gutters">
                                <span class="col" style="width: 20%"></span>
                                <span class="col-6" style="width: 250px">
                                    <div id="frontPhoto" style="margin-bottom:2px; font-size: 18px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
                                </span>
                                <span class="col" style="width: 20%"></span>
                            </div>                        
                        <label id="labelPrimePhoto" for="PrimePhoto" style="padding-top:12px; color: chocolate;" hidden="hidden">Файл фото</label>
        <input class="form-control" style="padding-top: 2px;padding-bottom: 12px" type="file" id="PrimePhoto" name="PrimePhoto" value="DDD" accept="image/*" onchange="resizeImagePlus('PrimePhoto')"  hidden="hidden">
        <div id="canvasBlockPrimePhoto"  style=" align-content: center; align-items: center; text-align: center" hidden="hidden"> <div style="padding-top:12px; color: chocolate;">Предварительный просмотр фото</div><canvas style="width: 250px ;" id="canvasPrimePhoto"></canvas></div>
        <label for="firstNameAddFM" style="color: chocolate">FirstName:</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="firstNameAddFM" name="firstNameAddFM" value="${tempPerson.firstName}" required>
        <label for="middleNameAddFM" style="color: chocolate; padding-top: 4px">MiddleName:</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="middleNameAddFM" name="middleNameAddFM" value="${tempPerson.middleName}" required>
        <label for="lastNameAddFM" style="color: chocolate; padding-top: 4px">LastName:</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="lastNameAddFM" name="lastNameAddFM" value="${tempPerson.lastName}" required>
        <label for="birthdayAddFM" style="color: chocolate; padding-top: 5px">Birthday:</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="date" id="birthdayAddFM" name="birthdayAddFM">
        <label for="deathdayAddFM" style="color: chocolate; padding-top: 4px">Date of death:</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="date" id="deathdayAddFM" name="deathdayAddFM" >
        <label for="chooseSexAddFM" style="color: chocolate; padding-top: 4px">Gender:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;" id="chooseSexAddFM" aria-label="chooseSexAddFM">            
            <option value="MALE" selected>MALE</option>
            <option value="FEMALE">FEMALE</option>
        </select>
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseParents" aria-expanded="false" aria-controls="collapseParents">
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
        <input class="form-control" type="text" id="middleNameMotherAddFM" name="middleMotherAddFM">
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
                    <br>
                    <span id="lockParentsInfo"></span>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="otherNamesEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed"  style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOtherName" aria-expanded="false" aria-controls="collapseOtherName">
                    Псевдонимы/Девичьи/Другие имена
                </button>
            </h2>
            <div id="collapseOtherName" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body" >
                    <div id="otherAddFM0" class="container-fluid row mh-100 no-gutters" style="font-size:14px; color: chocolate; padding: 0; margin-top: 5px">
                        <div class="col-2" style="padding: 3px">
        <label for="firstNameOtherAddFM0">FirstName:</label>
        <input class="form-control form-control-border-color--warning" style="padding:3px; font-size:12px" type="text" id="firstNameOtherAddFM0" name="firstNameOtherAddFM0" autocomplete="on" >
                        
                        </div>
                        <div class="col-3" style="padding: 3px">
        <label for="middleNameOtherAddFM0" >MiddleName:</label>
        <input class="form-control" type="text" style="padding:3px; font-size:12px" id="middleNameOtherAddFM0" name="middleNameOtherAddFM0" >
                        </div>
                         <div class="col" style="padding: 3px">
        <label for="lastNameOtherAddFM0">LastName:</label>
        <input class="form-control" type="text" style="padding:3px; font-size:12px" id="lastNameOtherAddFM0" name="lastNameOtherAddFM0" >
                        </div>
                        <div id="addOtherAddFM" class="col-1" style="width: 5%;">
                                              <br> 
                        <button class="btn btn-outline-success" type="button" style="padding:5px; margin-bottom: 10px; font-size:14px" onclick="addOtherFieldEdit()">+</button>  
<!--                        <span id="addedButtonsOtherAddFM"></span>-->
                        </div>
                        <div  class="col-1" style="width: 5%;">
                        <br>
                        <button id="changeWithMain0" class="btn btn-outline-warning" type="button" style="padding:5px; margin-bottom: 10px; font-size:14px" onclick="changeWithMain(0)">&#8693;</button>  
                        </div>
                    </div>
                    <div id="otherAddFM1" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM2" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM3" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM4" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <br>
                    <div id="otherNamesInfo"></div>
                    <div>Количество попыток введения дополнительных данных ограничено</div>
                    <div>Осталось попыток:</div>
                    <span>OtherNames <span id="tryOther">4</span></span>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="contactPage" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseContact" aria-expanded="false" aria-controls="collapseContact">
                    Контактная информация
                </button>
            </h2>
            <div id="collapseContact" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="text-align: right; width: 20%"> 
                            <div id="deleteButtonsEmailAddFM" style="margin-bottom: 45px; margin-top: 45px;"><br>
                            </div>
                            <div id="deleteButtonsPhoneAddFM" style=" margin-top: 0"><br>
                            </div>  
                        </span>
                        <span class="col-6" style="min-width: 200px">
        <label for="mainEmailAddFM" style="color: chocolate; padding-top: 5px" >Main Email:</label>
        <input class="form-control" type="text" id="mainEmailAddFM" name="mainEmailAddFM" autocomplete="off" disabled="disabled">
        <div id="addedEmailAddFM0"></div>
        <div id="addedEmailAddFM1"></div>
        <div id="addedEmailAddFM2"></div>
        <div id="addedEmailAddFM3"></div>
        <div id="addedEmailAddFM4"></div>
        
        <label for="mainPhoneAddFM" style="color: chocolate; padding-top: 5px">Main phone:</label>
        <input class="form-control" type="text" id="mainPhoneAddFM" name="mainPhoneAddFM" autocomplete="off" disabled="disabled">
        <div id="addedPhoneAddFM0" ></div>
        <div id="addedPhoneAddFM1" ></div>
        <div id="addedPhoneAddFM2" ></div>
        <div id="addedPhoneAddFM3" ></div>
        <div id="addedPhoneAddFM4" ></div>
                        </span>
                        <span id="addButtons" class="col" style="text-align: left; width: 20%">                        
                        <br> 
                        <button class="btn btn-outline-success" type="button" style="margin-top: 5px" onclick="addEmailsFieldEdit()" id="addEmailButtom" hidden="hidden">+</button>  
                        <span id="addedButtonsEmailAddFM"></span>
                        <br>
                        <button class="btn btn-outline-success" type="button" style="margin-top: 29px" onclick="addPhonesFieldEdit()" id="addPhoneButtom" hidden="hidden">+</button>
                        <span id="addedButtonsPhoneAddFM"></span>  
                        </span>
                    </div>
                    <div>Осталось попыток:
                    <span>Email: <span id="tryEmail">4</span> Phone: <span id="tryPhone">4</span></span></div>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="addressEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                    Main address
                </button>
            </h2>
            <div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
        <label for="indexAddFM" style="color: chocolate">Почтовый индекс:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="indexAddFM" name="indexAddFM" >                  
        <label for="countryAddFM" style="color: chocolate; padding-top: 2px">Страна:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="countryAddFM" name="countryAddFM" >
        <label for="regionAddFM" style="color: chocolate; padding-top: 2px">Регион / область:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="regionAddFM" name="regionAddFM" >
        <label for="cityAddFM" style="color: chocolate; padding-top: 2px">Город / Населенный пункт:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="cityAddFM" name="cityAddFM" >
        <label for="streetAddFM" style="color: chocolate; padding-top: 2px">Улица / Проспект / Аллея:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="streetAddFM" name="streetAddFM" >        
        <label for="houseAddFM" style="color: chocolate; padding-top: 2px">Номер дома:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="houseAddFM" name="houseAddFM" >
        <label for="buildingAddFM" style="color: chocolate; padding-top: 2px">Корпус здания:</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:1px;" type="text" id="buildingAddFM" name="buildingAddFM" >
        <label for="flatAddFM" style="color: chocolate; padding-top: 4px">Номер квартиры:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:2px;" type="text" id="flatAddFM" name="flatAddFM" >  
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="securitySecurity" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSecurity" aria-expanded="false" aria-controls="collapseSecurity">
                    Уровни конфиденциальности
                </button>
            </h2>
            <div id="collapseSecurity" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 30%"></span>
                        <span class="col-6" style="min-width: 300px">
                                <label for="chooseSecurePP" style="color: chocolate; padding-top: 5px" id="labelSecurePP">Видимость фото:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecurePP" aria-label="chooseSecurePP">            
            <option value="OPEN" selected>Открыто</option>            
            <option value="CONFIDENTIAL">Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureEE" style="color: chocolate; padding-top: 5px" id="labelSecureEE">Secure edit:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureEE" aria-label="chooseSecureEE">            
            <option value="OPEN" >Открыто</option>
            <option value="CONFIDENTIAL" >Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" selected>Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureMain" style="color: chocolate; padding-top: 5px" id="labelSecureMain">Secure edit main:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureMain" aria-label="chooseSecureMain">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureBirthday" style="color: chocolate; padding-top: 5px" id="labelSecureBirthday">Secure Birthday:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureBirthday" aria-label="chooseSecureBirthday">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" >Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE" selected>Семейное древо</option>
        </select>
        <label for="chooseSecureRM" style="color: chocolate; padding-top: 5px" id="labelSecureRM">Secure remove:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureRM" aria-label="chooseSecureRM">          
            
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureME" style="color: chocolate; padding-top: 5px" id="labelSecureME">Secure main email:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureME" aria-label="chooseSecureME">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
                <label for="chooseSecurePBur" style="color: chocolate; padding-top: 5px" id="labelSecurePBur">Secure place of burial:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecurePBur" aria-label="chooseSecurePBur">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecurePBirth" style="color: chocolate; padding-top: 5px" id="labelSecurePBirth">Secure place of birth:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecurePBirth" aria-label="chooseSecurePBirth">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureMP" style="color: chocolate; padding-top: 5px" id="labelSecureMP">Secure main phone:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px; text-align: center" id="chooseSecureMP" aria-label="chooseSecureMP">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureMA" style="color: chocolate; padding-top: 5px" id="labelSecureMA">Secure main address:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px; text-align: center" id="chooseSecureMA" aria-label="chooseSecureMA">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureBio" style="color: chocolate; padding-top: 5px" id="labelSecureBio">Secure biometric:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureBio" aria-label="chooseSecureBio">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
        <label for="chooseSecureDes" style="color: chocolate; padding-top: 5px" id="labelSecureDes">Secure description:</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecureDes" aria-label="chooseSecureDes">            
            <option value="OPEN">Открыто</option>
            <option value="CONFIDENTIAL" selected>Личное</option>
            <option value="ACTIVE_FAMILY" >Активная семья</option>
            <option value="LOGIC_PRIMARY_FAMILY" >Логическая по рождению</option>
            <option value="ANCESTOR" >Предки</option>   
            <option value="STRAIGHT_BLOOD">Прямое родство</option>
            <option value="PRIMARY_FAMILY" >Биологическая по рождению</option> 
            <option value="GENETIC_TREE">Семейное древо</option>
        </select>
                        </span>
                        <span class="col" style="width: 30%"></span>
                    </div>
                    <div style="margin-top: 15px">Внимание! Уровни конфиденциальности корректно</div> 
        <div>будут работать только при условии связанности или наличия  </div>
        <div>у данной персоны родственников-пользователей портала !</div>                    
                </div>
            </div>
        </div>
        <div class="accordion-item" id="biometricEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseBiometric" aria-expanded="false" aria-controls="collapseBiometric">
                    Биометрические данные
                </button>
            </h2>
            <div id="collapseBiometric" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 30%"></span>
                        <span class="col-6" style="min-width: 300px">
        <label for="ageFM" style="color: chocolate; padding-top: 5px">Возраст наблюдения</label>
        <input class="form-control" style="text-align: center" type="number" id="ageFM" name="ageFM" placeholder="введите число">                
        <label for="heightFM" style="color: chocolate; padding-top: 5px">Рост (см)</label>
        <input class="form-control" style="text-align: center" type="number" id="heightFM" name="heightFM" placeholder="введите число">
        <label for="weightFM" style="color: chocolate; padding-top: 5px">Вес (кг)</label>
        <input class="form-control" style="text-align: center" type="number" id="weightFM" name="weightFM" placeholder="введите число">
        <label for="footSizeFM" style="color: chocolate; padding-top: 5px">Размер стопы (см)</label>
        <input class="form-control" style="text-align: center" type="number" id="footSizeFM" name="footSizeFM" placeholder="введите число">
        <label for="shirtSizeFM" style="color: chocolate; padding-top: 5px">Размер рубашки:</label>
        <input class="form-control" style="text-align: center" type="number" id="shirtSizeFM" name="shirtSizeFM" placeholder="введите число">
        <label for="eyesColorFM" style="color: chocolate; padding-top: 5px">Цвет глаз:</label>
        <select class="form-select" style="text-align: center" id="eyesColorFM" aria-label="eyesColorFM" >
            <option value="UNKNOWN" selected>Неизвестный</option>        
            <option value="RED">Красный</option>
            <option value="BROWN" >Коричневый</option>
            <option value="BLACK">Черный</option>
            <option value="YELLOW">Желтый</option>
            <option value="BLUE">Голубой</option>
            <option value="GREEN">Зеленый</option>
        </select>
        <label for="hairColorFM" style="color: chocolate; padding-top: 5px">Цвет волос:</label>
        <select class="form-select" style="text-align: center" id="hairColorFM" aria-label="hairColorFM">
            <option value="UNKNOWN" selected>Неизвестный</option>               
            <option value="WHITE">Белый</option>
            <option value="BROWN" >Коричневый</option>
            <option value="BLACK">Черный</option>
            <option value="YELLOW">Желтый</option>
            <option value="RED">Рыжий</option>
        </select>
        <label for="descriptionFM" style="color: chocolate; padding-top: 5px">Возраст наблюдения</label>
        <input class="form-control" style="text-align: center" type="text" id="descriptionFM" name="descriptionFM" placeholder="введите описание">  
        <br>
        <div id="addAndRemoveBio">        
        <button class="btn btn-outline-success" type="button" style="margin-top: 5px" onclick="addBiometricField()">Дополнительный возраст</button>
        <button class="btn btn-outline-success" type="button" style="margin-top: 5px" onclick="removeBiometricField()">Очистить возраст</button>
         </div>        
         <div id="pagan"> 
         </div>  
                        </span>
                        <span class="col" style="width: 30%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="birthEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#birthAddressAddFM" aria-expanded="false" aria-controls="birthAddressAddFM">
                    Place of birth
                </button>
            </h2>
            <div id="birthAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
                        <div class="container-fluid row mh-100 no-gutters">
                                <span class="col" style="width: 20%"></span>
                                <span class="col-6" style="width: 250px">
                                    <div id="frontBirthPhoto" style="margin-bottom:2px; font-size: 18px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
                                </span>
                                <span class="col" style="width: 20%"></span>
                        </div>
                        
        <label id="labelBirthPhoto" for="BirthPhoto" style="padding-top:12px; color: chocolate;" >Файл фото</label>
        <input class="form-control" style="padding-top: 2px;padding-bottom: 12px" type="file" id="BirthPhoto" name="BirthPhoto" accept="image/*" onchange="resizeImagePlus('BirthPhoto')"> 
        <div id="canvasBlockBirthPhoto"  style=" align-content: center; align-items: center; text-align: center" hidden="hidden"> 
            <div style="padding-top:12px; color: chocolate;">Предварительный просмотр фото</div>
            <canvas style="width: 250px ;" id="canvasBirthPhoto"></canvas></div>

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
        <div class="accordion-item" id="burialEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#burialAddressAddFM" aria-expanded="false" aria-controls="burialAddressAddFM">
                    Place of burial
                </button>
            </h2>
            <div id="burialAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#accordionExample">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px">
                        <div class="container-fluid row mh-100 no-gutters">
                                <span class="col" style="width: 20%"></span>
                                <span class="col-6" style="width: 250px">
                                    <div id="frontBurialPhoto" style="margin-bottom:2px; font-size: 18px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
                                </span>
                                <span class="col" style="width: 20%"></span>
                        </div>
        <label id="labelBurialPhoto" for="BurialPhoto" style="padding-top:12px; color: chocolate;" >Файл фото</label>
        <input class="form-control" style="padding-top: 2px;padding-bottom: 12px" type="file" id="BurialPhoto" name="BurialPhoto" accept="image/*" onchange="resizeImagePlus('BurialPhoto')">   
        <div id="canvasBlockBurialPhoto"  style=" align-content: center; align-items: center; text-align: center" hidden="hidden"> <div style="padding-top:12px; color: chocolate;">Предварительный просмотр фото</div><canvas style="width: 250px ;" id="canvasBurialPhoto"></canvas></div>

        <label for="burialCountryAddFM" style="color: chocolate">Страна:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialCountryAddFM" name="burialCountryAddFM" >
        <label for="burialRegionAddFM" style="color: chocolate; padding-top: 2px">Регион / область:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialRegionAddFM" name="burialRegionAddFM" >
        <label for="burialCityAddFM" style="color: chocolate; padding-top: 2px">Город / Населенный пункт:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialCityAddFM" name="burialCityAddFM" >
        <label for="burialCemeteryAddFM" style="color: chocolate; padding-top: 2px">Кладбище:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialCemeteryAddFM" name="burialCemeteryAddFM" >  
        <label for="burialStreetAddFM" style="color: chocolate; padding-top: 5px">Улица / Проспект / Аллея:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialStreetAddFM" name="burialStreetAddFM" >        
        <label for="burialHouseAddFM" style="color: chocolate; padding-top: 2px">Массив / Раздел:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialHouseAddFM" name="burialHouseAddFM" >
        <label for="burialBuildingAddFM" style="color: chocolate; padding-top: 2px">Номер участка:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialBuildingAddFM" name="burialBuildingAddFM" >
        <label for="burialFlatAddFM" style="color: chocolate; padding-top: 2px">Номер могилы:</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialFlatAddFM" name="burialFlatAddFM" >
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <br>
    <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitBaseFormEditFM()">Edit Person</button>
    <br>  
        <span style="font-size: larger; font-family: 'Times New Roman',serif; text-align: left">Common Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultListCreateFM"></span></span>
            <br>  
        <span style="font-size: larger; font-family: 'Times New Roman',serif; text-align: left">Prime Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSavePrimePhoto"></span></span>
                    <br>  
        <span style="font-size: larger; font-family: 'Times New Roman',serif; text-align: left">Birth Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveBirthPhoto"></span></span>
                    <br>  
        <span style="font-size: larger; font-family: 'Times New Roman',serif; text-align: left">Burial Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveBurialPhoto"></span></span>
    <br>
</form>
 `
    if (tempPerson.secretLevelPhoto !== "CLOSE") {
        let tempURL = URL.createObjectURL(tempPerson.primePhotoImj)

        document.getElementById("frontPhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${tempURL}" class="card-img" alt="loading...">
                        <div class="card-img-overlay">
                            <div class="card-text" style="padding-top: 35%">${tempTextPhoto}</div>
                        </div>
                    </div>`

        URL.revokeObjectURL(tempPerson.primePhotoImj);
    } else document.getElementById("frontPhoto").hidden = true;
    if (tempPerson.birthday !== null) document.getElementById("birthdayAddFM").value = tempPerson.birthday;
    document.getElementById("chooseSexAddFM").value = tempPerson.sex;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.firstName !== null) document.getElementById("firstNameMotherAddFM").value = tempPerson.motherFio.firstName;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.middleName !== null) document.getElementById("middleNameMotherAddFM").value = tempPerson.motherFio.middleName;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.lastName !== null) document.getElementById("lastNameMotherAddFM").value = tempPerson.motherFio.lastName;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.birthday !== null) document.getElementById("birthdayMotherAddFM").value = tempPerson.motherFio.birthday;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.firstName !== null) document.getElementById("firstNameFatherAddFM").value = tempPerson.fatherFio.firstName;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.middleName !== null) document.getElementById("middleNameFatherAddFM").value = tempPerson.fatherFio.middleName;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.lastName !== null) document.getElementById("lastNameFatherAddFM").value = tempPerson.fatherFio.lastName;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.birthday !== null) document.getElementById("birthdayFatherAddFM").value = tempPerson.fatherFio.birthday;
    if (tempPerson.deathday !== null) document.getElementById("deathdayAddFM").value = tempPerson.deathday
    if (tempPerson.secretLevelMainInfo === "CLOSE") {
        document.getElementById("firstNameAddFM").disabled = "disabled";
        document.getElementById("middleNameAddFM").disabled = "disabled";
        document.getElementById("lastNameAddFM").disabled = "disabled";
        document.getElementById("birthdayAddFM").disabled = "disabled";
        document.getElementById("deathdayAddFM").disabled = "disabled";
        document.getElementById("chooseSexAddFM").disabled = "disabled";
        lockMother();
        lockFather();
    } else {
        document.getElementById("otherNamesEdit").hidden = false;
        document.getElementById("securitySecurity").hidden = false;
        if (tempPerson.secretLevelMainInfo !== "CLOSE")
            document.getElementById("chooseSecureMain").value = tempPerson.secretLevelMainInfo;
        if (tempPerson.secretLevelEdit !== "CLOSE")
            document.getElementById("chooseSecureEE").value = tempPerson.secretLevelEdit;
        if (tempPerson.secretLevelBirthday !== "CLOSE") {
            document.getElementById("chooseSecureBirthday").value = tempPerson.secretLevelBirthday;
        } else {
            document.getElementById("chooseSecureBirthday").hidden = true;
            document.getElementById("labelSecureBirthday").hidden = true;
        }

        if (tempPerson.secretLevelPhoto !== "CLOSE") {
            document.getElementById("labelPrimePhoto").hidden = false;
            document.getElementById("PrimePhoto").hidden = false;
            document.getElementById("chooseSecurePP").value = tempPerson.secretLevelPhoto;
        } else {
            document.getElementById("chooseSecurePP").hidden = true;
            document.getElementById("labelSecurePP").hidden = true;
        }
        if (tempPerson.secretLevelRemove !== "CLOSE") {
            document.getElementById("chooseSecureRM").value = tempPerson.secretLevelRemove;
        } else {
            document.getElementById("chooseSecureRM").hidden = true;
            document.getElementById("labelSecureRM").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelPhone !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelPhone !== "UNDEFINED")
                document.getElementById("chooseSecureMP").value = tempPerson.memberInfo.secretLevelPhone;
        } else {
            document.getElementById("chooseSecureMP").hidden = true;
            document.getElementById("labelSecureMP").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelEmail !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelEmail !== "UNDEFINED")
                document.getElementById("chooseSecureME").value = tempPerson.memberInfo.secretLevelEmail;
        } else {
            document.getElementById("chooseSecureME").hidden = true;
            document.getElementById("labelSecureME").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelAddress !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelAddress !== "UNDEFINED")
                document.getElementById("chooseSecureMA").value = tempPerson.memberInfo.secretLevelAddress;
        } else {
            document.getElementById("chooseSecureMA").hidden = true;
            document.getElementById("labelSecureMA").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelBiometric !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelBiometric !== "UNDEFINED")
                document.getElementById("chooseSecureBio").value = tempPerson.memberInfo.secretLevelBiometric;
        } else {
            document.getElementById("chooseSecureBio").hidden = true;
            document.getElementById("labelSecureBio").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelBurial !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelBurial !== "UNDEFINED")
                document.getElementById("chooseSecurePBur").value = tempPerson.memberInfo.secretLevelBurial;
        } else {
            document.getElementById("chooseSecurePBur").hidden = true;
            document.getElementById("labelSecurePBur").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelDescription !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelDescription !== "UNDEFINED")
                document.getElementById("chooseSecureDes").value = tempPerson.memberInfo.secretLevelDescription;
        } else {
            document.getElementById("chooseSecureDes").hidden = true;
            document.getElementById("labelSecureDes").hidden = true;
        }
        let linkingMother =tempPerson.motherInfo !== infoAbsent && tempPerson.motherInfo.charAt(0) !== '(' && tempPerson.motherInfo !== infoUncorrected;
        let linkingFather=tempPerson.fatherInfo !== infoAbsent && tempPerson.fatherInfo.charAt(0) !== '(' && tempPerson.fatherInfo !== infoUncorrected;
        if (linkingMother&&tempPerson.secretLevelRemove==="CLOSE") {
            lockMother();
            document.getElementById("lockParentsInfo").innerHTML = `
            Родителя - персону в базе Вам<strong>нельзя</strong> отсюда изменить (его поля затенены): <br> Если хотите изменить данные родителя используйте редактирование его записи. <br> Если хотите разорвать связь - используйте Remove`
        }else if (linkingMother&&!linkingFather&&tempPerson.secretLevelRemove!=="CLOSE"){
            document.getElementById("lockParentsInfo").innerHTML = `
            Мать - персона в базе. Ее данные <strong>нельзя</strong> отсюда изменить: <br> Если хотите изменить данные этого родителя используйте редактирование его записи. <br> Изменение данных здесь приведет к разрыву связи и попытке установления новой.`

        }
        if (linkingFather&&tempPerson.secretLevelRemove==="CLOSE") {
            lockFather();
            document.getElementById("lockParentsInfo").innerHTML = `
            Родителя - персону в базе Вам<strong>нельзя</strong> отсюда изменить (его поля затенены): <br> Если хотите изменить данные родителя используйте редактирование его записи. <br> Если хотите разорвать связь - используйте Remove`
        }else if (linkingFather&&!linkingMother&&tempPerson.secretLevelRemove!=="CLOSE") {
            document.getElementById("lockParentsInfo").innerHTML = `
            Отец - персона в базе. Его данные <strong>нельзя</strong> отсюда изменить: <br> Если хотите изменить данные этого родителя используйте редактирование его записи. <br> Изменение данных здесь приведет к разрыву связи и попытке установления новой.`
        }
        if (linkingFather&&linkingMother&&tempPerson.secretLevelRemove!=="CLOSE"){
            document.getElementById("lockParentsInfo").innerHTML = `
            Оба родителя - персоны в базе. Их данные <strong>нельзя</strong> отсюда изменить: <br> Если хотите изменить данные родителей используйте редактирование их записи. <br> Изменение данных здесь приведет к разрыву связей и попытке установления новых.`
        }

            if (tempPerson.fioDtos !== null) {
            document.getElementById(`firstNameOtherAddFM0`).value = tempPerson.fioDtos[0].firstName;
            document.getElementById(`middleNameOtherAddFM0`).value = tempPerson.fioDtos[0].middleName;
            document.getElementById(`lastNameOtherAddFM0`).value = tempPerson.fioDtos[0].lastName;
            document.getElementById(`firstNameOtherAddFM0`).disabled = "disabled";
            document.getElementById(`middleNameOtherAddFM0`).disabled = "disabled";
            document.getElementById(`lastNameOtherAddFM0`).disabled = "disabled";
            insertOther[0]='no';
            document.getElementById("otherNamesInfo").innerHTML = `
            Альтернативное имя <strong>нельзя</strong> изменить/удалить напрямую(его поля затенены): <br> Для изменения - введите новое альтернативное имя, а для неправильной записи используйте <em>Remove</em><br> Кроме того, Вы можете поменять <strong>любое</strong> альтернативное имя с основным местами <br><br>`
            while (countOtherNames < tempPerson.fioDtos.length) {
                addOtherFieldEdit();
                countOtherNames--;
                document.getElementById(`deleteOtherAddFM${countOtherNames}`).hidden = true;
                document.getElementById(`firstNameOtherAddFM${countOtherNames}`).value = tempPerson.fioDtos[countOtherNames].firstName;
                document.getElementById(`middleNameOtherAddFM${countOtherNames}`).value = tempPerson.fioDtos[countOtherNames].middleName;
                document.getElementById(`lastNameOtherAddFM${countOtherNames}`).value = tempPerson.fioDtos[countOtherNames].lastName;
                document.getElementById(`firstNameOtherAddFM${countOtherNames}`).disabled = "disabled";
                document.getElementById(`middleNameOtherAddFM${countOtherNames}`).disabled = "disabled";
                document.getElementById(`lastNameOtherAddFM${countOtherNames}`).disabled = "disabled";
                insertOther[countOtherNames]='no';
                countOtherNames++;
            }
        }
    }
    if (tempPerson.memberInfo.secretLevelPhone !== "CLOSE" || tempPerson.memberInfo.secretLevelEmail !== "CLOSE") {
        document.getElementById("contactPage").hidden = false;
        document.getElementById("contactPage").disabled = false;

        if (tempPerson.memberInfo.secretLevelEmail !== "CLOSE") {
            document.getElementById("mainEmailAddFM").disabled = false;
            document.getElementById("addEmailButtom").hidden = false;
            if (tempPerson.memberInfo.mainEmail !== null && tempPerson.memberInfo.mainEmail !== infoAbsent)
                document.getElementById("mainEmailAddFM").value = tempPerson.memberInfo.mainEmail;
            if (tempPerson.memberInfo.emails !== null)
                while (countEmail < tempPerson.memberInfo.emails.length) {
                    if (tempPerson.memberInfo.emails[countEmail].internName !== tempPerson.memberInfo.mainEmail) {
                        addEmailsFieldEdit();
                        countEmail--;
                        document.getElementById(`emailAddFM${countEmail}`).value = tempPerson.memberInfo.emails[countEmail].internName
                    }
                    countEmail++;
                }
        } else {
            document.getElementById("mainEmailAddFM").value = infoClosed;
        }
        if (tempPerson.memberInfo.secretLevelPhone !== "CLOSE") {
            document.getElementById("mainPhoneAddFM").disabled = false;
            document.getElementById("addPhoneButtom").hidden = false;
            if (tempPerson.memberInfo.mainPhone !== null && tempPerson.memberInfo.mainPhone !== infoAbsent)
                document.getElementById("mainPhoneAddFM").value = tempPerson.memberInfo.mainPhone;
            if (tempPerson.memberInfo.phones !== null)
                while (countPhone < tempPerson.memberInfo.phones.length) {
                    if (tempPerson.memberInfo.phones[countPhone].internName !== tempPerson.memberInfo.mainPhone) {
                        addPhonesFieldEdit();
                        countPhone--;
                        document.getElementById(`phoneAddFM${countPhone}`).value = tempPerson.memberInfo.phones[countPhone].internName;
                    }
                    countPhone++;
                }
        } else {
            document.getElementById("mainPhoneAddFM").value = infoClosed;
        }
    }
    if (tempPerson.memberInfo.secretLevelAddress !== "CLOSE") {
        document.getElementById("addressEdit").hidden = false;
        if (tempPerson.memberInfo.addresses !== null) {
            document.getElementById("indexAddFM").value = tempPerson.memberInfo.addresses[0].index;
            document.getElementById("countryAddFM").value = tempPerson.memberInfo.addresses[0].country;
            document.getElementById("regionAddFM").value = tempPerson.memberInfo.addresses[0].region;
            document.getElementById("cityAddFM").value = tempPerson.memberInfo.addresses[0].city;
            document.getElementById("streetAddFM").value = tempPerson.memberInfo.addresses[0].street;
            document.getElementById("houseAddFM").value = tempPerson.memberInfo.addresses[0].house;
            document.getElementById("buildingAddFM").value = tempPerson.memberInfo.addresses[0].building;
            document.getElementById("flatAddFM").value = tempPerson.memberInfo.addresses[0].flatNumber;
        }
    }
    if (tempPerson.memberInfo.secretLevelBiometric !== "CLOSE") {
        document.getElementById("biometricEdit").hidden = false;
        if (tempPerson.memberInfo.biometric !== null) {
            document.getElementById("ageFM").value = tempPerson.memberInfo.biometric[0].age;
            document.getElementById("ageFM").disabled=true;
            document.getElementById("heightFM").value = tempPerson.memberInfo.biometric[0].height;
            document.getElementById("weightFM").value = tempPerson.memberInfo.biometric[0].weight;
            document.getElementById("footSizeFM").value = tempPerson.memberInfo.biometric[0].footSize;
            document.getElementById("shirtSizeFM").value = tempPerson.memberInfo.biometric[0].shirtSize;
            document.getElementById("eyesColorFM").value = (tempPerson.memberInfo.biometric[0].eyesColor === null) ? "UNKNOWN" : tempPerson.memberInfo.biometric[0].eyesColor;
            document.getElementById("hairColorFM").value = (tempPerson.memberInfo.biometric[0].hairColor === null) ? "UNKNOWN" : tempPerson.memberInfo.biometric[0].hairColor;
            document.getElementById("descriptionFM").value = tempPerson.memberInfo.biometric[0].description;
        }
        if (countBio>0) createCurrentPagination(0);

    }
    if (tempPerson.memberInfo.secretLevelBurial !== "CLOSE") {
        document.getElementById("burialEdit").hidden = false;
        if (tempPerson.memberInfo.photoBurialExist !== false) {
            let tempURL = URL.createObjectURL(tempPerson.memberInfo.burialImj)

            document.getElementById("frontBurialPhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${tempURL}" class="card-img" alt="loading...">                        
                    </div>`

            URL.revokeObjectURL(tempPerson.memberInfo.burialImj);
        } else document.getElementById("frontBurialPhoto").hidden = true;
        if (tempPerson.memberInfo.burial !== null) {
            document.getElementById("burialCountryAddFM").value = tempPerson.memberInfo.burial.country;
            document.getElementById("burialRegionAddFM").value = tempPerson.memberInfo.burial.region;
            document.getElementById("burialCityAddFM").value = tempPerson.memberInfo.burial.city;
            document.getElementById("burialCemeteryAddFM").value = tempPerson.memberInfo.burial.cemetery;
            document.getElementById("burialStreetAddFM").value = tempPerson.memberInfo.burial.street;
            document.getElementById("burialHouseAddFM").value = tempPerson.memberInfo.burial.chapter;
            document.getElementById("burialBuildingAddFM").value = tempPerson.memberInfo.burial.square;
            document.getElementById("burialFlatAddFM").value = tempPerson.memberInfo.burial.grave;
        }
    }
    if (tempPerson.memberInfo.secretLevelBirth !== "CLOSE") {
        document.getElementById("birthEdit").hidden = false;
        if (tempPerson.memberInfo.photoBirthExist !== false) {
            let tempURL = URL.createObjectURL(tempPerson.memberInfo.birthImj)

            document.getElementById("frontBirthPhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${tempURL}" class="card-img" alt="loading...">                        
                    </div>`
            URL.revokeObjectURL(tempPerson.memberInfo.birthImj);
        } else document.getElementById("frontBirthPhoto").hidden = true;
        if (tempPerson.memberInfo.birth !== null) {
            document.getElementById("birthCountryAddFM").value = tempPerson.memberInfo.birth.country;
            document.getElementById("birthRegionAddFM").value = tempPerson.memberInfo.birth.region;
            document.getElementById("birthCityAddFM").value = tempPerson.memberInfo.birth.city;
            document.getElementById("birthStreetAddFM").value = tempPerson.memberInfo.birth.street;
            document.getElementById("birthHouseAddFM").value = tempPerson.memberInfo.birth.birthHouse;
            document.getElementById("birthRegisterAddFM").value = tempPerson.memberInfo.birth.registration;
        }
    }
}
function lockMother() {
    document.getElementById("firstNameMotherAddFM").disabled = "disabled";
    document.getElementById("middleNameMotherAddFM").disabled = "disabled";
    document.getElementById("lastNameMotherAddFM").disabled = "disabled";
    document.getElementById("birthdayMotherAddFM").disabled = "disabled";
}
function lockFather() {
    document.getElementById("firstNameFatherAddFM").disabled = "disabled";
    document.getElementById("middleNameFatherAddFM").disabled = "disabled";
    document.getElementById("lastNameFatherAddFM").disabled = "disabled";
    document.getElementById("birthdayFatherAddFM").disabled = "disabled";
}
function changeWithMain(otherField){
    let first = document.getElementById("firstNameAddFM").value;
    let middle = document.getElementById("middleNameAddFM").value;
    let last = document.getElementById("lastNameAddFM").value;
    if (document.getElementById(`firstNameOtherAddFM${otherField}`).value.trim().length===0
        ||document.getElementById(`middleNameOtherAddFM${otherField}`).value.trim().length===0
        ||document.getElementById(`lastNameOtherAddFM${otherField}`).value.trim().length===0)  return;
    document.getElementById("firstNameAddFM").value = document.getElementById(`firstNameOtherAddFM${otherField}`).value;
    document.getElementById("middleNameAddFM").value = document.getElementById(`middleNameOtherAddFM${otherField}`).value;
    document.getElementById("lastNameAddFM").value = document.getElementById(`lastNameOtherAddFM${otherField}`).value;
    document.getElementById(`firstNameOtherAddFM${otherField}`).value = first;
    document.getElementById(`middleNameOtherAddFM${otherField}`).value = middle;
    document.getElementById(`lastNameOtherAddFM${otherField}`).value = last;

    if (!clickOther) {
        document.getElementById("firstNameAddFM").disabled = true;
        document.getElementById("middleNameAddFM").disabled = true;
        document.getElementById("lastNameAddFM").disabled = true;
        document.getElementById(`firstNameOtherAddFM${otherField}`).disabled = true;
        document.getElementById(`middleNameOtherAddFM${otherField}`).disabled = true;
        document.getElementById(`lastNameOtherAddFM${otherField}`).disabled = true;
        if (document.getElementById(`deleteOther${otherField}`)!==undefined&&insertOther[otherField]==='yes') document.getElementById(`deleteOther${otherField}`).disabled = true;
        insertOther[otherField]='yes';
        for (let i = 0; i < countOtherNames; i++) {
            if (i!==otherField && document.getElementById(`changeWithMain${i}`)!==undefined) document.getElementById(`changeWithMain${i}`).disabled = true;
        }
    }else {
        document.getElementById("firstNameAddFM").disabled = false;
        document.getElementById("middleNameAddFM").disabled = false;
        document.getElementById("lastNameAddFM").disabled = false;
        if (tempPerson.fioDtos.length > otherField) insertOther[otherField]='no';
        else {insertOther[otherField]='yes';
            if (document.getElementById(`deleteOther${otherField}`) !== undefined
                && insertOther[otherField]==='yes') document.getElementById(`deleteOther${otherField}`).disabled = false;
            document.getElementById(`firstNameOtherAddFM${otherField}`).disabled = false;
            document.getElementById(`middleNameOtherAddFM${otherField}`).disabled = false;
            document.getElementById(`lastNameOtherAddFM${otherField}`).disabled = false;}
        for (let i = 0; i < countOtherNames; i++) {
            if (document.getElementById(`changeWithMain${i}`) !== undefined) document.getElementById(`changeWithMain${i}`).disabled = false;
        }
    }
    clickOther=!clickOther;
}
function addPhonesFieldEdit() {
    if (countPhone < 4) {
        document.getElementById("deleteButtonsPhoneAddFM").innerHTML += `
        <div id="leftPhoneAddFM${countPhone}">            
            <br> 
            <button class="btn btn-outline-danger" style="padding: 1px 4px; margin-top: 5px" onclick="deletePhoneFieldEdit(${countPhone})">X</button>  
        </div>`
        document.getElementById("addedPhoneAddFM" + countPhone).innerHTML = ` 
        <span id="centrePhoneAddFM${countPhone}">
            <label for="phoneAddFM${countPhone}" style="color: black; padding-top: 5px">Additional phone:</label>
            <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="phoneAddFM${countPhone}" name="phoneAddFM${countPhone}" autoComplete="off">
        </span>`
        document.getElementById("addedButtonsPhoneAddFM").innerHTML += `
        <span id="rightPhoneAddFM${countPhone}">
            <br>
            <div style="padding-bottom:1px; padding-top:1px; margin-top: 7px"><br></div>
        </span>`
        insertPhone[countPhone] = 'yes'
        countPhone++;
        document.getElementById("tryPhone").innerHTML = (4 - countPhone) + ``;
    }
}
function addEmailsFieldEdit() {
    if (countEmail < 4) {
        document.getElementById("deleteButtonsEmailAddFM").innerHTML += `          
        <div id="leftEmailAddFM${countEmail}">            
            <br> 
            <button class="btn btn-outline-danger" style="padding: 1px 4px; margin-top: 5px" onclick="deleteEmailFieldEdit(${countEmail})">X</button>  
        </div>`
        document.getElementById("addedEmailAddFM" + countEmail).innerHTML = `
        <span id="centerEmailAddFM${countEmail}">
            <label for="emailAddFM${countEmail}" style="color: black; padding-top: 5px" >Additional Email:</label>
            <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="emailAddFM${countEmail}" name="emailAddFM${countEmail}" autoComplete="off">
        </span>`
        document.getElementById("addedButtonsEmailAddFM").innerHTML += `
        <span id="rightEmailAddFM${countEmail}">
            <br>
            <div style="padding-bottom:1px; padding-top:1px; margin-top: 7px"><br></div>
        </span>`
        insertEmail[countEmail] = 'yes'
        countEmail++;
        document.getElementById("tryEmail").innerHTML = (4 - countEmail) + ``;
    }
}
function deletePhoneFieldEdit(phoneString) {
    document.getElementById("leftPhoneAddFM" + phoneString).innerHTML = ``
    document.getElementById("centrePhoneAddFM" + phoneString).innerHTML = ``
    document.getElementById("rightPhoneAddFM" + phoneString).innerHTML = ``
    insertPhone[phoneString] = null;
}
function deleteEmailFieldEdit(emailString) {
    document.getElementById("leftEmailAddFM" + emailString).innerHTML = ``
    document.getElementById("centerEmailAddFM" + emailString).innerHTML = ``
    document.getElementById("rightEmailAddFM" + emailString).innerHTML = ``
    insertEmail[emailString] = null;
}
function addOtherFieldEdit() {
    if (countOtherNames <= 4) {
        document.getElementById("otherAddFM" + countOtherNames).innerHTML = `
        <div id="deleteOtherAddFM${countOtherNames}" class="col-1" style="width: 8%">
                <button  id="deleteOther${countOtherNames}" class="btn btn-outline-danger" style="padding:5px; font-size:14px; margin-top: 14px; margin-bottom: 10px" onclick="deleteOtherFieldEdit(${countOtherNames})">X</button>
        </div>
        <div class="col-3" >
            <label for="firstNameOtherAddFM${countOtherNames}" >FirstName:</label>
            <input class="form-control form-control-border-color--warning" style="padding:3px; font-size:12px" type="text" id="firstNameOtherAddFM${countOtherNames}" name="firstNameOtherAddFM${countOtherNames}" autocomplete="off" ></div>
        <div class="col-3" >
            <label for="middleNameOtherAddFM${countOtherNames}" >MiddleName:</label>
            <input class="form-control" style="padding:3px; font-size:12px" type="text" id="middleNameOtherAddFM${countOtherNames}" name="middleNameOtherAddFM${countOtherNames}" autocomplete="off" >
        </div>
        <div class="col" >
            <label for="lastNameOtherAddFM${countOtherNames}" >LastName:</label>
            <input class="form-control" style="padding:3px; font-size:12px" type="text" id="lastNameOtherAddFM${countOtherNames}" name="lastNameOtherAddFM${countOtherNames}" autocomplete="off">
        </div>
        <div class="col-1" >
        
            <button class="btn btn-outline-warning" type="button" style="padding:5px; font-size:14px; margin-top: 14px; margin-bottom: 10px" id="changeWithMain${countOtherNames}" onclick="changeWithMain(${countOtherNames})">&#8693;</button>  
        </div>
        `;
        insertOther[countOtherNames] = 'yes';
        if (clickOther) document.getElementById(`changeWithMain${countOtherNames}`).disabled = true;
        countOtherNames++;

        document.getElementById("tryOther").innerHTML = (5 - countOtherNames) + ``;
    }
}
function deleteOtherFieldEdit(otherNamesString) {
    document.getElementById("otherAddFM" + otherNamesString).innerHTML = ``;
    insertOther[otherNamesString] = 'no';
}
function submitBaseFormEditFM() {
    dropPhotosToServer();
    let other= markOtherToDrop();
    let otherPhones;
    let otherEmails;
    (tempPerson.memberInfo!==null&&tempPerson.memberInfo.secretLevelEmail==="CLOSE")?otherEmails=null:otherEmails=markEmailToDrop();
    (tempPerson.memberInfo!==null&&tempPerson.memberInfo.secretLevelPhone==="CLOSE")?otherPhones=null:otherPhones=markPhoneToDrop();
    if (inputBio!==null&&tempPerson.memberInfo!==null&&tempPerson.memberInfo.secretLevelBiometric !== "CLOSE") createAgeBio(activeBio);
    const jsonData = getFormToDrop(other,otherPhones,otherEmails);
console.log(jsonData);
    fetch("/base/family_member/edit", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateFM").innerHTML = await status.text();
        tempPerson=``;
        tempSecurity=``;
        loadBio=0;
        countBio=0;
    });
}