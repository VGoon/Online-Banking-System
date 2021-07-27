
let fromDropDown = document.getElementById("from-account-select");

let cancelBtn = document.getElementById("cancel-button");

cancelBtn.addEventListener("click", function(e){
    window.location.href = "http://localhost:9000/CustomerPortal.html";
});

window.onload = function(){
    populateDropDowns();
};

function populateDropDowns(){
    let xhr = new XMLHttpRequest();
    const url = "customer-accounts";

    xhr.onreadystatechange = function(){
        if(xhr.readyState == 4){
            if(xhr.status == 200){

                let accountList = JSON.parse(xhr.responseText);
                accountList.forEach(
                    element => {   
                        if(element["approved"] == true)
                            addToDropDown(element, fromDropDown);
                    });
            }
        }
    }
    xhr.open("GET", url);
    xhr.send();
}

function addToDropDown(element, body){
    let option = document.createElement("option");

    let balance = moneyView(element["balance"]);

    if(element["nickname"] == ""){
        option.innerHTML = "CHECKING ...XX" + element["id"] + " (Available balance = " + balance + ")";
    }else{
        option.innerHTML = element["nickName"].toUpperCase() + " ...XX" + element["id"] + " (Available balance = " + balance + ")";
    }

    option.value = element["id"];
    body.options.add(option);
}


function moneyView(num){
    //place in money format
    let n = parseFloat(num, 10).toFixed(2);
    let dollarUSLocale = Intl.NumberFormat('en-US');
    dollarUSLocale.format(n);
    return "$" + n;
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

function userDetails(){
    let xhr = new XMLHttpRequest();
    const url = "customer-details";

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