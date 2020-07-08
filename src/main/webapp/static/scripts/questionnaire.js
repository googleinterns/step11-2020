
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
    console.log(document.getElementById(valueLabel).value);
  }
}

function checkForOther(val){
  if(val.toLowerCase()=='other') {
    document.getElementById('other-box').innerHTML = 'Other: <input type ="text" name="genderOther" />';
  } else {
    document.getElementById('other-box').innerHTML = '';
  }
}
