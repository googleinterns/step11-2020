$(function() {
  var selectedCountry = "Mexico"
  if(selectedCountry != "") {
    $("#country").val(selectedCountry).change();
    $("#country").remove($("#country-select"));
  }
}

function checkGender(val){
  if(val=='other') {
    document.getElementById('other-box').innerHTML = 'Other: <input type ="text" name="other" />';
  } else {
    document.getElementById('other-box').innerHTML = '';
  }
}
