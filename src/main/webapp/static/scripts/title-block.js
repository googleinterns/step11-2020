const titleBlock = document.querySelector(".title-block");
const titleBlockInner = document.querySelector(".title-block-inner");
const titleImage = document.querySelector("#title-image");
const mainContent = document.querySelector(".main-content");
const setTitleImageWidth = () => {
  if (titleBlock && titleImage) {
    let imgWidth = titleImage.naturalWidth*500/titleImage.naturalHeight;
    let pageWidth = parseInt(window.getComputedStyle(titleBlock).width);
    titleBlockInner.style.width = Math.min(imgWidth,pageWidth)+"px";
    mainContent.style.maxWidth = imgWidth+"px";
  }
}
const windowResizeHandlerTitleBlock = () => {
  setTitleImageWidth();
}
window.addEventListener("resize", windowResizeHandlerTitleBlock);

const domContentLoadedHandlerTitleBlock = () => {
  setTitleImageWidth();
}
document.addEventListener("DOMContentLoaded", domContentLoadedHandlerTitleBlock);
