function getCommunication() {
    loadStandardMainPanel();
    document.getElementById("taskPart").innerHTML = `    
    <br>
    <div style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: chocolate">Написать:</div>
    <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px; margin-right: -5px">
        <input type="radio" class="btn-check" name="news-radio" onclick="sendMessage()" id="news-radio1" autoComplete="off" checked>
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio1">Портал
        </label>
        <input type="radio" class="btn-check" name="news-radio" onclick="" id="news-radio2" autoComplete="off">
        <label class="btn btn-outline-warning" style="padding-right: 0; padding-left: 0; text-align:center; font-size: 14px; color: darkred" for="news-radio2">Email           
        </label>            
    </div>`
    loadNewsCounts();
    sendMessage();
}

function sendMessage() {
    document.getElementById("resultPart").innerHTML = `
<div style="text-align: center; color: chocolate;font-family: 'Times New Roman',serif; font-size: 18px">*** Отправка сообщения ***</div>
<form class="form-group" style="margin:5px; text-align: center" id="messageForm">
    <div class="container-fluid row mh-100 no-gutters">
        <span class="col" style="width: 20%"></span>
        <span class="col-6" style="min-width: 250px">
            <label for="subjectCreat" style="color: chocolate; padding-top: 5px">Тема:</label>
            <input class="form-control" type="text" id="subjectCreat" name="subjectCreat" required>
            <label for="sendingTo" style="color: chocolate; padding-top: 5px">Получатель:</label>
            <input class="form-control" type="text" id="sendingTo" name="sendingTo" required>
            <label for="sendingText" style="color: chocolate; padding-top: 5px">text:</label>
            <textarea rows="5" cols="50" class="form-control" type="text" id="sendingText" name="sendingText" required></textarea>
            <label for="chooseCategory" style="color: chocolate; padding-top: 5px">Category:</label>
            <select class="form-select" id="chooseCategory" aria-label="chooseCategory">            
                <option value="PRIVATE" selected>Private</option>
                <option value="SYSTEM">System</option>
                <option value="FAMILY">Family</option>
                <option value="COMMON">Common</option>
            </select>
        </span>
        <span class="col" style="width: 20%"></span>
    </div>  
    <br>
    <label for="newsPhoto" style="color: chocolate;">Файл фото</label>
    <input class="form-control" type="file" style="font-size: 10px" id="newsPhoto" name="newsPhoto" value="DDD">
    <br>
    <button class="btn btn-outline-warning" style="color: darkred" type="button" onclick="submitMessage()">Отправить</button>
</form>
<div style="font-size: 20px; text-align: center">Статус операции: <span id="resultListCreateMessage"></span></div>
<div style="font-size: larger; font-family: 'Times New Roman',serif; text-align: center">Photo Result: 
        <span style="font-size: larger;font-family: bold,'Monotype Corsiva',serif" id="resultSaveNewsPhoto"></span></div>
    <br>
`
}

function submitMessage() {
    const form = document.getElementById('messageForm');
    if (document.getElementById("newsPhoto").files[0] != null) {
        let newsPhoto = document.getElementById("newsPhoto").files[0];
        let file = new FormData()
        let category=document.getElementById("chooseCategory").value;
        file.append("newsPhoto", newsPhoto);
        file.append("name", document.getElementById("subjectCreat").value)
        file.append("bucket", category)
        fetch(`/file/saveNewsPhoto`, {
            method: 'POST',
            headers: {},
            body: file,
        }).then(async status => {
            document.getElementById("resultSaveNewsPhoto").innerHTML = await status.text();
        });
    } else document.getElementById("resultSaveNewsPhoto").innerHTML = 'News photo not selected!';
    let formData = {
        subject: form.elements.subjectCreat.value,
        textInfo: form.elements.sendingText.value,
        sendingTo: form.elements.sendingTo.value,
        category: form.elements.chooseCategory.value
    }

    const jsonData = JSON.stringify(formData);
    fetch("/message/sendMessage", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData,
    }).then(async status => {
        document.getElementById("resultListCreateMessage").innerHTML = await status.text();
    });
}