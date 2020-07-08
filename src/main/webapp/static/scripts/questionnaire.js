
const form = document.getElementById('information-form');
form.addEventListener('submit', function() {
  getCheckboxValues('.ethnicityCheckbox', 'ethnicity');
  getCheckboxValues('.focusListCheckbox', 'focusList')
});

function getCheckboxValues(checkboxClass, valueLabel) {
  var checkboxes = document.querySelectorAll(checkboxClass + ':checked');
  if (checkboxes.length > 0) {
    for (var i = 0; i < checkboxes.length-1; i++) {
      document.getElementById(valueLabel).value += checkboxes[i].value + ', ';
    }
    document.getElementById(valueLabel).value += checkboxes[checkboxes.length-1].value;
  }
}

function checkForOther(val){
  if(val.toLowerCase()=='other') {
    document.getElementById('other-box').innerHTML = 'Other: <input type ="text" name="otherGender" />';
  } else {
    document.getElementById('other-box').innerHTML = '';
  }
}
