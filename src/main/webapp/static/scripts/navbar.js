const topNav = document.getElementById("top-nav");

const responsiveNav = () => {
  if (topNav.classList.contains("responsive")){
    topNav.classList.remove("responsive");
  } else {
    topNav.classList.add("responsive");
  }
}

const navToSection = (elementSelector) => {
  jumpToSection(elementSelector);
  topNav.classList.remove("responsive");
}

const goToPageSection = (pagePath, sectionID) => {
  window.location = pagePath + "#"+sectionID;
}

const windowResizeHandlerNavBar = () => {
  if (window.innerWidth > 700) {
    topNav.classList.remove("responsive");
  }
}

window.addEventListener("resize", windowResizeHandlerNavBar);

const navBarHeight = document.getElementById("top-nav").computedStyleMap().get("min-height")["value"];
const jumpToSection = (elementSelector) => {
  element = document.querySelector(elementSelector);
  const elemRect = element.getBoundingClientRect();
  scrollBy(0,elemRect.top-navBarHeight);
}

const docClickHandlerNavBar = (event) => {
  if (!topNav.contains(event.target)) {
    topNav.classList.remove("responsive");
  }
};
document.addEventListener("click", docClickHandlerNavBar);
