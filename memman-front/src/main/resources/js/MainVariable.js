let defaultPhotos;
let contacts=new Map([]);
let contactImages
await loadDefaultPhotos().then((f)=>{defaultPhotos=f; console.log("DefaultPhotos are loaded",f)});
// document.querySelectorAll('a').forEach(link => {
//     link.addEventListener('keydown', function(event) {
//         if (event.keyCode === 13) {
//             event.preventDefault();
//         }
//     });
// });
document.body.addEventListener('keydown', function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
        }
});
window.addEventListener('hide.bs.modal', event => {
    event.target.inert = true
})
window.addEventListener('show.bs.modal', event => {
    event.target.inert = false
})
window.contacts=contacts;
window.defaultPhotos=defaultPhotos;