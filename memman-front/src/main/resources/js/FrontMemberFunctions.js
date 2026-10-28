function selectInputImage(place, picWidthMax, picHeightMax) {
    const fileName = document.getElementById(place).files[0].name;
    document.getElementById(`label_${place}`).innerHTML = `${globalTexts.get(7)} : ${fileName}`
    resizeImagePlus(place, picWidthMax, picHeightMax)
}

function removeEntered(place) {
    document.getElementById(place).files = null;
    document.getElementById(`label_${place}`).innerHTML = globalTexts.get(6)
    document.getElementById(`canvasBlock_${place}`).hidden = true;
    document.getElementById(`imageDefault_${place}`).hidden = false;
}

function resizeImagePlus(place, picWidthMax, picHeightMax) {
    let file;
    if (place === "little") file = document.getElementById("prime").files[0];
    else file = document.getElementById(place).files[0];
    if (file.type.match(/image.*/)) {
        const img = new Image();
        img.src = URL.createObjectURL(file);
        img.onload = function () {
            let newWidth;
            let newHeight;
            let placeImg = document.getElementById(`canvas_${place}`);
            const aspectMax = picWidthMax / picHeightMax;
            const aspectRatio = img.width / img.height;
            if (aspectMax > aspectRatio) {
                newWidth = picWidthMax;
                newHeight = newWidth / aspectRatio;
            } else {
                newHeight = picHeightMax;
                newWidth = newHeight * aspectRatio;
            }
            placeImg.width = newWidth;
            placeImg.height = newHeight;
            let ctx = placeImg.getContext("2d");
            ctx.drawImage(img, 0, 0, placeImg.width, placeImg.height);
            if (document.getElementById(`imageDefault_${place}`) !== undefined && (document.getElementById(`imageDefault_${place}`) !== null)) document.getElementById(`imageDefault_${place}`).hidden = true;
            if (place !== "little") document.getElementById(`canvasBlock_${place}`).hidden = false;
            URL.revokeObjectURL(img.src);
        };
    }
    if (place === "prime") resizeImagePlus("little", 200, 200);
}

function backgroundChange(idEl, color) {
    document.getElementById(idEl).style.backgroundColor = color;
}

function getExistToProfileMember(place) {
    return (document.getElementById(`${place}`).files[0] !== undefined && document.getElementById(`${place}`).files[0] !== null
        && document.getElementById(`canvasBlock_${place}`).hidden !== true)
}

function markOtherToDrop() {
    let other = [];
    let firstOther = "firstNameOtherAddFM";
    let secondOther = "middleNameOtherAddFM";
    let thirdOther = "lastNameOtherAddFM";
    let exist = false;
    if (document.getElementById(firstOther + 0).value.trim().length !== 0
        && document.getElementById(secondOther + 0).value.trim().length !== 0
        && document.getElementById(thirdOther + 0).value.trim().length !== 0) insertOther[0] = 'yes';
    for (let i = 0; i < 5; i++) {
        if (insertOther[i] === 'yes') {
            other.push({
                firstName: document.getElementById(firstOther + i).value,
                middleName: document.getElementById(secondOther + i).value,
                lastName: document.getElementById(thirdOther + i).value
            });
            exist = true;
        }
    }
    return (exist) ? other : null;
}

function markEmailToDrop() {
    let otherEmails = [];
    let exist = false;
    for (let i = 0; i < 5; i++) {
        if (insertEmail[i] === 'yes') {
            otherEmails.push({internName: document.getElementById("emailAddFM" + i).value});
            exist = true;
        }
    }
    return (exist) ? otherEmails : null;
}

function markPhoneToDrop() {
    let otherPhones = [];
    let exist = false;
    for (let i = 0; i < 5; i++) {
        if (insertPhone[i] === 'yes') {
            otherPhones.push({internName: document.getElementById("phoneAddFM" + i).value});
            exist = true;
        }
    }
    return (exist) ? otherPhones : null;
}

async function dropBlobToServer(canvas, place, bucket, namePhoto) {
    canvas.toBlob(function (blob) {
            uploadPhoto(place, blob, bucket, namePhoto)
        }
        , 'image/jpeg', 0.9);
}

async function uploadPhoto(place, blob, bucket, namePhoto) {
    let file = new FormData();
    console.log("отгружаем фото", place, bucket, namePhoto);
    if (namePhoto !== undefined && namePhoto !== null) file.append("name", namePhoto)
    if (bucket !== undefined && bucket !== null) file.append("bucket", bucket)
    file.append("photo", blob);
    file.append("place", place)
    let url;
    if (place === "news") url = endPoints.get("fileSystem") + `/save`; else url = endPoints.get("filePrivate") + `/save`
    await fetch(url, {
        method: 'POST',
        headers: {},
        body: file,
        // }).then(async status => {
        //     URL.revokeObjectURL(document.getElementById(`canvas_${place}`).src)
        //     if (document.getElementById(`resultSave_${place}`) !== undefined && document.getElementById(`resultSave_${place}`) !== null)
        //         document.getElementById(`resultSave_${place}`).innerHTML = await status.text();
    });
}

function clearMainVariable() {
    activeBio = 0;
    countPhone = 0;
    countEmail = 0;
    countOtherNames = 1;
    clickOther = false;
    insertOther = [];
    insertEmail = [];
    insertPhone = [];
    primePhotoExist = false;
    birthPhotoExist = false;
    burialPhotoExist = false;
}

function createAgeBio(pagActive) {
    const form = document.getElementById('baseFormAddFM');
    inputBio[pagActive] = {
        age: (~~(form.elements.ageFM.value / 1000) === 0 && form.elements.ageFM.value > 0) ? form.elements.ageFM.value : '',
        height: (form.elements.heightFM.value === null || form.elements.heightFM.value === '') ? 0 : form.elements.heightFM.value,
        weight: (form.elements.weightFM.value === null || form.elements.weightFM.value === '') ? 0 : form.elements.weightFM.value,
        footSize: (form.elements.footSizeFM.value === null || form.elements.footSizeFM.value === '') ? 0 : form.elements.footSizeFM.value,
        hairColor: (form.elements.hairColorFM.value === "UNKNOWN") ? null : form.elements.hairColorFM.value,
        eyesColor: (form.elements.eyesColorFM.value === "UNKNOWN") ? null : form.elements.eyesColorFM.value,
        shirtSize: (form.elements.shirtSizeFM.value === null || form.elements.shirtSizeFM.value === '') ? 0 : form.elements.shirtSizeFM.value,
        description: form.elements.descriptionFM.value
    };
}

function addBiometricField() {
    createAgeBio(activeBio);
    countBio++;
    activeBio = countBio;
    clearCurrentEntries();
    createCurrentPagination(countBio);
}

function clearCurrentEntries() {
    const form = document.getElementById('baseFormAddFM');
    form.elements.ageFM.value = null;
    form.elements.ageFM.disabled = false;
    form.elements.heightFM.value = null;
    form.elements.weightFM.value = null;
    form.elements.footSizeFM.value = null;
    form.elements.hairColorFM.value = "UNKNOWN";
    form.elements.eyesColorFM.value = "UNKNOWN";
    form.elements.shirtSizeFM.value = null;
    form.elements.descriptionFM.value = null;

}

function removeBiometricField() {
    if (countBio === 0) clearCurrentEntries()
    else {
        countBio--;
        inputBio.splice(activeBio, 1);
        if (activeBio !== 0) activeBio--;
        displayBio(activeBio, -1);
        if (countBio !== 0) createCurrentPagination(activeBio)
        else document.getElementById("pagan").innerHTML = ``;
    }
}

function displayBio(pagActive, oldActive) {
    const form = document.getElementById('baseFormAddFM');
    if (oldActive !== -1) {
        createAgeBio(oldActive);
        activeBio = pagActive;
    }
    form.elements.ageFM.value = inputBio[pagActive].age;
    form.elements.heightFM.value = inputBio[pagActive].height;
    form.elements.weightFM.value = inputBio[pagActive].weight;
    form.elements.footSizeFM.value = inputBio[pagActive].footSize;
    form.elements.hairColorFM.value = (inputBio[pagActive].hairColor === null) ? "UNKNOWN" : inputBio[pagActive].hairColor;
    form.elements.eyesColorFM.value = (inputBio[pagActive].eyesColor === null) ? "UNKNOWN" : inputBio[pagActive].eyesColor;
    form.elements.shirtSizeFM.value = inputBio[pagActive].shirtSize;
    form.elements.descriptionFM.value = inputBio[pagActive].description;
    createCurrentPagination(pagActive);
    form.elements.ageFM.disabled = pagActive < loadBio;
}

function createCurrentPagination(pagActive) {
    let tempPagBio = `
        <div style="margin-top: 15px; color: chocolate">${globalTexts.get(2144)}</div>
        <div style="align-content: center">`
    if (pagActive !== 0) tempPagBio += `
                        <span class="page-item" >
                            <button class="btn btn-outline-warning" type="button" style="padding: 4px;font-size: 20px;color: black"  onclick="displayBio(${pagActive - 1},${pagActive})">&#171;</button>
                        </span>`
    else tempPagBio += `
                        <span class="page-item " >
                            <button class="btn btn-outline-secondary" type="button" style="padding: 4px;font-size: 20px;color: black" disabled>&#171;</button>
                        </span>`
    let lastPoint = false;
    for (let i = 0; i <= countBio; i++) {
        if (countBio < 6 || i === 0 || i === countBio || i === activeBio - 1 || i === activeBio || i === activeBio + 1) {
            if (i === pagActive && inputBio[i] !== undefined && inputBio[i].age !== undefined && inputBio[i].age !== ``)
                tempPagBio += `<span class="page-item active">
                        <button class="btn btn-warning" type="button" style="padding:6px;color: black" onclick="displayBio(${i},${pagActive})">${inputBio[i].age}</button>
                     </span>`
            else if (inputBio[i] !== undefined && inputBio[i].age !== undefined && inputBio[i].age !== ``) tempPagBio += `<span class="page-item">
                        <button class="btn btn-outline-warning" type="button" style="padding:6px;color: black" onclick="displayBio(${i},${pagActive})">${inputBio[i].age}</button>
                     </span>`
            else if (i === pagActive && i === countBio && inputBio[i] === undefined) tempPagBio += `<span class="page-item">
                        <button class="btn btn-warning" type="button" style="padding:6px;color: black" onclick="displayBio(${i},${pagActive})">${globalTexts.get(2143)}</button>
                     </span>`
            else if (i === countBio && inputBio[i] === undefined) tempPagBio += `<span class="page-item">
                        <button class="btn btn-outline-warning" type="button" style="padding:6px;color: black" onclick="displayBio(${i},${pagActive})">${globalTexts.get(2143)}</button>
                     </span>`
            else if (i === pagActive && (inputBio[i] === undefined || inputBio[i].age === ``)) tempPagBio += `<span class="page-item">
                        <button class="btn btn-warning" type="button" style="padding:6px;color: black" onclick="displayBio(${i},${pagActive})">!</button>
                     </span>`
            else if (inputBio[i] === undefined || inputBio[i].age === ``) tempPagBio += `<span class="page-item">
                        <button class="btn btn-outline-warning" type="button" style="padding:6px;color: black" onclick="displayBio(${i},${pagActive})">!</button>
                     </span>`;
            lastPoint = false;
        } else if (!lastPoint) {
            tempPagBio += `<span style="margin: 5px">...</span>`;
            lastPoint = true;
        }

    }
    if (pagActive !== countBio) tempPagBio += `
                        <span class="page-item" id="navBioPrev">
                            <button class="btn btn-outline-warning" type="button" style="padding: 4px;font-size: 20px;color: black" onclick="displayBio(${pagActive + 1},${pagActive})">&#187;</button>
                        </span>`
    else tempPagBio += `
                        <span class="page-item" id="navBioPrev">
                            <button class="btn btn-outline-secondary" type="button" style="padding: 4px;font-size: 20px;color: black" disabled>&#187;</button>
                        </span>`
    tempPagBio += `</div>`
    document.getElementById("pagan").innerHTML = tempPagBio;
}

function bioReduce() {
    let reduceBio = [];
    for (let i = 0; i < inputBio.length; i++) {
        if (inputBio[i].age !== null && inputBio[i].age !== '' && inputBio[i].age > 0) reduceBio.push(inputBio[i])
    }
    return reduceBio;
}

function getFormToDrop(other, otherPhones, otherEmails, reduceBio) {
    const form = document.getElementById('baseFormAddFM');
    let formData = {
        primePhoto: getExistToProfileMember("prime"),
        id: (tempPerson !== null && tempPerson.id !== null) ? tempPerson.id : null,
        localisation: currentLang.toUpperCase(),
        alive: alive,
        secretLevelPhoto: form.elements.chooseSecure0.value,
        secretLevelEdit: form.elements.chooseSecure1.value,
        secretLevelMainInfo: form.elements.chooseSecure2.value,
        secretLevelBirthday: form.elements.chooseSecure3.value,
        secretLevelRemove: form.elements.chooseSecure4.value,
        firstName: form.elements.firstNameAddFM.value,
        middleName: form.elements.middleNameAddFM.value,
        lastName: form.elements.lastNameAddFM.value,
        birthday: form.elements.birthdayAddFM.value,
        deathday: (!alive) ? form.elements.deathdayAddFM.value : null,
        sex: form.elements.chooseSexAddFM.value,
        memberInfo: {
            mainPhone: form.elements.mainPhoneAddFM.value,
            mainEmail: form.elements.mainEmailAddFM.value,
            photoBurialExist: getExistToProfileMember("burial"),
            photoBirthExist: getExistToProfileMember("birth"),
            secretLevelEmail: (alive)? form.elements.chooseSecure5.value:null,
            secretLevelPhone: (alive)? form.elements.chooseSecure6.value:null,
            secretLevelAddress: form.elements.chooseSecure7.value,
            secretLevelBirth: form.elements.chooseSecure8.value,
            secretLevelBurial: (alive) ? form.elements.chooseSecure9.value : null,
            secretLevelBiometric: form.elements.chooseSecure10.value,
            // secretLevelDescription: form.elements.chooseSecure11.value,
            biometric: reduceBio,
            birth: {
                country: form.elements.birthCountryAddFM.value,
                region: form.elements.birthRegionAddFM.value,
                city: form.elements.birthCityAddFM.value,
                street: form.elements.birthStreetAddFM.value,
                birthHouse: form.elements.birthHouseAddFM.value,
                registration: form.elements.birthRegisterAddFM.value,
                photoExist: getExistToProfileMember("birth")
            },
            burial: (!alive) ? {
                country: form.elements.burialCountryAddFM.value,
                region: form.elements.burialRegionAddFM.value,
                city: form.elements.burialCityAddFM.value,
                cemetery: form.elements.burialCemeteryAddFM.value,
                street: form.elements.burialStreetAddFM.value,
                chapter: form.elements.burialHouseAddFM.value,
                square: form.elements.burialBuildingAddFM.value,
                grave: form.elements.burialFlatAddFM.value,
                photoExist: getExistToProfileMember("burial")
            } : null,
            phones: (alive)?otherPhones:null,
            emails: (alive)?otherEmails:null,
            addresses: [
                {
                    country: form.elements.countryAddFM.value,
                    region: form.elements.regionAddFM.value,
                    city: form.elements.cityAddFM.value,
                    index: form.elements.indexAddFM.value,
                    street: form.elements.streetAddFM.value,
                    house: form.elements.houseAddFM.value,
                    building: form.elements.buildingAddFM.value,
                    flatNumber: form.elements.flatAddFM.value
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
        fioDtos: other
    };
    return JSON.stringify(formData);
}

async function assignPersonInfoToPerson(person) {
    console.log(person);
    if (tempPerson.memberInfo.photoBirthExist && tempPerson.memberInfo.secretLevelBirth !== "CLOSE") {
        await loadPicture(endPoints.get("filePrivate") + "/get/birth/" + tempPerson.uuid)
            .then((r) => {
                tempPerson.memberInfo.birthImj = r;
            })
            .catch(() => {
                tempPerson.memberInfo.birthImj = "";
            });
    }
    if (tempPerson.memberInfo.photoBurialExist && tempPerson.memberInfo.secretLevelBurial !== "CLOSE") {
        await loadPicture(endPoints.get("filePrivate") + "/get/burial/" + tempPerson.uuid)
            .then((r) => {
                tempPerson.memberInfo.burImj = r;
            })
            .catch(() => {
                tempPerson.memberInfo.burImj = "";
            });
    }
    tempPerson.memberInfo.emails = person.memberInfo.emails;
    tempPerson.memberInfo.biometric = person.memberInfo.biometric;
    tempPerson.memberInfo.description = person.memberInfo.description;
    tempPerson.memberInfo.burial = person.memberInfo.burial;
    tempPerson.memberInfo.birth = person.memberInfo.birth;
    tempPerson.memberInfo.phones = person.memberInfo.phones;
    tempPerson.memberInfo.addresses = person.memberInfo.addresses;
    tempPerson.fioDtos = person.fioDtos;
    if (tempPerson.memberInfo.biometric !== null && tempPerson.memberInfo.secretLevelBiometric !== "CLOSE") {
        inputBio = tempPerson.memberInfo.biometric.sort((a, b) => a.age - b.age);
        countBio = inputBio.length - 1;
        loadBio = inputBio.length;
    }
}

function getAge(birthday, deathday) {
    if (deathday === infoAbsent) return deathday;
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

function analyzeResult(successString, stat, data) {
    let temp = ``;
    if (stat.person !== undefined) {
        if (stat.person) temp += `<div>${successString}</div><div style="font-weight: bold">Id</div><div style="color: darkred">${data}</div>`;
    } else throw new Error();
    if (stat.prime !== undefined) {
        if (stat.prime) temp += `<div>${globalTexts.get(2161)}: <span style="font-style: italic">${globalTexts.get(2166)}</span></div>`;
        else temp += `<div>${globalTexts.get(2161)}: <span style="font-style: italic">${globalTexts.get(2164)}</span></div>`;
    } else temp += `<div>${globalTexts.get(2161)}: <span style="font-style: italic">${globalTexts.get(2165)}</span></div>`;
    if (stat.birth !== undefined) {
        if (stat.birth) temp += `<div>${globalTexts.get(2162)}: <span style="font-style: italic">${globalTexts.get(2166)}</span></div>`;
        else temp += `<div>${globalTexts.get(2162)}: <span style="font-style: italic">${globalTexts.get(2164)}</span></div>`;
    } else temp += `<div>${globalTexts.get(2162)}: <span style="font-style: italic"> ${globalTexts.get(2165)}</span></div>`;
    if (!stat.alive) {
    if (stat.burial !== undefined) {
        if (stat.burial) temp += `<div>${globalTexts.get(2163)}: <span style="font-style: italic">${globalTexts.get(2166)}</span></div>`;
        else temp += `<div>${globalTexts.get(2163)}: <span style="font-style: italic">${globalTexts.get(2164)}</span></div></div>`;
    } else temp += `<div>${globalTexts.get(2163)}: <span style="font-style: italic">${globalTexts.get(2165)}</span></div>`;}
    return temp;
}