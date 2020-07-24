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

var errorMessage = '';
const form = document.getElementById('information-form');

function checkForOther(val, label){
  let otherID = 'other-input-' + label;
  let otherContainer = document.getElementById(otherID);
  if(val.toLowerCase()=='other') {
    otherContainer.innerHTML = 'Other: <input type ="text" name="' + label + 'Other" id="' + label + 'Other"/>';
  } else {
    otherContainer.innerHTML = '';
  }
}

function checklistCheckForOther(label) {
  let otherID = 'other-input-' + label;
  let otherContainer = document.getElementById(otherID);
  if (document.getElementById('ethnicity-OTHER').checked) {
    otherContainer.innerHTML = 'Other: <input type ="text" name="' + label + 'Other" id="' + label + 'Other"/>';
  } else {
    otherContainer.innerHTML = '';
  }
}

function checkForm() {
  validateDate(document.getElementById('dateOfBirth').value);
  if (errorMessage == '') {
    return true;
  } else {
    return false;
  }
}

function validateDate(date){
  let currentDay = moment();
  let birthday = moment(date);
  let dateError = document.getElementById('date-of-birth-error-container');
  if (!birthday.isValid()) {
    dateError.innerHTML =
    '<label id="date-of-birth-error-label" for="dateOfBirth">Invalid date format: Should be MM/DD/YYYY</label>';
    errorMessage = 'invalid date';
    document.getElementById('step1').scrollIntoView();
    return false;
  } else if (currentDay.isBefore(birthday)) {
    dateError.innerHTML = '<label id="date-of-birth-error-label" for="dateOfBirth">You can\'t be born in the future!</label>';
    errorMessage = 'invalid date';
    document.getElementById('step1').scrollIntoView();
    return false;
  } else {
    errorMessage = '';
    dateError.innerHTML = '';
    return true;
  }
}
