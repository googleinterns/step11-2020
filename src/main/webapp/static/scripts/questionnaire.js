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

function checkForOther(val, label){
  var otherID = 'other-input-' + label;
  if(val.toLowerCase()=='other') {
    document.getElementById(otherID).innerHTML = 'Other: <input type ="text" name="' + label + 'Other" id="' + label + 'Other"/>';
  } else {
    document.getElementById(otherID).innerHTML = '';
  }
}

function checklistCheckForOther(label) {
  var otherID = 'other-input-' + label;
  if (document.getElementById('ethnicity-OTHER').checked) {
    document.getElementById(otherID).innerHTML = 'Other: <input type ="text" name="' + label + 'Other" id="' + label + 'Other"/>';
  } else {
    document.getElementById(otherID).innerHTML = '';
  }
