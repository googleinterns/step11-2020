const mentorCardContainer = document.getElementById("mentor-cards-container");
const yesButton = document.getElementById("yes");
const YES_TEXT = yesButton.innerText;
const maybeButton = document.getElementById("maybe");
const MAYBE_TEXT = maybeButton.innerText;
const noButton = document.getElementById("no");
const NO_TEXT = noButton.innerText;

const getMiddleCard = () => {
  const boundingBox = mentorCardContainer.getBoundingClientRect();
  const middleCard = document.elementFromPoint(window.innerWidth/2,boundingBox.top + boundingBox.height / 2);
  return middleCard;
};

const updateChoiceButtons = () => {
  const middleCard = getMiddleCard();
  const name = middleCard.querySelector(".mentor-name").innerText;
  yesButton.innerText = `${YES_TEXT} (${name})`;
  maybeButton.innerText = `${MAYBE_TEXT} (${name})`;
  noButton.innerText = `${NO_TEXT} (${name})`;
}
updateChoiceButtons();

mentorCardContainer.addEventListener("scroll", (event) => {
  updateChoiceButtons();
});

const buttonDict = {
  "yes": yesButton,
  "maybe": maybeButton,
  "no": noButton
}

for (const buttonName in buttonDict) {
  buttonDict[buttonName].addEventListener("click", async (event) => {
    const middleCard = getMiddleCard();
    const mentorID = middleCard.querySelector(".mentor-id").innerText;
    let response = await fetch("/find-mentor", {
        method: "POST",
        body: new URLSearchParams({mentorID, choice: buttonName})
      });
    let text = await response.text();
    let data = JSON.parse(text);
    console.log(data);
  });
}
