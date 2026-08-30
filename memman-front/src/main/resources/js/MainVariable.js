let defaultPhotos;
let contacts;
let contactImages
await loadDefaultPhotos().then((f)=>{defaultPhotos=f; console.log("DefaultPhotos are loaded",f)});
await getContacts(0)
    .then(c=>{contacts=c;console.log("Contacts are loaded")})
    .then(()=>{if (contacts!==null&&contacts.length>0) loadContactsImage(contacts,defaultPhotos)
        .then(ci=>console.log("Contacts image are loaded"))});
window.addEventListener('hide.bs.modal', event => {
    event.target.inert = true
})
window.addEventListener('show.bs.modal', event => {
    event.target.inert = false
})
window.contacts=contacts;
window.defaultPhotos=defaultPhotos;