
const form = document.getElementById('information-form');
form.addEventListener('submit', function() {
  getCheckboxValues('.focusListCheckbox', 'focusList');
  getCheckboxValues('.ethnicityCheckbox', 'ethnicity');
});

function getCheckboxValues(checkboxClass, valueLabel) {
  let checkboxes = document.querySelectorAll(checkboxClass + ':checked');
  if (checkboxes.length > 0) {
    for (let i = 0; i < checkboxes.length-1; i++) {
      document.getElementById(valueLabel).value += checkboxes[i].value + ', ';
    }
    document.getElementById(valueLabel).value += checkboxes[checkboxes.length-1].value;
  }
}

function checkForOther(val, label){
  var otherID = 'other-input-' + label;
  if(val.toLowerCase()=='other') {
    document.getElementById(otherID).innerHTML = 'Other: <input type ="text" name="' + label + 'Other" id="' + label + 'Other"/>';
  } else {
    document.getElementById(otherID).innerHTML = '';
  }
}
