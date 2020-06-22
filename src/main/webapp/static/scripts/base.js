let authButtons = document.querySelectorAll(".authentication-button");
if (authButtons && authButtons.length > 0) {
  (async () => {
    let response = await fetch(`/auth-url?entryURLPath=${window.location.pathname}`);
    let text = await response.text();
    console.log(text);
    let data = JSON.parse(text);
    authButtons.forEach((authButton) => {
      authButton.innerText = data.type;
      authButton.onclick = (event) => {
        window.location = data.url;
      };
    });
  })();

}
