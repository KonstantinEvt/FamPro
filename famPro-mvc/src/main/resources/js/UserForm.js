let response = await fetch("http://localhost:8080/api/users/auth", {
    method: "get",
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*"
    }
});
let user = await response.json();

document.getElementById("nav1").innerHTML = user.username;
document.getElementById("nav2").innerHTML = user.roles.map(role => role.role).join(' ');

let temp = '';
temp += `<tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => role.role).join(' ')}</td>
            </tr>`;
document.getElementById("user-info").innerHTML = `${temp}`;


