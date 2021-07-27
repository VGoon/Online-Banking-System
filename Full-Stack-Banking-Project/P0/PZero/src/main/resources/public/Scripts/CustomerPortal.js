
let accountBody = document.getElementById("accountBody");

let logoutBtn = document.getElementById("logoutBtn");

logoutBtn.addEventListener("click", function(){
    let xhr = new XMLHttpRequest();
    const url = "logout-path";
 
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                 window.location.href = "http://localhost:9000/LoginPageCustomer.html";
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
});

const accountHTML = "<div class=\"account\"><div class=\"account-header\"><div class=\"account-details\"><h5 id=\"nickname\">Ben's Checking Account</h5>"
    +"<p id=\"accountID\">XXXXXXXXX1234</p></div><div class=\"account-amount\"><h1 id=\"balance\">$1020.50</h1></div></div>"
    +"<div class=\"account-footer\"><ul class=\"nav\"><li class=\"nav-item\"><a class=\"nav-link\" id=\"withdraw\"href=\"#\">Withdraw</a></li><li class=\"nav-item\">"
    +"<a class=\"nav-link\" id=\"deposit\" href=\"#\">Deposit</a></li></ul></div></div>";


requestNewAccount = document.getElementById("requestAccount")
requestNewAccount.addEventListener("click", function(e){
   let xhr = new XMLHttpRequest();
   const url = "customer-request-account";

   xhr.onreadystatechange = function(){
       if(xhr.readyState == 4){
           if(xhr.status == 200){
                window.location.href = "http://localhost:9000/CustomerPortal.html";
           }
       }
   }
   xhr.open("GET", url);
   xhr.send();
});

let transferBtn = document.getElementById("transferButton");
transferBtn.addEventListener("click", function(e){
    window.location.href = "http://localhost:9000/TransferPage.html";
});

window.onload = function(){
    getUser();
    reloadAccounts();
};

function getUser(){
    let xhr = new XMLHttpRequest();
    const url = "customer-details";
    // console.log("Here i am.");

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let userDetails = JSON.parse(xhr.responseText);
                populateUserDetails(userDetails);
                populateHeader(userDetails);
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function reloadAccounts(){
    // populate the window with the cookie from the previous page (EmployeeAccount)
    let xhr = new XMLHttpRequest();
    const url = "customer-accounts";
    
    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                let accountList = JSON.parse(xhr.responseText);
    
                accountList.forEach(
                    element => {   
                        addAccountToTable(element, accountBody);
                    });
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function populateUserDetails(userDetails){
    let fullname = document.getElementsByClassName("wholeName");

    let n = userDetails["lastName"] + ", " + userDetails["firstName"];
    fullname[0].innerHTML = n;
}

function addAccountToTable(element, tableBody){
    let account = new DOMParser().parseFromString(accountHTML, "text/html");
    let balance = account.getElementById("balance");
    let nickname = account.getElementById("nickname");
    let accountID = account.getElementById("accountID");
    let withdraw = account.getElementById("withdraw");
    let deposit = account.getElementById("deposit");

    let nicknameStr = "CHECKING ACCOUNT";
    let accountNumber = "XXXXXXXXXX";

    if(element["nickName"] == ""){
        nickname.innerHTML = nicknameStr;
    }else{
        nickname.innerHTML = element["nickName"].toUpperCase();
    }

    //place in money format
    let n = parseFloat(element["balance"], 10).toFixed(2);
    let dollarUSLocale = Intl.NumberFormat('en-US');
    dollarUSLocale.format(n);
    balance.innerHTML = "$" + n;


    if(element["approved"]){
        accountID.innerHTML = accountNumber + element["id"];
    }else{
        accountID.innerHTML = accountNumber + element["id"] + "  (FROZEN)";
    }

    withdraw.addEventListener("click", withdrawMoney);
    deposit.addEventListener("click", depositMoney);

    account = account.documentElement;

    accountBody.appendChild(account);
}

function withdrawMoney(e){
    e.preventDefault();

    let element = e.target.parentElement.parentElement.parentElement.parentElement.firstChild.firstChild.lastChild;

    //regex for getting only numbers
    var regex = /\d+/g;
    element = element.innerHTML.match( regex ).join([]);

    let xhr = new XMLHttpRequest();
    const url = "customer-withdraw-store";
    let data = new FormData();
    data.append("id", element);

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                console.log("Should redirect");
                window.location.href = "http://localhost:9000/WithdrawPage.html";
            }
        }
    }
    xhr.open("POST", url);
    xhr.send(data);
}

function depositMoney(e){
    e.preventDefault();

    let element = e.target.parentElement.parentElement.parentElement.parentElement.firstChild.firstChild.lastChild;

    //regex for getting only numbers
    var regex = /\d+/g;
    element = element.innerHTML.match( regex ).join([]);
    
    let xhr = new XMLHttpRequest();
    const url = "customer-deposit-store";
    let data = new FormData();
    data.append("id", element);

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){
                console.log("Should redirect");
                window.location.href = "http://localhost:9000/DepositPage.html";
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
                window.location.href = "http://localhost:9000/LoginPageCustomer.html";
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
});

function populateHeader(userDetails){
    firstName.innerHTML = userDetails["firstName"];
    headername.innerHTML = "Name: " + userDetails["lastName"] + ", " + userDetails["firstName"];
    headerUsername.innerHTML = "Username: " + userDetails["userName"];
    headerUserid.innerHTML = "UserID #" + userDetails["id"];
}