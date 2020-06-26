function checkForOther(val){
  if(val.toLowerCase()=='other') {
    document.getElementById('other-box').innerHTML = 'Other: <input type ="text" name="other" />';
  } else {
    document.getElementById('other-box').innerHTML = '';
  }
}
