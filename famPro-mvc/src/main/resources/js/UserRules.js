function commonRules(){
    loadStandardMainPanel();
    document.getElementById("resultPart").innerHTML=`<br> Как и везде тут тоже существуют правила и их надо стараться соблюдать :)`;
    document.getElementById("taskPart").innerHTML=    `<br>
<ul class="nav flex-column">
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: chocolate" href="#" >Rules</a>
  </li>
  <div style="margin-left:15px">__________</div>
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="roleRules()">Roles</a>
  </li>
  <li class="nav-item">
    <a class="nav-link " style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="seoRules()" >SEO</a>
  </li>
  <li class="nav-item">
    <a class="nav-link active" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" aria-current="page" onclick="baseRules()">Base</a>
  </li>
  <li class="nav-item">
    <a class="nav-link disabled" style="margin-left:15px; font-family: 'Times New Roman', serif; color: grey" aria-disabled="true">Any</a>
  </li>
</ul>`}
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
document.getElementById("taskPart").innerHTML=    `<br>
<ul class="nav flex-column">
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="commonRules()" >Rules</a>
  </li>
    <div style="margin-left:15px">__________</div>
  <li class="nav-item">
    <a class="nav-link active" style="margin-left:15px; font-family: 'Times New Roman', serif; color: chocolate" aria-current="page" >Roles</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="seoRules()" >SEO</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="baseRules()" >Base</a>
  </li>
  <li class="nav-item">
    <a class="nav-link disabled" style="margin-left:15px; font-family: 'Times New Roman', serif; color: grey" aria-disabled="true">Any</a>
  </li>
</ul>`
}
function seoRules(){
    loadStandardMainPanel();
    document.getElementById("resultPart").innerHTML=`<br> тут надо писать про авторизаию`;
    document.getElementById("taskPart").innerHTML=    `<br>
<ul class="nav flex-column">
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="commonRules()" >Rules</a>
  </li>
    <div style="margin-left:15px">__________</div>
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="roleRules()">Roles</a>
  </li>
  <li class="nav-item">
    <a class="nav-link active" style="margin-left:15px; font-family: 'Times New Roman', serif; color: chocolate" aria-current="page" href="#">SEO</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="baseRules()">Base</a>
  </li>
  <li class="nav-item">
    <a class="nav-link disabled" style="margin-left:15px; font-family: 'Times New Roman', serif; color: grey" aria-disabled="true">Any</a>
  </li>
</ul>`
}
function baseRules(){
    loadStandardMainPanel();
    document.getElementById("resultPart").innerHTML=`<br> тут надо писать про базу`;
    document.getElementById("taskPart").innerHTML=    `<br>
<ul class="nav flex-column">
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="commonRules()" >Rules</a>
  </li>
    <div style="margin-left:15px">__________</div>
  <li class="nav-item">
    <a class="nav-link" style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="roleRules()">Roles</a>
  </li>
  <li class="nav-item">
    <a class="nav-link " style="margin-left:15px; font-family: 'Times New Roman', serif; color: black" onclick="seoRules()" >SEO</a>
  </li>
  <li class="nav-item">
    <a class="nav-link active" style="margin-left:15px; font-family: 'Times New Roman', serif; color: chocolate" aria-current="page" href="#">Base</a>
  </li>
  <li class="nav-item">
    <a class="nav-link disabled" style="margin-left:15px; font-family: 'Times New Roman', serif; color: grey" aria-disabled="true">Any</a>
  </li>
</ul>`}