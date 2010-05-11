/** javascript to add or remove div. 
 * @author kumaramit01,gzhu1
 * 
 */

var count=1;
function add(parentDiv,templateDiv){
var clone = $(templateDiv.cloneNode(true));
count++;
clone.id=templateDiv.id+count;
//$(clone).down("input").value="";
$(templateDiv).insert({after: clone}); 
clone.show();
}
function removeNode(node,first){
	var parent =  $(node).parentNode;
	if($(parent).id==first){
	//parent.hide();//alert("Cannot delete the element");
	}else{
		$(parent).remove();
	}
}