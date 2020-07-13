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
