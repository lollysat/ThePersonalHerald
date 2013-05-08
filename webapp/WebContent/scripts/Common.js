String.prototype.trim = function() { return this.replace(/^\s+|\s+$/, ''); };

function isNumeric(x)
{	
	return !isNaN(parseFloat(x)) && isFinite(x);
}
function hideErrorElements()
{
	if($('#clientSideErrors li').length==0)
		$('#clientSideErrors').hide();
	else
		$('#clientSideErrors').show();
	
	if($('#msgLabel').text()=='')
		$('#msgLabel').hide();
	else
		$('#msgLabel').show();
}

function clearAllErrors()
{
	$('#clientSideErrors li').remove();
	$('.errorMessage li').remove();
	$('#clientSideMessages li').remove();
	$('.actionMessage li').remove();
	
	$('#msgLabel').text('');
	$('#actionMessages').children().remove();
	$('#actionErrors').children().remove();
}

function clientSideErrorsFlagged()
{
	var errorFlagged = false;
	
	var noOfErrors = $('#clientSideErrors li'); 
	if(noOfErrors.length > 0)
		errorFlagged = true;
	
	
	return(errorFlagged);
}


function showClientErrors(errorMsgs)
{
	$('#clientSideErrors').append(errorMsgs);
	$('#clientSideErrors').show();
	
	resetFooter();
	$(window).scrollTop(0);

}


function showClientMessages(msg)
{
	$('#clientSideMessages').append(msg);
	$('#clientSideMessages').show();
	
	resetFooter();
	$(window).scrollTop(0);
}

function addClientError(errorMsg)
{
	showClientErrors("<li><span>" + errorMsg + "</span></li>");
}
function addClientMessage(clientMsg)
{
	showClientMessages("<li><span>" + clientMsg + "</span></li>");
}

function showJsonMessages(jsonData)
{
	if(jsonData.actionErrors != null && jsonData.actionErrors.length > 0) 
	{
		$.each(jsonData.actionErrors, function(i) 
		{
			addClientError(jsonData.actionErrors[i]);
		});
	}
	if(jsonData.actionMessages != null && jsonData.actionMessages.length > 0) 
	{
		$.each(jsonData.actionMessages, function(i) 
		{
			addClientMessage(jsonData.actionMessages[i]);
		});
	}
}

function isControlValid(control) {
    var validControl = false;
    if ((control != null) && (control != undefined)) {
	validControl = true;
    }

    return (validControl);
}

function isValueValid(val) {

    if ((val != null) && (val != undefined) && (val != '')) 
    	return true;

    return false;
}

function deleteRow(deleteButton) {
 try {
	
	var row = deleteButton.parentNode.parentNode;
	var rowToBeDeleted = row.rowIndex;
	
	var table = row.parentNode;
	if ((isControlValid(table)) && (isControlValid(row))) 
	{
		table.deleteRow(rowToBeDeleted);
	    resetFooter();
	}
 } catch (e) {
	alert(e);
 }
}

function isArray(obj) {
	//returns true is it is an array
	if ($.isArray(obj))
	return true;
	else
	return false;
}

function resetFooter()
{
	$("#wrapper").removeClass("wrapper");
	$("#wrapper").addClass("wrapper");
	 
   $("#footer").removeClass("footer");
   $("#footer").addClass("footer");
	
}

function showTableHeader(tableId)
{
	var selTH = "#" + tableId + ' tr:has(th):not(:has(td))';
	$(selTH).show();
}

function hideTableHeader(tableId)
{
	var selTH = "#" + tableId + ' tr:has(th):not(:has(td))';
	var selTR = "#" + tableId + ' tr:has(td)';
	
	if($(selTR).length == 0)
		$(selTH).hide();
}

function DisableFieldsForEdit(arrOfElementsToDisable)
{
	for(var i=0;i<arrOfElementsToDisable.length;i++)
	{
	  if(arrOfElementsToDisable[i]==null)
		  continue;
	  
	  arrOfElementsToDisable[i].readOnly= true;
	  arrOfElementsToDisable[i].style.color = "#646060";
	}	  
}

function hideAllButtons(formElement)
{
	var elem = formElement.elements;
    for(var i = 0; i < elem.length; i++)
    {
	    if(elem[i].type =="button" || elem[i].type=="submit")
		    elem[i].style.visibility = "hidden";
    }
}

String.prototype.ReplaceAll = function(stringToFind,stringToReplace){
    var temp = this;
    var index = temp.indexOf(stringToFind);
        while(index != -1){
            temp = temp.replace(stringToFind,stringToReplace);
            index = temp.indexOf(stringToFind);
        }
        return temp;
 }

function validateField(name) {

	var re = new RegExp("(^[A-Za-z][A-Za-z0-9_ -]*$)");
	
	return name.match(re) ? true : false;
	
 }

function setCellBackground(cell,color)
{
	cell.style.background = color;
	if(color=='#000000' || color=='Black')
		cell.style.color = '#FFFFFF';
	else
		cell.style.color = '#000000';
}

function setCellBackgroundJQ(cell,color)
{
	cell.css('background', color);
	if(color=='#000000' || color=='Black')
		cell.css('color','#FFFFFF');
	else
		cell.css('color','#000000');
}


function removeItem(array, item)
{
    for(var i=0;i<array.length;i++)
    {
        if(array[i]==item)
        {
            array.splice(i,1);
            return ;
        }
    }
}

function handleWrongContentType(jqXHR, textStatus, errorThrown)
{
	
	 var contentType = jqXHR.getResponseHeader("Content-Type");
	 if (jqXHR.status === 200 && contentType!=null && contentType.toLowerCase().indexOf("text/html") >= 0) 
	 {
		 window.location.reload();
		 return;
	 }
	 addClientError("Error occurred  while retrieving data: " + errorThrown.message);
}

function expandDesc(elementId,problemDesc)
{
	
	var tagSelector = "#" + elementId;
 	$(tagSelector).text(problemDesc);
	 
 	$(tagSelector).addClass("fixedHeightDiv");	 
	
}

function collapseDesc(elementId,problemDesc)
{
	var tagSelector = "#" + elementId;
	$(tagSelector).removeClass("fixedHeightDiv");
 	if(problemDesc.length > 58)
 	{ 		 
		 $(tagSelector).text(problemDesc.substring(0,50) + "... ");		 
		 $(tagSelector).append("<a href=\"javascript:expandDesc('" + elementId + "','" + problemDesc + "')\">more</a>");
	 
 	}
}

function disableDefaultOption(dropdownObjName)
{	
 	$("select[name=" + dropdownObjName + "]").each(function(){ 
 		$(this).find('option:eq(0)').attr("disabled", "disabled");
 	});
}

function createToolTip(title)
{
	return $('<div>',{title: labelCurrentStateTitle}).addClass("tooltip"); 	
	//$('<div class="tooltip" title="title here" />"></div>&nbsp;<span class='TextColorRed'>*</span>
}
function createRequired()
{
	return $('<span>*</span>').addClass("TextColorRed");
}
function checkURL(value) {
    var urlregex = new RegExp("^(http:\/\/|https:\/\/|ftp:\/\/|www.){1}([0-9A-Za-z]+\.)");
    if (urlregex.test(value)) {
        return (true);
    }
    return (false);
}

function getCategoryIcon(category)
{
	category = category.toLowerCase();
	
	categoryIcon = "";
	
	if(category=='sport')
		categoryIcon = "sports.png";
	else if(category=='business')
		categoryIcon = "business.png";
	else if(category=='entertainment')
		categoryIcon = "Entertainment.png";
	else if(category=='us')
		categoryIcon = "US.png";
	else if(category=='world')
		categoryIcon = "world.png";
	else if(category=='health')
		categoryIcon = "health.jpg";
	else if(category=='sci_tech')
		categoryIcon = "tech.jpg";
	
	return categoryIcon;
}
