// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

const mentorCardContainer = document.getElementById("mentor-cards-container");
const sendRequestButton = document.getElementById("send-request");
const SEND_REQUEST_TEXT = sendRequestButton.innerText;
const dislikeMentorButton = document.getElementById("dislike-mentor");
const DISLIKE_MENTOR_TEXT = dislikeMentorButton.innerText;

const getMiddleCard = () => {
  const mentorCards = mentorCardContainer.children;
  if (mentorCards.length > 1) {
    const boundingBox = mentorCardContainer.getBoundingClientRect();
    let middleCard = document.elementFromPoint(window.innerWidth / 2, boundingBox.top + boundingBox.height / 2);
    while (middleCard.className != "mentor-card") {
      middleCard = middleCard.parentElement;
    }
    return middleCard;
  }
  return mentorCards[0];
};
const refillMentors = async () => {
  console.log("calling refillMentors");
  let response = await fetch("/refill-mentor", {
    method: "GET"
  });
  let mentors = await response.json();
  console.log(JSON.stringify(mentors));
  console.log("got mentors");
  let i = 0;
  for (i = 0; i < mentors.length; i++) {
    let mentor = mentors[i];
    let newCard = document.createElement("div");
    newCard.class = "mentor-card";
    console.log(JSON.stringify(mentor));
    newCard.innerHTML = "<h3>" + mentor.name + "</h3>";
    mentorCardContainer.appendChild(newCard);
  }
};
const updateChoiceButtons = () => {
  const middleCard = getMiddleCard();
  if (!middleCard) {
    sendRequestButton.innerText = `${SEND_REQUEST_TEXT}`;
    dislikeMentorButton.innerText = `${DISLIKE_MENTOR_TEXT}`;
    return;
  }
  const name = middleCard.querySelector(".mentor-name").innerText;
  sendRequestButton.innerText = `${SEND_REQUEST_TEXT} (${name})`;
  dislikeMentorButton.innerText = `${DISLIKE_MENTOR_TEXT} (${name})`;
}
updateChoiceButtons();

mentorCardContainer.addEventListener("scroll", (event) => {
  updateChoiceButtons();
});

const buttonDict = {
  "sendRequest": sendRequestButton,
  "dislikeMentor": dislikeMentorButton
}

for (const buttonName in buttonDict) {
  buttonDict[buttonName].addEventListener("click", async (event) => {
    const middleCard = getMiddleCard();
    const mentorID = middleCard.querySelector(".mentor-id").innerText;
    let response = await fetch("/find-mentor", {
      method: "POST",
      body: new URLSearchParams({ mentorID, choice: buttonName })
    });
    let text = await response.text();
    let data = JSON.parse(text);
    if (data.success) {
      mentorCardContainer.removeChild(middleCard);
      updateChoiceButtons();
      if (mentorCardContainer.childElementCount < 5) {
        refillMentors();
      }
    }
  });
}
