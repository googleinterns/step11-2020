
const form = document.getElementById('form');
form.addEventListener('submit', getCheckboxValues);

function getCheckboxValues() {

  document.getElementById('ethnicity').value = document.querySelector('.ethnicityCheckbox').checked;
  document.getElementById('focusList').value = document.querySelector('.focusListCheckbox').checked;
}

function checkForOther(val){
  if(val.toLowerCase()=='other') {
    document.getElementById('other-box').innerHTML = 'Other: <input type ="text" name="otherGender" />';
  } else {
    document.getElementById('otherGender').innerHTML = '';
  }
}
