const menteeCardContainer = document.getElementById("mentee-card-container");
Array.prototype.forEach.call(document.querySelectorAll(".mentee-card"), (menteeCard) => {
  const acceptButton = menteeCard.querySelector("button#yes");
  const denyButton = menteeCard.querySelector("button#no");
  acceptButton.addEventListener("click", async (event) => {
    const requestID = menteeCard.querySelector(".request-id").innerText;
    let success = await sendRequestDecision(requestID, "accept");
    if (success) {
      menteeCardContainer.removeChild(menteeCard);
    }
  });
  denyButton.addEventListener("click", async (event) => {
    const requestID = menteeCard.querySelector(".request-id").innerText;
    let success = await sendRequestDecision(requestID, "deny");
    if (success) {
      menteeCardContainer.removeChild(menteeCard);
    }
  });
});

const sendRequestDecision = async (requestID, choice) => {
  let response = await fetch("/connection-requests", {
      method: "POST",
      body: new URLSearchParams({requestID, choice})
    });
  let text = await response.text();
  let data = JSON.parse(text);
  return data.success;
}
