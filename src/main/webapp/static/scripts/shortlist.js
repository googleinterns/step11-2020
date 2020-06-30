const mentorCardContainer = document.getElementById("mentor-cards-container");
const sendButton = document.getElementById("send");
const SEND_TEXT = sendButton.innerText;
const cancelButton = document.getElementById("cancel");
const CANCEL_TEXT = cancelButton.innerText;

const getMiddleCard = () => {
  const boundingBox = mentorCardContainer.getBoundingClientRect();
  const middleCard = document.elementFromPoint(window.innerWidth/2,boundingBox.top + boundingBox.height / 2);
  return middleCard;
};

const updateChoiceButtons = () => {
  const middleCard = getMiddleCard();
  const name = middleCard.querySelector(".mentor-name").innerText;
  sendButton.innerText = `${SEND_TEXT} (${name})`;
  cancelButton.innerText = `${CANCEL_TEXT} (${name})`;
}
updateChoiceButtons();

mentorCardContainer.addEventListener("scroll", (event) => {
  updateChoiceButtons();
});

const buttonDict = {
  "send": sendButton,
  "cancel": cancelButton
}

for (const buttonName in buttonDict) {
  buttonDict[buttonName].addEventListener("click", async (event) => {
    const middleCard = getMiddleCard();
    const mentorID = middleCard.querySelector(".mentor-id").innerText;
    let response = await fetch("/shortlist", {
        method: "POST",
        body: new URLSearchParams({mentorID, choice: buttonName})
      });
    let text = await response.text();
    let data = JSON.parse(text);
    if (data.success) {
      mentorCardContainer.removeChild(middleCard);
    }
  });
}
