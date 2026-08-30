function resizeImagePlus(place) {
    const file = document.getElementById(place).files[0];
    if (file.type.match(/image.*/)) {
        const img = new Image();
        img.src = URL.createObjectURL(file);
        img.onload = function () {
            let canvas = document.getElementById(`canvas${place}`);
            const aspectRatio = img.width / img.height; // вычисляем соотношение сторон

            // Устанавливаем новые размеры
            const newWidth = 600;  // Желаемая ширина
            const newHeight = newWidth / aspectRatio;
            canvas.width = newWidth; // Задайте требуемые размеры
            canvas.height = newHeight; // Определите высоту и ширину
            let ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
            document.getElementById(`canvasBlock${place}`).hidden = false;
            URL.revokeObjectURL(img.src); // Освобождаем память
        };

    }
}

function dropPhotosToServer() {
    if (document.getElementById("PrimePhoto").files[0] !== null
        && document.getElementById("canvasBlockPrimePhoto").hidden !== true) {
        primePhotoExist = true;
        dropBlobToServer(document.getElementById("canvasPrimePhoto"), "PrimePhoto");

    } else document.getElementById("resultSavePrimePhoto").innerHTML = 'Prime photo not selected!';

    if (document.getElementById("BirthPhoto").files[0] !== null && document.getElementById("canvasBlockBirthPhoto").hidden !== true) {
        birthPhotoExist = true;
        dropBlobToServer(document.getElementById("canvasBirthPhoto"), "BirthPhoto");

    } else document.getElementById("resultSaveBirthPhoto").innerHTML = 'BirthPhoto photo not selected!';

    if (document.getElementById("BurialPhoto").files[0] !== null && document.getElementById("canvasBlockBurialPhoto").hidden !== true) {
        burialPhotoExist = true;
        dropBlobToServer(document.getElementById("canvasBurialPhoto"), "BurialPhoto");
    } else document.getElementById("resultSaveBurialPhoto").innerHTML = 'BurialPhoto photo not selected!';

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

function dropBlobToServer(canvas, place) {
    canvas.toBlob(function (blob) {
            uploadPhoto(place, blob)
        }
        , 'image/jpeg', 0.9);
}

function uploadPhoto(place, blob) {
    let file = new FormData();
    file.append(place, blob, place + '.jpeg');
    fetch(`/file/save${place}`, {
        method: 'POST',
        headers: {},
        body: file,
    }).then(async status => {
        URL.revokeObjectURL(document.getElementById(`canvas${place}`).src)
        document.getElementById(`resultSave${place}`).innerHTML = await status.text();
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
        age: form.elements.ageFM.value,
        height: form.elements.heightFM.value,
        weight: form.elements.weightFM.value,
        footSize: form.elements.footSizeFM.value,
        hairColor: (form.elements.hairColorFM.value === "UNKNOWN") ? null : form.elements.hairColorFM.value,
        eyesColor: (form.elements.eyesColorFM.value === "UNKNOWN") ? null : form.elements.eyesColorFM.value,
        shirtSize: form.elements.shirtSizeFM.value,
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
    form.elements.ageFM.value = '';
    form.elements.ageFM.disabled =false;
    form.elements.heightFM.value = '';
    form.elements.weightFM.value = '';
    form.elements.footSizeFM.value = '';
    form.elements.hairColorFM.value = "UNKNOWN";
    form.elements.eyesColorFM.value = "UNKNOWN";
    form.elements.shirtSizeFM.value = '';
    form.elements.descriptionFM.value = '';

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
    let tempPagBio = `<br>
        <div>Введенные возрасты</div>
        <nav aria-label="BioNavigation">
            <ul class="pagination">`
    if (pagActive !== 0) tempPagBio += `
                        <li class="page-item" >
                            <button class="btn btn-outline-warning" type="button" onclick="displayBio(${pagActive - 1},${pagActive})">Предыдущий</button>
                        </li>`
    else tempPagBio += `
                        <li class="page-item " >
                            <button class="btn btn-outline-secondary" type="button" disabled>Предыдущий</button>
                        </li>`
    for (let i = 0; i <= countBio; i++) {
        if (i === pagActive && inputBio[i] !== undefined && inputBio[i].age !== undefined && inputBio[i].age !== ``)
            tempPagBio += `<li class="page-item active">
                        <button class="btn btn-warning" type="button" onclick="displayBio(${i},${pagActive})">${inputBio[i].age}</button>
                     </li>`
        else if (inputBio[i] !== undefined && inputBio[i].age !== undefined && inputBio[i].age !== ``) tempPagBio += `<li class="page-item">
                        <button class="btn btn-outline-warning" type="button" onclick="displayBio(${i},${pagActive})">${inputBio[i].age}</button>
                     </li>`
        else if (i === pagActive && i === countBio && inputBio[i] === undefined) tempPagBio += `<li class="page-item">
                        <button class="btn btn-warning" type="button" onclick="displayBio(${i},${pagActive})">новый</button>
                     </li>`
        else if (i === countBio && inputBio[i] === undefined) tempPagBio += `<li class="page-item">
                        <button class="btn btn-outline-warning" type="button" onclick="displayBio(${i},${pagActive})">новый</button>
                     </li>`
        else if (i === pagActive && (inputBio[i] === undefined || inputBio[i].age === ``)) tempPagBio += `<li class="page-item">
                        <button class="btn btn-warning" type="button" onclick="displayBio(${i},${pagActive})">!</button>
                     </li>`
        else if (inputBio[i] === undefined || inputBio[i].age === ``) tempPagBio += `<li class="page-item">
                        <button class="btn btn-outline-warning" type="button" onclick="displayBio(${i},${pagActive})">!</button>
                     </li>`
    }
    if (pagActive !== countBio) tempPagBio += `
                        <li class="page-item" id="navBioPrev">
                            <button class="btn btn-outline-warning" type="button" onclick="displayBio(${pagActive + 1},${pagActive})">Следующий</button>
                        </li>`
    else tempPagBio += `
                        <li class="page-item" id="navBioPrev">
                            <button class="btn btn-outline-secondary" type="button" disabled>Следующий</button>
                        </li>`
    tempPagBio += `
            </ul>
        </nav>`
    document.getElementById("pagan").innerHTML = tempPagBio;
}

function getFormToDrop(other, otherPhones, otherEmails) {
    const form = document.getElementById('baseFormAddFM');
    let formData = {
        primePhoto: primePhotoExist,
        id: (tempPerson !== null && tempPerson.id !== null) ? tempPerson.id : null,
        secretLevelPhoto: form.elements.chooseSecurePP.value,
        secretLevelEdit: form.elements.chooseSecureEE.value,
        secretLevelBirthday: form.elements.chooseSecureBirthday.value,
        secretLevelMainInfo: form.elements.chooseSecureMain.value,
        secretLevelRemove: form.elements.chooseSecureRM.value,
        firstName: form.elements.firstNameAddFM.value,
        middleName: form.elements.middleNameAddFM.value,
        lastName: form.elements.lastNameAddFM.value,
        birthday: form.elements.birthdayAddFM.value,
        deathday: form.elements.deathdayAddFM.value,
        sex: form.elements.chooseSexAddFM.value,
        memberInfo: {
            mainPhone: form.elements.mainPhoneAddFM.value,
            mainEmail: form.elements.mainEmailAddFM.value,
            photoBurialExist: burialPhotoExist,
            photoBirthExist: birthPhotoExist,
            secretLevelBirth: form.elements.chooseSecurePBur.value,
            secretLevelBurial: form.elements.chooseSecurePBirth.value,
            secretLevelEmail: form.elements.chooseSecureME.value,
            secretLevelAddress: form.elements.chooseSecureMA.value,
            secretLevelPhone: form.elements.chooseSecureMP.value,
            secretLevelBiometric: form.elements.chooseSecureBio.value,
            secretLevelDescription: form.elements.chooseSecureDes.value,
            biometric: inputBio,
            birth: {
                country: form.elements.birthCountryAddFM.value,
                region: form.elements.birthRegionAddFM.value,
                city: form.elements.birthCityAddFM.value,
                street: form.elements.birthStreetAddFM.value,
                birthHouse: form.elements.birthHouseAddFM.value,
                registration: form.elements.birthRegisterAddFM.value,
                photoExist: birthPhotoExist
            },
            burial: {
                country: form.elements.burialCountryAddFM.value,
                region: form.elements.burialRegionAddFM.value,
                city: form.elements.burialCityAddFM.value,
                cemetery: form.elements.burialCemeteryAddFM.value,
                street: form.elements.burialStreetAddFM.value,
                chapter: form.elements.burialHouseAddFM.value,
                square: form.elements.burialBuildingAddFM.value,
                grave: form.elements.burialFlatAddFM.value,
                photoExist: burialPhotoExist
            },
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
    if (tempPerson.memberInfo.photoBirthExist && tempPerson.memberInfo.secretLevelBirth !== "CLOSE") tempPerson.memberInfo.birthImj = await loadPicture("/file/get/birth/" + tempPerson.uuid);
    if (tempPerson.memberInfo.photoBurialExist && tempPerson.memberInfo.secretLevelBurial !== "CLOSE") tempPerson.memberInfo.burialImj = await loadPicture("/file/get/burial/" + tempPerson.uuid);
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