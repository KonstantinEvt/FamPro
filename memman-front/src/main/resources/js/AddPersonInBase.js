function menuFunc2100() {
    clearMainVariable();
    tempPerson = ``;
    tempSecurity = ``;
    loadBio = 0;
    countBio = 0;
    let background=URL.createObjectURL(defaultPhotos.groundTrees);
    let noPhoto=URL.createObjectURL(defaultPhotos.borderImage);
    document.getElementById("mainPanel").innerHTML = `<div style="vertical-align: top; "><div class="main_title" id="addPerson-title">${globalTexts.get(2101)}</div>
<form class="form-group" style="margin:5px; text-align: center; background: url(${background}) repeat" id="baseFormAddFM">
        <div class="accordion" style=" --bs-accordion-active-bg: #eaecbd;  --bs-accordion-btn-focus-box-shadow: 0 0 0 0.25rem rgb(234 212 101 / 25%)" id="addPersonAccord" >
        <div class="accordion-item">
            <h2 class="accordion-header">
               <button class="accordion-button" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#mainInfoNote" aria-expanded="true" aria-controls="collapseOne">
                ${globalTexts.get(2102)}
                </button>
            </h2>
            <div id="mainInfoNote" class="accordion-collapse collapse show" data-bs-parent="#addPersonAccord">
                <div class="accordion-body" style="background: rgb(255,255,255,30)">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px;margin-left:15px">
        <label for="firstNameAddFM" style="color: chocolate">${langMatrix.get("name")}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="firstNameAddFM" name="firstNameAddFM" required>
        <label for="middleNameAddFM" style="color: chocolate; padding-top: 4px">${langMatrix.get("patronymic")}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="middleNameAddFM" name="middleNameAddFM" required>
        <label for="lastNameAddFM" style="color: chocolate; padding-top: 4px">${langMatrix.get("surname")}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="lastNameAddFM" name="lastNameAddFM" required>
        <label for="birthdayAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2103)}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="date" id="birthdayAddFM" name="birthdayAddFM" required>
        <label for="deathdayAddFM" style="color: chocolate; padding-top: 4px">${globalTexts.get(2104)}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="date" id="deathdayAddFM" name="deathdayAddFM">
        <label for="chooseSexAddFM" style="color: chocolate; padding-top: 4px">${langMatrix.get("gender")}</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;" id="chooseSexAddFM" aria-label="chooseSexAddFM">            
            <option value="MALE" selected>${langMatrix.get("male")}</option>
            <option value="FEMALE">${langMatrix.get("female")}</option>
        </select>
        <div class="card" id="primeCard" style="max-height:700px; align-items:center; border:0; margin-top: 20px">
                     <div class="container-fluid row">
                     <span class="col-10" style="text-align: center;padding: 0 0 0 18%" >
                        <label for="prime" id="label_prime" style="display:inline-block; border: 1px solid #dee2e6; border-radius: 4px; cursor: pointer; background-color: RGB(255,255,255,90%);padding:4px; color: grey;" >${globalTexts.get(6)}</label>
                        <input class="form-control" type="file" id="prime" name="prime" accept="image/*" style="display:none" onchange="selectInputImage('prime', 600,600)" >
                    </span>
                    <span class="col" style="text-align: left; align-content: center">
                        <button type="button" class="btn btn-outline-danger" style="padding: 3px" onclick="removeEntered('prime')">X</button>
                    </span>
                    </div>
                    <div id="imageDefault_prime" style="align-content: center; align-items: center; text-align: center; min-height:200px; min-width: 300px">
                    <img style="max-width: 95% ;max-height:40vh;" id="pictureDef_prime" src=${noPhoto} class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="align-content:center;margin-top:40px;">
                        <div class="card-text" style="margin-top:-30px;">${globalTexts.get(3)}</div>
                    </div>                   
                    </div>
                    <div class="card-body" style="text-align: center; padding: 0">                        
                        <div id="canvasBlock_prime" hidden="hidden" style="align-content: center; align-items: center; text-align: center; min-height:300px; min-width: 300px" > 
                        <div style="padding-top:5px; color: chocolate;">${globalTexts.get(4)}</div>
                        <canvas  style="max-width: 95% ;max-height:40vh;" id="canvas_prime"></canvas>
                        <div id="canvasBlock_little" hidden="hidden"><canvas id="canvas_little"></canvas></div>
                        </div>
                    </div>                  
        </div>
      <div style="margin-top: 15px; font-weight: bold">${langMatrix.get("attention")}! <span style="font-style:italic;font-weight: normal; font-size:14px">${globalTexts.get(2105)}</span></div>
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseParents" aria-expanded="false" aria-controls="collapseParents">
                    ${globalTexts.get(2106)}
                </button>
            </h2>
            <div id="collapseParents" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 48%;margin-left:4%">
        <div style="color: darkred; padding-top: 5px; padding-bottom: 10px">${langMatrix.get("mother")}</div>                        
        <label for="firstNameMotherAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("name")}</label>
        <input class="form-control" type="text" id="firstNameMotherAddFM" name="firstNameMotherAddFM">
        <label for="middleNameMotherAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("patronymic")}</label>
        <input class="form-control" type="text" id="middleNameMotherAddFM" name="middleMotherAddFM" >
        <label for="lastNameMotherAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("surname")}</label>
        <input class="form-control" type="text" id="lastNameMotherAddFM" name="lastNameMotherAddFM">
        <label for="birthdayMotherAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2103)}</label>
        <input class="form-control" type="date" id="birthdayMotherAddFM" name="birthdayMotherAddFM">
                        </span>
                        <span class="col" style="width: 48%">
        <div style="color: darkred; padding-top: 5px; padding-bottom: 10px">${langMatrix.get("father")}</div>                        
        <label for="firstNameFatherAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("name")}</label>
        <input class="form-control" type="text" id="firstNameFatherAddFM" name="firstNameFatherAddFM">
        <label for="middleNameFatherAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("patronymic")}</label>
        <input class="form-control" type="text" id="middleNameFatherAddFM" name="middleFatherAddFM">
        <label for="lastNameFatherAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("surname")}</label>
        <input class="form-control" type="text" id="lastNameFatherAddFM" name="lastNameFatherAddFM">
        <label for="birthdayFatherAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2103)}</label>
        <input class="form-control" type="date" id="birthdayFatherAddFM" name="birthdayFatherAddFM">
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item" >
            <h2 class="accordion-header">
                <button class="accordion-button collapsed"  style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOtherName" aria-expanded="false" aria-controls="collapseOtherName">
                    ${globalTexts.get(2107)}
                </button>
            </h2>
            <div id="collapseOtherName" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body" >
                    <div id="otherAddFM0" class="container-fluid row mh-100 no-gutters" style="font-size:14px; color: chocolate; padding: 0; margin-top: 5px">
                        <div class="col-3" style="padding:0 5px 0 5px">
        <label for="firstNameOtherAddFM0">${langMatrix.get("name")}</label>
        <input class="form-control form-control-border-color--warning" style="font-size:14px;padding: 3px" type="text" id="firstNameOtherAddFM0" name="firstNameOtherAddFM0" autocomplete="on" >
                        
                        </div>
                        <div class="col-3" style="padding:0 5px 0 5px">
        <label for="middleNameOtherAddFM0" >${langMatrix.get("patronymic")}</label>
        <input class="form-control" type="text" style="font-size:14px;padding: 3px" id="middleNameOtherAddFM0" name="middleNameOtherAddFM0" >
                        </div>
                         <div class="col" style="padding:0 5px 0 5px">
        <label for="lastNameOtherAddFM0">${langMatrix.get("surname")}</label>
        <input class="form-control" type="text" style="font-size:14px;padding: 3px" id="lastNameOtherAddFM0" name="lastNameOtherAddFM0" >
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
                    <div>${globalTexts.get(2108)}</div>
                    <div style="color: chocolate">${globalTexts.get(2109)}: <span id="tryOther" style="color: black;font-weight: bold">4</span></div>                    
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                    ${globalTexts.get(2110)}
                </button>
            </h2>
            <div id="collapseTwo" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body" style="padding-right:5px">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="text-align: center; padding:0 5px 0 5px; width: 20%"> 
                            <div id="deleteButtonsEmailAddFM" style="margin-bottom: 45px; margin-top: 45px;"><br>
                            </div>
                            <div id="deleteButtonsPhoneAddFM" style=" margin-top: 0"><br>
                            </div>  
                        </span>
                        <span class="col-8" style="min-width: 200px;padding:0">
        <label for="mainEmailAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("main")} ${langMatrix.get("email").toLowerCase()}</label>
        <input class="form-control" type="text" id="mainEmailAddFM" name="mainEmailAddFM" autocomplete="off" >
        <div id="addedEmailAddFM0"></div>
        <div id="addedEmailAddFM1"></div>
        <div id="addedEmailAddFM2"></div>
        <div id="addedEmailAddFM3"></div>
        <div id="addedEmailAddFM4"></div>
        
        <label for="mainPhoneAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("main")} ${langMatrix.get("phone").toLowerCase()}</label>
        <input class="form-control" type="text" id="mainPhoneAddFM" name="mainPhoneAddFM" autocomplete="off">
        <div id="addedPhoneAddFM0" ></div>
        <div id="addedPhoneAddFM1" ></div>
        <div id="addedPhoneAddFM2" ></div>
        <div id="addedPhoneAddFM3" ></div>
        <div id="addedPhoneAddFM4" ></div>
        
                        </span>
                        <span id="addButtons" class="col" style="text-align: center; width: 20%; padding:0">                        
                        <br> 
                        <button class="btn btn-outline-success" type="button" style="margin-top: 5px" onclick="addEmailsField()">+</button>  
                        <span id="addedButtonsEmailAddFM"></span>
                        <br>
                        <button class="btn btn-outline-success" type="button" style="margin-top: 29px" onclick="addPhonesField()">+</button>
                        <span id="addedButtonsPhoneAddFM"></span>  
</span>
                    </div>
                    <div style="color: chocolate">${globalTexts.get(2109)}: 
                    <span style="font-style: italic; color: black">${langMatrix.get("email")}: <span id="tryEmail" style="font-weight: bold">4</span> ${langMatrix.get("phone")}: <span id="tryPhone" style="font-weight: bold">4</span></span></div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                    ${langMatrix.get("main")} ${langMatrix.get("address").toLowerCase()}
                </button>
            </h2>
            <div id="collapseThree" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px;margin-left:15px">
        <label for="indexAddFM" style="color: chocolate">${langMatrix.get("index")}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="indexAddFM" name="indexAddFM" >                  
        <label for="countryAddFM" style="color: chocolate; padding-top: 2px">${langMatrix.get("country")}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="countryAddFM" name="countryAddFM" >
        <label for="regionAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2111)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="regionAddFM" name="regionAddFM" >
        <label for="cityAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2112)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="cityAddFM" name="cityAddFM" >
        <label for="streetAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2113)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="streetAddFM" name="streetAddFM" >        
        <label for="houseAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2114)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="houseAddFM" name="houseAddFM" >
        <label for="buildingAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2115)}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:1px;" type="text" id="buildingAddFM" name="buildingAddFM" >
        <label for="flatAddFM" style="color: chocolate; padding-top: 4px">${globalTexts.get(2116)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:2px;" type="text" id="flatAddFM" name="flatAddFM" >  
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseSecurity" aria-expanded="false" aria-controls="collapseSecurity">
                    ${globalTexts.get(2117)}
                </button>
            </h2>
            <div id="collapseSecurity" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body" style="padding-top:0">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 30%"></span>
                        <span class="col-6" style="min-width: 300px; margin-left: 15px"><div id="securityMenu"></div>                                
                        </span>
                        <span class="col" style="width: 30%"></span>
                    </div>
                    <div style="margin-top: 15px; font-weight: bold">${langMatrix.get("attention")}! <span style="font-style:italic;font-weight: normal; font-size:14px">${globalTexts.get(2123)}</span></div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseBiometric" aria-expanded="false" aria-controls="collapseBiometric">
                    ${globalTexts.get(2210)}
                </button>
            </h2>
            <div id="collapseBiometric" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 30%"></span>
                        <span class="col-6" style="min-width: 300px;margin-left:15px">                               
        <label for="ageFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2124)}</label>
        <input class="form-control" style="text-align: center" type="number" id="ageFM" name="ageFM" placeholder="${globalTexts.get(2133)}">                
        <label for="heightFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2125)}</label>
        <input class="form-control" style="text-align: center" type="number" id="heightFM" name="heightFM" placeholder="${globalTexts.get(2133)}">
        <label for="weightFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2126)}</label>
        <input class="form-control" style="text-align: center" type="number" id="weightFM" name="weightFM" placeholder="${globalTexts.get(2133)}">
        <label for="footSizeFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2127)}</label>
        <input class="form-control" style="text-align: center" type="number" id="footSizeFM" name="footSizeFM" placeholder="${globalTexts.get(2133)}">
        <label for="shirtSizeFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2128)}</label>
        <input class="form-control" style="text-align: center" type="number" id="shirtSizeFM" name="shirtSizeFM" placeholder="${globalTexts.get(2133)}">
        <label for="eyesColorFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2129)}</label>
        <select class="form-select" style="text-align: center" id="eyesColorFM" aria-label="eyesColorFM" >
            <option value="UNKNOWN" selected>${langMatrix.get("unknown")}</option>        
            <option value="RED">${langMatrix.get("red")}</option>
            <option value="BROWN" >${langMatrix.get("brown")}</option>
            <option value="BLACK">${langMatrix.get("black")}</option>
            <option value="YELLOW">${langMatrix.get("yellow")}</option>
            <option value="BLUE">${langMatrix.get("blue")}</option>
            <option value="GREEN">${langMatrix.get("green")}</option>
        </select>
        <label for="hairColorFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2130)}</label>
        <select class="form-select" style="text-align: center" id="hairColorFM" aria-label="hairColorFM">
            <option value="UNKNOWN" selected>${langMatrix.get("unknown")}</option>               
            <option value="WHITE">${langMatrix.get("white")}</option>
            <option value="BROWN" >${langMatrix.get("brown")}</option>
            <option value="BLACK">${langMatrix.get("black")}</option>
            <option value="YELLOW">${langMatrix.get("yellow")}</option>
            <option value="RED">${langMatrix.get("red")}</option>
            <option value="BALD">${langMatrix.get("bald")}</option>
        </select>
        <label for="descriptionFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("description")}</label>
        <input class="form-control" style="text-align: center" type="text" id="descriptionFM" name="descriptionFM" placeholder="${globalTexts.get(2134)}">
        </span>
        <span class="col" style="width: 30%"></span>
                    </div>
                    <div id="addAndRemoveBio" style="margin-top: 15px">        
        <button class="btn btn-outline-success" type="button" style="margin-top: 5px; font-size:12px" onclick="addBiometricField()">${globalTexts.get(2131)}</button>
        <button class="btn btn-outline-success" type="button" style="margin-top: 5px; font-size:12px" onclick="removeBiometricField()">${globalTexts.get(2132)}</button>
         <div id="pagan"> </div>
         <div style="margin-top: 15px; font-weight: bold">${langMatrix.get("attention")}! <span style="font-style:italic;font-weight: normal; font-size:14px">${globalTexts.get(2145)}</span></div>
 
                    </div>                      
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#birthAddressAddFM" aria-expanded="false" aria-controls="birthAddressAddFM">
                    ${globalTexts.get(2135)}
                </button>
            </h2>
            <div id="birthAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px;margin-left:15px">
        <label for="birthCountryAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("country")}</label>
        <input class="form-control" type="text" id="birthCountryAddFM" name="birthCountryAddFM" >
        <label for="birthRegionAddFM" style="color: chocolate; padding-top: 5px"> ${globalTexts.get(2111)}</label>
        <input class="form-control" type="text" id="birthRegionAddFM" name="birthRegionAddFM" >
        <label for="birthCityAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2112)}</label>
        <input class="form-control" type="text" id="birthCityAddFM" name="birthCityAddFM" >
        <label for="birthStreetAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2113)}</label>
        <input class="form-control" type="text" id="birthStreetAddFM" name="birthStreetAddFM" >        
        <label for="birthHouseAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2137)}</label>
        <input class="form-control" type="text" id="birthHouseAddFM" name="birthHouseAddFM" >
        <label for="birthRegisterAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2138)}</label>
        <input class="form-control" type="text" id="birthRegisterAddFM" name="birthRegisterAddFM" >
        <div class="card" id="birdCard" style="max-height:700px; align-items:center; border:0; margin-top: 20px">
                     <div class="container-fluid row">
                     <span class="col-10" style="text-align: center;padding: 0 0 0 18%" >
                        <label for="birth" id="label_birth" style="display:inline-block; border: 1px solid #dee2e6; border-radius: 4px; cursor: pointer; background-color: RGB(255,255,255,90%);padding:4px; color: grey;" >${globalTexts.get(6)}</label>
                        <input class="form-control" type="file" id="birth" name="birth" accept="image/*" style="display:none" onchange="selectInputImage('birth', 600,600)" >
                    </span>
                    <span class="col" style="text-align: left; align-content: center">
                        <button type="button" class="btn btn-outline-danger" style="padding: 3px" onclick="removeEntered('birth')">X</button>
                    </span>
                    </div>
                    <div id="imageDefault_birth" style="align-content: center; align-items: center; text-align: center; min-height:200px; min-width: 300px">
                    <img style="max-width: 95% ;max-height:40vh;" id="pictureDef_birth" src=${noPhoto} class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="align-content:center;margin-top:40px;">
                        <div class="card-text" style="margin-top:-30px;">${globalTexts.get(3)}</div>
                    </div>                   
                    </div>
                    <div class="card-body" style="text-align: center; padding: 0">                        
                        <div id="canvasBlock_birth" hidden="hidden" style="align-content: center; align-items: center; text-align: center; min-height:300px; min-width: 300px" > 
                        <div style="padding-top:5px; color: chocolate;">${globalTexts.get(4)}</div>
                        <canvas  style="max-width: 95% ;max-height:40vh;" id="canvas_birth"></canvas>                        
                        </div>
                    </div>                  
        </div>
                       </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="accordion-item">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#burialAddressAddFM" aria-expanded="false" aria-controls="burialAddressAddFM">
                   ${globalTexts.get(2136)}
                </button>
            </h2>
            <div id="burialAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px;margin-left:15px">
        <label for="burialCountryAddFM" style="color: chocolate">${langMatrix.get("country")}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialCountryAddFM" name="burialCountryAddFM" >
        <label for="burialRegionAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2111)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialRegionAddFM" name="burialRegionAddFM" >
        <label for="burialCityAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2112)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialCityAddFM" name="burialCityAddFM" >
        <label for="burialCemeteryAddFM" style="color: chocolate; padding-top: 2px">${langMatrix.get("cemetery")}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialCemeteryAddFM" name="burialCemeteryAddFM" >  
        <label for="burialStreetAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2113)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialStreetAddFM" name="burialStreetAddFM" >        
        <label for="burialHouseAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2140)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialHouseAddFM" name="burialHouseAddFM" >
        <label for="burialBuildingAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2141)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialBuildingAddFM" name="burialBuildingAddFM" >
        <label for="burialFlatAddFM" style="color: chocolate; padding-top: 2px">${globalTexts.get(2142)}</label>
        <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="burialFlatAddFM" name="burialFlatAddFM" >
                <div class="card" id="burialCard" style="max-height:700px; align-items:center; border:0; margin-top: 20px">
                     <div class="container-fluid row">
                     <span class="col-10" style="text-align: center;padding: 0 0 0 18%" >
                        <label for="burial" id="label_burial" style="display:inline-block; border: 1px solid #dee2e6; border-radius: 4px; cursor: pointer; background-color: RGB(255,255,255,90%);padding:4px; color: grey;" >${globalTexts.get(6)}</label>
                        <input class="form-control" type="file" id="burial" name="burial" accept="image/*" style="display:none" onchange="selectInputImage('burial', 600,600)" >
                    </span>
                    <span class="col" style="text-align: left; align-content: center">
                        <button type="button" class="btn btn-outline-danger" style="padding: 3px" onclick="removeEntered('burial')">X</button>
                    </span>
                    </div>
                    <div id="imageDefault_burial" style="align-content: center; align-items: center; text-align: center; min-height:200px; min-width: 300px">
                    <img style="max-width: 95% ;max-height:40vh;" id="pictureDef_burial" src=${noPhoto} class="card-img-top" alt="${globalTexts.get(3)}">
                    <div class="card-img-overlay" style="align-content:center;margin-top:40px;">
                        <div class="card-text" style="margin-top:-30px;">${globalTexts.get(3)}</div>
                    </div>                   
                    </div>
                    <div class="card-body" style="text-align: center; padding: 0">                        
                        <div id="canvasBlock_burial" hidden="hidden" style="align-content: center; align-items: center; text-align: center; min-height:300px; min-width: 300px" > 
                        <div style="padding-top:5px; color: chocolate;">${globalTexts.get(4)}</div>
                        <canvas  style="max-width: 95% ;max-height:40vh;" id="canvas_burial"></canvas>                        
                        </div>
                    </div>                  
                </div> 
                        </span>
                        <span class="col" style="width: 20%"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <button class="btn btn-outline-warning" style="margin-top:20px; margin-bottom: 20px;color: darkred" type="button" onclick="submitBaseFormAddFM()">${globalTexts.get(2139)}</button>
    <button type="submit" onclick="submitBaseFormAddFM()" hidden="hidden"></button>
</form></div>
 `
    URL.revokeObjectURL(defaultPhotos.groundTrees);
    URL.revokeObjectURL(defaultPhotos.borderImage);
    drawSecurityMenu()
}

function drawSecurityMenu(){
    let temp=``
    for (let i = 0; i < 11; i++) {
        temp+=`<label for="chooseSecure${i}" style="color: chocolate; padding-top: 5px">${globalTexts.get(2200+i)}</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;text-align: center" id="chooseSecure${i}" aria-label="chooseSecure${i}">            
            <option value="CONFIDENTIAL" selected>${globalMenu.get(1400)}</option>
            <option value="ACTIVE_FAMILY">${globalTexts.get(2118)}</option>
            <option value="LOGIC_PRIMARY_FAMILY" >${globalTexts.get(2119)}</option>
            <option value="ANCESTOR" >${langMatrix.get("ancestors")}</option>   
            <option value="STRAIGHT_BLOOD">${globalTexts.get(2120)}</option>
            <option value="PRIMARY_FAMILY">${globalTexts.get(2121)}</option> 
            <option value="GENETIC_TREE">${globalTexts.get(2122)}</option>   
            <option value="OPEN" >${langMatrix.get("open")}</option>         
        </select>`
    }
    document.getElementById("securityMenu").innerHTML=temp;
}
function addPhonesField() {
    if (countPhone < 4) {
        document.getElementById("deleteButtonsPhoneAddFM").innerHTML += `
        <div id="leftPhoneAddFM${countPhone}">            
            <br> 
            <button class="btn btn-outline-danger" style="padding: 1px 4px; margin-top: 5px" onclick="deletePhoneField(${countPhone})">X</button>  
        </div>     
`
        document.getElementById("addedPhoneAddFM" + countPhone).innerHTML = ` 
        <span id="centrePhoneAddFM${countPhone}">
            <label for="phoneAddFM${countPhone}" style="color: black; padding-top: 5px">${langMatrix.get("additional")} ${langMatrix.get("phone").toLowerCase()}</label>
            <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="phoneAddFM${countPhone}" name="phoneAddFM${countPhone}" autoComplete="off">
        </span>
`
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

function addEmailsField() {
    if (countEmail < 4) {
        document.getElementById("deleteButtonsEmailAddFM").innerHTML += `          
        <div id="leftEmailAddFM${countEmail}">            
            <br> 
            <button class="btn btn-outline-danger" style="padding: 1px 4px; margin-top: 5px" onclick="deleteEmailField(${countEmail})">X</button>  
        </div>     
`
        document.getElementById("addedEmailAddFM" + countEmail).innerHTML = `
        <span id="centerEmailAddFM${countEmail}">
            <label for="emailAddFM${countEmail}" style="color: black; padding-top: 5px" >${langMatrix.get("additional")} ${langMatrix.get("email").toLowerCase()}</label>
            <input class="form-control" style="padding-bottom:1px; padding-top:1px;" type="text" id="emailAddFM${countEmail}" name="emailAddFM${countEmail}" autoComplete="off">
        </span>
`
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
        <div id="deleteOtherAddFM${countOtherNames}" class="col-1" style="width: 12%;padding:0">
            <br> 
                <button class="btn btn-outline-danger" style="font-size:14px; padding:0 2px 0 2px" onclick="deleteOtherField(${countOtherNames})">X</button>
        </div>
        <div class="col-3" style="padding:0 5px 0 5px">
            <label for="firstNameOtherAddFM${countOtherNames}">${langMatrix.get("name")}</label>
            <input class="form-control form-control-border-color--warning" style="font-size:14px;padding:0 5px 0 5px" type="text" id="firstNameOtherAddFM${countOtherNames}" name="firstNameOtherAddFM${countOtherNames}" autocomplete="off" ></div>
        <div class="col-3" style="padding:0 5px 0 5px">
            <label for="middleNameOtherAddFM${countOtherNames}">${langMatrix.get("patronymic")}</label>
            <input class="form-control" style="font-size:14px;padding:0 5px 0 5px" type="text" id="middleNameOtherAddFM${countOtherNames}" name="middleNameOtherAddFM${countOtherNames}" autocomplete="off" >
        </div>
        <div class="col" style="padding:0 5px 0 5px">
            <label for="lastNameOtherAddFM${countOtherNames}">${langMatrix.get("surname")}</label>
            <input class="form-control" style="font-size:14px;padding:0 5px 0 5px" type="text" id="lastNameOtherAddFM${countOtherNames}" name="lastNameOtherAddFM${countOtherNames}" autocomplete="off">
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

    let other = markOtherToDrop();
    let otherPhones = markPhoneToDrop();
    let otherEmails = markEmailToDrop();
    if (inputBio !== null) createAgeBio(activeBio);
    const jsonData = getFormToDrop(other, otherPhones, otherEmails,bioReduce());
    let editStat = {person: undefined, prime: undefined, birth: undefined, burial: undefined};
    fetch(endPoints.get("base") + "/add", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(r => r.json())
        .then(fm => {
            editStat.person = true;
            if (getExistToProfileMember("prime")) {
                dropBlobToServer(document.getElementById("canvas_prime"), "prime", null, fm.uuid)
                    .then(() => editStat.prime = true).catch(() => console.log("save Prime error"));
                dropBlobToServer(document.getElementById("canvas_little"), "little", null, fm.uuid)
                    .catch(() => console.log("save Little error"));
            } else editStat.prime = false;
            if (getExistToProfileMember("burial")) dropBlobToServer(document.getElementById("canvas_burial"), "burial", null, fm.uuid)
                .then(() => editStat.burial = true)
                .catch(() => console.log("save Burial error"));
            else editStat.burial = false;
            if (getExistToProfileMember("birth")) dropBlobToServer(document.getElementById("canvas_birth"), "birth", null, fm.uuid)
                .then(() => editStat.birth = true)
                .catch(() => console.log("save Birth error"));
            else editStat.birth = false;
        return fm;
        })
        .then((fm)=>{
            let temp=analyzeResult(globalTexts.get(2158), editStat,fm.id);
            getDialogFrame(`menuFunc2100()`,langMatrix.get("success"), temp);}
        )
        .catch(()=>getDialogFrame(`menuFunc2100()`, langMatrix.get("failure"), globalTexts.get(2160)));

}