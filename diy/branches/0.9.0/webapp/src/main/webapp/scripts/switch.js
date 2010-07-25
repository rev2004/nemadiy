function switchToLong(index) {
	var longNote = dojo.byId("longNote" + index);
	var shortNote = dojo.byId("shortNote" + index);
	longNote.show();
	shortNote.hide();
}
function switchToShort(index) {
	var longNote = dojo.byId("longNote" + index);
	var shortNote = dojo.byId("shortNote" + index);
	longNote.hide();
	shortNote.show();
}


function swtich(toBeHide,toBeShow){
	var nodeToHide = dojo.byId(toBeHide);
	var nodeToShow = dojo.byId(toBeShow);
	nodeToHide.style.display="none";
	nodeToShow.style.display="block";
}