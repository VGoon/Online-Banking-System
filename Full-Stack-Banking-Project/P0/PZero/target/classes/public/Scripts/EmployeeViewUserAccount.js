var userAccounts = document.getElementById("tableBody");

let backBtn = document.getElementById("backBtn");
backBtn.addEventListener("click", function(e){
    e.preventDefault();
    let xhr = new XMLHttpRequest();
    const url = "employee-back-button";
    
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                window.location.href = "http://localhost:9000/EmployeePortal.html";
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();    
});

window.onload = function(){
    retrieveUser();
    retireveUserInformation();
}

function retrieveUser(){
    let xhr = new XMLHttpRequest();
    const url = "employee-portal-view-user-page-userdetails";
    
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let userDetails = JSON.parse(xhr.responseText);
                console.log(userDetails);
                populateUserDetails(userDetails);
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function retireveUserInformation(){
    // populate the window with the cookie from the previous page (EmployeeAccount)
    let xhr = new XMLHttpRequest();
    console.log("VIEWING ACCOUNT PAGE");
    const url = "employee-portal-view-user-page";
    
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let accountList = JSON.parse(xhr.responseText);
    
                accountList.forEach(
                    element => {   
                        addTableRow(element, userAccounts);
                    });
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

//pass in basically a user object
function populateUserDetails(userDetails){
    let fullname = document.getElementsByClassName("wholeName");
    let username = document.getElementsByClassName("username");
    let email = document.getElementsByClassName("email");
    let userid = document.getElementsByClassName("userID");

    let n = userDetails["lastName"] + ", " + userDetails["firstName"];
    fullname[0].innerHTML = n;

    email[0].innerHTML = userDetails["email"];
    username[0].innerHTML = userDetails["userName"];
    userid[0].innerHTML = userDetails["id"];
}

function addTableRow(tableElement, tableBody){
    //table row
    let tr = document.createElement("tr");

    //table td's
    let id = document.createElement("td");
    let nickname = document.createElement("td");
    let balance = document.createElement("td");
    let unlock = document.createElement("td");
    let closeAccount = document.createElement("td");

    id.innerHTML = tableElement["id"];
    nickname.innerHTML = tableElement["nickName"];
    balance.innerHTML = tableElement["balance"];

    //adding unlock button
    let btnUnlock = document.createElement("button");
    if(tableElement["approved"] == true){
        btnUnlock.innerHTML = "Lock?";
    }else{
        btnUnlock.innerHTML = "Unlock?";
    }
    btnUnlock.className = "btn btn-secondary";
    btnUnlock.addEventListener("click", toggleLock);
    unlock.appendChild(btnUnlock);

    //adding close account button
    let btnClose = document.createElement("button");
    btnClose.innerHTML = "Close Account";
    btnClose.className = "btn btn-secondary";
    btnClose.addEventListener("click", close);
    closeAccount.appendChild(btnClose);

    //adding all td's to row
    tr.appendChild(id);
    tr.appendChild(nickname);
    tr.appendChild(balance);
    tr.appendChild(unlock);
    tr.appendChild(closeAccount);

    //adding row to the table
    tableBody.appendChild(tr);
}

//close account if the account has no balance and is locked
function close(e){
    console.log("close account clicked.");
    console.log(e.target);
    let id = e.target.parentElement.parentElement.children[0];
    closeAccount(id.innerHTML);
}

//lock / unlock the account
function toggleLock(e){
    let id = e.target.parentElement.parentElement.children[0];
    accountApproval(id.innerHTML, e.target);
}

function accountApproval(accountID, lockBtn){
    let xhr = new XMLHttpRequest();
    const url = "employee-toggle-approval";

    let data = new FormData();
    data.append("id", accountID);
        
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                if(lockBtn.innerHTML == "Lock?")
                    lockBtn.innerHTML = "Unlock?";
                else
                    lockBtn.innerHTML = "Lock?";
            }
        }
    }
    xhr.open("POST", url);
    xhr.send(data);
}

function closeAccount(accountId){
    let xhr = new XMLHttpRequest();
    const url = "employee-delete-account";

    let data = new FormData();
    data.append("id", accountId);
        
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let newTable = document.createElement('tbody');
                userAccounts.parentNode.replaceChild(newTable, userAccounts);
                userAccounts = newTable;
                retireveUserInformation();
            }
            if(xhr.status == 418){
                console.log("Account couldn't be deleted.");
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