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
  `<div class="mentor-profile-card shadow-sm mx-1 mb-1 position-relative overflow-auto" id="mentor-${mentor.datastoreKey}-card" style="max-height: calc(95vh - 15rem);">
    <a class="stretched-link d-none" href="/profile?userID=${mentor.userID}"></a>
    <p class="mentor-id d-none" hidden>${mentor.datastoreKey}</p>
    <main class="profile container divider-top position-relative p-3" id="${mentor.userID}-profile" style="width: fit-content;">
      <div class="row mb-2 mb-md-4">
        <div class="col-12 col-md-6 d-flex flex-column align-items-center align-items-md-end">
          <div class="profile-image-container col-12 p-0 position-relative">
            <img class="img-thumbnail rounded-circle" id="profile-image" src="${ mentor.profilePicBlobKey ? ("/images?blob-key=" + mentor.profilePicBlobKey) : "/static/images/defaultProfilePicture.png"}" style="width: 20em">
          </div>
        </div>
        <div class="col-12 col-md-6 d-flex flex-column justify-content-between" id="profile-title">
          <span></span>
          <div class="mb-md-5">
            <h1 class="mb-0">${mentor.name}</h1>
            <h4 class="mb-0">${mentor.userType.title}</h4>
            <!-- <h6 class="mb-1">${!mentor.visibility ? "Not a" : "A"}vailable for mentorship</h6> -->
          </div>
        </div>
      </div>
      <div class="row mb-4 mb-md-2">
        <div class="col-12 col-md-6">
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-country">Country:</p>
            <p class="mb-1 col-8 text-left user-country">${mentor.country.longName}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-language">Language:</p>
            <p class="mb-1 col-8 text-left user-language">${mentor.language.longName}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-timezone">Timezone:</p>
            <p class="mb-1 col-8 text-left user-timezone">${mentor.timezone.name}: GMT ${(mentor.timezone.offset >= 0 ? "+" : "") + mentor.timezone.offset}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-ethnicity">Ethnicity:</p>
            <p class="mb-1 col-8 text-left user-ethnicity">${mentor.ethnicityList.map(ethnicity => ethnicity.title !== "Other" ? ethnicity.title : (mentor.ethnicityOther || "Other")).join(", ")}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-gender">Gender:</p>
            <p class="mb-1 col-8 text-left user-gender">${mentor.gender.title !== "Other" ? mentor.gender.title : mentor.genderOther}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-first-gen">First-gen:</p>
            <p class="mb-1 col-8 text-left user-first-gen">${mentor.firstGen  ? "Yes" : "No"}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-low-income">Low income:</p>
            <p class="mb-1 col-8 text-left user-low-income">${mentor.lowIncome  ? "Yes" : "No"}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-education-level">Education:</p>
            <p class="mb-1 col-8 text-left user-education-level">${mentor.educationLevel.title != "Other" ? mentor.educationLevel.title : mentor.educationLevelOther}</p>
          </div>
        </div>
        <div class="col-12 col-md-6 order-first order-md-last">
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-mentor-type">Mentor type:</p>
            <p class="mb-1 col-8 text-left user-mentor-type">${mentor.mentorType.title}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-focus">${mentor.focusList.length === 0 ? "Focuses:" : mentor.focusList.length === 1 ? `Focus:` : "Focuses:"}</p>
            <p class="mb-1 col-8 text-left user-focus">${mentor.focusList.length === 0 ? "None" : mentor.focusList.map(focus => focus.title).join(", ")}</p>
          </div>
          <div class="row no-gutters">
            <p class="mb-1 col-4 user-age">Age:</p>
            <p class="mb-1 col-8 text-left user-age">${mentor.age}</p>
          </div>
        </div>
      </div>
      <div class="row no-gutters">
        <div class="col-12">
          <p class="user-description text-break">${mentor.description}</p>
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
  updateButtonContainer();
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
      updateButtonContainer();
    }
  });
}

const RATING_BUTTONS = document.getElementById("mentor-rating-buttons");
const NO_MENTOR_WARNING = document.getElementById("no-mentors-warning");
const updateButtonContainer = () => {
  if (mentorCardContainer.children.length == 0) {
    RATING_BUTTONS.classList.remove("d-flex");
    RATING_BUTTONS.classList.add("d-none");
    NO_MENTOR_WARNING.classList.remove("d-none");
  } else {
    RATING_BUTTONS.classList.remove("d-none");
    RATING_BUTTONS.classList.add("d-flex");
    NO_MENTOR_WARNING.classList.add("d-none");
  }
}
