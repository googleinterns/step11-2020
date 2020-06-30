const btnJoinAsMentee = document.getElementById("join-as-mentee");
const btnJoinAsMentor = document.getElementById("join-as-mentor");

btnJoinAsMentee.addEventListener("click", (event) => {
  window.location.pathname = "/questionnaire?formType=mentee";
});

btnJoinAsMentor.addEventListener("click", (event) => {
  window.location.pathname = "/questionnaire?formType=mentor";
});

const loginButton = document.getElementById("login-button");
loginButton.addEventListener("click", (event) => {
  window.location.pathname = "/authenticate";
});
