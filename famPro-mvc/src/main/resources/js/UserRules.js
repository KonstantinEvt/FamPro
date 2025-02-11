function systemRules(){
    loadStandardMainPanel();
    document.getElementById("resultPart").innerHTML=`<br> Как и везде тут тоже существуют правила и их надо стараться соблюдать :)`;

    document.getElementById("taskPart").innerHTML = `    
    <br>
    <div style="font-family: 'Times New Roman', serif; font-size: 16px; text-align: center; color: chocolate">Rules:</div>
    <div class="btn-group-vertical" role="group" aria-label="Vertical button group" style="margin-left: 5px">
        <input type="radio" class="btn-check" name="rules-radio" onclick="commonRules()" id="rules-radio1" autoComplete="off" checked>
        <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="rules-radio1">Roles </label>
        <input type="radio" class="btn-check" name="rules-radio" onclick="seoRules()" id="rules-radio2" autoComplete="off">
        <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="rules-radio2">SEO </label>
        <input type="radio" class="btn-check" name="rules-radio" id="rules-radio3" onclick="baseRules()" autoComplete="off">
        <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="rules-radio3">Base</label>
                <input type="radio" class="btn-check" name="rules-radio" id="rules-radio4" onclick="roleRules()" autoComplete="off">
        <label class="btn btn-outline-warning" style="font-size: 14px; color: darkred" for="rules-radio4">Other</label>
    </div>
`
}
function roleRules() {

    let text = `
<br>
<div style="text-align: center">В системе присутствует несколько ролей, которые дают следующие права:</div>
<div style="font-family: 'Times New Roman', serif; color: chocolate" >Admin: </div>
<span>Права:</span>
<span>все права</span>
<br>
    <span>Как получить:</span>
    <span>Никак</span>
    <div style="font-family: 'Times New Roman',serif; color: chocolate">Manager: </div>
    <span>Права:</span>
    <span>редактирование любых записей в базе, возможность перевести запись в состояние "MANAGE" в котором она не просматривается другими ролями </span>
    <br>
        <span>Как получить:</span>
        <span>По договоренности с администрацией</span>
        <br>
            <div style="font-family: 'Times New Roman',serif; color: chocolate">VIP: </div>
            <span>Права:</span>
            <span>Расширенные возможности по хранению файлов</span>
            <br>
                <span>Как получить:</span>
                <span>Иметь роль LinkedUser. Купить</span>
                <br>
                    <div style="font-family: 'Times New Roman',serif; color: chocolate">LinkedUser: </div>
                    <span>Права:</span>
                    <span>Возможность вносить уровень секретности к данным в связанной записи базы</span>
                    <br>
                        <span>Как получить:</span>
                        <span>Иметь роль BaseUser. Связать свою учетную запись с записью в базе</span>
                        <br>
                            <div style="font-family: 'Times New Roman',serif; color: chocolate">BasedUser: </div>
                            <span>Права:</span>
                            <span>Полноценный доступ к базе</span>
                            <br>
                                <span>Как получить:</span>
                                <span>Иметь роль SimpleUser. Подтвердить Email</span>
                                <br>
                                    <div style="font-family: 'Times New Roman',serif; color: chocolate">SimpleUser: </div>
                                    <span>Права:</span>
                                    <span>Доступ к базе</span>
                                    <br>
                                        <span>Как получить:</span>
                                        <span>Создать в учетную запись (зарегистрироваться)</span>
                                        <br>'`;
document.getElementById("resultPart").innerHTML=text;

}
function otherRules(){
    document.getElementById("resultPart").innerHTML=`<br> тут надо писать про авторизаию`;

}
function seoRules(){
    document.getElementById("resultPart").innerHTML=`<br> тут надо писать про авторизаию`;

}
function baseRules(){
    document.getElementById("resultPart").innerHTML=`<br> тут надо писать про базу`;
 }
function commonRules(){
    document.getElementById("resultPart").innerHTML=`<br> Как и везде тут тоже существуют правила и их надо стараться соблюдать :)`;

}