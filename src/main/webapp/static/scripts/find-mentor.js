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
const dislikeMentorButton = document.querySelector("#dislike-mentor");

const getActiveMentorCard = () => {
  let activeCard = mentorCardContainer.querySelector(".carousel-item.active");
  if (!activeCard && mentorCardContainer.children.length > 0) {
    mentorCardContainer.children[0].classList.add("active");
    activeCard = mentorCardContainer.querySelector(".carousel-item.active");
  }
  return activeCard;
};

const newMentorCardHTML = mentor =>
  `<div class="mentor-profile-card shadow-sm mx-1 mb-1 position-relative" id="mentor-${mentor.datastoreKey}-card">
    <a class="stretched-link d-none" href="/profile?userID=${mentor.userID}"></a>
    <p class="mentor-id d-none" hidden>${mentor.datastoreKey}</p>
    <main class="container divider-top p-4 d-flex flex-row flex-wrap align-items-start justify-content-center">
      <div class="d-flex flex-column align-items-start justify-content-start mb-3 mr-md-3 mw-md-50" id="profile-title" >
        <h1 class="mb-0">${mentor.name}</h1>
        <h6 class="mb-2">${!mentor.visibility ? "Not a" : "A"}vailable for mentorship</h6>
        <p class="mentor-mentor-type">${mentor.mentorType.title}</p>
        <p class="m-0 mentor-focus-label">${mentor.focusList.length === 0 ? "Focuses: None" : mentor.focusList.length === 1 ? `Focus: ${mentor.focusList[0].title}` : "Focuses:"}</p>
        <ul class="mentor-focus-list">
          ${mentor.focusList.length === 1 ? "" : mentor.focusList.map(focus => `<li class="mentor-focus-item">${focus.title}</li>`).join("\n")}
        </ul>
        <p class="m-0">Bio:</p>
        <p class="mentor-description">${mentor.description}</p>
      </div>
      <div class="d-flex flex-column align-items-start justify-content-start mw-md-50">
        <div class="stats">
          <p class="mb-1 mentor-age">Age: ${mentor.age}</p>
          <p class="mb-1 mentor-country">Country: ${mentor.country.longName}</p>
          <p class="mb-1 mentor-language">Language: ${mentor.language.longName}</p>
          <p class="mb-1 mentor-timezone">TimeZone: ${mentor.timezone.name}: GMT ${(mentor.timezone.offset >= 0 ? "+" : "") + mentor.timezone.offset }</p>
          <p class="mb-1 mentor-ethnicity">Ethnicity: ${mentor.ethnicityList.map(ethnicity => ethnicity.title !== "Other" ? ethnicity.title : mentor.ethnicityOther).join(", ")}</p>
          <p class="mb-1 mentor-gender">Gender: ${mentor.gender.title !== "Other" ? mentor.gender.title : mentor.genderOther}</p>
          <p class="mb-1 mentor-first-gen">First-gen: ${mentor.firstGen  ? "Yes" : "No"}</p>
          <p class="mb-1 mentor-low-income">Low income: ${mentor.lowIncome  ? "Yes" : "No"}</p>
          <p class="mb-1 mentor-education-level">Education: ${mentor.educationLevel.title != "Other" ? mentor.educationLevel.title : mentor.educationLevelOther}</p>
        </div>
      </div>
    </main>
  </div>`;

const refillMentors = async () => {
  let response = await fetch("/refill-mentor");
  let mentors = await response.json();
  let hasActive = getActiveMentorCard() !== null;
  mentors.forEach((mentor, i) => {
    if (!mentorCardContainer.querySelector(`#mentor-${mentor.datastoreKey}-card`)) {
      let newCard = document.createElement("div");
      newCard.className = `carousel-item${i == 0 && !hasActive ? " active" : ""}`;
      newCard.innerHTML = newMentorCardHTML(mentor);
      mentorCardContainer.appendChild(newCard);
    }
  });
};
refillMentors();

const buttonDict = {
  "sendRequest": sendRequestButton,
  "dislikeMentor": dislikeMentorButton
}
const MIN_MENTOR_THRESHOLD = 5;
for (const buttonName in buttonDict) {
  buttonDict[buttonName].addEventListener("click", async (event) => {
    const activeMentorCard = getActiveMentorCard();
    const mentorID = activeMentorCard.querySelector(".mentor-id").innerText;
    let response = await fetch("/find-mentor", {
      method: "POST",
      body: new URLSearchParams({ mentorID, choice: buttonName })
    });
    let text = await response.text();
    let data = JSON.parse(text);
    if (data.success) {
      mentorCardContainer.removeChild(activeMentorCard);
      if (mentorCardContainer.childElementCount < MIN_MENTOR_THRESHOLD) {
        refillMentors();
      }
      getActiveMentorCard();
    }
  });
}
