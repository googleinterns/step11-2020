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

const carouselContainer = document.querySelector("#mentor-cards-carousel");
const mentorCardContainer = carouselContainer.querySelector(".carousel-inner");
const sendRequestButton = document.querySelector("#send-request");
const SEND_REQUEST_TEXT = sendRequestButton.innerText;
const dislikeMentorButton = document.querySelector("#dislike-mentor");
const DISLIKE_MENTOR_TEXT = dislikeMentorButton.innerText;

const getActiveMentorCard = () => {
  let activeCard = mentorCardContainer.querySelector(".carousel-item.active");
  if (!activeCard && mentorCardContainer.children.length > 0) {
    mentorCardContainer.children[0].classList.add("active");
    activeCard = mentorCardContainer.querySelector(".carousel-item.active");
  }
  return activeCard;
};

const updateChoiceButtons = () => {
  const middleCard = getActiveMentorCard();
  sendRequestButton.blur();
  dislikeMentorButton.blur();
  if (!middleCard) {
    sendRequestButton.innerText = `${SEND_REQUEST_TEXT}`;
    dislikeMentorButton.innerText = `${DISLIKE_MENTOR_TEXT}`;
    return;
  }
  const name = middleCard.querySelector(".mentor-name").innerText;
  sendRequestButton.innerText = `${SEND_REQUEST_TEXT} (${name})`;
  dislikeMentorButton.innerText = `${DISLIKE_MENTOR_TEXT} (${name})`;
};

const refillMentors = async () => {
  let response = await fetch("/refill-mentor");
  let mentors = await response.json();
  let hasActive = getActiveMentorCard() !== null;
  mentors.forEach((mentor, i) => {
    let newCard = document.createElement("div");
    newCard.className = `carousel-item${i == 0 && !hasActive ? " active" : ""}`;
    newCard.innerHTML =
        `<div class="mentor-card px-5 py-4 mw-100 bg-light-blue border border-primary border-width-medium rounded-lg" id="${mentor.name}-card">
          <p class="mentor-id" hidden>${mentor.datastoreKey}</p>
          <h3 class="mentor-name">${mentor.name}</h3>
          <p class="mentor-dob">DOB: ${mentor.dateOfBirth}</p>
          <p class="mentor-country">Country: ${mentor.country.longName}</p>
          <p class="mentor-language">Language: ${mentor.language.longName}</p>
          <p class="mentor-timezone">TimeZone: ${mentor.timezone.name}: GMT ${(mentor.timezone.offset >= 0 ? "+" : "") + mentor.timezone.offset}</p>
          <p class="mentor-ethnicity">Ethnicity: ${mentor.ethnicityList.map(ethnicity => ethnicity.title !== "Other" ? ethnicity.title : mentor.ethnicityOther).join(", ")}</p>
          <p class="mentor-gender">Gender: ${mentor.gender.title !== "Other" ? mentor.gender.title : mentor.genderOther}</p>
          <p class="mentor-first-gen">First-gen: ${mentor.firstGen  ? "Yes" : "No"}</p>
          <p class="mentor-low-income">Low income: ${mentor.lowIncome  ? "Yes" : "No"}</p>
          <p class="mentor-education-level">Education: ${mentor.educationLevel.title != "Other" ? mentor.educationLevel.title : mentor.educationLevelOther}</p>
          <p class="mentor-description">${mentor.description}</p>
          <p class="mentor-mentor-type">${mentor.mentorType.title}</p>
          <p class="m-0 mentor-focus-label">${mentor.focusList.length === 0 ? "Focuses: None" : mentor.focusList.length === 1 ? `Focus: ${mentor.focusList[0].title}` : "Focuses:"}</p>
          <ul class="mentor-focus-list">
            ${mentor.focusList.length === 1 ? "" : mentor.focusList.map(focus => `<li class="mentor-focus-item">${focus.title}</li>`).join("\n")}
          </ul>
        </div>`;
    mentorCardContainer.appendChild(newCard);
  });
  updateChoiceButtons();
};
refillMentors();


$('#mentor-cards-carousel').on('slid.bs.carousel', function () {
  updateChoiceButtons();
});

const buttonDict = {
  "sendRequest": sendRequestButton,
  "dislikeMentor": dislikeMentorButton
}
const MIN_MENTOR_THRESHOLD = 5;
for (const buttonName in buttonDict) {
  buttonDict[buttonName].addEventListener("click", async (event) => {
    const middleCard = getActiveMentorCard();
    const mentorID = middleCard.querySelector(".mentor-id").innerText;
    let response = await fetch("/find-mentor", {
      method: "POST",
      body: new URLSearchParams({ mentorID, choice: buttonName })
    });
    let text = await response.text();
    let data = JSON.parse(text);
    if (data.success) {
      mentorCardContainer.removeChild(middleCard);
      if (mentorCardContainer.childElementCount < MIN_MENTOR_THRESHOLD) {
        refillMentors();
      }
      updateChoiceButtons();
    }
  });
}
