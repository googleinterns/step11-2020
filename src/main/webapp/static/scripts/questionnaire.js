// Copyright 2019 Google LLC
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

$(window).on('load', function() {
  checkForOther($('#gender')[0].value, 'gender');
  checkForOther($('#educationLevel')[0].value, 'educationLevel');
  checklistCheckForOther('ethnicity');
});

function checkForOther(val, label){
  let otherID = '#'+label+'Other';
  let otherContainer = $(otherID)[0];
  if(val.toLowerCase()=='other') {
    otherContainer.hidden = false;
  } else {
    otherContainer.hidden = true;
  }
}

function checklistCheckForOther(label) {
  let otherID = '#'+label+'Other';
  let otherContainer = $(otherID)[0];
  if ($('#' + label + '-OTHER')[0].checked) {
    otherContainer.hidden = false;
  } else {
    otherContainer.hidden = true;
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
    return false;
  } else if (currentDay.isBefore(birthday)) {
    dateError.innerHTML = '<label id="date-of-birth-error-label" for="dateOfBirth">You can\'t be born in the future!</label>';
    errorMessage = 'invalid date';
    return false;
  } else {
    errorMessage = '';
    dateError.innerHTML = '';
    return true;
  }
}
