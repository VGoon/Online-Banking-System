var customerTableBody = document.getElementById("CustomerTableBody");
var employeeTableBody = document.getElementById("EmployeeTableBody");
var accountTableBody = document.getElementById("AccountTableBody");
var historyTableBody = document.getElementById("HistoryTableBody");

let mockObj = {
    "id": 5432,
    "first": "firstName",
    "last": "lastName",
    "email": "email@email.com",
    "username": "username",
    "freeze": false
}

window.onload = function(){
    populateUserTables();
    populateAccountTable();
    populateLogTable();
    userDetails();
}

function populateUserTables(){
    let xhr = new XMLHttpRequest();
    const url = "employee-portal-users";

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let customerList = JSON.parse(xhr.responseText);

                customerList.forEach(
                    element => {   
                        
                        if(element.userType == "Customer"){
                            populateCustomerTable(element);     
                        }else if(element.userType == "Employee"){
                            populateEmployeeTable(element);
                        }
                    });


            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function populateAccountTable(){
    let xhr = new XMLHttpRequest();
    const url = "employee-portal-accounts";

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){

                let accountList = JSON.parse(xhr.responseText);

                accountList.forEach(
                    element => {   
                        
                        addTableRow(element, accountTableBody);

                    });


            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function populateLogTable(){
    let xhr = new XMLHttpRequest();
    const url = "logging-events";

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let logList = JSON.parse(xhr.responseText);

                logList.forEach(
                    element => {   
                        addLogRow(element, historyTableBody);
                    });
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function addLogRow(element, table){
    let tr = document.createElement("tr");
    let id = document.createElement("td");
    let msg = document.createElement("td");
    id.innerHTML = element["id"];
    msg.innerHTML = element["message"];
    
    tr.appendChild(id);
    tr.appendChild(msg);

    table.appendChild(tr);
}

function populateCustomerTable(userObject){
    addTableRow(userObject, customerTableBody);
}

function populateEmployeeTable(userObject){
    addTableRow(userObject, employeeTableBody);
}

function addTableRow(tableElement, tableBody){
    let len = Object.keys(tableElement).length;

    if(tableElement.hasOwnProperty("userPassword"))
        len = len--;
    if(tableElement.hasOwnProperty("userType")) 
        len = len--;

    let tr = document.createElement("tr");

    let arr = [];

    for(let i = 0; i < len; i++){
        arr[i] = document.createElement("td");
    }

    let i = 0;
    for(let property in tableElement){
        if(property != "userPassword" && property != "userType"){
            arr[i].innerHTML = tableElement[property];
            tr.appendChild(arr[i]);
            i++;
        }
    }

    //if the customer table, add event listener
    if(tableBody.id == "CustomerTableBody")
        tr.addEventListener("click", rowClick);

    tableBody.appendChild(tr);
}

function rowClick(e){
    e.preventDefault();

    let element;
    if(e.target.nodeName == "TD"){
        element = e.target.parentElement.firstChild;
    }

    console.log("Row click");
    
    let xhr = new XMLHttpRequest();
    const url = "employee-portal-viewuser";
    let data = new FormData();
    data.append("id", element.innerHTML);

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                console.log("Should redirect");
                window.location.href = "http://localhost:9000/EmployeeViewUserAccount.html";
            }
        }
    }
    xhr.open("POST", url);
    xhr.send(data);
}

//HEADER POPULATION

let headername = document.getElementById("header_name");
let headerUsername = document.getElementById("header_username");
let headerUserid = document.getElementById("header_id");
let firstName = document.getElementById("header_firstName");

let signOffBtn = document.getElementById("header-signoff-button");
signOffBtn.addEventListener("click", function(){
    let xhr = new XMLHttpRequest();
    const url = "logout-path";

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                window.location.href = "http://localhost:9000/LoginPageEmployee.html";
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
});

function userDetails(){
    let xhr = new XMLHttpRequest();
    const url = "employee-details";

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let userDetails = JSON.parse(xhr.responseText);
                populateHeader(userDetails);
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function populateHeader(userDetails){
    firstName.innerHTML = userDetails["firstName"];
    headername.innerHTML = "Name: " + userDetails["lastName"] + ", " + userDetails["firstName"];
    headerUsername.innerHTML = "Username: " + userDetails["userName"];
    headerUserid.innerHTML = "UserID #" + userDetails["id"];
}

