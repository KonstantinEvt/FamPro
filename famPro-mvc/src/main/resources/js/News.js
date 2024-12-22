function getNews(){
    loadStandardMainPanel();
document.getElementById("resultPart").innerHTML=`<br> тут ленты событий`;
document.getElementById("taskPart").innerHTML=    `<br>
<ul class="nav flex-column">
  <li class="nav-item">
    <a class="nav-link" style="font-family: 'Times New Roman', serif;" aria-disabled="true" >Семья</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" style="font-family: 'Times New Roman', serif; " aria-disabled="true">Система</a>
  </li>
  <li class="nav-item">
    <a class="nav-link active" style="font-family: 'Times New Roman', serif;" aria-disabled="true">Личная</a>

  <li class="nav-item">
    <a class="nav-link disabled" style="font-family: 'Times New Roman', serif; color: grey" aria-disabled="true">Any</a>
  </li>
</ul>`}