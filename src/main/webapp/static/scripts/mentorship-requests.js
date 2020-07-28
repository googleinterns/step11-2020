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

const menteeCardContainer = document.getElementById("mentee-card-container");

const onSuccess = (menteeCard) => {
  menteeCardContainer.removeChild(menteeCard);
  if (menteeCardContainer.children.length === 0) {
    window.location = "/dashboard";
  }
};

document.querySelectorAll(".mentee-card").forEach(menteeCard => {
  const acceptButton = menteeCard.querySelector("button#yes");
  const denyButton = menteeCard.querySelector("button#no");
  acceptButton.addEventListener("click", async (event) => {
    const requestID = menteeCard.querySelector(".request-id").innerText;
    let success = await sendRequestDecision(requestID, "accept");
    if (success) {
      onSuccess(menteeCard);
    }
  });
  denyButton.addEventListener("click", async (event) => {
    const requestID = menteeCard.querySelector(".request-id").innerText;
    let success = await sendRequestDecision(requestID, "deny");
    if (success) {
      onSuccess(menteeCard);
    }
  });
});

const sendRequestDecision = async (requestID, choice) => {
  let response = await fetch("/mentorship-requests", {
      method: "POST",
      body: new URLSearchParams({requestID, choice})
    });
  let data = await response.json();
  return data.success;
}
