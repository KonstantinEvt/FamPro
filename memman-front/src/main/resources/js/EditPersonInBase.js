function editPerson() {
    clearMainVariable();
    let background = URL.createObjectURL(defaultPhotos.groundTrees);
    let noPhoto = URL.createObjectURL(defaultPhotos.borderImage);
    document.getElementById("mainPanel").innerHTML = `
<div style="vertical-align: top; "><div class="main_title" id="addPerson-title">${globalTexts.get(2301)}</div>
<div class="main_under">ID: <span class="main_title" style="color: darkred; font-style:normal">${tempPerson.id}</span></div>
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
                                <span class="col-6" style="width: 250px">
                                    <div id="frontPhoto" style="margin-bottom:2px; font-size: 18px; text-align: center; color: black;font-family: 'Times New Roman',serif"></div>
                                </span>
                                <span class="col" style="width: 20%"></span>
                            </div>  
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="width: 20%"></span>
                        <span class="col-8" style="min-width: 300px;margin-left:15px">
        <label for="firstNameAddFM" style="color: chocolate">${langMatrix.get("name")}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="firstNameAddFM" name="firstNameAddFM" value="${tempPerson.firstName}"required>
        <label for="middleNameAddFM" style="color: chocolate; padding-top: 4px">${langMatrix.get("patronymic")}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="middleNameAddFM" name="middleNameAddFM" value="${tempPerson.middleName}"required>
        <label for="lastNameAddFM" style="color: chocolate; padding-top: 4px">${langMatrix.get("surname")}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="text" id="lastNameAddFM" name="lastNameAddFM" value="${tempPerson.lastName}"required>
        <label for="birthdayAddFM" style="color: chocolate; padding-top: 5px">${globalTexts.get(2103)}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="date" id="birthdayAddFM" name="birthdayAddFM" required>
        <label for="deathdayAddFM" style="color: chocolate; padding-top: 4px">${globalTexts.get(2104)}</label>
        <input class="form-control" style="padding-bottom:2px; padding-top:2px;" type="date" id="deathdayAddFM" name="deathdayAddFM">
        <label for="chooseSexAddFM" style="color: chocolate; padding-top: 4px">${langMatrix.get("gender")}</label>
        <select class="form-select" style="padding-bottom:2px; padding-top:2px;" id="chooseSexAddFM" aria-label="chooseSexAddFM">            
            <option value="MALE" selected>${langMatrix.get("male")}</option>
            <option value="FEMALE">${langMatrix.get("female")}</option>
        </select>
        <div class="card" id="primeCard" style="max-height:700px; align-items:center; border:0; margin-top: 20px" hidden="hidden">
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
                        <div class="card-text" style="margin-top:-30px;">${globalTexts.get(2312)}</div>
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
                        <span class="col" style="width: 48%;margin-left:4%;padding:0 6px 0 6px">
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
                        <span class="col" style="width: 48%;padding:0 6px 0 6px">
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
                     <div style="margin-top: 15px; font-weight: bold" id="attentionParents" hidden="hidden">${langMatrix.get("attention")}! <span id="lockParentsInfo" style="font-style:italic;font-weight: normal; font-size:14px"></span></div>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="otherNamesEdit" hidden="hidden">
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
        <input class="form-control form-control-border-color--warning" style="padding:3px; font-size:14px" type="text" id="firstNameOtherAddFM0" name="firstNameOtherAddFM0" autocomplete="on" >
                        
                        </div>
                        <div class="col-3" style="padding:0 5px 0 5px">
        <label for="middleNameOtherAddFM0" >${langMatrix.get("patronymic")}</label>
        <input class="form-control" type="text" style="padding:3px; font-size:14px" id="middleNameOtherAddFM0" name="middleNameOtherAddFM0" >
                        </div>
                         <div class="col" style="padding:0 5px 0 5px">
        <label for="lastNameOtherAddFM0">${langMatrix.get("surname")}</label>
        <input class="form-control" type="text" style="padding:3px; font-size:14px" id="lastNameOtherAddFM0" name="lastNameOtherAddFM0" >
                        </div>
                        <div id="addOtherAddFM" class="col-1" style="padding:0 5px 0 5px;;min-width:40px;margin-top: 18px">
                            <button class="btn btn-outline-success" type="button" style="font-size:12px" onclick="addOtherFieldEdit()">+</button>  
                        </div>
                        <div  class="col-1" style="padding:0 5px 0 5px;margin-top: 18px">
                            <button id="changeWithMain0" class="btn btn-outline-warning" type="button" style="font-size:12px" onclick="changeWithMain(0)">&#8693;</button>  
                        </div>
                    </div>
                    <div id="otherAddFM1" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM2" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM3" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div id="otherAddFM4" class="container-fluid row mh-100 no-gutters"  style="font-size:12px; color: chocolate; padding: 0; margin-top: 5px"></div>
                    <div style="margin-top: 15px; font-weight: bold" id="attentionOtherNames" hidden="hidden">${langMatrix.get("attention")}! <span id="otherNamesInfo" style="font-style:italic;font-weight: normal; font-size:14px"></span></div>
                    <div>${globalTexts.get(2108)}</div>
                    <div style="color: chocolate">${globalTexts.get(2109)}: <span id="tryOther" style="color: black;font-weight: bold">4</span></div> 
                </div>
            </div>
        </div>
        <div class="accordion-item" id="contactPage" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#collapseContact" aria-expanded="false" aria-controls="collapseContact">
                    ${globalTexts.get(2110)}
                </button>
            </h2>
            <div id="collapseContact" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
                <div class="accordion-body">
                    <div class="container-fluid row mh-100 no-gutters">
                        <span class="col" style="text-align: right; width: 20%"> 
                            <div id="deleteButtonsEmailAddFM" style="margin-bottom: 45px; margin-top: 45px;"><br>
                            </div>
                            <div id="deleteButtonsPhoneAddFM" style=" margin-top: 0"><br>
                            </div>  
                        </span>
                        <span class="col-8" style="min-width: 200px;padding:0">
        <label for="mainEmailAddFM" style="color: chocolate; padding-top: 5px" >${langMatrix.get("main")} ${langMatrix.get("email").toLowerCase()}</label>
        <input class="form-control" type="text" id="mainEmailAddFM" name="mainEmailAddFM" autocomplete="off" disabled="disabled">
        <div id="addedEmailAddFM0"></div>
        <div id="addedEmailAddFM1"></div>
        <div id="addedEmailAddFM2"></div>
        <div id="addedEmailAddFM3"></div>
        <div id="addedEmailAddFM4"></div>
        
        <label for="mainPhoneAddFM" style="color: chocolate; padding-top: 5px">${langMatrix.get("main")} ${langMatrix.get("phone").toLowerCase()}</label>
        <input class="form-control" type="text" id="mainPhoneAddFM" name="mainPhoneAddFM" autocomplete="off" disabled="disabled">
        <div id="addedPhoneAddFM0" ></div>
        <div id="addedPhoneAddFM1" ></div>
        <div id="addedPhoneAddFM2" ></div>
        <div id="addedPhoneAddFM3" ></div>
        <div id="addedPhoneAddFM4" ></div>
                        </span>
                        <span id="addButtons" class="col" style="text-align: center; width: 20%; padding:0">                        
                        <br> 
                        <button class="btn btn-outline-success" type="button" style="margin-top: 5px" onclick="addEmailsFieldEdit()" id="addEmailButtom" hidden="hidden">+</button>  
                        <span id="addedButtonsEmailAddFM"></span>
                        <br>
                        <button class="btn btn-outline-success" type="button" style="margin-top: 29px" onclick="addPhonesFieldEdit()" id="addPhoneButtom" hidden="hidden">+</button>
                        <span id="addedButtonsPhoneAddFM"></span>  
                        </span>
                    </div>
                    <div style="color: chocolate">${globalTexts.get(2109)}:
                    <span style="font-style: italic; color: black">${langMatrix.get("email")}: <span id="tryEmail" style="font-weight: bold">4</span> ${langMatrix.get("phone")}: <span id="tryPhone" style="font-weight: bold">4</span></span></div>
                </div>
            </div>
        </div>
        <div class="accordion-item" id="addressEdit" hidden="hidden">
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
        <div class="accordion-item" id="securitySecurity" hidden="hidden">
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
                </div></div>
        </div>
        <div class="accordion-item" id="biometricEdit" hidden="hidden">
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
        <div class="accordion-item" id="birthEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#birthAddressAddFM" aria-expanded="false" aria-controls="birthAddressAddFM">
                    ${globalTexts.get(2135)}
                </button>
            </h2>
            <div id="birthAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
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
                        <div class="card-text" style="margin-top:-30px;">${globalTexts.get(2312)}</div>
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
        <div class="accordion-item" id="burialEdit" hidden="hidden">
            <h2 class="accordion-header">
                <button class="accordion-button collapsed" style="padding-bottom:8px; padding-top:8px; color: darkred" type="button" data-bs-toggle="collapse" data-bs-target="#burialAddressAddFM" aria-expanded="false" aria-controls="burialAddressAddFM">
                   ${globalTexts.get(2136)}
                </button>
            </h2>
            <div id="burialAddressAddFM" class="accordion-collapse collapse" data-bs-parent="#addPersonAccord">
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
                        <div class="card-text" style="margin-top:-30px;">${globalTexts.get(2312)}</div>
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
    <button class="btn btn-outline-warning" style="margin-top:20px; margin-bottom: 20px; color: darkred" type="button" onclick="submitBaseFormEditFM()">${globalTexts.get(2314)}</button>
    <button  type="submit" onclick="submitBaseFormEditFM()" hidden="hidden"></button>
</form>
 `
    URL.revokeObjectURL(defaultPhotos.groundTrees);
    URL.revokeObjectURL(defaultPhotos.borderImage);
    drawSecurityMenu()
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
    if (tempPerson.birthday !== null) document.getElementById("birthdayAddFM").value = getDateToFull(new Date(tempPerson.birthday));
    document.getElementById("chooseSexAddFM").value = tempPerson.sex;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.firstName !== null) document.getElementById("firstNameMotherAddFM").value = tempPerson.motherFio.firstName;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.middleName !== null) document.getElementById("middleNameMotherAddFM").value = tempPerson.motherFio.middleName;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.lastName !== null) document.getElementById("lastNameMotherAddFM").value = tempPerson.motherFio.lastName;
    if (tempPerson.motherFio !== null && tempPerson.motherFio.birthday !== null) document.getElementById("birthdayMotherAddFM").value = getDateToFull(new Date(tempPerson.motherFio.birthday));
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.firstName !== null) document.getElementById("firstNameFatherAddFM").value = tempPerson.fatherFio.firstName;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.middleName !== null) document.getElementById("middleNameFatherAddFM").value = tempPerson.fatherFio.middleName;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.lastName !== null) document.getElementById("lastNameFatherAddFM").value = tempPerson.fatherFio.lastName;
    if (tempPerson.fatherFio !== null && tempPerson.fatherFio.birthday !== null) document.getElementById("birthdayFatherAddFM").value = getDateToFull(new Date(tempPerson.fatherFio.birthday));
    if (tempPerson.deathday !== null) document.getElementById("deathdayAddFM").value = getDateToFull(new Date(tempPerson.deathday));
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
        document.getElementById("primeCard").hidden=false;
        document.getElementById("otherNamesEdit").hidden = false;
        document.getElementById("securitySecurity").hidden = false;
        if (tempPerson.secretLevelEdit !== "CLOSE")
            document.getElementById("chooseSecure1").value = tempPerson.secretLevelEdit;
        document.getElementById("chooseSecure2").value = tempPerson.secretLevelMainInfo;
        if (tempPerson.secretLevelBirthday !== "CLOSE") {
            document.getElementById("chooseSecure3").value = tempPerson.secretLevelBirthday;
        } else {
            document.getElementById("chooseSecure3").hidden = true;
            document.getElementById("labelSecure3").hidden = true;
        }
        if (tempPerson.secretLevelPhoto !== "CLOSE") {
            // document.getElementById("labelPrimePhoto").hidden = false;
            // document.getElementById("prime").hidden = false;
            document.getElementById("chooseSecure0").value = tempPerson.secretLevelPhoto;
        } else {
            document.getElementById("primeCard").hidden = true;
            document.getElementById("chooseSecure0").hidden = true;
            document.getElementById("labelSecure0").hidden = true;
        }
        if (tempPerson.secretLevelRemove !== "CLOSE") {
            document.getElementById("chooseSecure4").value = tempPerson.secretLevelRemove;
        } else {
            document.getElementById("chooseSecure4").hidden = true;
            document.getElementById("labelSecure4").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelPhone !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelPhone !== "UNDEFINED")
                document.getElementById("chooseSecure6").value = tempPerson.memberInfo.secretLevelPhone;
        } else {
            document.getElementById("chooseSecure6").hidden = true;
            document.getElementById("labelSecure6").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelEmail !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelEmail !== "UNDEFINED")
                document.getElementById("chooseSecure5").value = tempPerson.memberInfo.secretLevelEmail;
        } else {
            document.getElementById("chooseSecure5").hidden = true;
            document.getElementById("labelSecure5").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelAddress !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelAddress !== "UNDEFINED")
                document.getElementById("chooseSecure7").value = tempPerson.memberInfo.secretLevelAddress;
        } else {
            document.getElementById("chooseSecure7").hidden = true;
            document.getElementById("labelSecure7").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelBiometric !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelBiometric !== "UNDEFINED")
                document.getElementById("chooseSecure10").value = tempPerson.memberInfo.secretLevelBiometric;
        } else {
            document.getElementById("chooseSecure10").hidden = true;
            document.getElementById("labelSecure10").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelBirth !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelBirth !== "UNDEFINED")
                document.getElementById("chooseSecure8").value = tempPerson.memberInfo.secretLevelBirth;
        } else {
            document.getElementById("chooseSecure8").hidden = true;
            document.getElementById("labelSecure8").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelBurial !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelBurial !== "UNDEFINED")
                document.getElementById("chooseSecure9").value = tempPerson.memberInfo.secretLevelBurial;
        } else {
            document.getElementById("chooseSecure9").hidden = true;
            document.getElementById("labelSecure9").hidden = true;
        }
        if (tempPerson.memberInfo.secretLevelDescription !== "CLOSE") {
            if (tempPerson.memberInfo.secretLevelDescription !== "UNDEFINED")
                document.getElementById("chooseSecure11").value = tempPerson.memberInfo.secretLevelDescription;
        } else {
            document.getElementById("chooseSecure11").hidden = true;
            document.getElementById("labelSecure11").hidden = true;
        }
        let linkingMother = tempPerson.motherInfo !== globalTexts.get(2302) && tempPerson.motherInfo !== globalTexts.get(2303) && tempPerson.motherInfo !== globalTexts.get(2304) && tempPerson.motherInfo !== globalTexts.get(2305) && tempPerson.motherInfo.charAt(0) !== '(';
        let linkingFather = tempPerson.fatherInfo !== globalTexts.get(2302) && tempPerson.fatherInfo !== globalTexts.get(2303) && tempPerson.fatherInfo !== globalTexts.get(2304) && tempPerson.fatherInfo !== globalTexts.get(2305) && tempPerson.fatherInfo.charAt(0) !== '(';
        if (linkingMother || linkingFather) {
            document.getElementById("attentionParents").hidden=false;
            if (linkingMother && tempPerson.secretLevelRemove === "CLOSE") {
                lockMother();
                document.getElementById("lockParentsInfo").innerHTML = globalTexts.get(2306);
            } else if (linkingMother && !linkingFather && tempPerson.secretLevelRemove !== "CLOSE") {
                document.getElementById("lockParentsInfo").innerHTML = globalTexts.get(2307);
            }
            if (linkingFather && tempPerson.secretLevelRemove === "CLOSE") {
                lockFather();
                document.getElementById("lockParentsInfo").innerHTML = globalTexts.get(2306);
            } else if (linkingFather && !linkingMother && tempPerson.secretLevelRemove !== "CLOSE") {
                document.getElementById("lockParentsInfo").innerHTML = globalTexts.get(2308);
            }
            if (linkingFather && linkingMother && tempPerson.secretLevelRemove !== "CLOSE") {
                document.getElementById("lockParentsInfo").innerHTML = globalTexts.get(2309);
            }
        }
        if (tempPerson.fioDtos !== null) {
            document.getElementById(`firstNameOtherAddFM0`).value = tempPerson.fioDtos[0].firstName;
            document.getElementById(`middleNameOtherAddFM0`).value = tempPerson.fioDtos[0].middleName;
            document.getElementById(`lastNameOtherAddFM0`).value = tempPerson.fioDtos[0].lastName;
            document.getElementById(`firstNameOtherAddFM0`).disabled = "disabled";
            document.getElementById(`middleNameOtherAddFM0`).disabled = "disabled";
            document.getElementById(`lastNameOtherAddFM0`).disabled = "disabled";
            insertOther[0] = 'no';
            document.getElementById("attentionOtherNames").hidden=false;
            document.getElementById("otherNamesInfo").innerHTML = globalTexts.get(2310);
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
                insertOther[countOtherNames] = 'no';
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
        if (tempPerson.memberInfo.addresses !== null && tempPerson.memberInfo.addresses.length !== 0) {
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
        if (tempPerson.memberInfo.biometric !== null && tempPerson.memberInfo.biometric.length !== 0) {
            document.getElementById("ageFM").value = tempPerson.memberInfo.biometric[0].age;
            document.getElementById("ageFM").disabled = true;
            document.getElementById("heightFM").value = tempPerson.memberInfo.biometric[0].height;
            document.getElementById("weightFM").value = tempPerson.memberInfo.biometric[0].weight;
            document.getElementById("footSizeFM").value = tempPerson.memberInfo.biometric[0].footSize;
            document.getElementById("shirtSizeFM").value = tempPerson.memberInfo.biometric[0].shirtSize;
            document.getElementById("eyesColorFM").value = (tempPerson.memberInfo.biometric[0].eyesColor === null) ? "UNKNOWN" : tempPerson.memberInfo.biometric[0].eyesColor;
            document.getElementById("hairColorFM").value = (tempPerson.memberInfo.biometric[0].hairColor === null) ? "UNKNOWN" : tempPerson.memberInfo.biometric[0].hairColor;
            document.getElementById("descriptionFM").value = tempPerson.memberInfo.biometric[0].description;
        }
        if (countBio > 0) createCurrentPagination(0);

    }
    if (tempPerson.memberInfo.secretLevelBurial !== "CLOSE") {
        document.getElementById("burialEdit").hidden = false;
        if (tempPerson.memberInfo.photoBurialExist !== false && tempPerson.memberInfo.burImj !== undefined && tempPerson.memberInfo.burImj !== "") {
            let tempURL = URL.createObjectURL(tempPerson.memberInfo.burImj)

            document.getElementById("frontBurialPhoto").innerHTML = `
                    <div class="card text-bg-dark" style="width: 250px">
                        <img src="${tempURL}" class="card-img" alt="loading...">                        
                    </div>`

            URL.revokeObjectURL(tempPerson.memberInfo.burImj);
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
        if (tempPerson.memberInfo.photoBirthExist !== false && tempPerson.memberInfo.birthImj !== undefined && tempPerson.memberInfo.birthImj !== "") {
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

function changeWithMain(otherField) {
    let first = document.getElementById("firstNameAddFM").value;
    let middle = document.getElementById("middleNameAddFM").value;
    let last = document.getElementById("lastNameAddFM").value;
    if (document.getElementById(`firstNameOtherAddFM${otherField}`).value.trim().length === 0
        || document.getElementById(`middleNameOtherAddFM${otherField}`).value.trim().length === 0
        || document.getElementById(`lastNameOtherAddFM${otherField}`).value.trim().length === 0) return;
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
        if (document.getElementById(`deleteOther${otherField}`) !== undefined && insertOther[otherField] === 'yes') document.getElementById(`deleteOther${otherField}`).disabled = true;
        insertOther[otherField] = 'yes';
        for (let i = 0; i < countOtherNames; i++) {
            if (i !== otherField && document.getElementById(`changeWithMain${i}`) !== undefined) document.getElementById(`changeWithMain${i}`).disabled = true;
        }
    } else {
        document.getElementById("firstNameAddFM").disabled = false;
        document.getElementById("middleNameAddFM").disabled = false;
        document.getElementById("lastNameAddFM").disabled = false;
        if (tempPerson.fioDtos.length > otherField) insertOther[otherField] = 'no';
        else {
            insertOther[otherField] = 'yes';
            if (document.getElementById(`deleteOther${otherField}`) !== undefined
                && insertOther[otherField] === 'yes') document.getElementById(`deleteOther${otherField}`).disabled = false;
            document.getElementById(`firstNameOtherAddFM${otherField}`).disabled = false;
            document.getElementById(`middleNameOtherAddFM${otherField}`).disabled = false;
            document.getElementById(`lastNameOtherAddFM${otherField}`).disabled = false;
        }
        for (let i = 0; i < countOtherNames; i++) {
            if (document.getElementById(`changeWithMain${i}`) !== undefined) document.getElementById(`changeWithMain${i}`).disabled = false;
        }
    }
    clickOther = !clickOther;
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
            <label for="phoneAddFM${countPhone}" style="color: black; padding-top: 5px">${langMatrix.get("additional")} ${langMatrix.get("phone").toLowerCase()}</label>
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
            <label for="emailAddFM${countEmail}" style="color: black; padding-top: 5px" >${langMatrix.get("additional")} ${langMatrix.get("email").toLowerCase()}</label>
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
        <div id="deleteOtherAddFM${countOtherNames}" class="col-1" style="min-width: 40px">
                <button  id="deleteOther${countOtherNames}" class="btn btn-outline-danger" style="padding:5px; font-size:14px; margin-top: 14px; margin-bottom: 10px" onclick="deleteOtherFieldEdit(${countOtherNames})">X</button>
        </div>
        <div class="col-3" style="padding:0 5px 0 5px">
            <label for="firstNameOtherAddFM${countOtherNames}" >${langMatrix.get("name")}</label>
            <input class="form-control form-control-border-color--warning" style="font-size:14px;padding:0 5px 0 5px" type="text" id="firstNameOtherAddFM${countOtherNames}" name="firstNameOtherAddFM${countOtherNames}" autocomplete="off" ></div>
        <div class="col-3" style="padding:0 5px 0 5px">
            <label for="middleNameOtherAddFM${countOtherNames}" >${langMatrix.get("patronymic")}</label>
            <input class="form-control" style="font-size:14px;padding:0 5px 0 5px" type="text" id="middleNameOtherAddFM${countOtherNames}" name="middleNameOtherAddFM${countOtherNames}" autocomplete="off" >
        </div>
        <div class="col" style="padding:0 5px 0 5px">
            <label for="lastNameOtherAddFM${countOtherNames}" >${langMatrix.get("surname")}</label>
            <input class="form-control" style="font-size:14px;padding:0 5px 0 5px" type="text" id="lastNameOtherAddFM${countOtherNames}" name="lastNameOtherAddFM${countOtherNames}" autocomplete="off">
        </div>
        <div class="col-1" >        
            <button class="btn btn-outline-warning" type="button" style="padding:5px; font-size:14px; margin-top: 14px; margin-bottom: 10px" id="changeWithMain${countOtherNames}" onclick="changeWithMain(${countOtherNames})">&#8693;</button>  
        </div> `;
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
    let other = markOtherToDrop();
    let otherPhones;
    let otherEmails;
    (tempPerson.memberInfo !== null && tempPerson.memberInfo.secretLevelEmail === "CLOSE") ? otherEmails = null : otherEmails = markEmailToDrop();
    (tempPerson.memberInfo !== null && tempPerson.memberInfo.secretLevelPhone === "CLOSE") ? otherPhones = null : otherPhones = markPhoneToDrop();
    if (inputBio !== null && tempPerson.memberInfo !== null && tempPerson.memberInfo.secretLevelBiometric !== "CLOSE") createAgeBio(activeBio);
    const jsonData = getFormToDrop(other, otherPhones, otherEmails);
    console.log(jsonData);
    let editStat = {person: undefined, prime: undefined, birth: undefined, burial: undefined};
    fetch(endPoints.get("base") + "/edit", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    })
        .then(r => r.json())
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
            tempPerson = ``;
            tempSecurity = ``;
            loadBio = 0;
            countBio = 0;
            return fm;
        })
        .then((fm) => {
            let temp = analyzeResult(globalTexts.get(2311), editStat, fm.id);
            getDialogFrame(`menuFunc2100()`, langMatrix.get("success"), temp);
        })
        .catch(() => getDialogFrame(`menuFunc2100()`, langMatrix.get("failure"), globalTexts.get(2313)));
}
