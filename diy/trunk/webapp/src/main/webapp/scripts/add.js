/** javascript to add or remove div. 
 * @author kumaramit01,gzhu1
 * 
 */

var count=1;
function add(parentDiv,templateDiv,clearCount){
var clone = $(templateDiv.cloneNode(true));
count++;
clone.id=templateDiv.id+count;
for (i=0;i<clearCount;i++){
	$(clone).down("input",i).value="";
}
$(clone).down("input",0).value="";
$(templateDiv).insert({after: clone}); 
clone.show();
}
function addSelect(parentDiv,templateDiv){
	var clone = $(templateDiv.cloneNode(true));
	count++;
	clone.id=templateDiv.id+count;

	$(clone).down("select").value="";
	
	$(templateDiv).insert({after: clone}); 
	clone.show();
	}
function removeNode(node,first){
	var parent =  $(node).parentNode;
	if($(parent).id==first){
	//parent.hide();
		alert("Cannot delete the element");
	}else{
		$(parent).remove();
	}
}


//from phpMyAdmin
function insertAtCursor(myField, prefix,myValueField,orderField) {
	 myValue=myValueField.options[myValueField.selectedIndex].value;
	 orderValue=orderField.options[orderField.selectedIndex].value;
	 if (myValue!="") {
		 myValue="'"+prefix+orderValue+"{"+myValue+"}'";
	 }
	 insertStrAtCursor(myField,myValue);
	};
	
	
function insertStrAtCursor(myField,myValue) {
		 
		//IE support
		if (document.selection) {
		myField.focus();
		sel = document.selection.createRange();
		sel.text = myValue;
		}
		//MOZILLA/NETSCAPE support
		else if (myField.selectionStart || myField.selectionStart == '0') {
		var startPos = myField.selectionStart;
		var endPos = myField.selectionEnd;
		myField.value = myField.value.substring(0, startPos)
		+ myValue
		+ myField.value.substring(endPos, myField.value.length);
		} else {
		myField.value += myValue;
		}
		myField.focus();
		};