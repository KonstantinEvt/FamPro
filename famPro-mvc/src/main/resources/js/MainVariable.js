let defaultPhotos=await loadDefaultPhotos();
console.log("DefaultPhotos are loaded", defaultPhotos);
let contacts = await getContacts(0);
console.log("Contacts are loaded", contacts);
let contactImages =await loadContactsImage(contacts,defaultPhotos);
console.log("Contacts image are loaded", contactImages);
console.log("Contacts are loaded", contacts);

window.contacts=contacts;
window.defaultPhotos=defaultPhotos;